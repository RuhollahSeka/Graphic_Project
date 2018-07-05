package model.shape;

import util.Vector3f;

import java.util.ArrayList;

/**
 * Created by msi1 on 7/5/2018.
 */
public class Cube
{
    private ArrayList<Vector3f> points;
    private Vector3f center;
    private float width;
    private float height;
    private float depth;

    public Cube(Vector3f center, float width, float height, float depth)
    {
        this.center = center;
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    private void addPoints()
    {
        float minX = center.x - (width / 2.0f);
        float maxX = center.x + (width / 2.0f);
        float minY = center.y - (height / 2.0f);
        float maxY = center.y + (height / 2.0f);
        float minZ = center.z - (depth / 2.0f);
        float maxZ = center.z + (depth / 2.0f);

        // front
        points.add(new Vector3f(minX, maxY, maxZ));
        points.add(new Vector3f(maxX, maxY, maxZ));
        points.add(new Vector3f(minX, minY, maxZ));
        points.add(new Vector3f(maxX, minY, maxZ));

        // back
        points.add(new Vector3f(minX, maxY, minZ));
        points.add(new Vector3f(maxX, maxY, minZ));
        points.add(new Vector3f(minX, minY, minZ));
        points.add(new Vector3f(maxX, minY, minZ));

        // right
        points.add(new Vector3f(maxX, maxY, maxZ));
        points.add(new Vector3f(maxX, maxY, minZ));
        points.add(new Vector3f(maxX, minY, maxZ));
        points.add(new Vector3f(maxX, minY, minZ));

        // left
        points.add(new Vector3f(minX, maxY, maxZ));
        points.add(new Vector3f(minX, maxY, minZ));
        points.add(new Vector3f(minX, minY, maxZ));
        points.add(new Vector3f(minX, minY, minZ));

        // top
        points.add(new Vector3f(minX, maxY, minZ));
        points.add(new Vector3f(maxX, maxY, minZ));
        points.add(new Vector3f(minX, maxY, maxZ));
        points.add(new Vector3f(maxX, maxY, maxZ));

        // bottom
        points.add(new Vector3f(minX, minY, minZ));
        points.add(new Vector3f(maxX, minY, minZ));
        points.add(new Vector3f(minX, minY, maxZ));
        points.add(new Vector3f(maxX, minY, maxZ));
    }

    public ArrayList<Vector3f> getPoints()
    {
        return points;
    }
}
