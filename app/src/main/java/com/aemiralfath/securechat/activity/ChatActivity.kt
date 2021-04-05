package com.aemiralfath.securechat.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.aemiralfath.securechat.MessageViewHolder
import com.aemiralfath.securechat.R
import com.aemiralfath.securechat.databinding.ActivityChatBinding
import com.aemiralfath.securechat.entity.FirebaseMessage
import com.aemiralfath.securechat.observer.MyButtonObserver
import com.aemiralfath.securechat.observer.MyScrollToBottomObserver
import com.aemiralfath.securechat.security.AES
import com.aemiralfath.securechat.security.DigitalSignature
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.security.KeyPair

class ChatActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "ChatActivity"
        private const val TITLE = "Chat Room"
        private const val MESSAGES_CHILD = "messages"
        private const val ANONYMOUS = "anonymous"
        private const val REQUEST_IMAGE = 2
        private const val LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif"
    }

    private lateinit var binding: ActivityChatBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var signInClient: GoogleSignInClient
    private lateinit var firebaseAdapter: FirebaseRecyclerAdapter<FirebaseMessage, MessageViewHolder>

    private lateinit var aes: AES
    private lateinit var keyPair: KeyPair
    private lateinit var digitalSignature: DigitalSignature


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = TITLE

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        signInClient = GoogleSignIn.getClient(this, gso)
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        aes = AES()
        digitalSignature = DigitalSignature()
        keyPair = digitalSignature.getKeyPair()


        val messagesRef = database.reference.child(MESSAGES_CHILD)

        val options = FirebaseRecyclerOptions.Builder<FirebaseMessage>()
            .setQuery(messagesRef, FirebaseMessage::class.java).build()

        firebaseAdapter =
            object : FirebaseRecyclerAdapter<FirebaseMessage, MessageViewHolder>(options) {
                override fun onBindViewHolder(
                    holder: MessageViewHolder,
                    position: Int,
                    model: FirebaseMessage
                ) {
                    receiveMessage(model, holder)
                }

                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): MessageViewHolder {
                    val inflater = LayoutInflater.from(parent.context)
                    return MessageViewHolder(inflater.inflate(R.layout.item_message, parent, false))
                }
            }

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd = true
        binding.rvMessages.layoutManager = linearLayoutManager
        binding.rvMessages.adapter = firebaseAdapter

        firebaseAdapter.registerAdapterDataObserver(
            MyScrollToBottomObserver(binding.rvMessages, firebaseAdapter, linearLayoutManager)
        )

        binding.edtMessage.addTextChangedListener(MyButtonObserver(binding.btnSend, null))

        val publicKeyBytes = Base64.encode(keyPair.public.encoded, 0)
        val publicK = String(publicKeyBytes)

        binding.btnSend.setOnClickListener {
            val start = System.nanoTime()
            val text = binding.edtMessage.text.toString()
            val cipherText = aes.encrypt(text)
            val signText = digitalSignature.sign(cipherText, keyPair.private)
            val end = System.nanoTime()

            val message = FirebaseMessage(
                cipherText,
                text.length,
                end - start,
                signText,
                publicK,
                getUserName(),
                getUserPhotoUrl(),
                null
            )

            database.reference.child(MESSAGES_CHILD).push().setValue(message)
            binding.edtMessage.setText("")
        }

        binding.imgAddMessage.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_IMAGE)
        }
    }

    private fun receiveMessage(model: FirebaseMessage, holder: MessageViewHolder) {
        if (model.text != null && model.decrypt == null) {
            if (model.sign != null && model.publicKey != null) {

                val verify =
                    digitalSignature.verifySign(
                        model.text!!, model.sign!!, digitalSignature.getPublic(
                            model.publicKey.toString()
                        )
                    )

                if (verify) {
                    model.text = aes.decrypt(model.text!!)
                } else {
                    model.text = "Pesan Tidak Dapat Dipercaya!!!"
                }
            } else {
                model.text = aes.decrypt(model.text!!)
            }
            model.decrypt = true
        }

        binding.progressBar.visibility = ProgressBar.INVISIBLE
        holder.bindMessage(model)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(
            MainActivity.TAG,
            "onActivityResult: requestCode=$requestCode, resultCode=$resultCode"
        )
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK && data != null) {
                val uri = data.data
                Log.d(MainActivity.TAG, "Uri:" + uri.toString())
                val user = firebaseAuth.currentUser
                val tempMessage = FirebaseMessage(
                    name = getUserName(),
                    photoUrl = getUserPhotoUrl(),
                    imageUrl = LOADING_IMAGE_URL
                )

                database.reference.child(MESSAGES_CHILD).push()
                    .setValue(tempMessage,
                        DatabaseReference.CompletionListener { error, ref ->
                            if (error != null) {
                                Log.w(
                                    MainActivity.TAG,
                                    "Unable to write message to database.",
                                    error.toException()
                                )
                                return@CompletionListener
                            }
                            val key = ref.key
                            val storageReference = FirebaseStorage.getInstance()
                                .getReference(user?.uid.toString()).child(key!!)
                                .child(uri!!.lastPathSegment!!)
                            putImageInStorage(storageReference, uri, key)
                        })
            }
        }
    }

    private fun putImageInStorage(storageReference: StorageReference, uri: Uri, key: String) {
        storageReference.putFile(uri).addOnSuccessListener(
            this
        ) { taskSnapshot ->
            taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                Log.d(TAG, "Success Upload")
                val friendlyMessage =
                    FirebaseMessage(
                        name = getUserName(),
                        photoUrl = getUserPhotoUrl(),
                        imageUrl = uri.toString()
                    )
                database.reference.child(MESSAGES_CHILD).child(key)
                    .setValue(friendlyMessage)
            }
        }.addOnFailureListener(
            this
        ) { e -> Log.w(TAG, "Image upload task was not succesful.", e) }
    }

    private fun getUserPhotoUrl(): String? {
        val user = firebaseAuth.currentUser
        return if (user != null && user.photoUrl != null) {
            user.photoUrl.toString()
        } else {
            null
        }
    }

    private fun getUserName(): String {
        return firebaseAuth.currentUser?.displayName ?: ANONYMOUS
    }

    override fun onPause() {
        firebaseAdapter.stopListening()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        firebaseAdapter.startListening()
    }
}