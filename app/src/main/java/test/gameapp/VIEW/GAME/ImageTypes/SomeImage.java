package test.gameapp.VIEW.GAME.ImageTypes;

import android.content.Context;
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

/**
 * Created by Marcus on 2017-01-07.
 */

/*********
 *  Innehåller standardlösningar för OpenglES som är tagna från internet.
 *
 *
 * //klassen används som mall för bild-objekt till spelet
 *
 * //KOD HAR KOPIERATS FRÅN http://androidblog.reindustries.com/a-real-opengl-es-2-0-2d-tutorial-part-2/
 */
abstract class SomeImage {

    // Geometric variables
    protected float vertices[];
    protected short indices[];
    protected float uvs[];
    protected FloatBuffer vertexBuffer;
    protected ShortBuffer drawListBuffer;
    protected FloatBuffer uvBuffer;
    protected int position;
    protected boolean shouldDraw;

    protected static int sp_SolidColor;
    protected static int sp_Image;

    protected abstract void setupTriangle(); //säg åt sub-klasser att sätta upp koordinater för bilden
    private void SetupImage() {
        // Create our UV coordinates.
        uvs = new float[]{
                0.0f, 0.0f,
                0.0f, 0.1f,
                0.1f, 0.1f,
                0.1f, 0.0f
        };

        // The texture buffer
        ByteBuffer bb = ByteBuffer.allocateDirect(uvs.length * 4);
        bb.order(ByteOrder.nativeOrder());
        uvBuffer = bb.asFloatBuffer();
        uvBuffer.put(uvs);
        uvBuffer.position(0);
    }

    protected void initVertices(){
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

    protected void initUvBuff(){
        // The texture buffer
        ByteBuffer bb = ByteBuffer.allocateDirect(uvs.length * 4);
        bb.order(ByteOrder.nativeOrder());
        uvBuffer = bb.asFloatBuffer();
        uvBuffer.put(uvs);
        uvBuffer.position(0);
    }

    public SomeImage(){
        shouldDraw = true;
        regularInit();
    }

    public SomeImage(int position) {
        this.position = position;
        shouldDraw = true;
        regularInit();
    }

    private void regularInit(){
        setupTriangle();
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

        if(!shouldDraw)
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


}
