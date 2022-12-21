package ru.alexdeadman.cfttesttask.ui.binmetadata

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.alexdeadman.cfttesttask.collectOnLifecycle
import ru.alexdeadman.cfttesttask.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
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

        val binMetadataViewModel: BinMetadataViewModel by viewModels()

        binMetadataViewModel.binMetadataStateFlow
            .collectOnLifecycle(viewLifecycleOwner) { state ->
                when (state) {
                    is BinMetadataState.Default -> {}
                    is BinMetadataState.Loading -> { /*TODO*/ }
                    is BinMetadataState.Loaded -> {
                        binding.textviewFirst.text = state.result.toString()
                    }
                    is BinMetadataState.Error -> { /*TODO*/ }
                }
            }

        binding.buttonFirst.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            binMetadataViewModel.fetchBinMetadata("45717360")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}