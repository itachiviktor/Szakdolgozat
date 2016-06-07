package com.hb.entity;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import com.hb.Id;
import com.hb.gamestate.Handler;

public abstract class Entity {
	/*Ez az osztály arra szolgál,hogy leszármazzanak belõle a játék entitásai(player,zombie,mozgó elemek).
	 Itt olyan változókat és metódusokat definiálok,amik minden entitásnak vannak.*/
	
	public double x=0;
	public double y=0;
	public int width;
	public int height;
	
	public int health;
	public int maxHealth;
	
	public double angle;
		
	public Id id;
	
	public Handler handler;
	
	private Rectangle entitysRectangle;
	
	public boolean collideplayer = false;/*playerrel ütközött-e, erre azért van szükség,hogy az ütközésnél
	fontos tudni,hogy playerrel ütköztem-e.*/
	private Rectangle rect;
	
	public boolean heldover = false;/*Felette van e az egér,az entitás felett, mert ekkor majd négyzetet
	rajzolunk köré, és rálehet kattintani.*/
	
	public Entity(double x,double y,int width,int height,Id id,Handler handler) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		this.id = id;
		this.handler = handler;
		
		entitysRectangle = new Rectangle();
		entitysRectangle.x = (int)this.x;
		entitysRectangle.y = (int)this.y;
		entitysRectangle.width = width;
		entitysRectangle.height = height;
		
	}
	
	
	public abstract void render(Graphics g);
	
	public abstract void tick();
	

	public abstract void die();
	
	public abstract boolean isDead();
	
	public abstract Polygon getPolygon();/*Minden entitásnak lennie kell olyan metódusnak,ami polygon-t ad vissza.*/
	
	public int getHealth() {
		return health;
	}


	public abstract void setHealth(int health);


	public double getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	
	public Id getId(){
		return this.id;
	}
	
	public int getWidth() {
		return width;
	}
	
	/*Ez minden entitásnál ugyan úgy kell mûködnie.A contains metódus azt csinálja,hogy kap egy pontot,és megnézi,hogy a rectangle
	 -jében benne van-e a pont.*/
	public boolean contains(Point point){
		rect.x = (int)x;
		rect.y = (int)y;
		rect.width = width;
		rect.height = height;
		return rect.contains(point);
	}
	public int getHeight() {
		return height;
	}
	
	public Rectangle getBounds(){
		/*Magát az entitást adja vissza*/
		entitysRectangle.x = (int)this.x;
		entitysRectangle.y = (int)this.y;
		entitysRectangle.width = width;
		entitysRectangle.height = height;
		return this.entitysRectangle;
		
		//return new Rectangle((int)this.x,(int)this.y,width,height);
	}
	
	public abstract Rectangle getDamagedArea();/*Minden entitásnak vissza kell tudni adnia azt a területet, amin sebezhetik õt.*/
	public abstract Rectangle getCollisionArea();/*Minden entitásnak vissza kell tudnia adni azt a területet, ami ütközésben
	szerepet játtszik, ez általában valamivel nagyobb,mint a getDamagedArea*/
	
}
