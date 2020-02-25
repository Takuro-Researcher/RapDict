package com.rapdict.takuro.rapdict.Common

class CommonTool {
    public  fun makeNumArray(min:Int, max:Int, inc:Int = 1):ArrayList<Int>{
        val numArray = ArrayList<Int>()
        for(i in min..max step inc){
            numArray.add(i)
        }
        return numArray
    }
}