package com.aemiralfath.securechat.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.aemiralfath.securechat.R
import com.aemiralfath.securechat.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {


    private lateinit var mSignInClient: GoogleSignInClient
    private lateinit var binding: ActivityMainBinding

    // Firebase instance variables
    private lateinit var mFirebaseAuth: FirebaseAuth

    private var title = "Chat Room"
    private val ANONYMOUS = "anonymous"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail().build()

        mSignInClient = GoogleSignIn.getClient(this, gso)

        binding.rvRoom.setHasFixedSize(true)
        supportActionBar?.title = title
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu_form, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        setMode(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    private fun setMode(selectedMode: Int) {
        when (selectedMode) {
            R.id.friend_list -> {

            }
            R.id.search_friend -> {

            }
            R.id.settings -> {

            }
        }
    }

    override fun onStart() {
        super.onStart()
        mFirebaseAuth = FirebaseAuth.getInstance()
        if (mFirebaseAuth.currentUser == null){
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
            return
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
        } else ANONYMOUS
    }

    private fun signOut() {
        mFirebaseAuth.signOut()
        mSignInClient.signOut()
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }

}