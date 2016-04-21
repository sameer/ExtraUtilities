// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package invtweaks.api;

import java.util.Random;
import java.util.Collection;
import java.util.List;

public interface IItemTree
{
    void registerOre(final String p0, final String p1, final String p2, final int p3);
    
    boolean matches(final List<IItemTreeItem> p0, final String p1);
    
    boolean isKeywordValid(final String p0);
    
    Collection<IItemTreeCategory> getAllCategories();
    
    IItemTreeCategory getRootCategory();
    
    IItemTreeCategory getCategory(final String p0);
    
    boolean isItemUnknown(final String p0, final int p1);
    
    List<IItemTreeItem> getItems(final String p0, final int p1);
    
    List<IItemTreeItem> getItems(final String p0);
    
    IItemTreeItem getRandomItem(final Random p0);
    
    boolean containsItem(final String p0);
    
    boolean containsCategory(final String p0);
    
    void setRootCategory(final IItemTreeCategory p0);
    
    IItemTreeCategory addCategory(final String p0, final String p1) throws NullPointerException;
    
    void addCategory(final String p0, final IItemTreeCategory p1) throws NullPointerException;
    
    IItemTreeItem addItem(final String p0, final String p1, final String p2, final int p3, final int p4) throws NullPointerException;
    
    void addItem(final String p0, final IItemTreeItem p1) throws NullPointerException;
    
    int getKeywordDepth(final String p0);
    
    int getKeywordOrder(final String p0);
}


