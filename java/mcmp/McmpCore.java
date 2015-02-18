package mcmp;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import mcmp.block.MusicPlayer;
import mcmp.gui.MusicGui;
import mcmp.proxy.ClientProxy;
import mcmp.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

/**
 * Created by setuga on 2015/02/01.
 *
 * Guiは tana@ヒキニートP様 作 [1.7.2]MMD-Mod を参考にしています。
 */

@Mod(modid = McmpCore.modid, name = "MinecraftMusicPlayer", version = McmpCore.version)
public class McmpCore
{

    public static final String modid = "mcmp";
    public static final String version = "1.1.0";

    @Mod.Instance(modid)
    public static McmpCore instance;
    @SidedProxy(clientSide = "mcmp.proxy.ClientProxy", serverSide = "mcmp.proxy.Proxy.CommonProxy")
    public static CommonProxy Proxy;

    //Dir
    public static final String dirPath = "./mcmp-resources/";
    //double
    public static double volume = 0.5;
    //int
    public int musicGuiID = 1;
    //Blocks
    public static final Block musicPlayer = new MusicPlayer(Material.iron);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        try
        {
            File dir = new File(dirPath);
            if (!dir.exists())
            {
                dir.mkdirs();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        //Registry
        GameRegistry.registerBlock(musicPlayer, "Player");
    }
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

        Proxy.registerKeyEvent();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, Proxy);
        FMLCommonHandler.instance().bus().register(this);

        GameRegistry.addRecipe(new ItemStack(musicPlayer),
                "III",
                "IJI",
                "III",
                'I', Items.iron_ingot,
                'J', Blocks.jukebox
        );

    }
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void inputKey(InputEvent.KeyInputEvent event)
    {
        if (ClientProxy.addKey.isPressed())
        {
            try {
                int status = MusicGui.basicPlayer.getStatus();
                if (status == BasicPlayer.PLAYING) {
                    if (volume < 1.0) {
                        volume = volume + 0.1;
                        System.out.println(volume);
                        MusicGui.basicPlayer.setGain(volume);
                        //String.valueOf(volume*100)+"%"
                    } else if(volume > 1.0){
                        volume = 1.0;
                        System.out.println(volume);
                        MusicGui.basicPlayer.setGain(volume);
                    }
                }
            } catch (BasicPlayerException e) {
                e.printStackTrace();
            }
        }
        if (ClientProxy.subtractKey.isPressed())
        {
            try {
                int status = MusicGui.basicPlayer.getStatus();
                if (status == BasicPlayer.PLAYING) {
                    if (volume > 0.0) {
                        volume = volume - 0.1;
                        System.out.println(volume);
                        MusicGui.basicPlayer.setGain(volume);
                        //String.valueOf(volume*100)+"%"
                    } else if(volume == 0.0){
                        volume = 0.1;
                        System.out.println(volume);
                        MusicGui.basicPlayer.setGain(volume);
                    } else if(volume < 0.0){
                        volume = 0.1;
                        System.out.println(volume);
                        MusicGui.basicPlayer.setGain(volume);
                    }
                }
            } catch (BasicPlayerException e) {
                e.printStackTrace();
            }
        }
    }
}
