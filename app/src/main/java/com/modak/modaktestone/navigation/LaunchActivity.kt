package com.modak.modaktestone.navigation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.modak.modaktestone.MainActivity
import com.modak.modaktestone.databinding.ActivityLaunchBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LaunchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLaunchBinding

    var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLaunchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //초기화
        auth = FirebaseAuth.getInstance()

        binding.launchBtn.setOnClickListener {
            startActivity(Intent(this, PhoneCertificationActivity::class.java))
            finish()
        }


    }

    fun moveMainpage(user: FirebaseUser?) {
        if(user != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    //어스에 커렌트유저가 있으면 바로메인페이지
    override fun onStart() {
        super.onStart()
        moveMainpage(auth?.currentUser)
    }
}