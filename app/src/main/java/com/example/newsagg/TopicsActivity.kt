package com.example.newsagg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import org.json.JSONObject
import java.io.File

class TopicsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topics)

        val linearLayout : LinearLayout = findViewById<LinearLayout>(R.id.linear_layout)
        val list = getTopics()
        val updatedList : MutableList<String> = mutableListOf<String>()

        val checkBoxList = mutableListOf<CheckBox>()

        for (i in 0 until list.size) {
            checkBoxList.add(i, CheckBox(this))
            checkBoxList[i].text = list[i]
            checkBoxList[i].setOnClickListener {
                if(checkBoxList[i].isChecked) {
                    updatedList.add(checkBoxList[i].text.toString())
                }
                else {
                    if(checkBoxList[i].text.toString() in updatedList) {
                        updatedList.remove(checkBoxList[i].text.toString())
                    }
                }

                File("/data/data/com.example.newsagg/cache/topics.txt").bufferedWriter().use {
                    for (i in 0 until updatedList.size) {
                        it.write("${updatedList[i]}\n")
                    }
                }

            }

            if(checkBoxList[i].text !in list) {
                checkBoxList[i].isChecked = true
            }

            linearLayout.addView(checkBoxList[i])
        }


    }

    fun getTopics() : MutableList<String> {
        var topics : MutableList<String> = mutableListOf<String>()

        val sources = Repository.parseSources("/data/data/com.example.newsagg/cache/sources.json")

        for (i in 0 until sources.size) {
            if (sources.get(i).category != null && sources.get(i).category!! !in topics) {
                topics.add(sources.get(i).category!!)
            }
        }
        return topics

    }
}