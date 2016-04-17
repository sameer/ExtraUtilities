// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.modintegration;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import com.rwtema.extrautils.texture.TextureUnstableLava;
import net.minecraft.util.IIcon;
import com.rwtema.extrautils.texture.TextureBedrockLava;
import net.minecraftforge.client.event.TextureStitchEvent;
import tconstruct.tools.TinkerTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.nbt.NBTTagList;
import tconstruct.library.event.ToolCraftEvent;
import tconstruct.library.tools.ToolCore;
import tconstruct.library.event.ToolCraftedEvent;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.SharedMonsterAttributes;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import tconstruct.library.crafting.ToolBuilder;
import cpw.mods.fml.common.eventhandler.EventPriority;
import tconstruct.library.event.ToolBuildEvent;
import net.minecraft.world.WorldServer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.DimensionManager;
import tconstruct.library.util.IToolPart;
import com.rwtema.extrautils.ExtraUtils;
import tconstruct.library.event.SmelteryCastedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.common.gameevent.TickEvent;
import java.util.UUID;

public class TConEvents
{
    public final double SPEED_REDUCTION = -0.1;
    public static final String TAG_DEADLINE = "XUDeadline";
    public final String TAG_LOCALDEADLINE = "XULocalDeadline";
    public final String TAG_LOCALDIM = "XULocalDim";
    public static final int TICKSTILDESTRUCTION = 200;
    public static final UUID uuid;
    public final String TAG_PREFIX = "[TCon]";
    public static final String iconName = "extrautils:unstableFluid";
    public static final String iconName2 = "extrautils:bedrockFluid";
    int curDim;
    
    @SubscribeEvent
    public void getCurrentWorldTicking(final TickEvent.WorldTickEvent event) {
        if (event.side == Side.SERVER && event.phase == TickEvent.Phase.START) {
            this.curDim = event.world.provider.dimensionId;
        }
    }
    
    @SubscribeEvent
    public void addUnstableTimer(final SmelteryCastedEvent.CastingTable event) {
        if (ExtraUtils.tcon_unstable_material_id <= 0) {
            return;
        }
        final ItemStack output = event.output;
        if (output == null || !(output.getItem() instanceof IToolPart)) {
            return;
        }
        final IToolPart part = (IToolPart)output.getItem();
        if (part.getMaterialID(output) != ExtraUtils.tcon_unstable_material_id) {
            return;
        }
        final NBTTagCompound tag = getOrInitTag(output);
        final WorldServer world = DimensionManager.getWorld(0);
        if (world == null) {
            return;
        }
        tag.setLong("XUDeadline", world.getTotalWorldTime());
        final WorldServer localWorld = DimensionManager.getWorld(this.curDim);
        if (localWorld != null) {
            tag.setLong("XULocalDeadline", localWorld.getTotalWorldTime());
            tag.setInteger("XULocalDim", this.curDim);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void denyCraft(final ToolBuildEvent event) {
        if (ExtraUtils.tcon_unstable_material_id <= 0) {
            return;
        }
        final WorldServer world = DimensionManager.getWorld(0);
        if (world == null) {
            return;
        }
        if (isToolExpired(event.headStack, world) || isToolExpired(event.handleStack, world) || isToolExpired(event.accessoryStack, world) || isToolExpired(event.extraStack, world)) {
            event.headStack = null;
            event.handleStack = null;
            event.accessoryStack = null;
            event.extraStack = null;
        }
    }
    
    public ItemStack handleToolPart(final ItemStack stack, final WorldServer world) {
        return isToolExpired(stack, world) ? null : stack;
    }
    
    public static boolean isToolExpired(final ItemStack stack) {
        final WorldServer world = DimensionManager.getWorld(0);
        return world != null && isToolExpired(stack, world);
    }
    
    public static boolean isToolExpired(final ItemStack stack, final WorldServer world) {
        if (stack == null) {
            return false;
        }
        if (ToolBuilder.instance.getMaterialID(stack) == ExtraUtils.tcon_unstable_material_id && stack.hasTagCompound()) {
            final NBTTagCompound tag = stack.getTagCompound();
            if (tag.hasKey("XUDeadline", 4)) {
                final long deadline = tag.getLong("XUDeadline");
                if (world.getTotalWorldTime() - deadline > 200L) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void unstableTooltip(final ItemTooltipEvent event) {
        if (event.itemStack == null || event.entityPlayer == null || event.entityPlayer.worldObj == null) {
            return;
        }
        if (ExtraUtils.tcon_unstable_material_id <= 0) {
            return;
        }
        if (ToolBuilder.instance.getMaterialID(event.itemStack) != ExtraUtils.tcon_unstable_material_id) {
            return;
        }
        final NBTTagCompound tag = event.itemStack.getTagCompound();
        if (tag == null || !tag.hasKey("XULocalDeadline") || tag.getInteger("XULocalDim") != event.entityPlayer.worldObj.provider.dimensionId) {
            event.toolTip.add(EnumChatFormatting.RED + "Unstable parts will denature after " + 10 + " seconds" + EnumChatFormatting.RESET);
            return;
        }
        final long finalTime = tag.getLong("XULocalDeadline") + 200L;
        final long curTime = event.entityPlayer.worldObj.getTotalWorldTime();
        if (curTime <= finalTime) {
            EnumChatFormatting col = EnumChatFormatting.RED;
            if (curTime >= finalTime - 100L && Minecraft.getSystemTime() % 200L < 100L) {
                col = EnumChatFormatting.YELLOW;
            }
            event.toolTip.add(col + "Part will denature in " + String.format(Locale.ENGLISH, "%.1f", (finalTime - curTime) / 20.0f) + " seconds" + EnumChatFormatting.RESET);
            event.toolTip.add(col + "After that it will become useless" + EnumChatFormatting.RESET);
        }
        else {
            event.toolTip.add(EnumChatFormatting.RED + "Denatured" + EnumChatFormatting.RESET);
        }
    }
    
    @SubscribeEvent
    public void addBedrockiumPartSlowness(final SmelteryCastedEvent.CastingTable event) {
        if (ExtraUtils.tcon_bedrock_material_id <= 0) {
            return;
        }
        final ItemStack output = event.output;
        if (output == null || !(output.getItem() instanceof IToolPart)) {
            return;
        }
        final IToolPart part = (IToolPart)output.getItem();
        if (part.getMaterialID(output) != ExtraUtils.tcon_bedrock_material_id) {
            return;
        }
        final NBTTagCompound tag = getOrInitTag(output);
        this.assignAttribute(tag, SharedMonsterAttributes.movementSpeed, new AttributeModifier(TConEvents.uuid, "[TCon]Bedrockium Weight", -0.1, 2));
    }
    
    public static NBTTagCompound getOrInitTag(final ItemStack output) {
        NBTTagCompound tag = output.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
            output.setTagCompound(tag);
        }
        return tag;
    }
    
    @SubscribeEvent
    public void handleBedrockMod(final ToolCraftedEvent event) {
        if (!(event.tool.getItem() instanceof ToolCore)) {
            return;
        }
        final NBTTagCompound tag = event.tool.getTagCompound();
        if (tag == null) {
            return;
        }
        this.assignProperSlowness(tag);
    }
    
    @SubscribeEvent
    public void handleBedrockModification(final ToolCraftEvent.NormalTool event) {
        this.assignProperSlowness(event.toolTag);
    }
    
    public void assignProperSlowness(final NBTTagCompound tag) {
        this.removeTags(tag);
        if (ExtraUtils.tcon_bedrock_material_id <= 0) {
            return;
        }
        final int i = this.getNumMaterials(tag.getCompoundTag("InfiTool"), ExtraUtils.tcon_bedrock_material_id);
        if (i == 0) {
            return;
        }
        this.assignAttribute(tag, SharedMonsterAttributes.movementSpeed, new AttributeModifier(TConEvents.uuid, "[TCon]Bedrockium Weight", -0.1 * i, 2));
        this.assignAttribute(tag, SharedMonsterAttributes.knockbackResistance, new AttributeModifier(TConEvents.uuid, "[TCon]Bedrockium Weight", 0.5 * i, 2));
    }
    
    public void removeTags(final NBTTagCompound tag) {
        final NBTTagList nbttaglist = tag.getTagList("AttributeModifiers", 10);
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            final NBTTagCompound tagAt = nbttaglist.getCompoundTagAt(i);
            if (tagAt.getString("Name").startsWith("[TCon]")) {
                nbttaglist.removeTag(i--);
            }
        }
    }
    
    public void assignAttribute(final NBTTagCompound tag, final IAttribute attribute, final AttributeModifier modifier) {
        final NBTTagList nbttaglist = tag.getTagList("AttributeModifiers", 10);
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setString("AttributeName", attribute.getAttributeUnlocalizedName());
        nbttagcompound.setString("Name", modifier.getName());
        nbttagcompound.setDouble("Amount", modifier.getAmount());
        nbttagcompound.setInteger("Operation", modifier.getOperation());
        nbttagcompound.setLong("UUIDMost", modifier.getID().getMostSignificantBits());
        nbttagcompound.setLong("UUIDLeast", modifier.getID().getLeastSignificantBits());
        nbttaglist.appendTag((NBTBase)nbttagcompound);
        tag.setTag("AttributeModifiers", (NBTBase)nbttaglist);
    }
    
    @SubscribeEvent
    public void handleUnstableCrafting(final ToolCraftEvent.NormalTool event) {
        if (ExtraUtils.tcon_unstable_material_id <= 0) {
            return;
        }
        final NBTTagCompound toolTag = event.toolTag.getCompoundTag("InfiTool");
        if (!this.isUniformTool(toolTag, ExtraUtils.tcon_unstable_material_id)) {
            return;
        }
        toolTag.setInteger("Unbreaking", 10);
    }
    
    @SubscribeEvent
    public void handleMagicWood(final ToolCraftEvent.NormalTool event) {
        if (ExtraUtils.tcon_magical_wood_id <= 0) {
            return;
        }
        final NBTTagCompound toolTag = event.toolTag.getCompoundTag("InfiTool");
        int modifiers = toolTag.getInteger("Modifiers");
        if (!this.isUniformTool(toolTag, ExtraUtils.tcon_magical_wood_id)) {
            final int bonusModifiers = this.getNumMaterials(toolTag, ExtraUtils.tcon_magical_wood_id);
            modifiers += bonusModifiers;
            toolTag.setInteger("Modifiers", modifiers);
        }
        else {
            if (event.tool == TinkerTools.battlesign) {
                modifiers += 3;
            }
            toolTag.setInteger("Modifiers", modifiers + 8);
        }
    }
    
    public int getNumMaterials(final NBTTagCompound toolTag, final int materialID) {
        int bonusModifiers = 0;
        if (toolTag.getInteger("Head") == materialID) {
            ++bonusModifiers;
        }
        if (toolTag.getInteger("Handle") == materialID) {
            ++bonusModifiers;
        }
        if (toolTag.getInteger("Accessory") == materialID) {
            ++bonusModifiers;
        }
        if (toolTag.getInteger("Extra") == materialID) {
            ++bonusModifiers;
        }
        return bonusModifiers;
    }
    
    public boolean isUniformTool(final NBTTagCompound toolTag, final int materialId) {
        return toolTag.getInteger("Head") == materialId && toolTag.getInteger("Handle") == materialId && this.valid(toolTag.getInteger("Accessory"), materialId) && this.valid(toolTag.getInteger("Extra"), materialId);
    }
    
    public boolean valid(final int i, final int materialId) {
        return i == materialId || i == -1 || i == 0;
    }
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void handleStich(final TextureStitchEvent.Pre event) {
        if (event.map.getTextureType() != 0) {
            return;
        }
        TConIntegration.bedrock.setIcons(event.map.registerIcon("TConIntegration.bedrock"));
        TextureAtlasSprite sprite = new TextureBedrockLava("extrautils:bedrockFluid", "lava_still");
        event.map.setTextureEntry("extrautils:bedrockFluid", sprite);
        if (TConIntegration.bedrock != null) {
            TConIntegration.bedrock.setIcons((IIcon)sprite);
        }
        sprite = new TextureBedrockLava("extrautils:bedrockFluid_flowing", "lava_flow");
        if (event.map.setTextureEntry("extrautils:bedrockFluid_flowing", sprite) && TConIntegration.bedrock != null) {
            TConIntegration.bedrock.setFlowingIcon((IIcon)sprite);
        }
        sprite = new TextureUnstableLava("extrautils:unstableFluid", "water_still");
        event.map.setTextureEntry("extrautils:unstableFluid", sprite);
        if (TConIntegration.unstable != null) {
            TConIntegration.unstable.setIcons((IIcon)sprite);
        }
        sprite = new TextureUnstableLava("extrautils:unstableFluid_flowing", "water_flow");
        if (event.map.setTextureEntry("extrautils:unstableFluid_flowing", sprite) && TConIntegration.unstable != null) {
            TConIntegration.unstable.setFlowingIcon((IIcon)sprite);
        }
    }
    
    static {
        uuid = UUID.fromString("52ca0342-0a6b-11e5-a6c0-1697f925ec7b");
    }
}
