package com.rithikjain.projectgists.firebase

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthCredential
import com.google.firebase.auth.OAuthProvider
import com.rithikjain.projectgists.model.register.RegisterRequest
import com.rithikjain.projectgists.repository.AppRepository
import com.rithikjain.projectgists.ui.PostAuthActivity
import com.rithikjain.projectgists.ui.auth.AuthViewModel
import com.rithikjain.projectgists.util.Constants
import com.rithikjain.projectgists.util.PrefHelper
import com.rithikjain.projectgists.util.PrefHelper.set
import com.rithikjain.projectgists.util.shortToast

class GitHubAuth (private val activity: Activity, private val viewModel: AuthViewModel) {
    private val firebaseAuth = FirebaseAuth.getInstance()

    private val provider = OAuthProvider.newBuilder("github.com")
    private val scopes = listOf("gist")

    private val pref = PrefHelper.customPrefs(activity, Constants.PREF_NAME)

    fun signIn() {
        provider.setScopes(scopes)

        val pendingResultTask = firebaseAuth.pendingAuthResult
        if (pendingResultTask != null) {
            pendingResultTask.addOnSuccessListener(
                OnSuccessListener {
                    activity.shortToast("Success")
                })
                .addOnFailureListener {
                    activity.shortToast("Failure")
                }
        } else {
            firebaseAuth.startActivityForSignInWithProvider(
                activity,
                provider.build()
            )
                .addOnSuccessListener {
                    val oAuth = it.credential as OAuthCredential
                    Log.d("esh", oAuth.accessToken)
                    pref[Constants.PREF_AUTH_TOKEN] = oAuth.accessToken

                    activity.shortToast("Authentication Successful")

                    val registerRequest = RegisterRequest(
                        it.user!!.email ?: "",
                        it.user!!.displayName ?: "",
                        oAuth.accessToken
                    )

                    /*
                    val intent = Intent(activity, PostAuthActivity::class.java)
                    activity.startActivity(intent)
                    activity.finish()
                     */
                }
                .addOnFailureListener {
                    activity.shortToast("Authentication Failed")
                }
        }
    }

}