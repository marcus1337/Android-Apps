package SINGLE;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Random;

public class Apple{
    public int x,y;
    public boolean eaten;

    public void init(SingPlayer player, int w, int h){
        ArrayList<Pair<Integer, Integer>> avoid = player.getPositions();
        ArrayList<Pair<Integer, Integer>> possible = new ArrayList<Pair<Integer, Integer>>();

        for(int i = 0; i < w; i++){
            for (int j = 0 ; j < h; j++){
                possible.add(new Pair<Integer, Integer>(i,j));
            }
        }
        for(int i = 0; i < avoid.size(); i++){
            possible.remove(possible.get(i));
        }

        Random rand = new Random();
        int randomNum = rand.nextInt(possible.size()-1);
        Pair<Integer, Integer> p = possible.get(randomNum);
        x = p.first;
        y = p.second;
        eaten = false;
    }

    public Apple(SingPlayer player, int w, int h){
        init(player, w, h);
    }
}