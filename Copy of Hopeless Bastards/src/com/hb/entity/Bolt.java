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
	private double x, y, angle; // x,y and angle(sz�g)
	   private int width, height; //width and height 
	   private Handler handler;
	   private Player player;
	   private Sound boltSong;/*a vill�ml�s wav hangfileja*/
	   
	   private boolean damagingNow = false;/*Ez a tick met�dsu sz�m�ra fontos,azt jelzi,hogy meddig kell sebezni,�s most jelenleg
	   sebez-e a t�mad�s.*/
	   public int cdtime = 1;/*ct a skillre*/
	   private int skillStartedMainTime = 0;/*v�ltoz� inicializ�l�sa, ami a buff eltol�s�nak kezd�ideje,
	   el�sz�r az�rt null�ra �ll�tom,mert �gy ha m�g sosem volt haszn�lva a j�t�kind�t�s �ta a skill
	   akkor ha ez az �rt�k nulla, akkor nincs rajta cd, �s lehet haszn�lni, azaz megh�v�dhat az activateSkill met�dus belseje.*/
	   
	   public int damageValue = 250;/*Ennyi �leter�t vesz le arr�l,akit �r a t�mad�s*/
	   private int manacost = 100;/*Ennyi man�t vesz le a haszn�lata a t�mad�snak*/
	   
	   
	   private int boltframe = 0;/*A k�pkock�k v�ltakoz�s�nak 2 v�ltoz�ja*/
	   private int boltframePerSec = 0;
	   
	   private boolean animaionStillRuning = false;/*A render met�dusnak jelzi,hogy kell-e m�g a kirajzol�st csin�lnia vagy sem,
	   azaz v�get �rt az anim�ci�.*/
	   
	   public int timeuntilcdend = 0;/*Ez az id� m�sodpercben,ami azt az �rt�ket tartalmazza,hogy mennyi m�sodperc van m�g
	   a cd lej�r�s�ig.*/
		
		
	   public Bolt(Handler handler,Player player) {
		   /*konstruktorban nem inicializ�lom a poz�ci� adattagokat, az mind�g a player helyzet�t�l fog f�ggni.*/
		   	this.player = player;
		    this.handler = handler;
		    boltSong = new Sound("/lightning.wav");
		 
	   }
	   
	   public void activateSkill(double x,double y,double angle,int width,int height){
		   
		   /*Ez a met�dus az, amit a player objektum megh�v, ha a t�mad�st el kell ind�tani.
		    Csak akkor hajtja v�gre az utas�t�sokat, ha a skillkezd�si ideje + a cd ideje kisebb mint a jelenlegi
		    id�, hisz ez azt jelenti,hogy lej�rt a cd a skillre,vagy ha a skillkezd�sideje v�ltoz� �rt�ke nulla,
		    az azt jelenti,hogy m�g sosem volt haszn�lva a skill a j�t�k bet�lt�se �ta, azaz tuti nincs rajta cd. */
		   if(this.skillStartedMainTime + this.cdtime < Game.maintime || skillStartedMainTime == 0){
			   /*A skill haszn�latakor els� l�p�sben lej�ttszom a vill�m hangot.*/
			   boltSong.play();
			   
			   /*A playern�l leveszem a man�t,amibe a skill ker�lt.*/
			   if(player.mana - this.manacost < 0){
			    	player.mana = 0;
			    }else{
			    	player.mana-=this.manacost;
			    }
			   /*be�ll�tom a param�terben kapott koordin�t�kat �s sz�get, ami a player saj�t helyzete is egyben.*/
			   this.x = x;
			   this.y = y;
			   this.angle = angle;
			   this.width = width;
			   this.height = height;
			   
			   /*Be�ll�tom a skillkezd�si id�t, a jelenlegi id�re, amit a Game oszt�ly maintime v�ltoz�ja t�rol*/
			   this.skillStartedMainTime = Game.maintime;
			   
			   damagingNow = true;/*most sebezhet a skill*/
			   animaionStillRuning = true;/*most m�r kirajzolhatjuk az anim�ci�t.*/
			   player.monitorScreenmanager.skill0useable = false;/*A skillk�p els�t�t�l,hisz cd ker�l a skillre*/
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
			   /*Visszaadja a befoglal� n�gysz�get.*/
				return new Rectangle((int)x,(int)y,(int)width,(int)height);
		   }
		   
		   public Polygon getPolygon(){
			   /*Visszaadja egy bizonyos sz�ggel elforgatott polygont.Ez a polygon vizsg�lja,hogy van-e mettsz�spontja vmelyik
			    entit�ssal,mert akkor sebz�s az mehet.*/
			   return RotateViktor.rotate(new Point((int)x,(int)y), new Point((int)x + width,(int)y), new Point((int)x+width,(int)y+height) ,
					   new Point((int)x,(int)y+height),angle,(int)(player.x+player.width/2), (int)(player.y+player.height/2));
		   }
		   
		   public void render(Graphics g){
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
				    	  
				    	  if(boltframePerSec < 4){
								if(boltframe < 10){
									g.drawImage(ImageAssets.bolt[boltframe], (int)player.x + player.width, (int)player.y-118, 512,300,null);
								}
								boltframePerSec++;
				    	  }else{
				    		  /*Ha a boltFramePerSec nagyobb mint 4,akkor azt jelenti,hogy a k�vetkez� k�pet kell kirajzolni az 
				    		   anim�ci�ban.Ez�rt megint null�ra megy a sz�ml�l�.*/
				    		  boltframePerSec = 0;
				    		  
				    		  /*Ha a kirajzolt k�p m�g nem az utols� anim�ci�s k�p, akkor n�velj�k az index�rt�ket,hisz nem lesz
				    		   ArrayOutIndexException.*/
				    		  if(boltframe<9){
									g.drawImage(ImageAssets.bolt[boltframe], (int)player.x + player.width, (int)player.y-118, 512,300,null);
									boltframe++;
									
								}else{
									/*Viszont ,ha m�r az utols� anim�ci�s k�pkocka vet�t�si ideje is lej�rt, akkor be�ll�tom az
									 animationStillRuning met�dust falsera, azaz a render met�dus belseje nem fog v�grejat�dni.
									 Illetve a k�pkocka renderel� v�ltoz�kat alap�rtelmezettre �ll�tom,hisz ha �jra kell renderelni
									 akkor mindent el�r�l kell kezdeni.*/
									animaionStillRuning = false;
									boltframe = 0;
									boltframePerSec = 0;
								}
				    	  }
				    	 
				    	g2d.setTransform(old);
			 	}
			  
		   }
		   
		   public void tick(){
			   /*A tick met�dus�ban megvizsg�lom,hogy most sebez-e a skill.Ezt a v�ltoz�t az activateSkill met�dusban �ll�tom igazra.
			    V�gigmegy az �sszes entit�son, �s ellen�rzi,hogy melyik entit�snak van mettsz�spontja a vill�ml�si
			    ter�lettel(a bolt oszt�ly polygonja), �s azoknak az entit�soknak az �let�t cs�kkenti.*/
			   if(damagingNow){
				   for(int i=0;i<handler.entity.size();i++){
					   Entity en = handler.entity.get(i);
					   if(en.getId() != Id.PLAYER && getPolygon().intersects(en.getBounds())){
						   en.setHealth(-damageValue);
						   handler.damagetext.add(new DamagingText(en.x, en.y,String.valueOf(damageValue),true, handler));
					   }
				   }
				 /*Mivel csak egyszer szeretn�nk,hogy sebezzen a skill,nem pedig m�sodpercenk�nt 60-szor, ez�rt r�gt�n az els� haszn�lat
				 ut�n hamisra �ll�tjuk a damagingNow v�ltoz�t, �gy a sebz�s nem fog megt�rt�nni t�bbet, csak ha �jra elnyomjuk a skillt
				 teh�t ez a skill egyszeri sebz�s,nem olyan,hogy m�sodpercenk�nt sebez.*/
				damagingNow = false;
			   }
			   
			   /*Itt kisz�molom a tick met�dusban mind�g,hogy h�ny m�sodperc van m�g h�tra am�g �jra haszn�lhatom a skillt.
			    Ezt az�rt a tick met�dusban kell kisz�molni,mert ez sokszor megh�v�dik, �e ez az �rt�k m�sodpercenk�nt ugyeb�r
			    cs�kkenni fog.Az a l�nyeg,hogy a skillkezd�si id�+cd id�b�l , ha kivonom a jelenlegi id�t, az visszaadja,hogy 
			    mennyi id� van m�g h�tra(azaz visszasz�ml�l�s lesz)*/
			   this.timeuntilcdend = this.skillStartedMainTime + this.cdtime - (Game.maintime);
				
				/*Itt azt vizsg�ljuk,hogy a skill haszn�lhat�-e,azaz nincs rajta cd(vagy majd a mana vizsg�lat ,illetve stun effect
				 stb.. is kelll),mert ha nincs akkor a skill1useable true legyen a hudmanagerben,azaz a kis skillk�p
				 legyen kivil�g�tva.(skill1 azaz ez a bolt a haszn�l�j�nak az els� skillje)*/
				if(this.skillStartedMainTime + this.cdtime < Game.maintime || skillStartedMainTime == 0){
					player.monitorScreenmanager.skill0useable = true;
				}
		   }   
}