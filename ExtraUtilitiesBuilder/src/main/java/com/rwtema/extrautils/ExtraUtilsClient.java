// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils;

import com.rwtema.extrautils.network.XUPacketBase;
import com.rwtema.extrautils.network.NetworkHandler;
import com.rwtema.extrautils.network.packets.PacketUseItemAlt;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import com.rwtema.extrautils.network.PacketHandlerClient;
import com.rwtema.extrautils.network.PacketHandler;
import net.minecraft.world.World;
import java.util.Iterator;
import com.rwtema.extrautils.item.RenderItemGlove;
import com.rwtema.extrautils.item.RenderItemLawSword;
import com.rwtema.extrautils.multipart.microblock.RenderItemMicroblock;
import net.minecraft.block.Block;
import com.rwtema.extrautils.block.BlockColor;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import cpw.mods.fml.client.registry.ClientRegistry;
import com.rwtema.extrautils.tileentity.RenderTileEntitySpike;
import com.rwtema.extrautils.tileentity.TileEntityEnchantedSpike;
import com.rwtema.extrautils.block.render.RenderBlockConnectedTexturesEthereal;
import com.rwtema.extrautils.block.render.RenderBlockDrum;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraft.item.Item;
import com.rwtema.extrautils.item.ItemBlockSpike;
import com.rwtema.extrautils.block.render.RenderBlockMultiBlock;
import com.rwtema.extrautils.block.render.RenderBlockFullBright;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import com.rwtema.extrautils.block.render.RenderBlockColor;
import cpw.mods.fml.client.registry.RenderingRegistry;
import com.rwtema.extrautils.command.CommandUUID;
import com.rwtema.extrautils.command.CommandHologram;
import com.rwtema.extrautils.block.render.RenderBlockConnectedTextures;
import com.rwtema.extrautils.multipart.FakeRenderBlocksMultiPart;
import cpw.mods.fml.common.Loader;
import com.rwtema.extrautils.particle.ParticleHelperClient;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import com.rwtema.extrautils.texture.LiquidColorRegistry;
import net.minecraft.client.resources.IReloadableResourceManager;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import com.rwtema.extrautils.command.CommandDumpTextureSheet;
import com.rwtema.extrautils.command.CommandDumpNEIInfo2;
import net.minecraft.command.ICommand;
import com.rwtema.extrautils.command.CommandDumpNEIInfo;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraft.client.Minecraft;
import com.rwtema.extrautils.helper.XUHelper;
import com.rwtema.extrautils.block.render.RenderBlockSpike;
import com.rwtema.extrautils.item.RenderItemBlockDrum;
import com.rwtema.extrautils.item.RenderItemMultiTransparency;
import com.rwtema.extrautils.item.RenderItemSpikeSword;
import com.rwtema.extrautils.item.RenderItemUnstable;
import com.rwtema.extrautils.item.RenderItemBlockColor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ExtraUtilsClient extends ExtraUtilsProxy
{
    public static final RenderItemBlockColor renderItemBlockColor;
    public static final RenderItemUnstable renderItemUnstable;
    public static final RenderItemSpikeSword renderItemSpikeSword;
    public static final RenderItemMultiTransparency renderItemMultiTransparency;
    public static final RenderItemBlockDrum renderItemDrum;
    public static final RenderBlockSpike renderBlockSpike;
    
    public ExtraUtilsClient() {
        ExtraUtilsClient.INSTANCE = this;
    }
    
    @Override
    public void registerClientCommands() {
        if (XUHelper.deObf || XUHelper.isTema(Minecraft.getMinecraft().getSession().func_148256_e())) {
            ClientCommandHandler.instance.registerCommand((ICommand)new CommandDumpNEIInfo());
            ClientCommandHandler.instance.registerCommand((ICommand)new CommandDumpNEIInfo2());
            ClientCommandHandler.instance.registerCommand((ICommand)new CommandDumpTextureSheet());
        }
        ClientCommandHandler.instance.registerCommand((ICommand)CommandTPSTimer.INSTANCE);
    }
    
    @Override
    public EntityPlayer getPlayerFromNetHandler(final INetHandler handler) {
        final EntityPlayer player = super.getPlayerFromNetHandler(handler);
        if (player == null) {
            return this.getClientPlayer();
        }
        return player;
    }
    
    @Override
    public void postInit() {
    }
    
    @Override
    public void registerEventHandler() {
        MinecraftForge.EVENT_BUS.register((Object)new EventHandlerClient());
        FMLCommonHandler.instance().bus().register((Object)new EventHandlerClient());
        ((IReloadableResourceManager)Minecraft.getMinecraft().getResourceManager()).registerReloadListener((IResourceManagerReloadListener)new LiquidColorRegistry());
        ((IReloadableResourceManager)Minecraft.getMinecraft().getResourceManager()).registerReloadListener((IResourceManagerReloadListener)new ParticleHelperClient());
        ExtraUtils.handlesClientMethods = true;
        if (Loader.isModLoaded("ForgeMultipart")) {
            RenderBlockConnectedTextures.fakeRender = new FakeRenderBlocksMultiPart();
        }
        ClientCommandHandler.instance.registerCommand((ICommand)new CommandHologram());
        ClientCommandHandler.instance.registerCommand((ICommand)new CommandUUID());
        KeyHandler.INSTANCE.register();
        super.registerEventHandler();
    }
    
    @Override
    public void throwLoadingError(final String cause, final String... message) {
        throw new CustomErrorWGui(cause, message);
    }
    
    @Override
    public boolean isClientSideAvailable() {
        return true;
    }
    
    @Override
    public void newServerStart() {
        super.newServerStart();
    }
    
    @Override
    public void registerRenderInformation() {
        ExtraUtilsClient.colorBlockID = RenderingRegistry.getNextAvailableRenderId();
        ExtraUtilsClient.fullBrightBlockID = RenderingRegistry.getNextAvailableRenderId();
        ExtraUtilsClient.multiBlockID = RenderingRegistry.getNextAvailableRenderId();
        ExtraUtilsClient.spikeBlockID = RenderingRegistry.getNextAvailableRenderId();
        ExtraUtilsClient.drumRendererID = RenderingRegistry.getNextAvailableRenderId();
        ExtraUtilsClient.connectedTextureID = RenderingRegistry.getNextAvailableRenderId();
        ExtraUtilsClient.connectedTextureEtheralID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler((ISimpleBlockRenderingHandler)new RenderBlockColor());
        RenderingRegistry.registerBlockHandler((ISimpleBlockRenderingHandler)new RenderBlockFullBright());
        RenderingRegistry.registerBlockHandler((ISimpleBlockRenderingHandler)new RenderBlockMultiBlock());
        RenderingRegistry.registerBlockHandler((ISimpleBlockRenderingHandler)ExtraUtilsClient.renderBlockSpike);
        for (final Item item : ItemBlockSpike.itemHashSet) {
            MinecraftForgeClient.registerItemRenderer(item, (IItemRenderer)ExtraUtilsClient.renderItemSpikeSword);
        }
        RenderingRegistry.registerBlockHandler((ISimpleBlockRenderingHandler)new RenderBlockDrum());
        RenderingRegistry.registerBlockHandler((ISimpleBlockRenderingHandler)new RenderBlockConnectedTextures());
        RenderingRegistry.registerBlockHandler((ISimpleBlockRenderingHandler)new RenderBlockConnectedTexturesEthereal());
        if (ExtraUtils.spikeGoldEnabled) {
            ClientRegistry.bindTileEntitySpecialRenderer((Class)TileEntityEnchantedSpike.class, (TileEntitySpecialRenderer)new RenderTileEntitySpike());
        }
        if (ExtraUtils.colorBlockDataEnabled) {
            for (final BlockColor b : ExtraUtils.colorblocks) {
                MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock((Block)b), (IItemRenderer)ExtraUtilsClient.renderItemBlockColor);
            }
        }
        if (ExtraUtils.unstableIngot != null) {
            MinecraftForgeClient.registerItemRenderer(ExtraUtils.unstableIngot, (IItemRenderer)ExtraUtilsClient.renderItemUnstable);
        }
        if (ExtraUtils.erosionShovel != null) {
            MinecraftForgeClient.registerItemRenderer(ExtraUtils.erosionShovel, (IItemRenderer)ExtraUtilsClient.renderItemMultiTransparency);
        }
        if (ExtraUtils.destructionPickaxe != null) {
            MinecraftForgeClient.registerItemRenderer(ExtraUtils.destructionPickaxe, (IItemRenderer)ExtraUtilsClient.renderItemMultiTransparency);
        }
        if (ExtraUtils.buildersWand != null) {
            MinecraftForgeClient.registerItemRenderer(ExtraUtils.buildersWand, (IItemRenderer)ExtraUtilsClient.renderItemMultiTransparency);
        }
        if (ExtraUtils.ethericSword != null) {
            MinecraftForgeClient.registerItemRenderer(ExtraUtils.ethericSword, (IItemRenderer)ExtraUtilsClient.renderItemMultiTransparency);
        }
        if (ExtraUtils.healingAxe != null) {
            MinecraftForgeClient.registerItemRenderer(ExtraUtils.healingAxe, (IItemRenderer)ExtraUtilsClient.renderItemMultiTransparency);
        }
        if (ExtraUtils.creativeBuildersWand != null) {
            MinecraftForgeClient.registerItemRenderer(ExtraUtils.creativeBuildersWand, (IItemRenderer)ExtraUtilsClient.renderItemMultiTransparency);
        }
        if (ExtraUtils.precisionShears != null) {
            MinecraftForgeClient.registerItemRenderer((Item)ExtraUtils.precisionShears, (IItemRenderer)ExtraUtilsClient.renderItemMultiTransparency);
        }
        if (ExtraUtils.temporalHoe != null) {
            MinecraftForgeClient.registerItemRenderer(ExtraUtils.temporalHoe, (IItemRenderer)ExtraUtilsClient.renderItemMultiTransparency);
        }
        if (ExtraUtils.drum != null) {
            MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ExtraUtils.drum), (IItemRenderer)ExtraUtilsClient.renderItemDrum);
        }
        if (ExtraUtils.microBlocks != null) {
            MinecraftForgeClient.registerItemRenderer(ExtraUtils.microBlocks, (IItemRenderer)new RenderItemMicroblock());
        }
        if (ExtraUtils.lawSwordEnabled) {
            MinecraftForgeClient.registerItemRenderer(ExtraUtils.lawSword, (IItemRenderer)new RenderItemLawSword());
        }
        if (ExtraUtils.glove != null) {
            MinecraftForgeClient.registerItemRenderer(ExtraUtils.glove, (IItemRenderer)RenderItemGlove.INSTANCE);
        }
    }
    
    @Override
    public EntityPlayer getClientPlayer() {
        return (EntityPlayer)Minecraft.getMinecraft().thePlayer;
    }
    
    @Override
    public World getClientWorld() {
        return (World)Minecraft.getMinecraft().theWorld;
    }
    
    @Override
    public PacketHandler getNewPacketHandler() {
        return new PacketHandlerClient();
    }
    
    @Override
    public void exectuteClientCode(final IClientCode clientCode) {
        clientCode.exectuteClientCode();
    }
    
    @Override
    public void sendUsePacket(final PlayerInteractEvent event) {
        if (event.world.isRemote) {
            final Vec3 hitVec = Minecraft.getMinecraft().objectMouseOver.hitVec;
            final float f = (float)hitVec.xCoord - event.x;
            final float f2 = (float)hitVec.yCoord - event.y;
            final float f3 = (float)hitVec.zCoord - event.z;
            Minecraft.getMinecraft().getNetHandler().addToSendQueue((Packet)new C08PacketPlayerBlockPlacement(event.x, event.y, event.z, event.face, event.entityPlayer.inventory.getCurrentItem(), f, f2, f3));
        }
    }
    
    @Override
    public void sendUsePacket(final int x, final int y, final int z, final int face, final ItemStack item, final float hitX, final float hitY, final float hitZ) {
        if (this.isAltSneaking()) {
            this.sendAltUsePacket(x, y, z, face, item, hitX, hitY, hitZ);
        }
        else {
            Minecraft.getMinecraft().getNetHandler().addToSendQueue((Packet)new C08PacketPlayerBlockPlacement(x, y, z, face, item, hitX, hitY, hitZ));
        }
    }
    
    @Override
    public void sendAltUsePacket(final int x, final int y, final int z, final int face, final ItemStack item, final float hitX, final float hitY, final float hitZ) {
        NetworkHandler.sendPacketToServer(new PacketUseItemAlt(x, y, z, face, item, hitX, hitY, hitZ));
    }
    
    @Override
    public void sendAltUsePacket(final ItemStack item) {
        this.sendAltUsePacket(-1, -1, -1, 255, item, 0.0f, 0.0f, 0.0f);
    }
    
    @Override
    public boolean isAltSneaking() {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            return KeyHandler.getIsKeyPressed(Minecraft.getMinecraft().gameSettings.keyBindSprint);
        }
        return super.isAltSneaking();
    }
    
    @Override
    public <F, T> T apply(final ISidedFunction<F, T> func, final F input) {
        return func.applyClient(input);
    }
    
    static {
        renderItemBlockColor = new RenderItemBlockColor();
        renderItemUnstable = new RenderItemUnstable();
        renderItemSpikeSword = new RenderItemSpikeSword();
        renderItemMultiTransparency = new RenderItemMultiTransparency();
        renderItemDrum = new RenderItemBlockDrum();
        renderBlockSpike = new RenderBlockSpike();
    }
}
