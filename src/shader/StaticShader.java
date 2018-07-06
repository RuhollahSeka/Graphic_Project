package shader;

import camera.Camera;
import util.Matrix4f;
import util.Vector3f;

import java.io.FileNotFoundException;

/**
 * Created by msi1 on 3/14/2018.
 */
public class StaticShader extends ShaderProgram
{
    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_diffuseColor;

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
        location_diffuseColor = super.getUniformLocation("diffuse");
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
        super.loadMatrix(location_viewMatrix, camera.getViewMatrix());
    }

    public void loadDiffuseColor(Vector3f diffuseColor)
    {
        super.load3DVector(location_diffuseColor, diffuseColor.x, diffuseColor.y, diffuseColor.z);
    }
}
