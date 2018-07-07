package model;

import model.shape.Cube;
import org.lwjgl.glfw.GLFW;
import util.Vector3f;

/**
 * Created by msi1 on 7/7/2018.
 */
public class Particle
{
    private Cube cube;
    private Vector3f velocity;
    private float gravityEffect, lifeLength, rotation, scale;
    private static final float GRAVITY = 0.01f;

    private float elapsedTime;
    private boolean firstUpdate = true;
    private float prevTime;

    private boolean alive = true;

    public Particle(Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, float rotation, float scale)
    {
        this.cube = new Cube(position, 0.1f, 0.1f, 0.1f, Visibility.VisibleOutside, "textures\\glassTile.png");
        this.velocity = velocity;
        this.gravityEffect = gravityEffect;
        this.lifeLength = lifeLength;
        this.rotation = rotation;
        this.scale = scale;
        this.elapsedTime = 0.0f;
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep((long) (lifeLength * 1000));
                    alive = false;
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    protected boolean update()
    {
        if (firstUpdate)
        {
            prevTime = (float) GLFW.glfwGetTime();
            firstUpdate = false;
            return true;
        }

        float currentTime = (float) GLFW.glfwGetTime();
        velocity.y += GRAVITY * gravityEffect * (currentTime - prevTime);
        Vector3f change = new Vector3f(velocity.x, velocity.y, velocity.z);
        change.scale(currentTime - prevTime);
        cube.translate(change);
        elapsedTime += currentTime - prevTime;

        prevTime = currentTime;
        return alive;
    }

    public float getRotation()
    {
        return rotation;
    }

    public float getScale()
    {
        return scale;
    }

    public Cube getCube()
    {
        return cube;
    }
}
