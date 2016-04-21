// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.nei;

import net.minecraft.nbt.NBTTagCompound;
import java.util.List;
import com.rwtema.extrautils.multipart.FMPBase;
import codechicken.nei.PositionedStack;
import java.util.Collection;
import codechicken.microblock.MicroRecipe;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.recipe.TemplateRecipeHandler;
import java.awt.Rectangle;
import codechicken.microblock.MicroMaterialRegistry;
import java.util.Iterator;
import codechicken.microblock.Saw;
import net.minecraft.item.Item;
import codechicken.nei.ItemList;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.recipe.RecipeInfo;
import net.minecraft.inventory.Container;
import net.minecraft.client.gui.inventory.GuiContainer;
import java.util.HashSet;
import net.minecraft.item.crafting.ShapedRecipes;
import java.util.ArrayList;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import codechicken.nei.recipe.ShapedRecipeHandler;

@SideOnly(Side.CLIENT)
public class FMPMicroBlocksHandler extends ShapedRecipeHandler
{
    public static String[] currentMaterials;
    public static ItemStack[] currentBlocks;
    public static ArrayList<ShapedRecipes> recipes;
    public String currentMaterial;
    public ItemStack currentBlock;
    public boolean scroll;
    public static HashSet<ItemStack> sawList;
    public static final String identifier = "microblocks";
    
    public FMPMicroBlocksHandler() {
        this.currentMaterial = "";
        this.currentBlock = null;
        this.scroll = true;
    }
    
    public boolean hasOverlay(final GuiContainer gui, final Container container, final int recipe) {
        return RecipeInfo.hasDefaultOverlay(gui, "microblocks") || RecipeInfo.hasOverlayHandler(gui, "microblocks") || (this.isRecipe2x2(recipe) && RecipeInfo.hasDefaultOverlay(gui, "microblocks2x2")) || super.hasOverlay(gui, container, recipe);
    }
    
    public IOverlayHandler getOverlayHandler(final GuiContainer gui, final int recipe) {
        IOverlayHandler handler = RecipeInfo.getOverlayHandler(gui, "microblocks");
        if (handler != null) {
            return handler;
        }
        if (this.isRecipe2x2(recipe)) {
            handler = RecipeInfo.getOverlayHandler(gui, "microblocks2x2");
            if (handler != null) {
                return handler;
            }
        }
        return super.getOverlayHandler(gui, recipe);
    }
    
    public static HashSet<ItemStack> getSawList() {
        if (FMPMicroBlocksHandler.sawList == null) {
            FMPMicroBlocksHandler.sawList = new HashSet<ItemStack>();
            synchronized (ItemList.class) {
                for (final Item item : ItemList.itemMap.keySet()) {
                    if (item instanceof Saw) {
                        for (final ItemStack stack : ItemList.itemMap.get((Object)item)) {
                            FMPMicroBlocksHandler.sawList.add(stack);
                        }
                    }
                }
            }
        }
        return FMPMicroBlocksHandler.sawList;
    }
    
    public ItemStack[] getSaws() {
        if (this.scroll || "".equals(this.currentMaterial) || this.currentBlock == null) {
            return getSawList().toArray(new ItemStack[0]);
        }
        final int p = MicroMaterialRegistry.getMaterial(this.currentMaterial).getCutterStrength();
        final HashSet<ItemStack> s = new HashSet<ItemStack>();
        for (final ItemStack saw : getSawList()) {
            final int sawStrength = ((Saw)saw.getItem()).getCuttingStrength(saw);
            if (sawStrength >= p || sawStrength == MicroMaterialRegistry.getMaxCuttingStrength()) {
                s.add(saw);
            }
        }
        return s.toArray(new ItemStack[s.size()]);
    }
    
    public static ArrayList<ShapedRecipes> getCraftingRecipes() {
        if (FMPMicroBlocksHandler.recipes == null || FMPMicroBlocksHandler.recipes.size() == 0) {
            FMPMicroBlocksHandler.recipes = FMPMicroBlockRecipeCreator.loadRecipes();
        }
        return FMPMicroBlocksHandler.recipes;
    }
    
    public void loadTransferRects() {
        this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(84, 23, 24, 18), "microblocks", new Object[0]));
    }
    
    public void loadCraftingRecipes(final String outputId, final Object... results) {
        if (outputId.equals("microblocks")) {
            for (final ShapedRecipes irecipe : getCraftingRecipes()) {
                final MicroblockCachedRecipe recipe = new MicroblockCachedRecipe(irecipe);
                recipe.computeVisuals();
                this.arecipes.add(recipe);
            }
            this.scroll = true;
            this.currentMaterial = "";
            this.currentBlock = null;
        }
        else {
            super.loadCraftingRecipes(outputId, results);
        }
    }
    
    public void loadCraftingRecipes(final ItemStack result) {
        if (!result.hasTagCompound() || "".equals(result.getTagCompound().getString("mat"))) {
            return;
        }
        final MicroMaterialRegistry.IMicroMaterial m = MicroMaterialRegistry.getMaterial(result.getTagCompound().getString("mat"));
        if (m == null) {
            return;
        }
        this.scroll = false;
        this.currentMaterial = result.getTagCompound().getString("mat");
        this.currentBlock = m.getItem().copy();
        for (final ShapedRecipes irecipe : getCraftingRecipes()) {
            if (NEIServerUtils.areStacksSameTypeCrafting(irecipe.getRecipeOutput(), result)) {
                final MicroblockCachedRecipe recipe = new MicroblockCachedRecipe(irecipe);
                recipe.computeVisuals();
                this.arecipes.add(recipe);
            }
        }
    }
    
    public void loadUsageRecipes(final ItemStack ingredient) {
        if (ingredient.getItem() instanceof Saw) {
            this.scroll = true;
            this.currentMaterial = "";
            this.currentBlock = null;
        }
        else if (!ingredient.hasTagCompound() || "".equals(ingredient.getTagCompound().getString("mat"))) {
            final int id = MicroRecipe.findMaterial(ingredient);
            if (id < 0) {
                return;
            }
            final MicroMaterialRegistry.IMicroMaterial m = MicroMaterialRegistry.getMaterial(id);
            if (m == null) {
                return;
            }
            this.scroll = false;
            this.currentMaterial = MicroMaterialRegistry.materialName(id);
            this.currentBlock = m.getItem().copy();
        }
        else {
            final MicroMaterialRegistry.IMicroMaterial m = MicroMaterialRegistry.getMaterial(ingredient.getTagCompound().getString("mat"));
            if (m == null) {
                return;
            }
            this.scroll = false;
            this.currentMaterial = ingredient.getTagCompound().getString("mat");
            this.currentBlock = m.getItem().copy();
        }
        for (final ShapedRecipes irecipe : getCraftingRecipes()) {
            final MicroblockCachedRecipe recipe = new MicroblockCachedRecipe(irecipe);
            recipe.computeVisuals();
            if (recipe.contains((Collection)recipe.ingredients, ingredient)) {
                recipe.setIngredientPermutation((Collection)recipe.ingredients, ingredient);
                this.arecipes.add(recipe);
            }
        }
    }
    
    public String getGuiTexture() {
        return "textures/gui/container/crafting_table.png";
    }
    
    public String getRecipeName() {
        return "Microblock Crafting";
    }
    
    static {
        FMPMicroBlocksHandler.currentMaterials = null;
        FMPMicroBlocksHandler.currentBlocks = null;
        FMPMicroBlocksHandler.recipes = null;
        FMPMicroBlocksHandler.sawList = null;
    }
    
    public class MicroblockCachedRecipe extends TemplateRecipeHandler.CachedRecipe
    {
        public ArrayList<PositionedStack> ingredients;
        public MicroblockPositionedStack result;
        
        public MicroblockCachedRecipe(final int width, final int height, final Object[] items, final ItemStack out) {
            super((TemplateRecipeHandler)FMPMicroBlocksHandler.this);
            this.result = new MicroblockPositionedStack(out, 119, 24);
            this.ingredients = new ArrayList<PositionedStack>();
            this.setIngredients(width, height, items);
        }
        
        public MicroblockCachedRecipe(final ShapedRecipes irecipe) {
            this(irecipe.recipeWidth, irecipe.recipeHeight, irecipe.recipeItems, irecipe.getRecipeOutput());
        }
        
        public void setIngredients(final int width, final int height, final Object[] items) {
            for (int x = 0; x < width; ++x) {
                for (int y = 0; y < height; ++y) {
                    if (y * width + x <= items.length) {
                        if (items[y * width + x] != null) {
                            final ItemStack item = (ItemStack)items[y * width + x];
                            PositionedStack stack;
                            if (item.getItem() == FMPMicroBlockRecipeCreator.stone.getItem()) {
                                stack = new MicroblockPositionedStack(item, 25 + x * 18, 6 + y * 18);
                            }
                            else if (item == FMPMicroBlockRecipeCreator.saw) {
                                stack = new PositionedStack((Object)FMPMicroBlocksHandler.this.getSaws(), 25 + x * 18, 6 + y * 18, false);
                            }
                            else if (item.getItem() == FMPBase.getMicroBlockItemId()) {
                                stack = new MicroblockPositionedStack((ItemStack)items[y * width + x], 25 + x * 18, 6 + y * 18);
                            }
                            else {
                                stack = new PositionedStack(items[y * width + x], 25 + x * 18, 6 + y * 18);
                            }
                            stack.setMaxSize(1);
                            this.ingredients.add(stack);
                        }
                    }
                }
            }
        }
        
        public void permMaterial() {
            if (!FMPMicroBlocksHandler.this.scroll) {
                return;
            }
            if (FMPMicroBlocksHandler.currentMaterials == null) {
                FMPMicroBlocksHandler.currentMaterials = new String[MicroMaterialRegistry.getIdMap().length];
                for (int i = 0; i < MicroMaterialRegistry.getIdMap().length; ++i) {
                    FMPMicroBlocksHandler.currentMaterials[i] = (String)MicroMaterialRegistry.getIdMap()[i]._1();
                }
            }
            if (FMPMicroBlocksHandler.currentBlocks == null) {
                FMPMicroBlocksHandler.currentBlocks = new ItemStack[MicroMaterialRegistry.getIdMap().length];
                for (int i = 0; i < MicroMaterialRegistry.getIdMap().length; ++i) {
                    FMPMicroBlocksHandler.currentBlocks[i] = ((MicroMaterialRegistry.IMicroMaterial)MicroMaterialRegistry.getIdMap()[i]._2()).getItem().copy();
                }
            }
            FMPMicroBlocksHandler.this.currentMaterial = FMPMicroBlocksHandler.currentMaterials[FMPMicroBlocksHandler.this.cycleticks / 20 % FMPMicroBlocksHandler.currentMaterials.length];
            FMPMicroBlocksHandler.this.currentBlock = FMPMicroBlocksHandler.currentBlocks[FMPMicroBlocksHandler.this.cycleticks / 20 % FMPMicroBlocksHandler.currentMaterials.length];
        }
        
        public List<PositionedStack> getIngredients() {
            return (List<PositionedStack>)this.getCycledIngredients(FMPMicroBlocksHandler.this.cycleticks / 20, (List)this.ingredients);
        }
        
        public void randomRenderPermutation(final PositionedStack stack, final long cycle) {
            super.randomRenderPermutation(stack, cycle);
        }
        
        public PositionedStack getResult() {
            this.result.setPermutationToRender(0);
            return this.result;
        }
        
        public void computeVisuals() {
            for (final PositionedStack p : this.ingredients) {
                p.generatePermutations();
            }
            this.result.generatePermutations();
        }
        
        public class MicroblockPositionedStack extends PositionedStack
        {
            boolean materialTag;
            int stacksize;
            
            public MicroblockPositionedStack(final ItemStack object, final int x, final int y) {
                super((Object)object, x, y, true);
                this.materialTag = false;
                this.stacksize = object.stackSize;
                this.item = this.items[0].copy();
                this.materialTag = (this.item.getItem() == FMPBase.getMicroBlockItemId() && this.item.getItemDamage() != 0 && this.item.getItemDamage() != 8);
                this.setPermutationToRender(0);
            }
            
            public void generatePermutations() {
            }
            
            public void setItem(final ItemStack item) {
                if (item != null) {
                    this.item = item.copy();
                    this.items[0] = item.copy();
                }
                else {
                    this.item = null;
                }
            }
            
            public void setPermutationToRender(final int index) {
                if (this.item == null) {
                    return;
                }
                MicroblockCachedRecipe.this.permMaterial();
                if (this.materialTag) {
                    this.addMaterial();
                }
                else {
                    this.items[0] = FMPMicroBlocksHandler.this.currentBlock.copy();
                    this.items[0].stackSize = this.stacksize;
                    this.item = this.items[0];
                }
                super.setPermutationToRender(0);
            }
            
            public void addMaterial() {
                NBTTagCompound tag = this.items[0].getTagCompound();
                if (tag == null) {
                    tag = new NBTTagCompound();
                }
                tag.setString("mat", FMPMicroBlocksHandler.this.currentMaterial);
                this.items[0].setTagCompound(tag);
            }
        }
    }
}

