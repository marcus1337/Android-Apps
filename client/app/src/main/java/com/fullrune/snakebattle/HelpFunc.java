package com.fullrune.snakebattle;

import STATES.Player;

public class HelpFunc {

    public static void setByteData(byte[] data, int begin, int end, byte value) {
        for (int i = begin; i <= end; i++) {
            int nowByte = i / 8;
            int nowBit = i - nowByte * 8;

            if (getBit((end - i), value) == 1)
                data[nowByte] = (byte) (data[nowByte] | (1 << nowBit));
            else
                data[nowByte] = (byte) (data[nowByte] & ~(1 << nowBit));
        }

    }

    public static int getByteData(byte[] data, int begin, int end) {
        int result = 0;
        for (int i = begin; i <= end; i++) {
            int nowByte = i / 8;
            int nowBit = i - nowByte * 8;
            int powBy = (end - i);

            if (getBit(nowBit, data[nowByte]) != 0)
                result += Math.pow(2, powBy);
        }
        return result;
    }

    public static int getBit(int position, byte ID)
    {
        return (byte) ((ID >> position) & 1);
    }

    public static Player.Move intToMove(int i){
        if(i == 1)
            return Player.Move.UP;
        if(i == 2)
            return Player.Move.LEFT;
        if(i == 3)
            return Player.Move.RIGHT;
        return Player.Move.DOWN;
    }

}
