package model;

import util.Matrix4f;
import util.Vector3f;

/**
 * Created by msi1 on 7/6/2018.
 */
public class TransformationData
{
    private Vector3f translation;
    private Vector3f rotation;
    private float scale;
    private Vector3f distance;
    private RotationAxisType rotationAxisType;

    public TransformationData(Vector3f pointOnAxis, RotationAxisType rotationAxisType)
    {
        this.translation = new Vector3f(0.0f, 0.0f, 0.0f);
        this.rotation = new Vector3f(0.0f, 0.0f, 0.0f);
        this.scale = 1.0f;
        this.rotationAxisType = rotationAxisType;
        calculateDistance(pointOnAxis);
    }

    public TransformationData()
    {
        this.translation = new Vector3f(0.0f, 0.0f, 0.0f);
        this.rotation = new Vector3f(0.0f, 0.0f, 0.0f);
        this.scale = 1.0f;
        this.distance = new Vector3f(0.0f, 0.0f, 0.0f);
        this.rotationAxisType = RotationAxisType.NULL;
    }

    private void calculateDistance(Vector3f pointOnAxis)
    {
        switch (rotationAxisType)
        {
            case ParallelX:
                distance = new Vector3f(0.0f, -pointOnAxis.y, -pointOnAxis.z);
                break;
            case ParallelY:
                distance = new Vector3f(-pointOnAxis.x, 0.0f, -pointOnAxis.z);
                break;
            case ParallelZ:
                distance = new Vector3f(-pointOnAxis.x, -pointOnAxis.y, 0.0f);
                break;
        }
    }

    public Matrix4f getTransformationMatrix()
    {
        Matrix4f scaleMatrix = Matrix4f.scale(scale, scale, scale);
        Matrix4f translationMatrix = Matrix4f.translate(translation.x, translation.y, translation.z);
        Matrix4f rotationMatrix = getRotationMatrix();

        return translationMatrix.multiply(rotationMatrix.multiply(scaleMatrix));
    }

    private Matrix4f getRotationMatrix()
    {
        Matrix4f xRotMatrix = Matrix4f.rotate(rotation.x, 1.0f, 0.0f, 0.0f);
        Matrix4f yRotMatrix = Matrix4f.rotate(rotation.y, 0.0f, 1.0f, 0.0f);
        Matrix4f zRotMatrix = Matrix4f.rotate(rotation.z, 0.0f, 0.0f, 1.0f);

        Matrix4f translateToAxis = Matrix4f.translate(distance.x, distance.y, distance.z);
        Matrix4f translateFromAxis = Matrix4f.translate(-distance.x, -distance.y, -distance.z);

        Matrix4f rotationMatrix = zRotMatrix.multiply(yRotMatrix.multiply(xRotMatrix));
        rotationMatrix = translateFromAxis.multiply(rotationMatrix.multiply(translateToAxis));

        return rotationMatrix;
    }

    public void translate(float dx, float dy, float dz)
    {
        translation.x += dx;
        translation.y += dy;
        translation.z += dz;
    }

    public void rotate(float dx, float dy, float dz)
    {
        rotation.x += dx;
        rotation.y += dy;
        rotation.z += dz;
    }

    public void setScale(float scale)
    {
        this.scale = scale;
    }
}
