package model;

import model.shape.Cube;
import util.Matrix4f;
import util.Vector3f;

import java.util.ArrayList;

public class Tree
{
    private ArrayList<Tree> children;
    private Tree parent;
    private Cube body;
    private int maximumDepth;

    public Tree(Tree parent, Cube body, int maximumDepth, int currentDepth)
    {
        this.children = new ArrayList<>();
        this.parent = parent;
        this.body = body;
        this.maximumDepth = maximumDepth;
        createChildren(maximumDepth, currentDepth);
    }

    private void createChildren(int maximumDepth, int currentDepth)
    {
        if (maximumDepth == 1)
        {
            return;
        }

        Vector3f bodyCenter = body.getCenter();

        for (int i = 0; i < 4; i++)
        {
            RotationAxisType rotationAxisType = i % 2 == 0 ? RotationAxisType.ParallelX : RotationAxisType.ParallelZ;
            float angle = i < 2 ? -60.0f : 60.0f;
            float scale = 2.0f / 3.0f;

            Vector3f childCenter = new Vector3f(bodyCenter.x, bodyCenter.y - body.getHeight()*5.0f/6.0f, bodyCenter.z);
            Cube childBody = new Cube(childCenter, body.getWidth() * scale, body.getHeight() * scale, body.getDepth() * scale,
                    Visibility.VisibleOutside, "textures\\knobTile.jpg");
            childBody.setTransformationData(new TransformationData(new Vector3f(childCenter.x, childCenter.y + childBody.getHeight()/2.0f, childCenter.z),
                    rotationAxisType));
//            childBody.setScale(2.0f / 3.0f);
            childBody.setRotation(angle);

            children.add(new Tree(this, childBody, maximumDepth - 1, currentDepth + 1));
        }
    }

    public Tree(Cube body, int maximumDepth, int currentDepth)
    {
        this(null, body, maximumDepth, currentDepth);
    }

    public Matrix4f getTransformationMatrix()
    {
        Tree p = parent;
        Matrix4f transformationMatrix = body.getTransformationMatrix();

        while (p != null)
        {
            transformationMatrix = p.getBody().getTransformationMatrix().multiply(transformationMatrix);
            p = p.getParent();
        }

        return transformationMatrix;
    }

    public int getNumberOfTrees()
    {
        int result = 0;

        for (int i = 0; i < maximumDepth; i++)
        {
            result += Math.pow(4, i);
        }

        return result;
    }

    public ArrayList<Tree> getAllTrees()
    {
        ArrayList<Tree> trees = new ArrayList<>();
        trees.add(this);

        for (Tree child : children)
        {
            trees.addAll(child.getAllTrees());
        }

        return trees;
    }

    public ArrayList<Cube> getCubes()
    {
        ArrayList<Cube> cubes = new ArrayList<>();
        cubes.add(body);

        for (Tree child : children)
        {
            cubes.addAll(child.getCubes());
        }

        return cubes;
    }

    public Cube getBody()
    {
        return body;
    }

    public Tree getParent()
    {
        return parent;
    }
}
