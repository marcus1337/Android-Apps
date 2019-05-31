package test.morris.MODELLEN;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Marcus on 2016-11-27.
 */

//har koll på alla sparade spel
public class GameState implements Serializable {

    private ArrayList<NineMenMorrisRules> games;

    public GameState() {
        games = new ArrayList<NineMenMorrisRules>();
    }

    //index 0 är alltid det nuvarande spelet
    public void addOrUpdateModel(NineMenMorrisRules model) {
        if (model != null) {
            NineMenMorrisRules tmpModel = model;
            if(size() > 0){
                games.set(0, tmpModel);
            }
            else{
                games.add(tmpModel);
            }
        }
    }

    public NineMenMorrisRules getGame(){
        if(size() > 0){
            NineMenMorrisRules tmp = games.get(0);
            return tmp;
        }
        return null;
    }

    public void deleteGame(int chosen){
        if (chosen > size() || chosen < 0 || size() == 0)
            return;
        games.remove(chosen);
    }

    public void startNewGame(NineMenMorrisRules newModel){
        if(newModel == null)
            return;
        NineMenMorrisRules tmp = newModel;
        games.add(0, tmp);
    }

    //index 0 är alltid det nuvarande spelet
    public NineMenMorrisRules selectNewGame(int chosen) {
        if (chosen > size() || chosen < 0 || size() == 0)
            return null;
        if(chosen == 0)
            return getGame();

        Collections.swap(games, 0, chosen);
        return getGame();
    }

    public ArrayList<NineMenMorrisRules> getAllGames(){
        ArrayList<NineMenMorrisRules> tmp = games;
        return tmp;
    }
    public void setAllGames(ArrayList<NineMenMorrisRules> newList){
        if(newList != null)
            games = newList;
    }

    public void setLevel(int level, int index){
        if (index > size() || index < 0 || size() == 0)
            return;
        games.get(index).setLevel(level);
    }

    public int size() {
        return games.size();
    }


}
