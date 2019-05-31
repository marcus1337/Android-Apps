package test.morris.MODELLEN;
/*F?LJANDE ?R ?NDRAT:
 * 1. public boolean remove(int to) -> private boolean partOfThreeInARow(int to)
 *
 * 2. public boolean win(int color) har skrivits p? f?ljande s?tt:
 * 		* While-loopen skrevs om till en for-loop
 *    	* Den felaktiga if-satsen i for-loopen har "?tg?rdats"
 *
 * 3. private boolean colorOnlyHasThreeInARows(int color) tillagd
 *    returnerar true om den f?rgen endast har marks som ing?r i three-in-a-row
 *
 * 4. public boolean remove(int From, int color)
 *    ?ndrad s? att man inte f?r ta bort en mark som ing?r i three-in-a-row OM
 *    det finns n?gon mark man kan ta bort som inte ing?r i three-in-a-row.
 *
 * 5. public boolean win(int color) tillagt s? att om en spelare inte kan flytta
 *    n?gon marker s? r?knas det som f?rlust.
 *
 * 6. private boolean canPlayerMakeAnyMove(int color) skapad. Kollar om color kan flytta p? n?gon marker
 *    eller om den ?r "trapped"
 *
 * 7. public int getTurn() lades till
 *
 * 8. lade till boolean remove samt getRemove()
 */

/*
 * The game board positions
 *
 * 03           06           09
 *     02       05       08
 *         01   04   07
 * 24  23  22        10  11  12
 *         19   16   13
 *     20       17       14
 * 21           18           15
 *
 */

import android.util.Log;

import java.io.Serializable;

public class NineMenMorrisRules implements Serializable {
    private int[] gameplan;
    private int bluemarker, redmarker; //kvar att placeras ut p? br?det
    private int turn; // player in turn

    private int level;

    private boolean remove;

    public static final int BLUE_MOVES = 1;
    public static final int RED_MOVES = 2;

    public static final int EMPTY_SPACE = 0;
    public static final int BLUE_MARKER = 4;
    public static final int RED_MARKER = 5;

    public NineMenMorrisRules(int level) {
        this.level = level;
        if(level!=1 && level!=2 && level!=3)
            this.level = 1;
        gameplan = new int[25]; // zeroes
        bluemarker = 9;
        redmarker = 9;
        turn = RED_MOVES;
    }

    /** OKLART OM DENNA ?R KLAR
     * Returns true if a move is successful
     */
    public boolean legalMove(int To, int From, int player) {
        if( !(player==1 || player==2) ){
            Log.i("ERROR:","Received bad int as arg in function NineMenMorrisRules.legalMove, received int "+player);
            return false;
        }
        int color = player+3;
        if (player == turn) {
            if (turn == RED_MOVES) {
                if (redmarker > 0) {
                    if (gameplan[To] == EMPTY_SPACE) {
                        gameplan[To] = RED_MARKER;
                        redmarker--;
                        if(partOfThreeInARow(To))
                            remove = true;
                        else
                            turn = BLUE_MOVES;

                        return true;
                    }else{
                        return false;
                    }
                }
                if (gameplan[To] == EMPTY_SPACE) {
                    boolean valid = isValidMove(To, From);
                    if ((valid == true || playerOnlyHasThreeMarksLeft(player)) && gameplan[From]==RED_MARKER) {
                        gameplan[To] = RED_MARKER;
                        gameplan[From] = EMPTY_SPACE;
                        if(partOfThreeInARow(To))
                            remove = true;
                        else
                            turn = BLUE_MOVES;
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                if (bluemarker > 0) {
                    if (gameplan[To] == EMPTY_SPACE) {
                        gameplan[To] = BLUE_MARKER;
                        bluemarker--;
                        if(partOfThreeInARow(To))
                            remove = true;
                        else
                            turn = RED_MOVES;
                        return true;
                    }else{
                        return false;
                    }
                }
                if (gameplan[To] == EMPTY_SPACE) {
                    boolean valid = isValidMove(To, From);
                    if ((valid == true || playerOnlyHasThreeMarksLeft(player)) && gameplan[From]==BLUE_MARKER) {
                        gameplan[To] = BLUE_MARKER;
                        gameplan[From] = EMPTY_SPACE;
                        if(partOfThreeInARow(To))
                            remove = true;
                        else
                            turn = RED_MOVES;
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }



    /** GODK?NT OCH ?NDRAD AV ANDREAS LJUNGSTR?M
     *  Returns true if the selected player have less than three markers left.
     *  "Or if the player is trapped"
     */
    public boolean loss(int player) {
        if( !(player==1 || player==2) ){
            Log.i("ERROR:","Received bad int as arg in function NineMenMorrisRules.loss, received int "+player);
            return false;
        }

        if(redmarker>0 || bluemarker>0)
            return false;

        int countMarker = 0;

        if(canPlayerMakeAnyMove(player)==false)
            return true;

        for(int count = 0;count<gameplan.length;count++){
            if (gameplan[count] == player+3){
                countMarker++;
            }
        }
        if (bluemarker == 0 && redmarker == 0 && countMarker < 3)
            return true;
        else
            return false;
    }

    /** GODK?ND/?NDRAD AV ANDREAS LJUNGSTR?M
     * Request to remove a marker for the selected player.
     * Returns true if the marker where successfully removed
     */
    public boolean remove(int From, int player) {
        if( !(player==1 || player==2) ){
            Log.i("ERROR:","Received bad int as arg in function NineMenMorrisRules.remove, received int "+player);
            return false;
        }

        if (gameplan[From] == player+3) {
            if(partOfThreeInARow(From)){//If the mark is part of three in a row
                if(playerOnlyHasThreeInARows(player)){//If all the colors mark are part of three in a row
                    gameplan[From] = EMPTY_SPACE; //remove
                    remove = false;
                    turn = player;
                    return true;
                }
            }else{//If the mark is not part of three in a row
                gameplan[From] = EMPTY_SPACE; //remove
                remove = false;
                turn = player;
                return true;
            }
        }
        return false;
    }

    /** GODK?ND AV ANDREAS LJUNGSTR?M
     * Returns EMPTY_SPACE = 0 BLUE_MARKER = 4 READ_MARKER = 5
     */
    public int board(int From) {
        return gameplan[From];
    }

    /**
     * returns whos turn it is
     */
    public int getTurn(){
        return turn;
    }

    public boolean allMarksPlaced(){
        if(redmarker==0 && bluemarker==0)
            return true;
        return false;
    }

    public boolean getRemove(){
        return remove;
    }

    public int[] getBoard(){
        int[] gameplancpy = new int[25];

        for(int i =0;i<25;i++){
            gameplancpy[i] = gameplan[i];
        }
        return gameplancpy;
    }





    /** TILLAGD AV ANDREAS LJUNGSTR?M
     *  Checks if a player only has three in a row:s left.
     *  Returns true if the player only has three in a rows.
     *  Returns false if the player has marks that are not
     *  included in three in a row.
     */
    private boolean playerOnlyHasThreeInARows(int player){
        if( !(player==1 || player==2) ){
            Log.i("ERROR:","Received bad int as arg in function NineMenMorrisRules.playerOnlyHasThreeInARows, received int "+player);
            return false;
        }

        for(int count = 0;count<gameplan.length;count++){
            if (gameplan[count] == player+3){
                if(!partOfThreeInARow(count))
                    return false;
            }
        }
        return true;
    }

    /** SKAPAD AV ANDREAS LJUNGSTR?M
     * Checks if player can make any valid move or if it is trapped.
     * Returns true if the player can make a move. Or false if the player is trapped.
     */
    private boolean canPlayerMakeAnyMove(int player){
        if( !(player==1 || player==2) ){
            Log.i("ERROR:","Received bad int as arg in function NineMenMorrisRules.canPlayerMakeAnyMove, received int "+player);
            return false;
        }

        if(playerOnlyHasThreeMarksLeft(player))
            return true;

        for(int i = 0;i<gameplan.length;i++){
            if(gameplan[i]==player+3){
                for(int j=0;j<gameplan.length;j++){
                    if(isValidMove(j,i))
                        return true;
                }
            }
        }
        return false;
    }


    private boolean playerOnlyHasThreeMarksLeft(int player){
        int countermarks = 0;
        for(int i = 0;i<gameplan.length;i++){
            if(gameplan[i]==player+3){
                countermarks++;
            }
        }
        if(countermarks==3)
            return true;
        return false;
    }
    /** GODK?ND AV ANDREAS LJUNGSTR?M
     * Check whether this is a legal move.
     */
    private boolean isValidMove(int to, int from) {

        if(this.gameplan[to] != EMPTY_SPACE) return false;
        if(level==2) {
            switch (to) {
                case 1:
                    return (from == 2 || from == 4 || from == 22);
                case 2:
                    return (from == 1 || from == 5 || from == 23);
                case 7:
                    return (from == 4 || from == 8 || from == 10);
                case 8:
                    return (from == 5 || from == 7 || from == 11);
                case 13:
                    return (from == 14 || from == 16 || from == 10);
                case 14:
                    return (from == 11 || from == 13 || from == 17);
                case 19:
                    return (from == 16 || from == 20 || from == 22);
                case 20:
                    return (from == 17 || from == 19 || from == 23);
            }
        }

        if(level==3){
            switch (to) {
                case 1:
                    return (from == 2 || from == 4 || from == 22);
                case 2:
                    return (from == 1 || from == 3 || from == 5 || from == 23);
                case 3:
                    return (from == 2 || from == 6 || from == 24);
                case 7:
                    return (from == 4 || from == 8 || from == 10);
                case 8:
                    return (from == 5 || from == 7 || from == 9 || from == 11);
                case 9:
                    return (from == 6 || from == 8 || from == 12);
                case 13:
                    return (from == 14 || from == 16 || from == 10);
                case 14:
                    return (from == 11 || from == 13 || from == 15 || from == 17);
                case 15:
                    return (from == 12 || from == 14 || from == 18);
                case 19:
                    return (from == 16 || from == 20 || from == 22);
                case 20:
                    return (from == 17 || from == 19 || from == 21 || from == 23);
                case 21:
                    return (from == 18 || from == 20 || from == 24);
            }
        }
        switch (to) {
            case 1:
                return (from == 4 || from == 22);
            case 2:
                return (from == 5 || from == 23);
            case 3:
                return (from == 6 || from == 24);
            case 4:
                return (from == 1 || from == 7 || from == 5);
            case 5:
                return (from == 4 || from == 6 || from == 2 || from == 8);
            case 6:
                return (from == 3 || from == 5 || from == 9);
            case 7:
                return (from == 4 || from == 10);
            case 8:
                return (from == 5 || from == 11);
            case 9:
                return (from == 6 || from == 12);
            case 10:
                return (from == 11 || from == 7 || from == 13);
            case 11:
                return (from == 10 || from == 12 || from == 8 || from == 14);
            case 12:
                return (from == 11 || from == 15 || from == 9);
            case 13:
                return (from == 16 || from == 10);
            case 14:
                return (from == 11 || from == 17);
            case 15:
                return (from == 12 || from == 18);
            case 16:
                return (from == 13 || from == 17 || from == 19);
            case 17:
                return (from == 14 || from == 16 || from == 20 || from == 18);
            case 18:
                return (from == 17 || from == 15 || from == 21);
            case 19:
                return (from == 16 || from == 22);
            case 20:
                return (from == 17 || from == 23);
            case 21:
                return (from == 18 || from == 24);
            case 22:
                return (from == 1 || from == 19 || from == 23);
            case 23:
                return (from == 22 || from == 2 || from == 20 || from == 24);
            case 24:
                return (from == 3 || from == 21 || from == 23);
        }
        return false;
    }


    /** GODK?ND/?NDRAD AV ANDREAS LJUNGSTR?M
     * Returns true if position "to" is part of three in a row.
     */
    private boolean partOfThreeInARow(int to) {

        if(level == 3) {
            if ((to == 1 || to == 2 || to == 3) && gameplan[1] == gameplan[2]
                    && gameplan[2] == gameplan[3]) {
                return true;
            } else if ((to == 7 || to == 8 || to == 9)
                    && gameplan[7] == gameplan[8] && gameplan[8] == gameplan[9]) {
                return true;
            } else if ((to == 13 || to == 14 || to == 15)
                    && gameplan[13] == gameplan[14] && gameplan[14] == gameplan[15]) {
                return true;
            } else if ((to == 19 || to == 20 || to == 21)
                    && gameplan[19] == gameplan[20] && gameplan[20] == gameplan[21]) {
                return true;
            }
        }

        if ((to == 1 || to == 4 || to == 7) && gameplan[1] == gameplan[4]
                && gameplan[4] == gameplan[7]) {
            return true;
        } else if ((to == 2 || to == 5 || to == 8)
                && gameplan[2] == gameplan[5] && gameplan[5] == gameplan[8]) {
            return true;
        } else if ((to == 3 || to == 6 || to == 9)
                && gameplan[3] == gameplan[6] && gameplan[6] == gameplan[9]) {
            return true;
        } else if ((to == 7 || to == 10 || to == 13)
                && gameplan[7] == gameplan[10] && gameplan[10] == gameplan[13]) {
            return true;
        } else if ((to == 8 || to == 11 || to == 14)
                && gameplan[8] == gameplan[11] && gameplan[11] == gameplan[14]) {
            return true;
        } else if ((to == 9 || to == 12 || to == 15)
                && gameplan[9] == gameplan[12] && gameplan[12] == gameplan[15]) {
            return true;
        } else if ((to == 13 || to == 16 || to == 19)
                && gameplan[13] == gameplan[16] && gameplan[16] == gameplan[19]) {
            return true;
        } else if ((to == 14 || to == 17 || to == 20)
                && gameplan[14] == gameplan[17] && gameplan[17] == gameplan[20]) {
            return true;
        } else if ((to == 15 || to == 18 || to == 21)
                && gameplan[15] == gameplan[18] && gameplan[18] == gameplan[21]) {
            return true;
        } else if ((to == 1 || to == 22 || to == 19)
                && gameplan[1] == gameplan[22] && gameplan[22] == gameplan[19]) {
            return true;
        } else if ((to == 2 || to == 23 || to == 20)
                && gameplan[2] == gameplan[23] && gameplan[23] == gameplan[20]) {
            return true;
        } else if ((to == 3 || to == 24 || to == 21)
                && gameplan[3] == gameplan[24] && gameplan[24] == gameplan[21]) {
            return true;
        } else if ((to == 22 || to == 23 || to == 24)
                && gameplan[22] == gameplan[23] && gameplan[23] == gameplan[24]) {
            return true;
        } else if ((to == 4 || to == 5 || to == 6)
                && gameplan[4] == gameplan[5] && gameplan[5] == gameplan[6]) {
            return true;
        } else if ((to == 10 || to == 11 || to == 12)
                && gameplan[10] == gameplan[11] && gameplan[11] == gameplan[12]) {
            return true;
        } else if ((to == 16 || to == 17 || to == 18)
                && gameplan[16] == gameplan[17] && gameplan[17] == gameplan[18]) {
            return true;
        }
        return false;
    }

    @Override
    public String toString(){
        return "Unplaced; blue:" + bluemarker + " red: " + redmarker + " Level:" + level;
    }

    public int getRedUnPlacedMarkers(){
        return redmarker;
    }

    public int getBlueUnPlacedMarkers(){
        return bluemarker;
    }

    public int getLevel(){
        return level;
    }
    public void setLevel(int level){
        if(level != 1 && level != 2 && level != 3)
            return;
        this.level = level;
    }

}