package render;

import camera.Camera;
import model.GLObject;
import model.shape.Cube;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import shader.StaticShader;
import util.Matrix4f;
import util.Vector3f;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by msi1 on 7/6/2018.
 */
public class NormalRenderer
{
    private StaticShader shader;
    private int vaoId;

    public NormalRenderer(String vertexShader, String fragmentShader, int vaoId) throws FileNotFoundException
    {
        this.shader = new StaticShader(vertexShader, fragmentShader);
        this.vaoId = vaoId;
    }

    public void loadProjectionMatrix(Matrix4f projectionMatrix)
    {
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(GLObject object, Camera camera, Vector3f diffuseColor)
    {
        shader.start();
        shader.loadViewMatrix(camera);
        shader.loadDiffuseColor(diffuseColor);
        Matrix4f objectTransformation = object.getTransformationMatrix();
        ArrayList<Cube> cubes = object.getCubicParts();

        for (int i = 0; i < cubes.size(); i++)
        {
            Cube cube = cubes.get(i);
            renderCube(cube, i, objectTransformation);
        }

        shader.stop();
    }

    private void renderCube(Cube cube, int index, Matrix4f objectTransformation)
    {
        Matrix4f cubeTransformation = cube.getTransformationMatrix();
        cubeTransformation = objectTransformation.multiply(cubeTransformation);
        shader.loadTransformationMatrix(cubeTransformation);

        GL30.glBindVertexArray(vaoId);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, cube.getTexture().getId());

        GL11.glDrawArrays(GL11.GL_TRIANGLES, 36 * index, 36);

        GL20.glDisableVertexAttribArray(2);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

    public void cleanUp()
    {
        shader.cleanUp();
    }
}
