// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.transfernodes;

import java.util.Collections;
import net.minecraft.tileentity.TileEntity;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeBuffer;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.HashMap;

public class TransferNodeEnderRegistry
{
    private static final HashMap<Frequency, List<WeakReference<INodeBuffer>>> receptors;
    
    public static synchronized void clearTileRegistrations(final INodeBuffer buffer) {
        synchronized (TransferNodeEnderRegistry.receptors) {
            Set<Frequency> t = null;
            for (final Frequency fs : TransferNodeEnderRegistry.receptors.keySet()) {
                final List<WeakReference<INodeBuffer>> list = TransferNodeEnderRegistry.receptors.get(fs);
                final Iterator<WeakReference<INodeBuffer>> iter = list.iterator();
                while (iter.hasNext()) {
                    final INodeBuffer next = iter.next().get();
                    if (next == null || next == buffer) {
                        iter.remove();
                    }
                }
                if (list.isEmpty()) {
                    if (t == null) {
                        t = new HashSet<Frequency>();
                    }
                    t.add(fs);
                }
            }
            if (t != null) {
                for (final Frequency fs : t) {
                    TransferNodeEnderRegistry.receptors.remove(fs);
                }
            }
        }
    }
    
    public static synchronized void registerTile(final Frequency freq, final INodeBuffer buffer) {
        synchronized (TransferNodeEnderRegistry.receptors) {
            final TileEntity a = buffer.getNode().getNode();
            for (final Frequency fs : TransferNodeEnderRegistry.receptors.keySet()) {
                final Iterator<WeakReference<INodeBuffer>> iter = TransferNodeEnderRegistry.receptors.get(fs).iterator();
                while (iter.hasNext()) {
                    final INodeBuffer next = iter.next().get();
                    if (next == null) {
                        iter.remove();
                    }
                    else {
                        final TileEntity b = next.getNode().getNode();
                        if (a.xCoord == b.xCoord && a.zCoord == b.zCoord && a.yCoord == b.yCoord && a.getWorldObj().provider.dimensionId == b.getWorldObj().provider.dimensionId) {
                            return;
                        }
                        continue;
                    }
                }
            }
            List<WeakReference<INodeBuffer>> b2 = TransferNodeEnderRegistry.receptors.get(freq);
            if (b2 == null) {
                b2 = new ArrayList<WeakReference<INodeBuffer>>();
                TransferNodeEnderRegistry.receptors.put(freq, b2);
            }
            b2.add(new WeakReference<INodeBuffer>(buffer));
        }
    }
    
    public static synchronized void doTransfer(final INodeBuffer sender, final Frequency f, final int no) {
        final List<WeakReference<INodeBuffer>> b = TransferNodeEnderRegistry.receptors.get(f);
        if (b == null) {
            return;
        }
        Collections.shuffle(b);
        final Iterator<WeakReference<INodeBuffer>> iterator = b.iterator();
        while (iterator.hasNext()) {
            final WeakReference<INodeBuffer> ref = iterator.next();
            final INodeBuffer reciever = ref.get();
            if (reciever == null) {
                iterator.remove();
            }
            else {
                if (reciever == sender) {
                    continue;
                }
                sender.transferTo(reciever, no);
            }
        }
    }
    
    static {
        receptors = new HashMap<Frequency, List<WeakReference<INodeBuffer>>>();
    }
}

