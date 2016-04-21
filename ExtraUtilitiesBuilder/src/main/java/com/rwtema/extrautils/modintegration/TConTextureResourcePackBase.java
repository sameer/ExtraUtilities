// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.modintegration;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import java.util.Set;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import com.google.common.base.Throwables;
import javax.imageio.ImageIO;
import net.minecraft.client.resources.ResourcePackRepository;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.IOException;
import java.io.InputStream;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import com.rwtema.extrautils.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.util.ResourceLocation;
import java.util.HashMap;
import java.awt.image.DirectColorModel;
import java.util.List;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.IResourcePack;

public abstract class TConTextureResourcePackBase implements IResourcePack, IResourceManagerReloadListener
{
    public static List<IResourcePack> packs;
    protected static DirectColorModel rgb;
    protected final String name;
    public HashMap<ResourceLocation, byte[]> cachedImages;
    protected IResourcePack delegate;
    protected List<IResourcePack> resourcePackz;
    
    public TConTextureResourcePackBase(final String name) {
        this.cachedImages = new HashMap<ResourceLocation, byte[]>();
        this.resourcePackz = null;
        this.name = name.toLowerCase();
        this.delegate = FMLClientHandler.instance().getResourcePackFor("TConstruct");
    }
    
    public int brightness(final int col) {
        return this.brightness(TConTextureResourcePackBase.rgb.getRed(col), TConTextureResourcePackBase.rgb.getGreen(col), TConTextureResourcePackBase.rgb.getBlue(col));
    }
    
    public int brightness(final int r, final int g, final int b) {
        return (int)(r * 0.2126f + g * 0.7152f + b * 0.0722f);
    }
    
    public void register() {
        final List<IResourcePack> packs = this.getiResourcePacks();
        packs.add((IResourcePack)this);
        ((IReloadableResourceManager)Minecraft.getMinecraft().getResourceManager()).registerReloadListener((IResourceManagerReloadListener)this);
        LogHelper.info("Registered TCon Resource Pack (" + this.name + ") - " + this.getClass().getSimpleName(), new Object[0]);
    }
    
    public List<IResourcePack> getiResourcePacks() {
        List<IResourcePack> packs1 = TConTextureResourcePackBase.packs;
        if (packs1 == null) {
            packs1 = (List<IResourcePack>)ObfuscationReflectionHelper.getPrivateValue((Class)FMLClientHandler.class, (Object)FMLClientHandler.instance(), new String[] { "resourcePackList" });
        }
        return packs1;
    }
    
    public InputStream getStream(final ResourceLocation location) {
        InputStream stream = null;
        for (final IResourcePack iResourcePack : this.getPacks()) {
            if (iResourcePack.resourceExists(location)) {
                try {
                    stream = iResourcePack.getInputStream(location);
                }
                catch (IOException ex) {}
            }
        }
        return stream;
    }
    
    public List<IResourcePack> getPacks() {
        if (this.resourcePackz == null) {
            (this.resourcePackz = new ArrayList<IResourcePack>()).add(this.delegate);
            final List<ResourcePackRepository.Entry> t = (List<ResourcePackRepository.Entry>)Minecraft.getMinecraft().getResourcePackRepository().getRepositoryEntries();
            for (final ResourcePackRepository.Entry entry : t) {
                final IResourcePack resourcePack = entry.getResourcePack();
                if (resourcePack.getResourceDomains().contains("tinker")) {
                    this.resourcePackz.add(resourcePack);
                }
            }
        }
        return this.resourcePackz;
    }
    
    public InputStream getInputStream(final ResourceLocation p_110590_1_) throws IOException {
        byte[] bytes = this.cachedImages.get(p_110590_1_);
        if (bytes == null) {
            ResourceLocation location = new ResourceLocation("tinker", p_110590_1_.getResourcePath().replace(this.name, ""));
            InputStream inputStream = this.getStream(location);
            if (inputStream == null) {
                location = new ResourceLocation("tinker", p_110590_1_.getResourcePath().replace(this.name, "iron"));
                inputStream = this.getStream(location);
            }
            if (inputStream == null) {
                location = new ResourceLocation("tinker", p_110590_1_.getResourcePath().replace(this.name, "stone"));
                inputStream = this.getStream(location);
            }
            if (inputStream == null) {
                return this.delegate.getInputStream(p_110590_1_);
            }
            BufferedImage bufferedimage;
            try {
                bufferedimage = ImageIO.read(inputStream);
            }
            catch (IOException err) {
                throw Throwables.propagate((Throwable)err);
            }
            BufferedImage image;
            try {
                image = this.modifyImage(bufferedimage);
            }
            catch (Throwable t) {
                t.printStackTrace();
                return this.delegate.getInputStream(location);
            }
            final ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", stream);
            bytes = stream.toByteArray();
            this.cachedImages.put(location, bytes);
        }
        return new ByteArrayInputStream(bytes);
    }
    
    public abstract BufferedImage modifyImage(final BufferedImage p0);
    
    public boolean resourceExists(final ResourceLocation p_110589_1_) {
        if (!"tinker".equals(p_110589_1_.getResourceDomain())) {
            return false;
        }
        final String resourcePath = p_110589_1_.getResourcePath();
        return resourcePath.startsWith("textures/items/") && resourcePath.endsWith(".png") && !this.delegate.resourceExists(p_110589_1_) && resourcePath.contains(this.name) && (this.delegate.resourceExists(new ResourceLocation("tinker", resourcePath.replace(this.name, "stone"))) || this.delegate.resourceExists(new ResourceLocation("tinker", resourcePath.replace(this.name, "iron"))) || this.delegate.resourceExists(new ResourceLocation("tinker", resourcePath.replace(this.name, ""))));
    }
    
    public Set getResourceDomains() {
        return this.delegate.getResourceDomains();
    }
    
    public IMetadataSection getPackMetadata(final IMetadataSerializer p_135058_1_, final String p_135058_2_) throws IOException {
        return null;
    }
    
    public BufferedImage getPackImage() throws IOException {
        return null;
    }
    
    public String getPackName() {
        return "XU_Delegate_Pack";
    }
    
    public void onResourceManagerReload(final IResourceManager p_110549_1_) {
        this.cachedImages.clear();
        this.resourcePackz = null;
    }
    
    static {
        TConTextureResourcePackBase.rgb = new DirectColorModel(32, 16711680, 65280, 255, -16777216);
    }
}

