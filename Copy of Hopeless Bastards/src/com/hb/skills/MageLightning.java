package com.hb.skills;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

import com.hb.Game;
import com.hb.Id;
import com.hb.Sound;
import com.hb.entity.AbstractSkill;
import com.hb.entity.DamagingText;
import com.hb.entity.Entity;
import com.hb.entity.Player;
import com.hb.graphics.ImageAssets;
import com.hb.math.RotateViktor;

public class MageLightning extends AbstractSkill{
	private Sound boltSong;/*a vill�ml�s wav hangfileja*/
	
	 private boolean damagingNow = false;/*Ez a tick met�dsu sz�m�ra fontos,azt jelzi,hogy meddig kell sebezni,�s most jelenleg
	   sebez-e a t�mad�s.*/
	 
	 public int damageValue = 250;/*Ennyi �leter�t vesz le arr�l,akit �r a t�mad�s*/
	 private int manacost = 100;/*Ennyi man�t vesz le a haszn�lata a t�mad�snak*/
	 
	 private double x, y, angle; // x,y and angle(sz�g)
	   private int width, height; //width and height 
	   
	   
	   /*seg�dadattagok, a mem�ria tartal�kol�sa miatt*/
		private Player enemy;
		private Entity enem;
		private Player ene;
		private Entity en;
	
	public MageLightning(Player player) {
		super(player);
		this.cdtime = 10;
		boltSong = new Sound("/lightning.wav");
	}
	
	@Override
	public void activateSkill() {
		/*Csak akkor aktiv�l�dik a  skill, ha a jelenlegi j�t�kid� nagyobb,mint a skillkezd�s �s a skillcd id�
		 �sszeadva, azaz ha lej�rt a cd,csak akkor aktiv�lhatom �jra a skillt.Illetve hs a skillstartedtime == 0, az azt jelenti
		 hogy mi�ta megy a j�t�k,azaz inicializ�lva lett a skill, az�ta m�g nem volt haszn�lva,teh�t cd sem lehet rajta.*/
		if(this.skillStartedMainTime + this.cdtime < Game.maintime || skillStartedMainTime == 0){
			this.skillStartedMainTime = Game.maintime;/*A skillkezd�si id�t be�ll�tom a j�t�k f�idej�re*/
			isactivated = true;/*akt�vnak tekintj�k innent�l a skillt*/
			player.monitorScreenmanager.skill0useable = false;/*A skillbaron elsz�rk�tj�k a k�pet, ami a skillt k�pviseli*/
			
			boltSong.play();
			
			/*A playern�l leveszem a man�t,amibe a skill ker�lt.*/
			   if(player.mana - this.manacost < 0){
			    	player.mana = 0;
			    }else{
			    	player.mana-=this.manacost;
			    }
			   
			   /*be�ll�tom a param�terben kapott koordin�t�kat �s sz�get, ami a player saj�t helyzete is egyben.*/
			   this.x = player.x + player.width;
			   this.y = player.y - 118;
			   this.angle = player.angle;
			   this.width = 512;
			   this.height = 300;
			   
			   damagingNow = true;/*most sebezhet a skill*/
			   animaionStillRuning = true;/*most m�r kirajzolhatjuk az anim�ci�t.*/
			
			player.skill0started = true;
		}
		
	}

	@Override
	public void activateSkillByServer() {
		this.skillStartedMainTime = Game.maintime;/*A skillkezd�si id�t be�ll�tom a j�t�k f�idej�re*/
		isactivated = true;/*akt�vnak tekintj�k innent�l a skillt*/
		//player.monitorScreenmanager.skill0useable = false;
		
		
		boltSong.play();
		
		/*A playern�l leveszem a man�t,amibe a skill ker�lt.*/
		   if(player.mana - this.manacost < 0){
		    	player.mana = 0;
		    }else{
		    	player.mana-=this.manacost;
		    }
		
		   /*be�ll�tom a param�terben kapott koordin�t�kat �s sz�get, ami a player saj�t helyzete is egyben.*/
		   this.x = player.x + player.width;
		   this.y = player.y - 118;
		   this.angle = player.angle;
		   this.width = 512;
		   this.height = 300;
		
		damagingNow = true;/*most sebezhet a skill*/
		animaionStillRuning = true;/*most m�r kirajzolhatjuk az anim�ci�t.*/
		   
		player.skill0started = false;
		
	}

	@Override
	public void tick() {
		/*A tick met�dus kisz�molja,hogy h�ny m�sodperc van a cd lej�r�s�ig,ez az�rt kell,hogy a MonitorScreenManager
		 kitudja �rni visszasz�ml�l�s form�j�ban minden m�sopercben.*/
		
		/*A tick met�dus�ban megvizsg�lom,hogy most sebez-e a skill.Ezt a v�ltoz�t az activateSkill met�dusban �ll�tom igazra.
	    V�gigmegy az �sszes entit�son, �s ellen�rzi,hogy melyik entit�snak van mettsz�spontja a vill�ml�si
	    ter�lettel(a bolt oszt�ly polygonja), �s azoknak az entit�soknak az �let�t cs�kkenti.*/
		
	   if(isactivated){
		   for(int i=0;i<player.handler.entity.size();i++){
			   Entity en = player.handler.entity.get(i);
			   if(en.getId() != Id.PLAYER && getPolygon().intersects(en.getBounds())){
				   en.setHealth(-damageValue);
				   player.handler.damagetext.add(new DamagingText(en.x, en.y,String.valueOf(damageValue),true, player.handler));
			   }
		   }
		   
		   /*El�sz�r az entit�sokat ellen�rizz�k v�gig.(Itt nem mozgo j�t�kososk(pl zombi), �s
			 az adott user karaktere tal�lhat�.)*/
				for(int i=0;i<player.handler.entity.size();i++){
					en = player.handler.entity.get(i);
					if(en.id == Id.PLAYER){
						
						ene = (Player)player.handler.entity.get(i);

						if(!(ene.networkId.equals(player.networkId)) && getPolygon().intersects(ene.getDamagedArea())){
							ene.setHealth(-400);
							player.handler.damagetext.add(new DamagingText(ene.x, ene.y, String.valueOf(400),true, player.handler));
						}
					}else{
						enem = player.handler.entity.get(i);
						if(getPolygon().intersects(enem.getDamagedArea())){
							enem.setHealth(-400);
							player.handler.damagetext.add(new DamagingText(enem.x, enem.y, String.valueOf(400),true, player.handler));
						}
					}		
				}
				
				for(int i=0;i<player.handler.enemies.size();i++){
					enemy = player.handler.enemies.get(i);
					if(!(enemy.networkId.equals(player.networkId)) && getPolygon().intersects(enemy.getDamagedArea())){
						enemy.setHealth(-400);
						player.handler.damagetext.add(new DamagingText(enemy.x, enemy.y, String.valueOf(400),true, player.handler));
					}	
				}
		   
		 /*Mivel csak egyszer szeretn�nk,hogy sebezzen a skill,nem pedig m�sodpercenk�nt 60-szor, ez�rt r�gt�n az els� haszn�lat
		 ut�n hamisra �ll�tjuk a damagingNow v�ltoz�t, �gy a sebz�s nem fog megt�rt�nni t�bbet, csak ha �jra elnyomjuk a skillt
		 teh�t ez a skill egyszeri sebz�s,nem olyan,hogy m�sodpercenk�nt sebez.*/
		isactivated = false;
	   }
		
		
		this.timeuntilcdend = this.skillStartedMainTime + this.cdtime - (Game.maintime);
		/*Illetve mind�g megn�zi,hogy a cd lej�rt-e m�r,mertha igen ,akkor a hudmanagernek a v�ltoz�j�t
		 truera kell �ll�tani,hogy kivil�gos�tsa a skillk�pet.*/
		if(this.skillStartedMainTime + this.cdtime < Game.maintime || skillStartedMainTime == 0){
			player.monitorScreenmanager.skill0useable = true;/*Ezzel l�that�,hogy az a baj,hogy ha 10 percig nem haszn�lom a skillt,
			akkor 10 percig el�rhet�,�s folymatosan mind�g truere �ll�tja az �rt�ket, ez pazarl� lehet,jav�tani kellene
			valami propertychangelistenert tudn�k elk�pzelni.*/
			
			
		}  
		
	}
	
	 public Polygon getPolygon(){
		   /*Visszaadja egy bizonyos sz�ggel elforgatott polygont.Ez a polygon vizsg�lja,hogy van-e mettsz�spontja vmelyik
		    entit�ssal,mert akkor sebz�s az mehet.*/
		   return RotateViktor.rotate(new Point((int)x,(int)y), new Point((int)x + width,(int)y), new Point((int)x+width,(int)y+height) ,
				   new Point((int)x,(int)y+height),angle,(int)(player.x+player.width/2), (int)(player.y+player.height/2));
	   }

	@Override
	public void render(Graphics g) {
		/*Render met�dus*/
		   /*Csak akkor hajt�dnak v�gre az utas�t�sok, ha az animationStillRuning v�ltoz� �rt�ke igaz,
		    k�l�nben nem csin�l semmit a render met�dus.Igazra az activateSkill met�dussal �ll�tom,
		    amikor is a skill elnyom�dik.Hamisra majd a render met�dus �ll�tja,hisz az tudja mikor van v�ge az anim�ci�nak.
		    Teh�t �gy a vill�ml�s csak egyszer fog elcsattanni, amikor kell.*/
		 	if(animaionStillRuning){
		 		 Graphics2D g2d = (Graphics2D) g;
				    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				          RenderingHints.VALUE_ANTIALIAS_ON);
				    
				    AffineTransform old = g2d.getTransform();
			    	
				    g2d.drawPolygon(getPolygon());
			    	  g2d.rotate(Math.toRadians(player.getAngle()), player.x + player.width/2,
					          player.y + player.height / 2);
					  
			    	  /*A render met�dus 60- szor h�v�dik meg m�sodpercenk�nt.A boltframePerSec v�ltoz� az�rt lett l�rtehozva,
			    	   hogy azt �ll�tsam,hogy a 60-b�l h�nyszor v�ltson k�pet, hisz m�sodpercenk�nt 60-szor
			    	   k�pet cser�lni az anim�ci�ban az t�l gyors lenne, alig l�tn�nk a k�peket.
			    	   Ami�g a bolrFramePerSec kisebb mint 4, addig ugyan azt a k�pet rajzolja ki.*/
			    	  
			    	  
			    	  /*A boltframe v�ltoz� pedig azt a sz�mot t�rolja,hogy hanyadik k�pet kell kirajzolni.Ugyanis egy anim�ci�
			    	   t�bb k�pb�l �ll, ezen k�pek egy t�mbben vannak, �s a boltframe az egy t�mbindex, ami azt mutatja,hogy
			    	   hanyadik index� k�pet kell most kirajzolni.*/
			    	  
			    	  if(framePerSec < 4){
							if(frame < 10){
								g.drawImage(ImageAssets.bolt[frame], (int)player.x + player.width, (int)player.y-118, 512,300,null);
							}
							framePerSec++;
			    	  }else{
			    		  /*Ha a boltFramePerSec nagyobb mint 4,akkor azt jelenti,hogy a k�vetkez� k�pet kell kirajzolni az 
			    		   anim�ci�ban.Ez�rt megint null�ra megy a sz�ml�l�.*/
			    		  framePerSec = 0;
			    		  
			    		  /*Ha a kirajzolt k�p m�g nem az utols� anim�ci�s k�p, akkor n�velj�k az index�rt�ket,hisz nem lesz
			    		   ArrayOutIndexException.*/
			    		  if(frame<9){
								g.drawImage(ImageAssets.bolt[frame], (int)player.x + player.width, (int)player.y-118, 512,300,null);
								frame++;
								
							}else{
								/*Viszont ,ha m�r az utols� anim�ci�s k�pkocka vet�t�si ideje is lej�rt, akkor be�ll�tom az
								 animationStillRuning met�dust falsera, azaz a render met�dus belseje nem fog v�grejat�dni.
								 Illetve a k�pkocka renderel� v�ltoz�kat alap�rtelmezettre �ll�tom,hisz ha �jra kell renderelni
								 akkor mindent el�r�l kell kezdeni.*/
								animaionStillRuning = false;
								frame = 0;
								framePerSec = 0;
							}
			    	  }
			    	 
			    	g2d.setTransform(old);
		 	}
		
	}

	@Override
	public Rectangle getBounds() {
		
		return null;
	}
}
