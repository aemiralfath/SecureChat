package com.aemiralfath.securechat.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aemiralfath.securechat.R
import com.aemiralfath.securechat.databinding.ActivitySignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class SignInActivity : AppCompatActivity() {
    private val TAG: String = "SignInActivity"
    private val RC_SIGN_IN: Int = 9001

    private lateinit var binding: ActivitySignInBinding
    private lateinit var mSignInClient: GoogleSignInClient

    private lateinit var mFirebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signInButton.setOnClickListener {
            signIn()
        }

        val gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build()

        mSignInClient = GoogleSignIn.getClient(this, gso)

        // Initialize FirebaseAuth
        mFirebaseAuth = FirebaseAuth.getInstance()
        supportActionBar?.title = ""
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
                if (account != null) {
                    firebaseAuthWithGoogle(account)
                } else {
                    Log.w(TAG, "Google sign in failed Null")
                }
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoole:" + account.id)
        val credential: AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)

        mFirebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener(this) {
                Log.d(TAG, "signInWithCredential:success")
                startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener(this) { e ->
                Log.w(TAG, "signInWithCredential", e)
                Toast.makeText(
                    this@SignInActivity, "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun signIn() {
        val signInIntent: Intent = mSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
}