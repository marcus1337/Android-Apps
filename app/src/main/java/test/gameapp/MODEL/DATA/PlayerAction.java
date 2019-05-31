package test.gameapp.MODEL.DATA;

import java.io.Serializable;

/**
 * Created by Marcus on 2016-12-13.
 */

public class PlayerAction implements Serializable {

    public boolean isButtonAttack() {
        return buttonAttack;
    }

    public void setButtonAttack(boolean buttonAttack) {
        this.buttonAttack = buttonAttack;
    }

    public boolean isButtonUse() {
        return buttonUse;
    }

    public void setButtonUse(boolean buttonUse) {
        this.buttonUse = buttonUse;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        setNoDirection();
        this.up = up;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        setNoDirection();
        this.down = down;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        setNoDirection();
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        setNoDirection();
        this.right = right;
    }

    public void setNoDirection(){
        right = false;
        down = false;
        left = false;
        up = false;
    }

    public byte[] getByte(){

        if(up)
            statusByte = (byte) (statusByte | (1 << 0));
        else
            statusByte &= ~(1 << 0);

        if(down)
            statusByte = (byte) (statusByte | (1 << 1));
        else
            statusByte &= ~(1 << 1);

        if(left)
            statusByte = (byte) (statusByte | (1 << 2));
        else
            statusByte &= ~(1 << 2);

        if(right)
            statusByte = (byte) (statusByte | (1 << 3));
        else
            statusByte &= ~(1 << 3);

        if(buttonAttack)
            statusByte = (byte) (statusByte | (1 << 4));
        else
            statusByte &= ~(1 << 4);

        if(buttonUse)
            statusByte = (byte) (statusByte | (1 << 5));
        else
            statusByte &= ~(1 << 5);

        byte[] tmp = new byte[1];
        tmp[0] = statusByte;

        return tmp;
    }

    public void setValues(byte[] nyByte){
        statusByte = nyByte[0];
        //b |= (1 << bitIndex); // bit to 1
        //b &= ~(1 << bitIndex); // bit to 0
        up = (getBit(0) == 1);
        down = (getBit(1) == 1);
        left = (getBit(2) == 1);
        right = (getBit(3) == 1);
        buttonAttack = (getBit(4) == 1);
        buttonUse = (getBit(5) == 1);
    }

    private byte getBit(int position)
    {
        return (byte) ((statusByte >> position) & 1);
    }

    private byte statusByte;
    private boolean buttonUse;
    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;
    private boolean buttonAttack;


    public PlayerAction(){
        statusByte = 0;
        buttonAttack = false;
        buttonUse = false;
        up = false;
        down = false;
        left = false;
        right = false;
    }

}
