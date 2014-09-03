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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

/******************************************************************************
 * Base class to be implemented by plugins to handle MagicItem actions.
 * 
 * @author mukunda
 *
 ******************************************************************************/
public abstract class ItemAction {

	private Plugin plugin;
	private ItemStack item; 
	private Player player;
	
	/**************************************************************************
	 * Get the plugin that is associated with this implementation
	 * 
	 * @return Bukkit plugin
	 **************************************************************************/
	public final Plugin getPlugin() {
		return plugin;
	}
	
	/**************************************************************************
	 * Get the item that was used, or otherwise involved in the event.
	 * 
	 * @return ItemStack
	 **************************************************************************/
	public final ItemStack getItem() {
		return item;
	}
	
	/**************************************************************************
	 * Get the player that was involved in the event.
	 * 
	 * @return The Player that owns/owned the item, or was otherwise the
	 * player primarily involved in the event. null if there is no player
	 * attached.
	 **************************************************************************/
	public final Player getPlayer() {
		return player;
	}
	
	/**************************************************************************
	 * Called when a player interacts with something (or air) while holding
	 * this item.
	 * 
	 * @param event Forwarded Bukkit event
	 **************************************************************************/
	public void onInteract( PlayerInteractEvent event ) {
		// default: nothing
	}
	
	/**************************************************************************
	 * Called when the player right clicks on an entity while holding this
	 * item.
	 * 
	 * @param event Forwarded Bukkit event
	 **************************************************************************/
	public void onInteractEntity( PlayerInteractEntityEvent event ) {
		// default: nothing
	}
	
	/**************************************************************************
	 * Called when a player right clicks this item in his inventory
	 * 
	 * TODO: only his inventory? or any inventory?
	 * 
	 * 
	 * @param player Player that clicked the item
	 * @return       false to allow normal action (picking up the item), true
	 *               to block the pickup action.
	 **************************************************************************/
	public boolean onRightClickInventory( Player player ) {
		// default: nothing
		return false;
	}
	
	/**************************************************************************
	 * Called when a player damages an entity while holding or wearing this
	 * item.
	 * 
	 * This event is forwarded to all MagicItems the player is wearing.
	 * (the item in his hand and his armor slots)
	 * 
	 * @param event Forwarded Bukkit event.
	 **************************************************************************/
	public void onDamageEntity( EntityDamageByEntityEvent event ) {
		// default: nothing
	}
	
	/**************************************************************************
	 * Called when a player is hurt while holding or wearing this item.
	 * 
	 * This event is forwarded to all MagicItems the player is wearing.
	 * (the item in his hand and his armor slots)
	 * 
	 * @param event Forwarded Bukkit event.
	 **************************************************************************/
	public void onHurt( EntityDamageByEntityEvent event ) {
		// default: nothing
	}
	
	/**************************************************************************
	 * Called when a player eats or drinks this item.
	 * 
	 * Warning: As of 9/2/2014, cancelling this event has strange side effects
	 *  such as a potion appearing in the head armor slot (deleting armor) if
	 *  you cancel a potion usage.
	 *   
	 * @param event Forwarded Bukkit event
	 **************************************************************************/
	public void onConsume( PlayerItemConsumeEvent event ) {
		// default: nothing
	}

	/**************************************************************************
	 * Called when this item breaks. 
	 * 
	 * Only happens if "breakable" is true, and if the item material 
	 * is breakable.
	 * 
	 * @param event Forwarded Bukkit event
	 * @return BreakResult.CONTINUE to allow the item to be broken.
	 *         BreakResult.KEEP_AND_FIX to prevent the item from breaking and
	 *         reset its damage to 0.
	 *         BreakResult.KEEP_BROKEN to prevent the item from breaking, but
	 *         keep its damage at maximum.
	 *         
	 **************************************************************************/
	public BreakResult onBreak( PlayerItemBreakEvent event ) {
		// default: nothing
		return BreakResult.CONTINUE;
	}
	
	/**************************************************************************
	 * Called every time a player switches to this item in his hand.
	 * 
	 * @param event Forwarded Bukkit event
	 **************************************************************************/
	public void onEquip( PlayerItemHeldEvent event ) {
		// default: nothing
	}
	
	/**************************************************************************
	 * Called when a player picks up this item from the ground. 
	 * 
	 * @param event Forwarded Bukkit event
	 **************************************************************************/
	public void onPickup( PlayerPickupItemEvent event ) {
		// default: nothing
	}

	/**************************************************************************
	 * Called when a player is splashed by this item (splash potion only)
	 * 
	 * @param event Forwarded Bukkit event
	 **************************************************************************/
	public void onSplash( PotionSplashEvent event ) {
		// default: nothing
	}
	
	/**************************************************************************
	 * Called when this item is bound to the player's soul
	 * 
	 * Requires SoulBinder plugin.
	 * 
	 * @param player Player who the item was bound to.
	 **************************************************************************/
	public void onSoulbind( Player player ) {
		// default: nothing
	}
	
	/**************************************************************************
	 * Called when a plugin calls MagicItems.fireCustomAction( ... )
	 * 
	 * @param action Name of implementation defined action. This is a value
	 * 	             passed in from fireCustomAction, and it may be null.
	 *               
	 * @param data   Optional objects attached to custom action.
	 * @return       Data returned to caller.
	 **************************************************************************/
	public Object onCustom( String action, Object ... data ) {
		// default: nothing
		return null;
	}
	
	//----------------------------------------------------------
	void setup( Plugin plugin, ItemStack item, Player player ) {
		this.plugin = plugin;
		this.item = item;
		this.player = player;
	}
	
}
