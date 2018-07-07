import camera.Camera;
import movement.MovementHandler;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.system.NativeType;
import util.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by msi1 on 7/6/2018.
 */
public class GLCallbackHandler // TODO events
{
    private Camera camera;
    private GLFWKeyCallback keyCallback;
    private GLFWMouseButtonCallback mouseButtonCallback;
    private GLFWCursorPosCallback cursorPosCallback;
    private MovementHandler movementHandler;
    private long window;

    public GLCallbackHandler(Camera camera, MovementHandler movementHandler)
    {
        this.camera = camera;
        this.keyCallback = new MyKeyCallback();
        this.mouseButtonCallback = new MyMouseButtonCallback();
        this.cursorPosCallback = new MyCursorPosCallback();
        this.movementHandler = movementHandler;
    }

    public void setWindow(long window)
    {
        this.window = window;
    }

    public GLFWKeyCallback getKeyCallback()
    {
        return keyCallback;
    }

    public GLFWMouseButtonCallback getMouseButtonCallback()
    {
        return mouseButtonCallback;
    }

    public GLFWCursorPosCallback getCursorPosCallback()
    {
        return cursorPosCallback;
    }

    private class MyCursorPosCallback extends GLFWCursorPosCallback
    {
        private boolean initialized = false;
        private float prevX, prevY;

        @Override
        public void invoke(@NativeType("GLFWwindow *") long window, double xpos, double ypos)
        {
            if (!initialized)
            {
                prevX = (float) xpos;
                prevY = (float) ypos;
                initialized = true;
                return;
            }

            float yaw, pitch;

            float xPos = (float) xpos;
            float yPos = (float) ypos;

            float xOffset = (xPos - prevX) * 0.5f;
            float yOffset = -(prevY - yPos) * 0.5f;

            camera.updatePitchAndYaw(yOffset, xOffset);
            yaw = camera.getYaw();
            pitch = camera.getPitch();

            prevX = xPos;
            prevY = yPos;

            Vector3f newCameraFront = new Vector3f();

            newCameraFront.x = (float) (Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
            newCameraFront.y = (float) Math.sin(Math.toRadians(pitch));
            newCameraFront.z = (float) (Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
            newCameraFront = newCameraFront.normalize();
            camera.setFront(newCameraFront);
        }
    }

    private class MyMouseButtonCallback extends GLFWMouseButtonCallback
    {

        @Override
        public void invoke(@NativeType("GLFWwindow *") long window, int button, int action, int mods)
        {

        }
    }

    private class MyKeyCallback extends GLFWKeyCallback
    {

        @Override
        public void invoke(@NativeType("GLFWwindow *") long window, int key, int scancode, int action, int mods)
        {
            if (action == GLFW_PRESS)
            {
                handlePressEvent(key);
            } else if (action == GLFW_RELEASE)
            {
                handleReleaseEvent(key);
            }
        }

        private void handleReleaseEvent(int key)
        {
            if (key == GLFW_KEY_W)
            {
                camera.addSpeed(0, 0, -0.002f);
            } else if (key == GLFW_KEY_S)
            {
                camera.addSpeed(0, 0, 0.002f);
            } else if (key == GLFW_KEY_A)
            {
                camera.addSpeed(0.002f, 0, 0);
            } else if (key == GLFW_KEY_D)
            {
                camera.addSpeed(-0.002f, 0, 0);
            } else if (key == GLFW_KEY_LEFT_SHIFT)
            {
                camera.setRunning(false);
            }
        }

        private void handlePressEvent(int key)
        {
            if (key == GLFW_KEY_W)
            {
                camera.addSpeed(0, 0, 0.002f);
            } else if (key == GLFW_KEY_S)
            {
                camera.addSpeed(0, 0, -0.002f);
            } else if (key == GLFW_KEY_A)
            {
                camera.addSpeed(-0.002f, 0, 0);
            } else if (key == GLFW_KEY_D)
            {
                camera.addSpeed(0.002f, 0, 0);
            } else if (key == GLFW_KEY_E)
            {
                movementHandler.open();
            } else if (key == GLFW_KEY_LEFT_SHIFT)
            {
                camera.setRunning(true);
            } else if (key == GLFW_KEY_SPACE)
            {
                camera.addSpeed(0.0f, -0.012f, 0.0f);
                camera.addAcceleration(0.0f, 0.0005f, 0.0f);
                camera.setJumping(true);
            } else if (key == GLFW_KEY_ESCAPE)
            {
                glfwSetWindowShouldClose(window, true);
            } else if (key == GLFW_KEY_F)
            {
                movementHandler.increaseMinutes(1);
            } else if (key == GLFW_KEY_H)
            {
                movementHandler.increaseHours(1);
            }
        }
    }
}
