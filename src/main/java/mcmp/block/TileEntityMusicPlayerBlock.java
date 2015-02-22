package mcmp.block;

import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import mcmp.gui.MusicGui;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by setuga on 2015/02/10.
 */
public class TileEntityMusicPlayerBlock extends TileEntity{

    private volatile boolean redPowerOn = false;

    public void setRedPower(boolean on)
    {
            synchronized (TileEntityMusicPlayerBlock.class)
            {
                if (!this.redPowerOn && on)
                {
                    this.redPowerOn = true;
                    int status = MusicGui.basicPlayer.getStatus();
                    if (status == BasicPlayer.PLAYING){
                        try {
                            MusicGui.basicPlayer.pause();
                        } catch (BasicPlayerException ex){
                            System.out.println(ex);
                        }
                    } else {

                    }
                }
                else if (this.redPowerOn && !on)
                {
                    this.redPowerOn = false;
                    int status = MusicGui.basicPlayer.getStatus();
                    if (status == BasicPlayer.PAUSED){
                        try {
                            MusicGui.basicPlayer.resume();
                        } catch (BasicPlayerException ex){
                            System.out.println(ex);
                        }
                    } else {

                    }
                }
            }
    }
}
