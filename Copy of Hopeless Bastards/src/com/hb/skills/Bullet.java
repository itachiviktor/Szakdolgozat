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
	   public int pipe;/*0-bal 1-jobb 2-közép    ez azt tartalmazza,hogy a golyó milyen fegyverbõl jött ki,egykezes nagy az közép
	   a kétkezes pisztolynál lehet a baloldali fegyverbõl vagy a jobboldali fegyverbõl.*/
	   
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
	      
	      this.startShoot = true;/*Ez a puskacsõbõl kijövõ szikra animáció miatt kell,ez jelzi,hogy kezdõállapotban van a golyó
	      kilövése,mihejst vége az animációnak,a player ezt az értéket false-ra állítja.*/
	      
	      /*A golyóknál nagyon fontos szerepe van a konstruktornak.Ugyanis itt pont a pisztoly elé helyezzük a létrehozott golyót.
	       polygon változóban az elforgatott, a lövés irányába forgatott polygont rakjuk.
	       Ennek lekérdezve az elsõ x,y koordinátáját megkapjuk a balfelsõ sarok reprezentációját.
	       */
	      
	      polygon = this.getPolygon();
	      
	      this.px = polygon.xpoints[0];
	      this.py = polygon.ypoints[0];
	      
	     /*A polygon csúcspontjai*/
	      int[] polx = polygon.xpoints;
	      int[] poly = polygon.ypoints;
	      
	    /*Ltrehozok 4 db point objektumot,hisz tudom,hogy 4 szög polygonról lesz mindíg szó.Feltöltöm a polygon objektum csúcsainak
	     értékeivel.*/
	      Point2D[] ps = new Point2D[4];
	      for(int i=0;i<4;i++){
	    	  ps[i] = new Point2D.Float(polx[i], poly[i]);
	      }
	      
	      /*Ebbe a változóba a polygon középpontját rakjuk,amit a centerOfMass() metódus számol ki.*/
	      center = this.centerOfMass(ps);
	      
	      /*Mivel Rectangle nem forgatható, ezért számolgattam polygonnal, majd annak értékét átalakítom rectanglere.
	       Azaz a középpont megvan,akkor a bal felsõ sarok abból könnyen kitalálható, és be is állítom.*/
	      this.x=center.getX()-w/2;
	      this.y=center.getY()-h/2;
	      
	      /*Beállítok egy írányegyenest, amit követni fog a golyó.Ezt az irányvektort elforgatom adott szöggel.*/
	      px += Math.cos(Math.toRadians(angle))*6000;
	      py += Math.sin(Math.toRadians(angle))*6000;
	      
	      /*A távolság megvan.*/
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
		   /*Elõre mozgatjuk a golyót.A konstruktorban kiszámolt irányvektort követve.A számolási mód a füzetben van, az a lényeg,hogy
		    adott pontot ,hogyan követhet egy objektum.*/
		   
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
		   /*Végigmegy az összes entitáson,és ha ütkötik valamelyikkel,azaz a golyó középpontja benne van az entitás négyszög
		    területében ,akkor sebez,és a golyót eltüntetjük a player golyó listájából,ezálltal nem létezik tovább.*/
		   
		   /*Végignézzük az entityben lévõ és az enmy listában lévõ elemeket , hogy ütközött-e valamelyikkel.
		    Az entityben azért nem kell megvizsgálni, hogy playerrel ütközött-e, mert amelyik player kilõtte,
		    azzal ez sosem fog érintkezni, hisz a pisztoly csõbõl jön ki.*/
		   for(int i=0;i<handler.entity.size();i++){
			   ene = handler.entity.get(i);
			   if(ene.getPolygon().contains(this.bulletDamageAreaCenter())){
				   /*Itt megvizsgálom,hogy a playerhez tartozó buff rajta van-e,mert ha igen,akkor többet sebez a golyó.*/
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
			   /*Csak azzal ez enemyvel ütközhet, amelyik nem lõtte ki a golyót, tehát mivel
			    tudom hogy a player adattagba van eltárolva az a muscleman, aki kilõtte a golyót.
			    Ha ez az enemy nem egyenlõ azzal aki kilõtte a golyót, és még ütközik
			    is a golyóval akkor sebzést kap.(networkid vizsgálat, arra nézve , hogy a golyó
			    tulajdonosa vajon megegyezik e az aktuálisan vizsgált enemy-vel.)*/
			   
			   if(!(en.networkId.equals(player.networkId)) && en.getPolygon().contains(this.bulletDamageAreaCenter())){
				   /*Itt megvizsgálom,hogy a playerhez tartozó buff rajta van-e,mert ha igen,akkor többet sebez a golyó.*/
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
		   
		   /*Végigmegy az összes pályalemen,és ami solid és ütközik a golyóval,az megfogja a golyót,így az nem megy tovább.*/
		   for(int i=0;i<handler.tile.size();i++){
			   t = handler.tile.get(i);
			   if(t.solid==true && t.getBounds().contains(this.bulletDamageAreaCenter())){
				   player.bullets.remove(this);
			   }
		   }
	   }
	   
	   public Point bulletDamageAreaCenter(){
		   /*Visszaadja,hogy a golyónak mely része sebez,azaz a képernyõn mely pontjával kell ütköznie pl a zombienak ,hogy
		    sebzõdjön.*/
		   
		   //Rectangle rect = this.getBounds();
		   this.bulletDamagedCenterPoint.setLocation(this.getBounds().x+width/2,this.getBounds().y+height);
		   return this.bulletDamagedCenterPoint;
		   //return new Point(rect.x+width/2,rect.y+height);
	   }
	   
	   /*Az alábbi két metódus polygon középontot számol,különösebb magyarázatot nem fûznék hozzá.*/
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
