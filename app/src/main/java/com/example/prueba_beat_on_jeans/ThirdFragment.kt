package com.example.prueba_beat_on_jeans


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ThirdFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class ThirdFragment : Fragment() {
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

    private var chatList = mutableListOf<Chat>()
    private var chatRVList = mutableListOf<ChatRV>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_third, container, false)
        getChats(view)
        return view
    }

    private fun getChats(view: View) {
        lifecycleScope.launch {
            try {
                val chats = if(MainActivity.UserSession.rolId == 1){
                    RetrofitClient.instance.getMusicChats(MainActivity.UserSession.id!!)
                }else{
                    RetrofitClient.instance.getLocalChats(MainActivity.UserSession.id!!)
                }
                if(chats.size != 0){
                    chatList = chats
                    when(MainActivity.UserSession.rolId!!){
                        1 ->{
                            chatList.forEach{ chat ->
                                val user = RetrofitClient.instance.getUser(chat.local_ID)
                                if(chat.mensajes.size != 0){
                                    val newChat = ChatRV(chat.id,user.nombre,chat.mensajes.last().mensaje,
                                        chat.mensajes.last().hora,false,R.drawable.hugo)
                                    chatRVList.add(newChat)
                                }else{
                                    val newChat = ChatRV(chat.id,user.nombre,"",
                                        "",false,R.drawable.hugo)
                                    chatRVList.add(newChat)
                                }
                            }
                        }
                        2 ->{
                            chatList.forEach{ chat ->
                                val user = RetrofitClient.instance.getUser(chat.musico_ID)
                                if(chat.mensajes.size != 0){
                                    val newChat = ChatRV(chat.id,user.nombre,chat.mensajes.last().mensaje,
                                        chat.mensajes[chat.mensajes.size-1].hora,false,R.drawable.hugo)
                                    chatRVList.add(newChat)
                                }else{
                                    val newChat = ChatRV(chat.id,user.nombre,"",
                                        "",false,R.drawable.hugo)
                                    chatRVList.add(newChat)
                                }
                            }
                        }
                    }
                    setChats(view)
                    setImages(view)
                }else{
                    setEmptyView(view)
                }

            }catch (e: Exception) {
                Log.e("API_ERROR", "Error: ${e.message}", e)
                Toast.makeText(
                    context,
                    "Error al conectar con el servidor",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }
    }

    private fun setEmptyView(view: View) {

    }


    private fun setChats(view: View) {
        val rvChats = view.findViewById<RecyclerView>(R.id.RVChats)
        val adapter = ChatsAdapter(chatRVList){ chat ->
            val intent = Intent(context,ChatActivity::class.java)
            intent.putExtra(ChatActivity.ChatInfo.CHATINFO,chat)
            startActivity(intent)
        }

        rvChats.adapter = adapter
        rvChats.layoutManager = GridLayoutManager(context,1)
    }

    private fun setImages(view: View) {
        val rvChats = view.findViewById<RecyclerView>(R.id.RVMaches)
        val adapter = ImgChatsAdapter(chatRVList)
        rvChats.adapter = adapter
        rvChats.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ThirdFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ThirdFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}