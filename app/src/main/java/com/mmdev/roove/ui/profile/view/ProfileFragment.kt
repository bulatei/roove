package com.mmdev.roove.ui.profile.view


import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.mmdev.business.user.model.UserItem
import com.mmdev.roove.R
import com.mmdev.roove.core.GlideApp
import com.mmdev.roove.core.injector
import com.mmdev.roove.ui.main.view.MainActivity
import com.mmdev.roove.ui.main.viewmodel.remote.RemoteUserRepoVM
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable


/* Created by A on 10.11.2019.*/

/**
 * This is the documentation block about the class
 */

class ProfileFragment: Fragment(R.layout.fragment_profile_view) {

	private lateinit var mMainActivity: MainActivity

	private val disposables = CompositeDisposable()

	private lateinit var remoteRepoViewModel: RemoteUserRepoVM
	private val remoteUserRepoFactory = injector.remoteUserRepoVMFactory()

	private lateinit var userId: String

	companion object{
		//todo: remove data transfer between fragments, need to make it more abstract
		@JvmStatic
		fun newInstance(userId: String) = ProfileFragment().apply {
			arguments = Bundle().apply {
				putString(USER_ID_KEY, userId)
			}
		}

		private const val USER_ID_KEY = "USER_ID"
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		activity?.let { mMainActivity = it as MainActivity }
		arguments?.let {
			userId = it.getString(USER_ID_KEY, "")
		}

		remoteRepoViewModel = ViewModelProvider(mMainActivity, remoteUserRepoFactory).get(RemoteUserRepoVM::class.java)

	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		disposables.add(remoteRepoViewModel.getUserById(userId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ updateContent(view, it) },
                       { mMainActivity.showInternetError("$it") }))

	}

	private fun updateContent(view:View, userItem: UserItem){
		val ivProfileUserPic = view.findViewById<ImageView>(R.id.profile_user_pic)
		val toolbarProfile = view.findViewById<Toolbar>(R.id.profile_toolbar)
		val collapsingToolbarLayout: CollapsingToolbarLayout = view.findViewById(R.id.profile_collapsing_toolbar)
		collapsingToolbarLayout.title = userItem.name
		GlideApp.with(ivProfileUserPic.context)
			.load(userItem.mainPhotoUrl)
			.into(ivProfileUserPic)
		toolbarProfile.setNavigationOnClickListener { mMainActivity.onBackPressed() }
		toolbarProfile.inflateMenu(R.menu.profile_view_options)
		toolbarProfile.setOnMenuItemClickListener { item ->
			when (item.itemId) {
				R.id.action_report -> { Toast.makeText(mMainActivity,
				                                       "action report click",
				                                       Toast.LENGTH_SHORT).show()
				}
			}
			return@setOnMenuItemClickListener true
		}


	}


	override fun onResume() {
		super.onResume()
		mMainActivity.appbar.visibility = View.GONE
		mMainActivity.toolbar.visibility = View.GONE
	}

	override fun onStop() {
		super.onStop()
		mMainActivity.appbar.visibility = View.VISIBLE
		mMainActivity.toolbar.visibility = View.VISIBLE
	}

	override fun onDestroy() {
		super.onDestroy()
		disposables.clear()
	}
}