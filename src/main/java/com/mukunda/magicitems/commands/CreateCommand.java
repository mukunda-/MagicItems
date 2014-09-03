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

package com.mukunda.magicitems.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.mukunda.cmdhandler.CommandGroup;
import com.mukunda.cmdhandler.CommandHandler;
import com.mukunda.magicitems.Definition;
import com.mukunda.magicitems.MagicItems;

//-----------------------------------------------------------------------------
public class CreateCommand extends CommandHandler {

	//-----------------------------------------------------------------------------
	public CreateCommand( CommandGroup parent ) {
		super( parent, "create", 1, true );
	}

	//-----------------------------------------------------------------------------
	@Override
	public void run( String[] args ) {
		Player player = getPlayer();
	
		Definition def = MagicItems.getContext().getDefinition( args[1] );
		if( def == null ) {
			reply( "That name is not recognized." );
			return;
		}
	
		ItemStack item = def.createItem();
		if( item == null ) {
			player.sendMessage( ChatColor.RED + "Couldn't create item. Check logs for details." );
			return;
		}
		MagicItems.giveItemToPlayer( player, item );
	}

	//-----------------------------------------------------------------------------
	@Override
	public void printSyntax() {
		reply( "/magicitems create <name>" );
	}

	//-----------------------------------------------------------------------------
	@Override
	public void printUsage() {
		reply( "Usage: /magicitems create <name>" );
		reply( "Creates a magic item. <name> must be defined in the item configs." );
	}
}
