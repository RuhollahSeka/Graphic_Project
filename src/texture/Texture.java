package texture;

import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture
{

    private final int id;
    private int width;
    private int height;
    private String fileName;

    //How long a single tile covers
    private float x;
    private float y;

    public Texture() {
        id = glGenTextures();
    }

    public Texture(float x, float y){
        this();
        this.x = x;
        this.y = y;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    /**
     * Sets a parameter of the texture.
     *
     * @param name  Name of the parameter
     * @param value Value to set
     */
    public void setParameter(int name, int value) {
        glTexParameteri(GL_TEXTURE_2D, name, value);
    }

    /**
     * Uploads image data with specified width and height.
     *
     * @param width  Width of the image
     * @param height Height of the image
     * @param data   Pixel data of the image
     */
    public void uploadData(int width, int height, ByteBuffer data) {
        uploadData(GL_RGBA8, width, height, GL_RGBA, data);
    }

    /**
     * Uploads image data with specified internal format, width, height and
     * image format.
     *
     * @param internalFormat Internal format of the image data
     * @param width          Width of the image
     * @param height         Height of the image
     * @param format         Format of the image data
     * @param data           Pixel data of the image
     */
    public void uploadData(int internalFormat, int width, int height, int format, ByteBuffer data) {
        glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, GL_UNSIGNED_BYTE, data);
    }

    public void delete() {
        glDeleteTextures(id);
    }

    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        if (width > 0) {
            this.width = width;
        }
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        if (height > 0) {
            this.height = height;
        }
    }

    /**
     * Creates a texture with specified width, height and data.
     *
     * @param width  Width of the texture
     * @param height Height of the texture
     * @param data   Picture Data in RGBA format
     *
     * @param path
     * @return Texture from the specified data
     */
    public static Texture createTexture(int width, int height, ByteBuffer data, String path) {
        Texture texture = new Texture();
        texture.fileName = path;
        texture.setWidth(width);
        texture.setHeight(height);

        texture.bind();

//        texture.setParameter(GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
//        texture.setParameter(GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
//        texture.setParameter(GL_TEXTURE_MIN_FILTER, GL_NEAREST);
//        texture.setParameter(GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        texture.setParameter(GL_TEXTURE_WRAP_S, GL_REPEAT);
        texture.setParameter(GL_TEXTURE_WRAP_T, GL_REPEAT);
        texture.setParameter(GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        texture.setParameter(GL_TEXTURE_MAG_FILTER, GL_LINEAR);

//        texture.uploadData(GL_RGBA8, width, height, GL_RGBA, data);
        texture.uploadData(GL_RGBA8, width, height, GL_RGBA, data);
        GL30.glGenerateMipmap(GL_TEXTURE_2D);

        return texture;
    }

    /**
     * Load texture from file.
     *
     * @param path File path of the texture
     *
     * @return Texture from specified file
     */
    public static Texture loadTexture(String path) {
        ByteBuffer image;
        int width, height;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            /* Prepare image buffers */
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            /* Load image */
            stbi_set_flip_vertically_on_load(true);
            image = stbi_load(path, w, h, comp, 4);
            if (image == null) {
                throw new RuntimeException("Failed to load a texture file!"
                        + System.lineSeparator() + stbi_failure_reason());
            }

            /* Get width and height of image */
            width = w.get();
            height = h.get();
        }

        return createTexture(width, height, image, path);
    }

    public int getId()
    {
        return id;
    }

    public String getFileName()
    {
        return fileName;
    }
}