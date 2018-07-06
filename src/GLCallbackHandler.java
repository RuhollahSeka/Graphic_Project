import camera.Camera;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.system.NativeType;

/**
 * Created by msi1 on 7/6/2018.
 */
public class GLCallbackHandler // TODO events
{
    private Camera camera;
    private GLFWKeyCallback keyCallback;
    private GLFWMouseButtonCallback mouseButtonCallback;
    private GLFWCursorPosCallback cursorPosCallback;

    public GLCallbackHandler(Camera camera)
    {
        this.camera = camera;
        this.keyCallback = new MyKeyCallback();
        this.mouseButtonCallback = new MyMouseButtonCallback();
        this.cursorPosCallback = new MyCursorPosCallback();
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

        @Override
        public void invoke(@NativeType("GLFWwindow *") long window, double xpos, double ypos)
        {

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

        }
    }
}
