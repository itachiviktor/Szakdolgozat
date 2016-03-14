package com.hb.entity;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import com.hb.Id;
import com.hb.gamestate.Handler;

public abstract class Entity {
	/*Ez az oszt�ly arra szolg�l,hogy lesz�rmazzanak bel�le a j�t�k entit�sai(player,zombie,mozg� elemek).
	 Itt olyan v�ltoz�kat �s met�dusokat defini�lok,amik minden entit�snak vannak.*/
	
	public double x=0;
	public double y=0;
	public int width;
	public int height;
	
	public int health;
	
	public double angle;
		
	public Id id;
	
	public Handler handler;
	
	public boolean collideplayer = false;/*playerrel �tk�z�tt-e, erre az�rt van sz�ks�g,hogy az �tk�z�sn�l
	fontos tudni,hogy playerrel �tk�ztem-e.*/
	
	public Entity(double x,double y,int width,int height,Id id,Handler handler) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		this.id = id;
		this.handler = handler;
	}
	
	
	public abstract void render(Graphics g);
	
	public abstract void tick();
	

	public abstract void die();
	
	public abstract boolean isDead();
	
	public abstract Polygon getPolygon();/*Minden entit�snak lennie kell olyan met�dusnak,ami polygon-t ad vissza.*/
	
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
	
	/*Ez minden entit�sn�l ugyan �gy kell m�k�dnie.A contains met�dus azt csin�lja,hogy kap egy pontot,�s megn�zi,hogy a rectangle
	 -j�ben benne van-e a pont.*/
	public boolean contains(Point point){
		Rectangle rect = new Rectangle((int)x,(int)y,width,height);
		return rect.contains(point);
	}
	public int getHeight() {
		return height;
	}
	
	public Rectangle getBounds(){
		/*Mag�t az entit�st adja vissza*/
		return new Rectangle((int)this.x,(int)this.y,width,height);
	}
	
	public abstract Rectangle getDamagedArea();/*Minden entit�snak vissza kell tudni adnia azt a ter�letet, amin sebezhetik �t.*/
	public abstract Rectangle getCollisionArea();/*Minden entit�snak vissza kell tudnia adni azt a ter�letet, ami �tk�z�sben
	szerepet j�ttszik, ez �ltal�ban valamivel nagyobb,mint a getDamagedArea*/
	
}
