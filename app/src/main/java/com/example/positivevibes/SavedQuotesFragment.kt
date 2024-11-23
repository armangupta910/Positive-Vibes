package com.example.positivevibes

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class SavedQuotesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var firestore: FirebaseFirestore
    private lateinit var userId: String  // The user ID should be fetched from your app, e.g., FirebaseAuth
    private lateinit var no:TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_saved_quotes, container, false)
        recyclerView = view.findViewById(R.id.recycler_view_saved)
        no = view.findViewById(R.id.no)
        recyclerView.layoutManager = LinearLayoutManager(context)

        firestore = FirebaseFirestore.getInstance()

        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        userId = sharedPreferences.getString("user_id", null).toString()

        // Load saved quotes from Firestore
        loadSavedQuotes()

        return view
    }

    private fun loadSavedQuotes() {
        firestore.collection("saved")
            .document(userId)  // Get the user's document
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Get the quotes array from the document
                    val quotes = document.get("quotes") as? List<Map<String, String>> ?: emptyList()

                    // Convert it to a list of QuoteResponse objects
                    val quoteList = quotes.map { quote ->
                        QuoteResponse(
                            id = (quote["id"]?.toIntOrNull() ?: 0),  // Handle any potential errors with safe casting
                            quote = quote["quote"] ?: "",
                            author = quote["author"] ?: ""
                        )
                    }

                    if(quoteList.size == 0){
                        no.visibility = View.VISIBLE
                    }

                    // Set up the adapter
                    recyclerView.adapter = SavedQuotesAdapter(quoteList)
                }
            }
            .addOnFailureListener { e ->
                // Handle error
                no.visibility = View.VISIBLE
            }
    }
}
