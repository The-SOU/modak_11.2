package com.modak.modaktestone.navigation.viewPager

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.modak.modaktestone.databinding.FragmentImageSlideBinding
import kotlinx.android.synthetic.main.fragment_image_slide.*
import kotlinx.android.synthetic.main.item_comment.view.*

class ImageSlideFragment(val image : Int) : Fragment() {
    private var _binding: FragmentImageSlideBinding? = null
    private val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        img_slide_image.setImageResource(image)
    }


}