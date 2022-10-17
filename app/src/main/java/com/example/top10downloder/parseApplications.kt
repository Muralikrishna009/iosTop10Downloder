package com.example.top10downloder

import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "parseApplications"

class ParseApplications {
    val applications = ArrayList<FeedEntry>()

    fun parse(xmlData : String) : Boolean {
        Log.d(TAG, "Parse called with XML data")
        var status = true
        var inEntry = false
        var textValue = ""

        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val xpp = factory.newPullParser()
            xpp.setInput(xmlData.reader())
            var eventType = xpp.eventType
            var currentRecord = FeedEntry()

            while( eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = xpp.name?.lowercase(Locale.getDefault())

                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        //Log.d(TAG, "Parse : Start tag for -> $tagName")
                        if (tagName == "entry") {
                            inEntry = true
                        }
                    }

                    XmlPullParser.TEXT -> textValue = xpp.text

                    XmlPullParser.END_TAG -> {
                       // Log.d(TAG, "Parse : Ending Tag for -> $tagName")
                        if (inEntry) {
                            when (tagName) {
                                "entry" -> {
                                    applications.add(currentRecord)
                                    inEntry = false
                                    currentRecord = FeedEntry()
                                }
                                "name" -> currentRecord.name = textValue
                                "artist" -> currentRecord.artist = textValue
                                "releasedate" -> currentRecord.releaseDate = textValue
                                "summary" -> currentRecord.summery = textValue
                                "image" -> currentRecord.imageUrl = textValue
                            }
                        }
                    }
                }
                eventType = xpp.next()
            }
//            for(app in applications){
//                Log.d(TAG, "****************************")
//                Log.d(TAG, app.toString())
//            }
        } catch (e : Exception){
            e.printStackTrace()
            status = false
        }
        return status
    }
}