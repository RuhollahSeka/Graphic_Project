package util;

import camera.Camera;

/**
 * Created by msi1 on 3/14/2018.
 */
public class Calculator
{
    public static Matrix4f createTransformationMatrix(Vector3f translation, float rotX, float rotY,
                                                      float rotZ, float scale)
    {
        Matrix4f scaleMatrix = Matrix4f.scale(scale, scale, scale);
        Matrix4f xRotMatrix = Matrix4f.rotate(rotX, 1.0f, 0.0f, 0.0f);
        Matrix4f yRotMatrix = Matrix4f.rotate(rotY, 0.0f, 1.0f, 0.0f);
        Matrix4f zRotMatrix = Matrix4f.rotate(rotZ, 0.0f, 0.0f, 1.0f);
        Matrix4f translationMatrix = Matrix4f.translate(translation.x, translation.y, translation.z);

        Matrix4f transformationMatrix = yRotMatrix.multiply(xRotMatrix.multiply(scaleMatrix));
        transformationMatrix = translationMatrix.multiply(zRotMatrix.multiply(transformationMatrix));

        return transformationMatrix;
    }

    public static Matrix4f createViewMatrix(Camera camera)
    {
        Vector3f translation = camera.getPosition();
        Matrix4f translationMatrix = Matrix4f.translate(-translation.x, -translation.y, -translation.z);
        Matrix4f xRotMatrix = Matrix4f.rotate(camera.getRotX(), 1.0f, 0.0f, 0.0f);
        Matrix4f yRotMatrix = Matrix4f.rotate(camera.getRotY(), 0.0f, 1.0f, 0.0f);
        Matrix4f zRotMatrix = Matrix4f.rotate(camera.getRotZ(), 0.0f, 0.0f, 1.0f);

        return zRotMatrix.multiply(yRotMatrix.multiply(xRotMatrix.multiply(translationMatrix)));
    }
}
