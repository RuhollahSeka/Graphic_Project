package camera;

import util.Matrix4f;
import util.Vector3f;
import util.Vector4f;

/**
 * Created by msi1 on 3/14/2018.
 */
public class Camera
{
    private Vector3f position;
    private Vector3f front;
    private Vector3f up;
    private float pitch;
    private float yaw;
    private float xSpeed, ySpeed, zSpeed;
    private float xAcceleration, yAcceleration, zAcceleration;
    private boolean running;
    private boolean jumping;
    private float mainY;

    public Camera(Vector3f position)
    {
        this.position = position;
        this.front = new Vector3f(0.0f, 0.0f, -1.0f);
        this.up = new Vector3f(0.0f, 1.0f, 0.0f);

        this.pitch = 0.0f;
        this.yaw = -90.f;

        this.xSpeed = 0.0f;
        this.ySpeed = 0.0f;
        this.zSpeed = 0.0f;

        this.xAcceleration = 0.0f;
        this.yAcceleration = 0.0f;
        this.zAcceleration = 0.0f;

        this.running = false;
        this.jumping = false;

        this.mainY = position.y;

        startUpdateThread();
    }

    private void startUpdateThread()
    {
        new Thread(() ->
        {
            while (true)
            {
                update();
                try
                {
                    Thread.sleep(10);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void update()
    {
        float effect = running ? 2.0f : 1.0f;

        Vector3f frontSpeedVector = front.scale(zSpeed * effect);
        frontSpeedVector.y = 0;
        position = position.add(frontSpeedVector);

        Vector3f rightSpeedVector = front.cross(up);
        rightSpeedVector = rightSpeedVector.scale(xSpeed * effect);
        rightSpeedVector.y = 0;
        position = position.add(rightSpeedVector);

        if (jumping)
        {
            position.y += ySpeed;
            ySpeed += yAcceleration;

            if (position.y >= mainY)
            {
                jumping = false;
                position.y = mainY;
                ySpeed = 0.0f;
                yAcceleration = 0.0f;
            }
        }

        xSpeed += xAcceleration;
        zSpeed += zAcceleration;
    }

    public void addSpeed(float dx, float dy, float dz)
    {
        xSpeed += dx;
        ySpeed += dy;
        zSpeed += dz;
    }

    public void addAcceleration(float dx, float dy, float dz)
    {
        xAcceleration += dx;
        yAcceleration += dy;
        zAcceleration += dz;
    }

    public void updatePitchAndYaw(float dPitch, float dYaw)
    {
        pitch += dPitch;
        yaw += dYaw;

        if (pitch > 50.0f)
        {
            pitch = 50.0f;
        } else if (pitch < -50.0f)
        {
            pitch = -50.0f;
        }
    }

    public Matrix4f getViewMatrix()
    {
        Vector3f realFront = front.negate().normalize();
        Vector3f right = front.cross(up);
        Vector3f realUp = front.cross(right);
//        Vector3f realFront = front.add(position);
//        Vector3f right = realFront.cross(up);
        Matrix4f firstMatrix = new Matrix4f(new Vector4f(right.x, realUp.x, realFront.x, 0.0f),
                new Vector4f(right.y, realUp.y, realFront.y, 0.0f),
                new Vector4f(right.z, realUp.z, realFront.z, 0.0f),
                new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));
        Matrix4f secondMatrix = new Matrix4f(new Vector4f(1.0f, 0.0f, 0.0f, 0.0f),
                new Vector4f(0.0f, 1.0f, 0.0f, 0.0f),
                new Vector4f(0.0f, 0.0f, 1.0f, 0.0f),
                new Vector4f(-position.x, -position.y, -position.z, 1.0f));

        return firstMatrix.multiply(secondMatrix);
//        Matrix4f translationMatrix = Matrix4f.translate(-position.x, -position.y, -position.z);
//        Matrix4f xRotMatrix = Matrix4f.rotate(rotX, 1.0f, 0.0f, 0.0f);
//        Matrix4f yRotMatrix = Matrix4f.rotate(rotY, 0.0f, 1.0f, 0.0f);
//        Matrix4f zRotMatrix = Matrix4f.rotate(rotZ, 0.0f, 0.0f, 1.0f);
//
//        return zRotMatrix.multiply(yRotMatrix.multiply(xRotMatrix.multiply(translationMatrix)));
//        return translationMatrix.multiply(zRotMatrix.multiply(yRotMatrix.multiply(xRotMatrix)));
    }

    public Vector3f getPosition()
    {
        return position;
    }

    public void setPosition(Vector3f position)
    {
        this.position = position;
    }

    public void setxSpeed(float xSpeed)
    {
        this.xSpeed = xSpeed;
    }

    public void setySpeed(float ySpeed)
    {
        this.ySpeed = ySpeed;
    }

    public void setzSpeed(float zSpeed)
    {
        this.zSpeed = zSpeed;
    }

    public void setxAcceleration(float xAcceleration)
    {
        this.xAcceleration = xAcceleration;
    }

    public void setyAcceleration(float yAcceleration)
    {
        this.yAcceleration = yAcceleration;
    }

    public void setzAcceleration(float zAcceleration)
    {
        this.zAcceleration = zAcceleration;
    }

    public void setRunning(boolean running)
    {
        this.running = running;
    }

    public float getYaw()
    {
        return yaw;
    }

    public void setYaw(float yaw)
    {
        this.yaw = yaw;
    }

    public float getPitch()
    {
        return pitch;
    }

    public void setPitch(float pitch)
    {
        this.pitch = pitch;
    }

    public void setFront(Vector3f front)
    {
        this.front = front;
    }

    public void setJumping(boolean jumping)
    {
        this.jumping = jumping;
    }
}
