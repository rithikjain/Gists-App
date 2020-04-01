package com.rithikjain.projectgists.ui.code

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.pddstudio.highlightjs.models.Language
import com.pddstudio.highlightjs.models.Theme
import com.rithikjain.projectgists.R
import io.github.kbiakov.codeview.adapters.Options
import io.github.kbiakov.codeview.highlight.ColorTheme
import kotlinx.android.synthetic.main.fragment_code.*


class CodeFragment : Fragment() {

    private val args: CodeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_code, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val codeString = args.codeString
        val filename = args.fileName

        codeToolbar.title = filename
        codeToolbar.navigationIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_back)
        codeToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        codeView.setOptions(
            Options.Default.get(requireContext())
                .withCode(codeString)
                .withTheme(ColorTheme.MONOKAI)
        )
    }
}
