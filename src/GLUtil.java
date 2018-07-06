import java.util.ArrayList;

/**
 * Created by msi1 on 7/6/2018.
 */
public class GLUtil
{
    public static float[] toPrimitiveFloatArray(Float[] objectArray)
    {
        float[] result = new float[objectArray.length];

        for (int i = 0; i < objectArray.length; i++)
        {
            result[i] = objectArray[i];
        }

        return result;
    }

    public static int findSize(ArrayList<Float[]> list)
    {
        int result = 0;

        for (Float[] array : list)
        {
            result += array.length;
        }

        return result;
    }
}
