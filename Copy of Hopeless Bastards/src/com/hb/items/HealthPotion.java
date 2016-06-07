package com.hb.items;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import com.hb.entity.DamagingText;
import com.hb.gamestate.Handler;

public class HealthPotion extends AbstractItem{
	/*Ez m�r egy p�lyaelem,ami item is egyben,ami azt jelenti,hogy felvehet� a p�ly�r�l,�s berakhat� az inventoryba.*/
	
	/*A ManaPotion is hasonl� m�k�d�s�.*/
	
	public HealthPotion(int x,int y,int width,int height,Handler handler,String itemName,int itemID,BufferedImage itemImage) {
		super(x,y,width,height,handler,itemName,itemID,itemImage);
	}
	
	@Override
	public void tick() {
		/*Csak akkor hajt�dik v�gre a tick met�dus belseje, ha az eg�r felette van, �s a p�ly�hoz(world)tartozik.*/
		if(getBounds().contains(handler.mouse) && this.worldElement){
			heldover = true;
			if(handler.leftClick){
				/*Ha balgombbal r�kattintok ,akkor belerakja az inventoryba,innent�l kezdve nem p�lyaelem,teh�t
				 a worldelement v�ltoz� �rt�k�t falsera kell �ll�tani, �s elt�ntetj�k a Tile list�b�l is.*/
				this.worldElement = false;
				handler.player.inventory.additem(this);
				handler.tile.remove(this);
			}
		}else{
			heldover = false;
		}
	}
	
	@Override
	public void render(Graphics g) {
		if(worldElement){
			/*Az item csak akkor rajzolja ki mag�t, ha p�lyaelem,ha inventoryban van,akkor a befoglal� Stack fogja kirajzolni a k�p�t.*/
			g.drawImage(itemImage, x, y, width,height,null);
			if(this.heldover){
				g.drawRect(x, y, width, height);
			}
		}
	}
	
	/*Ha inventoryban van, akkor ha a befoglal� slotra kattintanak,akkor az nem tudja mit csin�ljon,ez�rt megk�rdezi az itemt�l,
	 azaz megh�vja a use met�dus�t,hogy csin�lja amire val�,hisz a player aktiv�lta.*/
	public void use(){
		handler.player.setHealth(50);
		handler.damagetext.add(new DamagingText(handler.player.x, handler.player.y, String.valueOf(50),false, handler));
	}

	public int getItemID() {
		return itemID;
	}
	
	public BufferedImage getItemImage() {
		return itemImage;
	}
	
	public String getItemName() {
		return itemName;
	}
	
	public Rectangle getBounds(){
		return new Rectangle(x,y,width,height);
	}
}