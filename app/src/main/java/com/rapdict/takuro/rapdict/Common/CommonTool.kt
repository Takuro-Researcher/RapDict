package com.rapdict.takuro.rapdict.Common

import com.rapdict.takuro.rapdict.R
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
        fun setMusicRaw(measure:Int,bgm:Boolean): Int? {
            if (measure ==2){
                if (bgm){
                    return R.raw.beat_97
                }else{
                    return R.raw.beat_97_dronly
                }
            }
            if (measure ==4){
                if (bgm){
                    System.out.println("ここのはずやで")
                    return R.raw.beat_97_4var
                }else{
                    return R.raw.beat_97_4vardr
                }
            }
            if (measure ==8){
                if (bgm){
                    return R.raw.beat_97_8var
                }else{
                    return R.raw.beat_97_8vardr
                }
            }
            return null
        }
    }
}