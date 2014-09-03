/*
 * MagicItems
 *
 * Copyright (c) 2014 Mukunda Johnson
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.mukunda.magicitems;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Public MagicItems access.
 * 
 * @author mukunda
 */
public interface MagicItemsAPI {

	/************************************************************************************
	 * Check if an item is a MagicItem, i.e. it has MagicItem data associated with it.
	 * 
	 * @param item Item to check
	 * @return     true if the item has embedded MagicItem tags.
	 ************************************************************************************/
	public boolean isMagicItem( ItemStack item );
	
	/************************************************************************************
	 * Send a custom action to a MagicItem
	 * 
	 * @param item   MagicItem to trigger action with.
	 * @param player Player to associate with event, may be null.
	 * @param action Name of action to fire, this is implementation defined, and can even
	 *               be null.
	 * @param data   User data to pass to the handler
	 * @return       If the item is not a valid MagicItem object, this will return a
	 *               NotAMagicItem instance. Otherwise, this returns the data returned
	 *               from the custom handler, which may be null.
	 ************************************************************************************/
	public Object fireCustomAction( ItemStack item, Player player, 
									String action, Object...data );
	
	/************************************************************************************
	 * Create a MagicItem from a loaded configuration.
	 * 
	 * @param name Name of MagicItem to create. Must be defined in configs.
	 * @return     New ItemStack created from the definition, or null if the name
	 *             was invalid or if the definition contained an error.
	 ************************************************************************************/
	public ItemStack createItem( String name );
	
}
