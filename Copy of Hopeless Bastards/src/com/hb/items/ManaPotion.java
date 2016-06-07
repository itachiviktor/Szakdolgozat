package com.hb.items;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import com.hb.gamestate.Handler;


public class ManaPotion extends AbstractItem{
	
	public ManaPotion(int x,int y,int width,int height,Handler handler,String itemName,int itemID,BufferedImage itemImage) {
		super(x,y,width,height,handler,itemName,itemID,itemImage);
	}
	
	@Override
	public void tick() {
		if(getBounds().contains(handler.mouse) && this.worldElement){
			heldover = true;
			if(handler.leftClick){
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
			g.drawImage(itemImage, x, y, width,height,null);
			if(this.heldover){
				g.drawRect(x, y, width, height);
			}
		}
	}
	
	public void use(){
		handler.player.mana+=50;
	}
}
