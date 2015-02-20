package mcmp.gui;

import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import mcmp.McmpCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by setuga on 2015/02/11.
 *
 * 参考: tana@ヒキニートP様 作 [1.7.2]MMD-Mod
 */
public class MusicGui extends GuiContainer
{
    private static final ResourceLocation bgImage = new ResourceLocation(McmpCore.modid + ":textures/gui/bg.png");

    public static BasicPlayer basicPlayer = new BasicPlayer();

    private GuiFileList fileList;

    private GuiTextField textMusicFile;

    private GuiButton buttonSelect;
    private GuiButton buttonPlay;
    private GuiButton buttonStop;
    private GuiButton buttonPause;
    private GuiButton buttonResume;
    private GuiButton buttonTurnUpVolume;
    private GuiButton buttonTurnDownVolume;
    private GuiButton buttonApply;
    private GuiButton buttonCancel;

    private String filePath;

    private GuiButton clicked = null;

    public MusicGui(int x, int y, int z)
    {
        super(new MusicContainer(x, y, z));
    }

    @Override
    public void initGui()
    {
        super.initGui();

        this.guiLeft = 0;
        this.guiTop = 0;

        Keyboard.enableRepeatEvents(true);

        this.fileList = new GuiFileList(this.mc,
                (int)(this.width * 0.65F), this.height, (int)(this.height * 0.1F), (int)(this.height * 0.85F), 20);
        this.fileList.left = 33;
        this.fileList.registerScrollButtons(10, 11);

        this.textMusicFile = new GuiTextField(this.fontRendererObj,
                (int)(this.width * 0.225F), (int)(this.height * 0.02F), (int)(this.width * 0.38F), 16);
        this.textMusicFile.setMaxStringLength(2048);

        this.buttonSelect = new GuiButton(1,
                (int)(this.width * 0.74F), (int)(this.height * 0.15F) - 2, (int)(this.width * 0.1F), 20, "Select");
        this.buttonPlay = new GuiButton(2,
                (int)(this.width * 0.74F), (int)(this.height * 0.30F) - 2, (int)(this.width * 0.1F), 20, "Play");
        this.buttonStop = new GuiButton(3,
                (int)(this.width * 0.74F), (int)(this.height * 0.45F) - 2, (int)(this.width * 0.1F), 20, "Stop");
        this.buttonPause = new GuiButton(4,
                (int)(this.width * 0.74F), (int)(this.height * 0.60F) - 2, (int)(this.width * 0.1F), 20, "Pause");
        this.buttonResume = new GuiButton(5,
                (int)(this.width * 0.74F), (int)(this.height * 0.75F) - 2, (int)(this.width * 0.1F), 20, "Resume");

        this.buttonTurnUpVolume = new GuiButton(6,
                (int)(this.width * 0.88F), (int)(this.height * 0.35F) - 2, (int)(this.width * 0.1F), 20, "Up");
        this.buttonTurnDownVolume = new GuiButton(7,
                (int)(this.width * 0.88F), (int)(this.height * 0.55F) - 2, (int)(this.width * 0.1F), 20, "Down");

        this.buttonApply = new GuiButton(0,
                (int)(this.width * 0.08F), (int)(this.height * 0.9F) - 2, (int)(this.width * 0.4F), 20, "Apply");
        this.buttonCancel = new GuiButton(9,
                (int)(this.width * 0.52F), (int)(this.height * 0.9F) - 2, (int)(this.width * 0.4F), 20, "Cancel");

        this.buttonList.clear();
        this.buttonList.add(this.buttonSelect);
        this.buttonList.add(this.buttonPlay);
        this.buttonList.add(this.buttonStop);
        this.buttonList.add(this.buttonPause);
        this.buttonList.add(this.buttonResume);
        this.buttonList.add(this.buttonTurnUpVolume);
        this.buttonList.add(this.buttonTurnDownVolume);
        this.buttonList.add(this.buttonApply);
        this.buttonList.add(this.buttonCancel);

        initValues();
    }

    private void initValues()
    {

    }

    @Override
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
        super.onGuiClosed();
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button == this.buttonSelect){
            this.clicked = button;
            this.fileList.list.clear();
            this.fileList.currentIndex = 0;
            this.getRoots();
            this.findFiles(new File(McmpCore.dirPath), "mp3");
            this.findFiles(new File(McmpCore.dirPath), "wav");
            this.findFiles(new File(McmpCore.dirPath), "ogg");
        }
        else if (button == this.buttonPlay)
        {
            this.clicked = button;
            this.filePath = this.textMusicFile.getText();
            File musicFile = new File(filePath);
            try {
                open(musicFile);
            } catch (BasicPlayerException e) {
                e.printStackTrace();
            }
        }else if (button == this.buttonStop)
        {
            this.clicked = button;
            McmpCore.volume = 0.5;
            int status = basicPlayer.getStatus();
            if (status == BasicPlayer.PLAYING) {
                try {
                    stop();
                } catch (BasicPlayerException e) {
                    e.printStackTrace();
                }
            }
        }
        else if (button == this.buttonPause)
        {
            this.clicked = button;
            int status = basicPlayer.getStatus();
            if(status == BasicPlayer.PLAYING) {
                try {
                    pause();
                } catch (BasicPlayerException e) {
                    e.printStackTrace();
                }
            }
        }
        else if (button == this.buttonResume)
        {
            this.clicked = button;
            int status = basicPlayer.getStatus();
            if(status == BasicPlayer.PAUSED) {
                try {
                    resume();
                } catch (BasicPlayerException e) {
                    e.printStackTrace();
                }
            }
        }
        else if(button == this.buttonTurnUpVolume){
            try {
                int status = MusicGui.basicPlayer.getStatus();
                if (status == BasicPlayer.PLAYING) {
                    if (McmpCore.volume < 1.0) {
                        McmpCore.volume = McmpCore.volume + 0.1;
                        System.out.println(McmpCore.volume);
                        MusicGui.basicPlayer.setGain(McmpCore.volume);
                    } else if(McmpCore.volume > 1.0){
                        McmpCore.volume = 1.0;
                        System.out.println(McmpCore.volume);
                        MusicGui.basicPlayer.setGain(McmpCore.volume);
                    }
                }
            } catch (BasicPlayerException e) {
                e.printStackTrace();
            }
        }
        else if(button == this.buttonTurnDownVolume){
            try {
                int status = MusicGui.basicPlayer.getStatus();
                if (status == BasicPlayer.PLAYING) {
                    if (McmpCore.volume > 0.0) {
                        McmpCore.volume = McmpCore.volume - 0.1;
                        System.out.println(McmpCore.volume);
                        MusicGui.basicPlayer.setGain(McmpCore.volume);
                    }
                    else if(McmpCore.volume < 0.0){
                        McmpCore.volume = 0.0;
                        System.out.println(McmpCore.volume);
                        MusicGui.basicPlayer.setGain(McmpCore.volume);
                    }
                }
            } catch (BasicPlayerException e) {
                e.printStackTrace();
            }
        }
        else if (button == buttonApply)
        {
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
        }
        else if (button == buttonCancel)
        {
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
            McmpCore.volume = 0.5;
            int status = basicPlayer.getStatus();
            if(status == BasicPlayer.PLAYING) {
                try {
                    basicPlayer.stop();
                } catch (BasicPlayerException e) {
                    e.printStackTrace();
                }
            }
        }
        this.fileList.actionPerformed(button);
        super.actionPerformed(button);
    }

    @Override
    protected void mouseClicked(int x, int y, int button)
    {
        this.textMusicFile.mouseClicked(x, y, button);
        super.mouseClicked(x, y, button);
    }

    @Override
    protected void keyTyped(char par1, int par2)
    {
        this.textMusicFile.textboxKeyTyped(par1, par2);
        super.keyTyped(par1, par2);
    }

    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        super.drawScreen(par1, par2, par3);

        this.fontRendererObj.drawString("MusicFile",
                (int)(this.width * 0.15F), (int)(this.height * 0.06F) - 10, 0x000000);

        this.fileList.drawScreen(par1, par2, par3);
        this.textMusicFile.drawTextBox();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(bgImage);
        GL11.glEnable(GL11.GL_BLEND);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(0.0, 0.0, this.zLevel, 0.0, 0.0);
        tessellator.addVertexWithUV(0.0, this.height, this.zLevel, 0.0, 1.0);
        tessellator.addVertexWithUV(this.width, this.height, this.zLevel, 1.0, 1.0);
        tessellator.addVertexWithUV(this.width, 0.0, this.zLevel, 1.0, 0.0);
        tessellator.draw();
        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void drawWorldBackground(int par1)
    {
        if (this.mc.theWorld != null)
        {
            this.drawGradientRect(0, 0, this.width, this.height, 0x00000000, 0x00000000);
        }
        else
        {
            this.drawBackground(par1);
        }
    }
    private void pause() throws BasicPlayerException {
        basicPlayer.pause();
    }

    private void resume() throws BasicPlayerException {
        basicPlayer.resume();
    }

    private void play() throws BasicPlayerException {
        basicPlayer.play();
    }

    public static void stop() throws BasicPlayerException {
        basicPlayer.stop();
    }

    private void open(File file) throws BasicPlayerException {
        basicPlayer.open(file);
        play();
    }

    private void findFiles(File current, String suffix)
    {
        try
        {
            if (current.exists())
            {
                if (current.isDirectory())
                {
                    File[] files = current.listFiles();
                    if (files != null)
                    {
                        for (File file : files)
                        {
                            findFiles(file, suffix);
                        }
                    }
                }
                else
                {
                    String path = current.toString();
                    int index = path.lastIndexOf(".") + 1;
                    if (index >= 0 && path.substring(index).equalsIgnoreCase(suffix))
                    {
                        this.fileList.list.add(current);
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void getRoots(){
        File[] listRoots = new File(McmpCore.dirPath).listRoots();
        for (File root : listRoots) {
            this.fileList.list.add(root);
        }
    }

    private void getSubDirs(File current, String ... suffixes){
        if (current == null || !current.canRead()) {
            return;
        }
        if (current.isDirectory()){
            this.fileList.list.add(current);
            return;
        }
        if (current.isFile()){
            String path = current.toString();
            for (String suffix : suffixes){
                if (path.endsWith("." + suffix))
                {
                    this.fileList.list.add(current);
                }
            }
        }
    }

    public class GuiFileList extends GuiSlot_Custom
    {
        public List<File> list = new ArrayList<File>();

        public int currentIndex = 0;

        public GuiFileList(Minecraft mc, int width, int height, int top, int bottom, int slotHeight)
        {
            super(mc, width, height, top, bottom, slotHeight);
        }

        @Override
        protected int getSize()
        {
            return list.size();
        }

        @Override
        protected void elementClicked(int var1, boolean var2, int var3, int var4)
        {
            this.currentIndex = var1;

            File file = this.list.get(this.currentIndex);
            String path = file != null ? file.getAbsolutePath() : "";
            if (file != null && file.isDirectory()){
                fileList.list.clear();
                File parent = file.getParentFile();
                if (parent != null){
                    fileList.list.add(parent);
                } else {
                    getRoots();
                }
                for (File f : file.listFiles()){
                    if (!f.canRead()){
                        continue;
                    }
                    if (f.isHidden()){
                        continue;
                    }
                    String fp = f.getPath();
                    if (!Files.isReadable(Paths.get(fp)) || Files.isSymbolicLink(Paths.get(fp))){
                        continue;
                    }
                    getSubDirs(f, "mp3", "wav", "ogg");
                }
            }
            MusicGui.this.textMusicFile.setText(path);

        }

        @Override
        protected boolean isSelected(int var1)
        {
            return this.currentIndex == var1;
        }

        @Override
        protected void drawBackground()
        {
        }

        @Override
        protected void drawSlot(int var1, int var2, int var3, int var4, Tessellator var5, int var6, int var7)
        {
            File file = list.get(var1);
            String label = file != null ? file.getName() : "-";
            if (label.isEmpty()){
                label = file.getPath();
            }
            if (label.length() > 25)
            {
                label = label.substring(0, 25);
            }
            MusicGui.this.fontRendererObj.drawString(label,
                    (int)(this.width * 0.5F) - MusicGui.this.fontRendererObj.getStringWidth(label) / 2, var3 + 4,
                    0x00000000);
        }

        @Override
        protected void overlayBackground(int var1, int var2, int var3, int var4)
        {
        }

        @Override
        protected void drawContainerBackground(Tessellator tessellator)
        {
        }

        @Override
        public int getListWidth()
        {
            return (int)(this.width * 0.95F);
        }

        @Override
        protected int getScrollBarX()
        {
            return (int)(this.width - 10);
        }
    }
}
