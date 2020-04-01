package com.rithikjain.projectgists.ui.gists

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.github.ybq.android.spinkit.style.WanderingCubes
import com.google.firebase.auth.FirebaseAuth
import com.rithikjain.projectgists.R
import com.rithikjain.projectgists.adapter.GistListAdapter
import com.rithikjain.projectgists.model.Result
import com.rithikjain.projectgists.model.gists.DeleteGistRequest
import com.rithikjain.projectgists.model.gists.File
import com.rithikjain.projectgists.util.*
import kotlinx.android.synthetic.main.fragment_gists.*
import kotlinx.android.synthetic.main.gist_recycler_view_item.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.util.*


class GistsFragment : Fragment() {

    private lateinit var files: MutableList<File>

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

        gistsProgress.setIndeterminateDrawable(WanderingCubes())
        gistsProgress.hide()
        gistsRefresh.isRefreshing = false
        noGistsText.hide()

        Log.d("esh", "Token: ${sharedPref.getString(Constants.PREF_AUTH_TOKEN, "")}")

        toolbarTitle.text = "My Gists"
        toolbarSubtitle.text = "${firebaseAuth.currentUser!!.displayName}"

        Glide.with(this)
            .load(photoUri)
            .centerCrop()
            .placeholder(R.drawable.ic_github_logo)
            .into(profileImage)

        profileImage.setOnClickListener {
            val action = GistsFragmentDirections.actionGistsFragmentToProfileFragment()
            findNavController().navigate(action)
        }

        val gistListAdapter = GistListAdapter()
        gistsRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = gistListAdapter
        }

        getGists(gistsViewModel, gistListAdapter)

        gistsRefresh.setOnRefreshListener {
            getGists(gistsViewModel, gistListAdapter)
        }

        gistsRecyclerView.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                val content = files[position].Content
                val language = files[position].Language.toLowerCase(Locale.ROOT)
                val filename = files[position].Filename
                Log.d("esh", language)
                val action = GistsFragmentDirections.actionGistsFragmentToCodeFragment(
                    content,
                    filename
                )
                findNavController().navigate(action)
            }
        })

        gistsRecyclerView.addOnItemLongClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                val deleteGistRequest = DeleteGistRequest(
                    gistListAdapter.gistList[position].GistID,
                    gistListAdapter.gistList[position].Filename
                )

                gistsViewModel.deleteGist(deleteGistRequest)
                    .observe(viewLifecycleOwner, Observer {
                        when (it.status) {
                            Result.Status.LOADING -> {
                                requireContext().shortToast("Loading")
                            }
                            Result.Status.SUCCESS -> {
                                gistListAdapter.remove(position)

                                requireContext().shortToast("Deleted")
                            }
                            Result.Status.ERROR -> {
                                requireContext().shortToast("Error Occured!")
                            }
                        }
                    })
            }
        })

        addGistBtn.setOnClickListener {
            val action = GistsFragmentDirections.actionGistsFragmentToAddGistFragment()
            findNavController().navigate(action)
        }
    }

    private fun getGists(gistsViewModel: GistsViewModel, gistListAdapter: GistListAdapter) {
        gistsViewModel.viewAllGists().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Result.Status.LOADING -> {
                    gistsProgress.show()
                    gistsRefresh.disable()
                    gistsRecyclerView.hide()
                }
                Result.Status.SUCCESS -> {
                    gistsProgress.hide()
                    gistsRefresh.enable()
                    gistsRecyclerView.show()
                    gistsRefresh.isRefreshing = false

                    if (!it.data!!.Files.isNullOrEmpty()) {
                        files = it.data.Files as MutableList<File>
                        gistListAdapter.updateGists(files)
                    } else {
                        noGistsText.show()
                    }
                }
                Result.Status.ERROR -> {
                    requireContext().shortToast("Error in fetching gists")
                    gistsProgress.hide()
                    gistsRefresh.enable()
                    gistsRecyclerView.show()
                    gistsRefresh.isRefreshing = false
                    Log.d("esh", it.message)
                }
            }
        })
    }
}
