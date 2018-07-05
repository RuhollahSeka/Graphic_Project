package shader;

import camera.Camera;
import util.Calculator;
import util.Matrix4f;

import java.io.FileNotFoundException;

/**
 * Created by msi1 on 3/14/2018.
 */
public class StaticShader extends ShaderProgram
{
    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;

    public StaticShader(String vertexShaderFileName, String fragmentShaderFileName) throws FileNotFoundException
    {
        super(vertexShaderFileName, fragmentShaderFileName);
        getAllUniformLocations();
    }

    @Override
    protected void getAllUniformLocations()
    {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
    }

    public void loadTransformationMatrix(Matrix4f matrix)
    {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix)
    {
        super.loadMatrix(location_projectionMatrix, matrix);
    }

    public void loadViewMatrix(Camera camera)
    {
        super.loadMatrix(location_viewMatrix, Calculator.createViewMatrix(camera));
    }
}
