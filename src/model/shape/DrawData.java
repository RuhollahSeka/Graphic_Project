package model.shape;

import util.Vector2f;
import util.Vector3f;

import java.util.ArrayList;

/**
 * Created by msi1 on 7/6/2018.
 */
public class DrawData
{
    private Float[] vertices;
    private Float[] normals;
    private Float[] colors; // TODO handle this later
    private Float[] textureCoordinates;

    void setVertices(ArrayList<Vector3f> points)
    {
        ArrayList<Float> verticesList = new ArrayList<>();

        for (int i = 0; i < points.size(); i += 4)
        {
            addPointComponents(points, i, verticesList);
            addPointComponents(points, i + 1, verticesList);
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

    void calculateNormals(ArrayList<Vector3f> normalsList)
    {
        normals = new Float[normalsList.size() * 3];
        for (int i = 0; i < normalsList.size(); i++)
        {
            Vector3f normal = normalsList.get(i);
            normals[i] = normal.x;
            normals[i + 1] = normal.y;
            normals[i + 2] = normal.z;
        }
    }

    void setTextureCoordinates(ArrayList<Vector2f> allTextureCoordinates)
    {
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

    public Float[] getVertices()
    {
        return vertices;
    }

    public Float[] getNormals()
    {
        return normals;
    }

    public Float[] getColors()
    {
        return colors;
    }

    public Float[] getTextureCoordinates()
    {
        return textureCoordinates;
    }
}
