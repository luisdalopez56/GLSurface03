package com.videos.luisdalopez56.glsurface03;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;


public class Esfera {

    FloatBuffer m_VertexData;
    FloatBuffer m_NormalData;
    FloatBuffer m_ColorData;

    public float[] m_Pos = {0.0f, 0.0f, 0.0f};

    float m_Scale;
    float m_Proporcion;
    float m_Radio;
    int m_Pilas;
    int m_Porciones;

    public Esfera(int pilas, int porciones, float radio, float proporcion) {
        this.m_Pilas = pilas;
        this.m_Porciones = porciones;
        this.m_Radio = radio;
        this.m_Proporcion = proporcion;

        init(m_Pilas, m_Porciones, radio, proporcion, "durmiendo");
    }

    private void init(int pilas, int porciones, float radio, float proporcion, String textureFile) {
        float[] vertexData;
        float[] colorData;
        float[] normalData;
        float colorIncrement = 0f;

        float blue = 0f;
        float red = 1.0f;
        int vIndex = 0;                //VERTEX INDEX
        int cIndex = 0;                //COLOR INDEX
        int nIndex = 0;                //NORMAL INDEX
        m_Scale = radio;
        m_Proporcion = proporcion;

        colorIncrement = 1.0f / (float) pilas;

        {
            m_Pilas = pilas;
            m_Porciones = porciones;

            //VERTICES

            vertexData = new float[3 * ((m_Porciones * 2 + 2) * m_Pilas)];

            //DATOS DE COLOR

            colorData = new float[(4 * (m_Porciones * 2 + 2) * m_Pilas)];

            //DATOS DE NORMALIZACIÓN

            normalData = new float[(3 * (m_Porciones * 2 + 2) * m_Pilas)];

            int phiIdx, thetaIdx;

            //CÍRCULOS VERTICALES

            for (phiIdx = 0; phiIdx < m_Pilas; phiIdx++) {
                //INICIO EN -90º (-1.57 RADIANES) HASTA +90º (+1.57 radians)

                //PRIMER CÍRCULO

                float phi0 = (float) Math.PI * ((float) (phiIdx + 0) * (1.0f / (float) (m_Pilas)) - 0.5f);

                //SIGUIENTES CÍRCULOS

                float phi1 = (float) Math.PI * ((float) (phiIdx + 1) * (1.0f / (float) (m_Pilas)) - 0.5f);

                float cosPhi0 = (float) Math.cos(phi0);
                float sinPhi0 = (float) Math.sin(phi0);
                float cosPhi1 = (float) Math.cos(phi1);
                float sinPhi1 = (float) Math.sin(phi1);

                float cosTheta, sinTheta;

                //CÍRCULOS HORIZONTALES

                for (thetaIdx = 0; thetaIdx < m_Porciones; thetaIdx++) {
                    //INCREMENTO DE LONGITUD EN CADA PORCIÓN DE CÍRCULO

                    float theta = (float) (2.0f * (float) Math.PI * ((float) thetaIdx) * (1.0 / (float) (m_Porciones - 1)));
                    cosTheta = (float) Math.cos(theta);
                    sinTheta = (float) Math.sin(theta);

                    //GENERACIÓN DE PARES DE PUNTOS PARA REALIZAR LOS TRIÁNGULOS
                    //OBTIENE X, Y, Z PRA EL PRIMER VERTEX DE LA PILA

                    vertexData[vIndex + 0] = m_Scale * cosPhi0 * cosTheta;
                    vertexData[vIndex + 1] = m_Scale * (sinPhi0 * m_Proporcion);
                    vertexData[vIndex + 2] = m_Scale * (cosPhi0 * sinTheta);

                    vertexData[vIndex + 3] = m_Scale * cosPhi1 * cosTheta;
                    vertexData[vIndex + 4] = m_Scale * (sinPhi1 * m_Proporcion);
                    vertexData[vIndex + 5] = m_Scale * (cosPhi1 * sinTheta);

                    colorData[cIndex + 0] = (float) red;
                    colorData[cIndex + 1] = (float) 0f;
                    colorData[cIndex + 2] = (float) blue;
                    colorData[cIndex + 3] = (float) 1.0;
                    colorData[cIndex + 4] = (float) red;
                    colorData[cIndex + 5] = (float) 0f;
                    colorData[cIndex + 6] = (float) blue;
                    colorData[cIndex + 7] = (float) 1.0;

                    //NORMALIZAR DE PUNTEROS DE DATOS PARA LA ILUMINACIÓN
                    normalData[nIndex + 0] = cosPhi0 * cosTheta;
                    normalData[nIndex + 1] = sinPhi0;
                    normalData[nIndex + 2] = cosPhi0 * sinTheta;

                    normalData[nIndex + 3] = cosPhi1 * cosTheta;
                    normalData[nIndex + 4] = sinPhi1;
                    normalData[nIndex + 5] = cosPhi1 * sinTheta;


                    cIndex += 2 * 4;
                    vIndex += 2 * 3;
                    nIndex += 2 * 3;
                }

                //blue-=colorIncrement;
                red -= colorIncrement;

                // CREACIÓN DE UN TRIÁNGULO DEGRADADO PARA CONECTAR PILAS

                vertexData[vIndex + 0] = vertexData[vIndex + 3] = vertexData[vIndex - 3];
                vertexData[vIndex + 1] = vertexData[vIndex + 4] = vertexData[vIndex - 2];
                vertexData[vIndex + 2] = vertexData[vIndex + 5] = vertexData[vIndex - 1];
            }

        }

        m_VertexData = hacerFloatBuffer(vertexData);
        m_ColorData = hacerFloatBuffer(colorData);
        m_NormalData = hacerFloatBuffer(normalData);
    }

    public void draw(GL10 gl) {
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glCullFace(GL10.GL_BACK);

        gl.glNormalPointer(GL10.GL_FLOAT, 0, m_NormalData);
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_VertexData);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        gl.glColorPointer(4, GL10.GL_FLOAT, 0, m_ColorData);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, (m_Porciones + 1) * 2 * (m_Pilas - 1) + 2);
    }

    public void setPosition(float x, float y, float z) {
        m_Pos[0] = x;
        m_Pos[1] = y;
        m_Pos[2] = z;
    }

    protected static FloatBuffer hacerFloatBuffer(float[] arr) {
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(arr);
        fb.position(0);
        return fb;
    }
}
