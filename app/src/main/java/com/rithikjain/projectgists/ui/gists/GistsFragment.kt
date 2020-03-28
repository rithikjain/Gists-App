package com.rithikjain.projectgists.ui.gists

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.rithikjain.projectgists.R
import com.rithikjain.projectgists.ui.auth.AuthActivity
import com.rithikjain.projectgists.util.Constants
import com.rithikjain.projectgists.util.PrefHelper
import kotlinx.android.synthetic.main.fragment_gists.*


class GistsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gists, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = PrefHelper.customPrefs(requireContext(), Constants.PREF_NAME)

        val firebaseAuth = FirebaseAuth.getInstance()
        val photoUri = firebaseAuth.currentUser!!.photoUrl

        logOutBtn.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(activity, AuthActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        Log.d("esh", "Token: ${sharedPref.getString(Constants.PREF_AUTH_TOKEN, "")}")

        toolbarTitle.text = "My Gists"
        toolbarSubtitle.text = "${firebaseAuth.currentUser!!.displayName}"

        Glide.with(this)
            .load(photoUri)
            .centerCrop()
            .placeholder(R.drawable.ic_github_logo)
            .into(profileImage)
    }
}
