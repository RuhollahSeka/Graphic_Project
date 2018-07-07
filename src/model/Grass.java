package model;

import model.shape.Cube;
import util.Vector3f;

import java.util.ArrayList;

/**
 * Created by msi1 on 7/7/2018.
 */
public class Grass
{
    private ArrayList<Particle> particles;
    private Vector3f position;
    private Vector3f velocity;

    public Grass(Vector3f position, Vector3f velocity)
    {
        this.particles = new ArrayList<>();
        this.position = position;
        this.velocity = velocity;
    }

//    public void startParticleCreatingThread()
//    {
//        new Thread(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                try
//                {
//                    particles.add(new Particle(position, velocity, 1, 4, 0, 1));
//                    Thread.sleep(10);
//                } catch (InterruptedException e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }

    public ArrayList<Cube> getCubes()
    {
        ArrayList<Cube> cubes = new ArrayList<>();

        for (Particle particle : particles)
        {
            cubes.add(particle.getCube());
        }

        return cubes;
    }

    public void addParticle()
    {
        if (particles.size() >= 50)
        {
            return;
        }

        particles.add(new Particle(position, velocity, 1, 1, 0, 1));
    }

    public ArrayList<Particle> getParticles()
    {
        return particles;
    }
}
