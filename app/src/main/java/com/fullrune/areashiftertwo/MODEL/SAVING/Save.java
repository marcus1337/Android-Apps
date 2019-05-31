package com.fullrune.areashiftertwo.MODEL.SAVING;

import android.content.Context;

import java.io.Serializable;

import com.fullrune.areashiftertwo.CONTROL.IOHandler;
import com.fullrune.areashiftertwo.MODEL.Model;

public class Save implements Serializable {

    private IOHandler ioHandler;

    public Save(Context context){
        ioHandler = new IOHandler(context);
    }

    private void storeData(Model model){

    }

    public void saveGame(Model model){
        storeData(model);
        ioHandler.saveGame(this);
    }

    public Save loadGame(){
        return ioHandler.loadSave();
    }

}
