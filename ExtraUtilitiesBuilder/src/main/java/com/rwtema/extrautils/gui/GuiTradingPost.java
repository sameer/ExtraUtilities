// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.gui;

import net.minecraft.item.Item;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.opengl.GL11;
import com.rwtema.extrautils.network.XUPacketBase;
import com.rwtema.extrautils.network.NetworkHandler;
import com.rwtema.extrautils.network.packets.PacketVillagerReturn;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.Entity;
import java.util.Collections;
import java.util.ArrayList;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.village.MerchantRecipe;
import java.util.List;
import java.util.Comparator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.gui.GuiScreen;

public class GuiTradingPost extends GuiScreen
{
    protected static RenderItem itemRenderer;
    private static Comparator c;
    protected int guiLeft;
    protected int guiTop;
    protected String screenTitle;
    List<MerchantRecipe> merchant_recipes;
    List<Integer> merchant_id;
    int item_size;
    int space_between_items;
    int button_width;
    int button_height;
    int w;
    int h;
    int grid_w;
    int grid_h;
    int grid_no;
    int x0;
    int y0;
    int curPage;
    int maxPages;
    TileEntity tradingPost;
    EntityPlayer player;
    private GuiButton[] buttons;
    private GuiButton leftButton;
    private GuiButton rightButton;
    
    public GuiTradingPost(final EntityPlayer _player, final int[] p1ids, final MerchantRecipeList[] p2recipes, final TileEntity _post) {
        this.guiLeft = 0;
        this.guiTop = 0;
        this.screenTitle = "Trading Post";
        this.merchant_recipes = new ArrayList<MerchantRecipe>();
        this.merchant_id = new ArrayList<Integer>();
        this.item_size = 18;
        this.space_between_items = 10;
        this.button_width = this.item_size * 3 + this.space_between_items * 4;
        this.button_height = 20;
        this.w = this.button_width + this.space_between_items;
        this.h = this.button_height + 5;
        this.grid_w = 4;
        this.grid_h = 5;
        this.grid_no = this.grid_w * this.grid_h;
        this.x0 = 0;
        this.y0 = 20;
        this.curPage = 0;
        this.tradingPost = _post;
        this.player = _player;
        final List<Object[]> t = new ArrayList<Object[]>();
        for (int i = 0; i < p2recipes.length; ++i) {
            final Entity e = _player.worldObj.getEntityByID(p1ids[i]);
            if (e != null) {
                for (int j = 0; j < p2recipes[i].size(); ++j) {
                    t.add(new Object[] { e, p2recipes[i].get(j) });
                }
            }
        }
        Collections.sort(t, GuiTradingPost.c);
        int hash = -1;
        int k = 0;
        for (int l = 0; l < p2recipes.length; ++l) {
            final MerchantRecipe m = (MerchantRecipe)t.get(l)[1];
            final int h = m.writeToTags().hashCode();
            if (h != hash || l == 0) {
                hash = h;
                this.merchant_recipes.add(k, m);
                this.merchant_id.add(k, ((Entity)t.get(l)[0]).getEntityId());
                ++k;
            }
        }
    }
    
    public static int getProf(final Object a) {
        if (a instanceof EntityVillager) {
            return ((EntityVillager)a).getProfession();
        }
        return -1;
    }
    
    public void setPage(int page) {
        if (page < 0) {
            page = 0;
        }
        if (page >= this.maxPages) {
            page = this.maxPages - 1;
        }
        this.curPage = page;
        for (int i = 0; i < this.buttons.length; ++i) {
            this.buttons[i].enabled = (this.getRecipeForButton(i) < this.merchant_id.size() && !this.merchant_recipes.get(i).isRecipeDisabled());
        }
    }
    
    public int getRecipeForButton(final int i) {
        return this.curPage * this.grid_no + i;
    }
    
    public void initGui() {
        this.buttonList.clear();
        final int h2 = (int)Math.ceil(this.width / 1.5);
        this.grid_w = Math.min(this.width / this.w, 4);
        this.grid_h = Math.min((h2 - 40) / this.h, 5);
        if (this.grid_h < 0) {
            this.grid_h = 1;
        }
        this.grid_no = this.grid_w * this.grid_h;
        this.buttons = new GuiButton[this.grid_no];
        this.maxPages = (int)Math.ceil(this.merchant_recipes.size() / this.grid_no);
        this.x0 = (this.width - this.grid_w * this.w) / 2;
        this.y0 = 80;
        this.buttonList.add(this.leftButton = new GuiButton(0, this.width / 2 - 100, 15, 20, 20, "<"));
        this.buttonList.add(this.rightButton = new GuiButton(1, this.width / 2 + 80, 15, 20, 20, ">"));
        this.leftButton.enabled = (this.maxPages > 1);
        this.rightButton.enabled = (this.maxPages > 1);
        final String text = "";
        for (int i = 0; i < this.grid_no; ++i) {
            this.buttons[i] = new GuiButton(2 + i, this.x0 + i / this.grid_h * this.w, this.y0 + i % this.grid_h * this.h, this.button_width, this.button_height, text);
            if (i >= this.merchant_id.size()) {
                this.buttons[i].enabled = false;
            }
            this.buttonList.add(this.buttons[i]);
        }
        this.setPage(0);
    }
    
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    public void updateScreen() {
        boolean flag = false;
        if (this.tradingPost == null) {
            flag = true;
        }
        else if (this.tradingPost.getWorldObj() == null) {
            flag = true;
        }
        else if (this.tradingPost.getWorldObj().getTileEntity(this.tradingPost.xCoord, this.tradingPost.yCoord, this.tradingPost.zCoord) != this.tradingPost) {
            flag = true;
        }
        if (flag) {
            this.mc.displayGuiScreen((GuiScreen)null);
        }
    }
    
    protected void actionPerformed(final GuiButton par1GuiButton) {
        if (par1GuiButton.enabled) {
            if (par1GuiButton.id == 0) {
                this.setPage(this.curPage - 1);
            }
            else if (par1GuiButton.id == 1) {
                this.setPage(this.curPage + 1);
            }
            else {
                final int i = this.getRecipeForButton(par1GuiButton.id - 2);
                NetworkHandler.sendPacketToServer(new PacketVillagerReturn(this.merchant_id.get(i), this.tradingPost.getWorldObj().provider.dimensionId, this.tradingPost.xCoord, this.tradingPost.yCoord, this.tradingPost.zCoord));
                this.mc.displayGuiScreen((GuiScreen)null);
            }
        }
    }
    
    public void drawScreen(final int par1, final int par2, final float par3) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.screenTitle, this.width / 2, 15, 16777215);
        super.drawScreen(par1, par2, par3);
        GL11.glPushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glDisable(2896);
        GL11.glEnable(32826);
        GL11.glEnable(2903);
        GL11.glEnable(2896);
        GuiTradingPost.itemRenderer.zLevel = 100.0f;
        for (int i = 0; i < this.buttons.length; ++i) {
            final int j = this.getRecipeForButton(i);
            if (j < this.merchant_recipes.size()) {
                final int x = this.buttons[i].xPosition;
                final int y = this.buttons[i].yPosition;
                if (this.merchant_recipes.get(j).getSecondItemToBuy() != null) {
                    GuiTradingPost.itemRenderer.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.renderEngine, this.merchant_recipes.get(j).getItemToBuy(), x + this.space_between_items, y + 2);
                    GuiTradingPost.itemRenderer.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.renderEngine, this.merchant_recipes.get(j).getItemToBuy(), x + this.space_between_items, y + 2);
                    GuiTradingPost.itemRenderer.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.renderEngine, this.merchant_recipes.get(j).getSecondItemToBuy(), x + this.space_between_items * 2 + this.item_size, y + 2);
                    GuiTradingPost.itemRenderer.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.renderEngine, this.merchant_recipes.get(j).getSecondItemToBuy(), x + this.space_between_items * 2 + this.item_size, y + 2);
                    GuiTradingPost.itemRenderer.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.renderEngine, this.merchant_recipes.get(j).getItemToSell(), x + 3 * this.space_between_items + 2 * this.item_size, y + 2);
                    GuiTradingPost.itemRenderer.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.renderEngine, this.merchant_recipes.get(j).getItemToSell(), x + 3 * this.space_between_items + 2 * this.item_size, y + 2);
                }
                else {
                    GuiTradingPost.itemRenderer.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.renderEngine, this.merchant_recipes.get(j).getItemToBuy(), x + (this.item_size + this.space_between_items) / 2 + this.space_between_items, y + 2);
                    GuiTradingPost.itemRenderer.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.renderEngine, this.merchant_recipes.get(j).getItemToBuy(), x + (this.item_size + this.space_between_items) / 2 + this.space_between_items, y + 2);
                    GuiTradingPost.itemRenderer.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.renderEngine, this.merchant_recipes.get(j).getItemToSell(), x + (this.item_size + this.space_between_items) / 2 + 2 * this.space_between_items + this.item_size, y + 2);
                    GuiTradingPost.itemRenderer.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.renderEngine, this.merchant_recipes.get(j).getItemToSell(), x + (this.item_size + this.space_between_items) / 2 + 2 * this.space_between_items + this.item_size, y + 2);
                }
            }
        }
        GuiTradingPost.itemRenderer.zLevel = 0.0f;
        GL11.glDisable(2896);
        for (int i = 0; i < this.buttons.length; ++i) {
            final int j = this.getRecipeForButton(i);
            if (j < this.merchant_recipes.size()) {
                final int x = this.buttons[i].xPosition;
                final int y = this.buttons[i].yPosition;
                if (this.merchant_recipes.get(j).getSecondItemToBuy() != null) {
                    this.drawCenteredString(this.fontRendererObj, "+", x + this.space_between_items * 3 / 2 + this.item_size, y + 7, 16777215);
                    this.drawCenteredString(this.fontRendererObj, "=", x + this.space_between_items * 5 / 2 + 2 * this.item_size, y + 7, 16777215);
                }
                else {
                    this.drawCenteredString(this.fontRendererObj, "=", x + this.button_width / 2, y + 7, 16777215);
                }
            }
        }
        for (int i = 0; i < this.buttons.length; ++i) {
            final int j = this.getRecipeForButton(i);
            if (j < this.merchant_recipes.size()) {
                final int x = this.buttons[i].xPosition;
                final int y = this.buttons[i].yPosition;
                if (this.merchant_recipes.get(j).getSecondItemToBuy() != null) {
                    if (this.func_146978_c(x + this.space_between_items, y, this.item_size, this.item_size, par1, par2)) {
                        this.drawItemStackTooltip(this.merchant_recipes.get(j).getItemToBuy(), par1, par2);
                    }
                    if (this.func_146978_c(x + this.space_between_items * 2 + this.item_size, y, this.item_size, this.item_size, par1, par2)) {
                        this.drawItemStackTooltip(this.merchant_recipes.get(j).getSecondItemToBuy(), par1, par2);
                    }
                    if (this.func_146978_c(x + 3 * this.space_between_items + 2 * this.item_size, y, this.item_size, this.item_size, par1, par2)) {
                        this.drawItemStackTooltip(this.merchant_recipes.get(j).getItemToSell(), par1, par2);
                    }
                }
                else {
                    if (this.func_146978_c(x + (this.item_size + this.space_between_items) / 2 + this.space_between_items, y, this.item_size, this.item_size, par1, par2)) {
                        this.drawItemStackTooltip(this.merchant_recipes.get(j).getItemToBuy(), par1, par2);
                    }
                    if (this.func_146978_c(x + (this.item_size + this.space_between_items) / 2 + 2 * this.space_between_items + this.item_size, y, this.item_size, this.item_size, par1, par2)) {
                        this.drawItemStackTooltip(this.merchant_recipes.get(j).getItemToSell(), par1, par2);
                    }
                }
            }
        }
        GL11.glPopMatrix();
        GL11.glEnable(2896);
        GL11.glEnable(2929);
        RenderHelper.enableStandardItemLighting();
    }
    
    protected void drawItemStackTooltip(final ItemStack par1ItemStack, final int par2, final int par3) {
        GL11.glDisable(32826);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        final List<String> list = par1ItemStack.getTooltip((EntityPlayer)this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);
        if (!list.isEmpty()) {
            int k = 0;
            for (int l = 0; l < list.size(); ++l) {
                final int i1 = this.fontRendererObj.getStringWidth((String)list.get(l));
                if (i1 > k) {
                    k = i1;
                }
            }
            int l = par2 + 12;
            int i1 = par3 - 12;
            int j1 = 8;
            if (list.size() > 1) {
                j1 += 2 + (list.size() - 1) * 10;
            }
            this.zLevel = 300.0f;
            GuiTradingPost.itemRenderer.zLevel = 300.0f;
            final int k2 = -267386864;
            this.drawGradientRect(l - 3, i1 - 4, l + k + 3, i1 - 3, k2, k2);
            this.drawGradientRect(l - 3, i1 + j1 + 3, l + k + 3, i1 + j1 + 4, k2, k2);
            this.drawGradientRect(l - 3, i1 - 3, l + k + 3, i1 + j1 + 3, k2, k2);
            this.drawGradientRect(l - 4, i1 - 3, l - 3, i1 + j1 + 3, k2, k2);
            this.drawGradientRect(l + k + 3, i1 - 3, l + k + 4, i1 + j1 + 3, k2, k2);
            final int l2 = 1347420415;
            final int i2 = (l2 & 0xFEFEFE) >> 1 | (l2 & 0xFF000000);
            this.drawGradientRect(l - 3, i1 - 3 + 1, l - 3 + 1, i1 + j1 + 3 - 1, l2, i2);
            this.drawGradientRect(l + k + 2, i1 - 3 + 1, l + k + 3, i1 + j1 + 3 - 1, l2, i2);
            this.drawGradientRect(l - 3, i1 - 3, l + k + 3, i1 - 3 + 1, l2, l2);
            this.drawGradientRect(l - 3, i1 + j1 + 2, l + k + 3, i1 + j1 + 3, i2, i2);
            for (int j2 = 0; j2 < list.size(); ++j2) {
                String s = list.get(j2);
                if (j2 == 0) {
                    s = par1ItemStack.getRarity().rarityColor + s;
                }
                else {
                    s = EnumChatFormatting.GRAY + s;
                }
                this.fontRendererObj.drawStringWithShadow(s, l, i1, -1);
                if (j2 == 0) {
                    i1 += 2;
                }
                i1 += 10;
            }
            this.zLevel = 0.0f;
            GuiTradingPost.itemRenderer.zLevel = 0.0f;
        }
    }
    
    private void drawLine(final double x1, final double x2, final double y1, final double y2, final int col) {
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glDisable(3008);
        GL11.glBlendFunc(770, 771);
        GL11.glShadeModel(7425);
        final Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawing(1);
        tessellator.setColorOpaque_I(col);
        tessellator.addVertex(x1, y1, (double)this.zLevel);
        tessellator.addVertex(x2, y2, (double)this.zLevel);
        tessellator.draw();
        GL11.glShadeModel(7424);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glEnable(3553);
    }
    
    protected boolean func_146978_c(final int par1, final int par2, final int par3, final int par4, int par5, int par6) {
        final int k1 = this.guiTop;
        final int l1 = this.guiLeft;
        par5 -= k1;
        par6 -= l1;
        return par5 >= par1 - 1 && par5 < par1 + par3 + 1 && par6 >= par2 - 1 && par6 < par2 + par4 + 1;
    }
    
    static {
        GuiTradingPost.itemRenderer = new RenderItem();
        GuiTradingPost.c = new VillagerSorter();
    }
    
    public static class VillagerSorter implements Comparator
    {
        public int intCompare(final int a, final int b) {
            if (a == b) {
                return 0;
            }
            if (a > b) {
                return 1;
            }
            return -1;
        }
        
        public int itemCompare(final Item a, final Item b) {
            return a.getUnlocalizedName().compareTo(b.getUnlocalizedName());
        }
        
        @Override
        public int compare(final Object arg0, final Object arg1) {
            final int i = this.intCompare(GuiTradingPost.getProf(((Object[])arg0)[0]), GuiTradingPost.getProf(((Object[])arg0)[0]));
            if (i != 0) {
                return i;
            }
            final MerchantRecipe m1 = (MerchantRecipe)((Object[])arg0)[1];
            final MerchantRecipe m2 = (MerchantRecipe)((Object[])arg1)[1];
            final int i2 = this.itemCompare(m1.getItemToBuy().getItem(), m2.getItemToBuy().getItem());
            if (i2 != 0) {
                return i2;
            }
            final int i3 = this.intCompare(m1.getItemToBuy().stackSize, m2.getItemToBuy().stackSize);
            if (i3 != 0) {
                return i3;
            }
            final int i4 = this.itemCompare(m1.getItemToSell().getItem(), m2.getItemToSell().getItem());
            if (i4 != 0) {
                return i4;
            }
            final int i5 = this.intCompare(m1.getItemToSell().stackSize, m2.getItemToSell().stackSize);
            if (i5 == 0) {
                return this.intCompare(m1.writeToTags().hashCode(), m2.writeToTags().hashCode());
            }
            return i5;
        }
    }
}

