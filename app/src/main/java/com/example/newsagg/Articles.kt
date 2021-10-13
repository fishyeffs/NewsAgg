package com.example.newsagg


/**
 * Data class containing headings for
 */
data class Articles (var source : Source,
                     var author : String,
                     var desc : String?,
                     var url : String,
                     var urlToImg : String?,
                     var published : String,
                     var content : String?)