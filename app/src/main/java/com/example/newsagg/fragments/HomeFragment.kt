package com.example.newsagg.fragments

import android.os.Bundle
import android.provider.ContactsContract
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsagg.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.news_layout.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import org.json.JSONArray
import java.io.File
import java.io.OutputStreamWriter
import java.lang.Exception
import java.security.AccessController

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private final val articleFileName = "/data/data/com.example.newsagg/cache/articles.json"
    private final val sourceFileName = "/data/data/com.example.newsagg/cache/sources.json"
    private final val topicsFileName = "/data/data/com.example.newsagg/cache/topics.txt"
    private val dataList : MutableList<DataStore> = mutableListOf()
    private var list : MutableList<Articles> = mutableListOf()
    private val maxArticles = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
        ): View? {
        val view : View = inflater.inflate(R.layout.fragment_home, container, false)

        //get

        val url = Repository.createReq("top-headlines", null, null,"gb", null)
        Repository.getArticles(url)



            println("up to here")
            list = Repository.getArticles(url)
            println("up to here 1")
            val imageModelArrList = populateNewsItems(Repository.parseArticles(articleFileName))

            val recyclerView = view.findViewById<RecyclerView>(R.id.mainRecycler)
            val layoutManager = LinearLayoutManager(view.context)
            println("up to here 2")
            recyclerView.layoutManager = layoutManager
            println("up to here 3")
            val mAdapter = MyAdapter(imageModelArrList)


            recyclerView.adapter = mAdapter
//        }
//        catch (e : Exception) {
//            println("start up error")
//        }


        return view
    }

    private fun populateNewsItems(artList : MutableList<Articles>): MutableList<DataStore> {
        //val dataList : MutableList<DataStore> = mutableListOf()
        val sourceList = Repository.getSources()
        //Repository.parseSources(sourceFileName)
        var matchingArticles : MutableList<Articles> = mutableListOf<Articles>()
        matchingArticles = Repository.parseArticles(articleFileName)
        //first assign proper sources
        //check topics file isn't empty
        //then find sources that match category
        //add matching
//        if(File(articleFileName).exists()) {
//            for (i in 0 until 20) {
//                for (j in 0..sourceList.size) {
//
//                    if(matchingArticles[i].source.name == sourceList[j].name) {
//                        matchingArticles[i].source = sourceList[j]
//                    }
//                }
//            }
//        }


        val file : File = File(topicsFileName)

        if(file.exists()) {
            for (i in 0 until artList.size) {
                file.forEachLine {
                    //println("We goin crazy!!! $it")
                    //println("Here: ${artList[i].source.category}")
                    if(artList[i].source.category == it) {

                        matchingArticles.add(artList[i])
                    }
                }
            }
        }
        else {
            matchingArticles = artList
        }

        println("Size: ${artList.size}")
        for(i in 0 until artList.size) {
            //placeholder code
            val imageStore = DataStore()

            //println(artList[i].author) //throws error
            //imageStore.setNames(artList[i].source.name) //throws error

            imageStore.setNames(matchingArticles.get(i).source.name)


            if(!matchingArticles.get(i).desc.equals("")) {
                matchingArticles.get(i).desc?.let { imageStore.setDesc(it) }
            }
            matchingArticles.get(i).url?.let { imageStore.setUrl(it) }
            println(matchingArticles.get(i).url)
            if(!matchingArticles.get(i).published.equals("")) {
                matchingArticles.get(i).published?.let { imageStore.setPublished(it) }
            }
            if(!matchingArticles.get(i).urlToImg.equals("")) {
                matchingArticles.get(i).urlToImg?.let { imageStore.setImgUrl(it) }
            }
            matchingArticles.get(i).source.name.let { imageStore.setSource(it) }
            dataList.add(imageStore)
        }

        return dataList
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }


    }
}

