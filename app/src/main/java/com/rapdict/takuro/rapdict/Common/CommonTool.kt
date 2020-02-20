package com.rapdict.takuro.rapdict.Common

class CommonTool {
    public  fun makeNumArray(min:Int, max:Int):ArrayList<Int>{
        val numArray = ArrayList<Int>()
        for(i in min..max){
            numArray.add(i)
        }
        return numArray
    }
}