package com.rapdict.takuro.rapdict;

public class AnswerView {
    int question_id;
    String answer;
    String question;
    int word_len;
    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getWord_len() {
        return word_len;
    }

    public void setWord_len(int word_len) {
        this.word_len = word_len;
    }


}
