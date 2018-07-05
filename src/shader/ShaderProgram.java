package shader;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import util.Matrix4f;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.FloatBuffer;
import java.util.Scanner;

/**
 * Created by msi1 on 3/13/2018.
 */
public abstract class ShaderProgram
{
    private int programId;
    private int vertexShaderId;
    private int fragmentShaderId;

    private FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    public ShaderProgram(String vertexShaderFileName, String fragmentShaderFileName) throws FileNotFoundException
    {
        programId = GL20.glCreateProgram();
        vertexShaderId = loadShader(vertexShaderFileName, GL20.GL_VERTEX_SHADER);
        fragmentShaderId = loadShader(fragmentShaderFileName, GL20.GL_FRAGMENT_SHADER);

        GL20.glAttachShader(programId, vertexShaderId);
        GL20.glAttachShader(programId, fragmentShaderId);
        GL20.glLinkProgram(programId);
        GL20.glValidateProgram(programId);
    }

    protected abstract void getAllUniformLocations();

    protected int getUniformLocation(String varName)
    {
        return GL20.glGetUniformLocation(programId, varName);
    }

    protected void loadFloat(int location, float value)
    {
        GL20.glUniform1f(location, value);
    }

    protected void load2DVector(int location, float firstValue, float secondValue)
    {
        GL20.glUniform2f(location, firstValue, secondValue);
    }

    protected void load3DVector(int location, float firstValue, float secondValue, float thirdValue)
    {
        GL20.glUniform3f(location, firstValue, secondValue, thirdValue);
    }

    protected void loadMatrix(int location, Matrix4f mat)
    {
        mat.toBuffer(matrixBuffer);
        GL20.glUniformMatrix4fv(location, false, matrixBuffer);
    }

    protected void loadBoolean(int location, boolean bool)
    {
        float value = 0.0f;

        if (bool)
        {
            value = 1.0f;
        }

        GL20.glUniform1f(location, value);
    }

    private int loadShader(String fileName, int shaderType) throws FileNotFoundException
    {
        String shaderSource = getFileContents(fileName);
        int shaderId = GL20.glCreateShader(shaderType);
        GL20.glShaderSource(shaderId, shaderSource);
        GL20.glCompileShader(shaderId);

        if (GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
        {
            System.err.println("Failed to compile!");
            System.exit(-1);
        }

        return shaderId;
    }

    private String getFileContents(String fileName) throws FileNotFoundException
    {
        StringBuilder stringBuilder = new StringBuilder();

        try(Scanner in = new Scanner(new File(fileName)))
        {
            while (in.hasNext())
            {
                stringBuilder.append(in.nextLine()).append("\n");
            }
        }

        return stringBuilder.toString();
    }

    public void start()
    {
        GL20.glUseProgram(programId);
    }

    public void stop()
    {
        GL20.glUseProgram(0);
    }

    public void cleanUp()
    {
        stop();
        GL20.glDetachShader(programId, vertexShaderId);
        GL20.glDetachShader(programId, fragmentShaderId);
        GL20.glDeleteShader(vertexShaderId);
        GL20.glDeleteShader(fragmentShaderId);
        GL20.glDeleteProgram(programId);
    }

    public int getProgramId()
    {
        return programId;
    }

    public int getVertexShaderId()
    {
        return vertexShaderId;
    }

    public int getFragmentShaderId()
    {
        return fragmentShaderId;
    }
}
