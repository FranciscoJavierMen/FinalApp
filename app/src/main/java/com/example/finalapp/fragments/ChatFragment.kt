package com.example.finalapp.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalapp.R
import com.example.finalapp.adapters.ChatAdapter
import com.example.finalapp.models.Messages
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_chat.view.*
import java.util.*
import java.util.EventListener
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChatFragment : Fragment() {

    private lateinit var _view: View
    //Variables para la instancia de la base de datos de firebase
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var chatDBReference:  CollectionReference
    //Variables del usuario logueado
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser
    //Adaptador del chat para el recycler
    private lateinit var adapter: ChatAdapter
    private val messageList: ArrayList<Messages> = ArrayList()

    private var chatSubscription: ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        _view = inflater.inflate(R.layout.fragment_chat, container, false)

        setUpChatDB()
        setUpCurrenuser()
        setUpRecyclerView()
        setUpChatBtn()
        subscribeToChatMessages()
        return _view
    }

    //Configurando la base de datos
    private fun setUpChatDB() {
        chatDBReference = db.collection("chat")
    }
    //Obteniendo el usuario logueado
    private fun setUpCurrenuser() {
        currentUser = mAuth.currentUser!!
    }
    //Estableciendo el recyclerview
    private fun setUpRecyclerView() {
        var layoutManager = LinearLayoutManager(context)
        adapter = ChatAdapter(messageList, currentUser.uid)
        _view.recyclerChat.setHasFixedSize(true)
        _view.recyclerChat.layoutManager = layoutManager
        _view.recyclerChat.itemAnimator = DefaultItemAnimator()
        _view.recyclerChat.adapter = adapter
    }
    //Evento del botón para enviar el mensaje
    private fun setUpChatBtn() {
        _view.fabSendChat.setOnClickListener{
            val messageText = editText.text.toString()
            if(messageText.isNotEmpty()){
                val message = Messages(currentUser.uid, messageText, currentUser.photoUrl.toString(), Date())
                saveMessage(message)
                _view.editText.setText("")
            }
        }
    }
    //Guardar mensajes en FireStore
    private fun saveMessage(message: Messages){
        val newMessage = HashMap<String, Any>()
        newMessage["authorId"] = message.authorId
        newMessage["message"] = message.message
        newMessage["profileImageURL"] = message.profileImageURL
        newMessage["sentAt"] = message.sentAt

        chatDBReference.add(newMessage)
            .addOnCompleteListener {

            }
            .addOnFailureListener{
                Toast.makeText(context, "Fail when try to save the message", Toast.LENGTH_SHORT).show()
            }
    }
    //Subscribirse a los cambios del chat
    private fun subscribeToChatMessages(){
        chatSubscription = chatDBReference
            .orderBy("sentAt", Query.Direction.DESCENDING)
            .limit(100)
            .addSnapshotListener(object: EventListener, com.google.firebase.firestore.EventListener<QuerySnapshot>{
            override fun onEvent(snapshot: QuerySnapshot?, firebaseException: FirebaseFirestoreException?) {
                firebaseException?.let {
                    Toast.makeText(context, "Exception", Toast.LENGTH_SHORT).show()
                    return
                }

                snapshot?.let {
                    messageList.clear()
                    val messages = it.toObjects(Messages::class.java)

                    messageList.addAll(messages.asReversed())
                    adapter.notifyDataSetChanged()
                    _view.recyclerChat.smoothScrollToPosition(messageList.size)
                }
            }

        })
    }

    override fun onDestroy() {
        chatSubscription?.remove()
        super.onDestroy()
    }
}
