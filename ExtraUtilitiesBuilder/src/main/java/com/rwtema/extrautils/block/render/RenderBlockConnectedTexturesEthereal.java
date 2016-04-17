// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.block.render;

import com.rwtema.extrautils.ExtraUtilsProxy;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBlockConnectedTexturesEthereal extends RenderBlockConnectedTextures
{
    public static FakeRenderEtherealBlocks fakeRenderEtherealBlocks;
    
    @Override
    public boolean renderWorldBlock(final IBlockAccess world, final int x, final int y, final int z, final Block block, final int modelId, final RenderBlocks renderer) {
        if (renderer.hasOverrideBlockTexture()) {
            return renderer.renderStandardBlock(block, x, y, z);
        }
        RenderBlockConnectedTexturesEthereal.fakeRender.setWorld(renderer.blockAccess);
        RenderBlockConnectedTexturesEthereal.fakeRender.curBlock = world.getBlock(x, y, z);
        RenderBlockConnectedTexturesEthereal.fakeRender.curMeta = world.getBlockMetadata(x, y, z);
        block.setBlockBoundsBasedOnState(RenderBlockConnectedTexturesEthereal.fakeRender.blockAccess, x, y, z);
        RenderBlockConnectedTexturesEthereal.fakeRender.setRenderBoundsFromBlock(block);
        boolean render = RenderBlockConnectedTexturesEthereal.fakeRender.renderStandardBlock(block, x, y, z);
        RenderBlockConnectedTexturesEthereal.fakeRenderEtherealBlocks.setWorld(renderer.blockAccess);
        RenderBlockConnectedTexturesEthereal.fakeRenderEtherealBlocks.curBlock = RenderBlockConnectedTexturesEthereal.fakeRender.curBlock;
        RenderBlockConnectedTexturesEthereal.fakeRenderEtherealBlocks.curMeta = RenderBlockConnectedTexturesEthereal.fakeRender.curMeta;
        final double h = 0.05;
        RenderBlockConnectedTexturesEthereal.fakeRenderEtherealBlocks.setRenderBounds(h, h, h, 1.0 - h, 1.0 - h, 1.0 - h);
        render &= RenderBlockConnectedTexturesEthereal.fakeRenderEtherealBlocks.renderStandardBlock(block, x, y, z);
        return render;
    }
    
    @Override
    public int getRenderId() {
        return ExtraUtilsProxy.connectedTextureEtheralID;
    }
    
    static {
        RenderBlockConnectedTexturesEthereal.fakeRenderEtherealBlocks = new FakeRenderEtherealBlocks();
    }
}
