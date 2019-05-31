package test.gameapp.VIEW.GAME.ImageTypes;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import test.gameapp.VIEW.RendTools;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.GL_TEXTURE0;


/*********
 *  Innehåller standardlösningar för OpenglES som är tagna från internet.
 *
 *
 * //klassen används som mall för bild-objekt till spelet
 * KOD HAR KOPIERATS FRÅN http://androidblog.reindustries.com/a-real-opengl-es-2-0-2d-tutorial-part-2/
 *
 */
public class ItemImage {

    // Geometric variables
    private float vertices[];
    private short indices[];
    private float uvs[];
    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;
    private FloatBuffer uvBuffer;
    private float bildTyp;
    private boolean drawCheck;

    private boolean isEnemyPlayer;

    //Ändra var en bild ska ritas upp på mobilen om den ska ritas alls
    public void setCoordinates(float x, float y, float angle,float mainX, float mainY) {
        float tX = 0; float tY = 0;
        drawCheck = false;
        float eX = 0; float eY = 0;

        if(200 > (mainX - x) && (mainX - x) > -200){
            if(200 > (mainY - y) && (mainY - y) > -200){
                drawCheck = true;
                tX =  (mainX - x);
                tY = (mainY - y);

                eX = ((tX/400))*2;
                eY = (tY/400)*2;
            }
        }

        if(bildTyp == 2 || bildTyp == 3 || bildTyp >= 10){
            setVerts(eX - 0.12f, eY - 0.12f,       // The bottom left corner
                    eX - 0.12f, eY + 0.12f,        // The top left corner
                    eX + 0.12f, eY + 0.12f,        // The top right corner
                    eX + 0.12f, eY - 0.12f);       // The bottom right corner
        }else{

            if(isEnemyPlayer){
                switch((int) angle){
                    case 0: //up
                        setVerts(eX - 0.12f, eY + 0.12f,       // The bottom left corner
                                eX - 0.12f, eY - 0.12f,        // The top left corner
                                eX + 0.12f, eY - 0.12f,        // The top right corner
                                eX + 0.12f, eY + 0.12f);       // The bottom right corner
                        break;
                    case 180: //down
                        setVerts(eX - 0.12f, eY - 0.12f,       // The bottom left corner
                                eX - 0.12f, eY + 0.12f,        // The top left corner
                                eX + 0.12f, eY + 0.12f,        // The top right corner
                                eX + 0.12f, eY - 0.12f);       // The bottom right corner
                        break;
                    default:
                        setVerts(eX - 0.12f, eY - 0.12f,       // The bottom left corner
                                eX - 0.12f, eY + 0.12f,        // The top left corner
                                eX + 0.12f, eY + 0.12f,        // The top right corner
                                eX + 0.12f, eY - 0.12f);       // The bottom right corner
                        break;
                }
            }
            if(bildTyp == 4){
                setVerts(eX - 0.06f, eY - 0.06f,       // The bottom left corner
                        eX - 0.06f, eY + 0.06f,        // The top left corner
                        eX + 0.06f, eY + 0.06f,        // The top right corner
                        eX + 0.06f, eY - 0.06f);       // The bottom right corner
            }

        }
    }

    public void setEnemyPlayer(boolean isEnemyPlayer){
        this.isEnemyPlayer = isEnemyPlayer;
    }

    public float getBildTyp() {
        return bildTyp;
    }
    public void setBildTyp(float bildTyp){
        this.bildTyp = bildTyp;
    }

    private void SetupTriangle() {
        // We have to create the vertices of our triangle.
        vertices = new float[]
                {0.0f, 0f, 0.0f,
                        0.0f, 1f, 0.0f,
                        1f, 1f, 0.0f,
                        1f, 0f, 0.0f,
                };

        indices = new short[]{0, 1, 2, 0, 2, 3}; // The order of vertexrendering.

        // The vertex buffer.
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(indices);
        drawListBuffer.position(0);


    }

    public void setVerts(float v0, float v1, float v2, float v3, float v4, float v5, float v6, float v7) {
        vertices[0] = v0;
        vertices[1] = v1;

        vertices[3] = v2;
        vertices[4] = v3;

        vertices[6] = v4;
        vertices[7] = v5;

        vertices[9] = v6;
        vertices[10] = v7;

        vertexBuffer.put(vertices);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);
    }

    private void SetupImage() {
        // Create our UV coordinates.
        uvs = new float[]{
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f
        };

        // The texture buffer
        ByteBuffer bb = ByteBuffer.allocateDirect(uvs.length * 4);
        bb.order(ByteOrder.nativeOrder());
        uvBuffer = bb.asFloatBuffer();
        uvBuffer.put(uvs);
        uvBuffer.position(0);
    }

    public ItemImage(float bildTyp) {
        isEnemyPlayer = false;
        this.bildTyp = bildTyp;
        drawCheck = true;
        SetupTriangle();
        SetupImage();
        int vertexShader = RendTools.loadShader(GLES20.GL_VERTEX_SHADER, RendTools.vs_SolidColor);
        int fragmentShader = RendTools.loadShader(GLES20.GL_FRAGMENT_SHADER, RendTools.fs_SolidColor);

        sp_SolidColor = GLES20.glCreateProgram();             // create empty OpenGL ES Program
        GLES20.glAttachShader(sp_SolidColor, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(sp_SolidColor, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(sp_SolidColor);                  // creates OpenGL ES program executables

        // Create the shaders, images
        vertexShader = RendTools.loadShader(GLES20.GL_VERTEX_SHADER, RendTools.vs_Image);
        fragmentShader = RendTools.loadShader(GLES20.GL_FRAGMENT_SHADER, RendTools.fs_Image);

        sp_Image = GLES20.glCreateProgram();             // create empty OpenGL ES Program
        GLES20.glAttachShader(sp_Image, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(sp_Image, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(sp_Image);                  // creates OpenGL ES program executables

        // Set our shader programm
        GLES20.glUseProgram(sp_Image);

    }

    public void draw(float[] m, int idTex) {

        if(!drawCheck)
            return;

        // get handle to vertex shader's vPosition member
        int mPositionHandle =
                GLES20.glGetAttribLocation(sp_Image, "vPosition");

        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, 3,
                GLES20.GL_FLOAT, false,
                0, vertexBuffer);

        // Get handle to texture coordinates location
        int mTexCoordLoc = GLES20.glGetAttribLocation(sp_Image,
                "a_texCoord");

        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray(mTexCoordLoc);

        // Prepare the texturecoordinates
        GLES20.glVertexAttribPointer(mTexCoordLoc, 2, GLES20.GL_FLOAT,
                false,
                0, uvBuffer);

        // Get handle to shape's transformation matrix
        int mtrxhandle = GLES20.glGetUniformLocation(sp_Image,
                "uMVPMatrix");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, m, 0);

        // Get handle to textures locations
        int mSamplerLoc = GLES20.glGetUniformLocation(sp_Image,
                "s_texture");

        GLES20.glUniform1i(mSamplerLoc, 0);
        GLES20.glActiveTexture(GL_TEXTURE0);
        GLES20.glBindTexture(GL10.GL_TEXTURE_2D, idTex);

        GLES20.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glEnable(GL_BLEND);

        // Draw the triangle
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);
        GLES20.glDisable(mTexCoordLoc);
        GLES20.glDisable(GL_BLEND);

    }


    private static int sp_SolidColor;
    private static int sp_Image;


}
