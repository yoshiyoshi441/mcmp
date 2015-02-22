package mcmp.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import mcmp.McmpCore;
import mcmp.gui.MusicContainer;
import mcmp.gui.MusicGui;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

/**
 * Created by setuga on 2015/02/01.
 */
public class ClientProxy extends CommonProxy{

    //Key
    public static final KeyBinding addKey = new KeyBinding("key.inputKey.add", Keyboard.KEY_ADD, "mcmp.inputEvent.volume");
    public static final KeyBinding subtractKey = new KeyBinding("key.inputKey.subtract", Keyboard.KEY_SUBTRACT, "mcmp.inputEvent.volume");

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == McmpCore.instance.musicGuiID)
        {
            return new MusicContainer(x, y, z);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == McmpCore.instance.musicGuiID)
        {
            return new MusicGui(x, y, z);
        }
        return null;
    }

    @Override
    public void registerKeyEvent() {
        ClientRegistry.registerKeyBinding(addKey);
        ClientRegistry.registerKeyBinding(subtractKey);
    }

    @Override
    public void registerTileEntity() {

    }

    @Override
     public void registerEntity() {

    }
}
