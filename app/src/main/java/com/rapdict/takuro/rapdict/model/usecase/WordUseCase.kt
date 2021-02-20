package com.rapdict.takuro.rapdict.model.usecase

import com.rapdict.takuro.rapdict.model.entity.Word

class WordUseCase {
    // wordが足りない場合、増やすコード
    fun paddingWord(words: List<Word>, question: Int): List<Word> {
        if (words.isEmpty()) {
            return words
        }
        val addWords = mutableListOf<Word>()
        for (i in words.size until question) {
            addWords.add(words.random())
        }
        return words + addWords
    }
}