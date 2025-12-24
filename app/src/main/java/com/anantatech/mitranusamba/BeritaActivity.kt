package com.anantatech.mitranusamba

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.messaging.FirebaseMessaging

class BeritaActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var beritaList: ArrayList<CharSequence>
    private lateinit var adapter: ArrayAdapter<CharSequence>

    private lateinit var progressBar: ProgressBar
    private lateinit var contentLayout: LinearLayout

    private val URL_BERITA =
        "https://script.google.com/macros/s/AKfycbyS5jD7z9XyPenBWnnt91JntEeOnZwyESkJ45_0f2RRnD6xFCaDSZfn8a580yr5c-79/exec?action=berita"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_berita)

        listView = findViewById(R.id.listViewBerita)
        progressBar = findViewById(R.id.progressBar)
        contentLayout = findViewById(R.id.contentLayout)

        beritaList = ArrayList()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, beritaList)
        listView.adapter = adapter

        loadBeritaData()
        createNotificationChannel()

        // 🔔 Subscribe ke topik FCM
        FirebaseMessaging.getInstance().subscribeToTopic("berita")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM_TEST", "Berhasil subscribe ke topik 'berita'")
                    Toast.makeText(this, "Berlangganan notifikasi berita", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("FCM_TEST", "Gagal subscribe ke topik 'berita'", task.exception)
                    Toast.makeText(this, "Gagal berlangganan FCM", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun loadBeritaData() {
        showLoading(true)

        val queue = Volley.newRequestQueue(this)

        val request = JsonArrayRequest(Request.Method.GET, URL_BERITA, null,
            { response ->
                beritaList.clear()
                for (i in 0 until response.length()) {
                    val berita = response.getJSONObject(i)
                    val judul = berita.getString("Judul")
                    val deskripsi = berita.getString("Deskripsi")
                    val tanggal = berita.getString("Tanggal")

                    val fullText = "$judul\n$tanggal\n$deskripsi"
                    val spannable = SpannableString(fullText)
                    spannable.setSpan(
                        StyleSpan(android.graphics.Typeface.BOLD),
                        0,
                        judul.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )

                    beritaList.add(spannable)
                }
                adapter.notifyDataSetChanged()
                showLoading(false)
            },
            { error ->
                showLoading(false)
                Toast.makeText(this, "Gagal mengambil berita, coba lagi", Toast.LENGTH_SHORT).show()
                error.printStackTrace()
            })

        queue.add(request)
    }

    private fun showLoading(show: Boolean) {
        if (show) {
            progressBar.visibility = View.VISIBLE
            contentLayout.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            contentLayout.visibility = View.VISIBLE
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "berita_channel",
                "Notifikasi Berita",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    override fun onStart() {
        super.onStart()
        checkAndRequestNotificationPermission()
    }

    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = android.Manifest.permission.POST_NOTIFICATIONS
            val granted = ContextCompat.checkSelfPermission(this, permission) ==
                    PackageManager.PERMISSION_GRANTED
            if (!granted) {
                ActivityCompat.requestPermissions(this, arrayOf(permission), 1001)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1001) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Izin notifikasi diberikan
            } else {
                // Izin notifikasi ditolak
            }
        }
    }
}
