package com.rs.pathdemo;

import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @ProjectName: PathDemo
 * @Descpription:
 * @Author: liaorongsheng
 * @email: 1403233812@qq.com
 * @Date : 2022/9/3
 */
public class PathRender implements GLSurfaceView.Renderer {
    private static final String TAG = PathRender.class.getSimpleName();
    public static final float BRUSH_SIZE = 10;
    private int program = 0;
    private int uBrushSize;
    private static final String VERTEX_SHADER = "" +
            "attribute vec4 a_Position;\n" +
            "uniform float brushSize;\n"+
            "void main()\n" +
            "{\n" +
            "    gl_Position = a_Position;\n" +
            "    gl_PointSize = brushSize;\n" +
            "}";
    private static final String FRAGMENT_SHADER = "" +
            "precision mediump float;\n" +
            "uniform vec4 u_Color;\n" +
            "void main()\n" +
            "{\n" +
            "    gl_FragColor = u_Color;\n" +
            "}";

    public float[] POINT_DATA = new float[]{ -1f, 1f,
            1f, 1f};
    private final int POSITION_COMPONENT_COUNT = 2;
    private final int DRAW_COUNT = POINT_DATA.length / POSITION_COMPONENT_COUNT;
    private final FloatBuffer mVertexData;
    private int uColorLocation;
    private int drawIndex = 0;

    public float[] getVexData(){
        return POINT_DATA;
    }

    public PathRender() {
        mVertexData = createFloatBuffer(POINT_DATA);
    }

    private FloatBuffer createFloatBuffer(float[] array) {
        FloatBuffer buffer = ByteBuffer
                // 分配顶点坐标分量个数 * Float占的Byte位数
                .allocateDirect(array.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        // 将Dalvik的内存数据复制到Native内存中
        buffer.put(array);
        return buffer;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        makeProgram(VERTEX_SHADER, FRAGMENT_SHADER);
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        int aPositionLocation = getAttrib("a_Position");
        uColorLocation = getUniform("u_Color");
        uBrushSize = getUniform("brushSize");
        mVertexData.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false, 0, mVertexData);
        GLES20.glEnableVertexAttribArray(aPositionLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
//        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        drawIndex++;
        GLES20.glUniform1f(uBrushSize,BRUSH_SIZE);
        drawLine();
        drawPoint();
        if (drawIndex >= DRAW_COUNT) {
            drawIndex = 0;
        }
    }


    private void drawPoint() {
        GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, drawIndex);
    }

    private void drawLine() {
        // GL_LINES：每2个点构成一条线段
        // GL_LINE_LOOP：按顺序将所有的点连接起来，包括首位相连
        // GL_LINE_STRIP：按顺序将所有的点连接起来，不包括首位相连
        GLES10.glPointSize(40);
        GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_LINE_LOOP, 0, drawIndex);
    }


    private int getUniform(String name) {
        return GLES20.glGetUniformLocation(program, name);
    }

    private int getAttrib(String name) {
        return GLES20.glGetAttribLocation(program, name);
    }

    private void makeProgram(String vertexShader, String fragmentShader) {
        int vertexShaderId = compileVertexShader(vertexShader);
        int fragmentShaderId = compileFragmentShader(fragmentShader);
        // 步骤3：将顶点着色器、片段着色器进行链接，组装成一个OpenGL程序
        program = linkProgram(vertexShaderId, fragmentShaderId);
        // 步骤4：通知OpenGL开始使用该程序
        GLES20.glUseProgram(program);
    }

    private int compileVertexShader(String shaderCode) {
        return compileShader(GLES20.GL_VERTEX_SHADER, shaderCode);
    }

    private int compileFragmentShader(String shaderCode) {
        return compileShader(GLES20.GL_FRAGMENT_SHADER, shaderCode);
    }

    private int compileShader(int type, String shaderCode) {
        int shaderObjectId = GLES20.glCreateShader(type);
        if (shaderObjectId == 0) {
            // 在OpenGL中，都是通过整型值去作为OpenGL对象的引用。之后进行操作的时候都是将这个整型值传回给OpenGL进行操作。
            // 返回值0代表着创建对象失败。
            Log.e(TAG, "Could not create new shader.");
            return 0;
        }

        GLES20.glShaderSource(shaderObjectId, shaderCode);
        GLES20.glCompileShader(shaderObjectId);
        int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shaderObjectId, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
        Log.v(TAG, "Results of compiling source:" + "\n" + shaderCode + "\n:" + GLES20.glGetShaderInfoLog(shaderObjectId));
        if (compileStatus[0] == 0) {
            GLES20.glDeleteShader(shaderObjectId);
            Log.w(TAG, "Compilation of shader failed.");
            return 0;
        }
        return shaderObjectId;
    }

    private int linkProgram(int vertexShaderId, int fragmentShaderId) {
        int programObjectId = GLES20.glCreateProgram();

        if (programObjectId == 0) {
            Log.e(TAG, "Could not create new program");
            return 0;
        }

        GLES20.glAttachShader(programObjectId, vertexShaderId);
        GLES20.glAttachShader(programObjectId, fragmentShaderId);
        GLES20.glLinkProgram(programObjectId);
        int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(programObjectId, GLES20.GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] == 0) {
            GLES20.glDeleteProgram(programObjectId);
            Log.w(TAG, "Linking of program failed.");
            return 0;
        }
        return programObjectId;
    }

    private boolean validateProgram(int programObjectId) {
        GLES20.glValidateProgram(programObjectId);
        int[] validateStatus = new int[1];
        GLES20.glGetProgramiv(programObjectId, GLES20.GL_VALIDATE_STATUS, validateStatus, 0);
        Log.v(TAG, "Results of validating program: " + validateStatus[0]
                + "\nLog:" + GLES20.glGetProgramInfoLog(programObjectId));
        return validateStatus[0] != 0;
    }
}
