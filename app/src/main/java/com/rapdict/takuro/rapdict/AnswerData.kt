package sample.intent

import java.io.Serializable

class AnswerData : Serializable {
    var question_id: Int ?= null;
    var answer: String ?=null;
    var question: String ?=null;

    fun answerSet(rec_question_id: Int, rec_answer: String, rec_question: String) {
        answer = rec_answer
        question = rec_question
        question_id = rec_question_id
    }

}