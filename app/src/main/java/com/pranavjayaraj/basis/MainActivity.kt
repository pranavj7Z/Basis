package com.pranavjayaraj.basis;

import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Adapter
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import com.yuyakaido.android.cardstackview.*
import org.json.JSONObject
import java.io.BufferedInputStream
import kotlin.collections.ArrayList
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

public class MainActivity : AppCompatActivity(), CardStackListener {

    private val cardStackView by lazy { findViewById<CardStackView>(R.id.card_stack_view) }
    private val manager by lazy { CardStackLayoutManager(this, this) }
    private val indicator by lazy { findViewById<TextView>(R.id.indi) }
    lateinit var cd: ConnectionDetector
    private lateinit var adapter : SwipeAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cd = ConnectionDetector()
        /**
        check for internet connection
         **/
            if (cd.isConnectingToInternet(this@MainActivity))
            {
                var spots = Parse().execute().get()
                adapter = SwipeAdapter(spots)
                setupCardStackView()
                setupButton()
            }
            else
            {
                val contentList = ArrayList<Content>()
                contentList.add(Content(data_url = "", id_url = "Please check your internet connection"))
                indicator.visibility = View.INVISIBLE
                adapter = SwipeAdapter(contentList)
                setupCardStackView()
            }
    }

    override fun onCardDragging(direction: Direction, ratio: Float) {
        Log.d("CardStackView", "onCardDragging: d = ${direction.name}, r = $ratio")
    }

    override fun onCardSwiped(direction: Direction) {
        Log.d("CardStackView", "onCardSwiped: p = ${manager.topPosition}, d = $direction")

        if (manager.topPosition == adapter.itemCount) {
            reload()
        }
    }

    override fun onCardRewound() {
        Log.d("CardStackView", "onCardRewound: ${manager.topPosition}")
    }

    override fun onCardCanceled() {
        Log.d("CardStackView", "onCardCanceled: ${manager.topPosition}")
    }

    override fun onCardAppeared(view: View, position: Int) {
        val textView = view.findViewById<TextView>(R.id.id)
        /**Indicator which indicates the current card*/
        indicator.setText((position+1).toString()+"/"+adapter.itemCount.toString())
        Log.d("CardStackView", "onCardAppeared: ($position) ${textView.text}")
    }

    override fun onCardDisappeared(view: View, position: Int) {
        val textView = view.findViewById<TextView>(R.id.data)
        Log.d("CardStackView", "onCardDisappeared: ($position) ${textView.text}")
    }


    private fun setupCardStackView() {
        initialize()
    }

/**
Function to setup the buttons for next,previous and reload options
 */

    private fun setupButton() {
        val previous = findViewById<View>(R.id.skip_button)
        previous.setOnClickListener {
                val setting = RewindAnimationSetting.Builder()
                        .setDirection(Direction.Left)
                        .setDuration(Duration.Normal.duration)
                        .setInterpolator(DecelerateInterpolator())
                        .build()
                manager.setRewindAnimationSetting(setting)
                cardStackView.rewind()
        }

        val reload = findViewById<View>(R.id.rewind_button)
        reload.setOnClickListener {
           reload()
        }

        val next = findViewById<View>(R.id.like_button)
        next.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                    .setDirection(Direction.Right)
                    .setDuration(Duration.Normal.duration)
                    .setInterpolator(AccelerateInterpolator())
                    .build()
            manager.setSwipeAnimationSetting(setting)
            cardStackView.swipe()
        }
    }

/**
Function to initialize the swipeable cardview
 */
    private fun initialize() {
        manager.setStackFrom(StackFrom.None)
        manager.setVisibleCount(3)
        manager.setTranslationInterval(8.0f)
        manager.setScaleInterval(0.95f)
        manager.setSwipeThreshold(0.3f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)
        manager.setCanScrollHorizontal(true)
        manager.setCanScrollVertical(true)
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        manager.setOverlayInterpolator(LinearInterpolator())
        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
        cardStackView.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }

}

/**
Function to start from first item when either end is reached or want to reload from the middle
 */
    private fun reload() {
        val old = adapter.getSpots()
        adapter.setSpots(old)
        adapter.notifyDataSetChanged()

    }
    /**
        Class which loads the data
     */
    class Parse : AsyncTask<Unit, Unit, List<Content>>() {
        /**
        Json URL
         */
        private val BASIS_URL = "https://gist.githubusercontent.com/anishbajpai014/d482191cb4fff429333c5ec64b38c197/raw/b11f56c3177a9ddc6649288c80a004e7df41e3b9/HiringTask.json"

        @RequiresApi(Build.VERSION_CODES.KITKAT)
        override fun doInBackground(vararg params: Unit?): List<Content>? {
            val contentList = ArrayList<Content>()
            val url = URL(BASIS_URL)
            val httpClient = url.openConnection() as HttpURLConnection
            if (httpClient.responseCode == HttpURLConnection.HTTP_OK) {
                try {
                    val stream = BufferedInputStream(httpClient.inputStream)
                    val data: String = readStream(inputStream = stream)
                    /**
                    Replace all the occurences of "/" from the json
                     */
                    val jsonFormattedString = data.replace("/", "")
                    val jsonObject = JSONObject(jsonFormattedString)
                    val jsonArray = jsonObject.getJSONArray("data")
                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        val newVideo = Content(data_url = item.getString("text"), id_url = item.getString("id"))
                        contentList.add(newVideo)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    httpClient.disconnect()
                }
            } else {
                println("ERROR ${httpClient.responseCode}")
                contentList.add(Content(data_url = "", id_url = "Please check your internet connection"))

            }
            return contentList
        }

        /**
        Function to concatinate into string
         **/
        private fun readStream(inputStream: BufferedInputStream): String {
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            bufferedReader.forEachLine { stringBuilder.append(it) }
            return stringBuilder.toString()
        }

    }
}
