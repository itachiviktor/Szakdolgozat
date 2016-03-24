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
	
	public Player player = null;
	
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
				Entity en = handler.entity.get(i);
				
				if(en.id == Id.PLAYER){
					Player ene = (Player)handler.entity.get(i);
					if(!(ene.networkId.equals(player.networkId)) && ene.getDamagedArea().intersects(this.damagingarea)){
						ene.setHealth(-100);
						/*Ez az anim�lt t�mad�sfelirat*/
						handler.damagetext.add(new DamagingText(ene.x, ene.y, String.valueOf(100),true, handler));
						damagedEnemy = ene;/*mostm�r tudjuk ki a szenved� f�l, melyik entit�s rohant bele,ez�rt �rt�k�l is adjuk*/
						firerender = true;/*ezut�n elkezd�dhet az �g�s anim�l�sa*/
						break;
					}
				}else{
					Entity ene = handler.entity.get(i);
					if(ene.getDamagedArea().intersects(this.damagingarea)){
						ene.setHealth(-100);
						/*Ez az anim�lt t�mad�sfelirat*/
						handler.damagetext.add(new DamagingText(ene.x, ene.y, String.valueOf(100),true, handler));
						damagedEnemy = ene;/*mostm�r tudjuk ki a szenved� f�l, melyik entit�s rohant bele,ez�rt �rt�k�l is adjuk*/
						firerender = true;/*ezut�n elkezd�dhet az �g�s anim�l�sa*/
						break;
					}
				}
				
				
			}
			
			for(int i=0;i<handler.enemies.size();i++){
				Player ene = (Player)handler.enemies.get(i);
				if(!(ene.networkId.equals(player.networkId)) && ene.getDamagedArea().intersects(this.damagingarea)){
					ene.setHealth(-100);
					/*Ez az anim�lt t�mad�sfelirat*/
					handler.damagetext.add(new DamagingText(ene.x, ene.y, String.valueOf(100),true, handler));
					damagedEnemy = ene;/*mostm�r tudjuk ki a szenved� f�l, melyik entit�s rohant bele,ez�rt �rt�k�l is adjuk*/
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
