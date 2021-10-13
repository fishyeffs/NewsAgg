package com.example.newsagg

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.news_layout.view.*
import java.io.File
import java.lang.Exception

class MyAdapter (private val newsItemList: MutableList<DataStore>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.news_layout, parent, false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return newsItemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = newsItemList[position]

        try {
            Glide.with(holder.itemView).load(newsItemList[position].getImgUrl()).into(
                holder.imgView
            )
        }
        catch (e : Exception) {
            holder.imgView.tag = info.getThumbnail()
        }

        holder.headlineMsg.text = info.getNames()
        val time = info.getPublished().substring(11,19)
        val date = info.getPublished().substring(0,10)
        holder.publish.text = "$time, $date"   //10
        holder.descMsg.text = info.getDesc()
        holder.url.text = info.getUrl()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var imgView = itemView.findViewById<View>(R.id.icon) as ImageView
        var headlineMsg = itemView.findViewById<View>(R.id.headline) as TextView
        var descMsg = itemView.findViewById<View>(R.id.description) as TextView
        var url = itemView.findViewById<View>(R.id.url) as TextView
        var publish = itemView.findViewById<View>(R.id.published) as TextView


        init {
            itemView.setOnClickListener(this)
            var favImg = itemView.findViewById<View>(R.id.fav) as ImageView
            favImg.setOnClickListener{
                for (i in 0 until newsItemList.size) {
                    if(newsItemList[i].getUrl() == url.text.toString()) {
                        val art : Articles = Articles(
                            Source(newsItemList[i].getSource(),newsItemList[i].getSource(),"","","","",""),
                            "",
                            newsItemList[i].getDesc(),
                            newsItemList[i].getUrl(),
                            newsItemList[i].getImgUrl(),
                            newsItemList[i].getPublished(),
                            "")
                        Repository.writeArticleToFile(art)
                    }
                }
            }
        }
        override fun onClick(v : View) {
            var toOpen : String
            toOpen = url.text.toString()
            val page : Uri = Uri.parse(toOpen)
            val implicitIntent = Intent(Intent.ACTION_VIEW, page)
            startActivity(v.context, implicitIntent, null)
        }
    }
}