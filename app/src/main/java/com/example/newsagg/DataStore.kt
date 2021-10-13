package com.example.newsagg

import android.graphics.Bitmap
import android.graphics.drawable.Drawable

class DataStore {
    private var itemName: String? = null
    private lateinit var itemThumbnail: Drawable
    private var itemDescription : String? = "No description"
    private var published : String = ""
    private var url : String = ""
    private var urlToImage : String = ""
    private var source : String = ""


    /**
     * Get the names of the news items
     */
    fun getNames(): String {
        return itemName.toString()
    }

    /**
     * Set the names of the news items
     */
    fun setNames(name: String) {
        this.itemName = name
    }

    /**
     * Get the thumbnail
     */
    fun getThumbnail(): Drawable {
        return itemThumbnail
    }

    /**
     * Set the thumbnail
     */
    fun setThumbnail(thumb: Drawable) {
        this.itemThumbnail = thumb
    }

    /**
     * Get the names of the news items
     */
    fun getPublished(): String {
        return published
    }

    /**
     * Set the names of the news items
     */
    fun setPublished(published : String) {
        this.published = published
    }

    /**
     * Get the names of the news items
     */
    fun getImgUrl(): String {
        return urlToImage
    }

    /**
     * Set the names of the news items
     */
    fun setImgUrl(urlImg : String) {
        this.urlToImage = urlImg
    }
    /**
     * Get the names of the news items
     */
    fun getDesc(): String {
        return itemDescription.toString()
    }

    /**
     * Set the names of the news items
     */
    fun setDesc(desc : String) {
        this.itemDescription = desc
    }
    /**
     * Get the names of the news items
     */
    fun getUrl(): String {
        return url
    }

    /**
     * Set the names of the news items
     */
    fun setUrl(url : String) {
        this.url = url
    }


    fun getSource(): String {
        return source
    }

    /**
     * Set the names of the news items
     */
    fun setSource(src : String) {
        this.source = src
    }
}