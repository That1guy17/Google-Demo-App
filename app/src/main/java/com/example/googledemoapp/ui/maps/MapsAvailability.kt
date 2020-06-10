package com.example.googledemoapp.ui.maps

import android.app.Activity
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability


/**
Every android phone has google play services installed, but in order to use google maps it
it has to be a certain version.
 */
class MapsAvailability(private val activity: Activity) {

    private val isAvailable = GoogleApiAvailability.getInstance()
        .isGooglePlayServicesAvailable(activity)

    private val availabilityInstance = GoogleApiAvailability.getInstance()

    fun checkIfDeviceSupportsMaps(): Boolean {
        when {
            isAvailable == ConnectionResult.SUCCESS ->
                return true

            availabilityInstance.isUserResolvableError(isAvailable) -> {
                // an error occurred that's resolvable, google provides a dialog for this specific issue
                val dialog = availabilityInstance.getErrorDialog(activity, isAvailable, 125)
                dialog.show()
            }
        }
        return false
    }
}