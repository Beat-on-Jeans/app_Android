package com.example.prueba_beat_on_jeans

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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FourthFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FourthFragment : Fragment() {
    // TODO: Rename and change types of parameters
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

        val adapterImgs = ImgAcountAdapter(R.drawable.trumpet_man_img,R.drawable.trumpet_img)
        rcAcImgBac.adapter = adapterImgs
        rcAcImgBac.layoutManager = LinearLayoutManager(context)

        txtAcName.text = MainActivity.UserSession.username

        val adapterTags = TagsAcountAdapter(setBetaStats())
        rvAcTags.adapter = adapterTags
        rvAcTags.layoutManager = GridLayoutManager(context,4)

        txtAcDesc.text = "Capturing killer fashion shots by day, rocking out at concerts by night. Up for grabbing coffee and seeing if we vibe?"

        val btnSetting = view.findViewById<ImageButton>(R.id.BtnSettings)

        btnSetting.setOnClickListener{
            val intent = Intent(context,SettingsActivity::class.java)
            startActivity(intent)
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FourthFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FourthFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun setBetaStats(): List<Tag> {
        return listOf(Tag(1,"Clásica"),
                      Tag(2,"Blues"),
                      Tag(3,"R&B"),
                      Tag(4,"Metal"),
                      Tag(5,"Rock"),
                      Tag(6,"Harcore"),
                      Tag(7,"Pop"),
                      Tag(8,"Electro Swing")
            )
    }
}