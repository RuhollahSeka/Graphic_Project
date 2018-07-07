package model;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by msi1 on 7/7/2018.
 */
public class ParticleMaster
{
    private ArrayList<Grass> grasses = new ArrayList<>();

    public ParticleMaster(ArrayList<Grass> grasses)
    {
        this.grasses = grasses;
        startUpdatingThread();
    }

    private void startUpdatingThread()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                update();
                try
                {
                    Thread.sleep(20);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void update()
    {
        for (Grass grass : grasses)
        {
            ArrayList<Particle> particles = grass.getParticles();
            Iterator<Particle> iterator = particles.iterator();

            while (iterator.hasNext())
            {
                Particle p = iterator.next();
                boolean stillAlive = p.update();
                if (!stillAlive)
                {
                    iterator.remove();
                }
            }
        }
    }
}
