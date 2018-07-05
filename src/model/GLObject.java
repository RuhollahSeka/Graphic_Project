package model;

import model.shape.Cube;

import java.util.ArrayList;

/**
 * Created by msi1 on 7/5/2018.
 */
public abstract class GLObject
{
    private float[] vertices;
    private float[] textureCoordinates;
    private float[] normals;
    private ArrayList<Cube> cubicParts;


    protected void initVertices()
    {

    }

    protected abstract void initTextures();

    private void calculateNormals()
    {
        // TODO
    }

    public float[] getVertices()
    {
        return vertices;
    }

    public float[] getTextureCoordinates()
    {
        return textureCoordinates;
    }

    public float[] getNormals()
    {
        return normals;
    }
}
