package com.rapdict.takuro.rapdict;

public class AnswerView {
    private int answerview_id;
    private int question_id;
    private String answer;
    private String question;
    private int question_len;

    public void setColumn(int answerview_id, int question_id, String answer, String question, int question_len){
        setAnswer(answer);
        setQuestion(question);
        setQuestion_id(question_id);
        setQuestion_len(question_len);
        setAnswerview_id(answerview_id);
    }

    public int getAnswerview_id() {
        return answerview_id;
    }

    public void setAnswerview_id(int answerview_id) {
        this.answerview_id = answerview_id;
    }

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

    public int getQuestion_len() {
        return question_len;
    }

    public void setQuestion_len(int question_len) {
        this.question_len = question_len;
    }


}
