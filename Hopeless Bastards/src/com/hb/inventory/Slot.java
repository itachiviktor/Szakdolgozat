package com.hb.inventory;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import com.hb.gamestate.Handler;
import com.hb.items.AbstractItem;


public class Slot extends Rectangle{
	
	/*Az inventoryban lévõ hely.Egy inventory 36 helyet,azaz slotot tartalmaz.A slot tárolja az itemet.*/
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Font font = new Font("Calibri",20,20);/*A stackelést kiíró szám betûtípusa*/
	private AbstractItem item;/*A slot tartalmaz egy itemet, és az ha több van,akkor itt stackelõdhet.Az itemek õsét
	ismeri,ez bármilyen item típust felvehet.*/
	private int slotID;/*Slotazonosító szám*/

	private int size = 48;/*Slot mérete pixelben*/
	
	private boolean isHeldOver;
	private int maxStack = 64;/*Maximum stactsize*/
	private int currentStack = 0;
	
	private Handler handler;
	
	
	/*A slotokkal az a probléma,hogy nekik is mopzogni kell a playerrel,viszont a slotok pozícióját alapból számolni kell,
	 ezért az originalx,y tartalmazza az eredeti pozíciót.Az x,y koordináta az viszont mindíg a playerhez igazodik,
	 azok értéke folymatasoan változik.*/
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
			/*a stack az,hogy pl egy potiból 5 db van,akkor az egymásra rakva egy helyet foglal az öt,ha viszont elhasználom,
			 és a stackszám lecsökken nullára,akkor el kell távolítani az itemet onna.Tehát ha item nem null,akkor
			 ott van valami pedig 0 a stack,akkor clear m,etódus kipucolja a Slotot.*/
			if(item != null){
				clear();
			}
		}
		
		/*Itt lekérdezem a handler objektumtól az egér pozícióját, és a slot eldönti,hogy felette áll-e vagy sem az egér.*/
		if(this.contains(handler.mouse)){
			isHeldOver = true;
		}else{
			isHeldOver = false;
		}
		
		if(isLeftClicked()){
			/*Ha a slot felett bal egérgombbal kattintunk,akkor a slotban lévõ item hatása életbe lép, amit a slot nemtud
			 tehát az item use() metódusa határozza meg, ha az item stackje 1,akkor eltûnik a slotból az izem,
			 ha több mint 1, akkor eggyel csökkentjük a stacket.*/
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
		/*Ha van a slotban item akkor az itemmel együtt rajzol,ha nincs akkor csak a keretet rajzolja ki....*/
		
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
		/*Item kitörlése a slotbõl*/
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
		/*Ez a metódus azt vizsgálja,hogy a paraméterben kapott item azonosítója megeggyezik-e a már a slotban lévõ item
		 azonosítójával,mert ha igen,akkor stackelni kell.*/
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
		/*azt vizsgálja,hogy jobb egérgombbal kattintott-e a user a slotra.*/
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
		/*Itt megvizsgáljuk,hogy van-e a slotban item*/
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
