package movement;

import camera.Camera;
import model.GLObject;
import model.shape.Cube;
import util.Matrix4f;
import util.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by msi1 on 7/7/2018.
 */
public class MovementHandler
{
    private Camera camera;
    private HashMap<String, GLObject> objectMap;
    private boolean doorSelected;
    private boolean windowSelected;

    private Time time;

    public MovementHandler(Camera camera, HashMap<String, GLObject> objectMap)
    {
        this.camera = camera;
        this.objectMap = objectMap;
        this.doorSelected = false;
        this.time = new Time();
    }

    public void startThread()
    {
        new Thread(() ->
        {
            while (true)
            {
                camera.update(this);
                updateClockBars();
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

    private void updateClockBars()
    {
        GLObject clock = objectMap.get("Clock");
        Cube minuteBar = clock.getPart("Minute bar");
        Cube hourBar = clock.getPart("Hour bar");

        minuteBar.setGoal(time.getMinuteBarGoal());
        hourBar.setGoal(time.getHourBarGoal());
    }

    public void handleCollisions(Vector3f speedVector)
    {
        GLObject table = objectMap.get("Table");

        for (GLObject object : objectMap.values())
        {
            float collisionDistance = 0.05f;

            if (object == table)
            {
                collisionDistance = 0.15f;
            }

            ArrayList<Cube> cubes = object.getCubicParts();

            if (isCollision(cubes, object.getTransformationMatrix(), collisionDistance))
            {
                camera.setPosition(camera.getPosition().subtract(speedVector));
            }
        }
    }

    public void handleCollisions(float ySpeed)
    {
        GLObject table = objectMap.get("Table");

        for (GLObject object : objectMap.values())
        {
            float collisionDistance = 0.05f;

            if (object == table)
            {
                collisionDistance = 0.5f;
            }

            ArrayList<Cube> cubes = object.getCubicParts();

            if (isCollision(cubes, object.getTransformationMatrix(), collisionDistance))
            {
                Vector3f position = camera.getPosition();
                position.y -= ySpeed;
            }
        }
    }

    private boolean isCollision(ArrayList<Cube> cubes, Matrix4f transformationMatrix, float collisionDistance)
    {
        for (Cube cube : cubes)
        {
            float distance = cube.getDistance(camera.getPosition(), transformationMatrix);

            if (distance < collisionDistance && distance > -collisionDistance)
            {
                return true;
            }
        }

        return false;
    }

    public void afterEffects()
    {
        GLObject door = objectMap.get("Door");
        Cube mainPart = door.getCubicParts().get(0);
        float distance = mainPart.getDistance(camera.getPosition(), door.getTransformationMatrix());

        doorSelected = distance < 0.3 && distance > -0.3;

        GLObject window = objectMap.get("Window");
        mainPart = window.getCubicParts().get(0);
        distance = mainPart.getDistance(camera.getPosition(), window.getTransformationMatrix());

        windowSelected = distance < 0.3 && distance > -0.3;
    }

    public void open()
    {
        if (doorSelected)
        {
            GLObject door = objectMap.get("Door");
            Vector3f currentGoal = door.getGoal();
            Vector3f goal = currentGoal.y == 0.0f ? new Vector3f(0.0f, 90.0f, 0.0f) : new Vector3f(0.0f, 0.0f, 0.0f);
            door.setRotationGoal(goal);
        } else if (windowSelected)
        {
            GLObject window = objectMap.get("Window");
            Vector3f currentGoal = window.getGoal();
            Vector3f goal = currentGoal.y == 0.0f ? new Vector3f(0.0f, 90.0f, 0.0f) : new Vector3f(0.0f, 0.0f, 0.0f);
            window.setRotationGoal(goal);
        }
    }

    public void increaseMinutes(int amount)
    {
        time.nextMinute(amount);
    }

    public void increaseHours(int amount)
    {
        time.nextMinute(60 * amount);
    }

    public boolean isDoorSelected()
    {
        return doorSelected;
    }

    public boolean isWindowSelected()
    {
        return windowSelected;
    }
}
