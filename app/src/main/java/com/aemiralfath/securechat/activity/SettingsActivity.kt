package com.aemiralfath.securechat.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aemiralfath.securechat.R
import com.aemiralfath.securechat.databinding.ActivitySettingsBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : AppCompatActivity() {

    private lateinit var mSignInClient: GoogleSignInClient
    private lateinit var binding: ActivitySettingsBinding

    private var title = "Settings"

    private lateinit var mFirebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail().build()

        mSignInClient = GoogleSignIn.getClient(this, gso)
        mFirebaseAuth = FirebaseAuth.getInstance()
        supportActionBar?.title = title

        binding.tvItemName.text = getUserName()
        binding.tvItemEmail.text = getUserEmail()
        Glide.with(this).load(getUserPhotoUrl()).placeholder(R.drawable.ic_baseline_person_24)
            .apply(RequestOptions().override(75, 75)).into(binding.imgItemPhoto)

        binding.btnSignOut.setOnClickListener{
            signOut()
        }
    }

    private fun getUserPhotoUrl(): String? {
        val user = mFirebaseAuth.currentUser
        return if (user != null && user.photoUrl != null){
            return user.photoUrl.toString()
        }else null
    }

    private fun getUserName():String{
        val user = mFirebaseAuth.currentUser
        return if (user != null) {
            user.displayName!!
        } else "ANONYMOUS"
    }

    private fun getUserID():String{
        val user = mFirebaseAuth.currentUser
        return user?.uid ?: "ANONYMOUS"
    }

    private fun getUserEmail():String{
        val user = mFirebaseAuth.currentUser
        return if (user != null) {
            user.email!!
        } else "ANONYMOUS"
    }

    private fun signOut() {
        mFirebaseAuth.signOut()
        mSignInClient.signOut()
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }
}