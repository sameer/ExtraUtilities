// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.modintegration;

import net.minecraftforge.common.MinecraftForge;
import java.util.ListIterator;
import tconstruct.library.crafting.LiquidCasting;
import tconstruct.library.crafting.ToolBuilder;
import tconstruct.tools.TinkerTools;
import tconstruct.weaponry.TinkerWeaponry;
import tconstruct.util.config.PHConstruct;
import tconstruct.library.modifier.ItemModifier;
import tconstruct.library.crafting.ModifyBuilder;
import tconstruct.modifiers.tools.ModExtraModifier;
import java.util.Iterator;
import java.util.List;
import tconstruct.library.tools.DynamicToolPart;
import tconstruct.library.util.IPattern;
import tconstruct.library.crafting.CastingRecipe;
import java.util.LinkedList;
import tconstruct.library.client.TConstructClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import tconstruct.library.tools.ToolMaterial;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import com.rwtema.extrautils.IClientCode;
import com.rwtema.extrautils.ExtraUtilsMod;
import net.minecraft.nbt.NBTBase;
import tconstruct.library.TConstructRegistry;
import tconstruct.smeltery.TinkerSmeltery;
import net.minecraft.item.Item;
import tconstruct.library.crafting.Smeltery;
import net.minecraftforge.fluids.FluidStack;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;
import tconstruct.library.crafting.FluidType;
import net.minecraftforge.fluids.FluidRegistry;
import cpw.mods.fml.common.event.FMLInterModComms;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.nbt.NBTTagCompound;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraftforge.fluids.Fluid;
import com.rwtema.extrautils.ILoading;

public class TConIntegration implements ILoading
{
    public static final TConIntegration instance;
    public static Fluid unstable;
    public static Fluid bedrock;
    
    public void addBedrockiumMaterial() {
        if (ExtraUtils.bedrockiumBlock == null || ExtraUtils.bedrockium == null) {
            ExtraUtils.tcon_bedrock_material_id = -1;
            return;
        }
        final int id = ExtraUtils.tcon_bedrock_material_id;
        if (id <= 0) {
            return;
        }
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("Id", id);
        final String name = "Bedrockium";
        tag.setString("Name", "Bedrockium");
        tag.setString("localizationString", "material.extrautils.bedrockium");
        tag.setInteger("Durability", 7500);
        tag.setInteger("MiningSpeed", 800);
        tag.setInteger("HarvestLevel", 7);
        tag.setInteger("Attack", 4);
        tag.setFloat("HandleModifier", 1.75f);
        tag.setInteger("Reinforced", 0);
        tag.setFloat("Bow_ProjectileSpeed", 3.0f);
        tag.setInteger("Bow_DrawSpeed", 200);
        tag.setFloat("Projectile_Mass", 40.0f);
        tag.setFloat("Projectile_Fragility", 0.4f);
        tag.setString("Style", EnumChatFormatting.BLACK.toString());
        tag.setInteger("Color", 16777215);
        FMLInterModComms.sendMessage("TConstruct", "addMaterial", tag);
        FluidRegistry.registerFluid(TConIntegration.bedrock);
        FluidType.registerFluidType(TConIntegration.bedrock.getName(), (Block)ExtraUtils.bedrockiumBlock, 0, 850, TConIntegration.bedrock, true);
        Smeltery.addMelting(new ItemStack((Block)ExtraUtils.bedrockiumBlock, 1), (Block)ExtraUtils.bedrockiumBlock, 0, 850, new FluidStack(TConIntegration.bedrock, 1296));
        Smeltery.addMelting(new ItemStack((Item)ExtraUtils.bedrockium, 1, 0), (Block)ExtraUtils.bedrockiumBlock, 0, 850, new FluidStack(TConIntegration.bedrock, 144));
        final ItemStack ingotcast = new ItemStack(TinkerSmeltery.metalPattern, 1, 0);
        TConstructRegistry.getBasinCasting().addCastingRecipe(new ItemStack((Block)ExtraUtils.bedrockiumBlock, 1), new FluidStack(TConIntegration.bedrock, 1296), (ItemStack)null, true, 100);
        TConstructRegistry.getTableCasting().addCastingRecipe(new ItemStack((Item)ExtraUtils.bedrockium, 1), new FluidStack(TConIntegration.bedrock, 144), ingotcast, false, 50);
        tag = new NBTTagCompound();
        tag.setString("FluidName", TConIntegration.bedrock.getName());
        tag.setInteger("MaterialId", id);
        FMLInterModComms.sendMessage("TConstruct", "addPartCastingMaterial", tag);
        tag = new NBTTagCompound();
        tag.setInteger("MaterialId", id);
        tag.setTag("Item", (NBTBase)new ItemStack((Item)ExtraUtils.bedrockium, 1, 0).writeToNBT(new NBTTagCompound()));
        tag.setInteger("Value", 2);
        FMLInterModComms.sendMessage("TConstruct", "addMaterialItem", tag);
        ExtraUtilsMod.proxy.exectuteClientCode(new IClientCode() {
            @SideOnly(Side.CLIENT)
            @Override
            public void exectuteClientCode() {
                new TConTextureResourcePackBedrockium("Bedrockium").register();
            }
        });
    }
    
    public void addMagicWoodMaterial() {
        if (ExtraUtils.decorative1 == null) {
            ExtraUtils.tcon_magical_wood_id = -1;
            return;
        }
        final int id = ExtraUtils.tcon_magical_wood_id;
        if (id <= 0) {
            return;
        }
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("Id", id);
        final String name = "MagicWood";
        tag.setString("Name", "MagicWood");
        tag.setString("localizationString", "material.extrautils.magicwood");
        tag.setInteger("Durability", 97);
        tag.setInteger("MiningSpeed", 150);
        tag.setInteger("HarvestLevel", 1);
        tag.setInteger("Attack", 0);
        tag.setFloat("HandleModifier", 1.0f);
        tag.setInteger("Reinforced", 0);
        tag.setFloat("Bow_ProjectileSpeed", 3.0f);
        tag.setInteger("Bow_DrawSpeed", 18);
        tag.setFloat("Projectile_Mass", 0.69f);
        tag.setFloat("Projectile_Fragility", 0.5f);
        tag.setString("Style", EnumChatFormatting.YELLOW.toString());
        tag.setInteger("Color", 7690273);
        FMLInterModComms.sendMessage("TConstruct", "addMaterial", tag);
        final ItemStack itemstack = new ItemStack((Block)ExtraUtils.decorative1, 1, 8);
        tag = new NBTTagCompound();
        tag.setInteger("MaterialId", id);
        NBTTagCompound item = new NBTTagCompound();
        itemstack.writeToNBT(item);
        tag.setTag("Item", (NBTBase)item);
        tag.setInteger("Value", 2);
        FMLInterModComms.sendMessage("TConstruct", "addPartBuilderMaterial", tag);
        tag = new NBTTagCompound();
        tag.setInteger("MaterialId", id);
        tag.setInteger("Value", 2);
        item = new NBTTagCompound();
        itemstack.writeToNBT(item);
        tag.setTag("Item", (NBTBase)item);
        FMLInterModComms.sendMessage("TConstruct", "addMaterialItem", tag);
        ExtraUtilsMod.proxy.exectuteClientCode(new IClientCode() {
            @SideOnly(Side.CLIENT)
            @Override
            public void exectuteClientCode() {
                new TConTextureResourcePackMagicWood("MagicWood").register();
            }
        });
    }
    
    public void addUnstableMaterial() {
        if (ExtraUtils.unstableIngot == null || ExtraUtils.decorative1 == null) {
            ExtraUtils.tcon_unstable_material_id = -1;
            return;
        }
        final int id = ExtraUtils.tcon_unstable_material_id;
        if (id <= 0) {
            return;
        }
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("Id", id);
        final String name = "unstableIngot";
        final ToolMaterial mat = new ToolMaterial("unstableIngot", "material.extrautils.unstableIngot", 4, 100, 700, 2, 0.6f, 4, 0.0f, EnumChatFormatting.WHITE.toString(), 16777215);
        TConstructRegistry.addtoolMaterial(id, mat);
        TConstructRegistry.addDefaultToolPartMaterial(id);
        TConstructRegistry.addBowMaterial(id, 109, 1.0f);
        TConstructRegistry.addArrowMaterial(id, 2.4f, 0.0f);
        ExtraUtilsMod.proxy.exectuteClientCode(new IClientCode() {
            @SideOnly(Side.CLIENT)
            @Override
            public void exectuteClientCode() {
                if (FMLCommonHandler.instance().getSide().isClient()) {
                    TConstructClientRegistry.addMaterialRenderMapping(id, "tinker", mat.name().toLowerCase(), true);
                }
            }
        });
        FluidRegistry.registerFluid(TConIntegration.unstable);
        FluidType.registerFluidType(TConIntegration.unstable.getName(), (Block)ExtraUtils.decorative1, 5, 850, TConIntegration.unstable, true);
        Smeltery.addMelting(new ItemStack((Block)ExtraUtils.decorative1, 1, 5), (Block)ExtraUtils.decorative1, 5, 850, new FluidStack(TConIntegration.unstable, 648));
        Smeltery.addMelting(new ItemStack(ExtraUtils.unstableIngot, 1, 0), (Block)ExtraUtils.decorative1, 5, 850, new FluidStack(TConIntegration.unstable, 72));
        Smeltery.addMelting(new ItemStack(ExtraUtils.unstableIngot, 1, 1), (Block)ExtraUtils.decorative1, 5, 850, new FluidStack(TConIntegration.unstable, 8));
        Smeltery.addMelting(new ItemStack(ExtraUtils.unstableIngot, 1, 2), (Block)ExtraUtils.decorative1, 5, 850, new FluidStack(TConIntegration.unstable, 144));
        TConstructRegistry.getBasinCasting().addCastingRecipe(new ItemStack((Block)ExtraUtils.decorative1, 1, 5), new FluidStack(TConIntegration.unstable, 1296), (ItemStack)null, true, 100);
        final List<CastingRecipe> newRecipies = new LinkedList<CastingRecipe>();
        for (final CastingRecipe recipe : TConstructRegistry.getTableCasting().getCastingRecipes()) {
            if (recipe.castingMetal.getFluid() == TinkerSmeltery.moltenIronFluid && recipe.cast != null && recipe.cast.getItem() instanceof IPattern) {
                if (!(recipe.getResult().getItem() instanceof DynamicToolPart)) {
                    continue;
                }
                newRecipies.add(recipe);
            }
        }
        final FluidType ft = FluidType.getFluidType(TConIntegration.unstable);
        for (final CastingRecipe recipe2 : newRecipies) {
            final ItemStack output = recipe2.getResult().copy();
            output.setItemDamage(id);
            final FluidStack liquid2 = new FluidStack(TConIntegration.unstable, recipe2.castingMetal.amount);
            TConstructRegistry.getTableCasting().addCastingRecipe(output, liquid2, recipe2.cast, recipe2.consumeCast, recipe2.coolTime);
            Smeltery.addMelting(ft, output, 0, liquid2.amount / 2);
        }
        tag = new NBTTagCompound();
        tag.setInteger("MaterialId", id);
        tag.setTag("Item", (NBTBase)new ItemStack(ExtraUtils.unstableIngot, 1, 0).writeToNBT(new NBTTagCompound()));
        tag.setInteger("Value", 2);
        FMLInterModComms.sendMessage("TConstruct", "addMaterialItem", tag);
        tag = new NBTTagCompound();
        tag.setInteger("MaterialId", id);
        tag.setTag("Item", (NBTBase)new ItemStack(ExtraUtils.unstableIngot, 1, 2).writeToNBT(new NBTTagCompound()));
        tag.setInteger("Value", 2);
        FMLInterModComms.sendMessage("TConstruct", "addMaterialItem", tag);
        ExtraUtilsMod.proxy.exectuteClientCode(new IClientCode() {
            @SideOnly(Side.CLIENT)
            @Override
            public void exectuteClientCode() {
                new TConTextureResourcePackUnstableIngot("unstableIngot").register();
            }
        });
    }
    
    @Override
    public void init() {
        this.addBedrockiumMaterial();
        this.addUnstableMaterial();
        this.addMagicWoodMaterial();
        this.addModifiers();
    }
    
    public void addModifiers() {
        ModifyBuilder.registerModifier((ItemModifier)new ModExtraModifier(new ItemStack[] { new ItemStack((Item)ExtraUtils.soul, 1, 0) }, "XUSoul"));
    }
    
    @Override
    public void preInit() {
    }
    
    @Override
    public void postInit() {
        if (PHConstruct.alternativeBoltRecipe) {
            return;
        }
        final LiquidCasting tb = TConstructRegistry.getTableCasting();
        final ListIterator<CastingRecipe> iterator = tb.getCastingRecipes().listIterator();
        while (iterator.hasNext()) {
            final CastingRecipe castingRecipe = iterator.next();
            if (castingRecipe != null) {
                if (castingRecipe.getClass() != CastingRecipe.class) {
                    continue;
                }
                if (castingRecipe.output == null) {
                    continue;
                }
                if (castingRecipe.output.getItem() != TinkerWeaponry.partBolt) {
                    continue;
                }
                if (castingRecipe.cast == null) {
                    continue;
                }
                if (castingRecipe.cast.getItem() != TinkerTools.toolRod) {
                    continue;
                }
                final int materialID = ToolBuilder.instance.getMaterialID(castingRecipe.cast);
                if (materialID <= 0) {
                    continue;
                }
                if (materialID == ExtraUtils.tcon_unstable_material_id) {
                    iterator.set(new TConCastingRecipeUnsensitive(castingRecipe));
                }
                if (materialID != ExtraUtils.tcon_bedrock_material_id) {
                    continue;
                }
                iterator.set(new TConCastingRecipeUnsensitive(castingRecipe));
            }
        }
    }
    
    static {
        instance = new TConIntegration();
        MinecraftForge.EVENT_BUS.register((Object)new TConEvents());
        TConIntegration.unstable = new Fluid("molten.unstableIngots").setDensity(3000).setViscosity(6000).setTemperature(1300);
        TConIntegration.bedrock = new Fluid("molten.bedrockiumIngots").setDensity(3000).setViscosity(6000).setTemperature(1300);
    }
}
