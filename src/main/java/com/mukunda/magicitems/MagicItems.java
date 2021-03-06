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
  
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration; 
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;  
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * MagicItems Bukkit plugin
 * 
 * @author mukunda
 *
 */
public class MagicItems extends JavaPlugin implements Listener, MagicItemsAPI {

	private static MagicItems context;
	
	//-----------------------------------------------------------------------------------
	public static MagicItems getContext() {
		return context;
	}
	
	public static MagicItemsAPI getAPI() {
		return context;
	}

	//-----------------------------------------------------------------------------------
	// loaded config files
	private HashMap<String,Definition> definitions;
	
	//-----------------------------------------------------------------------------------
	public void reloadDefinitions() {
		definitions.clear();
		loadDefinitions();
	}

	//-----------------------------------------------------------------------------------
	private void loadDefinitions() {
		loadDefinitions(null);
	}

	//-----------------------------------------------------------------------------------
	private void loadDefinitions( File folder ) {
		if( folder == null ) folder = new File( getDataFolder(), "items" );
		File[] itemFiles = folder.listFiles();
		for( File file : itemFiles ) {

			if( file.isDirectory() ) {
				loadDefinitions( folder );
				continue;
			}

			if( !file.isFile() ) continue;
			if( !file.getName().endsWith(".yml") ) continue;

			FileConfiguration config = YamlConfiguration.loadConfiguration( file );
			Set<String> bookListing = config.getKeys(false);

			for( String bookName: bookListing ) {
				if( !config.isConfigurationSection( bookName ) ) {
					getLogger().warning( "Rogue key \""+bookName+"\" in book definition file." );
					continue;
				}

				try {
					if( definitions.containsKey( bookName ) ) {
						getLogger().warning( "Duplicate book name found: \""+bookName+"\". Skipping." );
						continue;
					}
					Definition book = new Definition( config.getConfigurationSection( bookName ) );
					definitions.put( bookName, book );
					getLogger().info( "Loaded: " + bookName );

				} catch( ItemConfigException e ) {
					getLogger().warning( "Couldn't load \""+ bookName +"\" - " + e.getMessage() );
				}
			} 
		}
	}

	//-----------------------------------------------------------------------------------
	public Definition getDefinition( String name ) {
		return definitions.get( name );
	}

	//-----------------------------------------------------------------------------------
	public Definition getDefinition( ItemStack item ) {
		if( item == null ) return null;
		String name = Definition.tryGetName( item );
		if( name == null ) return null;
		return getDefinition(name);
	}

	//-----------------------------------------------------------------------------------
	@Override
	public void onEnable() {
		context = this;
		definitions = new HashMap<String,Definition>();

		try {
			Files.createDirectories( getDataFolder().toPath().resolve("items") );
		} catch( IOException e ) {
			getLogger().severe( "Couldn't setup data folders." );
		}

		loadDefinitions();
		
		getServer().getPluginManager().registerEvents( this, this );
	}
	
	//-----------------------------------------------------------------------------------
	@Override
	public void onDisable() {
		context = null;
	}

	//-----------------------------------------------------------------------------------
	@SuppressWarnings("deprecation")
	public static void giveItemToPlayer( Player player, ItemStack item ) {
		player.getInventory().addItem( item );
		player.updateInventory();
		player.sendMessage( ChatColor.GREEN + "Here you go.");
	}

	//-----------------------------------------------------------------------------------
	@Override 
	public boolean onCommand( CommandSender sender, Command cmd, String label, String[] args ) {
		if( cmd.getName().equalsIgnoreCase("magicitems") ) {
			if( args.length < 1 ) {
				sender.sendMessage( "/magicitems <command>" );
				sender.sendMessage( "  create - create an item" );
				sender.sendMessage( "  reload - reload definitions" );
			} else {
				if( args[0].equalsIgnoreCase( "create" ) ) {
					if( !(sender instanceof Player) ) {
						sender.sendMessage( "This is a player only command." );
						return true;
					}
					
				} else if( args[0].equalsIgnoreCase( "reload" ) ) {
					
				} else if( args[0].equalsIgnoreCase( "enchantedbook" ) ) {
					/*
    				if( !(sender instanceof Player) ) {
    	    			sender.sendMessage( "This is a player only command." );
    	    			return true;
    	    		}
    	    		Player player = (Player)sender;
    				if( args.length < 3 ) {
    					player.sendMessage( "Usage: /magicitems enchantbook <enchant> <level>" );
    				}

    				ItemStack item = new ItemStack( Material.ENCHANTED_BOOK );
    				EnchantmentStorageMeta meta = (EnchantmentStorageMeta)item.getItemMeta();
    				meta.
					 *
					 */
					//TODO
				} else {
					sender.sendMessage( "[MagicItems] Unknown command: " + args[0] ); 
				}
			}
			return true;

		}
		return false;
	}

	//-------------------------------------------------------------------------------------------------
	@EventHandler( priority = EventPriority.HIGH )
	public void onPlayerInteract( PlayerInteractEvent event ) {

		Bukkit.broadcastMessage( "INTERACT, " + event.isCancelled() );
		Bukkit.broadcastMessage( event.getAction().toString() + "," + event.getBlockFace().toString() );
		if( event.hasBlock() ) Bukkit.broadcastMessage( event.getClickedBlock().toString() );
		if( event.hasItem() ) {

			ItemStack item = event.getItem();
			Definition def = getDefinition( item );
			if( def == null ) return;
			ItemAction action = def.createEvent( event.getPlayer(), item );
			action.onInteract( event );
		} 
	}

	//-------------------------------------------------------------------------------------------------
	@EventHandler( priority = EventPriority.HIGH, ignoreCancelled=true )
	public void onPlayerInteractEntity( PlayerInteractEntityEvent event ) {
		Player player = event.getPlayer();
		ItemStack item = player.getItemInHand();
		Definition def = getDefinition( item );
		if( def == null ) return;
		ItemAction action = def.createEvent( event.getPlayer(), item );
		action.onInteractEntity( event );

	}

	//-------------------------------------------------------------------------------------------------
	@EventHandler( priority = EventPriority.HIGH, ignoreCancelled=true )
	public void onInventoryClick( InventoryClickEvent event ) {
		if( event.getAction() == InventoryAction.PICKUP_HALF &&
				event.getClick() == ClickType.RIGHT ) {

			if( event.getWhoClicked() instanceof Player ) {

				Player player = (Player)event.getWhoClicked(); 
				Definition def = getDefinition( event.getCurrentItem() );
				if( def == null ) return;
				ItemAction action = def.createEvent( player, event.getCurrentItem() );
				action.onRightClickInventory( player );
			} 
		} 
	}

	//-------------------------------------------------------------------------------------------------
	private void passDamageEvent( Player player, EntityDamageByEntityEvent event, boolean offense ) {
		// TODO charms

		// check item in hand, and armor slots
		Definition def;
		def = getDefinition( player.getItemInHand() );
		if( def != null ) {
			ItemAction action = def.createEvent( player, player.getItemInHand() );
			if( offense ) {
				action.onDamageEntity( event );
			} else {
				action.onHurt( event );
			}
		}

		ItemStack[] armor = player.getInventory().getArmorContents();
		for( ItemStack item : armor ) {
			def = getDefinition( item );
			if( def != null ) {
				ItemAction action = def.createEvent( player, item );
				if( offense ) {
					action.onDamageEntity( event );
				} else {
					action.onHurt( event );
				}
			}
		}
	}

	//-------------------------------------------------------------------------------------------------
	@EventHandler( priority = EventPriority.HIGH, ignoreCancelled=true )
	public void onDamageEntity( EntityDamageByEntityEvent event ) {

		if( event.getDamager() instanceof Player ) {

			passDamageEvent( (Player)event.getDamager(), event, true ); 
			if( event.isCancelled() ) return; 
		}

		if( event.getEntity() instanceof Player ) {
			// check item in hand, and armor slots
			passDamageEvent( (Player)event.getEntity(), event, true );
		}
	}

	//-------------------------------------------------------------------------------------------------
	@EventHandler
	public void onPlayerItemBreak( PlayerItemBreakEvent event ) {

		ItemStack item = event.getBrokenItem();
		Definition def = getDefinition( item );
		if( def == null ) return;
		if( event.getBrokenItem().getAmount() != 0 ) return ;// another event cancelled the break.
		if( def.getBreakable() ) return;

		ItemAction action = def.createEvent( event.getPlayer(), event.getBrokenItem() );
		BreakResult result = action.onBreak( event );

		if( result == BreakResult.CONTINUE ) return;
		if( result == BreakResult.KEEP_AND_FIX ) {
			event.getBrokenItem().setAmount(1);			
		} else if( result == BreakResult.KEEP_BROKEN ) {
			event.getBrokenItem().setAmount(1);
			event.getBrokenItem().setDurability( 
					(short)(event.getBrokenItem().getType().getMaxDurability()-1) );
		}

	}

	//-------------------------------------------------------------------------------------------------
	@EventHandler( priority = EventPriority.HIGH, ignoreCancelled=true )
	public void onPlayerConsumeItem( PlayerItemConsumeEvent event ) {
		ItemStack item = event.getItem();
		Definition def = getDefinition( item );
		if( def == null ) return;

		ItemAction action = def.createEvent( event.getPlayer(), item );
		action.onConsume( event );
	}

	//-------------------------------------------------------------------------------------------------
	@EventHandler( priority = EventPriority.HIGH, ignoreCancelled=true )
	public void onPlayerEquipItem( PlayerItemHeldEvent event ) {
		ItemStack item = event.getPlayer().getInventory().getItem( event.getNewSlot() );
		Definition def = getDefinition( item );
		if( def == null ) return;

		ItemAction action = def.createEvent( event.getPlayer(), item );
		action.onEquip( event );
	}

	//-------------------------------------------------------------------------------------------------
	@EventHandler( priority = EventPriority.HIGH, ignoreCancelled=true )
	public void onPlayerPickupItem( PlayerPickupItemEvent event ) {
		ItemStack item = event.getItem().getItemStack();
		Definition def = getDefinition( item );
		if( def == null ) return;

		ItemAction action = def.createEvent( event.getPlayer(), item );
		action.onPickup( event );
	}
	 
	//-------------------------------------------------------------------------------------------------
	@EventHandler( priority = EventPriority.HIGH, ignoreCancelled=true )
	public void onPotionSplashEvent( PotionSplashEvent event ) {
		ThrownPotion potion = event.getPotion();
		Definition def = getDefinition( potion.getItem() );
		if( def == null ) return;

		Player source = null;
		if( potion.getShooter() instanceof Player ) {
			source = (Player)potion.getShooter();
		} 

		ItemAction action = def.createEvent( source, potion.getItem() );
		action.onSplash( event );
	} 

	/**************************************************************************
	 * {@inheritDoc}
	 **************************************************************************/
	@Override
	public boolean isMagicItem( ItemStack item ) { 
		if( item == null ) return false;
		ItemMeta meta = item.getItemMeta();
		if( !meta.hasLore() ) return false;
		return meta.getLore().get(0).contains( Definition.LOREMETA_TAG );
	}
	
	/**************************************************************************
	 * {@inheritDoc}
	 **************************************************************************/
	@Override
	public Object fireCustomAction( ItemStack item, Player player, 
			String action, Object...data ) {
		
		Definition def = getDefinition( item );
		if( def == null ) return new NotAMagicItem();

		ItemAction event = def.createEvent( player, item );
		return event.onCustom( action, data );
	}
	
	/**************************************************************************
	 * {@inheritDoc}
	 **************************************************************************/
	@Override
	public ItemStack createItem( String name ) {
		if( name == null ) return null;
		Definition def = getDefinition( name );
		if( def == null ) return null;
		return def.createItem();
	}
}
