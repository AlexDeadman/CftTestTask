package ru.alexdeadman.cfttesttask.ui.binmetadata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException
import ru.alexdeadman.cfttesttask.R
import ru.alexdeadman.cfttesttask.collectOnLifecycle
import ru.alexdeadman.cfttesttask.databinding.FragmentFirstBinding
import ru.alexdeadman.cfttesttask.toYesOrNo
import ru.alexdeadman.cfttesttask.uppercaseFirstChar

@AndroidEntryPoint
class BinMetadataFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            val binMetadataViewModel: BinMetadataViewModel by viewModels()

            binMetadataViewModel.binMetadataStateFlow
                .collectOnLifecycle(viewLifecycleOwner) { state ->
                    when (state) {
                        is BinMetadataState.Default -> {}
                        is BinMetadataState.Loading -> {
                            progressBar.visibility = View.VISIBLE
                        }
                        is BinMetadataState.Loaded -> {
                            state.result.let {
                                listOf(
                                    textViewScheme to it.scheme?.uppercaseFirstChar(),
                                    textViewBrand to it.brand,
                                    textViewType to it.type?.uppercaseFirstChar(),
                                    textViewPrepaid to it.prepaid?.toYesOrNo(),
                                    textViewLength to it.number?.length,
                                    textViewLuhn to it.number?.luhn?.toYesOrNo(),
                                    textViewCountryName to it.country?.emoji,
                                    textViewCountryFlag to it.country?.name,
                                    textViewLatitude to it.country?.latitude,
                                    textViewLongitude to it.country?.longitude,
                                    textViewBankName to it.bank?.name,
                                    textViewBankCity to it.bank?.city,
                                    textViewBankUrl to it.bank?.name,
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

            tiEditText.setOnEditorActionListener { _, _, _ ->
                val bin = tiEditText.text
                if (!bin.isNullOrBlank() && """\d{4,}""".toRegex().matches(bin)) {
                    binMetadataViewModel.fetchBinMetadata(bin.toString())
                    tiLayout.error = null
                } else {
                    tiLayout.error = getString(R.string.incorrect_input)
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}