package camera;

import util.Vector3f;

/**
 * Created by msi1 on 3/14/2018.
 */
public class Camera
{
    private Vector3f position;
    private float rotX, rotY, rotZ;
    private float speed = 0.05f;

    public Camera(Vector3f position, float rotX, float rotY, float rotZ)
    {
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
    }

    public Vector3f getPosition()
    {
        return position;
    }

    public float getRotX()
    {
        return rotX;
    }

    public float getRotY()
    {
        return rotY;
    }

    public float getRotZ()
    {
        return rotZ;
    }

    public void setPosition(Vector3f position)
    {
        this.position = position;
    }

    public void setRotX(float rotX)
    {
        this.rotX = rotX;
    }

    public void setRotY(float rotY)
    {
        this.rotY = rotY;
    }

    public void setRotZ(float rotZ)
    {
        this.rotZ = rotZ;
    }

    public float getSpeed()
    {
        return speed;
    }

    public void setSpeed(float speed)
    {
        this.speed = speed;
    }
}
