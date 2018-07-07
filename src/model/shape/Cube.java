package model.shape;

import model.TransformationData;
import model.Visibility;
import texture.Texture;
import util.Matrix4f;
import util.Vector2f;
import util.Vector3f;

import java.util.ArrayList;

/**
 * Created by msi1 on 7/5/2018.
 */
public class Cube
{
    private Texture texture;
    private Visibility visibility;
    private ArrayList<Vector3f> points;
    private ArrayList<Vector3f> normals;
    private ArrayList<Vector2f> textureCoordinates;
    private DrawData drawData;
    private TransformationData transformationData;

    private Vector3f center;
    private float width;
    private float height;
    private float depth;

    public Cube(Vector3f center, float width, float height, float depth, Visibility visibility, String texturePath)
    {
        this.center = center;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.visibility = visibility;

        this.texture = Texture.loadTexture(texturePath);
        this.points = new ArrayList<>();
        this.normals = new ArrayList<>();
        this.textureCoordinates = new ArrayList<>();
        this.drawData = new DrawData();
        this.transformationData = new TransformationData();

        addPoints();
        calculateNormals();
        addTextureCoordinates();
        drawData.setVertices(this.points);
        drawData.calculateNormals(this.normals);
        drawData.setTextureCoordinates(this.textureCoordinates);
    }


    public Cube(Vector3f center, float width, float height, float depth, Visibility visibility, String texturePath, float x, float y)
    {
        this.center = center;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.visibility = visibility;

        this.texture = Texture.loadTexture(texturePath);
        this.points = new ArrayList<>();
        this.normals = new ArrayList<>();
        this.textureCoordinates = new ArrayList<>();
        this.drawData = new DrawData();
        this.transformationData = new TransformationData();

        this.texture.setX(x);
        this.texture.setY(y);

        addPoints();
        calculateNormals();
        addTiledTextureCoordinates();
        drawData.setVertices(this.points);
        drawData.calculateNormals(this.normals);
        drawData.setTextureCoordinates(this.textureCoordinates);
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

    private void calculateNormals()
    {
        for (int i = 0; i < points.size(); i += 4)
        {
            Vector3f normal = getNormal(i);
            normals.add(normal);
            normals.add(normal);
            normals.add(normal);
            normals.add(normal);
            normals.add(normal);
            normals.add(normal);
        }
    }

    private void addTextureCoordinates()
    {
        for (int i = 1; i <= 6; i++)
        {
            textureCoordinates.add(new Vector2f(((float) i - 1.0f) * 1.0f / 6.0f, 0.0f));
            textureCoordinates.add(new Vector2f((float) i * 1.0f / 6.0f, 0.0f));
            textureCoordinates.add(new Vector2f(((float) i - 1.0f) * 1.0f / 6.0f, 1.0f));
            textureCoordinates.add(new Vector2f((float) i * 1.0f / 6.0f, 1.0f));
        }
    }

    private void addTiledTextureCoordinates()
    {
        float texX = texture.getX();
        float texY = texture.getY();

        //front
        textureCoordinates.add(new Vector2f(0.0f, 0.0f));
        textureCoordinates.add(new Vector2f( width / texX, 0.0f));
        textureCoordinates.add(new Vector2f(0.0f, height / texY));
        textureCoordinates.add(new Vector2f(width / texX, height / texY));

        //back
        textureCoordinates.add(new Vector2f(0.0f, 0.0f));
        textureCoordinates.add(new Vector2f( width / texX, 0.0f));
        textureCoordinates.add(new Vector2f(0.0f, height / texY));
        textureCoordinates.add(new Vector2f(width / texX, height / texY));

        //right
        textureCoordinates.add(new Vector2f(0.0f, 0.0f));
        textureCoordinates.add(new Vector2f( depth / texX, 0.0f));
        textureCoordinates.add(new Vector2f(0.0f, height / texY));
        textureCoordinates.add(new Vector2f(depth / texX, height / texY));

        //left
        textureCoordinates.add(new Vector2f(0.0f, 0.0f));
        textureCoordinates.add(new Vector2f( depth / texX, 0.0f));
        textureCoordinates.add(new Vector2f(0.0f, height / texY));
        textureCoordinates.add(new Vector2f(depth / texX, height / texY));

        //top
        textureCoordinates.add(new Vector2f(0.0f, 0.0f));
        textureCoordinates.add(new Vector2f( width / texX, 0.0f));
        textureCoordinates.add(new Vector2f(0.0f, depth / texY));
        textureCoordinates.add(new Vector2f(width / texX, depth / texY));

        //bottom
        textureCoordinates.add(new Vector2f(0.0f, 0.0f));
        textureCoordinates.add(new Vector2f( width / texX, 0.0f));
        textureCoordinates.add(new Vector2f(0.0f, depth / texY));
        textureCoordinates.add(new Vector2f(width / texX, depth / texY));
    }

    private Vector3f getNormal(int startPointIndex)
    {
        Vector3f firstPoint = points.get(startPointIndex);
        Vector3f secondPoint = points.get(startPointIndex + 1);
        Vector3f thirdPoint = points.get(startPointIndex + 2);

        Vector3f vectorFromCenter = firstPoint.subtract(center);
        Vector3f normal = firstPoint.subtract(secondPoint).cross(thirdPoint.subtract(secondPoint));
        if ((normal.dot(vectorFromCenter) < 0 && visibility == Visibility.VisibleOutside) ||
                (normal.dot(vectorFromCenter) > 0 && visibility == Visibility.VisibleInside))
        {
            normal = normal.negate();
        }

        return normal;
    }

    public Matrix4f getTransformationMatrix()
    {
        return transformationData.getTransformationMatrix();
    }

    public void setTransformationData(TransformationData transformationData)
    {
        this.transformationData = transformationData;
    }

    public ArrayList<Vector3f> getPoints()
    {
        return points;
    }

    public ArrayList<Vector3f> getNormals()
    {
        return normals;
    }

    public ArrayList<Vector2f> getTextureCoordinates()
    {
        return textureCoordinates;
    }

    public DrawData getDrawData()
    {
        return drawData;
    }

    public Texture getTexture()
    {
        return texture;
    }
}
