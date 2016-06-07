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
	private Sound boltSong;/*a villámlás wav hangfileja*/
	
	 private boolean damagingNow = false;/*Ez a tick metódsu számára fontos,azt jelzi,hogy meddig kell sebezni,és most jelenleg
	   sebez-e a támadás.*/
	 
	 public int damageValue = 250;/*Ennyi életerõt vesz le arról,akit ér a támadás*/
	 private int manacost = 100;/*Ennyi manát vesz le a használata a támadásnak*/
	 
	 private double x, y, angle; // x,y and angle(szög)
	   private int width, height; //width and height 
	   
	   
	   /*segédadattagok, a memória tartalékolása miatt*/
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
		/*Csak akkor aktiválódik a  skill, ha a jelenlegi játékidõ nagyobb,mint a skillkezdés és a skillcd idõ
		 összeadva, azaz ha lejárt a cd,csak akkor aktiválhatom újra a skillt.Illetve hs a skillstartedtime == 0, az azt jelenti
		 hogy mióta megy a játék,azaz inicializálva lett a skill, azóta még nem volt használva,tehát cd sem lehet rajta.*/
		if(this.skillStartedMainTime + this.cdtime < Game.maintime || skillStartedMainTime == 0){
			this.skillStartedMainTime = Game.maintime;/*A skillkezdési idõt beállítom a játék fõidejére*/
			isactivated = true;/*aktívnak tekintjük innentõl a skillt*/
			player.monitorScreenmanager.skill0useable = false;/*A skillbaron elszürkítjük a képet, ami a skillt képviseli*/
			
			boltSong.play();
			
			/*A playernél leveszem a manát,amibe a skill került.*/
			   if(player.mana - this.manacost < 0){
			    	player.mana = 0;
			    }else{
			    	player.mana-=this.manacost;
			    }
			   
			   /*beállítom a paraméterben kapott koordinátákat és szöget, ami a player saját helyzete is egyben.*/
			   this.x = player.x + player.width;
			   this.y = player.y - 118;
			   this.angle = player.angle;
			   this.width = 512;
			   this.height = 300;
			   
			   damagingNow = true;/*most sebezhet a skill*/
			   animaionStillRuning = true;/*most már kirajzolhatjuk az animációt.*/
			
			player.skill0started = true;
		}
		
	}

	@Override
	public void activateSkillByServer() {
		this.skillStartedMainTime = Game.maintime;/*A skillkezdési idõt beállítom a játék fõidejére*/
		isactivated = true;/*aktívnak tekintjük innentõl a skillt*/
		//player.monitorScreenmanager.skill0useable = false;
		
		
		boltSong.play();
		
		/*A playernél leveszem a manát,amibe a skill került.*/
		   if(player.mana - this.manacost < 0){
		    	player.mana = 0;
		    }else{
		    	player.mana-=this.manacost;
		    }
		
		   /*beállítom a paraméterben kapott koordinátákat és szöget, ami a player saját helyzete is egyben.*/
		   this.x = player.x + player.width;
		   this.y = player.y - 118;
		   this.angle = player.angle;
		   this.width = 512;
		   this.height = 300;
		
		damagingNow = true;/*most sebezhet a skill*/
		animaionStillRuning = true;/*most már kirajzolhatjuk az animációt.*/
		   
		player.skill0started = false;
		
	}

	@Override
	public void tick() {
		/*A tick metódus kiszámolja,hogy hány másodperc van a cd lejárásáig,ez azért kell,hogy a MonitorScreenManager
		 kitudja írni visszaszámlálás formájában minden másopercben.*/
		
		/*A tick metódusában megvizsgálom,hogy most sebez-e a skill.Ezt a változót az activateSkill metódusban állítom igazra.
	    Végigmegy az összes entitáson, és ellenõrzi,hogy melyik entitásnak van mettszéspontja a villámlási
	    területtel(a bolt osztály polygonja), és azoknak az entitásoknak az életét csökkenti.*/
		
	   if(isactivated){
		   for(int i=0;i<player.handler.entity.size();i++){
			   Entity en = player.handler.entity.get(i);
			   if(en.getId() != Id.PLAYER && getPolygon().intersects(en.getBounds())){
				   en.setHealth(-damageValue);
				   player.handler.damagetext.add(new DamagingText(en.x, en.y,String.valueOf(damageValue),true, player.handler));
			   }
		   }
		   
		   /*Elõször az entitásokat ellenõrizzük végig.(Itt nem mozgo játékososk(pl zombi), és
			 az adott user karaktere található.)*/
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
		   
		 /*Mivel csak egyszer szeretnénk,hogy sebezzen a skill,nem pedig másodpercenként 60-szor, ezért rögtön az elsõ használat
		 után hamisra állítjuk a damagingNow változót, így a sebzés nem fog megtörténni többet, csak ha újra elnyomjuk a skillt
		 tehát ez a skill egyszeri sebzés,nem olyan,hogy másodpercenként sebez.*/
		isactivated = false;
	   }
		
		
		this.timeuntilcdend = this.skillStartedMainTime + this.cdtime - (Game.maintime);
		/*Illetve mindíg megnézi,hogy a cd lejárt-e már,mertha igen ,akkor a hudmanagernek a változóját
		 truera kell állítani,hogy kivilágosítsa a skillképet.*/
		if(this.skillStartedMainTime + this.cdtime < Game.maintime || skillStartedMainTime == 0){
			player.monitorScreenmanager.skill0useable = true;/*Ezzel látható,hogy az a baj,hogy ha 10 percig nem használom a skillt,
			akkor 10 percig elérhetõ,és folymatosan mindíg truere állítja az értéket, ez pazarló lehet,javítani kellene
			valami propertychangelistenert tudnék elképzelni.*/
			
			
		}  
		
	}
	
	 public Polygon getPolygon(){
		   /*Visszaadja egy bizonyos szöggel elforgatott polygont.Ez a polygon vizsgálja,hogy van-e mettszéspontja vmelyik
		    entitással,mert akkor sebzés az mehet.*/
		   return RotateViktor.rotate(new Point((int)x,(int)y), new Point((int)x + width,(int)y), new Point((int)x+width,(int)y+height) ,
				   new Point((int)x,(int)y+height),angle,(int)(player.x+player.width/2), (int)(player.y+player.height/2));
	   }

	@Override
	public void render(Graphics g) {
		/*Render metódus*/
		   /*Csak akkor hajtódnak végre az utasítások, ha az animationStillRuning változó értéke igaz,
		    különben nem csinál semmit a render metódus.Igazra az activateSkill metódussal állítom,
		    amikor is a skill elnyomódik.Hamisra majd a render metódus állítja,hisz az tudja mikor van vége az animációnak.
		    Tehát így a villámlás csak egyszer fog elcsattanni, amikor kell.*/
		 	if(animaionStillRuning){
		 		 Graphics2D g2d = (Graphics2D) g;
				    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				          RenderingHints.VALUE_ANTIALIAS_ON);
				    
				    AffineTransform old = g2d.getTransform();
			    	
				    g2d.drawPolygon(getPolygon());
			    	  g2d.rotate(Math.toRadians(player.getAngle()), player.x + player.width/2,
					          player.y + player.height / 2);
					  
			    	  /*A render metódus 60- szor hívódik meg másodpercenként.A boltframePerSec változó azért lett lértehozva,
			    	   hogy azt állítsam,hogy a 60-ból hányszor váltson képet, hisz másodpercenként 60-szor
			    	   képet cserélni az animációban az túl gyors lenne, alig látnánk a képeket.
			    	   Amiíg a bolrFramePerSec kisebb mint 4, addig ugyan azt a képet rajzolja ki.*/
			    	  
			    	  
			    	  /*A boltframe változó pedig azt a számot tárolja,hogy hanyadik képet kell kirajzolni.Ugyanis egy animáció
			    	   több képbõl áll, ezen képek egy tömbben vannak, és a boltframe az egy tömbindex, ami azt mutatja,hogy
			    	   hanyadik indexü képet kell most kirajzolni.*/
			    	  
			    	  if(framePerSec < 4){
							if(frame < 10){
								g.drawImage(ImageAssets.bolt[frame], (int)player.x + player.width, (int)player.y-118, 512,300,null);
							}
							framePerSec++;
			    	  }else{
			    		  /*Ha a boltFramePerSec nagyobb mint 4,akkor azt jelenti,hogy a következõ képet kell kirajzolni az 
			    		   animációban.Ezért megint nullára megy a számláló.*/
			    		  framePerSec = 0;
			    		  
			    		  /*Ha a kirajzolt kép még nem az utolsó animációs kép, akkor növeljük az indexértéket,hisz nem lesz
			    		   ArrayOutIndexException.*/
			    		  if(frame<9){
								g.drawImage(ImageAssets.bolt[frame], (int)player.x + player.width, (int)player.y-118, 512,300,null);
								frame++;
								
							}else{
								/*Viszont ,ha már az utolsó animációs képkocka vetítési ideje is lejárt, akkor beállítom az
								 animationStillRuning metódust falsera, azaz a render metódus belseje nem fog végrejatódni.
								 Illetve a képkocka renderelõ változókat alapértelmezettre állítom,hisz ha újra kell renderelni
								 akkor mindent elõrõl kell kezdeni.*/
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
