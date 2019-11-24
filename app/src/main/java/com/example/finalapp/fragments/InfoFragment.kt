package com.example.finalapp.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.finalapp.R
import com.example.finalapp.utils.CircleTransform
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_info.view.*
import kotlinx.android.synthetic.main.item_chat_right.view.*

class InfoFragment : Fragment() {

    private lateinit var _view: View
    //Variables para la instancia de la base de datos de firebase
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var chatDBReference: CollectionReference
    //Variables del usuario logueado
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser

    private var chatSubscription: ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _view =  inflater.inflate(R.layout.fragment_info, container, false)

        setUpChatDB()
        setUpCurrenuser()
        setupCurrentUserInformationUI()

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

    private fun setupCurrentUserInformationUI() {
        _view.txtInfoEmail.text = currentUser.email
        _view.textView4.text = currentUser.displayName?.let { currentUser.displayName } ?: run {getString(R.string.text_no_name)}
        currentUser.photoUrl?.let {
            Picasso.get().load(currentUser.photoUrl)
                .resize(300, 300)
                .centerCrop()
                .transform(CircleTransform())
                .into(_view.imgInfo)
        } ?: run {
            Picasso.get().load(R.drawable.ic_person)
                .resize(300, 300)
                .centerCrop()
                .transform(CircleTransform())
                .into(_view.imgInfo)
        }

    }

}
