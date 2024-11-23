package com.example.positivevibes

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SavedQuotesAdapter(private val quotesList: List<QuoteResponse>) : RecyclerView.Adapter<SavedQuotesAdapter.SavedQuoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedQuoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.quote_item, parent, false)
        return SavedQuoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: SavedQuoteViewHolder, position: Int) {
        val quote = quotesList[position]
        holder.bind(quote)
    }

    override fun getItemCount(): Int = quotesList.size

    inner class SavedQuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val quoteTextView: TextView = itemView.findViewById(R.id.text_quote)
        private val shareButton: Button = itemView.findViewById(R.id.button_share)
        private val authorTextView :TextView = itemView.findViewById(R.id.text_author)
        private val saveButton: Button = itemView.findViewById(R.id.button_save)

        fun bind(quote: QuoteResponse) {
            quoteTextView.text = quote.quote
            authorTextView.text = "-" + quote.author

            // Hide the "Save" button
            saveButton.visibility = View.GONE

            // Share button logic
            shareButton.setOnClickListener {
                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "Hey there, I just found an amazing quote :- \"${quote.quote}\" - ${quote.author}")
                    type = "text/plain"
                }

                val chooser = Intent.createChooser(shareIntent, "Share Quote via")
                itemView.context.startActivity(chooser)
            }
        }
    }
}
