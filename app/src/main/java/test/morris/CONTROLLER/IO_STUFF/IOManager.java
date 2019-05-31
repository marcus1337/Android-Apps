package test.morris.CONTROLLER.IO_STUFF;

import test.morris.MODELLEN.GameState;
import test.morris.MainActivity;

/**
 * Created by Marcus on 2016-11-22.
 */

public class IOManager {
    private MainActivity mainActivity;
    private FileManager fileManager;

    //laddar data från filer
    public IOManager(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        fileManager = new FileManager(mainActivity.getApplicationContext());
    }

    public GameState loadData() {
        return fileManager.loadData();
    }

    public void saveData(GameState storage) {
        if(storage != null)
            fileManager.fileSaver(storage);
    }

}
