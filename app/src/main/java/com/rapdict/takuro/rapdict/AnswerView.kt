package com.rapdict.takuro.rapdict

class AnswerView {
    var answerview_id: Int = 0
    var question_id: Int=0
    var answer: String? = null
    var question: String? = null
    var question_len: Int = 0

    fun setColumn(rec_answerview_id:Int,rec_question_id:Int,rec_answer:String,rec_question:String,rec_question_len:Int){
        answerview_id = rec_answerview_id
        question_id = rec_question_id
        answer = rec_answer
        question = rec_question
        question_len = rec_question_len
    }





}
