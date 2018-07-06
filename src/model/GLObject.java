package model;

import model.shape.Cube;
import util.Vector2f;

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
        initTextureCoordinates();
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
