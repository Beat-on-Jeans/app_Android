package com.example.prueba_beat_on_jeans.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prueba_beat_on_jeans.R
import com.example.prueba_beat_on_jeans.activities.SettingsActivity
import com.example.prueba_beat_on_jeans.classes.Tag
import com.example.prueba_beat_on_jeans.adapters.TagsAcountAdapter
import com.example.prueba_beat_on_jeans.activities.MainActivity
import com.example.prueba_beat_on_jeans.adapters.ImgAcountAdapter

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class FourthFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fourth, container, false)
        setAccount(view)
        return view

    }

    private fun setAccount(view: View) {
        val rcAcImgBac = view.findViewById<RecyclerView>(R.id.RVImgsAccount)
        val txtAcName = view.findViewById<TextView>(R.id.TxtAccountName)
        val rvAcTags = view.findViewById<RecyclerView>(R.id.RVTagsAccount)
        val txtAcDesc = view.findViewById<TextView>(R.id.TxtAccountDescription)
        val rvAcEven = view.findViewById<RecyclerView>(R.id.RVEvents)

        val adapterImgs = ImgAcountAdapter(MainActivity.UserSession.urlImg.toString(),
            R.drawable.background
        )
        rcAcImgBac.adapter = adapterImgs
        rcAcImgBac.layoutManager = LinearLayoutManager(context)



        txtAcName.text = MainActivity.UserSession.username

        val adapterTags = TagsAcountAdapter(setBetaStats())
        rvAcTags.adapter = adapterTags
        rvAcTags.layoutManager = GridLayoutManager(context,4)

        txtAcDesc.text = "Capturing killer fashion shots by day, rocking out at concerts by night. Up for grabbing coffee and seeing if we vibe?"

        val btnSetting = view.findViewById<ImageButton>(R.id.BtnSettings)

        btnSetting.setOnClickListener{
            val intent = Intent(context, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setBetaStats(): List<Tag> {
        return listOf(
            Tag(1,"Clásica"),
            Tag(2,"Blues"),
            Tag(3,"Metal"),
        )
    }
}