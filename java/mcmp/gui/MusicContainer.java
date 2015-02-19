package mcmp.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

/**
 * Created by setuga on 2015/02/11.
 *
 * 参考: tana@ヒキニートP様 作 [1.7.2]MMD-Mod
 */
public class MusicContainer extends Container {

    int xCoord, yCoord, zCorrd;

    public MusicContainer(int x, int y, int z) {
        this.xCoord = x;
        this.yCoord = y;
        this.zCorrd = z;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
}
