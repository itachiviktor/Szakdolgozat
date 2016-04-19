package com.hg.muscleman;

import java.awt.Graphics;
import java.awt.Rectangle;
import com.hb.Id;
import com.hb.entity.DamagingText;
import com.hb.entity.Entity;
import com.hb.entity.Player;
import com.hb.gamestate.Handler;
import com.hb.graphics.ImageAssets;
import com.hb.tile.Tile;

public class Bomb extends Tile{
	/*Ez az osztály egy skill használati kelléke.A Muscleman skill0 használja, ugyanis lerak a földre egy bombát, és aki elsõként
	 belefog taposni az kap egy kis sebzést(majd érdemes megcsinálni,hogy le is lassuljon egy pár másodpercre).
	 A bomba egy pályaelemként jelenik meg.*/
	

	private Rectangle damagingarea;/*Az a terület,amivel ütközve az enemy sebzést fog kapni.*/
	private boolean firerender = false;/*Ha valaki belelép a bombába,akkor azon tûzdurranás animációt kell lejáttszani,
	és ez a változó azt jelzi,hogy lehet-e kirajzolni az animációt vagy sem.*/
	
	private Entity damagedEnemy;/*A bombának van referenciája arra az entitásra,aki belelép a bombába*/
	
	/*A bomba is animál, a robbanást ez az osztály animálja, a lenti két változó ehhez kell*/
	private int framePerSec = 0;
	private int frame = 0;
	
	/*Itt beállítjuk azt a playert(Muscleman), aki lerakta a bombát, azaz tudjuk, hogy ez a lerakott
	 bomba kihez tartozik, tehát ezt és ennek csapattársait nem sebezheti ez a bomba.*/
	public Player player = null;
	private Entity entity;
	private Player ene;
	private Entity enem;
	private Player enemy;
	
	public Bomb(int x, int y, int width, int height,Player player,Handler handler) {
		super(x, y, width, height, false, Id.BOMB, handler);
		this.damagingarea = new Rectangle(x,y,width,height);
		this.player = player;
	}

	@Override
	public void tick() {
		/*A tick metódus addig fut a bomba objektumnak,amíg nem kell a fireeffectet renderelni,hisz az azt jelenti,
		 hogy már a damaget megkapta az enemy.Azaz a tick metódus csak azt figyeli,hogy valaki beleütközött-e,és miután
		 valaki beleütközött, ez leveszi az életét, és innentõl már csak az égési animációt kell letolni,
		 azért nem itt szüntetem meg a referenciát a bombára,hanem a renderbe,mert a render tudja,hogy mikor van
		 vége a fireanimationnak,és utánna megszûnik ez a bomba.*/
		if(!firerender){
			for(int i=0;i<handler.entity.size();i++){
				entity = handler.entity.get(i);
				
				/*Az alábbi elágazásra azért van szükség, mert a player ami a mi gépünk az irányított
				 karakter, az az entity listában van, és két esetet különböztetek el
				 (Muszály azt az esetet is kezelnünk, hogy mivan ha a mi karakterünknek ez
				 ellenséges bomba, akkor fel kell robbanjon):
				 -ha az entitás a mi irányított karakterünk, akkor megvizsgáljuk, hogy a bomba tulajdonos
				 is mi vagyunk-e(ezt networkid alapján összemérem), mert ha nem, és a területe
				 a karakternek és a bombának egybevág, akkor fel kell robbanjak
				 -Ha valamely entitás(zombi,stb) rohan bele, akkor csak azt kell megnézni , hogy ütköztek
				 -e(majd késõbb barátságos zombik miatt kell ellenõrzést végezni)*/
				if(entity.id == Id.PLAYER){
					ene = (Player)handler.entity.get(i);
					if(!(ene.networkId.equals(player.networkId)) && ene.getDamagedArea().intersects(this.damagingarea)){
						ene.setHealth(-100);
						/*Ez az animált támadásfelirat*/
						handler.damagetext.add(new DamagingText(ene.x, ene.y, String.valueOf(100),true, handler));
						damagedEnemy = ene;/*mostmár tudjuk ki a szenvedõ fél, melyik entitás rohant bele,ezért értékül is adjuk*/
						firerender = true;/*ezután elkezdõdhet az égés animûlása*/
						break;
					}
				}else{
					enem = handler.entity.get(i);
					if(enem.getDamagedArea().intersects(this.damagingarea)){
						enem.setHealth(-100);
						/*Ez az animált támadásfelirat*/
						handler.damagetext.add(new DamagingText(enem.x, enem.y, String.valueOf(100),true, handler));
						damagedEnemy = enem;/*mostmár tudjuk ki a szenvedõ fél, melyik entitás rohant bele,ezért értékül is adjuk*/
						firerender = true;/*ezután elkezdõdhet az égés animûlása*/
						break;
					}
				}
				
				
			}
			
			/*Ha ide eljut a vezérlés az azt jelenti, hogy sem az adott kliens, sem egy zombi nem szaladt
			 bele a bombába, tehát megvizsgáljuk, az ellenséges karakterek listáját.Itt is megnézzük, 
			 hogy ez a karakter tulajdonosa-e a bombának(vagy késõbb barát raktee le), mert ha nem, és
			 beleszalad akkor sebzõdni fog.*/
			for(int i=0;i<handler.enemies.size();i++){
				enemy = (Player)handler.enemies.get(i);
				if(!(enemy.networkId.equals(player.networkId)) && enemy.getDamagedArea().intersects(this.damagingarea)){
					enemy.setHealth(-100);
					/*Ez az animált támadásfelirat*/
					handler.damagetext.add(new DamagingText(enemy.x, enemy.y, String.valueOf(100),true, handler));
					damagedEnemy = enemy;/*mostmár tudjuk ki a szenvedõ fél, melyik entitás rohant bele,ezért értékül is adjuk*/
					firerender = true;/*ezután elkezdõdhet az égés animûlása*/
					break;
				}
			}
		}
		
	}
	
	@Override
	public void render(Graphics g) {
		/*Két esetet rajzolhat a render metódus:
		 -egyik,ha nem kell firerenderelés,azaz nem futott senii a bombába,akkor magát a bombát kell kirajzolni a pályára.
		 -másik eset,ha valaki beleszaladt a bombába,akkor a firerenderer boolean értéke true,és az animációt le kell játtszani*/
		
		if(firerender){
			if(framePerSec < 4){
				if(frame<9){
					g.drawImage(ImageAssets.musclemantrapfire[frame].getBufferedImage(),  (int)damagedEnemy.x, (int)damagedEnemy.y, 64,64,null);
				}
				framePerSec++;
			}else{
				framePerSec = 0;
				if(frame<8){
					g.drawImage(ImageAssets.musclemantrapfire[frame].getBufferedImage(),  (int)damagedEnemy.x, (int)damagedEnemy.y, 64,64,null);
					frame++;
					
				}else{
					/*Ha vége a tûzbegyulladás animációnak,akkor ezt a bombát eltávolítjuk a pályalemek közül(nincs továbbá funkciója
					 hisz robbant egyszer)*/
					handler.tile.remove(this);
				}
			}
			
		
		}else{
			/*Itt rajzoljuk ki a bombát,amikor még nem lépett bele senki.*/
			g.drawImage(ImageAssets.musclemantrap.getBufferedImage(), x,y, width,height,null);
		}
		
	}
}
