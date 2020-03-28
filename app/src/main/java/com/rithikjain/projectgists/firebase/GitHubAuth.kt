package com.rithikjain.projectgists.firebase

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthCredential
import com.google.firebase.auth.OAuthProvider
import com.rithikjain.projectgists.ui.PostAuthActivity
import com.rithikjain.projectgists.util.shortToast

class GitHubAuth (private val activity: Activity) {
    private val firebaseAuth = FirebaseAuth.getInstance()

    private val provider = OAuthProvider.newBuilder("github.com")
    private val scopes = listOf("gist")

    fun signIn() {
        provider.setScopes(scopes)

        val pendingResultTask = firebaseAuth.pendingAuthResult
        if (pendingResultTask != null) {
            pendingResultTask.addOnSuccessListener(
                OnSuccessListener {
                    Log.d("esh", firebaseAuth.currentUser!!.photoUrl.toString())
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

                    activity.shortToast("Authentication Successful")

                    val intent = Intent(activity, PostAuthActivity::class.java)
                    activity.startActivity(intent)
                    activity.finish()
                }
                .addOnFailureListener {
                    activity.shortToast("Authentication Failed")
                }
        }
    }

}