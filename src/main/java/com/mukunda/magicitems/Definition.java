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

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection; 
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin; 

import com.mukunda.loremeta.LoreMeta;
import com.mukunda.loremeta.MetaKeyText;

public class Definition {
	
	public static final String LOREMETA_TAG = "MITM";

	//public static final String MAGIC_CODE = ChatColor.COLOR_CHAR + "\u0120";

	private String name;
	private String title;
	private ItemType type; 
	private boolean breakable;
	private double manaCost = 5;
	private String[] lore;
	private String plugin;
	private Plugin pluginInstance;
	private String className;
	private Class<?> classT;
	private Material material;

	private class EnchantmentLevel {
		public Enchantment enchantment;
		public int level;

		public EnchantmentLevel( Enchantment enchantment, int level ) {
			this.enchantment = enchantment;
			this.level = level;
		}
	}

	private List<EnchantmentLevel> safeEnchantments;
	private List<EnchantmentLevel> unsafeEnchantments;

	public Definition( ConfigurationSection config ) throws ItemConfigException {

		this.name = config.getName();
		type = ItemType.getType( config.getString( "type" ) );
		if( type == null ) 
			throw new ItemConfigException( "Missing or unknown TYPE." );

		title = config.getString( "title" );
		if( title == null || title.isEmpty() ) 
			throw new ItemConfigException( "Missing TITLE." );

		manaCost = config.getDouble( "mana_cost", 0 );

		List<String> itemLore = config.getStringList("lore");
		lore = itemLore.toArray( new String[ itemLore.size() ] );

		plugin = config.getString( "plugin" );
		if( plugin != null && plugin.isEmpty() ) plugin = null;

		className = config.getString( "class" );
		if( className == null && className.isEmpty() ) className = null;

		breakable = config.getBoolean( "breakable", true );

		if( config.isString( "material" ) ) {
			if( type == ItemType.BOOK ) {
				throw new ItemConfigException( "MATERIAL is not allowed for BOOKS." );
			} else {
				material = Material.getMaterial( config.getString("material").toUpperCase() );
				if( material == null ) {
					throw new ItemConfigException( "Unknown MATERIAL." );
				}
			}
		} else {
			if( type != ItemType.BOOK ) {
				throw new ItemConfigException( "Missing MATERIAL." );
			} else {
				material = Material.ENCHANTED_BOOK;
			}
		}

		safeEnchantments = new ArrayList<EnchantmentLevel>();	
		unsafeEnchantments = new ArrayList<EnchantmentLevel>();

		if( config.isConfigurationSection("enchant") ) {
			safeEnchantments = readEnchantments( 
					config.getConfigurationSection("enchant") );
		}

		if( config.isConfigurationSection("unsafe_enchant") ) {
			unsafeEnchantments = readEnchantments( 
					config.getConfigurationSection("unsafe_enchant") );
		}


	}

	private List<EnchantmentLevel> readEnchantments( ConfigurationSection config ) throws ItemConfigException {
		ArrayList<EnchantmentLevel> list = new ArrayList<EnchantmentLevel>();
		for( String key : config.getKeys(false) ) {
			if( config.isInt(key) ) {
				// validate enchantment name
				Enchantment enchantment = Enchantment.getByName( key.toUpperCase() );
				if( enchantment == null ) {
					throw new ItemConfigException( "Bad enchantment name." );
				}
				list.add( new EnchantmentLevel( enchantment, config.getInt(key) ) );
			} else {
				throw new ItemConfigException( "Bad enchantment entry; format is \"name: level\"" );
			}
		}
		return list;
	}

	public String getName() {
		return name;
	}

	public String getTitle() {
		return title;
	}

	public double getManaCost() {
		return manaCost;
	}

	public String[] getLore() {
		return lore;
	}

	public String getClassName() {
		return className;
	}

	public boolean getBreakable() {
		return breakable;
	}

	// todo - use LoreMeta
	/*
	private String encodeName() {
		StringBuilder builder = new StringBuilder();
		builder.append( MAGIC_CODE );
		for( int i = 0; i < name.length(); i++ ) {
			builder.append( ChatColor.COLOR_CHAR );
			builder.append( name.charAt(i) );
		}
		return builder.toString();
	}*/
/*
	private static String decodeName( String source ) {
		int index = source.indexOf( MAGIC_CODE ) ;
		if( index == -1 ) return null;
		StringBuilder builder = new StringBuilder();
		for( int i = index+3; i < source.length(); i += 2 ) {
			builder.append( source.charAt(i) );
		}
		return builder.toString();
	}*/

	private static String convertColorCodes( String source ) {
		String str = source.replace( "&", "\u00A7" );
		str = str.replace( "\u00A7\u00A7", "&" );
		return str;
	}

	public ItemStack createItem() {
		ItemStack item = new ItemStack( material );
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName( convertColorCodes(title) );
		ArrayList<String> lore = new ArrayList<String>();
		for( String loreEntry : lore ) {
			lore.add( convertColorCodes(loreEntry) );
		}
		meta.setLore( lore );
		item.setItemMeta( meta );
		
		LoreMeta.setData( 
				item, new MetaKeyText( LOREMETA_TAG ), getName() );

		// apply enchantments
		
		for( EnchantmentLevel enchant : safeEnchantments ) {
			try {
				item.addEnchantment( enchant.enchantment, enchant.level );
			} catch( IllegalArgumentException e ) {
				MagicItems.getContext().getLogger().info( 
						"Invalid enchantment for item \""+item.getType().toString()+"\": " + 
						enchant.enchantment.getName() );
				return null;

			}
		}

		for( EnchantmentLevel enchant : unsafeEnchantments ) {
			item.addUnsafeEnchantment( enchant.enchantment, enchant.level );
		}

		return item;
	}

	public static String tryGetName( ItemStack item ) {
		if( item == null ) return null;
		return LoreMeta.getData( item, new MetaKeyText(LOREMETA_TAG) );
	}

	public ItemAction getActionInstance() {
		if( className == null ) return null;

		if( classT == null ) {
			try {
				classT = Class.forName( className );
			} catch (ClassNotFoundException e) {
				MagicItems.getContext().getLogger().warning( "Couldn't find item class \""+className+"\"." );
				return null;
			}

			if( !ItemAction.class.isAssignableFrom(classT) ) {
				classT = null;
				MagicItems.getContext().getLogger().warning( "Item class is not compatible. \""+className+"\"." );
				return null;
			}
		}

		try {
			return (ItemAction) classT.newInstance();
		} catch (InstantiationException e) {
			MagicItems.getContext().getLogger().warning( "Couldn't instantiate item class \""+className+"\". " + e.getMessage() );
		} catch (IllegalAccessException e) {
			MagicItems.getContext().getLogger().warning( "Couldn't instantiate item class \""+className+"\". " + e.getMessage() );
		}
		return null;
	}

	public ItemAction createEvent( Player player, ItemStack item ) {
		ItemAction action = getActionInstance();
		if( action == null ) return null;

		Plugin pl = null;
		if( pluginInstance == null ) {
			if( plugin != null ) {
				pluginInstance = Bukkit.getPluginManager().getPlugin( plugin );
				if( pluginInstance == null ) return null; // plugin not loaded.
				pl = pluginInstance;
				if( !pl.isEnabled() ) return null;
			}
		} else {
			pl = pluginInstance;
			if( !pl.isEnabled() ) return null;
		}

		action.setup( pl, item, player );
		return action;
	}
}
