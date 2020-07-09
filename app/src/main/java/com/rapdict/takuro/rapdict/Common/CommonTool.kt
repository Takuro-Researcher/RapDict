package com.rapdict.takuro.rapdict.Common

import com.rapdict.takuro.rapdict.R

class CommonTool {
    companion object{
        fun makeNumArray(min:Int, max:Int, inc:Int = 1):ArrayList<Int>{
            val numArray = ArrayList<Int>()
            for(i in min..max step inc){
                numArray.add(i)
            }
            return numArray
        }
        fun makeApiUrl(min:Int,max:Int,num:Int): String{
            var url ="https://script.google.com/macros/s/AKfycbwlFWDy9zFZK2nWMyXrHdHrlIJ2wZPx-ufvaLwukpjs0fMzqg/exec?"
            url += "min=$min&"
            url += "max=$max&"
            url += "num=$num"
            return url
        }
        // typeごとに分ける
        fun choiceMusic(drumonly:Boolean,type:String,bar:Int):Int{
            val drumOnlyList: MutableList<Triple<Int,Int,Int>> = mutableListOf(
                    Triple(R.raw.beat_97_dronly,R.raw.beat_97_4bardr,R.raw.beat_97_8bardr),
                    Triple(R.raw.beat_110_2bardr,R.raw.beat_110_4bardr,R.raw.beat_110_8bardr),
                    Triple(R.raw.beat_130_2bardr,R.raw.beat_130_4bardr,R.raw.beat_130_8bardr),
                    Triple(R.raw.beat_130tri_2bardr,R.raw.beat_130tri_4bardr,R.raw.beat_130tri_8bardr)
            )
            val beatList: MutableList<Triple<Int,Int,Int>> = mutableListOf(
                    Triple(R.raw.beat_97,R.raw.beat_97_4bar,R.raw.beat_97_8bar),
                    Triple(R.raw.beat_110_2bar,R.raw.beat_110_4bar,R.raw.beat_110_8bar),
                    Triple(R.raw.beat_130_2bar,R.raw.beat_130_4bar,R.raw.beat_130_8bar),
                    Triple(R.raw.beat_130tri_2bar,R.raw.beat_130_4bar,R.raw.beat_130tri_8bar)
            )
            var index = 0
            when(type){
                "low" ->{ index = 0 }
                "middle" ->{ index = 1 }
                "high" ->{ index = 2 }
                "tri" ->{ index = 3}
            }
            if(drumonly){
                when(bar){
                    2 -> { return drumOnlyList[index].first}
                    4 -> { return drumOnlyList[index].second}
                    8 -> { return drumOnlyList[index].third}
                }
            }else{
                when(bar){
                    2 -> { return beatList[index].first}
                    4 -> { return beatList[index].second}
                    8 -> { return beatList[index].third}
                }
            }
            return beatList[0].first
        }
    }
}