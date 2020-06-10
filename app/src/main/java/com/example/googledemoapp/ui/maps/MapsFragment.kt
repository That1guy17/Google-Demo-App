package com.example.googledemoapp.ui.maps

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.googledemoapp.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlin.system.exitProcess


/**
 * A basic Google Maps implementation that gives the user access to satellite coverage of the globe
 * as well as their own location.
 */
class MapsFragment : Fragment() {

    private val locationPermission = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val callback = OnMapReadyCallback { googleMap ->
        val sydney = LatLng(-34.0, 151.0)

        googleMap.apply {
            addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
            moveCamera(CameraUpdateFactory.newLatLng(sydney))
            isMyLocationEnabled = true
            uiSettings.isMyLocationButtonEnabled = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        if (isLocationPermissionGranted())
            initMap()
        else requestLocationPermission()
    }

    private fun initMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(callback)
    }

    private fun getDeviceLocation(onFoundLocation: (Location?) -> Unit) {
        try {
            if (isLocationPermissionGranted()) {

                val locationTask = fusedLocationProviderClient.lastLocation

                locationTask.addOnCompleteListener { completedTask ->
                    if (completedTask.isSuccessful) onFoundLocation(
                        completedTask.result!!
                    )
                }
            }
        } catch (e: SecurityException) {
            Log.d("zwi", "Security Exception in MapsFragment: $e")
        }
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            locationPermission[0]
        ) == PackageManager.PERMISSION_GRANTED
    }

    //Invokes onRequestPermissionsResult
    private fun requestLocationPermission() {
        requestPermissions(locationPermission, 1234)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        //permission was granted
            initMap()
        else
        //leave app
            exitProcess(0)
    }
}