package com.hb.skills;


import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import com.hb.Id;
import com.hb.entity.DamagingText;
import com.hb.entity.Entity;
import com.hb.entity.Player;
import com.hb.gamestate.Handler;
import com.hb.math.RotateViktor;
import com.hb.tile.Tile;

public class Bullet {
	   private double x, y, angle; 
	   public int pipe;/*0-bal 1-jobb 2-k�z�p    ez azt tartalmazza,hogy a goly� milyen fegyverb�l j�tt ki,egykezes nagy az k�z�p
	   a k�tkezes pisztolyn�l lehet a baloldali fegyverb�l vagy a jobboldali fegyverb�l.*/
	   
	   public double px,py;
	   private int width, height; 
	   private Handler handler;
	   private Player player;
	   public Polygon polygon = null;
	   public Point distance;
	   public Rectangle rect;
	   public double velocityX;
	   public double velocityY;
	   private Point2D center;
	   private int dealingDamage = 50;
	   
	   public boolean startShoot;
	   
	   public int bulletframe = 0;
	   public int bulletframePerSec = 0;
	   
	   private Player en;
	   
	   private Point bulletDamagedCenterPoint = new Point();
	   private Tile t;
	   private Entity ene;
	 
	   public Bullet(double x, double y, double a, int w, int h,int pipe,Handler handler,Player player) {
		  this.player = player;
	      this.x = x;
	      this.y = y;
	      this.angle = a;
	      this.width = w;
	      this.height = h;
	      this.handler = handler;
	      
	      this.pipe = pipe;
	      
	      this.startShoot = true;/*Ez a puskacs�b�l kij�v� szikra anim�ci� miatt kell,ez jelzi,hogy kezd��llapotban van a goly�
	      kil�v�se,mihejst v�ge az anim�ci�nak,a player ezt az �rt�ket false-ra �ll�tja.*/
	      
	      /*A goly�kn�l nagyon fontos szerepe van a konstruktornak.Ugyanis itt pont a pisztoly el� helyezz�k a l�trehozott goly�t.
	       polygon v�ltoz�ban az elforgatott, a l�v�s ir�ny�ba forgatott polygont rakjuk.
	       Ennek lek�rdezve az els� x,y koordin�t�j�t megkapjuk a balfels� sarok reprezent�ci�j�t.
	       */
	      
	      polygon = this.getPolygon();
	      
	      this.px = polygon.xpoints[0];
	      this.py = polygon.ypoints[0];
	      
	     /*A polygon cs�cspontjai*/
	      int[] polx = polygon.xpoints;
	      int[] poly = polygon.ypoints;
	      
	    /*Ltrehozok 4 db point objektumot,hisz tudom,hogy 4 sz�g polygonr�l lesz mind�g sz�.Felt�lt�m a polygon objektum cs�csainak
	     �rt�keivel.*/
	      Point2D[] ps = new Point2D[4];
	      for(int i=0;i<4;i++){
	    	  ps[i] = new Point2D.Float(polx[i], poly[i]);
	      }
	      
	      /*Ebbe a v�ltoz�ba a polygon k�z�ppontj�t rakjuk,amit a centerOfMass() met�dus sz�mol ki.*/
	      center = this.centerOfMass(ps);
	      
	      /*Mivel Rectangle nem forgathat�, ez�rt sz�molgattam polygonnal, majd annak �rt�k�t �talak�tom rectanglere.
	       Azaz a k�z�ppont megvan,akkor a bal fels� sarok abb�l k�nnyen kital�lhat�, �s be is �ll�tom.*/
	      this.x=center.getX()-w/2;
	      this.y=center.getY()-h/2;
	      
	      /*Be�ll�tok egy �r�nyegyenest, amit k�vetni fog a goly�.Ezt az ir�nyvektort elforgatom adott sz�ggel.*/
	      px += Math.cos(Math.toRadians(angle))*6000;
	      py += Math.sin(Math.toRadians(angle))*6000;
	      
	      /*A t�vols�g megvan.*/
	      distance = new Point((int)px,(int)py);

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

	 
	  public void moveForward(int speed) {
		   /*El�re mozgatjuk a goly�t.A konstruktorban kisz�molt ir�nyvektort k�vetve.A sz�mol�si m�d a f�zetben van, az a l�nyeg,hogy
		    adott pontot ,hogyan k�vethet egy objektum.*/
		   
		   double newX = this.distance.x;
	        double newY = this.distance.y;
	        double distX = newX - this.x;
	        double distY = newY - this.y;
	        double length = Math.sqrt(((distX * distX) + (distY * distY)));
	      
	        if(length >= 1){
	        	 velocityX = distX/length*speed;
	             velocityY = distY/length*speed;
	        }
	        
		    this.x += velocityX;
	        this.y += velocityY;
	   }
	   
	   public Rectangle getBounds(){
			return new Rectangle((int)x,(int)y,(int)width,(int)height);
	   }
	   
	   public Polygon getPolygon(){
		   return RotateViktor.rotate(new Point((int)x,(int)y), new Point((int)x + width,(int)y), new Point((int)x+width,(int)y+height) ,
				   new Point((int)x,(int)y+height),angle,(int)(player.x+player.width/2), (int)(player.y+player.height/2));
	   }
	   public Point getWay(){
		   return new Point((int)px,(int)py);
	   }
	 
	   public void tick(){
		   /*V�gigmegy az �sszes entit�son,�s ha �tk�tik valamelyikkel,azaz a goly� k�z�ppontja benne van az entit�s n�gysz�g
		    ter�let�ben ,akkor sebez,�s a goly�t elt�ntetj�k a player goly� list�j�b�l,ez�lltal nem l�tezik tov�bb.*/
		   
		   /*V�gign�zz�k az entityben l�v� �s az enmy list�ban l�v� elemeket , hogy �tk�z�tt-e valamelyikkel.
		    Az entityben az�rt nem kell megvizsg�lni, hogy playerrel �tk�z�tt-e, mert amelyik player kil�tte,
		    azzal ez sosem fog �rintkezni, hisz a pisztoly cs�b�l j�n ki.*/
		   for(int i=0;i<handler.entity.size();i++){
			   ene = handler.entity.get(i);
			   if(ene.getPolygon().contains(this.bulletDamageAreaCenter())){
				   /*Itt megvizsg�lom,hogy a playerhez tartoz� buff rajta van-e,mert ha igen,akkor t�bbet sebez a goly�.*/
				   if(player.skills[1].isactivated){
					   ene.setHealth(- this.dealingDamage * 10);
					   player.bullets.remove(this);
					   handler.damagetext.add(new DamagingText(ene.x, ene.y,String.valueOf(this.dealingDamage*10),true, handler));
				   }else{
					   ene.setHealth(- this.dealingDamage);
					   player.bullets.remove(this);
					   handler.damagetext.add(new DamagingText(ene.x, ene.y,String.valueOf(this.dealingDamage),true, handler));
				   }
			   }
		   }
		   
		   for(int i=0;i<handler.enemies.size();i++){
			   en = handler.enemies.get(i);
			   /*Csak azzal ez enemyvel �tk�zhet, amelyik nem l�tte ki a goly�t, teh�t mivel
			    tudom hogy a player adattagba van elt�rolva az a muscleman, aki kil�tte a goly�t.
			    Ha ez az enemy nem egyenl� azzal aki kil�tte a goly�t, �s m�g �tk�zik
			    is a goly�val akkor sebz�st kap.(networkid vizsg�lat, arra n�zve , hogy a goly�
			    tulajdonosa vajon megegyezik e az aktu�lisan vizsg�lt enemy-vel.)*/
			   
			   if(!(en.networkId.equals(player.networkId)) && en.getPolygon().contains(this.bulletDamageAreaCenter())){
				   /*Itt megvizsg�lom,hogy a playerhez tartoz� buff rajta van-e,mert ha igen,akkor t�bbet sebez a goly�.*/
				   if(player.skills[1].isactivated){
					   en.setHealth(- this.dealingDamage * 10);
					   player.bullets.remove(this);
					   handler.damagetext.add(new DamagingText(en.x, en.y,String.valueOf(this.dealingDamage*10),true, handler));
				   }else{
					   en.setHealth(- this.dealingDamage);
					   player.bullets.remove(this);
					   handler.damagetext.add(new DamagingText(en.x, en.y,String.valueOf(this.dealingDamage),true, handler));
				   }
			   }
		   }
		   
		   /*V�gigmegy az �sszes p�lyalemen,�s ami solid �s �tk�zik a goly�val,az megfogja a goly�t,�gy az nem megy tov�bb.*/
		   for(int i=0;i<handler.tile.size();i++){
			   t = handler.tile.get(i);
			   if(t.solid==true && t.getBounds().contains(this.bulletDamageAreaCenter())){
				   player.bullets.remove(this);
			   }
		   }
	   }
	   
	   public Point bulletDamageAreaCenter(){
		   /*Visszaadja,hogy a goly�nak mely r�sze sebez,azaz a k�perny�n mely pontj�val kell �tk�znie pl a zombienak ,hogy
		    sebz�dj�n.*/
		   
		   //Rectangle rect = this.getBounds();
		   this.bulletDamagedCenterPoint.setLocation(this.getBounds().x+width/2,this.getBounds().y+height);
		   return this.bulletDamagedCenterPoint;
		   //return new Point(rect.x+width/2,rect.y+height);
	   }
	   
	   /*Az al�bbi k�t met�dus polygon k�z�pontot sz�mol,k�l�n�sebb magyar�zatot nem f�zn�k hozz�.*/
	   public double area(Point2D[] polyPoints) {
			int i, j, n = polyPoints.length;
			double area = 0;

			for (i = 0; i < n; i++) {
				j = (i + 1) % n;
				area += polyPoints[i].getX() * polyPoints[j].getY();
				area -= polyPoints[j].getX() * polyPoints[i].getY();
			}
			area /= 2.0;
			return (area);
		}
	   public  Point2D centerOfMass(Point2D[] polyPoints) {
			double cx = 0, cy = 0;
			double area = area(polyPoints);
			// could change this to Point2D.Float if you want to use less memory
			Point2D res = new Point2D.Double();
			int i, j, n = polyPoints.length;

			double factor = 0;
			for (i = 0; i < n; i++) {
				j = (i + 1) % n;
				factor = (polyPoints[i].getX() * polyPoints[j].getY()
						- polyPoints[j].getX() * polyPoints[i].getY());
				cx += (polyPoints[i].getX() + polyPoints[j].getX()) * factor;
				cy += (polyPoints[i].getY() + polyPoints[j].getY()) * factor;
			}
			area *= 6.0f;
			factor = 1 / area;
			cx *= factor;
			cy *= factor;
			res.setLocation(cx, cy);
			return res;
		}
}
