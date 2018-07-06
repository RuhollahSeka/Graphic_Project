import camera.Camera;
import model.GLObject;
import model.Visibility;
import model.shape.Cube;
import model.shape.DrawData;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;
import render.NormalRenderer;
import util.Matrix4f;
import util.Vector3f;
import util.Vector4f;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
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
    private Vector3f diffuseColor; // TODO change this with time
    private Camera camera;

    private HashMap<String, GLObject> objectsMap;
    private ArrayList<GLObject> objects;
    private NormalRenderer normalRenderer;

    public MainGL()
    {
        this.vaos = new ArrayList<>();
        this.vbos = new ArrayList<>();
        this.objects = new ArrayList<>();
        this.objectsMap = new HashMap<>();
        this.diffuseColor = new Vector3f(1.0f, 1.0f, 1.0f);
        this.camera = new Camera(new Vector3f(0.0f, 0.0f, 0.0f), 0, 0, 0);
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
        createVao();
        setVao();
        createRenderers();
    }

    private void createRenderers() throws FileNotFoundException
    {
        normalRenderer = new NormalRenderer("NormalVertexShader.vert",
                "NormalFragmentShader.frag", vaos.get(0));
        normalRenderer.loadProjectionMatrix(projectionMatrix);
    }

    private void setVao()
    {
        ArrayList<DrawData> drawData = collectDrawData();
        ArrayList<Float[]> positions = collectPositions(drawData);
        ArrayList<Float[]> normals = collectNormals(drawData);
        ArrayList<Float[]> textureCoordinates = collectTextureCoordinates(drawData);

        setVaoIndex(positions, 0, 3);
        setVaoIndex(textureCoordinates, 1, 2);
        setVaoIndex(normals, 2, 3);
    }

    private ArrayList<Float[]> collectTextureCoordinates(ArrayList<DrawData> drawData)
    {
        ArrayList<Float[]> textureCoordinates = new ArrayList<>();

        for (DrawData data : drawData)
        {
            textureCoordinates.add(data.getTextureCoordinates());
        }

        return textureCoordinates;
    }

    private ArrayList<Float[]> collectNormals(ArrayList<DrawData> drawData)
    {
        ArrayList<Float[]> normals = new ArrayList<>();

        for (DrawData data : drawData)
        {
            normals.add(data.getNormals());
        }

        return normals;
    }

    private ArrayList<Float[]> collectPositions(ArrayList<DrawData> drawData)
    {
        ArrayList<Float[]> positions = new ArrayList<>();

        for (DrawData data : drawData)
        {
            positions.add(data.getVertices());
        }

        return positions;
    }

    private ArrayList<DrawData> collectDrawData()
    {
        ArrayList<DrawData> drawData = new ArrayList<>();

        for (GLObject object : objects)
        {
            ArrayList<Cube> cubes = object.getCubicParts();
            for (Cube cube : cubes)
            {
                drawData.add(cube.getDrawData());
            }
        }

        return drawData;
    }

    private void setVaoIndex(ArrayList<Float[]> list, int index, int size)
    {
        int vaoId = vaos.get(0);
        GL30.glBindVertexArray(vaoId);

        try(MemoryStack stack = MemoryStack.stackPush())
        {
            FloatBuffer buffer = stack.mallocFloat(GLUtil.findSize(list));
            for (Float[] array : list)
            {
                buffer.put(GLUtil.toPrimitiveFloatArray(array));
            }

            buffer.flip();

            int vboId = GL15.glGenBuffers();
            vbos.add(vboId);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
            GL20.glVertexAttribPointer(index, size, GL11.GL_FLOAT, false, 4 * size, 0);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        }

        GL30.glBindVertexArray(0);
    }

    private void createVao() // TODO this place might cause a problem
    {
        int vaoId = GL30.glGenVertexArrays();
        vaos.add(vaoId);
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
        GLObject frontWall = new GLObject();
        frontWall.addCube(new Vector3f(0f, 7.5f, 11.0f), 20f, 15f, 1f, Visibility.VisibleOutside, "textures\\wallTile.jpg", 5f, 5f);
        objectsMap.put("Front Wall", frontWall);
        objects.add(frontWall);

        //Left Wall
        GLObject leftWall = new GLObject();
        leftWall.addCube(new Vector3f(-10f, 7.5f, 1f), 1f, 15f, 20f, Visibility.VisibleOutside, "textures\\wallTile.jpg", 5f, 5f);
        objectsMap.put("Left Wall", leftWall);
        objects.add(leftWall);

        //Right Wall
        GLObject rightWall = new GLObject();
        rightWall.addCube(new Vector3f(-10f, 3.25f, 1f), 1f, 6.5f, 20f, Visibility.VisibleOutside, "textures\\wallTile.jpg", 5f, 5f);
        rightWall.addCube(new Vector3f(-10f, 13.25f, 1f), 1f, 3.5f, 20f, Visibility.VisibleOutside, "textures\\wallTile.jpg", 5f, 5f);
        rightWall.addCube(new Vector3f(-10f, 7.5f, -5.25f), 1, 5f, 7.5f, Visibility.VisibleOutside, "textures\\wallTile.jpg", 5f, 5f);
        rightWall.addCube(new Vector3f(-10f, 7.5f, -0.5f), 0.5f, 0.5f, 0.5f, Visibility.VisibleOutside, "textures\\wallTile.jpg", 5f, 5f);
        objectsMap.put("Right Wall", rightWall);
        objects.add(rightWall);

        //Back Wall
        GLObject backWall = new GLObject();
        backWall.addCube(new Vector3f(0f, 12.5f, -9f), 20f, 5f, 1f, Visibility.VisibleOutside, "textures\\wallTile.jpg", 5f, 5f);
        backWall.addCube(new Vector3f(-6.25f, 7.5f, -9f), 7.5f, 10f, 1f, Visibility.VisibleOutside, "textures\\wallTile.jpg", 5f, 5f);
        backWall.addCube(new Vector3f(6.25f, 7.5f, -9f), 7.5f, 10f, 1f, Visibility.VisibleOutside, "textures\\wallTile.jpg", 5f, 5f);
        objectsMap.put("Back Wall", backWall);
        objects.add(backWall);

        //Roof
        GLObject roof = new GLObject();
        roof.addCube(new Vector3f(0f, 15f, 1f), 20f, 1f, 20f, Visibility.VisibleOutside, "textures\\wallTile.jpg", 5f, 5f);
        objectsMap.put("Roof", roof);
        objects.add(roof);

        //Table
        GLObject table = new GLObject();
        table.addCube(new Vector3f(-1f, 3f, 0f), 6f, 0.5f, 4f, Visibility.VisibleOutside, "textures\\tableTile.jpg", 5f, 5f);
        table.addCube(new Vector3f(-3.3f, 1.5f, 1.3f), 0.5f, 3f, 0.5f, Visibility.VisibleOutside, "textures\\tableTile.jpg", 5f, 5f);
        table.addCube(new Vector3f(1.3f, 1.5f, 1.3f), 0.5f, 3f, 0.5f, Visibility.VisibleOutside, "textures\\tableTile.jpg", 5f, 5f);
        table.addCube(new Vector3f(-3.3f, 1.5f, -1.3f), 0.5f, 3f, 0.5f, Visibility.VisibleOutside, "textures\\tableTile.jpg", 5f, 5f);
        table.addCube(new Vector3f(1.3f, 1.5f, -1.3f), 0.5f, 3f, 0.5f, Visibility.VisibleOutside, "textures\\tableTile.jpg", 5f, 5f);
        objectsMap.put("Table", table);
        objects.add(table);

        //Window
        GLObject window =new GLObject();
        window.addCube(new Vector3f(-10f, 9f, 1f), 1f, 5f, 5f, Visibility.VisibleOutside, "textures\\glassTile.jpg", 5f, 5f);
        objectsMap.put("Window", window);
        objects.add(window);

        //Door
        GLObject door = new GLObject();
        door.addCube(new Vector3f(0f, 5f, -9f), 5f, 10f, 1f, Visibility.VisibleOutside, "textures\\tableTile.jpg", 5f, 5f);
        door.addCube(new Vector3f(2f, 5f, -8.25f), 0.5f, 0.5f, 0.5f, Visibility.VisibleOutside, "textures\\knobTile.jpg", "Major Knob",5f, 5f);
        door.addCube(new Vector3f(1.5f, 5f, -8.25f), 0.5f, 0.25f, 0.25f, Visibility.VisibleOutside, "textures\\knobTile.jpg", "Minor Knob",5f, 5f);
        objectsMap.put("Door", door);
        objects.add(door);


        //Clock
        GLObject clock = new GLObject();
        clock.addCube(new Vector3f(9.4f, 11f, 1f), 0.2f, 2f, 2f, Visibility.VisibleOutside, "textures\\clock.jpg");
        clock.addCube(new Vector3f(9.3f, 11.45f, 1f), 0.2f, 0.9f, 0.1f, Visibility.VisibleOutside, "textures\\knobTile.jpg", "Minute bar", 2f, 2f);
        clock.addCube(new Vector3f(9.3f, 11f, 1.35f), 0.2f, 0.1f, 0.7f, Visibility.VisibleOutside, "textures\\knobTile.jpg", "Hour bar", 2f, 2f);
        objectsMap.put("Clock", clock);
        objects.add(clock);

        //Skybox
        GLObject skybox = new GLObject();
        skybox.addCube(new Vector3f(1000f, 1000f, 1000f), 0f, 500f, 0f, Visibility.VisibleInside, "textures\\sky.jpg");
        objectsMap.put("Skybox", skybox);
        objects.add(skybox);

    }

    private void loop() {
        // Set the clear color
        glClearColor(0.4f, 0.7f, 1.0f, 1.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(window) ) {
            glEnable(GL_DEPTH_TEST);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            for (GLObject object : objects)
            {
                normalRenderer.render(object, camera, diffuseColor);
            }

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

        normalRenderer.cleanUp();

        // TODO clean cubes and objects here?
    }
}
