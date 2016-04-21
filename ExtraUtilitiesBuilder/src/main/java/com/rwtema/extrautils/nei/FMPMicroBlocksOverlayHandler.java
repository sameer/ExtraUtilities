// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.nei;

import codechicken.microblock.Saw;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.HashMap;
import net.minecraft.item.ItemStack;
import codechicken.lib.inventory.InventoryUtils;
import net.minecraft.entity.player.InventoryPlayer;
import java.util.Iterator;
import codechicken.nei.FastTransferManager;
import net.minecraft.inventory.Slot;
import codechicken.nei.PositionedStack;
import java.util.List;
import codechicken.nei.recipe.IRecipeHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import codechicken.nei.api.IOverlayHandler;

@SideOnly(Side.CLIENT)
public class FMPMicroBlocksOverlayHandler implements IOverlayHandler
{
    int offsetx;
    int offsety;
    
    public FMPMicroBlocksOverlayHandler(final int x, final int y) {
        this.offsetx = x;
        this.offsety = y;
    }
    
    public FMPMicroBlocksOverlayHandler() {
        this(5, 11);
    }
    
    public void overlayRecipe(final GuiContainer gui, final IRecipeHandler recipe, final int recipeIndex, final boolean shift) {
        final List<PositionedStack> ingredients = (List<PositionedStack>)recipe.getIngredientStacks(recipeIndex);
        final List<DistributedIngred> ingredStacks = this.getPermutationIngredients(ingredients);
        if (!this.clearIngredients(gui, ingredients)) {
            return;
        }
        this.findInventoryQuantities(gui, ingredStacks);
        final List<IngredientDistribution> assignedIngredients = this.assignIngredients(ingredients, ingredStacks);
        if (assignedIngredients == null) {
            return;
        }
        this.assignIngredSlots(gui, ingredients, assignedIngredients);
        final int quantity = this.calculateRecipeQuantity(assignedIngredients);
        if (quantity != 0) {
            this.moveIngredients(gui, assignedIngredients, quantity);
        }
    }
    
    private boolean clearIngredients(final GuiContainer gui, final List<PositionedStack> ingreds) {
        for (final PositionedStack pstack : ingreds) {
            for (final Slot slot : (Collection<Slot>) gui.inventorySlots.inventorySlots) {
                if (slot.xDisplayPosition == pstack.relx + this.offsetx && slot.yDisplayPosition == pstack.rely + this.offsety) {
                    if (!slot.getHasStack()) {
                        continue;
                    }
                    FastTransferManager.clickSlot(gui, slot.slotNumber, 0, 1);
                    if (slot.getHasStack()) {
                        return false;
                    }
                    continue;
                }
            }
        }
        return true;
    }
    
    private void moveIngredients(final GuiContainer gui, final List<IngredientDistribution> assignedIngredients, final int quantity) {
        for (final IngredientDistribution distrib : assignedIngredients) {
            final ItemStack pstack = distrib.permutation;
            int transferCap = quantity * pstack.stackSize;
            int transferred = 0;
            if (distrib.distrib.isSaw) {
                transferCap = 1;
            }
            int destSlotIndex = 0;
            Slot dest = distrib.slots[0];
            int slotTransferred = 0;
            final int slotTransferCap = pstack.getMaxStackSize();
            for (final Slot slot : (Collection<Slot>) gui.inventorySlots.inventorySlots) {
                if (slot.getHasStack()) {
                    if (!(slot.inventory instanceof InventoryPlayer)) {
                        continue;
                    }
                    final ItemStack stack = slot.getStack();
                    if (distrib.distrib.isSaw) {
                        if (!sameItemStack(stack, pstack)) {
                            continue;
                        }
                    }
                    else if (!InventoryUtils.canStack(stack, pstack)) {
                        continue;
                    }
                    FastTransferManager.clickSlot(gui, slot.slotNumber);
                    for (int amount = Math.min(transferCap - transferred, stack.stackSize), c = 0; c < amount; ++c) {
                        FastTransferManager.clickSlot(gui, dest.slotNumber, 1);
                        ++transferred;
                        if (++slotTransferred >= slotTransferCap) {
                            if (++destSlotIndex == distrib.slots.length) {
                                dest = null;
                                break;
                            }
                            dest = distrib.slots[destSlotIndex];
                            slotTransferred = 0;
                        }
                    }
                    FastTransferManager.clickSlot(gui, slot.slotNumber);
                    if (transferred >= transferCap) {
                        break;
                    }
                    if (dest == null) {
                        break;
                    }
                    continue;
                }
            }
        }
    }
    
    public static boolean sameItemStack(final ItemStack stack1, final ItemStack stack2) {
        return stack1 == null || stack2 == null || (stack1.getItem() == stack2.getItem() && (!stack2.getHasSubtypes() || stack2.getItemDamage() == stack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(stack2, stack1));
    }
    
    private int calculateRecipeQuantity(final List<IngredientDistribution> assignedIngredients) {
        int quantity = Integer.MAX_VALUE;
        for (final IngredientDistribution distrib : assignedIngredients) {
            final DistributedIngred istack = distrib.distrib;
            if (distrib.distrib.isSaw) {
                continue;
            }
            if (istack.numSlots == 0) {
                return 0;
            }
            int allSlots = istack.invAmount;
            if (allSlots / istack.numSlots > istack.stack.getMaxStackSize()) {
                allSlots = istack.numSlots * istack.stack.getMaxStackSize();
            }
            quantity = Math.min(quantity, allSlots / istack.distributed);
        }
        return quantity;
    }
    
    private Slot[][] assignIngredSlots(final GuiContainer gui, final List<PositionedStack> ingredients, final List<IngredientDistribution> assignedIngredients) {
        final Slot[][] recipeSlots = this.mapIngredSlots(gui, ingredients);
        final HashMap<Slot, Integer> distribution = new HashMap<Slot, Integer>();
        for (int i = 0; i < recipeSlots.length; ++i) {
            for (final Slot slot : recipeSlots[i]) {
                if (!distribution.containsKey(slot)) {
                    distribution.put(slot, -1);
                }
            }
        }
        final HashSet<Slot> avaliableSlots = new HashSet<Slot>(distribution.keySet());
        final HashSet<Integer> remainingIngreds = new HashSet<Integer>();
        final ArrayList<LinkedList<Slot>> assignedSlots = new ArrayList<LinkedList<Slot>>();
        for (int j = 0; j < ingredients.size(); ++j) {
            remainingIngreds.add(j);
            assignedSlots.add(new LinkedList<Slot>());
        }
        while (avaliableSlots.size() > 0 && remainingIngreds.size() > 0) {
            final Iterator<Integer> iterator = remainingIngreds.iterator();
            while (iterator.hasNext()) {
                final int k = iterator.next();
                boolean assigned = false;
                final DistributedIngred istack = assignedIngredients.get(k).distrib;
                for (final Slot slot2 : recipeSlots[k]) {
                    if (avaliableSlots.contains(slot2)) {
                        avaliableSlots.remove(slot2);
                        if (!slot2.getHasStack()) {
                            final DistributedIngred distributedIngred = istack;
                            ++distributedIngred.numSlots;
                            assignedSlots.get(k).add(slot2);
                            assigned = true;
                            break;
                        }
                    }
                }
                if (!assigned || istack.numSlots * istack.stack.getMaxStackSize() >= istack.invAmount) {
                    iterator.remove();
                }
            }
        }
        for (int j = 0; j < ingredients.size(); ++j) {
            assignedIngredients.get(j).slots = assignedSlots.get(j).toArray(new Slot[0]);
        }
        return recipeSlots;
    }
    
    private List<IngredientDistribution> assignIngredients(final List<PositionedStack> ingredients, final List<DistributedIngred> ingredStacks) {
        final ArrayList<IngredientDistribution> assignedIngredients = new ArrayList<IngredientDistribution>();
        for (final PositionedStack posstack : ingredients) {
            DistributedIngred biggestIngred = null;
            ItemStack permutation = null;
            int biggestSize = 0;
            for (final ItemStack pstack : posstack.items) {
                final boolean isSaw = isSaw(pstack);
                for (int j = 0; j < ingredStacks.size(); ++j) {
                    final DistributedIngred istack = ingredStacks.get(j);
                    if (isSaw == istack.isSaw) {
                        if (isSaw) {
                            if (this.sameSaw(pstack, istack.stack)) {
                                if (istack.invAmount - istack.distributed >= pstack.stackSize) {
                                    biggestSize = 1;
                                    biggestIngred = istack;
                                    permutation = pstack;
                                    break;
                                }
                            }
                        }
                        else if (InventoryUtils.canStack(pstack, istack.stack)) {
                            if (istack.invAmount - istack.distributed >= pstack.stackSize) {
                                final int relsize = (istack.invAmount - istack.invAmount / istack.recipeAmount * istack.distributed) / pstack.stackSize;
                                if (relsize > biggestSize) {
                                    biggestSize = relsize;
                                    biggestIngred = istack;
                                    permutation = pstack;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if (biggestIngred == null) {
                return null;
            }
            final DistributedIngred distributedIngred = biggestIngred;
            distributedIngred.distributed += permutation.stackSize;
            assignedIngredients.add(new IngredientDistribution(biggestIngred, permutation));
        }
        return assignedIngredients;
    }
    
    public static boolean isSaw(final ItemStack a) {
        return !a.isStackable() && a.getItem() instanceof Saw;
    }
    
    public boolean sameSaw(final ItemStack a, final ItemStack b) {
        return a.getItem() == b.getItem() && a.getItem() instanceof Saw && ((Saw)a.getItem()).getCuttingStrength(a) == ((Saw)a.getItem()).getCuttingStrength(b);
    }
    
    private void findInventoryQuantities(final GuiContainer gui, final List<DistributedIngred> ingredStacks) {
        for (final Slot slot : (Collection<Slot>) gui.inventorySlots.inventorySlots) {
            if (slot.getHasStack() && slot.inventory instanceof InventoryPlayer) {
                final ItemStack pstack = slot.getStack();
                final DistributedIngred istack = this.findIngred(ingredStacks, pstack);
                if (istack == null) {
                    continue;
                }
                final DistributedIngred distributedIngred = istack;
                distributedIngred.invAmount += pstack.stackSize;
            }
        }
    }
    
    private List<DistributedIngred> getPermutationIngredients(final List<PositionedStack> ingredients) {
        final ArrayList<DistributedIngred> ingredStacks = new ArrayList<DistributedIngred>();
        for (final PositionedStack posstack : ingredients) {
            for (final ItemStack pstack : posstack.items) {
                DistributedIngred istack = this.findIngred(ingredStacks, pstack);
                if (istack == null) {
                    ingredStacks.add(istack = new DistributedIngred(pstack));
                }
                final DistributedIngred distributedIngred = istack;
                distributedIngred.recipeAmount += pstack.stackSize;
            }
        }
        return ingredStacks;
    }
    
    public Slot[][] mapIngredSlots(final GuiContainer gui, final List<PositionedStack> ingredients) {
        final Slot[][] recipeSlotList = new Slot[ingredients.size()][];
        for (int i = 0; i < ingredients.size(); ++i) {
            final LinkedList<Slot> recipeSlots = new LinkedList<Slot>();
            final PositionedStack pstack = ingredients.get(i);
            for (final Slot slot : (Collection<Slot>) gui.inventorySlots.inventorySlots) {
                if (slot.xDisplayPosition == pstack.relx + this.offsetx && slot.yDisplayPosition == pstack.rely + this.offsety) {
                    recipeSlots.add(slot);
                    break;
                }
            }
            recipeSlotList[i] = recipeSlots.toArray(new Slot[0]);
        }
        return recipeSlotList;
    }
    
    public DistributedIngred findIngred(final List<DistributedIngred> ingredStacks, final ItemStack pstack) {
        for (final DistributedIngred istack : ingredStacks) {
            if (istack.isSaw) {
                if (this.sameSaw(pstack, istack.stack)) {
                    return istack;
                }
                continue;
            }
            else {
                if (InventoryUtils.canStack(pstack, istack.stack)) {
                    return istack;
                }
                continue;
            }
        }
        return null;
    }
    
    public static class DistributedIngred
    {
        public boolean isSaw;
        public ItemStack stack;
        public int invAmount;
        public int distributed;
        public int numSlots;
        public int recipeAmount;
        
        public DistributedIngred(final ItemStack item) {
            this.stack = InventoryUtils.copyStack(item, 1);
            this.isSaw = FMPMicroBlocksOverlayHandler.isSaw(item);
        }
    }
    
    public static class IngredientDistribution
    {
        public DistributedIngred distrib;
        public ItemStack permutation;
        public Slot[] slots;
        
        public IngredientDistribution(final DistributedIngred distrib, final ItemStack permutation) {
            this.distrib = distrib;
            this.permutation = permutation;
        }
    }
}


