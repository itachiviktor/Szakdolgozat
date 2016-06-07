package com.hb.items;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import com.hb.entity.DamagingText;
import com.hb.gamestate.Handler;

public class HealthPotion extends AbstractItem{
	/*Ez már egy pályaelem,ami item is egyben,ami azt jelenti,hogy felvehetõ a pályáról,és berakható az inventoryba.*/
	
	/*A ManaPotion is hasonló mûködésû.*/
	
	public HealthPotion(int x,int y,int width,int height,Handler handler,String itemName,int itemID,BufferedImage itemImage) {
		super(x,y,width,height,handler,itemName,itemID,itemImage);
	}
	
	@Override
	public void tick() {
		/*Csak akkor hajtódik végre a tick metódus belseje, ha az egér felette van, és a pályához(world)tartozik.*/
		if(getBounds().contains(handler.mouse) && this.worldElement){
			heldover = true;
			if(handler.leftClick){
				/*Ha balgombbal rákattintok ,akkor belerakja az inventoryba,innentõl kezdve nem pályaelem,tehát
				 a worldelement változó értékét falsera kell állítani, és eltüntetjük a Tile listából is.*/
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
			/*Az item csak akkor rajzolja ki magát, ha pályaelem,ha inventoryban van,akkor a befoglaló Stack fogja kirajzolni a képét.*/
			g.drawImage(itemImage, x, y, width,height,null);
			if(this.heldover){
				g.drawRect(x, y, width, height);
			}
		}
	}
	
	/*Ha inventoryban van, akkor ha a befoglaló slotra kattintanak,akkor az nem tudja mit csináljon,ezért megkérdezi az itemtõl,
	 azaz meghívja a use metódusát,hogy csinálja amire való,hisz a player aktiválta.*/
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