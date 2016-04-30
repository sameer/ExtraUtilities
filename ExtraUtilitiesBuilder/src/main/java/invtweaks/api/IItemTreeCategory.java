// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package invtweaks.api;

import java.util.List;
import java.util.Collection;

public interface IItemTreeCategory
{
    boolean contains(final IItemTreeItem p0);
    
    void addCategory(final IItemTreeCategory p0);
    
    void addItem(final IItemTreeItem p0);
    
    Collection<IItemTreeCategory> getSubCategories();
    
    Collection<List<IItemTreeItem>> getItems();
    
    String getName();
    
    int getCategoryOrder();
    
    int findCategoryOrder(final String p0);
    
    int findKeywordDepth(final String p0);
}


