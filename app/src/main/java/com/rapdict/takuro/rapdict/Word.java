package com.rapdict.takuro.rapdict;

public class Word {
    int word_id;
    String furigana;
    String word;

    int word_len;

    public  int getWord_id(){
        return word_id;
    }

    public  void setWord_id(int word_id){this.word_id = word_id;}

    public String getFurigana() {
        return furigana;
    }

    public void setFurigana(String furigana) {
        this.furigana = furigana;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getWord_len() { return word_len; }

    public void setWord_len(int word_len) { this.word_len = word_len; }
}
