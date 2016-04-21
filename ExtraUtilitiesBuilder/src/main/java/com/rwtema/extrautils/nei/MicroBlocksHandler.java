// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.nei;

import net.minecraft.nbt.NBTTagCompound;
import com.rwtema.extrautils.multipart.FMPBase;
import codechicken.nei.PositionedStack;
import java.util.ArrayList;
import java.util.Collection;
import codechicken.nei.NEIServerUtils;
import codechicken.microblock.MicroMaterialRegistry;
import codechicken.nei.recipe.TemplateRecipeHandler;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.List;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.CraftingManager;
import java.util.HashSet;
import com.rwtema.extrautils.multipart.microblock.RecipeMicroBlocks;
import java.util.Set;
import net.minecraft.item.ItemStack;
import codechicken.nei.recipe.ShapedRecipeHandler;

public class MicroBlocksHandler extends ShapedRecipeHandler
{
    public static String[] currentMaterials;
    public static ItemStack[] currentBlocks;
    public static Set<RecipeMicroBlocks> recipes;
    public String currentMaterial;
    public ItemStack currentBlock;
    public boolean scroll;
    
    public MicroBlocksHandler() {
        this.currentMaterial = "";
        this.currentBlock = null;
        this.scroll = true;
    }
    
    public static Set<RecipeMicroBlocks> getCraftingRecipes() {
        if (MicroBlocksHandler.recipes == null) {
            MicroBlocksHandler.recipes = new HashSet<RecipeMicroBlocks>();
            final List<IRecipe> allrecipes = (List<IRecipe>)CraftingManager.getInstance().getRecipeList();
            for (final IRecipe irecipe : allrecipes) {
                if (irecipe instanceof RecipeMicroBlocks) {
                    MicroBlocksHandler.recipes.add((RecipeMicroBlocks)irecipe);
                }
            }
        }
        return MicroBlocksHandler.recipes;
    }
    
    public void loadTransferRects() {
        this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(84, 23, 24, 18), "xu_microblocks_crafting", new Object[0]));
    }
    
    public void loadCraftingRecipes(final String outputId, final Object... results) {
        if (outputId.equals("xu_microblocks_crafting")) {
            for (final RecipeMicroBlocks irecipe : getCraftingRecipes()) {
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
        for (final RecipeMicroBlocks irecipe : getCraftingRecipes()) {
            if (NEIServerUtils.areStacksSameTypeCrafting(irecipe.getRecipeOutput(), result)) {
                final MicroblockCachedRecipe recipe = new MicroblockCachedRecipe(irecipe);
                recipe.computeVisuals();
                this.arecipes.add(recipe);
            }
        }
    }
    
    public void loadUsageRecipes(final ItemStack ingredient) {
        if (!ingredient.hasTagCompound() || "".equals(ingredient.getTagCompound().getString("mat"))) {
            return;
        }
        final MicroMaterialRegistry.IMicroMaterial m = MicroMaterialRegistry.getMaterial(ingredient.getTagCompound().getString("mat"));
        if (m == null) {
            return;
        }
        this.scroll = false;
        this.currentMaterial = ingredient.getTagCompound().getString("mat");
        this.currentBlock = m.getItem().copy();
        for (final RecipeMicroBlocks irecipe : getCraftingRecipes()) {
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
        return "Extra Utilities: Microblocks";
    }
    
    static {
        MicroBlocksHandler.currentMaterials = null;
        MicroBlocksHandler.currentBlocks = null;
        MicroBlocksHandler.recipes = null;
    }
    
    public class MicroblockCachedRecipe extends TemplateRecipeHandler.CachedRecipe
    {
        public ArrayList<PositionedStack> ingredients;
        public MicroblockPositionedStack result;
        
        public MicroblockCachedRecipe(final int width, final int height, final Object[] items, final ItemStack out) {
            //super((TemplateRecipeHandler)MicroBlocksHandler.this);
	    super();
            this.result = new MicroblockPositionedStack(out, 119, 24);
            this.ingredients = new ArrayList<PositionedStack>();
            this.setIngredients(width, height, items);
        }
        
        public MicroblockCachedRecipe(final RecipeMicroBlocks irecipe) {
            this(irecipe.recipeWidth, irecipe.recipeHeight, irecipe.getRecipeItems(), irecipe.getRecipeOutput());
        }
        
        public void setIngredients(final int width, final int height, final Object[] items) {
            for (int x = 0; x < width; ++x) {
                for (int y = 0; y < height; ++y) {
                    if (items[y * width + x] != null) {
                        PositionedStack stack;
                        if (items[y * width + x] instanceof ItemStack && ((ItemStack)items[y * width + x]).getItem() == FMPBase.getMicroBlockItemId()) {
                            stack = new MicroblockPositionedStack((ItemStack)items[y * width + x], 25 + x * 18, 6 + y * 18);
                        }
                        else {
                            stack = new PositionedStack(items[y * width + x], 25 + x * 18, 6 + y * 18, false);
                        }
                        stack.setMaxSize(1);
                        this.ingredients.add(stack);
                    }
                }
            }
        }
        
        public void permMaterial() {
            if (!MicroBlocksHandler.this.scroll) {
                return;
            }
            if (MicroBlocksHandler.currentMaterials == null) {
                MicroBlocksHandler.currentMaterials = new String[MicroMaterialRegistry.getIdMap().length];
                for (int i = 0; i < MicroMaterialRegistry.getIdMap().length; ++i) {
                    MicroBlocksHandler.currentMaterials[i] = (String)MicroMaterialRegistry.getIdMap()[i]._1();
                }
            }
            if (MicroBlocksHandler.currentBlocks == null) {
                MicroBlocksHandler.currentBlocks = new ItemStack[MicroMaterialRegistry.getIdMap().length];
                for (int i = 0; i < MicroMaterialRegistry.getIdMap().length; ++i) {
                    MicroBlocksHandler.currentBlocks[i] = ((MicroMaterialRegistry.IMicroMaterial)MicroMaterialRegistry.getIdMap()[i]._2()).getItem().copy();
                }
            }
            MicroBlocksHandler.this.currentMaterial = MicroBlocksHandler.currentMaterials[MicroBlocksHandler.this.cycleticks / 20 % MicroBlocksHandler.currentMaterials.length];
            MicroBlocksHandler.this.currentBlock = MicroBlocksHandler.currentBlocks[MicroBlocksHandler.this.cycleticks / 20 % MicroBlocksHandler.currentMaterials.length];
        }
        
        public List<PositionedStack> getIngredients() {
            return this.getCycledIngredients(MicroBlocksHandler.this.cycleticks / 20, this.ingredients);
        }
        
        public List<PositionedStack> getCycledIngredients(final int cycle, final List<PositionedStack> ingredients) {
            return (List<PositionedStack>)super.getCycledIngredients(cycle, (List)ingredients);
        }
        
        public void randomRenderPermutation(final PositionedStack stack, final long cycle) {
            stack.setPermutationToRender(0);
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
            
            public MicroblockPositionedStack(final ItemStack object, final int x, final int y) {
                super((Object)object, x, y, false);
                this.materialTag = false;
                this.item = this.items[0].copy();
                this.materialTag = (this.item.getItem() != FMPBase.getMicroBlockItemId() || this.item.getItemDamage() != 0);
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
                    this.items[0] = MicroBlocksHandler.this.currentBlock.copy();
                }
                super.setPermutationToRender(0);
            }
            
            public void addMaterial() {
                NBTTagCompound tag = this.items[0].getTagCompound();
                if (tag == null) {
                    tag = new NBTTagCompound();
                }
                tag.setString("mat", MicroBlocksHandler.this.currentMaterial);
                this.items[0].setTagCompound(tag);
            }
        }
    }
}

