package com.rithikjain.projectgists.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthCredential
import com.google.firebase.auth.OAuthProvider
import com.rithikjain.projectgists.R
import com.rithikjain.projectgists.model.Result
import com.rithikjain.projectgists.model.register.RegisterRequest
import com.rithikjain.projectgists.ui.PostAuthActivity
import com.rithikjain.projectgists.util.*
import com.rithikjain.projectgists.util.PrefHelper.set
import kotlinx.android.synthetic.main.activity_auth.*
import org.koin.android.viewmodel.ext.android.viewModel


class AuthActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()

        val firebaseAuth = FirebaseAuth.getInstance()
        if (firebaseAuth.currentUser != null) {
            val intent = Intent(this, PostAuthActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val authViewModel by viewModel<AuthViewModel>()
        val firebaseAuth = FirebaseAuth.getInstance()

        val provider = OAuthProvider.newBuilder("github.com")
        val scopes = listOf("gist")
        provider.setScopes(scopes)

        val pref = PrefHelper.customPrefs(this, Constants.PREF_NAME)

        loginButton.setOnClickListener {
            loginButton.isEnabled = false

            val pendingResultTask = firebaseAuth.pendingAuthResult
            if (pendingResultTask != null) {
                pendingResultTask.addOnSuccessListener(
                    OnSuccessListener {
                        shortToast("Success")
                    })
                    .addOnFailureListener {
                        shortToast("Failure")
                    }
            } else {
                firebaseAuth.startActivityForSignInWithProvider(
                    this,
                    provider.build()
                )
                    .addOnSuccessListener {
                        val oAuth = it.credential as OAuthCredential

                        shortToast("Authentication Successful")

                        val registerRequest = RegisterRequest(
                            it.user!!.email ?: "",
                            it.user!!.displayName ?: "",
                            oAuth.accessToken
                        )

                        authViewModel.registerUser(registerRequest)
                            .observe(this, Observer { response ->
                                when (response.status) {
                                    Result.Status.LOADING -> {
                                        titleText.Hide()
                                        loginButton.Hide()
                                    }
                                    Result.Status.SUCCESS -> {
                                        pref[Constants.PREF_AUTH_TOKEN] = response.data!!.Token
                                        val intent = Intent(this, PostAuthActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                    Result.Status.ERROR -> {
                                        titleText.Show()
                                        loginButton.Show()
                                        loginButton.isEnabled = true
                                        Log.d("esh", response.message)
                                        longToast("Login failed, please try again")
                                    }
                                }
                            })
                    }
                    .addOnFailureListener {
                        shortToast("Authentication Failed")
                    }
            }

        }
    }
}
