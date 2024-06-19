package com.assess.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordQuestion {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private String questionNo;
    private String questionContent;
    private String answerContent;
    private String answerJudgement;
    private Long recordId;
    private Boolean answerCorrect;
    public String toString() {
        return "RecordQuestion(id=" + this.getId() + ", questionNo=" + this.getQuestionNo() + ", questionContent=" + this.getQuestionContent() + ", answerContent=" + this.getAnswerContent() + ", answerJudgement=" + this.getAnswerJudgement() + ", recordId=" + this.getRecordId() + ", answerCorrect=" + this.getAnswerCorrect() + ")";
    }
}
