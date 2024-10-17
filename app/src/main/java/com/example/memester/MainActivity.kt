package com.example.memester

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {


    private var memeLink: String? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val shareMeme: Button = findViewById(R.id.shareBtn)
        val nextMeme: Button = findViewById(R.id.nextBtn)
        val memeView: ImageView = findViewById(R.id.memeView)
        val loadingBar: ProgressBar = findViewById(R.id.loadingBar)


        fun loadMeme() {

            loadingBar.visibility = View.VISIBLE
            val url = "https://meme-api.com/gimme"

            // Request a string response from the provided URL.
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->
                    memeLink = response.getString("url")
                    Glide.with(this).load(memeLink).listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>,
                            isFirstResource: Boolean
                        ): Boolean {
                            loadingBar.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any,
                            target: Target<Drawable>?,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            loadingBar.visibility = View.GONE
                            return false
                        }

                    }).into(memeView)
                },
                {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
                })

            // Add the request to the RequestQueue.
            MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
        }

        shareMeme.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plane"
            intent.putExtra(Intent.EXTRA_TEXT,"Yo Dawg check this out $memeLink")
            val chooser = Intent.createChooser(intent, "Share this meme using...")
            startActivity(chooser)
        }

        nextMeme.setOnClickListener {
            loadMeme()
        }
    }

}