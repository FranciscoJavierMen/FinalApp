package com.example.finalapp.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalapp.R
import com.example.finalapp.adapters.RatesAdapter
import com.example.finalapp.dialogs.RateDialog
import com.example.finalapp.models.Rate
import com.example.finalapp.models.RateEvent
import com.example.finalapp.utils.RxBus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_rates.view.*
import kotlinx.android.synthetic.main.rate_dialog.view.*
import java.util.*
import java.util.EventListener
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class RatesFragment : Fragment() {

    private lateinit var _view: View
    private lateinit var adapter: RatesAdapter
    private val ratesList: ArrayList<Rate> = ArrayList()

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var ratesDBReference: CollectionReference

    private var ratesSubscription: ListenerRegistration? = null
    private lateinit var rateBusListener: Disposable

    private lateinit var scrollListener: RecyclerView.OnScrollListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _view = inflater.inflate(R.layout.fragment_rates, container, false)

        setUpRecyclerView()
        setUpFab()
        setUpRatesDB()
        setUpCurrentUser()
        subscribeToRatings()
        subscribeToNewRatings()

        return _view
    }

    private fun setUpRatesDB(){
        ratesDBReference = db.collection("rates")
    }

    private fun setUpCurrentUser(){
        currentUser = mAuth.currentUser!!
    }

    private fun setUpRecyclerView(){
        val layoutManager = LinearLayoutManager(context)
        adapter = RatesAdapter(ratesList)
        _view.recyclerRates.setHasFixedSize(true)
        _view.recyclerRates.layoutManager = layoutManager
        _view.recyclerRates.adapter = adapter

        scrollListener = object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if(dy > 0 || dy < 0 && _view.fabRate.isShown){
                    //_view.fabRate.hide()
                    _view.fabRate.isExtended = false
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    //_view.fabRate.show()
                    _view.fabRate.isExtended = true
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        }

        _view.recyclerRates.addOnScrollListener(scrollListener)
    }

    private fun setUpFab(){
        _view.fabRate.setOnClickListener{RateDialog().show(fragmentManager!!, "")}
    }


    private fun saveRates(rate: Rate){
        val newRating = HashMap<String, Any>()
        newRating["text"] = rate.text
        newRating["rate"] = rate.rate
        newRating["createdAt"] = rate.createdAt
        newRating["profileImgUrl"] = rate.profileImgUrl

        ratesDBReference.add(newRating).addOnCompleteListener {
            Toast.makeText(context, "Rate added successfully", Toast.LENGTH_SHORT).show()
        }
            .addOnFailureListener {
                Toast.makeText(context, "Fail while rating, try again", Toast.LENGTH_SHORT).show()
            }
    }

    private fun subscribeToRatings(){
        ratesSubscription = ratesDBReference.orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener(object: EventListener, com.google.firebase.firestore.EventListener<QuerySnapshot> {
                override fun onEvent(
                    snapshot: QuerySnapshot?,
                    firebaseException: FirebaseFirestoreException?) {
                    firebaseException?.let {
                        Toast.makeText(context, "Exception", Toast.LENGTH_SHORT).show()
                        return
                    }

                    snapshot?.let {
                        ratesList.clear()
                        val rates = it.toObjects(Rate::class.java)
                        ratesList.addAll(rates)
                        adapter.notifyDataSetChanged()
                        _view.recyclerRates.smoothScrollToPosition(0)
                    }
                }
            })
    }

    private fun subscribeToNewRatings(){
        rateBusListener = RxBus.listen(RateEvent::class.java).subscribe {
            saveRates(it.rate)
        }
    }

    override fun onDestroyView() {
        rateBusListener.dispose()
        ratesSubscription?.remove()
        _view.recyclerRates.removeOnScrollListener(scrollListener)

        super.onDestroyView()
    }
}
