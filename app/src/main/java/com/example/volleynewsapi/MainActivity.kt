package com.example.volleynewsapi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.retrofitnewsapi.Adaptar.NewsAdaptar
import com.example.retrofitnewsapi.model.Article
import com.google.gson.GsonBuilder
import com.littlemango.stacklayoutmanager.StackLayoutManager
import com.android.volley.VolleyError

import com.android.volley.RetryPolicy
import org.json.JSONObject
import java.lang.Exception
import java.util.HashMap


const val BASE_URL = "https://newsapi.org/"
const val API_KEY = "c92fcb9a055c41da855425cef2b26674"
const val url: String =
    "https://newsapi.org/v2/top-headlines?apikey=c92fcb9a055c41da855425cef2b26674&country=in&page=1"

class MainActivity : AppCompatActivity() {
    lateinit var adaptar: NewsAdaptar
    lateinit var mRecyclerView: RecyclerView
    private var mArticle = mutableListOf<Article>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        subscribePackage()
        mRecyclerView = findViewById(R.id.newsList)
        adaptar = NewsAdaptar(this@MainActivity, mArticle)
        mRecyclerView.adapter = adaptar
//        mRecyclerView.layoutManager=LinearLayoutManager(this@MainActivity)
        val layoutManager = StackLayoutManager(StackLayoutManager.ScrollOrientation.BOTTOM_TO_TOP)
        layoutManager.setPagerMode(true)
        layoutManager.setPagerFlingVelocity(3000)
        layoutManager.setItemChangedListener(object : StackLayoutManager.ItemChangedListener {
            override fun onItemChanged(position: Int) {
                val container = findViewById<ConstraintLayout>(R.id.container)

            }
        })
        mRecyclerView.layoutManager = layoutManager

//        getNews()
    }

    private fun getData() {
        val queue = Volley.newRequestQueue(this)
        val url1 = "v2/top-headlines?apikey=$API_KEY&country=in&page=1"

// Request a string response from the provided URL.
        val stringRequest = StringRequest(Request.Method.GET, url1,
            { response ->
                // Display the first 500 characters of the response string.
                Log.d("TAG", "getData: ${response.substring(0, 500)}")
            },
            {
                Log.d("TAG", "getData: That didn't work!")
            })
        stringRequest.retryPolicy = object : RetryPolicy {
            override fun getCurrentTimeout(): Int {
                return 500
            }

            override fun getCurrentRetryCount(): Int {
                return 500
            }

            @Throws(VolleyError::class)
            override fun retry(error: VolleyError) {
                Log.d("TAG", "retry: ")
            }
        }

// Add the request to the RequestQueue.
        queue.add(stringRequest)
    }

    private fun getNews() {
        val request = StringRequest(
            Request.Method.GET, url,
            { response ->
                Log.d("TAG", "Response: $response")

                /*val gsonBuilder = GsonBuilder()
                val gson = gsonBuilder.create()
                val users: List<Article>? = gson.fromJson(response, List<Article>::class.java)
                mArticle.addAll(users)
                adaptar.notifyDataSetChanged()*/
            },
            { volleyError ->
                Log.d("TAG", "Response: error:  ${volleyError.cause}")
            })
        Volley.newRequestQueue(this).add(request)
    }

    private fun subscribePackage() {
//        progressBar.setVisibility(View.VISIBLE);
        val JSON_URL = "https://newsapi.org/v2/top-headlines?apikey=c92fcb9a055c41da855425cef2b26674&country=in&page=1"
        try {
            val request: StringRequest = object : StringRequest(
                Method.POST, JSON_URL,
                Response.Listener { response ->
                    if (response != null) {
                        Log.d("TAG", "onResponse: paypal::$response")
                    }
                },
                Response.ErrorListener { error ->
                    Log.d("TAG", "onErrorResponse: " + error.message) }
            ) {
                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
            Volley.newRequestQueue(this).add(request)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }}
