package com.rithikjain.projectgists.ui.gists

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.ybq.android.spinkit.style.WanderingCubes

import com.rithikjain.projectgists.R
import com.rithikjain.projectgists.model.Result
import com.rithikjain.projectgists.model.gists.CreateGistRequest
import com.rithikjain.projectgists.model.gists.UpdateGistRequest
import com.rithikjain.projectgists.util.hide
import com.rithikjain.projectgists.util.shortToast
import com.rithikjain.projectgists.util.show
import kotlinx.android.synthetic.main.fragment_add_gist.*
import kotlinx.android.synthetic.main.fragment_edit_gist.*
import org.koin.android.viewmodel.ext.android.sharedViewModel


class EditGistFragment : Fragment() {

    private val args by navArgs<EditGistFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_gist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gistsViewModel by sharedViewModel<GistsViewModel>()

        requireActivity().window.setBackgroundDrawableResource(R.drawable.bg)

        editGistProgress.setIndeterminateDrawable(WanderingCubes())
        editGistProgress.hide()

        editGistToolbar.title = "Edit Gist"
        editGistToolbar.navigationIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_back)
        editGistToolbar.setNavigationOnClickListener {
            val action = EditGistFragmentDirections.actionEditGistFragmentToGistsFragment()
            findNavController().navigate(action)
        }

        val gistID = args.gistid
        val filename = args.filename
        val content = args.code
        val description = args.description

        editFileNameText.editText!!.setText(filename)
        editFileDescriptionText.editText!!.setText(description)
        editFileCodeText.editText!!.setText(content)

        editGistToolbar.inflateMenu(R.menu.edit_gist_toolbar_menu)
        editGistToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.editPreviewCode -> {
                    if (validateFields(requireContext())) {
                        val action =
                            EditGistFragmentDirections.actionEditGistFragmentToCodeFragment(
                                editFileCodeText.editText!!.text.toString(),
                                editFileNameText.editText!!.text.toString(),
                                false
                            )
                        findNavController().navigate(action)
                    }
                }
                R.id.updateCode -> {
                    if (validateFields(requireContext())) {
                        val updateGistRequest = UpdateGistRequest(
                            gistID,
                            editFileDescriptionText.editText!!.text.toString(),
                            editFileNameText.editText!!.text.toString(),
                            editFileCodeText.editText!!.text.toString()
                        )

                        gistsViewModel.updateGist(updateGistRequest)
                            .observe(viewLifecycleOwner, Observer { res ->
                                when (res.status) {
                                    Result.Status.LOADING -> {
                                        editDetailsView.hide()
                                        editGistProgress.show()
                                    }
                                    Result.Status.SUCCESS -> {
                                        if (res.data!!.Status == 200) {
                                            editGistProgress.hide()
                                            val action =
                                                EditGistFragmentDirections.actionEditGistFragmentToGistsFragment()
                                            findNavController().navigate(action)
                                            requireContext().shortToast("Gist Updated")
                                        } else {
                                            requireContext().shortToast("Hmm something went wrong!")
                                        }
                                    }
                                    Result.Status.ERROR -> {
                                        editDetailsView.show()
                                        editGistProgress.hide()
                                        requireContext().shortToast("Error in creating gist!")
                                    }
                                }
                            })
                    }
                }
            }
            return@setOnMenuItemClickListener false
        }
    }

    private fun validateFields(context: Context): Boolean {
        if (editFileNameText.editText!!.text.isBlank()) {
            context.shortToast("Filename cannot be empty")
            return false
        }
        if (editFileCodeText.editText!!.text.isBlank()) {
            context.shortToast("Code cannot be empty")
            return false
        }
        return true
    }
}
