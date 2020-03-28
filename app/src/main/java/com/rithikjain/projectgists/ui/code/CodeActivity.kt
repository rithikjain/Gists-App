package com.rithikjain.projectgists.ui.code

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rithikjain.projectgists.R
import io.github.kbiakov.codeview.adapters.Options
import io.github.kbiakov.codeview.highlight.ColorTheme
import io.github.kbiakov.codeview.highlight.Font
import kotlinx.android.synthetic.main.activity_code.*

class CodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code)

        val codeStr = intent.getStringExtra("code")

        codeView.setOptions(
            Options.Default.get(this)
                .withCode(codeStr)
                .withFont(Font.Consolas)
                .withTheme(ColorTheme.MONOKAI)
        )
    }
}
