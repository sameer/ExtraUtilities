// 
// Decompiled by Procyon v0.5.30
// 

package invtweaks.api;

public interface IItemTreeItem extends Comparable<IItemTreeItem>
{
    String getName();
    
    String getId();
    
    int getDamage();
    
    int getOrder();
}
