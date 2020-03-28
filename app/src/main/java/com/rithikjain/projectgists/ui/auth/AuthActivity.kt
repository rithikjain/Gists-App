package com.rithikjain.projectgists.ui.auth

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import com.rithikjain.projectgists.R
import kotlinx.android.synthetic.main.activity_auth.*


class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val provider = OAuthProvider.newBuilder("github")
        val firebaseAuth = FirebaseAuth.getInstance()

        login.setOnClickListener {
            val pendingResultTask = firebaseAuth.pendingAuthResult
            if (pendingResultTask != null) {
                // There's something already here! Finish the sign-in for your user.
                pendingResultTask.addOnSuccessListener(
                    OnSuccessListener {
                        // User is signed in.
                        // IdP data available in
                        // authResult.getAdditionalUserInfo().getProfile().
                        // The OAuth access token can also be retrieved:
                        // authResult.getCredential().getAccessToken().
                        Toast.makeText(this, "Pending Result onSuccess", Toast.LENGTH_SHORT).show()
                    })
                    .addOnFailureListener {
                        // Handle failure.
                        Toast.makeText(this, "Pending Result onFailure", Toast.LENGTH_SHORT).show()
                    }
            } else {
                // There's no pending result so you need to start the sign-in flow.
                // See below.

                firebaseAuth
                    .startActivityForSignInWithProvider( /* activity= */this, provider.build())
                    .addOnSuccessListener {
                        // User is signed in.
                        // IdP data available in
                        // authResult.getAdditionalUserInfo().getProfile().
                        // The OAuth access token can also be retrieved:
                        // authResult.getCredential().getAccessToken().
                        Toast.makeText(this, "Pending Result authSuccess", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        // Handle failure.
                        Toast.makeText(this, "Pending Result authFailure", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
