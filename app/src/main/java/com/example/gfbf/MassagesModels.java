package com.example.gfbf;

public class MassagesModels {

    String uId , massage;
    Long timestamp;

    public MassagesModels(String uId, String massage, Long timestamp) {
        this.uId = uId;
        this.massage = massage;
        this.timestamp = timestamp;
    }

    public MassagesModels(String uId, String massage) {
        this.uId = uId;
        this.massage = massage;
    }

    public MassagesModels() {
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
