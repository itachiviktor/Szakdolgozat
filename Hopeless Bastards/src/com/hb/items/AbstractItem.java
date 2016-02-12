package com.hb.items;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.hb.Id;
import com.hb.gamestate.Handler;
import com.hb.tile.Tile;

public abstract class AbstractItem extends Tile{
	/*Ez az osztály minden item õse,amit minden itemnek meg kell valósítania.Ez is leszármazik a Tileból,tehát ez pályaelem is egyben.*/
	
	public int itemID;/*Item szám azonosítója*/
	public BufferedImage itemImage;/*Item képe,amit renderelni kell.*/
	public String itemName;/*Item neve*/
	
	public boolean worldElement = true;/*Ez a változó azt tartalmazza,hogy az item a pályához tartozik,vagy már egy playerhez
	az inventoryba.Alapból ez a változó true,hisz elõször minden a pályára kerül,majd aztán ha felveszi a player,akkor lesz ez
	false,és kerül az inventoryba.*/
	
	public boolean heldover = false;/*Felette van e az egér*/
	
	public AbstractItem(int x,int y,int width,int height,Handler handler,String itemName,int itemID,BufferedImage itemImage) {
		super(x, y, width, height,true, Id.ITEM, handler);
		
		this.itemName = itemName;
		this.itemID = itemID;
		this.itemImage = itemImage;
	}
	
	/*Use metódus az,hogy az item tudja magáról,ha az inventoryban van,és használja a player,akkor mit kell csinálnia
	 pl.megnöveli a player életerejét,pénzt ad neki stb....*/
	public abstract void use();
	public abstract void tick();
	public abstract void render(Graphics g);
	
}
