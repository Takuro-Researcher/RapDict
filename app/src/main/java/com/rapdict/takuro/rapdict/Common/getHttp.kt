package com.rapdict.takuro.rapdict.Common

import android.os.AsyncTask
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class getHttp(url:String): AsyncTask<String, String, String>(){
    private var callBacktask = CallBackTask()
    val url = url
    override fun doInBackground(vararg params: String?): String {
        var data = ""
        val client:OkHttpClient = OkHttpClient()
        val request = Request.Builder().url(url).get().build()
        try {
            val response:Response = client.newCall(request).execute()
            data = response.body!!.string()
        }catch (e:IOException) {
            Log.e("Error", "エラーしてますわ", e);
        }
        return data
    }

    fun setOnCallBack(_cbj:CallBackTask){
        callBacktask = _cbj
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        callBacktask.CallBack(result.toString())
    }
    open class CallBackTask{
        open fun CallBack(result:String){

        }
    }

}