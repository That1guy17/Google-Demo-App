package com.example.googledemoapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateOvershootInterpolator
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import coil.api.load
import com.example.googledemoapp.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.fragment_login_signed_out.*

/*
Google's sign in docs were wrong, you have to enter the application ID in build.gradle when configuring the
project instead of adding the package name from manifest. Took me hours to figure that out :/
 */
class LoginFragment : Fragment() {

    companion object {
        const val RC_SIGN_IN = 0
    }

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var googleSignInClient: GoogleSignInClient
    private val signInConstraints = ConstraintSet()
    private val signedOutConstraints = ConstraintSet()
    private val defaultTransition = ChangeBounds().apply {
        interpolator = AnticipateOvershootInterpolator(.5f)
        duration = 1200
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login_signed_out, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(activity!!, gso);

        signIn.setOnClickListener { signIn() }
        signOut.setOnClickListener { signOut() }

        signedOutConstraints.clone(signedOutLayout)
        signInConstraints.clone(context, R.layout.fragment_login_signed_in)
    }

    override fun onStart() {
        super.onStart()

        val account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(context)

        if (account == null) signIn()
        else updateUi(account)
    }

    private fun signIn() {
        val signInIntent: Intent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN) //invokes onActivityResult below
    }

    private fun signOut() {
        googleSignInClient.signOut().addOnSuccessListener { startAnim() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from user signIn()
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            updateUi(account!!)
        } catch (e: ApiException) {
            Log.d("zwi", "Error at handleSignInResult in LoginFragment: $e")
        }
    }

    private fun updateUi(account: GoogleSignInAccount) {
        displayName.text = "Display Name: ${account.displayName}"
        email.text = "Email: ${account.email}"

        if (account.photoUrl == null)
            profilePic.load(R.drawable.default_pfp)
        else
            profilePic.load(account.photoUrl)

        startAnim()
    }

    private fun startAnim() {
        TransitionManager.beginDelayedTransition(signedOutLayout, defaultTransition)

        if (loginViewModel.altLayout)
            signInConstraints.applyTo(signedOutLayout)
        else
            signedOutConstraints.applyTo(signedOutLayout)

        loginViewModel.altLayout = !loginViewModel.altLayout
    }
}