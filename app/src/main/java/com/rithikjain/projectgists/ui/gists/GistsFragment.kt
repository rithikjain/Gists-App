package com.rithikjain.projectgists.ui.gists

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.github.ybq.android.spinkit.style.WanderingCubes
import com.google.firebase.auth.FirebaseAuth
import com.rithikjain.projectgists.R
import com.rithikjain.projectgists.model.Result
import com.rithikjain.projectgists.ui.auth.AuthActivity
import com.rithikjain.projectgists.util.*
import kotlinx.android.synthetic.main.fragment_gists.*
import org.koin.android.viewmodel.ext.android.sharedViewModel


class GistsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gists, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gistsViewModel by sharedViewModel<GistsViewModel>()

        val sharedPref = PrefHelper.customPrefs(requireContext(), Constants.PREF_NAME)

        val firebaseAuth = FirebaseAuth.getInstance()
        val photoUri = firebaseAuth.currentUser!!.photoUrl

        logOutBtn.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(activity, AuthActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        gistsProgress.setIndeterminateDrawable(WanderingCubes())
        gistsProgress.Hide()

        Log.d("esh", "Token: ${sharedPref.getString(Constants.PREF_AUTH_TOKEN, "")}")

        toolbarTitle.text = "My Gists"
        toolbarSubtitle.text = "${firebaseAuth.currentUser!!.displayName}"

        Glide.with(this)
            .load(photoUri)
            .centerCrop()
            .placeholder(R.drawable.ic_github_logo)
            .into(profileImage)

        gistsViewModel.viewAllGists().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Result.Status.LOADING -> {
                    gistsProgress.Show()
                }
                Result.Status.SUCCESS -> {
                    gistsProgress.Hide()
                    Log.d("esh", it.data.toString())
                }
                Result.Status.ERROR -> {
                    requireContext().shortToast("Error in fetching gists")
                    Log.d("esh", it.message)
                }
            }
        })
    }
}
