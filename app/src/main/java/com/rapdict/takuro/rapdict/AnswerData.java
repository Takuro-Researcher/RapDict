package sample.intent;

import java.io.Serializable;

public class AnswerData implements Serializable{
    int question_id;
    String answer;
    String question;
    public int getQuestion_id() {
        return question_id;
    }
    public void answerSet(int question_id, String answer, String question){
        setAnswer(answer);
        setQuestion(question);
        setQuestion_id(question_id);
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

}