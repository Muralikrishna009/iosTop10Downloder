package com.example.top10downloder

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

private const val TAG = "FeedAdapter"

class ViewHolder( v: View) {
    val tvName: TextView = v.findViewById(R.id.tvName)
    val tvArtist: TextView = v.findViewById(R.id.tvArtist)
    val tvImage: ImageView = v.findViewById(R.id.tvImage)
    val tvSummary : TextView = v.findViewById(R.id.tvSummery)
}


class FeedAdapter(context: Context, private val resource: Int, private val applications: List<FeedEntry>)
    : ArrayAdapter<FeedEntry>(context, resource) {

    private val inflater = LayoutInflater.from(context)
    override fun getCount(): Int {
        return applications.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val viewHolder : ViewHolder
        val view : View
        if(convertView == null){
            Log.d(TAG, "View Created")
            view = inflater.inflate(resource, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder

        } else{
            Log.d(TAG, "View used")
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

//        val tvName : TextView = view.findViewById(R.id.tvName)
//        val tvArtist : TextView = view.findViewById(R.id.tvArtist)
//        //val tvSummery : TextView = view.findViewById(R.id.tvSummery)
//        val tvImage : ImageView = view.findViewById(R.id.tvImage)
//
        val currentApp = applications[position]

        viewHolder.tvName.text = currentApp.name
        viewHolder.tvArtist.text = currentApp.artist
        viewHolder.tvSummary.text = currentApp.summery

        Glide.with(context)
            .load(currentApp.imageUrl)
            .into(viewHolder.tvImage)

        return view
    }
}