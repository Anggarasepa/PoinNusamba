package com.anantatech.mitranusamba

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class AdminPoinActivity : AppCompatActivity() {

    private lateinit var editTextId: EditText
    private lateinit var editTextJumlah: EditText
    private lateinit var btnTambah: Button
    private lateinit var btnKurangi: Button
    private lateinit var statusText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_poin)

        editTextId = findViewById(R.id.editTextId)
        editTextJumlah = findViewById(R.id.editTextJumlah)
        btnTambah = findViewById(R.id.btnTambah)
        btnKurangi = findViewById(R.id.btnKurangi)
        statusText = findViewById(R.id.statusText)

        btnTambah.setOnClickListener {
            val id = editTextId.text.toString()
            val jumlah = editTextJumlah.text.toString().toIntOrNull() ?: 0
            if (id.isNotEmpty() && jumlah > 0) {
                updatePoinKeServer(id, jumlah)
            } else {
                Toast.makeText(this, "Isi ID dan jumlah dengan benar", Toast.LENGTH_SHORT).show()
            }
        }

        btnKurangi.setOnClickListener {
            val id = editTextId.text.toString()
            val jumlah = editTextJumlah.text.toString().toIntOrNull() ?: 0
            if (id.isNotEmpty() && jumlah > 0) {
                updatePoinKeServer(id, -jumlah)
            } else {
                Toast.makeText(this, "Isi ID dan jumlah dengan benar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updatePoinKeServer(id: String, perubahan: Int) {
        val queue = Volley.newRequestQueue(this)
        val url = "https://script.google.com/macros/s/AKfycbyS5jD7z9XyPenBWnnt91JntEeOnZwyESkJ45_0f2RRnD6xFCaDSZfn8a580yr5c-79/exec"

        val jsonBody = JSONObject()
        jsonBody.put("ID", id)
        jsonBody.put("Change", perubahan)

        val request = object : StringRequest(
            Method.POST, url,
            { response ->
                Toast.makeText(this, "Poin berhasil diperbarui", Toast.LENGTH_SHORT).show()
                statusText.text = "✅ Poin berhasil diperbarui"
                editTextId.text.clear()
                editTextJumlah.text.clear()
            },
            { error ->
                Toast.makeText(this, "Gagal mengupdate poin", Toast.LENGTH_SHORT).show()
                statusText.text = "❌ Gagal memperbarui poin"
                error.printStackTrace()
            }
        ) {
            override fun getBodyContentType(): String = "application/json; charset=utf-8"
            override fun getBody(): ByteArray = jsonBody.toString().toByteArray()
        }

        queue.add(request)
    }
}
