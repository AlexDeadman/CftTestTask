package ru.alexdeadman.cfttesttask.ui.binmetadata

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import androidx.appcompat.widget.TooltipCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException
import ru.alexdeadman.cfttesttask.*
import ru.alexdeadman.cfttesttask.databinding.FragmentBinmetadataBinding
import ru.alexdeadman.cfttesttask.ui.binmetadata.states.BinMetadataState
import ru.alexdeadman.cfttesttask.ui.binmetadata.states.HistoryState
import java.net.HttpURLConnection

@AndroidEntryPoint
class BinMetadataFragment : Fragment() {

    private var _binding: FragmentBinmetadataBinding? = null
    private val binding get() = _binding!!

    private val binMetadataViewModel: BinMetadataViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBinmetadataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMenu()

        collectHistoryState()
        collectBinMetadataState()

        binding.apply {
            autoCompleteTextViewBin.apply {
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

            listOf(
                textViewMessage to R.string.bin_desc,
                textViewNumberLuhnTitle to R.string.luhn_desc,
                textViewPrepaidTitle to R.string.prepaid_desc,
            ).forEach {
                TooltipCompat.setTooltipText(it.first, getString(it.second))
            }
        }
    }

    private fun setupMenu() {
        requireActivity().addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_binmetadata, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                    when (menuItem.itemId) {
                        R.id.action_clear_history -> {
                            binMetadataViewModel.clearHistory()
                            true
                        }
                        R.id.action_about -> {
                            findNavController().navigate(
                                R.id.action_BinmetadataFragment_to_AboutFragment
                            )
                            true
                        }
                        else -> false
                    }
            },
            viewLifecycleOwner
        )
    }

    private fun collectHistoryState() {
        binding.apply {
            binMetadataViewModel.historyStateFlow
                .collectOnLifecycle(
                    viewLifecycleOwner,
                    Lifecycle.State.STARTED
                ) { state ->
                    when (state) {
                        HistoryState.Default -> {}
                        is HistoryState.Loaded -> {
                            autoCompleteTextViewBin.setAdapter(
                                ArrayAdapter(
                                    requireContext(),
                                    android.R.layout.simple_spinner_dropdown_item,
                                    state.result
                                )
                            )
                        }
                        is HistoryState.Cleared -> {
                            showToast(R.string.history_cleared)
                        }
                        is HistoryState.Error -> {
                            showToast(R.string.history_operation_failed)
                        }
                    }
                }
        }
    }

    private fun collectBinMetadataState() {
        binding.apply {
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

                            showToast(messageId)
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