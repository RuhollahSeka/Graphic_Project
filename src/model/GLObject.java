package model;

import model.shape.Cube;
import util.Vector3f;

import java.util.ArrayList;

/**
 * Created by msi1 on 7/5/2018.
 */
public abstract class GLObject
{
    private Float[] vertices;
    private float[] textureCoordinates;
    private float[] normals;
    private ArrayList<Cube> cubicParts;


    protected void initVertices()
    {
        ArrayList<Vector3f> allPoints = collectPoints();
        ArrayList<Float> verticesList = new ArrayList<>();

        for (int i = 0; i < allPoints.size(); i += 4)
        {
            addPointComponents(allPoints, i, verticesList);
            addPointComponents(allPoints, i + 1, verticesList);
        }

        vertices = verticesList.toArray(new Float[verticesList.size()]);
    }

    private void addPointComponents(ArrayList<Vector3f> allPoints, int startIndex, ArrayList<Float> verticesList)
    {
        for (int i = 0; i < 3; i++)
        {
            Vector3f point = allPoints.get(startIndex + i);
            verticesList.add(point.x);
            verticesList.add(point.y);
            verticesList.add(point.z);
        }
    }

    private ArrayList<Vector3f> collectPoints()
    {
        ArrayList<Vector3f> points = new ArrayList<>();

        for (Cube cubicPart : cubicParts)
        {
            points.addAll(cubicPart.getPoints());
        }

        return points;
    }

    protected abstract void initTextures();

    private void calculateNormals()
    {
        // TODO
    }

    public Float[] getVertices()
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
