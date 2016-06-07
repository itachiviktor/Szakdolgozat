package com.hb.entity;

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
import com.hb.gamestate.Handler;
import com.hb.graphics.ImageAssets;
import com.hb.math.RotateViktor;

public class Bolt {
	private double x, y, angle; // x,y and angle(szög)
	   private int width, height; //width and height 
	   private Handler handler;
	   private Player player;
	   private Sound boltSong;/*a villámlás wav hangfileja*/
	   
	   private boolean damagingNow = false;/*Ez a tick metódsu számára fontos,azt jelzi,hogy meddig kell sebezni,és most jelenleg
	   sebez-e a támadás.*/
	   public int cdtime = 1;/*ct a skillre*/
	   private int skillStartedMainTime = 0;/*változó inicializálása, ami a buff eltolásának kezdõideje,
	   elõször azért nullára állítom,mert így ha még sosem volt használva a játékindítás óta a skill
	   akkor ha ez az érték nulla, akkor nincs rajta cd, és lehet használni, azaz meghívódhat az activateSkill metódus belseje.*/
	   
	   public int damageValue = 250;/*Ennyi életerõt vesz le arról,akit ér a támadás*/
	   private int manacost = 100;/*Ennyi manát vesz le a használata a támadásnak*/
	   
	   
	   private int boltframe = 0;/*A képkockák váltakozásának 2 változója*/
	   private int boltframePerSec = 0;
	   
	   private boolean animaionStillRuning = false;/*A render metódusnak jelzi,hogy kell-e még a kirajzolást csinálnia vagy sem,
	   azaz véget ért az animáció.*/
	   
	   public int timeuntilcdend = 0;/*Ez az idõ másodpercben,ami azt az értéket tartalmazza,hogy mennyi másodperc van még
	   a cd lejárásáig.*/
		
		
	   public Bolt(Handler handler,Player player) {
		   /*konstruktorban nem inicializálom a pozíció adattagokat, az mindíg a player helyzetétõl fog függni.*/
		   	this.player = player;
		    this.handler = handler;
		    boltSong = new Sound("/lightning.wav");
		 
	   }
	   
	   public void activateSkill(double x,double y,double angle,int width,int height){
		   
		   /*Ez a metódus az, amit a player objektum meghív, ha a támadást el kell indítani.
		    Csak akkor hajtja végre az utasításokat, ha a skillkezdési ideje + a cd ideje kisebb mint a jelenlegi
		    idõ, hisz ez azt jelenti,hogy lejárt a cd a skillre,vagy ha a skillkezdésideje változó értéke nulla,
		    az azt jelenti,hogy még sosem volt használva a skill a játék betöltése óta, azaz tuti nincs rajta cd. */
		   if(this.skillStartedMainTime + this.cdtime < Game.maintime || skillStartedMainTime == 0){
			   /*A skill használatakor elsõ lépésben lejáttszom a villám hangot.*/
			   boltSong.play();
			   
			   /*A playernél leveszem a manát,amibe a skill került.*/
			   if(player.mana - this.manacost < 0){
			    	player.mana = 0;
			    }else{
			    	player.mana-=this.manacost;
			    }
			   /*beállítom a paraméterben kapott koordinátákat és szöget, ami a player saját helyzete is egyben.*/
			   this.x = x;
			   this.y = y;
			   this.angle = angle;
			   this.width = width;
			   this.height = height;
			   
			   /*Beállítom a skillkezdési idõt, a jelenlegi idõre, amit a Game osztály maintime változója tárol*/
			   this.skillStartedMainTime = Game.maintime;
			   
			   damagingNow = true;/*most sebezhet a skill*/
			   animaionStillRuning = true;/*most már kirajzolhatjuk az animációt.*/
			   player.monitorScreenmanager.skill0useable = false;/*A skillkép elsõtétül,hisz cd kerül a skillre*/
			}
	   }
	   
	public double getX() {
		      return x;
		   }

		   public double getY() {
		      return y;
		   }

		   public double getAngle() {
		      return angle;
		   }

		   public int getWidth() {
		      return width;
		   }

		   public int getHeight() {
		      return height;
		   }

		   // setting the values
		   public void setAngle(double aa) {

		      this.angle = aa;
		   }

		   public void setX(double x) {
		      this.x = x;
		   }

		   public void setY(double y) {
		      this.y = y;
		   }
		   public void setWidth(int w) {
		      this.width = w;
		   }

		   public void setHeight(int h) {
		      this.height = h;
		   }

		   public Rectangle getBounds(){
			   /*Visszaadja a befoglaló négyszöget.*/
				return new Rectangle((int)x,(int)y,(int)width,(int)height);
		   }
		   
		   public Polygon getPolygon(){
			   /*Visszaadja egy bizonyos szöggel elforgatott polygont.Ez a polygon vizsgálja,hogy van-e mettszéspontja vmelyik
			    entitással,mert akkor sebzés az mehet.*/
			   return RotateViktor.rotate(new Point((int)x,(int)y), new Point((int)x + width,(int)y), new Point((int)x+width,(int)y+height) ,
					   new Point((int)x,(int)y+height),angle,(int)(player.x+player.width/2), (int)(player.y+player.height/2));
		   }
		   
		   public void render(Graphics g){
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
				    	  
				    	  if(boltframePerSec < 4){
								if(boltframe < 10){
									g.drawImage(ImageAssets.bolt[boltframe], (int)player.x + player.width, (int)player.y-118, 512,300,null);
								}
								boltframePerSec++;
				    	  }else{
				    		  /*Ha a boltFramePerSec nagyobb mint 4,akkor azt jelenti,hogy a következõ képet kell kirajzolni az 
				    		   animációban.Ezért megint nullára megy a számláló.*/
				    		  boltframePerSec = 0;
				    		  
				    		  /*Ha a kirajzolt kép még nem az utolsó animációs kép, akkor növeljük az indexértéket,hisz nem lesz
				    		   ArrayOutIndexException.*/
				    		  if(boltframe<9){
									g.drawImage(ImageAssets.bolt[boltframe], (int)player.x + player.width, (int)player.y-118, 512,300,null);
									boltframe++;
									
								}else{
									/*Viszont ,ha már az utolsó animációs képkocka vetítési ideje is lejárt, akkor beállítom az
									 animationStillRuning metódust falsera, azaz a render metódus belseje nem fog végrejatódni.
									 Illetve a képkocka renderelõ változókat alapértelmezettre állítom,hisz ha újra kell renderelni
									 akkor mindent elõrõl kell kezdeni.*/
									animaionStillRuning = false;
									boltframe = 0;
									boltframePerSec = 0;
								}
				    	  }
				    	 
				    	g2d.setTransform(old);
			 	}
			  
		   }
		   
		   public void tick(){
			   /*A tick metódusában megvizsgálom,hogy most sebez-e a skill.Ezt a változót az activateSkill metódusban állítom igazra.
			    Végigmegy az összes entitáson, és ellenõrzi,hogy melyik entitásnak van mettszéspontja a villámlási
			    területtel(a bolt osztály polygonja), és azoknak az entitásoknak az életét csökkenti.*/
			   if(damagingNow){
				   for(int i=0;i<handler.entity.size();i++){
					   Entity en = handler.entity.get(i);
					   if(en.getId() != Id.PLAYER && getPolygon().intersects(en.getBounds())){
						   en.setHealth(-damageValue);
						   handler.damagetext.add(new DamagingText(en.x, en.y,String.valueOf(damageValue),true, handler));
					   }
				   }
				 /*Mivel csak egyszer szeretnénk,hogy sebezzen a skill,nem pedig másodpercenként 60-szor, ezért rögtön az elsõ használat
				 után hamisra állítjuk a damagingNow változót, így a sebzés nem fog megtörténni többet, csak ha újra elnyomjuk a skillt
				 tehát ez a skill egyszeri sebzés,nem olyan,hogy másodpercenként sebez.*/
				damagingNow = false;
			   }
			   
			   /*Itt kiszámolom a tick metódusban mindíg,hogy hány másodperc van még hátra amíg újra használhatom a skillt.
			    Ezt azért a tick metódusban kell kiszámolni,mert ez sokszor meghívódik, ée ez az érték másodpercenként ugyebár
			    csökkenni fog.Az a lényeg,hogy a skillkezdési idõ+cd idõbõl , ha kivonom a jelenlegi idõt, az visszaadja,hogy 
			    mennyi idõ van még hátra(azaz visszaszámlálás lesz)*/
			   this.timeuntilcdend = this.skillStartedMainTime + this.cdtime - (Game.maintime);
				
				/*Itt azt vizsgáljuk,hogy a skill használható-e,azaz nincs rajta cd(vagy majd a mana vizsgálat ,illetve stun effect
				 stb.. is kelll),mert ha nincs akkor a skill1useable true legyen a hudmanagerben,azaz a kis skillkép
				 legyen kivilágítva.(skill1 azaz ez a bolt a használójának az elsõ skillje)*/
				if(this.skillStartedMainTime + this.cdtime < Game.maintime || skillStartedMainTime == 0){
					player.monitorScreenmanager.skill0useable = true;
				}
		   }   
}