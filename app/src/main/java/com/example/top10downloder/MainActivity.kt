package com.example.top10downloder

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL
import java.util.concurrent.Executors

private const val TAG = "MainActivity"

class FeedEntry{
    var name : String = ""
    var artist : String = ""
    var releaseDate : String = ""
    var imageUrl : String = ""
    var summery : String = ""
    override fun toString(): String {
        return """
            name = $name
            artist = $artist
            releaseDate = $releaseDate
            imageUrl = $imageUrl
            summery = $summery
        """.trimIndent()
    }
}

class MainActivity : AppCompatActivity() {
    private var urlLimit = 10
    private var feedUrl : String = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml"
    private var oldUrl : String = "Invalid"
    private var oldLimit : Int = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "OnCreate Called")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        downloadData(feedUrl.format(urlLimit))

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.feeds_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.mnuFree ->
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml"
            R.id.mnuPaid ->
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=%d/xml"
            R.id.mnuSongs ->
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=%d/xml"
            R.id.mnuLimit10, R.id.mnuLimit20 -> {
                if(!item.isChecked){
                    item.isChecked = true
                    urlLimit = 35 - urlLimit
                }
            }
            else -> return super.onOptionsItemSelected(item)
        }
        if(oldUrl != feedUrl || oldLimit != urlLimit){
            downloadData(feedUrl.format(urlLimit))
            oldUrl = feedUrl
            oldLimit = urlLimit
        }
        return true
    }

    private fun downloadData(urlPath: String){
        val myExecutor = Executors.newSingleThreadExecutor()
        val myHandler = Handler(Looper.getMainLooper())

        myExecutor.execute{
            Log.d(TAG, "Running in BackGround")
            val rssFeed : String= downloadXML(urlPath)
            if(rssFeed.isEmpty()){
                Log.e(TAG, "Execute: connection failed ")
            }
            //Log.d(TAG, "Rss Feed Data : $rssFeed")

            myHandler.post{
                Log.d(TAG, "Post Execution : Handler ")
                val parseapplications = ParseApplications()
                parseapplications.parse(rssFeed)

//                val arrayAdapter = ArrayAdapter<FeedEntry>(this, R.layout.list_item, parseapplications.applications)
//                xmlListView.adapter = arrayAdapter
                val feedAdapter = FeedAdapter(this, R.layout.list_record, parseapplications.applications)
                xmlListView.adapter = feedAdapter
            }
        }
    }

    private fun downloadXML(urlPath: String) : String{
        return URL(urlPath).readText()
//        try {
//            val xmlResult = StringBuilder()
//
//            val url = URL(urlPath)
//            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
//            val response = connection.responseCode
//            Log.d(TAG, "response code is : $response")
//
//            val inputStream = connection.inputStream
//            val inputStreamReader = InputStreamReader(inputStream)
//            val reader = BufferedReader(inputStreamReader)
//            val reader = BufferedReader(InputStreamReader(connection.inputStream))
//
//            val inputBuffer = CharArray(500)
//            var charsRead = 0
//            while(charsRead >= 0 ){
//                charsRead = reader.read(inputBuffer)
//                if(charsRead > 0){
//                    xmlResult.append(String(inputBuffer, 0, charsRead))
//                }
//            }
//
//
//            connection.inputStream.buffered().reader().use { xmlResult.append(it.readText())  }
//
//            return  xmlResult.toString()
//        } catch (e : MalformedURLException){
//            Log.e(TAG, "Invalid URL : ${e.message}")
//        } catch (e : IOException) {
//            Log.e(TAG, "download xml error : ${e.message}")
//        } catch (e : Exception) {
//            Log.e(TAG, "Error in Download Xml: ${e.message}")
//        }
//
//        return ""

    }


}

