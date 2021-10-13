package com.example.newsagg

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.example.newsagg.fragments.HomeFragment
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.lang.Exception


class Repository {
    companion object {
        private val key : String = "41cbbf0789594eceae37b8b8cae9aa98" //this will be placed in http header in future
        private val endQuery : String = "&apiKey=$key"
        private val startUrl : String = "https://newsapi.org/v2/"
        private val sourcesPath : String = "/data/data/com.example.newsagg/cache/sources.json"
        private val articlesPath : String = "/data/data/com.example.newsagg/cache/articles.json"
        private val favArticles : String = "/data/data/com.example.newsagg/cache/favArticles.json"
        private val maxArticles = 20


        //everything, top headlines, or sources
        public fun createReq(
            endpoint: String,
            source: Source?,
            category: String?,
            country: String?,
            queryTerm: String?
        ): String {

            var req: String = "$startUrl$endpoint?"
            if (endpoint != "sources") {
                if (source != null) {
                    req += "source=" + source.id
                } else if (queryTerm != null) {
                    req += "q=$queryTerm"
                }
                if (endpoint == "top-headlines") {
                    if (country != null) {
                        req += "country=$country"
                    } else if (category != null) {
                        req += "category=$category"
                    }
                }
            } else {
                if (category != null) {
                    req += "category=$category"
                } else if (country != null) {
                    req += "country=$country"
                }
            }
            req += endQuery

            println(req)

            return req
        }

        fun post (url : String, callback: Callback) : Call {
            val sourcesReq = Request.Builder()
                .url(url)
                .build()
            val client = OkHttpClient()
            val call : Call = client.newCall(sourcesReq)
            call.enqueue(callback)
            return call
        }

        fun getSources(): MutableList<Source> {
            val url = "https://newsapi.org/v2/sources?$endQuery"

            post(url, object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) {
                            println("Unexpected response: $response")
                        }
                        else {
                            println("Fetch successful")

                            File(sourcesPath).bufferedWriter().use { out ->
                                out.write(response.body?.string())
                            }
                        }

                    }
                }
            })
            return mutableListOf<Source>()
        }

        //to test
        fun parseSources(filename : String): MutableList<Source> {
            var respBody : String = ""
            try {
                respBody = File(filename).inputStream().readBytes().toString(Charsets.UTF_8)
            }
            catch (e : Exception) {
                println(e.message)
            }

            val jsonObje: JSONObject = JSONObject(respBody)
            var sourceList: MutableList<Source> = mutableListOf()

            var body = jsonObje.get("sources").toString()
            var jsonArr = JSONArray(body)
            if (respBody != "" && jsonObje.getString("status") == "ok") {
                for (i in 0 until jsonArr.length()) {
                    var jsonObj = jsonArr.getJSONObject(i)
                    val id: String = jsonObj.getString("id")
                    val name: String = jsonObj.getString("name")
                    val desc: String = jsonObj.getString("description")
                    val url: String = jsonObj.getString("url")
                    val category: String = jsonObj.getString("category")
                    val lang: String = jsonObj.getString("language")
                    val country: String = jsonObj.getString("country")
                    sourceList.add(Source(id, name, desc, url, category, lang, country))
                }
            }
            return sourceList
        }


        fun parseArticles(filename : String): MutableList<Articles> {

            var respBody : String = ""
            try {
                respBody = File(filename).inputStream().readBytes().toString(Charsets.UTF_8)
            }
            catch (e : Exception) {
                println(e.message)
            }

            //response can be null or an empty string

            //val jsonArr: JSONArray = JSONArray(respBody)
            var articleList: MutableList<Articles> = mutableListOf()

            var jsonObjec = JSONObject(respBody)
            var jsonObjArr = arrayOf(JSONObject(respBody))
            val body = jsonObjec.get("articles").toString()
            var jsonArr = JSONArray(jsonObjec["articles"].toString())
            if (respBody != "" && jsonObjec.getString("status") == "ok") {
                var tempID : String = ""
                println(jsonObjec.getInt("totalResults"))
                for (i in 0 until maxArticles) {
                    var source : Source
                    //jsonObj = jsonArr.getJSONObject(i)
                    if (!jsonArr.getJSONObject(i).equals(null)) {
                        source = Source(
                            jsonArr.getJSONObject(i).getJSONObject("source").getString("id"),
                            jsonArr.getJSONObject(i).getJSONObject("source").getString("name"),
                            "",
                            "",
                            "",
                            "",
                            ""
                        )
                    }
                    else {
                        source = Source(
                            "",
                            jsonArr.getJSONObject(i).getString("name"),
                            "",
                            "",
                            "",
                            "",
                            ""
                        )
                    }

                    var author: String = jsonArr.getJSONObject(i).getString("author")
                    var desc: String? = jsonArr.getJSONObject(i).getString("description")
                    var url: String = jsonArr.getJSONObject(i).getString("url")
                    var urlToImg: String? = jsonArr.getJSONObject(i).getString("urlToImage")
                    var published: String = jsonArr.getJSONObject(i).getString("publishedAt")
                    var content: String? = jsonArr.getJSONObject(i).getString("content")
                    articleList.add(Articles(source, author, desc, url, urlToImg, published, content))
                }
            }
            return articleList
        }

        fun getArticles(url : String) : MutableList<Articles> {

            post(url, object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    println("Failure")
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) {
                            println("Unexpected response: $response")
                        }
                        else {
                            println("Fetch successful")

                            File(articlesPath).bufferedWriter().use { out ->
                                out.write(response.body?.string())
                            }
                            println(File(articlesPath).readText())
                        }

                    }
                }
            })

            return mutableListOf<Articles>()
        }
        fun writeArticleToFile(article : Articles) {
            var file = File(favArticles)
            var jsonString : String = ""

            println("welcome...")

            if (file.length().toInt() != 0) {
                jsonString = file.readText()
                jsonString += ","
            }

            jsonString += "{\"source\":{\"id\":\"${article.source}\",\"name\":\"\"},"
            jsonString += "\"author\":\"\","
            jsonString += "\"title\":\"\","
            jsonString += "\"description\":\"${article.desc}\","
            jsonString += "\"url\":\"${article.url}\","
            jsonString += "\"urlToImage\":\"${article.urlToImg}\","
            jsonString += "\"publishedAt\":\"${article.published}\","
            jsonString += "\"content\":\"${article.content}\""
            jsonString += "}"

            file.writeText(jsonString)
        }
    }
}