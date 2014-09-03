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

/**
 * The different types of MagicItems
 * 
 * @author mukunda
 *
 */
public enum ItemType {
	
	/**
	 * A magic item that is locked to being an Enchanted Book.
	 * 
	 */
	BOOK,
	
	/**
	 * An item that is ideally a weapon material. I think
	 * it can be other materials too though...
	 */
	WEAPON, 
	
	/**
	 * An item that is ideally an armor material. I think
	 * it can be other materials too though...
	 */
	ARMOR,
	
	/**
	 * Must be a tool, such as a pickaxe or axe or hoe.
	 * 
	 */
	TOOL, 
	
	/**
	 * A splash potion.
	 * 
	 */
	SPLASH, 
	
	/**
	 * A consumable item, such as bread, or an apple, or a potion.
	 * 
	 */
	CONSUMABLE;
 
	/**
	 * Translate a string into an ItemType
	 * 
	 * @param string String to test, must equal one of the item type names.
	 * @return       ItemType made from string.
	 */
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
