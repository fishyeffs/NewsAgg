package com.example.newsagg.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsagg.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FavouritesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavouritesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private final val favFileName = "/data/data/com.example.newsagg/cache/favArticles.json"
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
        val view : View = inflater.inflate(R.layout.fragment_favourites, container, false)


        //trying to parse empty json file, so if empty dont populate
        //get

        if(File(favFileName).length().toInt() == 0) {
            val txt = TextView(this.context)
            txt.text = "Empty"
        }
        else {
            list.clear()
            list = parseArticles(favFileName)
            val imageModelArrList = populateNewsItems(list)

            val recyclerView = view.findViewById<RecyclerView>(R.id.favRecycler)
            val layoutManager = LinearLayoutManager(view.context)

            recyclerView.layoutManager = layoutManager

            val mAdapter = MyAdapter(imageModelArrList)

            recyclerView.adapter = mAdapter
        }




        return view
    }

    private fun populateNewsItems(artList : MutableList<Articles>): MutableList<DataStore> {

        for(i in 0 until artList.size) {
            val imageStore = DataStore()

            imageStore.setNames(artList.get(i).source.name)

            if(!artList.get(i).desc.equals("")) {
                artList.get(i).desc?.let { imageStore.setDesc(it) }
            }
            artList.get(i).url?.let { imageStore.setUrl(it) }
            println(artList.get(i).url)
            if(!artList.get(i).published.equals("")) {
                artList.get(i).published?.let { imageStore.setPublished(it) }
            }
            if(!artList.get(i).urlToImg.equals("")) {
                artList.get(i).urlToImg?.let { imageStore.setImgUrl(it) }
            }
            artList.get(i).source.name.let { imageStore.setSource(it) }
            dataList.add(imageStore)
        }

        return dataList
    }

    fun parseArticles(filename : String) : MutableList<Articles> {
        val articleList = mutableListOf<Articles>()
        val file = File(filename)
        val jsonString = file.readText()

        var obj = JSONObject(jsonString)
        var jsonArr = arrayOf(obj)


        println(jsonArr.size)

        for(i in 0 until jsonArr.size) {
            var source : Source
            //jsonObj = jsonArr.getJSONObject(i)
            if (obj.getJSONObject("source").equals(null)) {
                source = Source(
                    obj.getJSONObject("source").getString("id"),
                    obj.getJSONObject("source").getString("name"),
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
                    "",
                    "",
                    "",
                    "",
                    "",
                    ""
                )
            }

            var author: String = obj.getString("author")
            var desc: String? = obj.getString("description")
            var url: String = obj.getString("url")
            var urlToImg: String? = obj.getString("urlToImage")
            var published: String = obj.getString("publishedAt")
            var content: String? = obj.getString("content")
            articleList.add(Articles(source, author, desc, url, urlToImg, published, content))
        }

        return articleList
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FavouritesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavouritesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}