package com.example.newsagg

data class ResponseContent(val status : String,
                           val results : Int,
                           val articles: MutableList<Articles>)