package com.videos.luisdalopez56.glsurface03;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLSurface extends GLSurfaceView {
    Renderizado miRender;

    public GLSurface(Context context) {
        super(context);
        miRender = new Renderizado();
        setRenderer(miRender);
    }

}

class Renderizado implements GLSurfaceView.Renderer {

    float angulo = 0.0f;
    float velocidad = 1.25f;
    float posX, posY, posZ;
    float[] misOjos = {0.0f, 0.0f, 0.0f};




    public final static int SOL = GL10.GL_LIGHT0;
    public final static int FOCO1 = GL10.GL_LIGHT1;
    public final static int FOCO2 = GL10.GL_LIGHT2;

    Esfera esfera;
    Esfera esfera1;
    Esfera esfera2;


    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        misOjos[0] = 0.0f;
        misOjos[1] = 0.0f;
        misOjos[2] = 1.3f;
        esfera = new Esfera(50, 50, .3f, 1.0f);
        esfera1 = new Esfera(50, 50, .3f, 1.0f);
        esfera2 = new Esfera(50, 50, .4f, 1.0f);
        esfera2.setPosition(0.0f, 0.0f, 0.0f);

        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        //gl.glClearDepthf(2.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        //gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glCullFace(GL10.GL_BACK);
        gl.glDepthMask(true);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glDisable(GL10.GL_DITHER);






    }

    private void encenderLuz (GL10 gl) {

        float[] foco1Posi = {-15.0f, 15.0f, 0.0f, 1.0f};
        float[] foco2Posi = {-10.0f, -4.0f, 1.0f, 1.0f};
        float[] solPosi = {0.0f, 0.0f, 0.0f, 0.0f};

        float[] azulSuave = {0.0f, 0.0f, .2f, 1.0f};
        float[] cianSuave = {0.0f, .5f, .5f, 1.0f};
        float[] magentaSuave = {.75f, 0.0f, .25f, 1.0f};
        float[] blanco = {1.0f, 1.0f, 1.0f, 1.0f};
        float[] cian = {0.0f, 1.0f, 1.0f, 1.0f};
        float[] amarillo = {1.0f, 1.0f, 0.0f, 1.0f};

        gl.glLightfv(FOCO1, GL10.GL_POSITION, hacerFloatBuffer(foco1Posi));
        gl.glLightfv(FOCO1, GL10.GL_DIFFUSE, hacerFloatBuffer(azulSuave));
        gl.glLightfv(FOCO1, GL10.GL_SPECULAR, hacerFloatBuffer(cianSuave));

        gl.glLightfv(FOCO2, GL10.GL_POSITION, hacerFloatBuffer(foco2Posi));
        gl.glLightfv(FOCO2, GL10.GL_DIFFUSE, hacerFloatBuffer(azulSuave));
        gl.glLightfv(FOCO2, GL10.GL_SPECULAR, hacerFloatBuffer(magentaSuave));

        //gl.glLightfv(SOL, GL10.GL_POSITION, hacerFloatBuffer(solPosi));
        gl.glLightfv(SOL, GL10.GL_DIFFUSE, hacerFloatBuffer(blanco));
        gl.glLightfv(SOL, GL10.GL_SPECULAR, hacerFloatBuffer(amarillo));

        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, hacerFloatBuffer(cian));
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, hacerFloatBuffer(blanco));
        gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 25);

        gl.glLightModelf(GL10.GL_LIGHT_MODEL_TWO_SIDE, 0.0f);
        gl.glLightf(SOL, GL10.GL_QUADRATIC_ATTENUATION, .001f);
        gl.glShadeModel(GL10.GL_SMOOTH);

        gl.glEnable(FOCO1);
        gl.glEnable(FOCO2);
        gl.glEnable(SOL);
        gl.glEnable(GL10.GL_LIGHTING);
    }

    protected static FloatBuffer hacerFloatBuffer(float[] arr) {
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(arr);
        fb.position(0);
        return fb;
    }

    private void ponerObjeto(Esfera esfera, GL10 gl){

        posX = esfera.m_Pos[0];
        posY = esfera.m_Pos[1];
        posZ = esfera.m_Pos[2];

        gl.glPushMatrix();

        gl.glLoadIdentity();
        gl.glTranslatef(posX, posY, posZ);
        angulo += velocidad;
        gl.glRotatef(angulo, -2.0f, 5.5f, 1.0f);

        gl.glTranslatef(-misOjos[0], -misOjos[1], -misOjos[2]);
        gl.glEnable(GL10.GL_DEPTH_TEST);

        esfera.setPosition(-0.1f,0.2f, -3.0f);
        esfera.draw(gl);
        gl.glPopMatrix();

    }

    private void ponerObjeto1(Esfera esfera, GL10 gl){

        posX = esfera.m_Pos[0];
        posY = esfera.m_Pos[1];
        posZ = esfera.m_Pos[2];

        gl.glPushMatrix();

        gl.glLoadIdentity();
        gl.glTranslatef(posX, posY, posZ);
        angulo += velocidad;
        gl.glRotatef(angulo, 5.0f, 10.6f, -4.0f);

        gl.glTranslatef(-misOjos[0], -misOjos[1], -misOjos[2]);
        gl.glEnable(GL10.GL_DEPTH_TEST);

        esfera.setPosition(0.1f,-0.2f, -3.0f);
        esfera.draw(gl);
        gl.glPopMatrix();



    }

    private void ponerObjeto2(Esfera esfera, GL10 gl){

        posX = esfera.m_Pos[0];
        posY = esfera.m_Pos[1];
        posZ = esfera.m_Pos[2];

        float rojo[] = {1.0f, 0.0f, 0.0f, 0.0f};
        float[] amarillo = {1.0f, 1.0f, 0.0f, 1.0f};

        gl.glPushMatrix();

        gl.glLoadIdentity();
        gl.glTranslatef(posX, posY, posZ);
        angulo += velocidad;
        gl.glRotatef(angulo, 5.0f, 10.6f, -4.0f);

        gl.glTranslatef(-misOjos[0], -misOjos[1], -misOjos[2]);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        esfera.setPosition(0.1f,-0.2f, -3.0f);
        esfera.draw(gl);
        gl.glPopMatrix();



    }


    @Override
    public void onSurfaceChanged(GL10 gl, int ancho, int alto) {
        if (alto == 0) alto = 1;
        float aspecto = (float) ancho / alto;
        gl.glViewport(0, 0, ancho, alto);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU.gluPerspective(gl, 45, aspecto, 0.1f, 100.f);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void onDrawFrame(GL10 gl) {

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);





        ponerObjeto(esfera, gl);
        ponerObjeto1(esfera1, gl);

        float blanco[] = {1.0f, 1.0f, 1.0f, 1.0f};
        float cian[] = {0.0f, 1.0f, 1.0f, 1.0f};
        float rojo[] = {1.0f, 0.0f, 0.0f, 0.0f};
        float amarillo[] = {1.0f, 1.0f, 0.0f, 1.0f};
        float solPosi[] = {1.0f,0.0f,0.0f,0.0f};

        gl.glLightfv(SOL, GL10.GL_POSITION, hacerFloatBuffer(solPosi));
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, hacerFloatBuffer(cian));
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, hacerFloatBuffer(blanco));
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, hacerFloatBuffer(rojo));
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, hacerFloatBuffer(cian));
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, hacerFloatBuffer(blanco));

        encenderLuz(gl);

        ponerObjeto2(esfera2, gl);

        //ponerObjeto(esfera2, gl);
        //ponerObjeto(esfera3, gl);
        //ponerObjeto(esfera4, gl);




    }



}

