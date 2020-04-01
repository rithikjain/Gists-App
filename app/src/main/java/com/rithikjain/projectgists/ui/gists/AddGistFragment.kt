package com.rithikjain.projectgists.ui.gists

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController

import com.rithikjain.projectgists.R
import com.rithikjain.projectgists.util.shortToast
import kotlinx.android.synthetic.main.fragment_add_gist.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

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

        val gistsViewModel by sharedViewModel<GistsViewModel>()

        requireActivity().window.setBackgroundDrawableResource(R.drawable.bg)

        fileCodeText.editText!!.setText(gistsViewModel.fileCode)
        fileNameText.editText!!.setText(gistsViewModel.fileName)
        fileDescriptionText.editText!!.setText(gistsViewModel.fileDescription)

        addGistToolbar.title = "Add A Gist"
        addGistToolbar.navigationIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_back)
        addGistToolbar.setNavigationOnClickListener {
            gistsViewModel.fileName = ""
            gistsViewModel.fileCode = ""
            gistsViewModel.fileDescription = ""
            findNavController().navigateUp()
        }

        addGistToolbar.inflateMenu(R.menu.add_gist_toolbar_menu)
        addGistToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.previewCode -> {
                    if (validateFields(requireContext())) {
                        gistsViewModel.fileName = fileNameText.editText!!.text.toString()
                        gistsViewModel.fileCode = fileCodeText.editText!!.text.toString()
                        gistsViewModel.fileDescription =
                            fileDescriptionText.editText!!.text.toString()
                        val action = AddGistFragmentDirections.actionAddGistFragmentToCodeFragment(
                            fileCodeText.editText!!.text.toString(),
                            fileNameText.editText!!.text.toString()
                        )
                        findNavController().navigate(action)
                    }
                }
                R.id.saveCode -> {
                    requireContext().shortToast("Save")
                }
            }
            return@setOnMenuItemClickListener false
        }
    }

    private fun validateFields(context: Context): Boolean {
        if (fileNameText.editText!!.text.isBlank()) {
            context.shortToast("Filename cannot be empty")
            return false
        }
        if (fileCodeText.editText!!.text.isBlank()) {
            context.shortToast("Code cannot be empty")
            return false
        }
        return true
    }
}
