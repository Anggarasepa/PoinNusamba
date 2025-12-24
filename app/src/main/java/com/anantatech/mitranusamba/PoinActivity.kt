package com.anantatech.mitranusamba

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class PoinActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var searchInput: EditText
    private lateinit var poinList: ArrayList<JSONObject>
    private lateinit var adapter: PoinAdapter

    private lateinit var progressBar: ProgressBar
    private lateinit var contentLayout: LinearLayout

    private val URL_API =
        "https://script.google.com/macros/s/AKfycbyS5jD7z9XyPenBWnnt91JntEeOnZwyESkJ45_0f2RRnD6xFCaDSZfn8a580yr5c-79/exec"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poin)

        listView = findViewById(R.id.listView)
        searchInput = findViewById(R.id.searchInput)

        progressBar = findViewById(R.id.progressBar)
        contentLayout = findViewById(R.id.contentLayout)

        poinList = ArrayList()
        adapter = PoinAdapter(this, poinList)
        listView.adapter = adapter

        loadPoinData()

        searchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().lowercase()
                val filtered = poinList.filter {
                    it.getString("ID").lowercase().contains(query) ||
                            it.getString("Nama").lowercase().contains(query)
                }
                adapter.updateData(ArrayList(filtered))
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun loadPoinData() {
        showLoading(true)

        val queue = Volley.newRequestQueue(this)

        val request = JsonArrayRequest(Request.Method.GET, URL_API, null,
            { response ->
                poinList.clear()
                for (i in 0 until response.length()) {
                    val item = response.getJSONObject(i)
                    if (item.getString("ID").isNotBlank()) {
                        poinList.add(item)
                    }
                }
                adapter.updateData(poinList)
                showLoading(false)
            },
            { error ->
                showLoading(false)
                Toast.makeText(this, "Gagal mengambil data, Coba Lagi", Toast.LENGTH_SHORT).show()
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

    class PoinAdapter(
        private val context: PoinActivity,
        private var data: ArrayList<JSONObject>
    ) : BaseAdapter() {

        override fun getCount(): Int = data.size
        override fun getItem(position: Int): Any = data[position]
        override fun getItemId(position: Int): Long = position.toLong()

        fun updateData(newData: ArrayList<JSONObject>) {
            data = newData
            notifyDataSetChanged()
        }

        override fun getView(
            position: Int,
            convertView: android.view.View?,
            parent: android.view.ViewGroup?
        ): android.view.View {
            val view =
                convertView ?: context.layoutInflater.inflate(R.layout.item_poin, parent, false)

            val idView = view.findViewById<TextView>(R.id.colId)
            val namaView = view.findViewById<TextView>(R.id.colNama)
            val poinView = view.findViewById<TextView>(R.id.colPoin)

            val item = data[position]

            idView.text = item.getString("ID")
            namaView.text = item.getString("Nama")
            poinView.text = item.getString("Poin")

            return view
        }
    }
}
