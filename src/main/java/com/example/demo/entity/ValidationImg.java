package com.example.demo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ValidationImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long validationImgId;
    private String path;
    private String answer;

    public long getValidationImgId() {
        return validationImgId;
    }

    public String getAnswer() {
        return answer;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setValidationImgId(long validationImgId) {
        this.validationImgId = validationImgId;
    }
}
