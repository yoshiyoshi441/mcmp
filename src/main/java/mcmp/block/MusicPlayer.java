package mcmp.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import javazoom.jlgui.basicplayer.BasicPlayer;
import mcmp.McmpCore;
import mcmp.gui.MusicGui;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.io.*;

/**
 * Created by setuga on 2015/02/01.
 */
public class MusicPlayer extends BlockContainer {

    @SideOnly(Side.CLIENT)
    private IIcon TopIcon;
    @SideOnly(Side.CLIENT)
    private IIcon SideIcon;

    public MusicPlayer(Material material) {
        super(material);
        setBlockName("Player");
        setCreativeTab(CreativeTabs.tabBlock);
        setStepSound(Block.soundTypeMetal);
        setHardness(2.0F);
        setResistance(1.0F);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float posX, float posY, float posZ){
        if (!world.isRemote)
        {
            player.openGui(McmpCore.instance, McmpCore.instance.musicGuiID, world, x, y, z);
        }
        return true;
    }

    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player){

    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighborBlock){
        if(!world.isRemote)
        {
            ((TileEntityMusicPlayerBlock)world.getTileEntity(x, y, z)).setRedPower(
                    world.isBlockIndirectlyGettingPowered(x, y, z));
        }
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int l){
        //壊されたとき
        try {
            int status = MusicGui.basicPlayer.getStatus();
            if (status == BasicPlayer.PLAYING) {
                MusicGui.basicPlayer.stop();
            }
        } catch (Exception ex) {
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.TopIcon = par1IconRegister.registerIcon(McmpCore.modid + ":MusicPlayer_Top");
        this.SideIcon = par1IconRegister.registerIcon(McmpCore.modid + ":MusicPlayer_Side");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int par1, int par2)
    {
        if(par1 == 0 || par1 == 1)
        {
            return TopIcon;
        }
        if(par1 == 2 || par1 == 3 || par1 == 4 || par1 == 5 || par1 == 6)
        {
            return SideIcon;
        }
        else
        {
            return null;
        }
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityMusicPlayerBlock();
    }
}
