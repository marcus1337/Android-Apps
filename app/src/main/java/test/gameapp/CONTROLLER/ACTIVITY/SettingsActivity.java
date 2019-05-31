package test.gameapp.CONTROLLER.ACTIVITY;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import test.gameapp.CONTROLLER.IO.IOManager;
import test.gameapp.MODEL.DATA.PlayerInfo;
import test.gameapp.R;

/**
 * Created by Marcus on 2016-12-20.
 */

/**Denna klass låter användaren köpa uppgraderingar med sitt guld
 *
 * **/

public class SettingsActivity extends Activity {

    private Button backButton;
    private TextView goldText;
    private IOManager ioManager;
    private HorizontalScrollView scrollView;
    private ArrayList<ImageView> weapons;
    private int selected = -1;
    private SettingsActivity settingsActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_lay);
        settingsActivity = this;
        ioManager = new IOManager(this);
        backButton = (Button) findViewById(R.id.buttonBack);
        goldText = (TextView) findViewById(R.id.textGold);
        backButton.setOnClickListener(new backBtnListen());
        scrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView1);
        weapons = new ArrayList<ImageView>();

        PlayerInfo playerInfo = ioManager.load();
        goldText.setText("Gold: " + playerInfo.getGold());
        selected = playerInfo.getSelectedSword();
        Log.i("Selectedsword123", "selected: " + selected);

        LinearLayout layout = (LinearLayout) findViewById(R.id.shapeLayout);
        for (int i = 0; i < 4; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(10, 10, 10, 10);
            if (i == 0) imageView.setImageBitmap(BitmapFactory.decodeResource(
                    getResources(), R.drawable.wep1));
            if (i == 1) imageView.setImageBitmap(BitmapFactory.decodeResource(
                    getResources(), R.drawable.wep2));
            if (i == 2) imageView.setImageBitmap(BitmapFactory.decodeResource(
                    getResources(), R.drawable.wep3));
            if (i == 3) imageView.setImageBitmap(BitmapFactory.decodeResource(
                    getResources(), R.drawable.wep4));

            if(selected == 0 && i == 0){
                imageView.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.drawable.wep1selected));
            }
            if(selected == 1 && i == 1){
                imageView.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.drawable.wep2selected));
            }
            if(selected == 2 && i == 2){
                imageView.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.drawable.wep3selected));
            }
            if(selected == 3 && i == 3){
                imageView.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.drawable.wep3selected));
            }

            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setOnClickListener(new WeaponListener());
            weapons.add(imageView);
            layout.addView(imageView);
        }

    }

    private void resetAllImages(){

        for(int i = 0; i < weapons.size(); i++){
            if (i == 0) weapons.get(i).setImageResource(R.drawable.wep1);
            if (i == 1) weapons.get(i).setImageResource(R.drawable.wep2);
            if (i == 2) weapons.get(i).setImageResource(R.drawable.wep3);
            if (i == 3) weapons.get(i).setImageResource(R.drawable.wep4);
        }
        goldText.setText("Gold: " + ioManager.getGold());

    }

    private void showToast(String msg){
        Toast.makeText(settingsActivity, msg, Toast.LENGTH_SHORT).show();
    }

    private class WeaponListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case 0:
                    if(selected == 0){
                        showToast("alredy using this");
                        return;
                    }
                    if(ioManager.buyWeapon(100, 0)){
                        resetAllImages();
                        weapons.get(0).setImageResource(R.drawable.wep1selected);
                        selected = 0;
                    }
                    break;
                case 1:
                    if(selected == 1){
                        showToast("alredy using this");
                        return;
                    }
                    if(ioManager.buyWeapon(200, 1)){
                        resetAllImages();
                        weapons.get(1).setImageResource(R.drawable.wep1selected);
                        selected = 1;
                    }
                    break;
                case 2:
                    if(selected == 2){
                        showToast("alredy using this");
                        return;
                    }
                    if(ioManager.buyWeapon(300, 2)){
                        resetAllImages();
                        weapons.get(2).setImageResource(R.drawable.wep1selected);
                        selected = 2;
                    }
                    break;
                case 3:
                    if(selected == 3){
                        showToast("alredy using this");
                        return;
                    }
                    if(ioManager.buyWeapon(5000, 3)){
                        resetAllImages();
                        weapons.get(3).setImageResource(R.drawable.wep1selected);
                        selected = 3;
                    }
                    break;
                default:
                    break;

            }
        }
    }

    private class backBtnListen implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result", "resultatet");
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }

}
