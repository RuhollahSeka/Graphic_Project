package model;

import model.shape.Cube;
import util.Vector2f;
import util.Vector3f;

import java.util.ArrayList;

/**
 * Created by msi1 on 7/5/2018.
 */
public abstract class GLObject
{
    private Float[] vertices;
    private float[] normals;
    private Float[] textureCoordinates;
    private ArrayList<Cube> cubicParts;

    public GLObject()
    {
        cubicParts = new ArrayList<>();
        initVertices();
        calculateNormals();
        initTextureCoordinates();
    }

    // Add Vertices
    private void initVertices()
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

    // Add Normals
    private void calculateNormals()
    {
        ArrayList<Vector3f> normalsList = collectNormals();
        normals = new float[normalsList.size() * 3];
        for (int i = 0; i < normalsList.size(); i++)
        {
            Vector3f normal = normalsList.get(i);
            normals[i] = normal.x;
            normals[i + 1] = normal.y;
            normals[i + 2] = normal.z;
        }
    }

    private ArrayList<Vector3f> collectNormals()
    {
        ArrayList<Vector3f> allNormals = new ArrayList<>();

        for (Cube cubicPart : cubicParts)
        {
            allNormals.addAll(cubicPart.getNormals());
        }

        return allNormals;
    }

    // Add Texture Coordinates
    private void initTextureCoordinates()
    {
        ArrayList<Vector2f> allTextureCoordinates = collectTextureCoordinates();
        ArrayList<Float> coordinatesList = new ArrayList<>();

        for (int i = 0; i < allTextureCoordinates.size(); i += 4)
        {
            addTextureComponents(allTextureCoordinates, i, coordinatesList);
            addTextureComponents(allTextureCoordinates, i + 1, coordinatesList);
        }

        textureCoordinates = coordinatesList.toArray(new Float[coordinatesList.size()]);
    }

    private void addTextureComponents(ArrayList<Vector2f> allCoordinates, int startIndex, ArrayList<Float> coordinatesList)
    {
        for (int i = 0; i < 3; i++)
        {
            Vector2f textureCoordinate = allCoordinates.get(startIndex + i);
            coordinatesList.add(textureCoordinate.x);
            coordinatesList.add(textureCoordinate.y);
        }
    }

    private ArrayList<Vector2f> collectTextureCoordinates()
    {
        ArrayList<Vector2f> allTextureCoordinates = new ArrayList<>();

        for (Cube cubicPart : cubicParts)
        {
            allTextureCoordinates.addAll(cubicPart.getTextureCoordinates());
        }

        return allTextureCoordinates;
    }

    public Float[] getVertices()
    {
        return vertices;
    }

    public Float[] getTextureCoordinates()
    {
        return textureCoordinates;
    }

    public float[] getNormals()
    {
        return normals;
    }
}
