package model;

import model.shape.Cube;
import util.Matrix4f;
import util.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by msi1 on 7/5/2018.
 */
public class GLObject
{
    private ArrayList<Cube> cubicParts;
    private HashMap<String, Cube> importantParts;
    private TransformationData transformationData;

    public GLObject()
    {
        this.cubicParts = new ArrayList<>();
        this.importantParts = new HashMap<>();
    }

    public void addCube(Vector3f center, float width, float height, float depth,
                        Visibility visibility, String texturePath)
    {
        cubicParts.add(new Cube(center, width, height, depth, visibility, texturePath));
    }


    public void addCube(Vector3f center, float width, float height, float depth,
                        Visibility visibility, String texturePath, float x, float y)
    {
        cubicParts.add(new Cube(center, width, height, depth, visibility, texturePath, x, y));
    }


    public void addCube(Vector3f center, float width, float height, float depth,
                        Visibility visibility, String texturePath, String name)
    {
        Cube newCube = new Cube(center, width, height, depth, visibility, texturePath);
        cubicParts.add(newCube);
        importantParts.put(name, newCube);
    }

    public void addCube(Vector3f center, float width, float height, float depth,
                        Visibility visibility, String texturePath, String name, float x, float y)
    {
        Cube newCube = new Cube(center, width, height, depth, visibility, texturePath, x, y);
        cubicParts.add(newCube);
        importantParts.put(name, newCube);
    }

    public Cube getPart(String partName)
    {
        return importantParts.get(partName);
    }

    public Matrix4f getTransformationMatrix()
    {
        return transformationData.getTransformationMatrix();
    }

    public ArrayList<Cube> getCubicParts()
    {
        return cubicParts;
    }
}
