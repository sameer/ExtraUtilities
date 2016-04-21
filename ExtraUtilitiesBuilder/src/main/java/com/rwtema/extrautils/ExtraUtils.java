// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils;

import com.google.common.collect.Lists;
import java.util.HashSet;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import com.rwtema.extrautils.crafting.RecipeGBEnchanting;
import com.rwtema.extrautils.crafting.RecipeBagDye;
import com.rwtema.extrautils.crafting.RecipeDifficultySpecific;
import net.minecraft.enchantment.Enchantment;
import com.rwtema.extrautils.crafting.RecipeUnstableCrafting;
import net.minecraft.nbt.NBTTagCompound;
import com.rwtema.extrautils.crafting.RecipeUnstableNuggetCrafting;
import com.rwtema.extrautils.crafting.RecipeUnstableIngotCrafting;
import com.rwtema.extrautils.crafting.RecipeUnEnchanting;
import com.rwtema.extrautils.crafting.RecipeUnsigil;
import com.rwtema.extrautils.crafting.RecipeHorseTransmutation;
import com.rwtema.extrautils.crafting.RecipeFilterInvert;
import com.rwtema.extrautils.crafting.RecipeCustomOres;
import net.minecraft.block.BlockColored;
import com.rwtema.extrautils.crafting.RecipeMagicalWood;
import com.rwtema.extrautils.crafting.RecipeSoul;
import com.rwtema.extrautils.multipart.microblock.RecipeMicroBlocks;
import com.rwtema.extrautils.crafting.RecipeGlove;
import net.minecraft.init.Items;
import com.rwtema.extrautils.crafting.ShapelessOreRecipeAlwaysLast;
import com.rwtema.extrautils.crafting.ShapedOreRecipeAlwaysLast;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.command.ICommand;
import com.rwtema.extrautils.command.CommandKillEntities;
import net.minecraft.entity.item.EntityItem;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import com.rwtema.extrautils.tileentity.enderconstructor.EnderConstructorRecipesHandler;
import com.rwtema.extrautils.helper.ThaumcraftHelper;
import com.rwtema.extrautils.tileentity.transfernodes.TNHelper;
import com.rwtema.extrautils.tileentity.generators.TileEntityGeneratorPotion;
import com.rwtema.extrautils.dispenser.DispenserStuff;
import com.rwtema.extrautils.tileentity.enderquarry.BlockBreakingRegistry;
import com.rwtema.extrautils.worldgen.Underdark.DarknessTickHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraftforge.common.ForgeChunkManager;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import codechicken.multipart.MultipartGenerator;
import com.rwtema.extrautils.multipart.RegisterMicroMaterials;
import com.rwtema.extrautils.modintegration.EE3Integration;
import com.rwtema.extrautils.modintegration.MFRIntegration;
import com.rwtema.extrautils.modintegration.TE4IMC;
import cpw.mods.fml.common.network.IGuiHandler;
import com.rwtema.extrautils.network.GuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import com.rwtema.extrautils.multipart.microblock.RegisterMicroBlocks;
import com.rwtema.extrautils.multipart.microblock.ItemMicroBlock;
import com.rwtema.extrautils.item.ItemGoldenBag;
import com.rwtema.extrautils.item.scanner.ItemScanner;
import com.rwtema.extrautils.block.BlockEtherealStone;
import com.rwtema.extrautils.item.ItemSonarGoggles;
import com.rwtema.extrautils.item.ItemTemporalHoe;
import com.rwtema.extrautils.item.ItemHealingAxe;
import com.rwtema.extrautils.item.ItemDestructionPickaxe;
import com.rwtema.extrautils.item.ItemErosionShovel;
import com.rwtema.extrautils.item.ItemEthericSword;
import com.rwtema.extrautils.item.ItemBuildersWand;
import com.rwtema.extrautils.worldgen.Underdark.EventHandlerUnderdark;
import net.minecraftforge.common.MinecraftForge;
import com.rwtema.extrautils.worldgen.Underdark.WorldProviderUnderdark;
import com.rwtema.extrautils.worldgen.endoftime.WorldProviderEndOfTime;
import net.minecraftforge.common.DimensionManager;
import com.rwtema.extrautils.tileentity.TileEntityPortal;
import com.rwtema.extrautils.block.BlockPortal;
import com.rwtema.extrautils.item.ItemUnstableIngot;
import com.rwtema.extrautils.item.ItemDivisionSigil;
import com.rwtema.extrautils.item.ItemGoldenLasso;
import com.rwtema.extrautils.item.ItemLawSword;
import com.rwtema.extrautils.item.ItemBlockGenerator;
import com.rwtema.extrautils.tileentity.generators.BlockGenerator;
import com.rwtema.extrautils.tileentity.TileEntityDrum;
import com.rwtema.extrautils.item.ItemBlockDrum;
import com.rwtema.extrautils.block.BlockDrum;
import codechicken.multipart.TMultiPart;
import com.rwtema.extrautils.multipart.RegisterBlockPart;
import com.rwtema.extrautils.multipart.MagnumTorchPart;
import com.rwtema.extrautils.tileentity.TileEntityAntiMobTorch;
import com.rwtema.extrautils.multipart.ItemBlockMultiPartMagnumTorch;
import com.rwtema.extrautils.block.BlockMagnumTorch;
import com.rwtema.extrautils.block.BlockTimer;
import cpw.mods.fml.common.IWorldGenerator;
import com.rwtema.extrautils.worldgen.WorldGenEnderLillies;
import com.rwtema.extrautils.item.ItemBlockEnderLily;
import com.rwtema.extrautils.tileentity.enderquarry.BlockEnderQuarry;
import com.rwtema.extrautils.tileentity.TileEntityEnderThermicLavaPump;
import com.rwtema.extrautils.block.BlockEnderthermicPump;
import com.rwtema.extrautils.tileentity.TileEntityEnchantedSpike;
import com.rwtema.extrautils.block.BlockSpikeGold;
import com.rwtema.extrautils.block.BlockSpikeDiamond;
import com.rwtema.extrautils.item.ItemBlockSpike;
import com.rwtema.extrautils.tileentity.TileEntityTrashCanEnergy;
import com.rwtema.extrautils.tileentity.TileEntityTrashCanFluids;
import com.rwtema.extrautils.tileentity.TileEntityTrashCan;
import com.rwtema.extrautils.block.BlockTrashCan;
import com.rwtema.extrautils.block.BlockCursedEarth;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityFilterPipe;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityRetrievalNodeLiquid;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityRetrievalNodeInventory;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeHyperEnergy;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeEnergy;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeLiquid;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeInventory;
import com.rwtema.extrautils.tileentity.transfernodes.multiparts.RegisterTransferNodeParts;
import com.rwtema.extrautils.tileentity.transfernodes.multiparts.RegisterPipeParts;
import com.rwtema.extrautils.tileentity.transfernodes.multiparts.ItemBlockTransferPipeMultiPart;
import com.rwtema.extrautils.tileentity.transfernodes.BlockRetrievalNode;
import com.rwtema.extrautils.tileentity.transfernodes.multiparts.ItemBlockTransferNodeMultiPart;
import com.rwtema.extrautils.tileentity.transfernodes.BlockTransferNode;
import com.rwtema.extrautils.tileentity.TileEntityRainMuffler;
import com.rwtema.extrautils.tileentity.TileEntitySoundMuffler;
import com.rwtema.extrautils.block.BlockSoundMuffler;
import com.rwtema.extrautils.tileentity.TileEntityTradingPost;
import com.rwtema.extrautils.block.BlockTradingPost;
import com.rwtema.extrautils.block.BlockPeacefulTable;
import com.rwtema.extrautils.tileentity.TileEntityFilingCabinet;
import com.rwtema.extrautils.item.ItemFilingCabinet;
import com.rwtema.extrautils.block.BlockConveyor;
import net.minecraft.block.material.Material;
import com.rwtema.extrautils.tileentity.TileEntityBlockColorData;
import com.rwtema.extrautils.item.ItemBlockColor;
import com.rwtema.extrautils.item.ItemPaintbrush;
import com.rwtema.extrautils.block.BlockColorData;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import com.rwtema.extrautils.block.BlockChandelier;
import com.rwtema.extrautils.tileentity.TileEntityBUD;
import com.rwtema.extrautils.item.ItemBlockMetadata;
import com.rwtema.extrautils.item.ItemAngelBlock;
import com.rwtema.extrautils.block.BlockAngelBlock;
import com.rwtema.extrautils.block.BlockCurtain;
import com.rwtema.extrautils.tileentity.enderquarry.TileEntityEnderMarker;
import com.rwtema.extrautils.tileentity.enderconstructor.TileEnderPillar;
import com.rwtema.extrautils.tileentity.enderconstructor.TileEnderConstructor;
import com.rwtema.extrautils.item.ItemBlockQED;
import com.rwtema.extrautils.item.ItemGlove;
import com.rwtema.extrautils.tileentity.endercollector.TileEnderCollector;
import com.rwtema.extrautils.tileentity.endercollector.BlockEnderCollector;
import com.rwtema.extrautils.tileentity.chests.TileFullChest;
import com.rwtema.extrautils.tileentity.chests.BlockFullChest;
import com.rwtema.extrautils.tileentity.chests.TileMiniChest;
import com.rwtema.extrautils.tileentity.chests.BlockMiniChest;
import net.minecraftforge.common.config.Property;
import com.rwtema.extrautils.multipart.microblock.CreativeTabMicroBlocks;
import com.rwtema.extrautils.tileentity.enderquarry.TileEntityEnderQuarry;
import java.util.Collections;
import net.minecraftforge.common.config.Configuration;
import java.io.File;
import cpw.mods.fml.common.Loader;
import java.util.Iterator;
import com.rwtema.extrautils.helper.XUHelper;
import net.minecraft.world.World;
import com.rwtema.extrautils.network.NetworkHandler;
import com.rwtema.extrautils.network.PacketCodec;
import com.rwtema.extrautils.network.XUPacketBase;
import java.util.Collection;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.discovery.ModCandidate;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.IEnergyHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraft.item.ItemBlock;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.WeightedRandomChestContent;
import java.util.Random;
import net.minecraftforge.common.ChestGenHooks;
import com.rwtema.extrautils.helper.XURandom;
import net.minecraft.item.ItemStack;
import com.rwtema.extrautils.block.BlockBUD;
import net.minecraft.item.crafting.IRecipe;
import com.rwtema.extrautils.block.BlockSpikeWood;
import com.rwtema.extrautils.tileentity.enderquarry.BlockQuarryUpgrades;
import com.rwtema.extrautils.item.ItemHeatingCoil;
import com.rwtema.extrautils.tileentity.enderquarry.BlockEnderMarkers;
import com.rwtema.extrautils.item.ItemAngelRing;
import java.util.Set;
import com.rwtema.extrautils.tileentity.enderconstructor.BlockEnderConstructor;
import com.rwtema.extrautils.item.ItemWateringCan;
import com.rwtema.extrautils.item.ItemSoul;
import com.rwtema.extrautils.tileentity.transfernodes.BlockTransferPipe;
import com.rwtema.extrautils.item.ItemBedrockiumIngot;
import com.rwtema.extrautils.block.BlockSpike;
import com.rwtema.extrautils.item.ItemPrecisionShears;
import com.rwtema.extrautils.item.ItemNodeUpgrade;
import com.rwtema.extrautils.block.BlockGreenScreen;
import com.rwtema.extrautils.block.BlockFilingCabinet;
import com.rwtema.extrautils.block.BlockEnderLily;
import com.rwtema.extrautils.block.BlockPureLove;
import com.rwtema.extrautils.block.BlockBedrockium;
import com.rwtema.extrautils.block.BlockDecoration;
import com.rwtema.extrautils.block.BlockColor;
import java.util.List;
import com.rwtema.extrautils.block.BlockCobblestoneCompressed;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import java.util.ArrayList;

public class ExtraUtils
{
    public static final int dec1EdgedStoneBricks = 0;
    public static final int dec1EnderObsidian = 1;
    public static final int dec1BurntQuartz = 2;
    public static final int dec1FrostStone = 3;
    public static final int dec1BorderStone = 4;
    public static final int dec1UnstableBlock = 5;
    public static final int dec1GravelBricks = 6;
    public static final int dec1BorderStone2 = 7;
    public static final int dec1MagicalWood = 8;
    public static final int dec1SandyGlass = 9;
    public static final int dec1GravelRoad = 10;
    public static final int dec1EnderCore = 11;
    public static final int dec1DiamondMatrix = 12;
    public static final int dec1EnderSandAlloy = 13;
    public static final int dec1EminenceStone = 14;
    public static final int dec2ThickGlass = 0;
    public static final int dec2ThickGlassEdged = 1;
    public static final int dec2ThickGlassBricks = 2;
    public static final int dec2ThickGlassCarved = 3;
    public static final int dec2ThickGlassGolden = 4;
    public static final int dec2ThickGlassObsidian = 5;
    public static final int dec2ThickGlassSwirling = 6;
    public static final int dec2ThickGlassGlowstone = 7;
    public static final int dec2ThickGlassHeart = 8;
    public static final int dec2ThickGlassSquare = 9;
    public static final int dec2DarkGlass = 10;
    public static final int dec2DarkGlassObsidian = 11;
    public static final ArrayList<Item> spikeSwords;
    public static boolean allNonVanillaDimensionsValidForEnderPump;
    public static Block angelBlock;
    public static Item buildersWand;
    public static boolean buildersWandEnabled;
    public static Block chandelier;
    public static boolean chandelierEnabled;
    public static BlockCobblestoneCompressed cobblestoneCompr;
    public static boolean cobblestoneComprEnabled;
    public static Block colorBlockData;
    public static List<BlockColor> colorblocks;
    public static boolean colorBlockDataEnabled;
    public static Block conveyor;
    public static boolean conveyorEnabled;
    public static Item creativeBuildersWand;
    public static boolean creativeBuildersWandEnabled;
    public static CreativeTabExtraUtils creativeTabExtraUtils;
    public static Item creativeTabIcon;
    public static Block cursedEarth;
    public static boolean cursedEarthEnabled;
    public static Block curtain;
    public static boolean curtainEnabled;
    public static BlockDecoration decorative1;
    public static boolean decorative1Enabled;
    public static BlockDecoration decorative2;
    public static boolean decorative2Enabled;
    public static BlockBedrockium bedrockiumBlock;
    public static boolean bedrockiumBlockEnabled;
    public static BlockPureLove pureLove;
    public static boolean pureLoveBlockEnabled;
    public static Item destructionPickaxe;
    public static boolean destructionPickaxeEnabled;
    public static int underdarkDimID;
    public static int endoftimeDimID;
    public static boolean disableAdvFilingCabinet;
    public static boolean disableBuildersWandRecipe;
    public static boolean disableChandelierRecipe;
    public static boolean disableColoredBlocksRecipes;
    public static boolean disableCompressedCobblestoneRecipe;
    public static boolean disableConveyorRecipe;
    public static boolean disableCrossoverPipeRecipe;
    public static boolean disableCurtainRecipe;
    public static boolean disableDestructionPickaxeRecipe;
    public static boolean disableDivisionSigilInChests;
    public static boolean disableDrumRecipe;
    public static boolean disableEnderLiliesInDungeons;
    public static boolean disableEnderThermicPumpRecipe;
    public static boolean disableEnergyPipeRecipe;
    public static boolean disableErosionShovelRecipe;
    public static boolean disableEtherealBlockRecipe;
    public static boolean disableEthericSwordRecipe;
    public static boolean disableFilingCabinet;
    public static boolean disableFilterPipeRecipe;
    public static boolean disableFilterRecipe;
    public static boolean disableGeneratorRecipe;
    public static boolean disableGoldenBagRecipe;
    public static boolean disableGoldenLassoRecipe;
    public static boolean disableHealingAxeRecipe;
    public static boolean disableMagnumTorchRecipe;
    public static boolean disableModSortingPipesRecipe;
    public static boolean disableEnergyExtractionPipeRecipe;
    public static boolean disableNodeUpgradeSpeedRecipe;
    public static boolean disableObsidianRecipe;
    public static boolean disablePaintbrushRecipe;
    public static boolean disablePeacefulTableRecipe;
    public static boolean disablePortalTexture;
    public static boolean disablePrecisionShears;
    public static boolean disableRainMufflerRecipe;
    public static boolean disableRationingPipeRecipe;
    public static boolean disableSonarGogglesRecipe;
    public static boolean disableSortingPipeRecipe;
    public static boolean disableSoundMufflerRecipe;
    public static boolean disableSpikeRecipe;
    public static boolean disableTemporalHoeRecipe;
    public static boolean disableTimerBlockRecipe;
    public static boolean disableTradingPostRecipe;
    public static boolean disableTransferNodeEnergyRecipe;
    public static boolean disableTransferNodeLiquidRecipe;
    public static boolean disableTransferNodeLiquidRemoteRecipe;
    public static boolean disableTransferNodeRecipe;
    public static boolean disableTransferNodeRemoteRecipe;
    public static boolean disableTransferPipeRecipe;
    public static boolean disableTrashCanRecipe;
    public static boolean disableUnstableIngotRecipe;
    public static boolean disableWateringCanRecipe;
    public static boolean disableWitherRecipe;
    public static boolean disableNodeParticles;
    public static boolean disableEnderQuarryUpgradesRecipes;
    public static boolean disableQEDIngotSmeltRecipes;
    public static boolean showMultiblocksTab;
    public static Item divisionSigil;
    public static boolean divisionSigilEnabled;
    public static Block drum;
    public static boolean drumEnabled;
    public static BlockEnderLily enderLily;
    public static boolean enderLilyEnabled;
    public static int enderLilyRetrogenId;
    public static Block enderQuarry;
    public static boolean enderQuarryEnabled;
    public static boolean enderQuarryRecipeEnabled;
    public static Block enderThermicPump;
    public static boolean enderThermicPumpEnabled;
    public static Item erosionShovel;
    public static boolean erosionShovelEnabled;
    public static Block etheralBlock;
    public static boolean etherealBlockEnabled;
    public static Item ethericSword;
    public static boolean ethericSwordEnabled;
    public static BlockFilingCabinet filingCabinet;
    public static boolean filingCabinetEnabled;
    public static Block generator;
    public static Block generator2;
    public static Block generator3;
    public static boolean generatorEnabled;
    public static boolean generator2Enabled;
    public static boolean generator3Enabled;
    public static Item goldenBag;
    public static boolean goldenBagEnabled;
    public static Item goldenLasso;
    public static boolean goldenLassoEnabled;
    public static BlockGreenScreen greenScreen;
    public static boolean greenScreenEnabled;
    public static boolean handlesClientMethods;
    public static Item healingAxe;
    public static boolean healingAxeEnabled;
    public static Block magnumTorch;
    public static boolean magnumTorchEnabled;
    public static Item microBlocks;
    public static boolean microBlocksEnabled;
    public static ItemNodeUpgrade nodeUpgrade;
    public static boolean nodeUpgradeEnabled;
    public static Item paintBrush;
    public static boolean paintBrushEnabled;
    public static Block peacefultable;
    public static boolean peacefultableEnabled;
    public static Block portal;
    public static boolean portalEnabled;
    public static ItemPrecisionShears precisionShears;
    public static boolean precisionShearsEnabled;
    public static Item scanner;
    public static boolean scannerEnabled;
    public static Item sonarGoggles;
    public static boolean sonarGogglesEnabled;
    public static Item lawSword;
    public static boolean lawSwordEnabled;
    public static Block soundMuffler;
    public static boolean soundMufflerEnabled;
    public static BlockSpike spike;
    public static boolean spikeEnabled;
    public static boolean spikeItemSword;
    public static ItemBedrockiumIngot bedrockium;
    public static boolean bedrockiumEnabled;
    public static Item temporalHoe;
    public static boolean temporalHoeEnabled;
    public static Block timerBlock;
    public static boolean timerBlockEnabled;
    public static Block tradingPost;
    public static boolean tradingPostEnabled;
    public static Block transferNode;
    public static boolean transferNodeEnabled;
    public static Block transferNodeRemote;
    public static boolean transferNodeRemoteEnabled;
    public static BlockTransferPipe transferPipe;
    public static BlockTransferPipe transferPipe2;
    public static boolean transferPipeEnabled;
    public static Block trashCan;
    public static boolean trashCanEnabled;
    public static ItemSoul soul;
    public static boolean soulEnabled;
    public static Item unstableIngot;
    public static boolean unstableIngotExplosion;
    public static boolean unstableIngotEnabled;
    public static int[] validDimensionsForEnderPump;
    public static ItemWateringCan wateringCan;
    public static boolean wateringCanEnabled;
    public static boolean qedEnabled;
    public static BlockEnderConstructor qed;
    public static Set<String> qedList;
    public static ItemAngelRing angelRing;
    public static boolean angelRingEnabled;
    public static BlockEnderMarkers enderMarker;
    public static boolean enderMarkerEnabled;
    public static boolean permaSoulDrainOff;
    public static boolean gloveEnabled;
    public static Item glove;
    public static boolean disableChestRecipe;
    public static boolean disableWitherNoiseUnlessNearby;
    public static boolean heatingCoilEnabled;
    public static ItemHeatingCoil heatingCoil;
    public static BlockColor colorBlockBrick;
    public static BlockColor coloredWood;
    public static BlockQuarryUpgrades enderQuarryUpgrade;
    public static boolean enderQuarryUpgradeEnabled;
    public static boolean spikeGoldEnabled;
    public static boolean spikeDiamondEnabled;
    public static boolean spikeWoodEnabled;
    public static BlockSpike spikeGold;
    public static BlockSpike spikeDiamond;
    public static BlockColor colorQuartz;
    public static BlockSpikeWood spikeWood;
    public static BlockColor colorBlockRedstone;
    public static boolean peacefulTableInAllDifficulties;
    public static boolean disableInfiniteWater;
    public static boolean disableCobblegen;
    public static int versionHash;
    public static Set<Class<? extends IRecipe>> registeredRecipes;
    private static boolean angelBlockEnabled;
    private static boolean BUDBlockEnabled;
    private static BlockBUD BUDBlock;
    private static boolean disableSuperWateringCanRecipe;
    boolean hasSpecialInit;
    public static int tcon_unstable_material_id;
    public static int tcon_magical_wood_id;
    public static int tcon_bedrock_material_id;
    private static boolean disableAngelBlockRecipe;
    private static boolean disableBUDBlockRecipe;
    private static boolean disableAdvBUDBlockRecipe;
    private static Block enderCollector;
    private static boolean enderCollectorEnabled;
    public static Block fullChest;
    public static Block miniChest;
    public static boolean fullChestEnabled;
    public static boolean miniChestEnabled;
    List<ILoading> loaders;
    
    public ExtraUtils() {
        this.hasSpecialInit = false;
        this.loaders = new ArrayList<ILoading>();
    }
    
    public static void addDungeonItem(final ItemStack item, final int min, final int max, final String category, final double probability) {
        final int n = getWeightTotal(ChestGenHooks.getItems(category, (Random)XURandom.getInstance()));
        final int a = (int)Math.ceil(probability * n);
        ChestGenHooks.addItem(category, new WeightedRandomChestContent(item, min, max, a));
    }
    
    public static void registerTile(final Class<? extends TileEntity> clazz, final String name) {
        GameRegistry.registerTileEntity((Class)clazz, name);
    }
    
    public static void registerTile(final Class<? extends TileEntity> clazz) {
        GameRegistry.registerTileEntity((Class)clazz, clazz.getSimpleName());
    }
    
    public static int getWeightTotal(final WeightedRandomChestContent[] items) {
        if (items == null || items.length == 0) {
            return 1;
        }
        int weight = 0;
        for (final WeightedRandomChestContent item : items) {
            weight += item.itemWeight;
        }
        return weight;
    }
    
    public static Block registerBlock(final Block block) {
        return registerBlock(block, ItemBlock.class);
    }
    
    public static Block registerBlock(final Block block, final Class<? extends ItemBlock> itemblock) {
        String s = block.getUnlocalizedName().substring("tile.".length());
        s = s.replace("extrautils:", "");
        return GameRegistry.registerBlock(block, (Class)itemblock, s);
    }
    
    public static Item registerItem(final Item item) {
        final String s = getDefaultRegisterName(item);
        registerItem(item, s);
        return item;
    }
    
    public static String getDefaultRegisterName(final Item item) {
        String s = item.getUnlocalizedName().substring("item.".length());
        s = s.replace("extrautils:", "");
        return s;
    }
    
    public static void registerItem(final Item item, final String s) {
        if (ExtraUtils.creativeTabIcon == null) {
            ExtraUtils.creativeTabIcon = item;
        }
        GameRegistry.registerItem(item, s);
    }
    
    public static void addRecipe(final IRecipe recipe) {
        registerRecipe(recipe.getClass());
        GameRegistry.addRecipe(recipe);
    }
    
    public static void addRecipe(final ItemStack out, final Object... ingreds) {
        GameRegistry.addRecipe((IRecipe)new ShapedOreRecipe(out, ingreds));
    }
    
    public static void registerRecipe(final Class<? extends IRecipe> recipe) {
        if (ExtraUtils.registeredRecipes.contains(recipe)) {
            return;
        }
        if (!recipe.getName().startsWith("com.rwtema.")) {
            return;
        }
        ExtraUtils.registeredRecipes.add(recipe);
        LogHelper.fine("Registering " + recipe.getSimpleName() + " to RecipeSorter", new Object[0]);
        if (ShapedOreRecipe.class.isAssignableFrom(recipe)) {
            RecipeSorter.register("extrautils:" + recipe.getSimpleName(), (Class)recipe, RecipeSorter.Category.SHAPED, "after:forge:shapedore");
        }
        else if (ShapelessOreRecipe.class.isAssignableFrom(recipe)) {
            RecipeSorter.register("extrautils:" + recipe.getSimpleName(), (Class)recipe, RecipeSorter.Category.SHAPELESS, "after:forge:shapelessore");
        }
        else if (ShapedRecipes.class.isAssignableFrom(recipe)) {
            RecipeSorter.register("extrautils:" + recipe.getSimpleName(), (Class)recipe, RecipeSorter.Category.SHAPED, "after:minecraft:shaped before:minecraft:shapeless");
        }
        else if (ShapelessRecipes.class.isAssignableFrom(recipe)) {
            RecipeSorter.register("extrautils:" + recipe.getSimpleName(), (Class)recipe, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless before:minecraft:bookcloning");
        }
        else {
            RecipeSorter.register("extrautils:" + recipe.getSimpleName(), (Class)recipe, RecipeSorter.Category.SHAPELESS, "after:forge:shapelessore");
        }
    }
    
    public void addSigil(final String category, final double probability) {
        addDungeonItem(new ItemStack(ExtraUtils.divisionSigil, 1), 1, 1, category, probability);
    }
    
    public void preInit(final FMLPreInitializationEvent event) {
        LogHelper.info("Hello World", new Object[0]);
        this.loadTcon();
        ExtraUtils.versionHash = event.getModMetadata().version.hashCode();
        try {
            IEnergyHandler.class.getMethod("canConnectEnergy", ForgeDirection.class);
            for (final ModCandidate t : event.getAsmData().getCandidatesFor("cofh.api.energy")) {
                final boolean hasProperApi = false;
                if (t.getClassList().contains("cofh/api/energy/IEnergyHandler") && !t.getClassList().contains("cofh/api/energy/IEnergyConnection")) {
                    for (final ModContainer mod : t.getContainedMods()) {
                        LogHelper.info("" + mod.getModId() + " (" + mod.getName() + ") appears to be missing the updated COFH api", new Object[0]);
                    }
                }
            }
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
            final List<ModContainer> suspects = new ArrayList<ModContainer>();
            for (final ModCandidate t2 : event.getAsmData().getCandidatesFor("cofh.api.energy")) {
                final boolean hasProperApi2 = false;
                if (t2.getClassList().contains("cofh/api/energy/IEnergyHandler") && !t2.getClassList().contains("cofh/api/energy/IEnergyConnection")) {
                    for (final ModContainer mod2 : t2.getContainedMods()) {
                        LogHelper.info("" + mod2.getModId() + " (" + mod2.getName() + ") appears to be missing the updated COFH api", new Object[0]);
                    }
                    suspects.addAll(t2.getContainedMods());
                }
            }
            final String[] data = new String[2 + suspects.size()];
            data[0] = "Some mod is including a older or incorrect copy of the COFH energy api. This will lead to instability and Extra Utilities will not run properly. Possible candidates that do not include the proper api are...";
            data[1] = "";
            for (int i = 0; i < suspects.size(); ++i) {
                data[i + 1] = suspects.get(i).getModId();
            }
            ExtraUtilsMod.proxy.throwLoadingError("COFH API Error", data);
        }
        final String networkPath = "com.rwtema.extrautils.network.packets.";
        for (final ModCandidate t3 : event.getAsmData().getCandidatesFor("com.rwtema.extrautils")) {
            for (String s : t3.getClassList()) {
                s = s.replace('/', '.');
                if (s.startsWith(networkPath)) {
                    try {
                        Class<?> clazz = Class.forName(s);
                        if (XUPacketBase.class.isAssignableFrom(clazz)) {
                            PacketCodec.addClass(clazz);
                        }
                        else {
                            clazz = clazz;
                        }
                    }
                    catch (ClassNotFoundException e2) {
                        throw new RuntimeException("Presented class missing, FML Bug?", e2);
                    }
                    catch (NoClassDefFoundError e3) {
                        LogHelper.error(s + " can't be created", new Object[0]);
                        throw new RuntimeException(e3);
                    }
                }
            }
        }
        NetworkHandler.register();
        try {
            World.class.getMethod("getBlock", Integer.TYPE, Integer.TYPE, Integer.TYPE);
            XUHelper.deObf = true;
            LogHelper.info("Dev Enviroment detected. Releasing hounds...", new Object[0]);
        }
        catch (NoSuchMethodException e4) {
            XUHelper.deObf = false;
        }
        catch (SecurityException e5) {
            XUHelper.deObf = false;
        }
        this.setupConfigs(event.getSuggestedConfigurationFile());
        ExtraUtils.creativeTabExtraUtils = new CreativeTabExtraUtils("extraUtil");
        this.registerStuff();
        this.registerRecipes();
        ExtraUtilsMod.proxy.registerClientCommands();
        for (final ILoading loader : this.loaders) {
            loader.preInit();
        }
    }
    
    public void loadTcon() {
        if (Loader.isModLoaded("TConstruct")) {
            ILoading r = null;
            try {
                final Class<?> clazz = Class.forName("com.rwtema.extrautils.modintegration.TConIntegration");
                r = (ILoading)clazz.newInstance();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            if (r != null) {
                this.loaders.add(r);
            }
        }
    }
    
    public void setupConfigs(final File file) {
        final Configuration config = new Configuration(file);
        config.load();
        ExtraUtils.unstableIngotExplosion = config.get("options", "unstableIngotsExplode", true).getBoolean(true);
        ExtraUtils.disableNodeParticles = config.get("options", "disableTransferNodeParticles", false).getBoolean(false);
        ExtraUtils.disablePortalTexture = config.get("client_options", "disablePortalAnimation", false).getBoolean(false);
        ExtraUtils.disableWitherRecipe = config.get("recipes", "disablePeacefulWitherRecipe", false).getBoolean(false);
        ExtraUtils.peacefulTableInAllDifficulties = config.get("options", "peacefulTableInAllDifficulties", false).getBoolean(false);
        ExtraUtils.disableInfiniteWater = config.get("options", "disableTransferNodeWatergen", false).getBoolean(false);
        ExtraUtils.disableCobblegen = config.get("options", "disableTransferNodeCobblegen", false).getBoolean(false);
        ExtraUtils.permaSoulDrainOff = config.get("options", "soulDrainResetsOnDeath", false).getBoolean(false);
        ExtraUtils.disableWitherNoiseUnlessNearby = config.get("options", "disableWitherNoisesIfNotNearby", true).getBoolean(true);
        if (config.hasKey("options", "mimicDeobf")) {
            XUHelper.deObf = true;
        }
        ExtraUtils.tcon_unstable_material_id = config.get("tinkersIntegration", "tcon_unstable_material_id", 314).getInt(314);
        ExtraUtils.tcon_bedrock_material_id = config.get("tinkersIntegration", "tcon_bedrock_material_id", 315).getInt(315);
        ExtraUtils.tcon_magical_wood_id = config.get("tinkersIntegration", "tcon_magical_wood_id", 316).getInt(316);
        ExtraUtils.fullChestEnabled = this.getBlock(config, "slightlyLargerChestEnabled");
        ExtraUtils.miniChestEnabled = this.getBlock(config, "miniChestEnabled");
        ExtraUtils.enderCollectorEnabled = this.getBlock(config, "enderCollectorEnabled");
        ExtraUtils.gloveEnabled = this.getItem(config, "gloveEnabled");
        ExtraUtils.pureLoveBlockEnabled = this.getBlock(config, "pureLoveBlockEnabled");
        ExtraUtils.bedrockiumBlockEnabled = this.getBlock(config, "bedrockiumBlockEnabled");
        ExtraUtils.enderMarkerEnabled = this.getBlock(config, "enderMarkerEnabled");
        ExtraUtils.angelBlockEnabled = this.getBlock(config, "angelBlockEnabled");
        ExtraUtils.BUDBlockEnabled = this.getBlock(config, "BUDBlockEnabled");
        ExtraUtils.chandelierEnabled = this.getBlock(config, "chandelierEnabled");
        ExtraUtils.disableChandelierRecipe = config.get("recipes", "disableChandelierRecipe", false).getBoolean(false);
        ExtraUtils.disableChestRecipe = config.get("recipes", "disableAltChestRecipe", false).getBoolean(false);
        ExtraUtils.disableColoredBlocksRecipes = config.get("recipes", "disableColoredBlocksRecipes", false).getBoolean(false);
        ExtraUtils.colorBlockDataEnabled = this.getBlock(config, "colorBlockDataEnabled");
        ExtraUtils.disableCompressedCobblestoneRecipe = config.get("recipes", "disableCompressedCobblestoneRecipe", false).getBoolean(false);
        ExtraUtils.cobblestoneComprEnabled = this.getBlock(config, "cobblestoneComprEnabled");
        ExtraUtils.disableConveyorRecipe = config.get("recipes", "disableConveyorRecipe", false).getBoolean(false);
        ExtraUtils.conveyorEnabled = this.getBlock(config, "conveyorEnabled");
        ExtraUtils.greenScreenEnabled = this.getBlock(config, "greenScreen");
        ExtraUtils.disablePeacefulTableRecipe = config.get("recipes", "disablePeacefulTableRecipe", false).getBoolean(false);
        ExtraUtils.peacefultableEnabled = this.getBlock(config, "peacefultableEnabled");
        ExtraUtils.disableSoundMufflerRecipe = config.get("recipes", "disableSoundMufflerRecipe", false).getBoolean(false);
        ExtraUtils.disableRainMufflerRecipe = config.get("recipes", "disableRainMufflerRecipe", false).getBoolean(false);
        ExtraUtils.soundMufflerEnabled = this.getBlock(config, "soundMufflerEnabled");
        ExtraUtils.disableTradingPostRecipe = config.get("recipes", "disableTradingPostRecipe", false).getBoolean(false);
        ExtraUtils.tradingPostEnabled = this.getBlock(config, "tradingPost");
        ExtraUtils.disableFilterPipeRecipe = config.get("recipes", "disableFilterPipeRecipe", false).getBoolean(false);
        ExtraUtils.disableSortingPipeRecipe = config.get("recipes", "disableSortingPipeRecipe", false).getBoolean(false);
        ExtraUtils.disableModSortingPipesRecipe = config.get("recipes", "disableModSortingPipeRecipe", false).getBoolean(false);
        ExtraUtils.disableEnergyExtractionPipeRecipe = config.get("recipes", "disableEnergyExtractionPipeRecipe", false).getBoolean(false);
        ExtraUtils.disableRationingPipeRecipe = config.get("recipes", "disableRationingPipeRecipe", false).getBoolean(false);
        ExtraUtils.disableTransferPipeRecipe = config.get("recipes", "disableTransferPipeRecipe", false).getBoolean(false);
        ExtraUtils.transferPipeEnabled = this.getBlock(config, "transferPipeEnabled");
        ExtraUtils.disableTransferNodeRecipe = config.get("recipes", "disableTransferNodeRecipe", false).getBoolean(false);
        ExtraUtils.disableTransferNodeLiquidRecipe = config.get("recipes", "disableTransferNodeLiquidRecipe", false).getBoolean(false);
        ExtraUtils.disableTransferNodeEnergyRecipe = config.get("recipes", "disableTransferNodeEnergyRecipe", false).getBoolean(false);
        ExtraUtils.transferNodeEnabled = this.getBlock(config, "transferNodeEnabled");
        ExtraUtils.disableCurtainRecipe = config.get("recipes", "disableCurtainRecipe", false).getBoolean(false);
        ExtraUtils.curtainEnabled = this.getBlock(config, "curtainEnabled");
        ExtraUtils.cursedEarthEnabled = this.getBlock(config, "cursedEarth");
        ExtraUtils.disableTrashCanRecipe = config.get("recipes", "disableTrashCanRecipe", false).getBoolean(false);
        ExtraUtils.trashCanEnabled = this.getBlock(config, "trashCan");
        ExtraUtils.disableSpikeRecipe = config.get("recipes", "disableSpikeRecipe", false).getBoolean(false);
        ExtraUtils.spikeEnabled = this.getBlock(config, "spikeEnabled");
        ExtraUtils.spikeGoldEnabled = this.getBlock(config, "spikeGoldEnabled");
        ExtraUtils.spikeDiamondEnabled = this.getBlock(config, "spikeDiamondEnabled");
        ExtraUtils.spikeWoodEnabled = this.getBlock(config, "spikeWoodEnabled");
        ExtraUtils.disableEtherealBlockRecipe = config.get("recipes", "disableEtherealGlassRecipe", false).getBoolean(false);
        ExtraUtils.etherealBlockEnabled = this.getBlock(config, "etherealBlockEnabled");
        ExtraUtils.disableEnderThermicPumpRecipe = config.get("recipes", "disableEnderThermicPumpRecipe", false).getBoolean(false);
        ExtraUtils.disableEnderQuarryUpgradesRecipes = config.get("recipes", "disableEnderQuarryUpgradesRecipes", false).getBoolean(false);
        ExtraUtils.allNonVanillaDimensionsValidForEnderPump = !config.get("options", "disableEnderPumpInAllDimensions", false).getBoolean(false);
        ExtraUtils.validDimensionsForEnderPump = config.get("options", "EnderPumpDimensionExceptions", new int[0]).getIntList();
        ExtraUtils.enderThermicPumpEnabled = this.getBlock(config, "enderThermicPumpEnabled");
        ExtraUtils.disableTimerBlockRecipe = config.get("recipes", "disableRedstoneClockRecipe", false).getBoolean(false);
        ExtraUtils.timerBlockEnabled = this.getBlock(config, "timerBlockEnabled");
        ExtraUtils.disableMagnumTorchRecipe = config.get("recipes", "disableMagnumTorchRecipe", false).getBoolean(false);
        ExtraUtils.magnumTorchEnabled = this.getBlock(config, "magnumTorchEnabled");
        ExtraUtils.disableDrumRecipe = config.get("recipes", "disableDrumRecipe", false).getBoolean(false);
        ExtraUtils.drumEnabled = this.getBlock(config, "drumEnabled");
        ExtraUtils.decorative1Enabled = this.getBlock(config, "decorative_1Enabled");
        ExtraUtils.filingCabinetEnabled = this.getBlock(config, "filingCabinetEnabled");
        ExtraUtils.disableFilingCabinet = config.get("recipes", "disableFilingCabinet", false).getBoolean(false);
        ExtraUtils.disableAdvFilingCabinet = config.get("recipes", "disableAdvFilingCabinet", false).getBoolean(false);
        ExtraUtils.disableTransferPipeRecipe = config.get("recipes", "disableTransferPipeRecipe", false).getBoolean(false);
        ExtraUtils.enderLilyEnabled = this.getBlock(config, "enderLilyEnabled");
        ExtraUtils.disableEnderLiliesInDungeons = config.get("recipes", "disableEnderLiliesInDungeons", false).getBoolean(false);
        ExtraUtils.disableEnergyPipeRecipe = config.get("recipes", "disableEnergyPipeRecipe", false).getBoolean(false);
        ExtraUtils.disableCrossoverPipeRecipe = config.get("recipes", "disableEnergyPipeRecipe", false).getBoolean(false);
        ExtraUtils.portalEnabled = this.getBlock(config, "portalEnabled");
        ExtraUtils.underdarkDimID = config.get("options", "deepDarkDimensionID", -100).getInt(-100);
        ExtraUtils.endoftimeDimID = config.get("options", "lastMilleniumDimensionID", -112).getInt(-112);
        ExtraUtils.decorative2Enabled = this.getBlock(config, "decorative_2Enabled");
        ExtraUtils.generatorEnabled = this.getBlock(config, "generatorEnabled");
        ExtraUtils.generator2Enabled = this.getBlock(config, "generator8Enabled");
        ExtraUtils.generator3Enabled = this.getBlock(config, "generator64Enabled");
        ExtraUtils.enderQuarryEnabled = this.getBlock(config, "enderQuarryEnabled");
        ExtraUtils.enderQuarryUpgradeEnabled = this.getBlock(config, "enderQuarryUpgradeEnabled");
        ExtraUtils.disableGeneratorRecipe = config.get("recipes", "disableGeneratorRecipe", false).getBoolean(false);
        ExtraUtils.transferNodeRemoteEnabled = this.getBlock(config, "transferNodeRemoteEnabled");
        ExtraUtils.disableTransferNodeRemoteRecipe = config.get("recipes", "disableRetrievalNodeRecipe", false).getBoolean(false);
        ExtraUtils.disableTransferNodeLiquidRemoteRecipe = config.get("recipes", "disableRetrievalNodeLiquidRecipe", false).getBoolean(false);
        ExtraUtils.disableGoldenLassoRecipe = config.get("recipes", "disableGoldenLassoRecipe", false).getBoolean(false);
        ExtraUtils.goldenLassoEnabled = this.getItem(config, "goldenLassoEnabled");
        ExtraUtils.disablePaintbrushRecipe = config.get("recipes", "disablePaintbrushRecipe", false).getBoolean(false);
        ExtraUtils.paintBrushEnabled = this.getItem(config, "paintBrush");
        ExtraUtils.disableUnstableIngotRecipe = config.get("recipes", "disableUnstableIngotRecipe", false).getBoolean(false);
        ExtraUtils.unstableIngotEnabled = this.getItem(config, "unstableIngotEnabled");
        ExtraUtils.disableBuildersWandRecipe = config.get("recipes", "disableBuildersWandRecipe", false).getBoolean(false);
        ExtraUtils.buildersWandEnabled = this.getItem(config, "buildersWandEnabled");
        ExtraUtils.creativeBuildersWandEnabled = this.getItem(config, "creativeBuildersWandEnabled");
        ExtraUtils.disableEthericSwordRecipe = config.get("recipes", "disableEthericSwordRecipe", false).getBoolean(false);
        ExtraUtils.ethericSwordEnabled = this.getItem(config, "ethericSword");
        ExtraUtils.disableErosionShovelRecipe = config.get("recipes", "disableErosionShovelRecipe", false).getBoolean(false);
        ExtraUtils.erosionShovelEnabled = this.getItem(config, "erosionShovel");
        ExtraUtils.disableDestructionPickaxeRecipe = config.get("recipes", "disableDestructionPickaxeRecipe", false).getBoolean(false);
        ExtraUtils.destructionPickaxeEnabled = this.getItem(config, "destructionPickaxe");
        ExtraUtils.disableHealingAxeRecipe = config.get("recipes", "disableHealingAxeRecipe", false).getBoolean(false);
        ExtraUtils.healingAxeEnabled = this.getItem(config, "healingAxe");
        ExtraUtils.disableTemporalHoeRecipe = config.get("recipes", "disableReversingHoeRecipe", false).getBoolean(false);
        ExtraUtils.temporalHoeEnabled = this.getItem(config, "temporalHoe");
        ExtraUtils.disableDivisionSigilInChests = config.get("recipes", "disableDivisionSiginInChests", false).getBoolean(false);
        ExtraUtils.divisionSigilEnabled = this.getItem(config, "divisionSigilEnabled");
        ExtraUtils.disableSonarGogglesRecipe = config.get("recipes", "disableSonarGogglesRecipe", false).getBoolean(false);
        ExtraUtils.sonarGogglesEnabled = this.getItem(config, "sonarGogglesEnabled");
        ExtraUtils.disableWateringCanRecipe = config.get("recipes", "disableWateringCanRecipe", false).getBoolean(false);
        ExtraUtils.disableSuperWateringCanRecipe = config.get("recipes", "disableSuperWateringCanRecipe", false).getBoolean(false);
        ExtraUtils.wateringCanEnabled = this.getItem(config, "wateringCanEnabled");
        ExtraUtils.disableFilterRecipe = config.get("recipes", "disableFilterRecipe", false).getBoolean(false);
        ExtraUtils.disableNodeUpgradeSpeedRecipe = config.get("recipes", "disableNodeUpgradeSpeedRecipe", false).getBoolean(false);
        ExtraUtils.nodeUpgradeEnabled = this.getItem(config, "upgradeNodeEnabled");
        ExtraUtils.disableGoldenBagRecipe = config.get("recipes", "disableGoldenBagRecipe", false).getBoolean(false);
        ExtraUtils.goldenBagEnabled = this.getItem(config, "goldenBagEnabled");
        ExtraUtils.scannerEnabled = this.getItem(config, "scannerEnabled");
        ExtraUtils.bedrockiumEnabled = this.getItem(config, "bedrockiumIngotEnabled");
        ExtraUtils.angelRingEnabled = this.getItem(config, "angelRingEnabled");
        ExtraUtils.soulEnabled = this.getItem(config, "soulEnabled");
        ExtraUtils.lawSwordEnabled = this.getItem(config, "lawSwordEnabled");
        ExtraUtils.disableQEDIngotSmeltRecipes = config.get("recipes", "disableQEDIngotSmeltRecipes", false).getBoolean(false);
        ExtraUtils.heatingCoilEnabled = this.getItem(config, "heatingCoilEnabled");
        ExtraUtils.qedEnabled = this.getBlock(config, "QEDEnabled");
        final Property prop_version = config.get("QEDCrafting", "QEDVersion", 0, "Internal version number to add/remove items in future updates. Set to -1 to disable auto-updates.");
        final int prev_version = prop_version.getInt();
        final Property prop = config.get("QEDCrafting", "QEDItems", new String[] { "tile.extrautils:extractor_base_remote.0", "tile.extrautils:extractor_base_remote.6", "item.extrautils:nodeUpgrade.5", "item.extrautils:nodeUpgrade.6", "tile.extrautils:enderQuarry", "tile.extrautils:magnumTorch" }, "ItemStack names to enforce crafting in the QED");
        final String[][] toAdd = { { "tile.extrautils:endMarker" }, { "tile.extrautils:extractor_base.12" }, { "tile.extrautils:enderQuarryUpgrade.0", "tile.extrautils:extractor_base.13" } };
        final String[][] toRemove = { { "tile.extrautils:enderQuarry" }, new String[0], new String[0] };
        assert toAdd.length == toRemove.length;
        final List<String> strings = new ArrayList<String>();
        Collections.addAll(strings, prop.getStringList());
        if (prev_version >= 0 && prev_version < toAdd.length) {
            for (int i = prev_version; i < toAdd.length; ++i) {
                if (toAdd[i] != null) {
                    for (final String s : toAdd[i]) {
                        if (strings.add(s)) {
                            LogHelper.info("QEDCrafting Updater: Added Recipe - " + s, new Object[0]);
                        }
                    }
                }
                if (toRemove[i] != null) {
                    for (final String s : toRemove[i]) {
                        if (strings.remove(s)) {
                            LogHelper.info("QEDCrafting Updater: Removed Recipe - " + s, new Object[0]);
                        }
                    }
                }
            }
            prop_version.set(toAdd.length);
        }
        prop.set((String[])strings.toArray(new String[strings.size()]));
        if (strings.size() > 0) {
            for (final String s2 : strings) {
                ExtraUtils.qedList.add(s2);
                LogHelper.fine("Recipes constructing " + s2 + " will now be used in the Ender Constructor", new Object[0]);
            }
        }
        else {
            ExtraUtils.qedEnabled = false;
            LogHelper.fine("No Recipes for QED found. QED will be disabled.", new Object[0]);
        }
        if (ExtraUtils.enderQuarryEnabled) {
            TileEntityEnderQuarry.baseDrain = config.get("Ender Quarry Power", "baseDrain", 1800).getInt(1800);
            TileEntityEnderQuarry.hardnessDrain = config.get("Ender Quarry Power", "hardnessDrain", 200).getInt(200);
        }
        if (Loader.instance().getIndexedModList().containsKey("ForgeMultipart")) {
            ExtraUtils.microBlocksEnabled = this.getItem(config, "microBlocksEnabled");
            ExtraUtils.showMultiblocksTab = (ExtraUtils.microBlocksEnabled && !config.get("options", "disableMultiBlocksCreativeTab", false).getBoolean(false));
            if (ExtraUtils.showMultiblocksTab) {
                CreativeTabMicroBlocks.instance = new CreativeTabMicroBlocks();
            }
        }
        ExtraUtils.precisionShearsEnabled = this.getItem(config, "precisionShearsId");
        ExtraUtils.enderLilyRetrogenId = config.get("worldgen", "retrogen_enderlillies", 1).getInt(1);
        config.save();
    }
    
    public void registerStuff() {
        if (ExtraUtils.miniChestEnabled) {
            registerBlock(ExtraUtils.miniChest = new BlockMiniChest());
            registerTile(TileMiniChest.class);
        }
        if (ExtraUtils.fullChestEnabled) {
            registerBlock(ExtraUtils.fullChest = new BlockFullChest());
            registerTile(TileFullChest.class);
        }
        if (ExtraUtils.enderCollectorEnabled) {
            registerBlock(ExtraUtils.enderCollector = new BlockEnderCollector());
            registerTile(TileEnderCollector.class);
        }
        if (ExtraUtils.gloveEnabled) {
            registerItem(ExtraUtils.glove = (Item)new ItemGlove());
        }
        if (ExtraUtils.heatingCoilEnabled) {
            registerItem(ExtraUtils.heatingCoil = new ItemHeatingCoil());
        }
        if (ExtraUtils.soulEnabled) {
            registerItem(ExtraUtils.soul = new ItemSoul());
        }
        if (ExtraUtils.angelRingEnabled) {
            registerItem(ExtraUtils.angelRing = new ItemAngelRing());
        }
        if (ExtraUtils.qedEnabled) {
            registerBlock(ExtraUtils.qed = new BlockEnderConstructor(), ItemBlockQED.class);
            registerTile(TileEnderConstructor.class);
            registerTile(TileEnderPillar.class);
        }
        if (ExtraUtils.enderMarkerEnabled) {
            registerBlock(ExtraUtils.enderMarker = new BlockEnderMarkers());
            registerTile(TileEntityEnderMarker.class);
        }
        if (ExtraUtils.curtainEnabled) {
            registerBlock(ExtraUtils.curtain = new BlockCurtain());
        }
        if (ExtraUtils.angelBlockEnabled) {
            registerBlock(ExtraUtils.angelBlock = new BlockAngelBlock(), ItemAngelBlock.class);
        }
        if (ExtraUtils.BUDBlockEnabled) {
            registerBlock(ExtraUtils.BUDBlock = new BlockBUD(), ItemBlockMetadata.class);
            registerTile(TileEntityBUD.class);
        }
        if (ExtraUtils.chandelierEnabled) {
            registerBlock(ExtraUtils.chandelier = new BlockChandelier());
        }
        if (ExtraUtils.decorative1Enabled && ExtraUtils.decorative2Enabled) {
            (ExtraUtils.decorative1 = new BlockDecoration(true)).setBlockName("extrautils:decorativeBlock1");
            registerBlock(ExtraUtils.decorative1, ItemBlockMetadata.class);
            ExtraUtils.decorative1.addBlock(0, "Edged Stone Bricks", "extrautils:ConnectedTextures/test", true, true);
            ExtraUtils.decorative1.addBlock(1, "Ender-infused Obsidian", "extrautils:ConnectedTextures/endsidian", true, true);
            ExtraUtils.decorative1.hardness[1] = Blocks.obsidian.getBlockHardness((World)null, 0, 0, 0);
            ExtraUtils.decorative1.resistance[1] = Blocks.obsidian.getExplosionResistance((Entity)null) * 5.0f;
            ExtraUtils.decorative1.addBlock(2, "Burnt Quartz", "extrautils:ConnectedTextures/dark", true, true);
            ExtraUtils.decorative1.addBlock(3, "Frosted Stone", "extrautils:ConnectedTextures/icystone", true, false);
            ExtraUtils.decorative1.addBlock(4, "Border Stone", "extrautils:ConnectedTextures/carved", true, true);
            ExtraUtils.decorative1.addBlock(5, "Unstable Ingot Block", "extrautils:ConnectedTextures/uingot", true, true);
            ExtraUtils.decorative1.addBlock(6, "Gravel Bricks", "extrautils:ConnectedTextures/gravel_brick");
            ExtraUtils.decorative1.addBlock(7, "Border Stone (Alternate)", "extrautils:ConnectedTextures/singlestonebrick", true, true);
            ExtraUtils.decorative1.addBlock(8, "Magical Wood", "extrautils:ConnectedTextures/magic_wood_corners");
            ExtraUtils.decorative1.enchantBonus[8] = 2.5f;
            ExtraUtils.decorative1.addBlock(9, "Sandy Glass", "extrautils:sandedGlass");
            ExtraUtils.decorative1.addBlock(10, "Gravel Road", "extrautils:ConnectedTextures/gravel_road", true, true);
            ExtraUtils.decorative1.flipTopBottom[10] = true;
            ExtraUtils.decorative1.addBlock(11, "Ender Core", "extrautils:endCore");
            ExtraUtils.decorative1.enchantBonus[11] = 10.0f;
            ExtraUtils.decorative1.isSuperEnder[11] = true;
            ExtraUtils.decorative1.light[11] = 5;
            ExtraUtils.decorative1.addBlock(12, "Diamond-Etched Computational Matrix", "extrautils:diamondCore");
            ExtraUtils.decorative1.light[12] = 14;
            ExtraUtils.decorative1.addBlock(13, "Ender-Sand Alloy", "extrautils:ConnectedTextures/endslab", true, true);
            ExtraUtils.decorative1.isEnder[13] = true;
            ExtraUtils.decorative1.addBlock(14, "Eminence Stone", "extrautils:ConnectedTextures/purplestone", true, true);
            OreDictionary.registerOre("bricksGravel", new ItemStack((Block)ExtraUtils.decorative1, 1, 6));
            OreDictionary.registerOre("blockEnderObsidian", new ItemStack((Block)ExtraUtils.decorative1, 1, 1));
            OreDictionary.registerOre("burntQuartz", new ItemStack((Block)ExtraUtils.decorative1, 1, 2));
            OreDictionary.registerOre("blockIcestone", new ItemStack((Block)ExtraUtils.decorative1, 1, 3));
            OreDictionary.registerOre("blockUnstable", new ItemStack((Block)ExtraUtils.decorative1, 1, 5));
            OreDictionary.registerOre("blockMagicWood", new ItemStack((Block)ExtraUtils.decorative1, 1, 7));
            OreDictionary.registerOre("blockEnderCore", new ItemStack((Block)ExtraUtils.decorative1, 1, 11));
            OreDictionary.registerOre("blockGlassSandy", new ItemStack((Block)ExtraUtils.decorative1, 1, 9));
            (ExtraUtils.decorative2 = new BlockDecoration(false)).setBlockName("extrautils:decorativeBlock2");
            registerBlock(ExtraUtils.decorative2, ItemBlockMetadata.class);
            ExtraUtils.decorative2.addBlock(0, "Thickened Glass", "extrautils:ConnectedTextures/glass1", true, false);
            ExtraUtils.decorative2.addBlock(1, "Edged Glass", "extrautils:ConnectedTextures/glass2", true, false);
            ExtraUtils.decorative2.addBlock(2, "Glass Bricks", "extrautils:ConnectedTextures/glass3", true, false);
            ExtraUtils.decorative2.addBlock(3, "Carved Glass", "extrautils:ConnectedTextures/glass4", true, false);
            ExtraUtils.decorative2.addBlock(4, "Golden Edged Glass", "extrautils:ConnectedTextures/glass5", true, false);
            ExtraUtils.decorative2.addBlock(5, "Obsidian Glass", "extrautils:ConnectedTextures/glass6", true, false);
            ExtraUtils.decorative2.hardness[5] = 4.0f;
            ExtraUtils.decorative2.resistance[5] = 2000.0f;
            ExtraUtils.decorative2.addBlock(6, "Swirling Glass", "extrautils:ConnectedTextures/glass7", true, false);
            ExtraUtils.decorative2.addBlock(7, "Glowstone Glass", "extrautils:ConnectedTextures/glass8", true, false);
            ExtraUtils.decorative2.light[7] = 15;
            ExtraUtils.decorative2.addBlock(8, "Heart Glass", "extrautils:ConnectedTextures/glass9", true, false);
            ExtraUtils.decorative2.addBlock(9, "Square Glass", "extrautils:glassQuadrants", false, false);
            ExtraUtils.decorative2.addBlock(10, "Dark Glass", "extrautils:ConnectedTextures/darkglass", true, false);
            ExtraUtils.decorative2.opacity[10] = 255;
            ExtraUtils.decorative2.addBlock(11, "Dark Obsidian Glass", "extrautils:ConnectedTextures/obsidiandarkglass", true, false);
            ExtraUtils.decorative2.opacity[11] = 255;
            ExtraUtils.decorative2.hardness[11] = 4.0f;
            ExtraUtils.decorative2.resistance[11] = 2000.0f;
            for (int i = 0; i < 12; ++i) {
                if (i != 4 && i != 5 && i != 7 && i != 8 && i != 10 && i != 11) {
                    OreDictionary.registerOre("blockGlass", new ItemStack((Block)ExtraUtils.decorative2, 1, i));
                }
            }
        }
        if (ExtraUtils.pureLoveBlockEnabled) {
            registerBlock(ExtraUtils.pureLove = new BlockPureLove());
        }
        if (ExtraUtils.bedrockiumBlockEnabled) {
            registerBlock((Block)(ExtraUtils.bedrockiumBlock = new BlockBedrockium()), BlockBedrockium.ItemBedrockium.class);
        }
        if (ExtraUtils.colorBlockDataEnabled) {
            registerBlock(ExtraUtils.colorBlockData = new BlockColorData());
            if (ExtraUtils.paintBrushEnabled) {
                registerItem(ExtraUtils.paintBrush = new ItemPaintbrush());
            }
            registerBlock(ExtraUtils.colorBlockBrick = (BlockColor)new BlockColor(Blocks.stonebrick, "bricksStone", "stonebrick").setBlockName("extrautils:colorStoneBrick"), ItemBlockColor.class);
            registerBlock(ExtraUtils.coloredWood = (BlockColor)new BlockColor(Blocks.planks, "plankWood", "planks_oak").setBlockName("extrautils:colorWoodPlanks"), ItemBlockColor.class);
            registerBlock(new BlockColor(Blocks.glowstone, "glowstone"), ItemBlockColor.class);
            registerBlock(new BlockColor(Blocks.stone, "stone"), ItemBlockColor.class);
            registerBlock(ExtraUtils.colorQuartz = new BlockColor(Blocks.quartz_block, null, "quartz_block_top"), ItemBlockColor.class);
            registerBlock(new BlockColor(Blocks.soul_sand, "soulsand"), ItemBlockColor.class);
            registerBlock(new BlockColor(Blocks.lit_redstone_lamp, null, "redstone_lamp_on").setCustomRecipe(6, "SRS", "SDS", "SPS", 'S', Blocks.redstone_lamp, 'R', Blocks.redstone_torch, 'D', "dye", 'P', ExtraUtils.paintBrush), ItemBlockColor.class);
            registerBlock(new BlockColor(Blocks.brick_block, "bricksClay"), ItemBlockColor.class);
            registerBlock(new BlockColor(Blocks.cobblestone, "cobblestone"), ItemBlockColor.class);
            registerBlock(new BlockColor(Blocks.lapis_block, "blockLapis"), ItemBlockColor.class);
            registerBlock(new BlockColor(Blocks.obsidian, "obsidian"), ItemBlockColor.class);
            registerBlock(ExtraUtils.colorBlockRedstone = new BlockColor(Blocks.redstone_block, "blockRedstone"), ItemBlockColor.class);
            registerBlock(new BlockColor(Blocks.coal_block, "blockCoal"), ItemBlockColor.class);
            registerTile(TileEntityBlockColorData.class);
        }
        if (ExtraUtils.cobblestoneComprEnabled) {
            registerBlock(ExtraUtils.cobblestoneCompr = new BlockCobblestoneCompressed(Material.rock), ItemBlockMetadata.class);
            for (int i = 0; i < 16; ++i) {
                final String s = "compressed" + BlockCobblestoneCompressed.getOreDictName(i) + (1 + BlockCobblestoneCompressed.getCompr(i)) + "x";
                OreDictionary.registerOre(s, new ItemStack((Block)ExtraUtils.cobblestoneCompr, 1, i));
            }
        }
        if (ExtraUtils.conveyorEnabled) {
            registerBlock(ExtraUtils.conveyor = new BlockConveyor());
        }
        if (ExtraUtils.filingCabinetEnabled) {
            registerBlock(ExtraUtils.filingCabinet = new BlockFilingCabinet(), ItemFilingCabinet.class);
            registerTile(TileEntityFilingCabinet.class);
        }
        if (ExtraUtils.greenScreenEnabled) {
            registerBlock(ExtraUtils.greenScreen = new BlockGreenScreen(), ItemBlockMetadata.class);
        }
        if (ExtraUtils.peacefultableEnabled) {
            registerBlock(ExtraUtils.peacefultable = new BlockPeacefulTable());
        }
        if (ExtraUtils.tradingPostEnabled) {
            registerBlock(ExtraUtils.tradingPost = (Block)new BlockTradingPost());
            registerTile(TileEntityTradingPost.class);
        }
        if (ExtraUtils.soundMufflerEnabled) {
            registerBlock(ExtraUtils.soundMuffler = new BlockSoundMuffler(), ItemBlockMetadata.class);
            registerTile(TileEntitySoundMuffler.class);
            registerTile(TileEntityRainMuffler.class);
        }
        if (ExtraUtils.transferNodeEnabled && ExtraUtils.transferPipeEnabled) {
            if (Loader.isModLoaded("ForgeMultipart")) {
                registerBlock(ExtraUtils.transferNode = new BlockTransferNode(), ItemBlockTransferNodeMultiPart.class);
                registerBlock(ExtraUtils.transferNodeRemote = new BlockRetrievalNode(), ItemBlockTransferNodeMultiPart.class);
                registerBlock(ExtraUtils.transferPipe = new BlockTransferPipe(0), ItemBlockTransferPipeMultiPart.class);
                registerBlock(ExtraUtils.transferPipe2 = new BlockTransferPipe(1), ItemBlockTransferPipeMultiPart.class);
            }
            else {
                registerBlock(ExtraUtils.transferNode = new BlockTransferNode(), ItemBlockMetadata.class);
                registerBlock(ExtraUtils.transferNodeRemote = new BlockRetrievalNode(), ItemBlockMetadata.class);
                registerBlock(ExtraUtils.transferPipe = new BlockTransferPipe(0), ItemBlockMetadata.class);
                registerBlock(ExtraUtils.transferPipe2 = new BlockTransferPipe(1), ItemBlockMetadata.class);
            }
            if (Loader.isModLoaded("ForgeMultipart")) {
                new RegisterPipeParts().init();
                new RegisterTransferNodeParts().init();
            }
            registerTile(TileEntityTransferNodeInventory.class);
            registerTile(TileEntityTransferNodeLiquid.class);
            registerTile(TileEntityTransferNodeEnergy.class);
            registerTile(TileEntityTransferNodeHyperEnergy.class);
            registerTile(TileEntityRetrievalNodeInventory.class);
            registerTile(TileEntityRetrievalNodeLiquid.class);
            registerTile(TileEntityFilterPipe.class);
        }
        if (ExtraUtils.nodeUpgradeEnabled) {
            registerItem(ExtraUtils.nodeUpgrade = new ItemNodeUpgrade());
        }
        if (ExtraUtils.cursedEarthEnabled) {
            registerBlock(ExtraUtils.cursedEarth = new BlockCursedEarth(), ItemBlockMetadata.class);
        }
        if (ExtraUtils.trashCanEnabled) {
            registerBlock(ExtraUtils.trashCan = new BlockTrashCan(), ItemBlockMetadata.class);
            registerTile(TileEntityTrashCan.class);
            registerTile(TileEntityTrashCanFluids.class);
            registerTile(TileEntityTrashCanEnergy.class);
        }
        if (ExtraUtils.spikeEnabled) {
            registerBlock(ExtraUtils.spike = new BlockSpike(), ItemBlockSpike.class);
        }
        if (ExtraUtils.spikeDiamondEnabled) {
            registerBlock(ExtraUtils.spikeDiamond = new BlockSpikeDiamond(), ItemBlockSpike.class);
        }
        if (ExtraUtils.spikeGoldEnabled) {
            registerBlock(ExtraUtils.spikeGold = new BlockSpikeGold(), ItemBlockSpike.class);
        }
        if (ExtraUtils.spikeWoodEnabled) {
            registerBlock(ExtraUtils.spikeWood = new BlockSpikeWood(), ItemBlockSpike.class);
        }
        if (ExtraUtils.spikeEnabled || ExtraUtils.spikeGoldEnabled || ExtraUtils.spikeDiamondEnabled || ExtraUtils.spikeWoodEnabled) {
            registerTile(TileEntityEnchantedSpike.class);
        }
        if (ExtraUtils.enderThermicPumpEnabled) {
            registerBlock(ExtraUtils.enderThermicPump = new BlockEnderthermicPump());
            registerTile((Class<? extends TileEntity>)TileEntityEnderThermicLavaPump.class, "enderPump");
        }
        if (ExtraUtils.enderQuarryEnabled) {
            registerBlock(ExtraUtils.enderQuarry = new BlockEnderQuarry());
            registerBlock(ExtraUtils.enderQuarryUpgrade = new BlockQuarryUpgrades(), ItemBlockMetadata.class);
            registerTile(TileEntityEnderQuarry.class, "enderQuarry");
        }
        if (ExtraUtils.enderLilyEnabled) {
            registerBlock((Block)(ExtraUtils.enderLily = new BlockEnderLily()), ItemBlockEnderLily.class);
            GameRegistry.registerWorldGenerator((IWorldGenerator)new WorldGenEnderLillies(), 9);
        }
        if (ExtraUtils.timerBlockEnabled) {
            registerBlock(ExtraUtils.timerBlock = new BlockTimer());
        }
        if (ExtraUtils.magnumTorchEnabled) {
            if (Loader.isModLoaded("ForgeMultipart")) {
                registerBlock(ExtraUtils.magnumTorch = new BlockMagnumTorch(), ItemBlockMultiPartMagnumTorch.class);
            }
            else {
                registerBlock(ExtraUtils.magnumTorch = new BlockMagnumTorch());
            }
            registerTile(TileEntityAntiMobTorch.class);
            if (Loader.isModLoaded("ForgeMultipart")) {
                new RegisterBlockPart(ExtraUtils.magnumTorch, (Class<? extends TMultiPart>)MagnumTorchPart.class, "XU|MagnumTorch").init();
            }
        }
        if (ExtraUtils.drumEnabled) {
            registerBlock(ExtraUtils.drum = new BlockDrum(), ItemBlockDrum.class);
            registerTile(TileEntityDrum.class, "drum");
        }
        if (ExtraUtils.generatorEnabled) {
            registerBlock(ExtraUtils.generator = new BlockGenerator(), ItemBlockGenerator.class);
            if (ExtraUtils.generator2Enabled) {
                registerBlock(ExtraUtils.generator2 = new BlockGenerator(8), ItemBlockGenerator.class);
            }
            if (ExtraUtils.generator3Enabled) {
                registerBlock(ExtraUtils.generator3 = new BlockGenerator(64), ItemBlockGenerator.class);
            }
            BlockGenerator.mapTiles();
        }
        if (ExtraUtils.lawSwordEnabled) {
            registerItem(ExtraUtils.lawSword = (Item)new ItemLawSword());
        }
        if (ExtraUtils.goldenLassoEnabled) {
            registerItem(ExtraUtils.goldenLasso = new ItemGoldenLasso());
        }
        if (ExtraUtils.divisionSigilEnabled) {
            registerItem(ExtraUtils.divisionSigil = new ItemDivisionSigil());
        }
        if (ExtraUtils.unstableIngotEnabled) {
            registerItem(ExtraUtils.unstableIngot = new ItemUnstableIngot());
            OreDictionary.registerOre("ingotUnstable", new ItemStack(ExtraUtils.unstableIngot, 1, 0));
            OreDictionary.registerOre("ingotUnstable", new ItemStack(ExtraUtils.unstableIngot, 1, 2));
            OreDictionary.registerOre("nuggetUnstable", new ItemStack(ExtraUtils.unstableIngot, 1, 1));
        }
        if (ExtraUtils.portalEnabled && (ExtraUtils.endoftimeDimID != 0 || ExtraUtils.underdarkDimID != 0)) {
            registerBlock(ExtraUtils.portal = new BlockPortal(), ItemBlockMetadata.class);
            registerTile(TileEntityPortal.class);
            if (ExtraUtils.endoftimeDimID != 0) {
                if (DimensionManager.isDimensionRegistered(ExtraUtils.endoftimeDimID)) {
                    ExtraUtilsMod.proxy.throwLoadingError("Invalid id for 'End of Time' dimension. Change endoftimeDimID in config.", new String[0]);
                }
                DimensionManager.registerProviderType(ExtraUtils.endoftimeDimID, (Class)WorldProviderEndOfTime.class, true);
                DimensionManager.registerDimension(ExtraUtils.endoftimeDimID, ExtraUtils.endoftimeDimID);
            }
            if (ExtraUtils.underdarkDimID != 0) {
                DimensionManager.registerProviderType(ExtraUtils.underdarkDimID, (Class)WorldProviderUnderdark.class, false);
                DimensionManager.registerDimension(ExtraUtils.underdarkDimID, ExtraUtils.underdarkDimID);
                MinecraftForge.EVENT_BUS.register((Object)new EventHandlerUnderdark());
                MinecraftForge.ORE_GEN_BUS.register((Object)new EventHandlerUnderdark());
                MinecraftForge.TERRAIN_GEN_BUS.register((Object)new EventHandlerUnderdark());
            }
        }
        if (ExtraUtils.buildersWandEnabled) {
            registerItem(ExtraUtils.buildersWand = new ItemBuildersWand(9));
        }
        if (ExtraUtils.precisionShearsEnabled) {
            registerItem((Item)(ExtraUtils.precisionShears = new ItemPrecisionShears()));
        }
        if (ExtraUtils.creativeBuildersWandEnabled) {
            registerItem(ExtraUtils.creativeBuildersWand = new ItemBuildersWand(49).setUnlocalizedName("extrautils:creativebuilderswand"));
        }
        if (ExtraUtils.ethericSwordEnabled) {
            registerItem(ExtraUtils.ethericSword = (Item)new ItemEthericSword());
        }
        if (ExtraUtils.erosionShovelEnabled) {
            registerItem(ExtraUtils.erosionShovel = (Item)new ItemErosionShovel());
        }
        if (ExtraUtils.destructionPickaxeEnabled) {
            registerItem(ExtraUtils.destructionPickaxe = (Item)new ItemDestructionPickaxe());
        }
        if (ExtraUtils.healingAxeEnabled) {
            registerItem(ExtraUtils.healingAxe = (Item)new ItemHealingAxe());
        }
        if (ExtraUtils.temporalHoeEnabled) {
            registerItem(ExtraUtils.temporalHoe = (Item)new ItemTemporalHoe());
        }
        if (ExtraUtils.sonarGogglesEnabled) {
            registerItem(ExtraUtils.sonarGoggles = (Item)new ItemSonarGoggles());
        }
        if (ExtraUtils.etherealBlockEnabled) {
            registerBlock(ExtraUtils.etheralBlock = new BlockEtherealStone(), ItemBlockMetadata.class);
        }
        if (ExtraUtils.wateringCanEnabled) {
            registerItem(ExtraUtils.wateringCan = new ItemWateringCan());
        }
        if (ExtraUtils.scannerEnabled) {
            registerItem(ExtraUtils.scanner = new ItemScanner());
        }
        if (ExtraUtils.goldenBagEnabled) {
            registerItem(ExtraUtils.goldenBag = new ItemGoldenBag());
        }
        if (ExtraUtils.bedrockiumEnabled) {
            registerItem(ExtraUtils.bedrockium = new ItemBedrockiumIngot());
        }
        if (ExtraUtils.microBlocksEnabled) {
            registerItem(ExtraUtils.microBlocks = new ItemMicroBlock());
            RegisterMicroBlocks.register();
        }
    }
    
    public void init(final FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler((Object)ExtraUtilsMod.instance, (IGuiHandler)new GuiHandler());
        if (Loader.isModLoaded("ForgeMultipart")) {
            this.FMPRegisterPassThroughInterfaces();
        }
        ExtraUtilsMod.proxy.registerEventHandler();
        ExtraUtilsMod.proxy.registerRenderInformation();
        if (Loader.isModLoaded("ThermalExpansion")) {
            TE4IMC.addIntegration();
        }
        if (Loader.isModLoaded("MineFactoryReloaded")) {
            MFRIntegration.registerMFRIntegration();
        }
        for (final ILoading loader : this.loaders) {
            loader.init();
        }
        EE3Integration.finalRegister();
    }
    
    private void FMPRegisterPassThroughInterfaces() {
        if (Loader.isModLoaded("ForgeMultipart")) {
            RegisterMicroMaterials.registerBlock(ExtraUtils.cobblestoneCompr, 0, 16);
            RegisterMicroMaterials.registerBlock(ExtraUtils.enderThermicPump);
            RegisterMicroMaterials.registerBlock(ExtraUtils.tradingPost);
            RegisterMicroMaterials.registerConnectedTexture(ExtraUtils.etheralBlock, 0);
            RegisterMicroMaterials.registerConnectedTexture(ExtraUtils.etheralBlock, 1);
            RegisterMicroMaterials.registerConnectedTexture(ExtraUtils.etheralBlock, 2);
            RegisterMicroMaterials.registerConnectedTexture(ExtraUtils.etheralBlock, 3);
            RegisterMicroMaterials.registerConnectedTexture(ExtraUtils.etheralBlock, 4);
            RegisterMicroMaterials.registerConnectedTexture(ExtraUtils.etheralBlock, 5);
            RegisterMicroMaterials.registerFullBright(ExtraUtils.greenScreen);
            for (final BlockColor col : ExtraUtils.colorblocks) {
                RegisterMicroMaterials.registerColorBlock(col);
            }
            for (int i = 0; i < 16; ++i) {
                if (ExtraUtils.decorative1 != null && ExtraUtils.decorative1.name[i] != null) {
                    RegisterMicroMaterials.registerConnectedTexture(ExtraUtils.decorative1, i);
                }
                if (ExtraUtils.decorative2 != null && ExtraUtils.decorative2.name[i] != null) {
                    RegisterMicroMaterials.registerConnectedTexture(ExtraUtils.decorative2, i);
                }
            }
        }
        MultipartGenerator.registerPassThroughInterface("com.rwtema.extrautils.tileentity.IAntiMobTorch", false, true);
        MultipartGenerator.registerPassThroughInterface("com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipe");
        MultipartGenerator.registerPassThroughInterface("com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipeCosmetic");
        MultipartGenerator.registerPassThroughInterface("com.rwtema.extrautils.tileentity.transfernodes.pipes.IFilterPipe");
        MultipartGenerator.registerPassThroughInterface("com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INode");
        MultipartGenerator.registerPassThroughInterface("com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeInventory");
        MultipartGenerator.registerPassThroughInterface("com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeLiquid");
        MultipartGenerator.registerPassThroughInterface("com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeEnergy");
        MultipartGenerator.registerPassThroughInterface("cofh.api.energy.IEnergyHandler");
    }
    
    public void postInit(final FMLPostInitializationEvent evt) {
        ForgeChunkManager.setForcedChunkLoadingCallback((Object)ExtraUtilsMod.instance, (ForgeChunkManager.LoadingCallback)new ChunkloadCallback());
        CommandTPSTimer.init();
        if (ExtraUtils.underdarkDimID != 0 && ExtraUtils.portalEnabled) {
            FMLCommonHandler.instance().bus().register((Object)new DarknessTickHandler());
        }
        if (ExtraUtils.divisionSigilEnabled && !ExtraUtils.disableDivisionSigilInChests) {
            this.addSigil("dungeonChest", 0.01);
            this.addSigil("mineshaftCorridor", 0.001);
            this.addSigil("pyramidDesertyChest", 0.02);
            this.addSigil("pyramidJungleChest", 0.05);
            this.addSigil("strongholdCrossing", 0.01);
            this.addSigil("strongholdCorridor", 0.01);
        }
        if (!ExtraUtils.disableEnderLiliesInDungeons && ExtraUtils.enderLily != null) {
            addDungeonItem(new ItemStack((Block)ExtraUtils.enderLily), 1, 5, "dungeonChest", 0.03);
        }
        ExtraUtilsMod.proxy.postInit();
        if (ExtraUtils.enderQuarryEnabled) {
            BlockBreakingRegistry.instance.setupBreaking();
        }
        DispenserStuff.registerItems();
        if (ExtraUtils.generatorEnabled) {
            TileEntityGeneratorPotion.genPotionLevels();
        }
        if (ExtraUtils.transferPipeEnabled) {
            TNHelper.initBlocks();
        }
        if (Loader.isModLoaded("Thaumcraft")) {
            ThaumcraftHelper.registerItems();
        }
        for (final ILoading loader : this.loaders) {
            loader.postInit();
        }
    }
    
    private String standardizeName(String name) {
        if (name.endsWith("enabled")) {
            name = name.replaceAll("enabled", "Enabled");
        }
        if (!name.endsWith("Enabled")) {
            name += "Enabled";
        }
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        return name;
    }
    
    private boolean getItem(final Configuration config, final String string) {
        return config.get("Items", this.standardizeName(string), true).getBoolean(true);
    }
    
    private boolean getBlock(final Configuration config, final String string) {
        return config.get("Blocks", this.standardizeName(string), true).getBoolean(true);
    }
    
    public void specialInit() {
        if (ExtraUtils.qed != null && (!ExtraUtils.qedList.isEmpty() || !ExtraUtils.disableQEDIngotSmeltRecipes)) {
            EnderConstructorRecipesHandler.postInit();
        }
    }
    
    public void serverStarting(final FMLServerStartingEvent event) {
        event.registerServerCommand((ICommand)new CommandKillEntities("items", (Class<? extends Entity>)EntityItem.class, true));
        event.registerServerCommand((ICommand)new CommandKillEntities("mobs", (Class<? extends Entity>)EntityMob.class, true));
        event.registerServerCommand((ICommand)new CommandKillEntities("living", (Class<? extends Entity>)EntityLiving.class, true));
        event.registerServerCommand((ICommand)new CommandKillEntities("xp", (Class<? extends Entity>)EntityXPOrb.class, true));
    }
    
    public void registerRecipes() {
        this.registerRecipe((Class<? extends IRecipe>)ShapedOreRecipeAlwaysLast.class, RecipeSorter.Category.SHAPED, "after:forge:shapelessore");
        this.registerRecipe((Class<? extends IRecipe>)ShapelessOreRecipeAlwaysLast.class, RecipeSorter.Category.SHAPELESS, "after:forge:shapelessore");
        final String unstableIngot = "ingotUnstable";
        if (ExtraUtils.fullChestEnabled) {
            addRecipe((IRecipe)new ShapedOreRecipe(new ItemStack(ExtraUtils.fullChest), new Object[] { "sss", "sCs", "sss", 's', "stickWood", 'C', Blocks.chest }));
        }
        if (ExtraUtils.miniChestEnabled) {
            addRecipe((IRecipe)new ShapelessOreRecipeAlwaysLast(new ItemStack(ExtraUtils.miniChest, 9), new Object[] { Blocks.chest }));
            if (ExtraUtils.fullChestEnabled) {
                addRecipe((IRecipe)new ShapelessOreRecipe(new ItemStack(ExtraUtils.miniChest, 9), new Object[] { ExtraUtils.fullChest }));
            }
            this.addShapedRecipe(new ItemStack((Block)Blocks.chest), "ccc", "ccc", "ccc", 'c', ExtraUtils.miniChest);
        }
        if (ExtraUtils.enderCollectorEnabled) {
            addRecipe(new ItemStack(ExtraUtils.enderCollector), "eEe", " E ", "OOO", 'e', Items.ender_pearl, 'E', (ExtraUtils.decorative1 == null) ? Blocks.obsidian : "blockEnderObsidian", 'O', Blocks.obsidian);
        }
        if (ExtraUtils.gloveEnabled) {
            addRecipe((IRecipe)new RecipeGlove(ExtraUtils.glove));
        }
        if (ExtraUtils.heatingCoilEnabled) {
            addRecipe(new ItemStack((Item)ExtraUtils.heatingCoil), "III", "I I", "IRI", 'I', Blocks.heavy_weighted_pressure_plate, 'R', Items.redstone);
        }
        if (ExtraUtils.microBlocksEnabled) {
            addRecipe((IRecipe)new RecipeMicroBlocks(3, 3, new Object[] { 1, 1, 1, 1, new ItemStack(Blocks.wool, 1), 1, 1, 1, 1 }, new ItemStack(ExtraUtils.microBlocks, 8)));
            addRecipe((IRecipe)new RecipeMicroBlocks(3, 3, new Object[] { 770, 772, 770, 770, 772, 770, null, 772, null }, new ItemStack(ExtraUtils.microBlocks, 2, 1)));
            addRecipe((IRecipe)new RecipeMicroBlocks(3, 3, new Object[] { null, 0, null, 4, 0, 4, 4, 0, 4 }, new ItemStack(ExtraUtils.microBlocks, 5, 2)));
            addRecipe((IRecipe)new RecipeMicroBlocks(3, 3, new Object[] { 1, 4, 1, 4, new ItemStack((Block)ExtraUtils.decorative1, 1, 5), 4, 1, 4, 1 }, new ItemStack(ExtraUtils.microBlocks, 1, 3)));
        }
        if (ExtraUtils.bedrockiumBlockEnabled && ExtraUtils.bedrockiumEnabled) {
            addRecipe(new ItemStack((Block)ExtraUtils.bedrockiumBlock), "KKK", "KKK", "KKK", 'K', ExtraUtils.bedrockium);
            addRecipe(new ItemStack((Item)ExtraUtils.bedrockium, 9), "K", 'K', ExtraUtils.bedrockiumBlock);
            if (ExtraUtils.cobblestoneComprEnabled) {
                this.addSmelting(new ItemStack((Block)ExtraUtils.cobblestoneCompr, 1, 7), new ItemStack((Block)ExtraUtils.bedrockiumBlock), 0);
            }
        }
        if (ExtraUtils.soulEnabled && ExtraUtils.ethericSwordEnabled) {
            addRecipe((IRecipe)new RecipeSoul());
        }
        if (ExtraUtils.angelBlockEnabled && !ExtraUtils.disableAngelBlockRecipe) {
            addRecipe(new ItemStack(ExtraUtils.angelBlock, 1), " H ", "WOW", 'H', Items.gold_ingot, 'W', Items.feather, 'O', Blocks.obsidian);
        }
        if (ExtraUtils.BUDBlockEnabled) {
            if (!ExtraUtils.disableBUDBlockRecipe) {
                addRecipe(new ItemStack((Block)ExtraUtils.BUDBlock, 1, 0), "SRS", "SPS", "STS", 'R', Items.redstone, 'S', Blocks.stone, 'P', Blocks.sticky_piston, 'T', Blocks.redstone_torch);
            }
            if (!ExtraUtils.disableAdvBUDBlockRecipe) {
                addRecipe(new ItemStack((Block)ExtraUtils.BUDBlock, 1, 3), "SRS", "RBR", "SRS", 'R', Blocks.redstone_block, 'S', Blocks.stone, 'B', new ItemStack((Block)ExtraUtils.BUDBlock, 1, 0));
            }
        }
        if (ExtraUtils.chandelierEnabled && !ExtraUtils.disableChandelierRecipe) {
            addRecipe(new ItemStack(ExtraUtils.chandelier, 1), "GDG", "TTT", " T ", 'G', Items.gold_ingot, 'D', Items.diamond, 'T', Blocks.torch);
        }
        if (ExtraUtils.decorative1Enabled && ExtraUtils.decorative2Enabled) {
            addRecipe(new ItemStack((Block)ExtraUtils.decorative1, 9, 0), "SBS", "BBB", "SBS", 'S', Blocks.stone, 'B', new ItemStack(Blocks.stonebrick, 1, 0));
            addRecipe(new ItemStack((Block)ExtraUtils.decorative1, 4, 1), " O ", "OEO", " O ", 'O', Blocks.obsidian, 'E', Items.ender_pearl);
            this.addSmelting(new ItemStack(Blocks.quartz_block), new ItemStack((Block)ExtraUtils.decorative1, 1, 2), 0);
            addRecipe(new ItemStack((Block)ExtraUtils.decorative1, 5, 3), " I ", "ISI", " I ", 'I', Blocks.ice, 'S', Blocks.stone);
            addRecipe(new ItemStack((Block)ExtraUtils.decorative1, 4, 4), "BB", "BB", 'B', new ItemStack((Block)ExtraUtils.decorative1, 1, 0));
            addRecipe(new ItemStack((Block)ExtraUtils.decorative1, 4, 7), "BB", "BB", 'B', new ItemStack((Block)ExtraUtils.decorative1, 1, 4));
            addRecipe(new ItemStack((Block)ExtraUtils.decorative1, 1, 6), "GG", "GG", 'G', Blocks.gravel);
            addRecipe((IRecipe)new RecipeMagicalWood());
            addRecipe(new ItemStack((Block)ExtraUtils.decorative1, 8, 10), "SGS", "GGG", "SGS", 'S', new ItemStack((Block)Blocks.stone_slab, 1, 5), 'G', new ItemStack((Block)ExtraUtils.decorative1, 1, 6));
            addRecipe(new ItemStack((Block)ExtraUtils.decorative1, 1, 11), "GBG", "BDB", "GBG", 'G', new ItemStack((Block)ExtraUtils.decorative1, 1, 8), 'D', Items.ender_eye, 'B', new ItemStack((Block)ExtraUtils.decorative1, 1, 1));
            addRecipe(new ItemStack((Block)ExtraUtils.decorative1, 1, 12), "dEd", "EDE", "dEd", 'D', new ItemStack((Block)ExtraUtils.decorative1, 1, 1), 'E', Items.diamond, 'd', new ItemStack((Block)ExtraUtils.decorative1, 1, 2));
            addRecipe((IRecipe)new ShapelessOreRecipe(new ItemStack((Block)ExtraUtils.decorative1, 4, 13), new Object[] { Blocks.end_stone, "sandstone", "sandstone", Blocks.end_stone }));
            addRecipe((IRecipe)new ShapelessOreRecipe(new ItemStack((Block)ExtraUtils.decorative1, 4, 14), new Object[] { "dyeMagenta", "dyePurple", Blocks.stone, Blocks.stone, Blocks.stone, Blocks.stone, Items.ender_pearl }));
            this.addShapedRecipe(new ItemStack((Block)ExtraUtils.decorative1, 4, 9), "SG", "GS", 'G', Blocks.glass, 'S', Blocks.sand);
            this.addSmelting(new ItemStack((Block)ExtraUtils.decorative1, 1, 9), new ItemStack((Block)ExtraUtils.decorative2, 1, 0), 0);
            final ItemStack glass = new ItemStack((Block)ExtraUtils.decorative2, 1, 0);
            addRecipe(new ItemStack((Block)ExtraUtils.decorative2, 8, 1), "GGG", "G G", "GGG", 'G', glass);
            addRecipe(new ItemStack((Block)ExtraUtils.decorative2, 4, 2), "GG", "GG", 'G', glass);
            addRecipe(new ItemStack((Block)ExtraUtils.decorative2, 1, 3), "GP", 'G', glass, 'P', Items.gunpowder);
            addRecipe(new ItemStack((Block)ExtraUtils.decorative2, 1, 4), "ggg", "gGg", "ggg", 'G', glass, 'g', Items.gold_nugget);
            addRecipe(new ItemStack((Block)ExtraUtils.decorative2, 4, 5), "GOG", "O O", "GOG", 'G', glass, 'O', Blocks.obsidian);
            addRecipe(new ItemStack((Block)ExtraUtils.decorative2, 5, 6), " G ", "GGG", " G ", 'G', glass);
            addRecipe(new ItemStack((Block)ExtraUtils.decorative2, 1, 7), " g ", "gGg", " g ", 'G', glass, 'g', Items.glowstone_dust);
            addRecipe((IRecipe)new ShapedOreRecipe(new ItemStack((Block)ExtraUtils.decorative2, 6, 8), new Object[] { "GpG", "GGG", " G ", 'G', glass, 'p', "dyePink" }));
            addRecipe(new ItemStack((Block)ExtraUtils.decorative2, 1, 9), "G", 'G', glass);
            addRecipe((IRecipe)new ShapedOreRecipe(new ItemStack((Block)ExtraUtils.decorative2, 5, 10), new Object[] { "GCG", "CGC", "GCG", 'G', glass, 'C', (ExtraUtils.curtain == null) ? Items.coal : ExtraUtils.curtain, 'I', "dyeBlack" }));
            addRecipe(new ItemStack((Block)ExtraUtils.decorative2, 4, 11), "GOG", "O O", "GOG", 'G', new ItemStack((Block)ExtraUtils.decorative2, 1, 10), 'O', Blocks.obsidian);
        }
        if (ExtraUtils.curtainEnabled && !ExtraUtils.disableCurtainRecipe) {
            addRecipe(new ItemStack(ExtraUtils.curtain, 12), "WW", "WW", "WW", 'W', Blocks.wool);
        }
        if (ExtraUtils.colorBlockDataEnabled && ExtraUtils.paintBrushEnabled) {
            if (!ExtraUtils.disablePaintbrushRecipe) {
                addRecipe(new ItemStack(ExtraUtils.paintBrush, 1), "S  ", " W ", "  W", 'S', Items.string, 'W', Items.stick);
            }
            for (final BlockColor b : ExtraUtils.colorblocks) {
                for (int i = 0; i < 16; ++i) {
                    if (b.oreName != null) {
                        OreDictionary.registerOre(b.oreName, new ItemStack((Block)b, 1, i));
                        OreDictionary.registerOre(b.oreName + XUHelper.dyes[i].substring(3), new ItemStack((Block)b, 1, i));
                    }
                    if (!ExtraUtils.disableColoredBlocksRecipes) {
                        if (b.customRecipe == null) {
                            addRecipe((IRecipe)new ShapedOreRecipe(new ItemStack((Block)b, 7, BlockColored.func_150032_b(i)), new Object[] { "SSS", "SDS", "SPS", 'S', b.baseBlock, 'D', XUHelper.dyes[i], 'P', ExtraUtils.paintBrush }));
                        }
                        else {
                            final Object[] tempRecipe = new Object[b.customRecipe.length];
                            for (int j = 0; j < b.customRecipe.length; ++j) {
                                if ("dye".equals(b.customRecipe[j])) {
                                    tempRecipe[j] = XUHelper.dyes[i];
                                }
                                else {
                                    tempRecipe[j] = b.customRecipe[j];
                                }
                            }
                            addRecipe((IRecipe)new ShapedOreRecipe(new ItemStack((Block)b, b.customRecipeNo, BlockColored.func_150032_b(i)), tempRecipe));
                        }
                    }
                }
            }
        }
        if (ExtraUtils.cobblestoneComprEnabled && !ExtraUtils.disableCompressedCobblestoneRecipe) {
            for (int k = 0; k < 16; ++k) {
                if (BlockCobblestoneCompressed.getCompr(k) == 0) {
                    final String s = BlockCobblestoneCompressed.getOreDictName(k).toLowerCase();
                    OreDictionary.registerOre(s, BlockCobblestoneCompressed.getBlock(k));
                    addRecipe((IRecipe)new ShapedOreRecipe(new ItemStack((Block)ExtraUtils.cobblestoneCompr, 1, k), new Object[] { "XXX", "XXX", "XXX", 'X', s }));
                    addRecipe(new ItemStack(BlockCobblestoneCompressed.getBlock(k), 9), "X", 'X', new ItemStack((Block)ExtraUtils.cobblestoneCompr, 1, k));
                }
                else {
                    addRecipe(new ItemStack((Block)ExtraUtils.cobblestoneCompr, 1, k), "XXX", "XXX", "XXX", 'X', new ItemStack((Block)ExtraUtils.cobblestoneCompr, 1, k - 1));
                    addRecipe(new ItemStack((Block)ExtraUtils.cobblestoneCompr, 9, k - 1), "X", 'X', new ItemStack((Block)ExtraUtils.cobblestoneCompr, 1, k));
                }
            }
        }
        if (ExtraUtils.conveyorEnabled && !ExtraUtils.disableConveyorRecipe) {
            addRecipe(new ItemStack(ExtraUtils.conveyor, 8), "TTT", "IRI", "TTT", 'T', Blocks.rail, 'I', Items.iron_ingot, 'R', Items.redstone);
        }
        if (ExtraUtils.filingCabinetEnabled) {
            if (!ExtraUtils.disableFilingCabinet) {
                addRecipe(new ItemStack((Block)ExtraUtils.filingCabinet, 1, 0), "ICI", "ICI", "ICI", 'I', Items.iron_ingot, 'C', Blocks.chest);
            }
            if (!ExtraUtils.disableAdvFilingCabinet && ExtraUtils.decorative1Enabled) {
                addRecipe(new ItemStack((Block)ExtraUtils.filingCabinet, 1, 1), "ICI", "ICI", "ICI", 'I', new ItemStack((Block)ExtraUtils.decorative1, 1, 8), 'C', new ItemStack((Block)ExtraUtils.filingCabinet, 1, 0));
            }
        }
        if (ExtraUtils.peacefultableEnabled && !ExtraUtils.disablePeacefulTableRecipe) {
            addRecipe(new ItemStack(ExtraUtils.peacefultable, 1), "EWE", "WDW", "EWE", 'E', Items.emerald, 'D', Items.ender_pearl, 'W', Blocks.planks);
        }
        if (ExtraUtils.tradingPostEnabled && !ExtraUtils.disableTradingPostRecipe) {
            addRecipe(new ItemStack(ExtraUtils.tradingPost, 1), "WEW", "WJW", "WWW", 'W', Blocks.planks, 'E', Blocks.emerald_block, 'J', Blocks.jukebox);
        }
        if (ExtraUtils.soundMufflerEnabled) {
            if (!ExtraUtils.disableSoundMufflerRecipe) {
                addRecipe(new ItemStack(ExtraUtils.soundMuffler, 1, 0), "WWW", "WNW", "WWW", 'W', Blocks.wool, 'N', Blocks.noteblock);
            }
            if (!ExtraUtils.disableRainMufflerRecipe) {
                addRecipe(new ItemStack(ExtraUtils.soundMuffler, 1, 1), "WWW", "WBW", "WWW", 'W', Blocks.wool, 'B', Items.water_bucket);
            }
        }
        if (ExtraUtils.transferNodeEnabled && ExtraUtils.transferPipeEnabled && ExtraUtils.transferNodeRemoteEnabled) {
            if (!ExtraUtils.disableTransferPipeRecipe) {
                addRecipe(new ItemStack((Block)ExtraUtils.transferPipe, 8), "SSS", "GRG", "SSS", 'S', new ItemStack((Block)Blocks.stone_slab, 6, 0), 'G', Blocks.glass, 'R', Items.redstone);
            }
            if (!ExtraUtils.disableSortingPipeRecipe) {
                addRecipe(new ItemStack((Block)ExtraUtils.transferPipe, 2, 8), "SSS", "G#G", "SSS", 'S', new ItemStack((Block)Blocks.stone_slab, 6, 0), 'G', Blocks.glass, '#', Items.gold_ingot);
            }
            if (!ExtraUtils.disableFilterPipeRecipe) {
                final ArrayList<ItemStack> dyes = new ArrayList<ItemStack>();
                for (final String s2 : new String[] { "dyeBlack", "dyeRed", "dyeGreen", "dyeBrown", "dyeBlue", "dyePurple", "dyeCyan", "dyeLightGray", "dyeGray", "dyePink", "dyeLime", "dyeYellow", "dyeLightBlue", "dyeMagenta", "dyeOrange", "dyeWhite" }) {
                    dyes.addAll(OreDictionary.getOres(s2));
                }
                addRecipe((IRecipe)new RecipeCustomOres(new ItemStack((Block)ExtraUtils.transferPipe, 5, 9), new ItemStack(Items.stick), dyes, new Object[] { "sPs", "PPP", "sPs", 's', new ItemStack(Items.stick), 'P', new ItemStack((Block)ExtraUtils.transferPipe, 1, 0) }));
            }
            if (!ExtraUtils.disableRationingPipeRecipe) {
                addRecipe((IRecipe)new ShapedOreRecipe(new ItemStack((Block)ExtraUtils.transferPipe, 2, 10), new Object[] { "SSS", "GBG", "SSS", 'B', new ItemStack(Items.dye, 1, 4), 'S', new ItemStack((Block)Blocks.stone_slab, 1, 0), 'G', Blocks.glass }));
                addRecipe((IRecipe)new ShapedOreRecipe(new ItemStack((Block)ExtraUtils.transferPipe2, 2, 0), new Object[] { "SSS", "GBG", "SSS", 'B', new ItemStack(Items.dye, 1, 4), 'S', new ItemStack(Blocks.stone_button, 1, 0), 'G', Blocks.glass }));
            }
            if (!ExtraUtils.disableEnergyPipeRecipe) {
                addRecipe((IRecipe)new ShapedOreRecipe(new ItemStack((Block)ExtraUtils.transferPipe, 8, 11), new Object[] { "SSS", "RRR", "SSS", 'R', Items.redstone, 'S', new ItemStack((Block)Blocks.stone_slab, 6, 0) }));
            }
            if (!ExtraUtils.disableCrossoverPipeRecipe) {
                addRecipe((IRecipe)new ShapedOreRecipe(new ItemStack((Block)ExtraUtils.transferPipe, 1, 12), new Object[] { " P ", "PBP", " P ", 'P', new ItemStack((Block)ExtraUtils.transferPipe, 1, 0), 'B', Items.redstone }));
            }
            if (!ExtraUtils.disableModSortingPipesRecipe) {
                addRecipe((IRecipe)new ShapedOreRecipe(new ItemStack((Block)ExtraUtils.transferPipe, 1, 13), new Object[] { "BPB", 'P', new ItemStack((Block)ExtraUtils.transferPipe, 1, 8), 'B', Items.redstone }));
            }
            if (!ExtraUtils.disableEnergyExtractionPipeRecipe) {
                addRecipe((IRecipe)new ShapedOreRecipe(new ItemStack((Block)ExtraUtils.transferPipe, 1, 14), new Object[] { "B B", "BPB", " P ", 'P', new ItemStack((Block)ExtraUtils.transferPipe, 1, 11), 'B', Items.gold_ingot }));
            }
            if (!ExtraUtils.disableTransferNodeRecipe) {
                addRecipe(new ItemStack(ExtraUtils.transferNode, 1, 0), " P ", "RER", "SHS", 'S', Blocks.stone, 'E', Blocks.redstone_block, 'H', Blocks.chest, 'R', Items.redstone, 'P', ExtraUtils.transferPipe);
                addRecipe(new ItemStack(ExtraUtils.transferNode, 4, 0), " P ", "RER", "SHS", 'S', Blocks.stone, 'E', Items.ender_pearl, 'H', Blocks.chest, 'R', Items.redstone, 'P', ExtraUtils.transferPipe);
            }
            if (!ExtraUtils.disableTransferNodeLiquidRecipe) {
                addRecipe(new ItemStack(ExtraUtils.transferNode, 1, 6), " P ", "LEL", "IHI", 'I', Items.iron_ingot, 'E', Blocks.redstone_block, 'H', Items.bucket, 'L', new ItemStack(Items.dye, 1, 4), 'P', ExtraUtils.transferPipe);
                addRecipe(new ItemStack(ExtraUtils.transferNode, 4, 6), " P ", "LEL", "IHI", 'I', Items.iron_ingot, 'E', Items.ender_pearl, 'H', Items.bucket, 'L', new ItemStack(Items.dye, 1, 4), 'P', ExtraUtils.transferPipe);
            }
            if (!ExtraUtils.disableTransferNodeRemoteRecipe) {
                addRecipe(new ItemStack(ExtraUtils.transferNodeRemote, 1, 0), " E ", "TeT", " E ", 'e', Items.emerald, 'E', Items.ender_pearl, 'T', new ItemStack(ExtraUtils.transferNode, 2, 0));
            }
            if (!ExtraUtils.disableTransferNodeLiquidRemoteRecipe) {
                addRecipe(new ItemStack(ExtraUtils.transferNodeRemote, 1, 6), " E ", "TeT", " E ", 'e', Items.diamond, 'E', Items.ender_pearl, 'T', new ItemStack(ExtraUtils.transferNode, 2, 6));
            }
            if (!ExtraUtils.disableTransferNodeEnergyRecipe) {
                addRecipe(new ItemStack(ExtraUtils.transferNode, 1, 12), "GNG", "NRN", "GNG", 'N', new ItemStack(ExtraUtils.transferNode, 1, 0), 'G', Items.gold_ingot, 'R', (ExtraUtils.nodeUpgrade == null) ? Blocks.redstone_block : new ItemStack((Item)ExtraUtils.nodeUpgrade, 1, 8));
                if (ExtraUtils.bedrockiumEnabled) {
                    addRecipe(new ItemStack(ExtraUtils.transferNode, 1, 13), " N ", "NBN", " N ", 'N', new ItemStack(ExtraUtils.transferNode, 1, 12), 'B', ExtraUtils.bedrockium);
                }
            }
        }
        if (ExtraUtils.nodeUpgradeEnabled) {
            if (!ExtraUtils.disableFilterRecipe) {
                addRecipe(new ItemStack((Item)ExtraUtils.nodeUpgrade, 1, 1), "RSR", "STS", "RSR", 'R', Items.redstone, 'S', Items.stick, 'T', Items.string);
                addRecipe((IRecipe)new RecipeFilterInvert(Item.getItemFromBlock(Blocks.redstone_torch), "Inverted", new ItemStack((Item)ExtraUtils.nodeUpgrade, 1, 1)));
                addRecipe((IRecipe)new RecipeFilterInvert(Item.getItemFromBlock(Blocks.wool), "FuzzyNBT", new ItemStack((Item)ExtraUtils.nodeUpgrade, 1, 1)));
                addRecipe((IRecipe)new RecipeFilterInvert(Items.stick, "FuzzyMeta", new ItemStack((Item)ExtraUtils.nodeUpgrade, 1, 1)));
                addRecipe(new ItemStack((Item)ExtraUtils.nodeUpgrade, 1, 10), "L L", " T ", "L L", 'L', "gemLapis", 'T', new ItemStack((Item)ExtraUtils.nodeUpgrade, 1, 1));
                addRecipe((IRecipe)new RecipeFilterInvert(Item.getItemFromBlock(Blocks.redstone_torch), "Inverted", new ItemStack((Item)ExtraUtils.nodeUpgrade, 1, 10)));
            }
            if (!ExtraUtils.disableNodeUpgradeSpeedRecipe) {
                addRecipe(new ItemStack((Item)ExtraUtils.nodeUpgrade, 4, 0), "RgR", "gGg", "RGR", 'R', Blocks.redstone_block, 'G', Items.gold_ingot, 'g', Items.gold_nugget);
                addRecipe(new ItemStack((Item)ExtraUtils.nodeUpgrade, 1, 2), "LiL", "iPi", "LiL", 'L', new ItemStack(Items.dye, 1, 4), 'i', Items.iron_ingot, 'P', Items.iron_pickaxe);
                addRecipe(new ItemStack((Item)ExtraUtils.nodeUpgrade, 1, 3), "GgG", "DuD", "GDG", 'u', new ItemStack((Item)ExtraUtils.nodeUpgrade, 1, 0), 'G', Items.gold_ingot, 'D', Items.diamond, 'g', Items.gold_nugget);
                addRecipe(new ItemStack((Item)ExtraUtils.nodeUpgrade, 1, 5), "ere", "qeq", "ere", 'e', Items.ender_pearl, 'r', Blocks.redstone_torch, 'q', Items.quartz);
                addRecipe(new ItemStack((Item)ExtraUtils.nodeUpgrade, 1, 6), "ere", "qeq", "eqe", 'e', Items.ender_pearl, 'r', Items.redstone, 'q', Items.quartz);
                addRecipe(new ItemStack((Item)ExtraUtils.nodeUpgrade, 3, 7), "sRR", "sGs", "GRR", 'R', Blocks.redstone_block, 'G', Items.gold_ingot, 'g', Items.gold_nugget, 's', new ItemStack((Item)ExtraUtils.nodeUpgrade, 4, 0));
                addRecipe(new ItemStack((Item)ExtraUtils.nodeUpgrade, 3, 8), "RRR", "sts", "RRR", 'R', Blocks.redstone_block, 'G', Items.gold_ingot, 'g', Items.gold_nugget, 's', new ItemStack((Item)ExtraUtils.nodeUpgrade, 4, 0), 't', new ItemStack((Item)ExtraUtils.nodeUpgrade, 4, 7));
                addRecipe(new ItemStack((Item)ExtraUtils.nodeUpgrade, 3, 9), "RgR", "RGg", "RgR", 'R', Blocks.redstone_block, 'G', Items.gold_ingot, 'g', Items.gold_nugget, 's', new ItemStack((Item)ExtraUtils.nodeUpgrade, 4, 0));
            }
        }
        if (ExtraUtils.trashCanEnabled && !ExtraUtils.disableTrashCanRecipe) {
            addRecipe(new ItemStack(ExtraUtils.trashCan, 1, 0), "SSS", "CHC", "CCC", 'S', Blocks.stone, 'C', Blocks.cobblestone, 'H', Blocks.chest);
            this.addShapelessRecipe(new ItemStack(ExtraUtils.trashCan, 1, 1), ExtraUtils.trashCan, Items.bucket);
            this.addShapelessRecipe(new ItemStack(ExtraUtils.trashCan, 1, 2), ExtraUtils.trashCan, Items.redstone, Items.gold_ingot, Items.redstone, Items.gold_ingot);
        }
        if (!ExtraUtils.disableSpikeRecipe) {
            if (ExtraUtils.spikeEnabled) {
                addRecipe(new ItemStack(ExtraUtils.spike.itemSpike, 4), " S ", "SIS", "ICI", 'S', Items.iron_sword, 'C', Blocks.iron_block, 'I', Items.iron_ingot);
            }
            if (ExtraUtils.spikeGoldEnabled) {
                addRecipe(new ItemStack(ExtraUtils.spikeGold.itemSpike, 4), " S ", "SIS", "ICI", 'S', Items.golden_sword, 'C', Blocks.gold_block, 'I', ExtraUtils.decorative1Enabled ? new ItemStack((Block)ExtraUtils.decorative1, 1, 8) : Items.gold_ingot);
            }
            if (ExtraUtils.spikeDiamondEnabled) {
                final ItemStack dSword = new ItemStack(Items.diamond_sword, 1);
                addRecipe(new ItemStack(ExtraUtils.spikeDiamond.itemSpike, 4), " S ", "SIS", "ICI", 'S', dSword, 'C', Blocks.diamond_block, 'I', ExtraUtils.spikeGoldEnabled ? ExtraUtils.spikeGold : Items.diamond);
            }
            if (ExtraUtils.spikeWoodEnabled) {
                addRecipe((IRecipe)new ShapedOreRecipe(new ItemStack(ExtraUtils.spikeWood.itemSpike, 4), new Object[] { " S ", "SIS", "ICI", 'S', Items.wooden_sword, 'C', "logWood", 'I', "plankWood" }));
            }
        }
        if (ExtraUtils.enderThermicPumpEnabled && !ExtraUtils.disableEnderThermicPumpRecipe) {
            if (!ExtraUtils.decorative1Enabled) {
                addRecipe(new ItemStack(ExtraUtils.enderThermicPump, 1), "ODO", "LEW", "OPO", 'O', Blocks.obsidian, 'D', Items.diamond, 'E', Items.ender_eye, 'P', Items.iron_pickaxe, 'L', Items.lava_bucket, 'W', Items.water_bucket);
                addRecipe(new ItemStack(ExtraUtils.enderThermicPump, 1), "ODO", "WEL", "OPO", 'O', Blocks.obsidian, 'D', Items.diamond, 'E', Items.ender_eye, 'P', Items.iron_pickaxe, 'L', Items.lava_bucket, 'W', Items.water_bucket);
            }
            else {
                addRecipe(new ItemStack(ExtraUtils.enderThermicPump, 1), "ODO", "LEW", "OPO", 'O', new ItemStack((Block)ExtraUtils.decorative1, 1, 1), 'D', Items.diamond, 'E', Items.ender_eye, 'P', Items.iron_pickaxe, 'L', Items.lava_bucket, 'W', Items.water_bucket);
                addRecipe(new ItemStack(ExtraUtils.enderThermicPump, 1), "ODO", "WEL", "OPO", 'O', new ItemStack((Block)ExtraUtils.decorative1, 1, 1), 'D', Items.diamond, 'E', Items.ender_eye, 'P', Items.iron_pickaxe, 'L', Items.lava_bucket, 'W', Items.water_bucket);
            }
        }
        if (ExtraUtils.enderQuarryEnabled && ExtraUtils.decorative1Enabled) {
            addRecipe((IRecipe)new ShapedOreRecipe(new ItemStack(ExtraUtils.enderQuarry), new Object[] { "EsE", "CDC", "pPp", 'E', new ItemStack((Block)ExtraUtils.decorative1, 1, 1), 's', "treeSapling", 'M', new ItemStack((Block)ExtraUtils.decorative1, 1, 8), 'C', new ItemStack((Block)ExtraUtils.decorative1, 1, 11), 'D', new ItemStack((Block)ExtraUtils.decorative1, 1, 12), 'P', Items.diamond_pickaxe, 'p', (ExtraUtils.enderThermicPump == null) ? new ItemStack((Block)ExtraUtils.decorative1, 1, 12) : ExtraUtils.enderThermicPump }));
            if (ExtraUtils.enderQuarryUpgradeEnabled && !ExtraUtils.disableEnderQuarryUpgradesRecipes) {
                TileEntityEnderQuarry.addUpgradeRecipes();
            }
        }
        if (ExtraUtils.enderMarkerEnabled && ExtraUtils.decorative1Enabled) {
            addRecipe(new ItemStack((Block)ExtraUtils.enderMarker), "E", "D", "D", 'E', Items.ender_pearl, 'D', new ItemStack((Block)ExtraUtils.decorative1, 1, 1));
        }
        if (ExtraUtils.timerBlockEnabled && !ExtraUtils.disableTimerBlockRecipe) {
            addRecipe(new ItemStack(ExtraUtils.timerBlock, 1), "RSR", "STS", "RSR", 'S', Blocks.stone, 'T', Blocks.redstone_torch, 'R', Items.redstone);
        }
        if (ExtraUtils.magnumTorchEnabled && !ExtraUtils.disableMagnumTorchRecipe) {
            if (ExtraUtils.chandelier != null) {
                addRecipe((IRecipe)new ShapedOreRecipe(new ItemStack(ExtraUtils.magnumTorch), new Object[] { "RCH", "CWC", "CWC", 'C', ExtraUtils.chandelier, 'W', "logWood", 'R', new ItemStack((Item)Items.potionitem, 1, 8225), 'H', new ItemStack((Item)Items.potionitem, 1, 8229) }));
            }
            else {
                addRecipe((IRecipe)new ShapedOreRecipe(new ItemStack(ExtraUtils.magnumTorch), new Object[] { "RCH", "CWC", "CWC", 'C', Items.diamond, 'W', "logWood", 'R', new ItemStack((Item)Items.potionitem, 1, 8225), 'H', new ItemStack((Item)Items.potionitem, 1, 8229) }));
            }
        }
        if (ExtraUtils.drumEnabled && !ExtraUtils.disableDrumRecipe) {
            addRecipe(new ItemStack(ExtraUtils.drum, 1, 0), "ISI", "ICI", "ISI", 'I', Items.iron_ingot, 'S', Blocks.heavy_weighted_pressure_plate, 'C', Items.cauldron);
            if (ExtraUtils.bedrockiumEnabled) {
                addRecipe(new ItemStack(ExtraUtils.drum, 1, 1), "ISI", "ICI", "ISI", 'I', ExtraUtils.bedrockium, 'S', Blocks.light_weighted_pressure_plate, 'C', Items.cauldron);
            }
        }
        if (ExtraUtils.generatorEnabled && !ExtraUtils.disableGeneratorRecipe) {
            BlockGenerator.addRecipes();
            if (ExtraUtils.generator2Enabled) {
                BlockGenerator.addUpgradeRecipes(ExtraUtils.generator, ExtraUtils.generator2);
                if (ExtraUtils.generator3Enabled) {
                    BlockGenerator.addSuperUpgradeRecipes(ExtraUtils.generator2, ExtraUtils.generator3);
                }
            }
        }
        if (ExtraUtils.goldenLassoEnabled && !ExtraUtils.disableGoldenLassoRecipe) {
            addRecipe(new ItemStack(ExtraUtils.goldenLasso, 1), "GSG", "SES", "GSG", 'G', Items.gold_nugget, 'S', Items.string, 'E', Items.ender_eye);
            addRecipe((IRecipe)new RecipeHorseTransmutation());
        }
        if (ExtraUtils.divisionSigilEnabled) {
            addRecipe((IRecipe)new RecipeUnsigil());
        }
        if (ExtraUtils.unstableIngotEnabled) {
            if (!ExtraUtils.disableUnstableIngotRecipe && ExtraUtils.divisionSigilEnabled) {
                addRecipe((IRecipe)new RecipeUnEnchanting());
                addRecipe(RecipeUnstableIngotCrafting.addRecipe(new ItemStack(ExtraUtils.unstableIngot), "I", "S", "D", 'I', Items.iron_ingot, 'S', ItemDivisionSigil.newActiveSigil(), 'D', Items.diamond));
                addRecipe(RecipeUnstableNuggetCrafting.addRecipe(new ItemStack(ExtraUtils.unstableIngot, 1, 1), "g", "S", "D", 'g', Items.gold_nugget, 'S', ItemDivisionSigil.newActiveSigil(), 'D', Items.diamond));
                final ItemStack item = new ItemStack(ExtraUtils.unstableIngot);
                final NBTTagCompound tags = new NBTTagCompound();
                tags.setBoolean("stable", true);
                item.setTagCompound(tags);
                addRecipe(item, "uuu", "uuu", "uuu", 'u', new ItemStack(ExtraUtils.unstableIngot, 1, 1));
            }
            if (ExtraUtils.decorative1Enabled) {
                addRecipe((IRecipe)RecipeUnstableCrafting.addRecipe(new ItemStack((Block)ExtraUtils.decorative1, 1, 5), "III", "III", "III", 'I', unstableIngot));
            }
        }
        if (ExtraUtils.portalEnabled && ExtraUtils.underdarkDimID != 0) {
            ItemStack u;
            if (ExtraUtils.unstableIngot != null) {
                u = new ItemStack(ExtraUtils.unstableIngot);
            }
            else {
                u = new ItemStack(Items.nether_star);
            }
            ItemStack b2;
            ItemStack b3;
            if (ExtraUtils.cobblestoneCompr != null) {
                b2 = new ItemStack((Block)ExtraUtils.cobblestoneCompr, 1, 4);
                b3 = new ItemStack((Block)ExtraUtils.cobblestoneCompr, 1, 3);
            }
            else {
                b2 = new ItemStack(Blocks.diamond_block);
                b3 = new ItemStack(Blocks.diamond_block);
            }
            addRecipe((IRecipe)RecipeUnstableCrafting.addRecipe(new ItemStack(ExtraUtils.portal, 1), "cNc", "NCN", "cNc", 'C', b2, 'c', b3, 'N', u));
        }
        if (ExtraUtils.portalEnabled && ExtraUtils.endoftimeDimID != 0) {
            final Object end_stone = (ExtraUtils.decorative1 == null) ? Blocks.end_stone : new ItemStack((Block)ExtraUtils.decorative1, 1, 14);
            final Object obsidian = (ExtraUtils.decorative1 == null) ? Blocks.obsidian : "burntQuartz";
            addRecipe((IRecipe)new ShapedOreRecipe(new ItemStack(ExtraUtils.portal, 1, 2), new Object[] { "PEP", "ECE", "PEP", 'C', Items.clock, 'E', end_stone, 'P', obsidian }));
        }
        if (ExtraUtils.buildersWandEnabled && !ExtraUtils.disableBuildersWandRecipe && ExtraUtils.unstableIngotEnabled) {
            final RecipeUnstableCrafting recipe = RecipeUnstableCrafting.addRecipe(new ItemStack(ExtraUtils.buildersWand, 1), " I", "S ", 'I', unstableIngot, 'S', Blocks.obsidian);
            if (ExtraUtils.creativeBuildersWandEnabled) {
                recipe.setStable(new ItemStack(ExtraUtils.creativeBuildersWand));
            }
            addRecipe((IRecipe)recipe);
        }
        if (ExtraUtils.ethericSwordEnabled && !ExtraUtils.disableEthericSwordRecipe && ExtraUtils.unstableIngotEnabled) {
            addRecipe((IRecipe)RecipeUnstableCrafting.addRecipe(new ItemStack(ExtraUtils.ethericSword, 1), "I", "I", "S", 'I', unstableIngot, 'S', Blocks.obsidian).setStableItem(ExtraUtils.lawSword));
        }
        if (ExtraUtils.erosionShovelEnabled && !ExtraUtils.disableErosionShovelRecipe && ExtraUtils.unstableIngotEnabled) {
            addRecipe((IRecipe)RecipeUnstableCrafting.addRecipe(new ItemStack(ExtraUtils.erosionShovel, 1), "I", "S", "S", 'I', unstableIngot, 'S', Blocks.obsidian).addStableEnchant(Enchantment.efficiency, 10));
        }
        if (ExtraUtils.destructionPickaxeEnabled && !ExtraUtils.disableDestructionPickaxeRecipe && ExtraUtils.unstableIngotEnabled) {
            addRecipe((IRecipe)RecipeUnstableCrafting.addRecipe(new ItemStack(ExtraUtils.destructionPickaxe, 1), "III", " S ", " S ", 'I', unstableIngot, 'S', Blocks.obsidian).addStableEnchant(Enchantment.efficiency, 10));
        }
        if (ExtraUtils.healingAxeEnabled && !ExtraUtils.disableHealingAxeRecipe && ExtraUtils.unstableIngotEnabled) {
            addRecipe((IRecipe)RecipeUnstableCrafting.addRecipe(new ItemStack(ExtraUtils.healingAxe, 1), "II", "IS", " S", 'I', unstableIngot, 'S', Blocks.obsidian).addStableEnchant(Enchantment.efficiency, 10));
        }
        if (ExtraUtils.temporalHoeEnabled && !ExtraUtils.disableTemporalHoeRecipe && ExtraUtils.unstableIngotEnabled) {
            addRecipe((IRecipe)RecipeUnstableCrafting.addRecipe(new ItemStack(ExtraUtils.temporalHoe, 1), "II", " S", " S", 'I', unstableIngot, 'S', Blocks.obsidian));
        }
        if (ExtraUtils.sonarGogglesEnabled && !ExtraUtils.disableSonarGogglesRecipe && ExtraUtils.unstableIngotEnabled) {
            addRecipe((IRecipe)RecipeUnstableCrafting.addRecipe(new ItemStack(ExtraUtils.sonarGoggles, 1), "III", "EIE", 'I', unstableIngot, 'E', Items.ender_eye));
        }
        if (ExtraUtils.greenScreen != null) {
            addRecipe((IRecipe)RecipeUnstableCrafting.addRecipe(new ItemStack((Block)ExtraUtils.greenScreen, 4, 8), "GGG", "GIG", "GGG", 'I', unstableIngot, 'G', Blocks.stonebrick));
            if (ExtraUtils.colorBlockBrick != null) {
                for (int k = 0; k < 16; ++k) {
                    addRecipe((IRecipe)RecipeUnstableCrafting.addRecipe(new ItemStack((Block)ExtraUtils.greenScreen, 4, k), "GGG", "GIG", "GGG", 'I', unstableIngot, 'G', new ItemStack((Block)ExtraUtils.colorBlockBrick, 1, k)));
                }
            }
            final String[] dyes2 = { "Black", "Red", "Green", "Brown", "Blue", "Purple", "Cyan", "LightGray", "Gray", "Pink", "Lime", "Yellow", "LightBlue", "Magenta", "Orange", "White" };
            for (int l = 0; l < 16; ++l) {
                addRecipe((IRecipe)new ShapelessOreRecipe(new ItemStack((Block)ExtraUtils.greenScreen, 1, l), new Object[] { new ItemStack((Block)ExtraUtils.greenScreen, 1, 32767), "dye" + dyes2[15 - l] }));
            }
        }
        if (ExtraUtils.etherealBlockEnabled && !ExtraUtils.disableEtherealBlockRecipe && ExtraUtils.unstableIngotEnabled) {
            addRecipe((IRecipe)RecipeUnstableCrafting.addRecipe(new ItemStack(ExtraUtils.etheralBlock, 4, 0), "GGG", "GIG", "GGG", 'I', unstableIngot, 'G', Blocks.glass));
            if (ExtraUtils.decorative2Enabled) {
                addRecipe((IRecipe)RecipeUnstableCrafting.addRecipe(new ItemStack(ExtraUtils.etheralBlock, 4, 1), "GGG", "GIG", "GGG", 'I', unstableIngot, 'G', new ItemStack((Block)ExtraUtils.decorative2, 1, 0)));
                addRecipe((IRecipe)RecipeUnstableCrafting.addRecipe(new ItemStack(ExtraUtils.etheralBlock, 4, 2), "GGG", "GIG", "GGG", 'I', unstableIngot, 'G', new ItemStack((Block)ExtraUtils.decorative2, 1, 10)));
            }
            for (int k = 0; k < 3; ++k) {
                this.addShapelessRecipe(new ItemStack(ExtraUtils.etheralBlock, 1, k + 3), new ItemStack(ExtraUtils.etheralBlock, 1, k), Blocks.redstone_torch);
            }
        }
        if (ExtraUtils.wateringCanEnabled) {
            if (!ExtraUtils.disableWateringCanRecipe) {
                addRecipe(new ItemStack((Item)ExtraUtils.wateringCan, 1, 1), "SB ", "SWS", " S ", 'S', Items.iron_ingot, 'B', new ItemStack(Items.dye, 1, 15), 'W', Items.bowl);
                addRecipe((IRecipe)RecipeDifficultySpecific.addRecipe(new boolean[] { true, false, false, false }, new ItemStack((Item)ExtraUtils.wateringCan, 1, 1), new String[] { "Peaceful Mode only" }, "S  ", "SWS", " S ", 'S', Blocks.stone, 'W', Items.bowl));
            }
            if (!ExtraUtils.disableSuperWateringCanRecipe && ExtraUtils.soulEnabled && ExtraUtils.bedrockiumEnabled) {
                addRecipe(new ItemStack((Item)ExtraUtils.wateringCan, 1, 3), "SB ", "SWS", " S ", 'S', ExtraUtils.bedrockium, 'B', ExtraUtils.soul, 'W', Items.bowl);
            }
        }
        if (ExtraUtils.scannerEnabled) {
            addRecipe(new ItemStack(ExtraUtils.scanner), "III", "ERI", "III", 'E', Items.ender_eye, 'R', Items.redstone, 'I', Items.iron_ingot);
        }
        if (ExtraUtils.goldenBagEnabled && !ExtraUtils.disableGoldenBagRecipe) {
            addRecipe(new ItemStack(ExtraUtils.goldenBag, 1), "WdW", "gCg", "WGW", 'W', new ItemStack(Blocks.wool, 1, 32767), 'd', Items.diamond, 'g', Items.gold_ingot, 'B', Items.blaze_powder, 'G', Blocks.gold_block, 'C', Blocks.chest);
            addRecipe((IRecipe)new RecipeBagDye());
            if (ExtraUtils.decorative1Enabled) {
                addRecipe(RecipeGBEnchanting.addRecipe(new ItemStack(ExtraUtils.goldenBag, 1), "WGW", 'd', Items.diamond, 'G', new ItemStack(ExtraUtils.goldenBag, 1), 'W', new ItemStack((Block)ExtraUtils.decorative1, 1, 8)));
            }
        }
        if (ExtraUtils.bedrockiumEnabled) {
            addRecipe(new ItemStack((Item)ExtraUtils.bedrockium, 1), "IcI", "cdc", "IcI", 'I', new ItemStack((Block)ExtraUtils.cobblestoneCompr, 1, 2), 'd', Blocks.diamond_block, 'c', new ItemStack((Block)ExtraUtils.cobblestoneCompr, 1, 3));
        }
        if (ExtraUtils.qedEnabled) {
            addRecipe(new ItemStack((Block)ExtraUtils.qed, 1, 0), "RcR", "EdE", "EEE", 'R', Items.ender_eye, 'c', Blocks.crafting_table, 'd', ExtraUtils.decorative1Enabled ? new ItemStack((Block)ExtraUtils.decorative1, 1, 12) : Blocks.diamond_block, 'E', ExtraUtils.decorative1Enabled ? new ItemStack((Block)ExtraUtils.decorative1, 1, 1) : Blocks.obsidian);
            addRecipe(new ItemStack((Block)ExtraUtils.qed, 1, 2), " e ", " E ", "EEE", 'e', Items.ender_eye, 'd', Items.diamond, 'E', ExtraUtils.decorative1Enabled ? new ItemStack((Block)ExtraUtils.decorative1, 1, 1) : Blocks.obsidian);
        }
        if (!ExtraUtils.disableWitherRecipe) {
            final ItemStack t2 = new ItemStack(Items.nether_star, 1);
            addRecipe((IRecipe)RecipeDifficultySpecific.addRecipe(new boolean[] { true, false, false, false }, t2, new String[] { "Peaceful Mode only" }, "WWW", "SSS", "DSB", 'W', new ItemStack(Items.skull, 1, 1), 'S', Blocks.soul_sand, 'D', Items.diamond_sword, 'B', Items.bow));
        }
        if (ExtraUtils.precisionShearsEnabled && !ExtraUtils.disablePrecisionShears && ExtraUtils.unstableIngotEnabled) {
            addRecipe((IRecipe)RecipeUnstableCrafting.addRecipe(new ItemStack((Item)ExtraUtils.precisionShears), "AI", "IA", 'I', unstableIngot, 'B', new ItemStack((Block)ExtraUtils.decorative1, 1, 5), 'A', (ExtraUtils.angelBlock != null) ? ExtraUtils.angelBlock : Blocks.obsidian).addStableEnchant(Enchantment.unbreaking, 10));
        }
        if (ExtraUtils.angelRingEnabled && ExtraUtils.unstableIngotEnabled) {
            final Object[] leftWing = { Blocks.glass, Items.feather, new ItemStack(Items.dye, 1, 5), Items.leather, Items.gold_nugget };
            final Object[] rightWing = { Blocks.glass, Items.feather, new ItemStack(Items.dye, 1, 9), Items.leather, Items.gold_nugget };
            for (int i = 0; i < leftWing.length; ++i) {
                if (ExtraUtils.decorative1Enabled) {
                    addRecipe((IRecipe)RecipeUnstableCrafting.addRecipe(new ItemStack((Item)ExtraUtils.angelRing, 1, i), "LGR", "GNG", "IGI", 'I', unstableIngot, 'G', Items.gold_ingot, 'N', Items.nether_star, 'L', leftWing[i], 'R', rightWing[i]));
                }
                this.addShapelessRecipe(new ItemStack((Item)ExtraUtils.angelRing, 1, i), leftWing[i], new ItemStack((Item)ExtraUtils.angelRing, 1, 32767), rightWing[i]);
            }
        }
        if (!ExtraUtils.disableChestRecipe) {
            GameRegistry.addRecipe((IRecipe)new ShapedOreRecipeAlwaysLast(new ItemStack((Block)Blocks.chest, 4), new Object[] { "WWW", "W W", "WWW", 'W', "logWood" }));
        }
        EE3Integration.addEMCRecipes();
    }
    
    public void addSmelting(final ItemStack ingredient, final ItemStack result, final int experience) {
        GameRegistry.addSmelting(ingredient, result, (float)experience);
    }
    
    public void addShapedRecipe(final ItemStack out, final Object... ingreds) {
        GameRegistry.addShapedRecipe(out, ingreds);
    }
    
    public void addShapelessRecipe(final ItemStack out, final Object... ingreds) {
        GameRegistry.addShapelessRecipe(out, ingreds);
    }
    
    public void registerRecipe(final Class<? extends IRecipe> recipe, final RecipeSorter.Category cat, final String s) {
        if (ExtraUtils.registeredRecipes.contains(recipe)) {
            return;
        }
        ExtraUtils.registeredRecipes.add(recipe);
        RecipeSorter.register("extrautils:" + recipe.getSimpleName(), (Class)recipe, cat, s);
    }
    
    public void serverStart(final FMLServerStartingEvent event) {
        ExtraUtilsMod.proxy.newServerStart();
    }
    
    public void remap(final FMLMissingMappingsEvent event) {
    }
    
    public void loadComplete(final FMLLoadCompleteEvent event) {
        if (!this.hasSpecialInit) {
            this.hasSpecialInit = true;
            this.specialInit();
        }
    }
    
    static {
        spikeSwords = new ArrayList<Item>();
        ExtraUtils.allNonVanillaDimensionsValidForEnderPump = false;
        ExtraUtils.colorblocks = new ArrayList<BlockColor>();
        ExtraUtils.creativeTabIcon = null;
        ExtraUtils.disableCrossoverPipeRecipe = false;
        ExtraUtils.disableEnergyPipeRecipe = false;
        ExtraUtils.disableGeneratorRecipe = false;
        ExtraUtils.disableModSortingPipesRecipe = false;
        ExtraUtils.disablePortalTexture = false;
        ExtraUtils.handlesClientMethods = false;
        ExtraUtils.microBlocks = null;
        ExtraUtils.unstableIngotExplosion = true;
        ExtraUtils.qedList = new HashSet<String>();
        ExtraUtils.registeredRecipes = new HashSet<Class<? extends IRecipe>>();
    }
    
    public class ChunkloadCallback implements ForgeChunkManager.OrderedLoadingCallback
    {
        public void ticketsLoaded(final List<ForgeChunkManager.Ticket> tickets, final World world) {
            for (final ForgeChunkManager.Ticket ticket : tickets) {
                final String ticket_id = ticket.getModData().getString("id");
                if (ticket_id.equals("pump")) {
                    final int pumpX = ticket.getModData().getInteger("pumpX");
                    final int pumpY = ticket.getModData().getInteger("pumpY");
                    final int pumpZ = ticket.getModData().getInteger("pumpZ");
                    final TileEntityEnderThermicLavaPump tq = (TileEntityEnderThermicLavaPump)world.getTileEntity(pumpX, pumpY, pumpZ);
                    tq.forceChunkLoading(ticket);
                }
                if (ticket_id.equals("quarry")) {
                    final int x = ticket.getModData().getInteger("x");
                    final int y = ticket.getModData().getInteger("y");
                    final int z = ticket.getModData().getInteger("z");
                    final TileEntityEnderQuarry tq2 = (TileEntityEnderQuarry)world.getTileEntity(x, y, z);
                    tq2.forceChunkLoading(ticket);
                }
            }
        }
        
        public List<ForgeChunkManager.Ticket> ticketsLoaded(final List<ForgeChunkManager.Ticket> tickets, final World world, final int maxTicketCount) {
            final List<ForgeChunkManager.Ticket> validTickets = (List<ForgeChunkManager.Ticket>)Lists.newArrayList();
            for (final ForgeChunkManager.Ticket ticket : tickets) {
                final String ticket_id = ticket.getModData().getString("id");
                if (ticket_id.equals("pump") && ExtraUtils.enderThermicPump != null) {
                    final int pumpX = ticket.getModData().getInteger("pumpX");
                    final int pumpY = ticket.getModData().getInteger("pumpY");
                    final int pumpZ = ticket.getModData().getInteger("pumpZ");
                    final Block blId = world.getBlock(pumpX, pumpY, pumpZ);
                    if (blId == ExtraUtils.enderThermicPump) {
                        validTickets.add(ticket);
                    }
                }
                if (ticket_id.equals("quarry") && ExtraUtils.enderQuarry != null && !TileEntityEnderQuarry.disableSelfChunkLoading) {
                    final int x = ticket.getModData().getInteger("x");
                    final int y = ticket.getModData().getInteger("y");
                    final int z = ticket.getModData().getInteger("z");
                    final Block blId = world.getBlock(x, y, z);
                    if (blId != ExtraUtils.enderQuarry) {
                        continue;
                    }
                    validTickets.add(ticket);
                }
            }
            return validTickets;
        }
    }
}


