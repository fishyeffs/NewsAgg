package com.example.newsagg

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.newsagg.fragments.FavouritesFragment
import com.example.newsagg.fragments.HomeFragment
import com.example.newsagg.fragments.TopicsFragment
import com.example.newsagg.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import okio.IOException
import java.io.File
import java.lang.Exception
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val url = Repository.createReq("top-headlines", null, null,"gb", null)
        var list = Repository.getArticles(url)

        //fragments
        val topicsFrag = TopicsFragment()
        val favFrag = FavouritesFragment()
        val homeFrag = HomeFragment()

        makeCurrentFragment(homeFrag)

        bottom_tray.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> makeCurrentFragment(homeFrag)
                R.id.navigation_favourites -> makeCurrentFragment(favFrag)
                R.id.navigation_account -> makeCurrentFragment(topicsFrag)
            }
        true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.settings_nav, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
            R.id.nav_refresh -> {
                //this will get from a file the preferences
                //https://newsapi.org/v2/top-headlines?country=gb&apiKey=41cbbf0789594eceae37b8b8cae9aa98
                val url = Repository.createReq("top-headlines", null, null,"gb", null)
                var list = Repository.getArticles(url)
                makeCurrentFragment(HomeFragment())
                true
            }
            R.id.nav_sign_in -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_sign_out -> {
                val mAuth = FirebaseAuth.getInstance()
                if (mAuth.currentUser != null) {
                    mAuth.signOut()
                }
                true
            }
            R.id.nav_topics -> {
                val intent = Intent(this, TopicsActivity::class.java)
                if (!File("/data/data/com.example.newsagg/cache/sources.json").exists()) {
                    Repository.getSources()
                }
                startActivity(intent)
                true
            }
            R.id.nav_sources -> {
                Repository.getSources()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.mainView, fragment)
            commit()
        }

}