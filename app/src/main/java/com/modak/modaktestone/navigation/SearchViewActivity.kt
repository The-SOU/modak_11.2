package com.modak.modaktestone.navigation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.algolia.search.saas.*
import com.modak.modaktestone.databinding.ActivitySearchViewBinding
import com.modak.modaktestone.databinding.ItemContentBinding
import com.modak.modaktestone.navigation.model.ContentDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONObject


class SearchViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchViewBinding

    var firestore: FirebaseFirestore? = null
    var auth: FirebaseAuth? = null
    var keyword: String? = null

    var listSecond: ArrayList<ContentDTO> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchViewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val client = Client("435FRQOQZV", "993b0e12c41c515fe18c3f75f2bdd874")
        val index: Index = client.getIndex("contents")


        //초기화
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()



        binding.searchEdittext.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == MotionEvent.ACTION_DOWN) {
                keyword = binding.searchEdittext.text.toString()

                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        binding.searchEdittext.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                val query: Query = Query(s.toString())
                    .setAttributesToRetrieve(
                        "title",
                        "explain",
                        "userName",
                        "uid",
                        "region",
                        "profileUrl",
                        "contentCategory",
                        "timestamp",
                        "imageUrl",
                        "favoriteCount",
                        "commentCount"
                    )
                    .setHitsPerPage(50)
                index.searchAsync(query, object : CompletionHandler {
                    override fun requestCompleted(content: JSONObject?, error: AlgoliaException?) {
                        val hits = content!!.getJSONArray("hits")
                        var list: List<String> = ArrayList()

                        listSecond.clear()
                        for (i in 0 until hits.length()) {
                            val jsonObject = hits.getJSONObject(i)
                            val titleItem = jsonObject.getString("title")
                            val explainItem = jsonObject.getString("explain")
//                            val uidItem = jsonObject.getString("uid")
                            val userNameItem = jsonObject.getString("userName")
//                            val regionItem = jsonObject.getString("region")
//                            val profileUrlItem = jsonObject.getString("profileUrl")
//                            val contentCategoryItem = jsonObject.getString("contentCategory")
//                            val timestampItem = jsonObject.getLong("timestamp")
//                            val imageUrl = jsonObject.getString("imageUrl")
                            val commentCountItem = jsonObject.getInt("commentCount")
                            val favoriteCountItem = jsonObject.getInt("favoriteCount")

                            var item = ContentDTO()

                            item.title = titleItem
                            item.explain = explainItem
//                            item.uid = uidItem
//                            item.userName = userNameItem
//                            item.region = regionItem
//                            item.profileUrl = profileUrlItem
//                            item.contentCategory = contentCategoryItem
//                            item.timestamp = timestampItem
//                            item.imageUrl = imageUrl
                            item.commentCount = commentCountItem
                            item.favoriteCount = favoriteCountItem
                            println("yes" + commentCountItem)
                            if (item != null) {
                                listSecond.add(item)
                            }
                        }
                        println("yes" + listSecond.size)
                        binding.searchRecyclerview.adapter = searchRecyclerViewAdapter(listSecond)
                        binding.searchRecyclerview.layoutManager =
                            LinearLayoutManager(this@SearchViewActivity)
                    }
                })
            }
        })


    }

    inner class searchRecyclerViewAdapter(list: ArrayList<ContentDTO>) :
        RecyclerView.Adapter<searchRecyclerViewAdapter.CustomViewHolder>() {
        var listTest = list

        //컨텐츠들 줄세우기.
        var contentUidList: ArrayList<String> = arrayListOf()


        init {
        }

        inner class CustomViewHolder(val binding: ItemContentBinding) :
            RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): searchRecyclerViewAdapter.CustomViewHolder {
            val binding =
                ItemContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CustomViewHolder(binding)
        }

        override fun onBindViewHolder(
            holder: searchRecyclerViewAdapter.CustomViewHolder,
            position: Int
        ) {


            holder.binding.contentTextviewTitle.text = listTest!![position].title
////
            holder.binding.contentTextviewExplain.text = listTest[position].explain

            holder.binding.contentTextviewUsername.text = listTest[position].userName

////
////
//            holder.binding.contentTextviewTimestamp.text =
//                SimpleDateFormat("MM/dd HH:mm").format(listTest[position].timestamp)
////
            holder.binding.contentTextviewCommentcount.text =
                listTest[position].commentCount.toString()
////
            holder.binding.contentTextviewFavoritecount.text =
                listTest[position].favoriteCount.toString()
//
//            //글을 클릭 했을 때
//            holder.binding.contentLinearLayout.setOnClickListener { v ->
//                var intent = Intent(v.context, DetailContentActivity::class.java)
//                if (contentDTOs[position].anonymity.containsKey(contentDTOs[position].uid)) {
//                    intent.putExtra("destinationUsername", "익명")
//                } else {
//                    intent.putExtra("destinationUsername", contentDTOs[position].userName)
//                }
//                if (contentDTOs[position].profileUrl != null) {
//                    intent.putExtra("destinationProfile", contentDTOs[position].profileUrl)
//                }
//                intent.putExtra("destinationTitle", contentDTOs[position].title)
//                intent.putExtra("destinationExplain", contentDTOs[position].explain)
//                intent.putExtra(
//                    "destinationTimestamp",
//                    SimpleDateFormat("MM/dd HH:mm").format(contentDTOs[position].timestamp)
//                )
//                intent.putExtra(
//                    "destinationCommentCount",
//                    contentDTOs[position].commentCount.toString()
//                )
//                intent.putExtra(
//                    "destinationFavoriteCount",
//                    contentDTOs[position].favoriteCount.toString()
//                )
//                intent.putExtra("destinationUid", contentDTOs[position].uid)
//                intent.putExtra("contentUid", contentUidList[position])
//                intent.putExtra("destinationImage", contentDTOs[position].imageUrl)
//                startActivity(intent)
//            }
        }

        override fun getItemCount(): Int {
            return listTest.size
        }
    }

    private fun search(keyword: String) {

    }


}

