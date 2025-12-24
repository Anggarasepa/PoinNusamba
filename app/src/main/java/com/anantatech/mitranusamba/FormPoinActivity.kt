package com.anantatech.mitranusamba

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class FormPoinActivity : AppCompatActivity() {

    private lateinit var etMemberId: EditText
    private lateinit var etJumlahPoin: EditText
    private lateinit var btnSubmitPoin: Button
    private lateinit var tvFormTitle: TextView

    private val URL_UPDATE = "https://script.google.com/macros/s/AKfycbyS5jD7z9XyPenBWnnt91JntEeOnZwyESkJ45_0f2RRnD6xFCaDSZfn8a580yr5c-79/exec"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_poin)

        etMemberId = findViewById(R.id.etMemberId)
        etJumlahPoin = findViewById(R.id.etJumlahPoin)
        btnSubmitPoin = findViewById(R.id.btnSubmitPoin)
        tvFormTitle = findViewById(R.id.tvFormTitle)

        val mode = intent.getStringExtra("mode") ?: "tambah"
        tvFormTitle.text = if (mode == "kurang") "Form Kurangi Poin" else "Form Tambah Poin"

        btnSubmitPoin.setOnClickListener {
            val id = etMemberId.text.toString().trim()
            val poinStr = etJumlahPoin.text.toString().trim()

            if (id.isEmpty() || poinStr.isEmpty()) {
                Toast.makeText(this, "Isi semua kolom!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val poin = poinStr.toInt()
            val finalPoin = if (mode == "kurang") -poin else poin

            kirimPoinKeServer(id, finalPoin)
        }
    }

    private fun kirimPoinKeServer(id: String, poin: Int) {
        val queue = Volley.newRequestQueue(this)
        val url = "$URL_UPDATE?action=update&id=$id&poin=$poin"

        val request = StringRequest(Request.Method.GET, url,
            { response ->
                Toast.makeText(this, "Berhasil: $response", Toast.LENGTH_LONG).show()
                finish()
            },
            { error ->
                Toast.makeText(this, "Gagal: ${error.message}", Toast.LENGTH_LONG).show()
                error.printStackTrace()
            })

        queue.add(request)
    }
}
