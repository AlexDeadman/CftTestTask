package ru.alexdeadman.cfttesttask.ui.binmetadata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.TooltipCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException
import ru.alexdeadman.cfttesttask.*
import ru.alexdeadman.cfttesttask.databinding.FragmentBinmetadataBinding
import java.net.HttpURLConnection

@AndroidEntryPoint
class BinMetadataFragment : Fragment() {

    private var _binding: FragmentBinmetadataBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBinmetadataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            val binMetadataViewModel: BinMetadataViewModel by viewModels()

            acTextView.apply {
                setOnEditorActionListener { _, _, _ ->
                    if (!text.isNullOrBlank() && """\d{4,}""".toRegex().matches(text)) {
                        binMetadataViewModel.fetchBinMetadata(text.toString())
                        clearFocus()
                        tiLayout.error = null
                        return@setOnEditorActionListener false
                    } else {
                        tiLayout.error = getString(R.string.incorrect_input)
                        return@setOnEditorActionListener true
                    }
                }
                setOnFocusChangeListener { v, hasFocus ->
                    v.post { // bypassing WindowManager$BadTokenException
                        if (hasFocus) showDropDown()
                    }
                }
                setOnItemClickListener { _, _, _, _ ->
                    onEditorAction(EditorInfo.IME_ACTION_DONE)
                }
            }

            binMetadataViewModel.historyStateFlow
                .collectOnLifecycle(
                    viewLifecycleOwner,
                    Lifecycle.State.STARTED
                ) { state ->
                    when (state) {
                        HistoryState.Default -> {}
                        is HistoryState.Loaded -> {
                            acTextView.setAdapter(
                                ArrayAdapter(
                                    requireContext(),
                                    android.R.layout.simple_spinner_dropdown_item,
                                    state.result
                                )
                            )
                        }
                        is HistoryState.Error -> {
                            Toast.makeText(
                                requireContext(),
                                R.string.cant_load_history,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

            binMetadataViewModel.binMetadataStateFlow
                .collectOnLifecycle(
                    viewLifecycleOwner,
                    Lifecycle.State.STARTED
                ) { state ->
                    when (state) {
                        BinMetadataState.Default -> {}
                        BinMetadataState.Loading -> {
                            textViewMessage.visibility = View.GONE
                            progressBar.visibility = View.VISIBLE
                        }
                        is BinMetadataState.Loaded -> {
                            state.result.let { bmt ->
                                listOf(
                                    textViewScheme to bmt.scheme?.uppercaseFirstChar(),
                                    textViewBrand to bmt.brand,
                                    textViewType to bmt.type?.uppercaseFirstChar(),
                                    textViewPrepaid to bmt.prepaid?.toYesOrNo(),
                                    textViewNumberLength to bmt.number?.length?.toString(),
                                    textViewNumberLuhn to bmt.number?.luhn?.toYesOrNo(),
                                    textViewCountryName to bmt.country?.name,
                                    textViewCountryFlag to bmt.country?.emoji,
                                    textViewCountryLatitude to bmt.country?.latitude?.toString(),
                                    textViewCountryLongitude to bmt.country?.longitude?.toString(),
                                    textViewBankName to bmt.bank?.name,
                                    textViewBankCity to bmt.bank?.city,
                                    textViewBankUrl to bmt.bank?.url,
                                    textViewBankPhone to bmt.bank?.phone,
                                ).forEach {
                                    it.first.text = it.second ?: "—"
                                }

                                listOf(
                                    textViewCountryName to bmt.country?.name,
                                    textViewBankCity to bmt.bank?.city,
                                ).forEach { pair ->
                                    pair.first.apply {
                                        setOnClickListener { }
                                        pair.second?.let { toGeoLink(requireContext(), it) }
                                    }
                                }
                            }

                            textViewMessage.visibility = View.GONE
                            progressBar.visibility = View.GONE
                            scrollView.visibility = View.VISIBLE
                        }
                        is BinMetadataState.Error -> {
                            progressBar.visibility = View.GONE

                            val messageId =
                                when (state.throwable) {
                                    is HttpException -> {
                                        when (state.throwable.code()) {
                                            HttpURLConnection.HTTP_NOT_FOUND -> R.string.not_found
                                            else -> R.string.unknown_error
                                        }
                                    }
                                    else -> R.string.unknown_error
                                }

                            Toast.makeText(
                                requireContext(),
                                messageId,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

            listOf(
                textViewMessage to R.string.bin_desc,
                textViewNumberLuhnTitle to R.string.luhn_desc,
                textViewPrepaidTitle to R.string.prepaid_desc,
            ).forEach {
                TooltipCompat.setTooltipText(it.first, getString(it.second))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}