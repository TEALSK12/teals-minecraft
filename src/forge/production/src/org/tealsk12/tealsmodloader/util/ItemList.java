package org.tealsk12.tealsmodloader.util;

import net.minecraft.item.Item;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;

/**
 * @author Connor Hollasch
 * https://github.com/CHollasch
 */
public class ItemList extends ArrayList<Item> {

    /**
     * Create an item list by passing in an unbound number of items as parameters.
     * ItemList extends an ArrayList of items so it behaves exactly the same.
     *
     * @param list list of all the items to add.
     */
    public ItemList(Item... list) {
        addAll(Arrays.asList(list));
    }
}
