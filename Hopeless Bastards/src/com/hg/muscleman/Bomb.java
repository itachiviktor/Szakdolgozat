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
	/*Ez az oszt�ly egy skill haszn�lati kell�ke.A Muscleman skill0 haszn�lja, ugyanis lerak a f�ldre egy bomb�t, �s aki els�k�nt
	 belefog taposni az kap egy kis sebz�st(majd �rdemes megcsin�lni,hogy le is lassuljon egy p�r m�sodpercre).
	 A bomba egy p�lyaelemk�nt jelenik meg.*/
	

	private Rectangle damagingarea;/*Az a ter�let,amivel �tk�zve az enemy sebz�st fog kapni.*/
	private boolean firerender = false;/*Ha valaki belel�p a bomb�ba,akkor azon t�zdurran�s anim�ci�t kell lej�ttszani,
	�s ez a v�ltoz� azt jelzi,hogy lehet-e kirajzolni az anim�ci�t vagy sem.*/
	
	private Entity damagedEnemy;/*A bomb�nak van referenci�ja arra az entit�sra,aki belel�p a bomb�ba*/
	
	/*A bomba is anim�l, a robban�st ez az oszt�ly anim�lja, a lenti k�t v�ltoz� ehhez kell*/
	private int framePerSec = 0;
	private int frame = 0;
	
	/*Itt be�ll�tjuk azt a playert(Muscleman), aki lerakta a bomb�t, azaz tudjuk, hogy ez a lerakott
	 bomba kihez tartozik, teh�t ezt �s ennek csapatt�rsait nem sebezheti ez a bomba.*/
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
		/*A tick met�dus addig fut a bomba objektumnak,am�g nem kell a fireeffectet renderelni,hisz az azt jelenti,
		 hogy m�r a damaget megkapta az enemy.Azaz a tick met�dus csak azt figyeli,hogy valaki bele�tk�z�tt-e,�s miut�n
		 valaki bele�tk�z�tt, ez leveszi az �let�t, �s innent�l m�r csak az �g�si anim�ci�t kell letolni,
		 az�rt nem itt sz�ntetem meg a referenci�t a bomb�ra,hanem a renderbe,mert a render tudja,hogy mikor van
		 v�ge a fireanimationnak,�s ut�nna megsz�nik ez a bomba.*/
		if(!firerender){
			for(int i=0;i<handler.entity.size();i++){
				entity = handler.entity.get(i);
				
				/*Az al�bbi el�gaz�sra az�rt van sz�ks�g, mert a player ami a mi g�p�nk az ir�ny�tott
				 karakter, az az entity list�ban van, �s k�t esetet k�l�nb�ztetek el
				 (Musz�ly azt az esetet is kezeln�nk, hogy mivan ha a mi karakter�nknek ez
				 ellens�ges bomba, akkor fel kell robbanjon):
				 -ha az entit�s a mi ir�ny�tott karakter�nk, akkor megvizsg�ljuk, hogy a bomba tulajdonos
				 is mi vagyunk-e(ezt networkid alapj�n �sszem�rem), mert ha nem, �s a ter�lete
				 a karakternek �s a bomb�nak egybev�g, akkor fel kell robbanjak
				 -Ha valamely entit�s(zombi,stb) rohan bele, akkor csak azt kell megn�zni , hogy �tk�ztek
				 -e(majd k�s�bb bar�ts�gos zombik miatt kell ellen�rz�st v�gezni)*/
				if(entity.id == Id.PLAYER){
					ene = (Player)handler.entity.get(i);
					if(!(ene.networkId.equals(player.networkId)) && ene.getDamagedArea().intersects(this.damagingarea)){
						ene.setHealth(-100);
						/*Ez az anim�lt t�mad�sfelirat*/
						handler.damagetext.add(new DamagingText(ene.x, ene.y, String.valueOf(100),true, handler));
						damagedEnemy = ene;/*mostm�r tudjuk ki a szenved� f�l, melyik entit�s rohant bele,ez�rt �rt�k�l is adjuk*/
						firerender = true;/*ezut�n elkezd�dhet az �g�s anim�l�sa*/
						break;
					}
				}else{
					enem = handler.entity.get(i);
					if(enem.getDamagedArea().intersects(this.damagingarea)){
						enem.setHealth(-100);
						/*Ez az anim�lt t�mad�sfelirat*/
						handler.damagetext.add(new DamagingText(enem.x, enem.y, String.valueOf(100),true, handler));
						damagedEnemy = enem;/*mostm�r tudjuk ki a szenved� f�l, melyik entit�s rohant bele,ez�rt �rt�k�l is adjuk*/
						firerender = true;/*ezut�n elkezd�dhet az �g�s anim�l�sa*/
						break;
					}
				}
				
				
			}
			
			/*Ha ide eljut a vez�rl�s az azt jelenti, hogy sem az adott kliens, sem egy zombi nem szaladt
			 bele a bomb�ba, teh�t megvizsg�ljuk, az ellens�ges karakterek list�j�t.Itt is megn�zz�k, 
			 hogy ez a karakter tulajdonosa-e a bomb�nak(vagy k�s�bb bar�t raktee le), mert ha nem, �s
			 beleszalad akkor sebz�dni fog.*/
			for(int i=0;i<handler.enemies.size();i++){
				enemy = (Player)handler.enemies.get(i);
				if(!(enemy.networkId.equals(player.networkId)) && enemy.getDamagedArea().intersects(this.damagingarea)){
					enemy.setHealth(-100);
					/*Ez az anim�lt t�mad�sfelirat*/
					handler.damagetext.add(new DamagingText(enemy.x, enemy.y, String.valueOf(100),true, handler));
					damagedEnemy = enemy;/*mostm�r tudjuk ki a szenved� f�l, melyik entit�s rohant bele,ez�rt �rt�k�l is adjuk*/
					firerender = true;/*ezut�n elkezd�dhet az �g�s anim�l�sa*/
					break;
				}
			}
		}
		
	}
	
	@Override
	public void render(Graphics g) {
		/*K�t esetet rajzolhat a render met�dus:
		 -egyik,ha nem kell firerenderel�s,azaz nem futott senii a bomb�ba,akkor mag�t a bomb�t kell kirajzolni a p�ly�ra.
		 -m�sik eset,ha valaki beleszaladt a bomb�ba,akkor a firerenderer boolean �rt�ke true,�s az anim�ci�t le kell j�ttszani*/
		
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
					/*Ha v�ge a t�zbegyullad�s anim�ci�nak,akkor ezt a bomb�t elt�vol�tjuk a p�lyalemek k�z�l(nincs tov�bb� funkci�ja
					 hisz robbant egyszer)*/
					handler.tile.remove(this);
				}
			}
			
		
		}else{
			/*Itt rajzoljuk ki a bomb�t,amikor m�g nem l�pett bele senki.*/
			g.drawImage(ImageAssets.musclemantrap.getBufferedImage(), x,y, width,height,null);
		}
		
	}
}
