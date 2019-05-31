package test.gameapp.VIEW.GAME;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import test.gameapp.MODEL.CLIENT.ClientModel;
import test.gameapp.MODEL.CLIENT.ClientModelItem;
import test.gameapp.MODEL.CLIENT.ClientModelPlayer;
import test.gameapp.R;
import test.gameapp.VIEW.GAME.ImageTypes.BackgroundImage;
import test.gameapp.VIEW.GAME.ImageTypes.GoldTextImage;
import test.gameapp.VIEW.GAME.ImageTypes.HPImage;
import test.gameapp.VIEW.GAME.ImageTypes.HPTextImage;
import test.gameapp.VIEW.GAME.ImageTypes.ItemImage;
import test.gameapp.VIEW.GAME.ImageTypes.WinnerLooserImage;

/**
 * Created by Marcus on 2016-12-08.
 */

/**
 * Inehåller några standardlösningar för OpenGL-ES med kod från internet
 * **/
public class GameRenderer implements GLSurfaceView.Renderer {

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private float[] mRotationMatrix = new float[16];

 //   public volatile float angle;
    private Context context;
    private ItemImage playerImage, enemyImage;
    private BackgroundImage backgroundImage;

    private int[] texturenames;
    private ClientModel model;
    private int playerNumber;
    private ItemImage image;
    private HPImage hpImage1, hpImage2;
    private HPTextImage hpTextImage;
    private WinnerLooserImage winnerLooserImage;
    private GoldTextImage goldTextImage;

    private Timer timer;

    private boolean splash1, splash2;
    private boolean showedOnce, showedOnce2;

    private int counter;

    public GameRenderer(Context context, ClientModel model, int playerNumber){
        texturenames = new int[13];
        this.context = context;
        this.model = model;
        this.playerNumber = playerNumber;
        counter = 0;
        splash1 = splash2 = true;
        showedOnce = false;
        showedOnce2 = false;
        timer = new Timer();

    }

    private int enemyNum(){
        if(playerNumber == 0)
            return 1;
        else
            return 0;
    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        SetupImage();
        image = new ItemImage(3);
        playerImage = new ItemImage(playerNumber);
        hpImage1 = new HPImage(0);
        hpImage2 = new HPImage(1);
        goldTextImage = new GoldTextImage();
        hpTextImage = new HPTextImage();
        hpTextImage.setUpUV();

        playerImage.setVerts(-0.12f,  0.12f, //mitten av skärmen
                -0.12f, -0.12f,
                0.12f, -0.12f,
                0.12f,  0.12f);
        enemyImage = new ItemImage(enemyNum());
        enemyImage.setEnemyPlayer(true);

        backgroundImage = new BackgroundImage();
        try{
            backgroundImage.setUpUV(model.getPlayer(playerNumber).getX(), model.getPlayer(playerNumber).getY(), 2048, 2048);

        }catch (Exception e){

        }
        winnerLooserImage = new WinnerLooserImage();

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    private static ClientModelPlayer p;
    private static ClientModelItem[] items;
    private static int totalHP;
    private static float[] scratch;

    public void onDrawFrame(GL10 gl) {
        scratch = new float[16];
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        try{
            Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
            Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

            //Rotera spelarens bild beronde på vilket håll han går
            Matrix.setRotateM(mRotationMatrix, 0, model.getPlayer(playerNumber).getAngle(), 0, 0, -1.0f);
            Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

            backgroundImage.setUpUV(model.getPlayer(playerNumber).getX(), model.getPlayer(playerNumber).getY(), 2048, 2048);
            backgroundImage.draw(mMVPMatrix, texturenames[5]);

            playerImage.draw(scratch, texturenames[playerNumber]);

            p = model.getPlayer(enemyNum());
            enemyImage.setCoordinates(p.getX(), p.getY(), p.getAngle(), model.getPlayer(playerNumber).getX(), model.getPlayer(playerNumber).getY());

            if(p.getAngle() == 0 || p.getAngle() == 180) //ändra bildtyp beroende på motspelarens vridning
                enemyImage.draw(mMVPMatrix, texturenames[enemyNum()]);
            else if(p.getAngle() == 90){
                enemyImage.draw(mMVPMatrix, texturenames[enemyNum()+6]);
            }
            else{
                enemyImage.draw(mMVPMatrix, texturenames[enemyNum()+8]);
            }

            items = model.getClientModelItems();
            for(int i = 0; i < items.length; i++){ //rita ut alla items på skärmen som är inom ett visst avstånd
                if(!items[i].getDraw())
                    continue;
                image.setBildTyp(items[i].getType());
                image.setCoordinates(items[i].getX(), items[i].getY(), items[i].getAngle(), model.getPlayer(playerNumber).getX(), model.getPlayer(playerNumber).getY());
                image.draw(mMVPMatrix, texturenames[items[i].getType()]);
            }

            //rita hur mycket liv spelaren har kvar
            totalHP = model.getPlayer(playerNumber).getHp();
            hpTextImage.draw(mMVPMatrix, texturenames[12]);
            hpImage1.setUpUV(getNthDigit(totalHP, 1));
            hpImage1.draw(mMVPMatrix, texturenames[12]);
            hpImage2.setUpUV(getNthDigit(totalHP, 2));
            hpImage2.draw(mMVPMatrix, texturenames[12]);

            if(model.getGameover() && splash1){ //rita ut winner eller looser
                if(!showedOnce){
                    showedOnce = true;
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            splash1 = false;
                        }
                    }, 10000);
                }
                if(model.getWinner() == playerNumber)
                    winnerLooserImage.setUpUV(true);
                else
                    winnerLooserImage.setUpUV(false);
                winnerLooserImage.draw(mMVPMatrix, texturenames[12]);
            }

            if(splash2 && model.getGoldTaken()){ //rita ut att någon tagit guld
                if(!showedOnce2){
                    showedOnce2 = true;
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            splash2 = false;
                        }
                    }, 5000);
                }
                goldTextImage.draw(mMVPMatrix, texturenames[12]);
            }

        }catch (Exception e){
            Log.i("hmmm", e.toString());
        }

        counter++; //används ej
        if(counter == 10)
            counter = 0;
    }

    private int getNthDigit(int number, int n) { //funktion tagen från stackoverflow
        return (int) ((number / Math.pow(10, n - 1)) % 10);
    }


    public void onSurfaceChanged(GL10 unused, int width, int height) { //standardlösning för openglES
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }


    /**Ladda in bilder till minnet som ska användas
     * Referera till bilderna via 'texturenames[]'
     * **/
    private void SetupImage()
    {
        // Generate Textures, if more needed, alter these numbers.
        GLES20.glGenTextures(texturenames.length, texturenames, 0);

        for(int i = 0; i < 13; i++){

            Bitmap bmp = null;

            if(i == 0)
                bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.hero1);
            if(i == 1)
                bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.defender1);
            if(i == 2)
                bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.tower1);
            if(i == 3)
                bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.brickwall);
            if(i == 4)
                bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.pil1);
            if(i == 5)
                bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.karta1);
            if(i == 6)
                bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.hero1twist1);
            if(i == 7)
                bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.defender1twist1);
            if(i == 8)
                bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.hero1twist2);
            if(i == 9)
                bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.defender1twist2);
            if(i == 10)
                bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.chest);
            if(i == 11)
                bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.chestopen);
            if(i == 12)
                bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.numtexture);


            // Bind texture to texturename
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[i]);

            // Set filtering
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                    GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                    GLES20.GL_LINEAR);

            // Set wrapping mode
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
                    GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                    GLES20.GL_CLAMP_TO_EDGE);

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);

            // We are done using the bitmap so we should recycle it.
            bmp.recycle();

        }

    }


}