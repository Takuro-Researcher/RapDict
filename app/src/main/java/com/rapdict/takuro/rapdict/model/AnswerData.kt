package sample.intent

import androidx.room.Entity
import java.io.Serializable
@Entity
data class AnswerData(var answer: String,var answer_len: Int, var question:String, var favorite: Int)
