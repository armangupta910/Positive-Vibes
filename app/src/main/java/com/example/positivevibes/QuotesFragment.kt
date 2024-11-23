package com.example.positivevibes

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class QuotesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private var quotesList = mutableListOf<QuoteResponse>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_quotes, container, false)
        recyclerView = view.findViewById(R.id.recycler_view_quotes)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Fetch quotes from API
        fetchQuotes()

        return view
    }

    private fun fetchQuotes() {
        // Show a loading indicator or progress bar if needed

        RetrofitClient.instance.getQuotes().enqueue(object : Callback<QuotesResponse> {
            override fun onResponse(call: Call<QuotesResponse>, response: Response<QuotesResponse>) {
                if (response.isSuccessful) {
                    // Get the list of quotes from the response body
                    quotesList = (response.body()?.quotes ?: emptyList()).toMutableList()

                    // Initialize or update the RecyclerView adapter with the quotes list
                    recyclerView.adapter = QuotesAdapter(requireContext(), quotesList)
                } else {
                    // Handle the error case (e.g., show a message)
                    Toast.makeText(requireContext(), "Failed to fetch quotes", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<QuotesResponse>, t: Throwable) {
                // Handle failure (e.g., network error)
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }



    private fun onShareQuote(quote: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, quote)
        startActivity(Intent.createChooser(shareIntent, "Share Quote via"))
    }
}