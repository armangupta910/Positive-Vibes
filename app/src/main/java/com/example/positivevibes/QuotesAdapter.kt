package com.example.positivevibes

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext




class QuotesAdapter(private val context: Context, private val quotesList: MutableList<QuoteResponse>) :
    RecyclerView.Adapter<QuotesAdapter.QuoteViewHolder>() {

    private val firestore = FirebaseFirestore.getInstance()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.quote_item, parent, false)
        return QuoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val quote = quotesList[position]
        holder.bind(quote)
    }

    override fun getItemCount(): Int = quotesList.size

    inner class QuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val quoteTextView: TextView = itemView.findViewById(R.id.text_quote)
        private val authorTextView: TextView = itemView.findViewById(R.id.text_author)
        private val quoteShare: Button = itemView.findViewById(R.id.button_share)
        private val quoteSave: Button = itemView.findViewById(R.id.button_save)

        fun bind(quote: QuoteResponse) {
            quoteTextView.text = quote.quote
            authorTextView.text = "-" + quote.author

            // Share button logic
            quoteShare.setOnClickListener {
                // Create an Intent to share the quote text
                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "Hey there, I just found an amazing quote :- \"${quote.quote}\" - ${quote.author}")
                    type = "text/plain"
                }

                // Check if there is an app available to handle the share intent
                val chooser = Intent.createChooser(shareIntent, "Share Quote via")
                context.startActivity(chooser)
            }

            // Save button logic
            // Save button logic
            quoteSave.setOnClickListener {
                Toast.makeText(context,"Saving the Quote for you...",Toast.LENGTH_SHORT).show()
                // Retrieve the UID from SharedPreferences
                val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                val userId = sharedPreferences.getString("user_id", null)

                if (userId != null) {
                    // Prepare the quote data to save to Firestore as a map
                    val quoteData = hashMapOf(
                        "quote" to quote.quote,
                        "author" to quote.author
                    )

                    firestore.collection("saved")
                        .document(userId)  // Document named after the user UID
                        .get()  // Try to get the document first
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                // If the document exists, update the quotes array
                                firestore.collection("saved")
                                    .document(userId)
                                    .update(
                                        "quotes", FieldValue.arrayUnion(quoteData) // Append the quote to the 'quotes' array
                                    )
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "Quote saved successfully!", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(context, "Failed to save quote: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                // If the document does not exist, create it and add the first quote
                                val newQuoteData = hashMapOf(
                                    "quotes" to arrayListOf(quoteData)  // Create an array with the first quote
                                )

                                firestore.collection("saved")
                                    .document(userId)
                                    .set(newQuoteData)  // Create the document with the new array field
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "Quote saved successfully!", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(context, "Failed to save quote: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Failed to fetch document: ${e.message}", Toast.LENGTH_SHORT).show()
                        }



                } else {
                    Toast.makeText(context, "User ID not found. Please try again.", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }
}
