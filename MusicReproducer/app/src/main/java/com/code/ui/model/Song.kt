package com.code.ui.model

class Song(title:String,author:String) {

     var title: String
     var author: String
     /*
     incapsula i dati della canzone e fa da KEY nell'HashMap <Song> <Resource>
      */

    init {
        check(title)
        check(author)

        this.title=title.trim()
        this.author=author.trim()
    }

    private fun check(s:String){
        if(s.trim().isEmpty())throw IllegalArgumentException("empty string")
    }
}