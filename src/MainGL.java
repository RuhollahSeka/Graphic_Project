import camera.Camera;
import model.GLObject;
import model.Visibility;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;
import util.Matrix4f;
import util.Vector3f;
import util.Vector4f;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Created by msi1 on 7/5/2018.
 */
public class MainGL
{
    public static void main(String[] args) throws IOException
    {
        new MainGL().start();
    }

    private long window;
    private ArrayList<Integer> vaos;
    private ArrayList<Integer> vbos;
    private int windowWidth;
    private int windowHeight;
    private GLCallbackHandler callbackHandler;

    private static final float FOV = 70.0f;
    private static final float NEAR_PLANE = 0.01f;
    private static final float FAR_PLANE = 10000.0f;
    private Matrix4f projectionMatrix;
    private Camera camera;

    private HashMap<String, GLObject> objectsMap;

    public MainGL()
    {
        this.vaos = new ArrayList<>();
        this.vbos = new ArrayList<>();
        this.objectsMap = new HashMap<>();
        this.camera = new Camera(new Vector3f(0, 0, 0), 0, 0, 0);
        this.callbackHandler = new GLCallbackHandler(camera);
        this.windowWidth = 1200;
        this.windowHeight = 800;
    }

    private void start() throws FileNotFoundException
    {
        init();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() throws FileNotFoundException
    {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(windowWidth, windowHeight, "Hello World!", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);

        GL.createCapabilities();

        createProjectionMatrix();
        addObjects();
        addCallbacks();

    }

    private void createProjectionMatrix()
    {
        float aspect = (float) windowWidth / (float) windowHeight;
        float yScale = 1.0f / (float) Math.tan(Math.toRadians(FOV / 2.0f));
        float xScale = yScale / aspect;
        float zp = FAR_PLANE + NEAR_PLANE;
        float zm = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f(new Vector4f(xScale, 0.0f, 0.0f, 0.0f),
                new Vector4f(0.0f, yScale, 0.0f, 0.0f), new Vector4f(0.0f, 0.0f, -zp / zm, -1.0f),
                new Vector4f(0.0f, 0.0f, -(2.0f*FAR_PLANE*NEAR_PLANE)/zm, 0));
    }

    private void addCallbacks()
    {
        glfwSetKeyCallback(window, callbackHandler.getKeyCallback());
        glfwSetMouseButtonCallback(window, callbackHandler.getMouseButtonCallback());
        glfwSetCursorPosCallback(window, callbackHandler.getCursorPosCallback());
    }

    private void addObjects()
    {
        //Front Wall
        GLObject obj = new GLObject();
        obj.addCube(new Vector3f(0f, 7.5f, 11f), 20f, 15f, 1f, Visibility.VisibleOutside, "..\\textures\\wallTile.jpg");
        objectsMap.put("Front Wall", obj);

        //Left Wall
        obj = new GLObject();
        obj.addCube(new Vector3f(-10f, 7.5f, 1f), 1f, 15f, 20f, Visibility.VisibleOutside, "..\\textures\\wallTile.jpg");
        objectsMap.put("Left Wall", obj);

        //Right Wall
      /*  obj = new GLObject();
        obj.addCube(new Vector3f(10f, 7.5f, 1f), 1f, 15f, 20f, Visibility.VisibleOutside, "..\\textures\\wallTile.jpg");
        objectsMap.put("Right Wall", obj);*/

    }

    private void loop() {
        // Set the clear color
        glClearColor(0.4f, 0.7f, 1.0f, 1.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(window) ) {
            glEnable(GL_DEPTH_TEST);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            // TODO the rendering must happen here, we MUST do it clean, I'm still not sure exactly how

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
            glfwSwapBuffers(window); // swap the color buffers
        }

        cleanUp();
    }

    private void cleanUp()
    {
        for (int vaoId : vaos)
        {
            GL30.glDeleteVertexArrays(vaoId);
        }

        for (int vboId : vbos)
        {
            GL15.glDeleteBuffers(vboId);
        }

        // TODO clean cubes and objects here?
    }
}
