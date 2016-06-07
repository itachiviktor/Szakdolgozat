package com.hb.inventory;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import com.hb.gamestate.Handler;
import com.hb.items.AbstractItem;

public class Inventory {
	private int x;
	private int y;
	
	private int dimension;
	private List<Slot> slots;
	
	private boolean toggle;
	
	private Handler handler;
	
	public Inventory(int x,int y,int dimension,Handler handler) {
		this.x = x;
		this.y = y;
		this.dimension = dimension;/*h�nyszor hanyas m�trixot reprezent�l az inventory (6x6)*/
		this.handler = handler;
		
	}
	
	public void init(){
		slots = new ArrayList<Slot>();
		/*Itt t�lt�m fel az inventory-t 6x6 slottal .A felt�lt�s els� sort�l indul mind�g balr�l jobbra,
		 ha v�ge egy sornak,akkor a k�vetkez� sort megint ballr�l ind�tja,azaz a null�t�l.*/
		
		for(int x = 0;x<dimension;x++){
			for(int y = 0;y<dimension;y++){
				slots.add(new Slot(0 + y*48,0+x*48,handler));
			}
		}
	}
	
	public void toggle(){
		toggle = !toggle; /*ha igaz hamisra,ha hamis igazra �ll�tja*/
		/*L�tsz�dik az inventory vagy sem*/
	}
	
	public void tick(){
		/*Csak akkor m�k�dik az inventory �s slot funkci�k,ha az inventory l�that�*/
		if(toggle){
			for(Slot slot : slots){
				slot.tick();
				slot.setBounds(slot.originalx + (int)handler.player.x - 600, slot.originaly + (int)handler.player.y - 150, slot.width, slot.height);
			}
		}
	}
	
	public void render(Graphics g){
		/*Csak akkor rajzol�dik az inventory �s slot funkci�k,ha az inventory l�that�*/
		if(toggle){
			for(Slot slot : slots){
				slot.render(g);
			}
		}
	}
	
	public void additem(AbstractItem item){
		/*v�gigmegy a helyeken,�s az els� �resbe belerakja*/
		for(Slot slot : slots){
			if(slot.isEmpty()){
				slot.setItem(item);
				break;
			}else{
				/*Itt stackales megy,azaz ha van m�r piros poti az inventoryban,akkor +1 stackat adunk hozz�
				 persze , ha a stack limitet ez nem haladja meg*/
				if(slot.hasSameID(item)){
					if(!slot.isFull()){
						slot.addItemToStack(item);
						break;
					}
				}
			}
		}
	}
	
	public void additem(AbstractItem item,int slotNum){
		/*Itt konkr�t slot sorsz�mba rakunk itemet.*/
		if(slotNum < slots.size()){
			Slot slot = slots.get(slotNum);
			
			if(slot.isEmpty()){
				slot.setItem(item);
			}else{
				if(slot.hasSameID(item)){
					if(!slot.isFull()){
						slot.addItemToStack(item);
					}
				}
			}
		}else{
			throw new NumberFormatException(); 
		}
	}
}
