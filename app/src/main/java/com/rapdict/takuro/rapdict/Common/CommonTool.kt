package com.rapdict.takuro.rapdict.Common

import java.net.URI

class CommonTool {
    fun makeNumArray(min:Int, max:Int, inc:Int = 1):ArrayList<Int>{
        val numArray = ArrayList<Int>()
        for(i in min..max step inc){
            numArray.add(i)
        }
        return numArray
    }

    companion object{
        fun makeApiUrl(min:Int,max:Int,num:Int): String{
            var url ="https://script.google.com/macros/s/AKfycbwlFWDy9zFZK2nWMyXrHdHrlIJ2wZPx-ufvaLwukpjs0fMzqg/exec?"
            url += "min=$min&"
            url += "max=$max&"
            url += "num=$num"
            return url
        }
    }
}