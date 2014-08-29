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

public abstract class ItemAction {

	protected Plugin plugin;
	private ItemStack item; 
	private Player player;
	
	//----------------------------------------------------------
	// get the plugin that is associated with the
	// implementation
	public final Plugin getPlugin() {
		return plugin;
	}
	
	//----------------------------------------------------------
	// get the item that was used, or otherwise involved
	// in the event
	public final ItemStack getItem() {
		return item;
	}
	
	//----------------------------------------------------------
	// get the player that owns/owned the item
	public final Player getPlayer() {
		return player;
	}
	
	//----------------------------------------------------------
	// when player interacts with something (or air) while holding this item
	public void onInteract( PlayerInteractEvent event ) {
		// default: nothing
	}
	
	//----------------------------------------------------------
	// when player right clicks an entity with this item
	public void onInteractEntity( PlayerInteractEntityEvent event ) {
		// default: nothing
	}
	
	//----------------------------------------------------------
	// when player right clicks the item in his inventory
	// return false to allow the item to be picked up. (normal behavior)
	// return true to block the item from being picked up. (action is performed instead)
	public boolean onRightClickInventory( Player player ) {
		// default: nothing
		return false;
	}
	
	//----------------------------------------------------------
	// when the player damages an entity
	// the event is forwarded to all MagicItems the player is wearing
	// (the item in his hand and his armor)
	public void onDamageEntity( EntityDamageByEntityEvent event ) {
		// default: nothing
	}
	
	//----------------------------------------------------------
	// when the player is hurt
	// the event is forwarded to all MagicItems the player is wearing
	// (the item in his hand and his armor)
	public void onHurt( EntityDamageByEntityEvent event ) {
		// default: nothing
	}
	
	//----------------------------------------------------------
	// when a player eats or drinks this item
	// warning: cancelling this event has weird side effects
	//  such as a potion going in the head armor slot deleting anything there.
	public void onConsume( PlayerItemConsumeEvent event ) {
		// default: nothing
	}

	//----------------------------------------------------------
	// when this item breaks
	// only happens if "breakable" is true
	public BreakResult onBreak( PlayerItemBreakEvent event ) {
		// default: nothing
		return BreakResult.CONTINUE;
	}
	
	//----------------------------------------------------------
	// when the player switches to this item in his hand
	public void onEquip( PlayerItemHeldEvent event ) {
		// default: nothing
	}
	
	//----------------------------------------------------------
	// when a player is picking up this item from the ground
	public void onPickup( PlayerPickupItemEvent event ) {
		// default: nothing
	}

	//----------------------------------------------------------
	// when this item is bound to the player's soul
	// called by SoulBinder plugin
	public void onSoulbind( Player player ) {
		// default: nothing
	}
	
	//----------------------------------------------------------
	// called when players are splashed by this item (splash potion)
	public void onSplash( PotionSplashEvent event ) {
		// default: nothing
	}
	
	//----------------------------------------------------------
	void setup( Plugin plugin, ItemStack item, Player player ) {
		this.plugin = plugin;
		this.item = item;
		this.player = player;
	}
	
}
