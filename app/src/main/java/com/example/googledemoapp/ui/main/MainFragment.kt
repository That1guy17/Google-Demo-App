package com.example.googledemoapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.googledemoapp.R
import com.example.googledemoapp.ui.maps.MapsAvailability
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private val mapsAvailability by lazy {
        MapsAvailability(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)

        authenticationCardView.setOnClickListener {
            viewModel.currentFragment!!(MainViewModel.FragmentNames.LoginFragment)
        }

        mapsCardView.setOnClickListener {

            if (mapsAvailability.checkIfDeviceSupportsMaps())
                viewModel.currentFragment!!(MainViewModel.FragmentNames.MapsFragment)
            else
                Toast.makeText(context, "Device is incompatible with Google Maps", Toast.LENGTH_LONG).show()
        }
    }
}