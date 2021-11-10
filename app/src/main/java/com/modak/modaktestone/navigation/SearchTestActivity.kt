package com.modak.modaktestone.navigation

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import com.algolia.search.saas.*
import com.modak.modaktestone.databinding.ActivitySearchTestBinding
import org.json.JSONObject

class SearchTestActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchTestBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val client = Client("435FRQOQZV", "993b0e12c41c515fe18c3f75f2bdd874")
        val index: Index = client.getIndex("products")

        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                val query: Query = Query(s.toString())
                    .setAttributesToRetrieve("firstname")
                    .setHitsPerPage(50)
                index.searchAsync(query, object : CompletionHandler {
                    override fun requestCompleted(content: JSONObject?, error: AlgoliaException?) {
                        val hits = content!!.getJSONArray("hits")
                        var list: List<String> = ArrayList()
                        for (i in 0 until hits.length()) {
                            val jsonObject = hits.getJSONObject(i)
                            val firstname = jsonObject.getString("firstname")
                            list = listOf(firstname)
                        }
                        val arrayAdapter = ArrayAdapter(this@SearchTestActivity, R.layout.simple_list_item_1, list)
                        binding.listView.setAdapter(arrayAdapter)

                    }
                })
            }
        })
    }
}