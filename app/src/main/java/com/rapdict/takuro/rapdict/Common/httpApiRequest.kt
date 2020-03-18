package com.rapdict.takuro.rapdict.Common

import android.os.AsyncTask
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class httpApiRequest: AsyncTask<String, String, String>(){
    override fun doInBackground(vararg params: String?): String {
        //ここでAPIを叩きます。バックグラウンドで処理する内容です。
        var connection: HttpURLConnection? = null
        var reader: BufferedReader? = null
        val buffer: StringBuffer

        try {
            //param[0]にはAPIのURI(String)を入れます(後ほど)。
            //AsynkTask<...>の一つめがStringな理由はURIをStringで指定するからです。
            val url = URL(params[0])
            connection = url.openConnection() as HttpURLConnection
            connection.connect()  //ここで指定したAPIを叩いてみてます。

            //ここから叩いたAPIから帰ってきたデータを使えるよう処理していきます。

            //とりあえず取得した文字をbufferに。
            val stream = connection.inputStream
            reader = BufferedReader(InputStreamReader(stream))
            buffer = StringBuffer()
            var line: String?
            while (true) {
                line = reader.readLine()
                if (line == null) {
                    break
                }
                buffer.append(line)
                //Log.d("CHECK", buffer.toString())
            }

            //ここからは、今回はJSONなので、いわゆるJsonをParseする作業（Jsonの中の一つ一つのデータを取るような感じ）をしていきます。

            //先ほどbufferに入れた、取得した文字列
            val jsonText = buffer.toString()

            //JSONObjectを使って、まず全体のJSONObjectを取ります。
            val parentJsonObj = JSONObject(jsonText)

            val fomi = parentJsonObj.get("rhymes") as JSONArray


            //ここから下は、接続エラーとかJSONのエラーとかで失敗した時にエラーを処理する為のものです。
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        //finallyで接続を切断してあげましょう。
        finally {
            connection?.disconnect()
            try {
                reader?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        //失敗した時はnullやエラーコードなどを返しましょう。
        return null.toString()
    }
}