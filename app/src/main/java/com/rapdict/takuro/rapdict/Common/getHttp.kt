package com.rapdict.takuro.rapdict.Common

import android.os.AsyncTask
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class getHttp(url:String): AsyncTask<String, String, String>(){
    private var callBacktask = CallBackTask()
    val url = url
    override fun doInBackground(vararg params: String?): String {
        var data = ""
        val client:OkHttpClient = OkHttpClient()
        val request = Request.Builder().url(url).get().build()
        try {
            val response:Response = client.newCall(request).execute()
            data = response.body()!!.string()
        }catch (e:IOException) {
            e.printStackTrace()
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