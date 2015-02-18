package mcmp.gui;

import java.io.File;

/**
 * Created by setuga on 2015/02/11.
 *
 * 参考: tana@ヒキニートP様 作 [1.7.2]MMD-Mod
 */
public class MemoryChecker
{
    public static boolean checkMemorySizeWithGC(String filename)
    {
        System.gc();

        if (filename == null)
        {
            return true;
        }

        long freeSize = Runtime.getRuntime().freeMemory();

        long audioSize = (long)(new File(filename).length() * 1.0);

        return freeSize > audioSize;
    }
}
