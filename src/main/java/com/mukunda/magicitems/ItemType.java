package com.mukunda.magicitems;

public enum ItemType {
	BOOK, WEAPON, TOOL, SPLASH, CONSUMABLE;
 
	public static ItemType getType( String string ) {
		if( string == null ) return null;
		if( string.equalsIgnoreCase( "book" ) ) {
			return BOOK;
		} else if( string.equalsIgnoreCase( "weapon" ) ) {
			return WEAPON;
		} else if( string.equalsIgnoreCase( "tool" ) ) {
			return TOOL;
		} else if( string.equalsIgnoreCase( "splash" ) ) {
			return SPLASH;
		} else if( string.equalsIgnoreCase( "consumable" ) ) {
			return CONSUMABLE;
		}
		return null;
	}
}
