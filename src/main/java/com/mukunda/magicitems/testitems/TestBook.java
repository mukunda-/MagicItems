package com.mukunda.magicitems.testitems;

import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerInteractEvent;

import com.mukunda.magicitems.ItemAction;

public class TestBook extends ItemAction {

	public void onInteract( PlayerInteractEvent event ) {
		Bukkit.broadcastMessage( "BOOK INTERACT." );
	}
	
	public Object onCustom( String action, Object ... data  ) {
		Bukkit.broadcastMessage( "ONCUSTOM action=" + action );
		
		return null;
	}
}
