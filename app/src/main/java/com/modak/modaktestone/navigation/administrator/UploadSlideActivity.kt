package com.modak.modaktestone.navigation.administrator

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.SpinnerAdapter
import android.widget.Toast
import com.modak.modaktestone.R
import com.modak.modaktestone.databinding.ActivityUploadSlideBinding
import com.modak.modaktestone.navigation.model.SlideDTO
import com.modak.modaktestone.navigation.model.SpinnerModel
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList

class UploadSlideActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadSlideBinding

    var firestore: FirebaseFirestore? = null

    private lateinit var spinnerAdapterRegion: SpinnerAdapter
    private lateinit var spinnerAdapterType: SpinnerAdapter

    private val listOfRegion = ArrayList<SpinnerModel>()
    private val listOfType = ArrayList<SpinnerModel>()

    var region: String? = null
    var kind: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadSlideBinding.inflate(layoutInflater)
        val view = binding.root

        firestore = FirebaseFirestore.getInstance()

        //키보드 숨기기
        binding.layout.setOnClickListener {
            hideKeyboard()
        }

        setupSpinnerRegion()
        setupSpinnerType()
        setupSpinnerHandler()

        binding.uploadSlideBtn.setOnClickListener {
            var slideDTO = SlideDTO()

            slideDTO.title = binding.uploadSlideTitle.text.toString()
            slideDTO.explain = binding.uploadSlideExplain.text.toString()
            slideDTO.url = binding.uploadSlideUrl.text.toString()
            slideDTO.region = region
            slideDTO.kind = kind
            slideDTO.timestamp = System.currentTimeMillis()

            firestore?.collection("slides")?.add(slideDTO)
                ?.addOnSuccessListener { documentReference ->
                    Log.d(
                        "TAG",
                        "DocumentSnapshot written with ID: ${documentReference.id}"
                    )
                    Toast.makeText(this, "입력완료", Toast.LENGTH_SHORT).show()
                }?.addOnFailureListener { e ->
                Log.w("TAG", "Error adding document", e)
            }
            finish()
        }


        setContentView(view)
    }

    private fun setupSpinnerRegion() {
        var regionDatas = listOf(
            "지역",
            "서울",
            "부산",
            "대구",
            "인천",
            "광주",
            "대전",
            "울산",
            "경기",
            "강원",
            "충북",
            "충남",
            "전북",
            "전남",
            "경북",
            "경남",
            "제주"
        )

        for (i in regionDatas.indices) {
            val item = SpinnerModel(regionDatas[i])
            listOfRegion.add(item)
        }
        spinnerAdapterRegion =
            com.modak.modaktestone.navigation.spinner.SpinnerAdapter(
                this,
                R.layout.item_spinner,
                listOfRegion
            )
        binding.uploadSlideRegion.adapter = spinnerAdapterRegion
    }

    private fun setupSpinnerType() {
        var regionDatas = listOf(
            "이벤트",
            "복지 정보"
        )

        for (i in regionDatas.indices) {
            val item = SpinnerModel(regionDatas[i])
            listOfType.add(item)
        }
        spinnerAdapterType =
            com.modak.modaktestone.navigation.spinner.SpinnerAdapter(
                this,
                R.layout.item_spinner,
                listOfType
            )
        binding.uploadSlideType.adapter = spinnerAdapterType
    }

    private fun setupSpinnerHandler() {
        binding.uploadSlideRegion.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val item =
                        binding.uploadSlideRegion.getItemAtPosition(position) as SpinnerModel
                    region = item.name
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        binding.uploadSlideType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val item =
                        binding.uploadSlideType.getItemAtPosition(position) as SpinnerModel
                    when (item.name) {
                        "이벤트" -> {
                            kind = 1
                        }
                        "복지 정보" -> {
                            kind = 2
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}

            }
    }

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}