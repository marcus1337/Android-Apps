package com.fullrune.areashiftertwo.MODEL.SAVING;

import java.util.Date;

public class SaveInfo {

    private Date date;
    private String status;

    public SaveInfo(){
        date = new Date();
        status = "working";
    }

    public String getStatus(){
        return status;
    }

    public Date getDate(){
        return date;
    }

    public void updateStatusDone(){
        status = "finished";
        date = new Date();
    }

}
