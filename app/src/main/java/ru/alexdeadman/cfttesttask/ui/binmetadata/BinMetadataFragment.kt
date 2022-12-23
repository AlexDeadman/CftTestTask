package ru.alexdeadman.cfttesttask.ui.binmetadata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException
import ru.alexdeadman.cfttesttask.R
import ru.alexdeadman.cfttesttask.collectOnLifecycle
import ru.alexdeadman.cfttesttask.databinding.FragmentBinmetadataBinding
import ru.alexdeadman.cfttesttask.toYesOrNo
import ru.alexdeadman.cfttesttask.uppercaseFirstChar

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
                .collectOnLifecycle(viewLifecycleOwner) { state ->
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
                .collectOnLifecycle(viewLifecycleOwner) { state ->
                    when (state) {
                        BinMetadataState.Default -> {}
                        BinMetadataState.Loading -> {
                            textView.visibility = View.GONE
                            progressBar.visibility = View.VISIBLE
                        }
                        is BinMetadataState.Loaded -> {
                            state.result.let {
                                listOf(
                                    textViewScheme to it.scheme?.uppercaseFirstChar(),
                                    textViewBrand to it.brand,
                                    textViewType to it.type?.uppercaseFirstChar(),
                                    textViewPrepaid to it.prepaid?.toYesOrNo(),
                                    textViewNumberLength to it.number?.length,
                                    textViewNumberLuhn to it.number?.luhn?.toYesOrNo(),
                                    textViewCountryName to it.country?.name,
                                    textViewCountryFlag to it.country?.emoji,
                                    textViewCountryLatitude to it.country?.latitude,
                                    textViewCountryLongitude to it.country?.longitude,
                                    textViewBankName to it.bank?.name,
                                    textViewBankCity to it.bank?.city,
                                    textViewBankUrl to it.bank?.url,
                                    textViewBankPhone to it.bank?.phone,
                                ).forEach { pair ->
                                    pair.first.text = pair.second?.toString() ?: "—"
                                }
                            }
                            progressBar.visibility = View.GONE
                            scrollView.visibility = View.VISIBLE
                        }
                        is BinMetadataState.Error -> {
                            progressBar.visibility = View.GONE

                            val messageId =
                                if (
                                    state.throwable is HttpException &&
                                    state.throwable.code() == 404
                                ) {
                                    R.string.not_found
                                } else {
                                    R.string.unknown_error
                                }

                            Toast.makeText(
                                requireContext(),
                                messageId,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}