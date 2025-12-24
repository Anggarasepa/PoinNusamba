package com.anantatech.mitranusamba

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Ambil elemen logo dari layout
        val logo = findViewById<ImageView>(R.id.splashLogo)

        // Tambahkan animasi ke logo
        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        fadeIn.duration = 4000
        logo.startAnimation(fadeIn)

        // Delay ke halaman login
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 4500) // tampilkan splash screen selama 4 detik
    }
}
