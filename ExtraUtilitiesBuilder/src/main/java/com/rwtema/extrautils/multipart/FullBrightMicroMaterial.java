// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.multipart;

import codechicken.lib.render.CCRenderPipeline;
import codechicken.lib.render.ColourMultiplier;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import com.rwtema.extrautils.block.BlockGreenScreen;
import codechicken.microblock.BlockMicroMaterial;

public class FullBrightMicroMaterial extends BlockMicroMaterial
{
    public FullBrightMicroMaterial(final BlockGreenScreen block, final int meta) {
        super((Block)block, meta);
    }
    
    @SideOnly(Side.CLIENT)
    public int getColour(final int pass) {
        return this.block().getRenderColor(this.meta()) << 8 | 0xFF;
    }
    
    public int getLightValue() {
        return BlockGreenScreen.getLightLevel(this.meta());
    }
    
    @SideOnly(Side.CLIENT)
    public void renderMicroFace(final Vector3 pos, final int pass, final Cuboid6 bounds) {
        final CCRenderPipeline.PipelineBuilder builder = CCRenderState.pipeline.builder();
        builder.add((CCRenderState.IVertexOperation)pos.translation()).add((CCRenderState.IVertexOperation)this.icont());
        builder.add((CCRenderState.IVertexOperation)ColourMultiplier.instance(this.getColour(pass)));
        builder.add((CCRenderState.IVertexOperation)Lighting.instance);
        builder.render();
    }
    
    public static class Lighting implements CCRenderState.IVertexOperation
    {
        public static Lighting instance;
        public static int id;
        
        public boolean load() {
            if (!CCRenderState.computeLighting) {
                return false;
            }
            CCRenderState.pipeline.addDependency(CCRenderState.colourAttrib);
            CCRenderState.pipeline.addDependency(CCRenderState.lightCoordAttrib);
            return true;
        }
        
        public void operate() {
            CCRenderState.setBrightness(16711935);
        }
        
        public int operationID() {
            return Lighting.id;
        }
        
        static {
            Lighting.instance = new Lighting();
            Lighting.id = CCRenderState.registerOperation();
        }
    }
}

