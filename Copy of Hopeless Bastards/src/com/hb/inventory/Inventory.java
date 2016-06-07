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
		this.dimension = dimension;/*hányszor hanyas mátrixot reprezentál az inventory (6x6)*/
		this.handler = handler;
		
	}
	
	public void init(){
		slots = new ArrayList<Slot>();
		/*Itt töltöm fel az inventory-t 6x6 slottal .A feltöltés elsõ sortól indul mindíg balról jobbra,
		 ha vége egy sornak,akkor a következõ sort megint ballról indítja,azaz a nullától.*/
		
		for(int x = 0;x<dimension;x++){
			for(int y = 0;y<dimension;y++){
				slots.add(new Slot(0 + y*48,0+x*48,handler));
			}
		}
	}
	
	public void toggle(){
		toggle = !toggle; /*ha igaz hamisra,ha hamis igazra állítja*/
		/*Látszódik az inventory vagy sem*/
	}
	
	public void tick(){
		/*Csak akkor mûködik az inventory és slot funkciók,ha az inventory látható*/
		if(toggle){
			for(Slot slot : slots){
				slot.tick();
				slot.setBounds(slot.originalx + (int)handler.player.x - 600, slot.originaly + (int)handler.player.y - 150, slot.width, slot.height);
			}
		}
	}
	
	public void render(Graphics g){
		/*Csak akkor rajzolódik az inventory és slot funkciók,ha az inventory látható*/
		if(toggle){
			for(Slot slot : slots){
				slot.render(g);
			}
		}
	}
	
	public void additem(AbstractItem item){
		/*végigmegy a helyeken,és az elsõ üresbe belerakja*/
		for(Slot slot : slots){
			if(slot.isEmpty()){
				slot.setItem(item);
				break;
			}else{
				/*Itt stackales megy,azaz ha van már piros poti az inventoryban,akkor +1 stackat adunk hozzá
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
		/*Itt konkrét slot sorszámba rakunk itemet.*/
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
