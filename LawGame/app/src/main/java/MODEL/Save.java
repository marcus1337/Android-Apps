package MODEL;

import android.content.Context;

import java.io.Serializable;
import java.util.Date;

import CONTROL.IOHandler;

public class Save implements Serializable {

    private IOHandler ioHandler;
    private int episode, chapter, line;
    private Date date;

    public int getEpisode(){
        return episode;
    }

    public int getChapter(){
        return chapter;
    }

    public int getLine(){
        return line;
    }

    public Save(Context context){
        ioHandler = new IOHandler(context);
    }

    private void storeData(Model model){
        date = new Date();
        episode = model.getEpisode();
        chapter = model.getChapter();
        line = model.getLine();
    }

    public void saveGame(Model model){
        storeData(model);
        ioHandler.saveGame(this);
    }

    public Save loadGame(){
        return ioHandler.loadGame();
    }

}
