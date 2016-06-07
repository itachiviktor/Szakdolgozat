package com.hb.items;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.hb.Id;
import com.hb.gamestate.Handler;
import com.hb.tile.Tile;

public abstract class AbstractItem extends Tile{
	/*Ez az oszt�ly minden item �se,amit minden itemnek meg kell val�s�tania.Ez is lesz�rmazik a Tileb�l,teh�t ez p�lyaelem is egyben.*/
	
	public int itemID;/*Item sz�m azonos�t�ja*/
	public BufferedImage itemImage;/*Item k�pe,amit renderelni kell.*/
	public String itemName;/*Item neve*/
	
	public boolean worldElement = true;/*Ez a v�ltoz� azt tartalmazza,hogy az item a p�ly�hoz tartozik,vagy m�r egy playerhez
	az inventoryba.Alapb�l ez a v�ltoz� true,hisz el�sz�r minden a p�ly�ra ker�l,majd azt�n ha felveszi a player,akkor lesz ez
	false,�s ker�l az inventoryba.*/
	
	public boolean heldover = false;/*Felette van e az eg�r*/
	
	public AbstractItem(int x,int y,int width,int height,Handler handler,String itemName,int itemID,BufferedImage itemImage) {
		super(x, y, width, height,true, Id.ITEM, handler);
		
		this.itemName = itemName;
		this.itemID = itemID;
		this.itemImage = itemImage;
	}
	
	/*Use met�dus az,hogy az item tudja mag�r�l,ha az inventoryban van,�s haszn�lja a player,akkor mit kell csin�lnia
	 pl.megn�veli a player �leterej�t,p�nzt ad neki stb....*/
	public abstract void use();
	public abstract void tick();
	public abstract void render(Graphics g);
	
}
