package com.example.googledemoapp.ui.main

import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    /**Init's in MainActivity's onStart() so it won't be null in the Fragments*/
    var currentFragment: ((FragmentNames) -> Unit)? = null

    enum class FragmentNames {
        LoginFragment, MapsFragment
    }
}