package com.rithikjain.projectgists.ui.gists

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController

import com.rithikjain.projectgists.R
import kotlinx.android.synthetic.main.fragment_add_gist.*

class AddGistFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_gist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().window.setBackgroundDrawableResource(R.drawable.bg)

        addGistToolbar.title = "Add A Gist"
        addGistToolbar.navigationIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_back)
        addGistToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}
