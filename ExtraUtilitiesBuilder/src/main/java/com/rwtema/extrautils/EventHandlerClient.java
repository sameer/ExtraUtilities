// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils;

import net.minecraft.client.audio.ISound;
import java.util.HashMap;
import net.minecraft.util.AxisAlignedBB;
import com.rwtema.extrautils.item.ItemBuildersWand;
import com.rwtema.extrautils.tileentity.TileEntitySoundMuffler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.client.audio.ITickableSound;
import net.minecraft.world.World;
import com.rwtema.extrautils.tileentity.TileEntityRainMuffler;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.Entity;
import com.rwtema.extrautils.core.CastIterator;
import net.minecraftforge.client.event.sound.PlaySoundEvent17;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.util.MathHelper;
import net.minecraft.init.Blocks;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderLivingEvent;
import com.rwtema.extrautils.item.ItemLawSword;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import net.minecraftforge.client.event.RenderPlayerEvent;
import com.rwtema.extrautils.tileentity.generators.TileEntityGeneratorFurnace;
import java.util.Locale;
import com.rwtema.extrautils.tileentity.generators.DynamicContainerGenerator;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import java.util.ArrayList;
import net.minecraft.util.IIcon;
import java.util.List;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EventHandlerClient
{
    private static final ResourceLocation[] wing_textures;
    public static Map<String, Integer> flyingPlayers;
    public static List<String> holograms;
    public boolean resetRender;
    boolean avoidRecursion;
    int maxSonarRange;
    List<ChunkPos> sonar_edges;
    ChunkPos curTarget;
    private static float renderTickTime;
    private static long time;
    public static IIcon particle;
    public static final ResourceLocation temaSword;
    
    public EventHandlerClient() {
        this.resetRender = false;
        this.avoidRecursion = false;
        this.maxSonarRange = 4;
        this.sonar_edges = new ArrayList<ChunkPos>();
        this.curTarget = new ChunkPos(-1, -1, -1);
    }
    
    public float getRenderTickTime() {
        return EventHandlerClient.renderTickTime;
    }
    
    public float getRenderTime() {
        return EventHandlerClient.time + EventHandlerClient.renderTickTime;
    }
    
    @SubscribeEvent
    public void getTimer(final TickEvent.ClientTickEvent event) {
        ++EventHandlerClient.time;
    }
    
    @SubscribeEvent
    public void registerParticle(final TextureStitchEvent.Pre event) {
        if (event.map.getTextureType() != 0) {
            return;
        }
        EventHandlerClient.particle = event.map.registerIcon("extrautils:particle");
    }
    
    @SubscribeEvent
    public void getTimer(final TickEvent.RenderTickEvent event) {
        EventHandlerClient.renderTickTime = event.renderTickTime;
    }
    
    @SubscribeEvent
    public void generatorTooltip(final ItemTooltipEvent event) {
        if (!ExtraUtils.generatorEnabled) {
            return;
        }
        if (event.entityPlayer == null || event.entityPlayer.openContainer == null) {
            return;
        }
        if (event.entityPlayer.openContainer.getClass() == DynamicContainerGenerator.class) {
            final TileEntityGeneratorFurnace gen = ((DynamicContainerGenerator)event.entityPlayer.openContainer).genFurnace;
            if (gen != null) {
                final double burn = gen.getFuelBurn(event.itemStack);
                if (burn > 0.0) {
                    final double level = gen.getGenLevelForStack(event.itemStack);
                    final double amount = level * burn;
                    String s = "Generates ";
                    if (amount == (int)amount) {
                        s += String.format(Locale.ENGLISH, "%,d", (int)amount);
                    }
                    else {
                        s += String.format(Locale.ENGLISH, "%,.1f", amount);
                    }
                    s += " RF at ";
                    if (level == (int)level) {
                        s += String.format(Locale.ENGLISH, "%,d", (int)level);
                    }
                    else {
                        s += String.format(Locale.ENGLISH, "%,.1f", level);
                    }
                    s += " RF/T";
                    event.toolTip.add(s);
                }
            }
        }
    }
    
    @SubscribeEvent
    public void renderWings(final RenderPlayerEvent.Specials.Post event) {
        if (EventHandlerClient.flyingPlayers.containsKey(event.entityPlayer.getGameProfile().getName())) {
            final int tex = EventHandlerClient.flyingPlayers.get(event.entityPlayer.getGameProfile().getName());
            if (tex <= 0 || tex >= EventHandlerClient.wing_textures.length) {
                return;
            }
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glPushMatrix();
            event.renderer.modelBipedMain.bipedBody.postRender(0.0625f);
            Minecraft.getMinecraft().renderEngine.bindTexture(EventHandlerClient.wing_textures[tex]);
            GL11.glTranslatef(0.0f, -0.3125f, 0.25f);
            final Tessellator t = Tessellator.instance;
            final double d = 0.0;
            float a = (1.0f + (float)Math.cos(this.getRenderTime() / 4.0f)) * 2.0f;
            if (event.entityPlayer.capabilities.isFlying) {
                a = (1.0f + (float)Math.cos(this.getRenderTime() / 4.0f)) * 20.0f;
            }
            a += 5.0f;
            GL11.glPushMatrix();
            GL11.glRotatef(-20.0f, 0.0f, 1.0f, 0.0f);
            GL11.glRotatef(-a, 0.0f, 1.0f, 0.0f);
            t.startDrawingQuads();
            t.addVertexWithUV(0.0, 0.0, 0.0, 0.0, 0.0);
            t.addVertexWithUV(0.0, 1.0, 0.0, 0.0, 1.0);
            t.addVertexWithUV(1.0, 1.0, d, 1.0, 1.0);
            t.addVertexWithUV(1.0, 0.0, d, 1.0, 0.0);
            t.draw();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glRotatef(a, 0.0f, 1.0f, 0.0f);
            GL11.glRotatef(20.0f, 0.0f, 1.0f, 0.0f);
            t.startDrawingQuads();
            t.addVertexWithUV(0.0, 0.0, 0.0, 0.0, 0.0);
            t.addVertexWithUV(0.0, 1.0, 0.0, 0.0, 1.0);
            t.addVertexWithUV(-1.0, 1.0, d, 1.0, 1.0);
            t.addVertexWithUV(-1.0, 0.0, d, 1.0, 0.0);
            t.draw();
            GL11.glPopMatrix();
            GL11.glPopMatrix();
        }
    }
    
    public float min0(final float x) {
        if (x < 0.0f) {
            return 0.0f;
        }
        return x;
    }
    
    @SubscribeEvent
    public void renderSword(final RenderPlayerEvent.Specials.Post event) {
        if (!"RWTema".equals(event.entityPlayer.getGameProfile().getName())) {
            return;
        }
        if (event.entityPlayer.getHideCape()) {
            return;
        }
        boolean holdingSword = false;
        if (event.entityPlayer.getHeldItem() != null && event.entityPlayer.getHeldItem().getItem() instanceof ItemLawSword) {
            holdingSword = true;
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        event.renderer.modelBipedMain.bipedBody.postRender(0.0625f);
        Minecraft.getMinecraft().renderEngine.bindTexture(EventHandlerClient.temaSword);
        GL11.glTranslatef(0.0f, 0.23750001f, 0.25f);
        final Tessellator t = Tessellator.instance;
        GL11.glRotatef(-55.0f, 0.0f, 1.0f, 1.0f);
        final float h = 87.0f;
        final float h2 = holdingSword ? 20.0f : 0.0f;
        final float w = 18.0f;
        final float w2 = 5.0f;
        final float w3 = 13.0f;
        final double u = w2 / w;
        final float h3 = h2 / h;
        GL11.glScalef(1.7f / h, 1.7f / h, 1.7f / h);
        GL11.glTranslatef(-w2 / 2.0f, -h / 2.0f, 0.0f);
        GL11.glPushMatrix();
        t.startDrawingQuads();
        t.setNormal(0.0f, 0.0f, -1.0f);
        t.addVertexWithUV(0.0, (double)h2, 0.0, 0.0, (double)h3);
        t.addVertexWithUV(0.0, (double)h, 0.0, 0.0, 1.0);
        t.addVertexWithUV((double)w2, (double)h, 0.0, u, 1.0);
        t.addVertexWithUV((double)w2, (double)h2, 0.0, u, (double)h3);
        t.setNormal(0.0f, 0.0f, 1.0f);
        t.addVertexWithUV((double)w2, (double)h2, (double)w2, u, (double)h3);
        t.addVertexWithUV((double)w2, (double)h, (double)w2, u, 1.0);
        t.addVertexWithUV(0.0, (double)h, (double)w2, 0.0, 1.0);
        t.addVertexWithUV(0.0, (double)h2, (double)w2, 0.0, (double)h3);
        t.setNormal(1.0f, 0.0f, 0.0f);
        t.addVertexWithUV((double)w2, (double)h2, (double)w2, u, (double)h3);
        t.addVertexWithUV((double)w2, (double)h, (double)w2, u, 1.0);
        t.addVertexWithUV((double)w2, (double)h, 0.0, 0.0, 1.0);
        t.addVertexWithUV((double)w2, (double)h2, 0.0, 0.0, (double)h3);
        t.setNormal(-1.0f, 0.0f, 0.0f);
        t.addVertexWithUV(0.0, (double)h2, 0.0, u, (double)h3);
        t.addVertexWithUV(0.0, (double)h, 0.0, u, 1.0);
        t.addVertexWithUV(0.0, (double)h, (double)w2, 0.0, 1.0);
        t.addVertexWithUV(0.0, (double)h2, (double)w2, 0.0, (double)h3);
        if (!holdingSword) {
            t.setNormal(0.0f, -1.0f, 0.0f);
            t.addVertexWithUV(0.0, 0.0, 0.0, (double)(9.0f / w), (double)(4.0f / h));
            t.addVertexWithUV((double)w2, 0.0, 0.0, (double)(13.0f / w), (double)(8.0f / h));
            t.addVertexWithUV((double)w2, 0.0, (double)w2, (double)(13.0f / w), (double)(8.0f / h));
            t.addVertexWithUV(0.0, 0.0, (double)w2, (double)(9.0f / w), (double)(4.0f / h));
        }
        t.setNormal(0.0f, 1.0f, 0.0f);
        t.addVertexWithUV(0.0, (double)h, 0.0, (double)(9.0f / w), (double)(4.0f / h));
        t.addVertexWithUV((double)w2, (double)h, 0.0, (double)(13.0f / w), (double)(8.0f / h));
        t.addVertexWithUV((double)w2, (double)h, (double)w2, (double)(13.0f / w), (double)(8.0f / h));
        t.addVertexWithUV(0.0, (double)h, (double)w2, (double)(9.0f / w), (double)(4.0f / h));
        if (!holdingSword) {
            t.setNormal(0.0f, -1.0f, 0.0f);
            t.addVertexWithUV(-3.0, 16.0, -3.0, (double)(6.0f / w), (double)(18.0f / h));
            t.addVertexWithUV(8.0, 16.0, -3.0, (double)(17.0f / w), (double)(18.0f / h));
            t.addVertexWithUV(8.0, 16.0, 8.0, (double)(17.0f / w), (double)(29.0f / h));
            t.addVertexWithUV(-3.0, 16.0, 8.0, (double)(6.0f / w), (double)(29.0f / h));
            t.setNormal(0.0f, 1.0f, 0.0f);
            t.addVertexWithUV(-3.0, 20.0, -3.0, (double)(6.0f / w), (double)(1.0f / h));
            t.addVertexWithUV(8.0, 20.0, -3.0, (double)(17.0f / w), (double)(1.0f / h));
            t.addVertexWithUV(8.0, 20.0, 8.0, (double)(17.0f / w), (double)(12.0f / h));
            t.addVertexWithUV(-3.0, 20.0, 8.0, (double)(6.0f / w), (double)(12.0f / h));
            t.setNormal(0.0f, 0.0f, -1.0f);
            t.addVertexWithUV(-3.0, 16.0, -3.0, u, (double)(12.0f / h));
            t.addVertexWithUV(-3.0, 20.0, -3.0, u, (double)(17.0f / h));
            t.addVertexWithUV(8.0, 20.0, -3.0, 1.0, (double)(17.0f / h));
            t.addVertexWithUV(8.0, 16.0, -3.0, 1.0, (double)(12.0f / h));
            t.setNormal(0.0f, 0.0f, 1.0f);
            t.addVertexWithUV(-3.0, 16.0, 8.0, u, (double)(12.0f / h));
            t.addVertexWithUV(-3.0, 20.0, 8.0, u, (double)(17.0f / h));
            t.addVertexWithUV(8.0, 20.0, 8.0, 1.0, (double)(17.0f / h));
            t.addVertexWithUV(8.0, 16.0, 8.0, 1.0, (double)(12.0f / h));
            t.setNormal(1.0f, 0.0f, 0.0f);
            t.addVertexWithUV(8.0, 16.0, 8.0, u, (double)(12.0f / h));
            t.addVertexWithUV(8.0, 20.0, 8.0, u, (double)(17.0f / h));
            t.addVertexWithUV(8.0, 20.0, -3.0, 1.0, (double)(17.0f / h));
            t.addVertexWithUV(8.0, 16.0, -3.0, 1.0, (double)(12.0f / h));
            t.setNormal(-1.0f, 0.0f, 0.0f);
            t.addVertexWithUV(-3.0, 16.0, 8.0, u, (double)(12.0f / h));
            t.addVertexWithUV(-3.0, 20.0, 8.0, u, (double)(17.0f / h));
            t.addVertexWithUV(-3.0, 20.0, -3.0, 1.0, (double)(17.0f / h));
            t.addVertexWithUV(-3.0, 16.0, -3.0, 1.0, (double)(12.0f / h));
        }
        t.draw();
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void entityColorRender(final RenderLivingEvent.Pre event) {
        final String s = EnumChatFormatting.getTextWithoutFormattingCodes(event.entity.getCommandSenderName());
        if (s.startsWith("Aureylian") && !(event.entity instanceof EntityPlayer)) {
            GL11.glColor4f(0.9686f, 0.7059f, 0.8392f, 1.0f);
            this.resetRender = true;
        }
        if (EventHandlerClient.holograms.contains(s) && (!(event.entity instanceof EntityPlayer) || !((EntityPlayer)event.entity).getHideCape())) {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.45f);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            this.resetRender = true;
        }
        if (s.equals("RWTema") && !(event.entity instanceof EntityPlayer)) {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.65f);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            this.resetRender = true;
        }
        if (s.equals("jadedcat") && (!(event.entity instanceof EntityPlayer) || !((EntityPlayer)event.entity).getHideCape())) {
            GL11.glColor4f(0.69f, 0.392f, 0.847f, 1.0f);
            this.resetRender = true;
        }
    }
    
    @SubscribeEvent
    public void entityColorRender(final RenderLivingEvent.Post event) {
        if (!this.avoidRecursion && this.resetRender) {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glDisable(3042);
        }
    }
    
    @SubscribeEvent
    public void SonarRender(final DrawBlockHighlightEvent event) {
        if (Minecraft.getMinecraft().gameSettings.thirdPersonView != 0) {
            return;
        }
        if (ExtraUtils.sonarGoggles == null || event.player.getCurrentArmor(3) == null || event.player.getCurrentArmor(3).getItem() != ExtraUtils.sonarGoggles) {
            return;
        }
        final Block id = event.player.worldObj.getBlock(event.target.blockX, event.target.blockY, event.target.blockZ);
        if (id == Blocks.air) {
            return;
        }
        if (!id.isOpaqueCube()) {
            return;
        }
        final int x = MathHelper.floor_double((double)event.player.chunkCoordX);
        final int y = MathHelper.floor_double((double)event.player.chunkCoordY);
        final int z = MathHelper.floor_double((double)event.player.chunkCoordZ);
        final double transparency = 1.0 - (event.player.worldObj.getSkyBlockTypeBrightness(EnumSkyBlock.Block, x, y, z) + event.player.worldObj.getSkyBlockTypeBrightness(EnumSkyBlock.Sky, x, y, z) - event.player.worldObj.calculateSkylightSubtracted(1.0f)) / 12.0;
        if (transparency <= 0.0) {
            return;
        }
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.35f);
        GL11.glLineWidth(3.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glShadeModel(7425);
        final double px = event.player.lastTickPosX + (event.player.chunkCoordX - event.player.lastTickPosX) * event.partialTicks;
        final double py = event.player.lastTickPosY + (event.player.chunkCoordY - event.player.lastTickPosY) * event.partialTicks;
        final double pz = event.player.lastTickPosZ + (event.player.chunkCoordZ - event.player.lastTickPosZ) * event.partialTicks;
        GL11.glTranslated(-px, -py, -pz);
        if (this.curTarget.x != event.target.blockX || this.curTarget.y != event.target.blockY || this.curTarget.z != event.target.blockZ) {
            this.curTarget = new ChunkPos(event.target.blockX, event.target.blockY, event.target.blockZ);
            this.sonar_edges.clear();
            final List<ChunkPos> blocks = new ArrayList<ChunkPos>();
            blocks.add(this.curTarget);
            for (int i = 0; i < blocks.size(); ++i) {
                int j = 0;
                final boolean[] s = new boolean[27];
                for (int dy = -1; dy <= 1; ++dy) {
                    for (int dx = -1; dx <= 1; ++dx) {
                        for (int dz = -1; dz <= 1; ++dz) {
                            s[j] = false;
                            final ChunkPos b = new ChunkPos(blocks.get(i).x + dx, blocks.get(i).y + dy, blocks.get(i).z + dz);
                            if (!blocks.contains(b)) {
                                if (this.dist(b.x - event.target.blockX, b.y - event.target.blockY, b.z - event.target.blockZ) < this.maxSonarRange && event.player.worldObj.getBlock(b.x, b.y, b.z) == id) {
                                    if ((dx == 0 && dy == 0) || (dz == 0 && dy == 0) || (dx == 0 && dz == 0)) {
                                        blocks.add(b);
                                    }
                                    s[j] = true;
                                }
                            }
                            else {
                                s[j] = true;
                            }
                            ++j;
                        }
                    }
                }
                final int x2 = blocks.get(i).x;
                final int y2 = blocks.get(i).y;
                final int z2 = blocks.get(i).z;
                if (this.d(s[10], s[12], s[9])) {
                    this.sonar_edges.add(new ChunkPos(x2, y2, z2));
                    this.sonar_edges.add(new ChunkPos(x2, y2 + 1, z2));
                }
                if (this.d(s[10], s[14], s[11])) {
                    this.sonar_edges.add(new ChunkPos(x2, y2, z2 + 1));
                    this.sonar_edges.add(new ChunkPos(x2, y2 + 1, z2 + 1));
                }
                if (this.d(s[16], s[12], s[15])) {
                    this.sonar_edges.add(new ChunkPos(x2 + 1, y2, z2));
                    this.sonar_edges.add(new ChunkPos(x2 + 1, y2 + 1, z2));
                }
                if (this.d(s[16], s[14], s[17])) {
                    this.sonar_edges.add(new ChunkPos(x2 + 1, y2, z2 + 1));
                    this.sonar_edges.add(new ChunkPos(x2 + 1, y2 + 1, z2 + 1));
                }
                if (this.d(s[22], s[10], s[19])) {
                    this.sonar_edges.add(new ChunkPos(x2, y2 + 1, z2));
                    this.sonar_edges.add(new ChunkPos(x2, y2 + 1, z2 + 1));
                }
                if (this.d(s[22], s[16], s[25])) {
                    this.sonar_edges.add(new ChunkPos(x2 + 1, y2 + 1, z2));
                    this.sonar_edges.add(new ChunkPos(x2 + 1, y2 + 1, z2 + 1));
                }
                if (this.d(s[22], s[12], s[21])) {
                    this.sonar_edges.add(new ChunkPos(x2, y2 + 1, z2));
                    this.sonar_edges.add(new ChunkPos(x2 + 1, y2 + 1, z2));
                }
                if (this.d(s[22], s[14], s[23])) {
                    this.sonar_edges.add(new ChunkPos(x2, y2 + 1, z2 + 1));
                    this.sonar_edges.add(new ChunkPos(x2 + 1, y2 + 1, z2 + 1));
                }
                if (this.d(s[4], s[10], s[1])) {
                    this.sonar_edges.add(new ChunkPos(x2, y2, z2));
                    this.sonar_edges.add(new ChunkPos(x2, y2, z2 + 1));
                }
                if (this.d(s[4], s[16], s[7])) {
                    this.sonar_edges.add(new ChunkPos(x2 + 1, y2, z2));
                    this.sonar_edges.add(new ChunkPos(x2 + 1, y2, z2 + 1));
                }
                if (this.d(s[4], s[12], s[3])) {
                    this.sonar_edges.add(new ChunkPos(x2, y2, z2));
                    this.sonar_edges.add(new ChunkPos(x2 + 1, y2, z2));
                }
                if (this.d(s[4], s[14], s[5])) {
                    this.sonar_edges.add(new ChunkPos(x2, y2, z2 + 1));
                    this.sonar_edges.add(new ChunkPos(x2 + 1, y2, z2 + 1));
                }
            }
        }
        final Tessellator t = Tessellator.instance;
        t.startDrawing(1);
        t.setColorRGBA(255, 255, 255, 255);
        for (final ChunkPos sonar_edge : this.sonar_edges) {
            t.setColorRGBA(255, 255, 255, (int)(transparency * (155.0 - 100.0 * this.dist(sonar_edge.x - px, sonar_edge.y - py, sonar_edge.z - pz) / (this.maxSonarRange + 1))));
            t.addVertex((double)sonar_edge.x, (double)sonar_edge.y, (double)sonar_edge.z);
        }
        t.draw();
        GL11.glShadeModel(7424);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glTranslated(px, py, pz);
    }
    
    public boolean d(final boolean side1, final boolean side2, final boolean corner) {
        return (!side1 && !side2) || (!corner && side1 && side2);
    }
    
    public double dist(final double x, final double y, final double z) {
        return (int)Math.max(Math.max(Math.abs(x), Math.abs(y)), Math.abs(z));
    }
    
    @SubscribeEvent
    public void witherSoundKilling(final PlaySoundEvent17 event) {
        if (!ExtraUtils.disableWitherNoiseUnlessNearby) {
            return;
        }
        if (!"mob.wither.spawn".equals(event.name)) {
            return;
        }
        for (final Entity e : new CastIterator(Minecraft.getMinecraft().theWorld.getLoadedEntityList())) {
            if (e.getClass() == EntityWither.class) {
                return;
            }
        }
        event.result = null;
    }
    
    @SubscribeEvent
    public void rainKiller(final PlaySoundEvent17 event) {
        if (Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().thePlayer != null && ExtraUtils.soundMuffler != null) {
            if (event == null || !"ambient.weather.rain".equals(event.name)) {
                return;
            }
            final World world = (World)Minecraft.getMinecraft().theWorld;
            NBTTagCompound tags = new NBTTagCompound();
            if (Minecraft.getMinecraft().thePlayer.getEntityData().hasKey("PlayerPersisted")) {
                tags = Minecraft.getMinecraft().thePlayer.getEntityData().getCompoundTag("PlayerPersisted");
            }
            else {
                Minecraft.getMinecraft().thePlayer.getEntityData().setTag("PlayerPersisted", (NBTBase)tags);
            }
            final boolean force = tags.hasKey("ExtraUtilities|Rain") && tags.getBoolean("ExtraUtilities|Rain");
            if (!force && TileEntityRainMuffler.playerNeedsMuffler) {
                TileEntityRainMuffler.playerNeedsMufflerInstantCheck = false;
            }
            else {
                event.result = null;
                if (force) {
                    return;
                }
                boolean flag = false;
                if (world.provider.dimensionId != TileEntityRainMuffler.curDimension) {
                    flag = true;
                }
                else if (!(world.getTileEntity(TileEntityRainMuffler.curX, TileEntityRainMuffler.curY, TileEntityRainMuffler.curZ) instanceof TileEntityRainMuffler)) {
                    flag = true;
                }
                else if (world.getTileEntity(TileEntityRainMuffler.curX, TileEntityRainMuffler.curY, TileEntityRainMuffler.curZ).getDistanceFrom((double)Minecraft.getMinecraft().thePlayer.chunkCoordX, (double)Minecraft.getMinecraft().thePlayer.chunkCoordX, (double)Minecraft.getMinecraft().thePlayer.chunkCoordZ) > 4096.0) {
                    flag = true;
                }
                if (flag) {
                    TileEntityRainMuffler.playerNeedsMuffler = true;
                    TileEntityRainMuffler.playerNeedsMufflerInstantCheck = true;
                    TileEntityRainMuffler.curDimension = -100;
                }
            }
        }
    }
    
    @SubscribeEvent
    public void soundMufflerPlay(final PlaySoundEvent17 event) {
        if (Minecraft.getMinecraft().theWorld != null && ExtraUtils.soundMuffler != null && event.result != null) {
            final World world = (World)Minecraft.getMinecraft().theWorld;
            if (event.result instanceof ITickableSound) {
                return;
            }
            final float x = event.result.getXPosF();
            final float y = event.result.getYPosF();
            final float z = event.result.getZPosF();
            for (int dx = (int)Math.floor(x - 8.0f) >> 4; dx <= (int)Math.floor(x + 8.0f) >> 4; ++dx) {
                for (int dz = (int)Math.floor(z - 8.0f) >> 4; dz <= (int)Math.floor(z + 8.0f) >> 4; ++dz) {
                    for (final TileEntity var19 : world.getChunkFromChunkCoords(dx, dz).chunkTileEntityMap.values()) {
                        if (var19 instanceof TileEntitySoundMuffler) {
                            if (var19.getBlockMetadata() == 1) {
                                continue;
                            }
                            double d = (var19.xCoord + 0.5 - x) * (var19.xCoord + 0.5 - x) + (var19.yCoord + 0.5 - y) * (var19.yCoord + 0.5 - y) + (var19.zCoord + 0.5 - z) * (var19.zCoord + 0.5 - z);
                            if (d > 64.0) {
                                continue;
                            }
                            if (d <= 0.0) {
                                continue;
                            }
                            event.result = (ISound)new SoundMuffled(event.result);
                            d = Math.sqrt(d);
                            if (d != 0.0) {
                                d = 1.0 / d / 8.0;
                            }
                            world.spawnParticle("smoke", (double)x, (double)y, (double)z, (var19.xCoord + 0.5 - x) * d, (var19.yCoord + 0.5 - y) * d, (var19.zCoord + 0.5 - z) * d);
                        }
                    }
                }
            }
        }
    }
    
    @SubscribeEvent
    public void BuildersWandRender(final DrawBlockHighlightEvent event) {
        if (event.currentItem != null && event.currentItem.getItem() instanceof ItemBuildersWand) {
            final List<ChunkPos> blocks = ((ItemBuildersWand)event.currentItem.getItem()).getPotentialBlocks(event.player, event.player.worldObj, event.target.blockX, event.target.blockY, event.target.blockZ, event.target.sideHit);
            final Block blockId = event.player.worldObj.getBlock(event.target.blockX, event.target.blockY, event.target.blockZ);
            if (blockId != Blocks.air & blocks.size() > 0) {
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.35f);
                GL11.glLineWidth(3.0f);
                GL11.glDisable(3553);
                GL11.glDisable(2929);
                GL11.glDepthMask(false);
                final double px = event.player.lastTickPosX + (event.player.posX - event.player.lastTickPosX) * event.partialTicks;
                final double py = event.player.lastTickPosY + (event.player.posY - event.player.lastTickPosY) * event.partialTicks;
                final double pz = event.player.lastTickPosZ + (event.player.posZ - event.player.lastTickPosZ) * event.partialTicks;
                GL11.glTranslated(-px, -py, -pz);
                for (final ChunkPos temp : blocks) {
                    this.drawOutlinedBoundingBox(AxisAlignedBB.getBoundingBox((double)temp.x, (double)temp.y, (double)temp.z, (double)(temp.x + 1), (double)(temp.y + 1), (double)(temp.z + 1)));
                }
                GL11.glDepthMask(true);
                GL11.glEnable(3553);
                GL11.glDisable(3042);
                GL11.glEnable(2929);
                GL11.glTranslated(px, py, pz);
                event.setCanceled(true);
            }
        }
    }
    
    private void drawOutlinedBoundingBox(final AxisAlignedBB par1AxisAlignedBB) {
        final Tessellator var2 = Tessellator.instance;
        var2.startDrawing(3);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        var2.draw();
        var2.startDrawing(3);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        var2.draw();
        var2.startDrawing(1);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
        var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
        var2.draw();
    }
    
    static {
        wing_textures = new ResourceLocation[] { null, new ResourceLocation("extrautils", "textures/wing_feather.png"), new ResourceLocation("extrautils", "textures/wing_butterfly.png"), new ResourceLocation("extrautils", "textures/wing_demon.png"), new ResourceLocation("extrautils", "textures/wing_golden.png"), new ResourceLocation("extrautils", "textures/wing_bat.png") };
        EventHandlerClient.flyingPlayers = new HashMap<String, Integer>();
        EventHandlerClient.holograms = new ArrayList<String>();
        EventHandlerClient.time = 0L;
        temaSword = new ResourceLocation("extrautils", "textures/rwtemaSword.png");
    }
    
    public class SoundMuffled implements ISound
    {
        protected final ISound sound;
        
        public SoundMuffled(final ISound sound) {
            this.sound = sound;
        }
        
        public ResourceLocation getPositionedSoundLocation() {
            return this.sound.getPositionedSoundLocation();
        }
        
        public boolean canRepeat() {
            return this.sound.canRepeat();
        }
        
        public int getRepeatDelay() {
            return this.sound.getRepeatDelay();
        }
        
        public float getVolume() {
            return this.sound.getVolume() / 10.0f;
        }
        
        public float getPitch() {
            return this.sound.getPitch();
        }
        
        public float getXPosF() {
            return this.sound.getXPosF();
        }
        
        public float getYPosF() {
            return this.sound.getYPosF();
        }
        
        public float getZPosF() {
            return this.sound.getZPosF();
        }
        
        public ISound.AttenuationType getAttenuationType() {
            return this.sound.getAttenuationType();
        }
    }
}

