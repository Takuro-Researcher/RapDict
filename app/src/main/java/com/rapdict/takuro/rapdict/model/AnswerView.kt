package com.rapdict.takuro.rapdict

import android.graphics.Color

class AnswerView {
    var answerview_id: Int = 0
    var question_id: Int=0
    var answer: String? = null
    var question: String? = null
    var question_len: Int = 0
    var favorite:Boolean ?=false

    fun setColumn(rec_answerview_id:Int,rec_question_id:Int,rec_answer:String,rec_question:String,rec_question_len:Int,rec_favorite:Boolean){
        answerview_id = rec_answerview_id
        question_id = rec_question_id
        answer = rec_answer
        question = rec_question
        question_len = rec_question_len
        favorite = rec_favorite
    }


    companion object {
        // SQL用にフラグを数値に変更するプログラム
        fun getSearchFav(id:Int):Int{
            if (id == R.id.withoutFav){
                return 0
            }else if(id == R.id.onlyFav){
                return 1
            }
            return 2
        }

        fun changeFavo(favoriteNum:Int):Int{
            if(favoriteNum==1){
                return 0
            }
            return 1
        }
        fun favo2background(favorite:Boolean):Int{
            return if (favorite){
                Color.YELLOW
            }else{
                Color.WHITE
            }
        }

    }
}
