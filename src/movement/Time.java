package movement;

import util.Vector3f;

import java.awt.*;

/**
 * Created by msi1 on 7/7/2018.
 */
public class Time
{
    private float minute;
    private float hour;

    public Time()
    {
        this.minute = 0.0f;
        this.hour = 3.0f;
    }

    public Vector3f getMinuteBarGoal()
    {
        return new Vector3f(-minute * 6.0f, 0.0f, 0.0f);
    }

    public Vector3f getHourBarGoal()
    {
        float xRot = 30 * (((hour % 12.0f) - 3.0f + 12) % 12);

        Vector3f goal = new Vector3f(xRot, 0.0f, 0.0f);
        goal = goal.add(new Vector3f(30 * ((minute % 60.0f) / 60.0f), 0.0f, 0.0f));
        goal.x = -goal.x;

        return goal;
    }

    public void nextMinute()
    {
        minute = minute + 1.0f;
        if (minute % 60.0f == 0.0f)
        {
            hour = hour + 1.0f;
        }

        if (minute % 60.0f == 0.0f && hour % 12.0f == 0.0f)
        Toolkit.getDefaultToolkit().beep();
    }

    public void nextMinute(int num)
    {
        for (int i = 0; i < num; i++)
        {
            nextMinute();
        }
    }
}
