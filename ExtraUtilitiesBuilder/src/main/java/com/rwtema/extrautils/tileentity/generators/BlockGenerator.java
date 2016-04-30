// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.tileentity.generators;

import com.rwtema.extrautils.helper.XURandom;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.stats.StatList;
import net.minecraft.nbt.NBTTagCompound;
import java.util.Iterator;
import java.util.ArrayList;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.EntityLivingBase;
import com.rwtema.extrautils.block.BoxModel;
import com.rwtema.extrautils.texture.TextureMultiIcon;
import net.minecraft.client.renderer.texture.IIconRegister;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.FluidStack;
import com.rwtema.extrautils.ExtraUtilsMod;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.block.material.Material;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.util.IIcon;
import java.util.Random;
import com.rwtema.extrautils.block.IBlockTooltip;
import com.rwtema.extrautils.block.IMultiBoxBlock;
import com.rwtema.extrautils.block.BlockMultiBlock;

public class BlockGenerator extends BlockMultiBlock implements IMultiBoxBlock, IBlockTooltip
{
    private static final int[] rotInd;
    public static int num_gens;
    public static Class<? extends TileEntityGenerator>[] tiles;
    public static String[] textures;
    public static String[] names;
    public static Random random;
    public final int numGenerators;
    private IIcon[][] icons;
    
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(final World world, final int x, final int y, final int z, final Random rand) {
        final TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileEntityGenerator) {
            ((TileEntityGenerator)tile).doRandomDisplayTickR(rand);
        }
        super.randomDisplayTick(world, x, y, z, rand);
    }
    
    public BlockGenerator() {
        this(1);
    }
    
    public BlockGenerator(final int numGenerators) {
        super(Material.rock);
        this.icons = new IIcon[16][8];
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        this.setBlockName("extrautils:generator" + ((numGenerators > 1) ? ("." + numGenerators) : ""));
        this.setHardness(5.0f);
        this.setStepSound(Block.soundTypeMetal);
        this.numGenerators = numGenerators;
    }
    
    public static void addUpgradeRecipes(final Block a, final Block b) {
        for (int i = 0; i < BlockGenerator.num_gens; ++i) {
            GameRegistry.addRecipe(new ItemStack(b, 1, i), new Object[] { "SSS", "SES", "SSS", 'S', new ItemStack(a, 1, i), 'E', ExtraUtils.transferNodeEnabled ? new ItemStack(ExtraUtils.transferNode, 1, 12) : Blocks.redstone_block });
        }
    }
    
    public static void addSuperUpgradeRecipes(final Block a, final Block b) {
        for (int i = 0; i < BlockGenerator.num_gens; ++i) {
            GameRegistry.addRecipe(new ItemStack(b, 1, i), new Object[] { "SSS", "SES", "SSS", 'S', new ItemStack(a, 1, i), 'E', ExtraUtils.transferNodeEnabled ? new ItemStack(ExtraUtils.transferNode, 1, 13) : Blocks.redstone_block });
        }
    }
    
    public static void addRecipes() {
        GameRegistry.addRecipe(new ItemStack(ExtraUtils.generator, 1, 0), new Object[] { "iii", "ItI", "rfr", 'i', Blocks.cobblestone, 'I', Items.iron_ingot, 'f', Blocks.furnace, 'r', Items.redstone, 't', Blocks.piston });
        GameRegistry.addRecipe(new ItemStack(ExtraUtils.generator, 1, 5), new Object[] { "iii", "iIi", "rfr", 'i', Items.iron_ingot, 'I', new ItemStack(ExtraUtils.generator, 1, 0), 'f', Blocks.furnace, 'r', Items.redstone });
        GameRegistry.addRecipe(new ItemStack(ExtraUtils.generator, 1, 1), new Object[] { "iii", "iIi", "rfr", 'i', Items.iron_ingot, 'I', Blocks.iron_block, 'f', Blocks.furnace, 'r', Items.redstone });
        GameRegistry.addRecipe(new ItemStack(ExtraUtils.generator, 1, 2), new Object[] { "iii", "iIi", "rfr", 'i', Items.gold_ingot, 'I', Blocks.iron_block, 'f', Blocks.furnace, 'r', Items.redstone });
        GameRegistry.addRecipe(new ItemStack(ExtraUtils.generator, 1, 3), new Object[] { "eee", "iIi", "rfr", 'e', Items.ender_pearl, 'i', Items.ender_eye, 'I', Blocks.iron_block, 'f', Blocks.furnace, 'r', Items.redstone });
        GameRegistry.addRecipe(new ItemStack(ExtraUtils.generator, 1, 4), new Object[] { "iii", "iIi", "rfr", 'i', Blocks.redstone_block, 'I', new ItemStack(ExtraUtils.generator, 1, 2), 'f', Blocks.furnace, 'r', Items.redstone });
        GameRegistry.addRecipe(new ItemStack(ExtraUtils.generator, 1, 6), new Object[] { "iii", "iIi", "rfr", 'i', Blocks.obsidian, 'I', Blocks.enchanting_table, 'f', Blocks.furnace, 'r', Items.redstone });
        GameRegistry.addRecipe(new ItemStack(ExtraUtils.generator, 1, 7), new Object[] { "lql", "qIq", "rfr", 'q', Items.quartz, 'l', new ItemStack(Items.dye, 1, 4), 'I', Blocks.diamond_block, 'f', Blocks.furnace, 'r', Items.redstone });
        GameRegistry.addRecipe(new ItemStack(ExtraUtils.generator, 1, 8), new Object[] { "iii", "iIi", "rfr", 'i', Blocks.tnt, 'I', Blocks.iron_block, 'f', Blocks.furnace, 'r', Items.redstone });
        GameRegistry.addRecipe(new ItemStack(ExtraUtils.generator, 1, 9), new Object[] { "iii", "iIi", "rfr", 'i', new ItemStack(Blocks.wool, 1, 6), 'I', new ItemStack(ExtraUtils.generator, 1, 0), 'f', Blocks.furnace, 'r', Items.redstone });
        GameRegistry.addRecipe(new ItemStack(ExtraUtils.generator, 1, 10), new Object[] { "iii", "iIi", "rfr", 'i', Items.iron_ingot, 'I', new ItemStack(ExtraUtils.generator, 1, 1), 'f', Blocks.furnace, 'r', Items.redstone });
        GameRegistry.addRecipe(new ItemStack(ExtraUtils.generator, 1, 11), new Object[] { "wiw", "wIw", "rfr", 'w', new ItemStack(Items.skull, 1, 1), 'i', Items.nether_star, 'I', ExtraUtils.decorative1Enabled ? new ItemStack((Block)ExtraUtils.decorative1, 1, 5) : Blocks.iron_block, 'f', Blocks.furnace, 'r', Items.redstone });
    }
    
    public static void mapTiles() {
        for (int i = 0; i < BlockGenerator.num_gens; ++i) {
            ExtraUtils.registerTile(BlockGenerator.tiles[i], "extrautils:generator" + BlockGenerator.textures[i]);
        }
    }
    
    public static void addName(final int i, final String texture, final String name, final Class<? extends TileEntityGenerator> clazz) {
        BlockGenerator.textures[i] = texture;
        BlockGenerator.names[i] = name;
        BlockGenerator.tiles[i] = clazz;
        BlockGenerator.num_gens = Math.max(BlockGenerator.num_gens, i + 1);
    }
    
    public boolean onBlockActivated(final World worldObj, final int x, final int y, final int z, final EntityPlayer player, final int side, final float dx, final float dy, final float dz) {
        if (worldObj.isRemote) {
            return true;
        }
        final TileEntity tile = worldObj.getTileEntity(x, y, z);
        if (player.getCurrentEquippedItem() != null && tile instanceof IFluidHandler) {
            final ItemStack item = player.getCurrentEquippedItem();
            final FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(item);
            if (fluid != null && ((IFluidHandler)tile).fill(ForgeDirection.getOrientation(side), fluid, false) == fluid.amount) {
                ((IFluidHandler)tile).fill(ForgeDirection.getOrientation(side), fluid, true);
                if (!player.capabilities.isCreativeMode) {
                    player.setCurrentItemOrArmor(0, item.getItem().getContainerItem(item));
                }
                return true;
            }
        }
        player.openGui((Object)ExtraUtilsMod.instance, 0, worldObj, x, y, z);
        return true;
    }
    
    public int damageDropped(final int par1) {
        return par1;
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int par1, final int x) {
        return this.icons[x][par1];
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final IBlockAccess world, final int x, final int y, final int z, int side) {
        for (int rot = ((TileEntityGenerator)world.getTileEntity(x, y, z)).rotation, i = 0; i < rot; ++i) {
            side = BlockGenerator.rotInd[side];
        }
        return this.icons[world.getBlockMetadata(x, y, z)][side];
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(final Item par1, final CreativeTabs par2CreativeTabs, final List par3List) {
        for (int i = 0; i < BlockGenerator.num_gens; ++i) {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister par1IIconRegister) {
        final int w = (this.numGenerators == 64) ? 4 : 3;
        final String s = (this.numGenerators > 1) ? ((this.numGenerators > 8) ? "extrautils:supergenerators/sgenerator_" : "extrautils:supergenerators/generator_") : "extrautils:generator_";
        for (int i = 0; i < BlockGenerator.num_gens; ++i) {
            final String texture = s + BlockGenerator.textures[i];
            for (int a = 0; a < w; ++a) {
                for (int b = 0; b < 2; ++b) {
                    this.icons[i][a * 2 + b] = TextureMultiIcon.registerGridIcon(par1IIconRegister, texture, a, b, w, 2);
                }
            }
        }
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    public boolean hasTileEntity(final int metadata) {
        return BlockGenerator.tiles[metadata] != null;
    }
    
    public TileEntity createTileEntity(final World world, final int metadata) {
        try {
            return (TileEntity)BlockGenerator.tiles[metadata].newInstance();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void prepareForRender(final String label) {
    }
    
    public BoxModel genericModel(final int meta) {
        if (this.numGenerators > 8) {
            return this.superAdvModel(meta);
        }
        if (this.numGenerators > 1) {
            return this.advModel(meta);
        }
        return this.standardModel(meta);
    }
    
    public BoxModel superAdvModel(final int meta) {
        final BoxModel model = new BoxModel();
        model.addBoxI(0, 0, 0, 16, 5, 16);
        model.addBoxI(0, 5, 0, 3, 7, 3);
        model.addBoxI(0, 5, 13, 3, 7, 16);
        model.addBoxI(13, 5, 0, 16, 7, 3);
        model.addBoxI(13, 5, 13, 16, 7, 16);
        model.addBoxI(4, 7, 4, 12, 15, 12).setTextureSides(0, this.icons[meta][6], 1, this.icons[meta][7]);
        return model;
    }
    
    public BoxModel advModel(final int meta) {
        final BoxModel model = new BoxModel();
        model.addBoxI(0, 0, 0, 16, 4, 16);
        model.addBoxI(0, 12, 0, 16, 16, 16);
        model.addBoxI(3, 3, 3, 13, 13, 13);
        return model;
    }
    
    public BoxModel standardModel(final int meta) {
        final BoxModel model = new BoxModel();
        model.addBoxI(2, 14, 3, 14, 15, 13);
        model.addBoxI(1, 9, 2, 15, 14, 14);
        model.addBoxI(1, 8, 2, 15, 9, 6).setTextureSides(3, this.icons[meta][2]);
        model.addBoxI(1, 8, 10, 15, 9, 14);
        model.addBoxI(1, 7, 2, 15, 8, 5);
        model.addBoxI(1, 4, 2, 15, 7, 4);
        model.addBoxI(2, 8, 6, 14, 9, 10);
        model.addBoxI(2, 7, 5, 14, 8, 11).setTextureSides(1, this.icons[meta][0]);
        model.addBoxI(2, 3, 4, 14, 7, 12).setTextureSides(1, this.icons[meta][0]);
        model.addBoxI(2, 2, 5, 14, 3, 11);
        model.addBoxI(2, 1, 6, 14, 2, 10);
        model.addBoxI(0, 1, 1, 1, 15, 2);
        model.addBoxI(0, 1, 14, 1, 15, 15);
        model.addBoxI(15, 1, 1, 16, 15, 2);
        model.addBoxI(15, 1, 14, 16, 15, 15);
        model.addBoxI(1, 0, 1, 15, 1, 2);
        model.addBoxI(1, 0, 14, 15, 1, 15);
        model.addBoxI(1, 1, 1, 2, 2, 2);
        model.addBoxI(14, 1, 1, 15, 2, 2);
        model.addBoxI(1, 1, 14, 2, 2, 15);
        model.addBoxI(14, 1, 14, 15, 2, 15);
        model.addBoxI(0, 15, 2, 1, 16, 14);
        model.addBoxI(15, 15, 2, 16, 16, 14);
        model.addBoxI(0, 14, 2, 1, 15, 3);
        model.addBoxI(0, 14, 13, 1, 15, 14);
        model.addBoxI(15, 14, 2, 16, 15, 3);
        model.addBoxI(15, 14, 13, 16, 15, 14);
        return model;
    }
    
    @Override
    public BoxModel getWorldModel(final IBlockAccess world, final int x, final int y, final int z) {
        return this.genericModel(world.getBlockMetadata(x, y, z)).rotateY((world.getTileEntity(x, y, z) instanceof TileEntityGenerator) ? ((TileEntityGenerator)world.getTileEntity(x, y, z)).rotation : 0);
    }
    
    @Override
    public BoxModel getInventoryModel(final int metadata) {
        return this.genericModel(metadata).rotateY(1);
    }
    
    public void onBlockPlacedBy(final World world, final int x, final int y, final int z, final EntityLivingBase par5EntityLivingBase, final ItemStack item) {
        int l = MathHelper.floor_double(par5EntityLivingBase.rotationYaw * 4.0f / 360.0f + 0.5) % 4;
        l = (l + 6) % 4;
        ((TileEntityGenerator)world.getTileEntity(x, y, z)).rotation = (byte)l;
        if (item.hasTagCompound()) {
            ((TileEntityGenerator)world.getTileEntity(x, y, z)).readInvFromTags(item.getTagCompound());
        }
    }
    
    public boolean removedByPlayer(final World world, final EntityPlayer player, final int x, final int y, final int z, final boolean willHarvest) {
        final ArrayList<ItemStack> items = this.getDrops(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
        if (world.setBlockToAir(x, y, z)) {
            if (!world.isRemote) {
                for (final ItemStack item : items) {
                    if (player == null || !player.capabilities.isCreativeMode || item.hasTagCompound()) {
                        this.dropBlockAsItem(world, x, y, z, item);
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    public ArrayList<ItemStack> getDrops(final World world, final int x, final int y, final int z, final int metadata, final int fortune) {
        final ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        final ItemStack item = new ItemStack((Block)this, 1, this.damageDropped(metadata));
        if (world.getTileEntity(x, y, z) instanceof TileEntityGenerator) {
            final NBTTagCompound tags = new NBTTagCompound();
            ((TileEntityGenerator)world.getTileEntity(x, y, z)).writeInvToTags(tags);
            if (!tags.hasNoTags()) {
                item.setTagCompound(tags);
            }
        }
        ret.add(item);
        return ret;
    }
    
    public void harvestBlock(final World par1World, final EntityPlayer par2EntityPlayer, final int par3, final int par4, final int par5, final int par6) {
        par2EntityPlayer.addStat(StatList.mineBlockStatArray[getIdFromBlock((Block)this)], 1);
        par2EntityPlayer.addExhaustion(0.025f);
    }
    
    public void breakBlock(final World world, final int x, final int y, final int z, final Block par5, final int par6) {
        if (world.getTileEntity(x, y, z) instanceof TileEntityGenerator && world.getTileEntity(x, y, z) instanceof IInventory) {
            final IInventory tile = (IInventory)world.getTileEntity(x, y, z);
            for (int i = 0; i < tile.getSizeInventory(); ++i) {
                final ItemStack itemstack = tile.getStackInSlot(i);
                if (itemstack != null) {
                    final float f = BlockGenerator.random.nextFloat() * 0.8f + 0.1f;
                    final float f2 = BlockGenerator.random.nextFloat() * 0.8f + 0.1f;
                    final float f3 = BlockGenerator.random.nextFloat() * 0.8f + 0.1f;
                    while (itemstack.stackSize > 0) {
                        int k1 = BlockGenerator.random.nextInt(21) + 10;
                        if (k1 > itemstack.stackSize) {
                            k1 = itemstack.stackSize;
                        }
                        final ItemStack itemStack = itemstack;
                        itemStack.stackSize -= k1;
                        final EntityItem entityitem = new EntityItem(world, (double)(x + f), (double)(y + f2), (double)(z + f3), new ItemStack(itemstack.getItem(), k1, itemstack.getItemDamage()));
                        final float f4 = 0.05f;
                        entityitem.motionX = (float)BlockGenerator.random.nextGaussian() * f4;
                        entityitem.motionY = (float)BlockGenerator.random.nextGaussian() * f4 + 0.2f;
                        entityitem.motionZ = (float)BlockGenerator.random.nextGaussian() * f4;
                        if (itemstack.hasTagCompound()) {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                        }
                        world.spawnEntityInWorld((Entity)entityitem);
                    }
                }
            }
        }
        super.breakBlock(world, x, y, z, par5, par6);
    }
    
    @Override
    public void addInformation(final ItemStack par1ItemStack, final EntityPlayer par2EntityPlayer, final List par3List, final boolean par4) {
        par3List.add("Power Multiplier: x" + this.numGenerators);
        if (par1ItemStack.hasTagCompound() && par1ItemStack.getTagCompound().hasKey("Energy")) {
            par3List.add(par1ItemStack.getTagCompound().getInteger("Energy") + " / " + 100000 * this.numGenerators + " RF");
        }
    }
    
    public boolean hasComparatorInputOverride() {
        return true;
    }
    
    public int getComparatorInputOverride(final World world, final int x, final int y, final int z, final int par5) {
        final TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileEntityGenerator) {
            return ((TileEntityGenerator)tile).getCompLevel();
        }
        return 0;
    }
    
    static {
        rotInd = new int[] { 0, 1, 4, 5, 3, 2 };
        BlockGenerator.num_gens = 0;
        BlockGenerator.tiles = (Class<? extends TileEntityGenerator>[])new Class[16];
        BlockGenerator.textures = new String[16];
        BlockGenerator.names = new String[16];
        addName(0, "stone", "Survivalist", TileEntityGeneratorFurnaceSurvival.class);
        addName(1, "base", "Furnace", TileEntityGeneratorFurnace.class);
        addName(2, "lava", "Lava", TileEntityGeneratorMagma.class);
        addName(3, "ender", "Ender", TileEntityGeneratorEnder.class);
        addName(4, "redflux", "Heated Redstone", TileEntityGeneratorRedFlux.class);
        addName(5, "food", "Culinary", TileEntityGeneratorFood.class);
        addName(6, "potion", "Potions", TileEntityGeneratorPotion.class);
        addName(7, "solar", "Solar", TileEntityGeneratorSolar.class);
        addName(8, "tnt", "TNT", TileEntityGeneratorTNT.class);
        addName(9, "pink", "Pink", TileEntityGeneratorPink.class);
        addName(10, "overclocked", "High-temperature Furnace", TileEntityGeneratorFurnaceOverClocked.class);
        addName(11, "nether", "Nether Star", TileEntityGeneratorNether.class);
        BlockGenerator.random = XURandom.getInstance();
    }
}


