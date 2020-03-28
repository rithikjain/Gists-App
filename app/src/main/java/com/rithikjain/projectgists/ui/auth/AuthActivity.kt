package com.rithikjain.projectgists.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rithikjain.projectgists.R
import com.rithikjain.projectgists.firebase.GitHubAuth
import kotlinx.android.synthetic.main.activity_auth.*


class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val authHelper = GitHubAuth(this)

        login.setOnClickListener {
            authHelper.signIn()
        }
    }
}
