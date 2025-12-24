package com.anantatech.mitranusamba

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private lateinit var idInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginBtn: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var contentLayout: LinearLayout

    private val URL_LOGIN =
        "https://script.google.com/macros/s/AKfycbyS5jD7z9XyPenBWnnt91JntEeOnZwyESkJ45_0f2RRnD6xFCaDSZfn8a580yr5c-79/exec"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        idInput = findViewById(R.id.editTextId)
        passwordInput = findViewById(R.id.editTextPassword)
        loginBtn = findViewById(R.id.btnLogin)

        progressBar = findViewById(R.id.progressBar)
        contentLayout = findViewById(R.id.contentLayout)

        loginBtn.setOnClickListener {
            val id = idInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (id.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "ID dan Password harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            showLoading(true)

            val url = "$URL_LOGIN?action=login&id=$id&password=$password"
            val queue = Volley.newRequestQueue(this)

            val request = StringRequest(Request.Method.GET, url,
                { response ->
                    showLoading(false)
                    try {
                        val json = JSONObject(response)
                        val status = json.getString("status")

                        if (status == "success") {
                            val role = json.getString("role")
                            val nama = json.getString("nama")

                            if (role.equals("Admin", ignoreCase = true)) {
                                val intent = Intent(this, HomeAdminActivity::class.java)
                                intent.putExtra("userId", id)
                                intent.putExtra("userName", nama)
                                startActivity(intent)
                            } else {
                                val intent = Intent(this, HomeMemberActivity::class.java)
                                intent.putExtra("userId", id)
                                intent.putExtra("userName", nama)
                                startActivity(intent)
                            }

                            finish()

                        } else {
                            Toast.makeText(this, "ID atau password salah, Coba Lagi", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this, "Terjadi kesalahan saat parsing data, Coba Lagi", Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }
                },
                { error ->
                    showLoading(false)
                    Toast.makeText(this, "Terjadi kesalahan jaringan, Coba Lagi", Toast.LENGTH_SHORT).show()
                    error.printStackTrace()
                })

            queue.add(request)
        }
    }

    private fun showLoading(show: Boolean) {
        if (show) {
            progressBar.visibility = View.VISIBLE
            contentLayout.alpha = 0.5f
            idInput.isEnabled = false
            passwordInput.isEnabled = false
            loginBtn.isEnabled = false
        } else {
            progressBar.visibility = View.GONE
            contentLayout.alpha = 1f
            idInput.isEnabled = true
            passwordInput.isEnabled = true
            loginBtn.isEnabled = true
        }
    }
}
