package com.hb.inventory;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import com.hb.gamestate.Handler;
import com.hb.items.AbstractItem;


public class Slot extends Rectangle{
	
	/*Az inventoryban l�v� hely.Egy inventory 36 helyet,azaz slotot tartalmaz.A slot t�rolja az itemet.*/
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Font font = new Font("Calibri",20,20);/*A stackel�st ki�r� sz�m bet�t�pusa*/
	private AbstractItem item;/*A slot tartalmaz egy itemet, �s az ha t�bb van,akkor itt stackel�dhet.Az itemek �s�t
	ismeri,ez b�rmilyen item t�pust felvehet.*/
	private int slotID;/*Slotazonos�t� sz�m*/

	private int size = 48;/*Slot m�rete pixelben*/
	
	private boolean isHeldOver;
	private int maxStack = 64;/*Maximum stactsize*/
	private int currentStack = 0;
	
	private Handler handler;
	
	
	/*A slotokkal az a probl�ma,hogy nekik is mopzogni kell a playerrel,viszont a slotok poz�ci�j�t alapb�l sz�molni kell,
	 ez�rt az originalx,y tartalmazza az eredeti poz�ci�t.Az x,y koordin�ta az viszont mind�g a playerhez igazodik,
	 azok �rt�ke folymatasoan v�ltozik.*/
	public int originalx;
	public int originaly;
	
	
	public Slot(int x,int y,Handler handler) {
		this.x = x;
		this.y = y;
		this.originalx = x;
		this.originaly = y;
		
		setBounds(x,y,size,size);
		this.handler = handler;
	}
	
	public void tick(){
		//setBounds(x,y,size,size);
		
		if(currentStack == 0){
			/*a stack az,hogy pl egy potib�l 5 db van,akkor az egym�sra rakva egy helyet foglal az �t,ha viszont elhaszn�lom,
			 �s a stacksz�m lecs�kken null�ra,akkor el kell t�vol�tani az itemet onna.Teh�t ha item nem null,akkor
			 ott van valami pedig 0 a stack,akkor clear m,et�dus kipucolja a Slotot.*/
			if(item != null){
				clear();
			}
		}
		
		/*Itt lek�rdezem a handler objektumt�l az eg�r poz�ci�j�t, �s a slot eld�nti,hogy felette �ll-e vagy sem az eg�r.*/
		if(this.contains(handler.mouse)){
			isHeldOver = true;
		}else{
			isHeldOver = false;
		}
		
		if(isLeftClicked()){
			/*Ha a slot felett bal eg�rgombbal kattintunk,akkor a slotban l�v� item hat�sa �letbe l�p, amit a slot nemtud
			 teh�t az item use() met�dusa hat�rozza meg, ha az item stackje 1,akkor elt�nik a slotb�l az izem,
			 ha t�bb mint 1, akkor eggyel cs�kkentj�k a stacket.*/
			if(currentStack == 1){
				item.use();
				clear();
			}else if(currentStack > 1){
				item.use();
				currentStack--;
			}
		}
		
	}
	
	public void render(Graphics g){
		/*Ha van a slotban item akkor az itemmel egy�tt rajzol,ha nincs akkor csak a keretet rajzolja ki....*/
		
		if(item != null){
			/*item is added to the slot*/
			g.drawImage(item.itemImage, x, y, size,size,null);
			
			g.setFont(this.font);
			g.setColor(Color.yellow);
			
			g.drawString(currentStack + "", x+size/2, y+size);
			g.setColor(Color.white);
			
		}
		
		if(isHeldOver){
			g.setColor(Color.green);
		}
		
		g.drawRect(x, y, size-1, size-1);
		
		g.setColor(Color.white);
		
	}
	
	public void setItem(AbstractItem item){
		this.item = item;
		slotID = item.itemID;
		currentStack+=1;
	}
	
	private void clear() {
		/*Item kit�rl�se a slotb�l*/
		slotID = 0;
		item = null;
		currentStack = 0;
	}
	
	public AbstractItem getItem(){
		return this.item;
	}
	
	public int getSlotID() {
		return slotID;
	}
	
	public int getCurrentStack() {
		return currentStack;
	}
	
	
	public void setCurrentStack(int currentStack) {
		this.currentStack = currentStack;
	}
	
	
	public boolean isEmpty(){
		return (item == null && slotID == 0);
	}
	
	public boolean hasSameID(AbstractItem item){
		/*Ez a met�dus azt vizsg�lja,hogy a param�terben kapott item azonos�t�ja megeggyezik-e a m�r a slotban l�v� item
		 azonos�t�j�val,mert ha igen,akkor stackelni kell.*/
		if(item.itemID == slotID){
			return true;
		}else{
			return false;
		}
	}
	
	public void addItemToStack(AbstractItem item){
		currentStack+=1;
		
	}
	
	public boolean isFull(){
		if(currentStack < maxStack){
			return false;
		}else{
			return true;
		}
	}
	
	public boolean isLeftClicked(){
		if(isHeldOver){
			if(handler.leftClick){
				handler.leftClick = false;
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	public boolean isRightClicked(){
		/*azt vizsg�lja,hogy jobb eg�rgombbal kattintott-e a user a slotra.*/
		if(isHeldOver){
			if(handler.rightClick){
				handler.rightClick = false;
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	public boolean hasItem(){
		/*Itt megvizsg�ljuk,hogy van-e a slotban item*/
		if(item != null){
			if(slotID != 0){
				return true;/*Itt biztosan van item*/
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
}
