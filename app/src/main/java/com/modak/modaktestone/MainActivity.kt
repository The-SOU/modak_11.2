package com.modak.modaktestone

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.modak.modaktestone.databinding.ActivityMainBinding
import com.modak.modaktestone.navigation.*
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask


class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding

    var firestore: FirebaseFirestore? = null

    private val FCM_API = "https://fcm.googleapis.com/fcm/send"
    private val serverKey =
        "key=" + "Enter your Key"
    private val contentType = "application/json"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        firestore = FirebaseFirestore.getInstance()

        binding.bottomNavigation.setOnNavigationItemSelectedListener(this)

        binding.bottomNavigation.selectedItemId = R.id.action_home

        binding.bottomNavigation.itemIconTintList = null

        retrieveAndStoreToken()

    }

    private fun retrieveAndStoreToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token: String? = task.result

                val userId: String? = FirebaseAuth.getInstance().currentUser?.uid

                FirebaseDatabase.getInstance().getReference("tokens").child(userId!!)
                    .setValue(token)
            }
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_home -> {
                var detailViewFragment = DetailViewFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_content, detailViewFragment).commit()
                return true
            }
            R.id.action_board -> {
                var boardFragment = BoardFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content, boardFragment)
                    .commit()
                return true
            }
            R.id.action_alarm -> {
                var alarmFragment = AlarmFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content, alarmFragment)
                    .commit()
                return true
            }
            R.id.action_account -> {
                var accountFragment = AccountFragment()
                var bundle = Bundle()
                var uid = FirebaseAuth.getInstance().currentUser?.uid
                bundle.putString("destinationUid", uid)
                accountFragment.arguments = bundle
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_content, accountFragment).commit()
                return true
            }
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AccountFragment.PICK_PROFILE_FROM_ALBUM && resultCode == Activity.RESULT_OK) {
            var imageUri = data?.data
            var uid = FirebaseAuth.getInstance().currentUser?.uid
            var storageRef =
                FirebaseStorage.getInstance().reference.child("userProfileImages")?.child(uid!!)
            storageRef?.putFile(imageUri!!)
                ?.continueWithTask { task: Task<UploadTask.TaskSnapshot> ->
                    return@continueWithTask storageRef.downloadUrl
                }?.addOnCompleteListener {
                storageRef.downloadUrl
                    .addOnSuccessListener(OnSuccessListener<Uri?> { uri ->
                        var map = HashMap<String, Any>()
                        map["profileUrl"] = uri.toString()
                        FirebaseFirestore.getInstance().collection("users").document(uid!!).update(map)
                    })
            }
        }
    }
}