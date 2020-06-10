package com.example.googledemoapp.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.googledemoapp.R
import com.example.googledemoapp.ui.login.LoginFragment
import com.example.googledemoapp.ui.maps.MapsFragment


class MainActivity : AppCompatActivity() {

    companion object {
        const val MAIN_FRAGMENT = "main frag"
        const val LOGIN_FRAGMENT = "login frag"
        const val MAPS_FRAGMENT = "maps frag"
    }

    private lateinit var viewModel: MainViewModel

    //avoids creating duplicates
    private val mainFragment: Fragment =
        supportFragmentManager.findFragmentByTag(MAIN_FRAGMENT)
            ?: MainFragment()

    private val loginFragment =
        supportFragmentManager.findFragmentByTag(LOGIN_FRAGMENT)
            ?: LoginFragment()

    private val mapsFragment =
        supportFragmentManager.findFragmentByTag(MAPS_FRAGMENT)
            ?: MapsFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if (savedInstanceState == null)
            addInitialFragment()

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        viewModel.currentFragment = { fragmentNames -> replaceContainer(fragmentNames) }
    }

    private fun replaceContainer(fragmentNames: MainViewModel.FragmentNames) {
        when (fragmentNames) {
            MainViewModel.FragmentNames.LoginFragment -> switchToLoginFragment()
            MainViewModel.FragmentNames.MapsFragment -> switchToMapsFragment()
        }
    }

    private fun addInitialFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, mainFragment)
            .commit()
    }

    private fun switchToLoginFragment() {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.enter_from_left,
                R.anim.exit_to_right,
                R.anim.enter_from_right,
                R.anim.exit_to_left
            )
            .replace(R.id.container, loginFragment)
            .addToBackStack(LOGIN_FRAGMENT)
            .commit()
    }

    private fun switchToMapsFragment() {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
            .replace(R.id.container, mapsFragment)
            .addToBackStack(MAPS_FRAGMENT)
            .commit()
    }
}