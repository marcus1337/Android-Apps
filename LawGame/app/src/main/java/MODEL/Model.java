package MODEL;


import java.util.ArrayList;

public class Model {

    private Save save;

    public Model(){
        health = 100;
    }

    public void init(ArrayList<String> story, Save save){
        this.story = story;
        this.save = save;

        if(save == null){
            line = 1;
            chapter = 1;
        }else{
            chapter = save.getChapter();
            line = save.getLine();
        }
    }

    private int chapter;
    private int line;
    private ArrayList<String> story;
    private int health;

    public int getChapter() {
        return chapter;
    }

    public int getLine() {
        return line;
    }

    public int getEpisode() {
        return 1;
    }

    public String getTextCurrent(){
        return "Hello, I am a banana/n problem pro lel? heh eh eh ehe he h ee he he";
    }

    public boolean allowSkip(){

        return true;
    }

}
