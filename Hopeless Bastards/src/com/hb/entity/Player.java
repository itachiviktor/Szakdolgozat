package com.hb.entity;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import com.hb.Collision;
import com.hb.Game;
import com.hb.Id;
import com.hb.gamestate.Handler;
import com.hb.graphics.ImageAssets;
import com.hb.inventory.Inventory;
import com.hb.managers.MonitorScreenManager;
import com.hb.math.RotateViktor;
import com.hb.skills.FireBolt;
import com.hg.muscleman.Bullet;
import com.hg.muscleman.Skill0;
import com.hg.muscleman.Skill1;
import com.hg.muscleman.Skill2;
import com.hg.muscleman.Skill3;
import com.hg.muscleman.Skill4;
import com.hg.muscleman.Skill5;
import com.hg.muscleman.Skill6;

public abstract class Player extends Entity{
	/*Ez az oszt�ly az abstract Entityb�l sz�rmazik, viszont tov�bbra is abstract marad.Itt defini�lok mindent
	 amit egy karakternek tudnia kell.A Player egy k�z�s �s, �s lesz�rmazott p�ld�ul a m�gus,a harcos, 
	 amelyeket a felhaszn�l� kedv�rir�ny�that.A funkci�ik k�z�sek, viszont a megval�s�t�s elt�r�, el�g arra gondolni
	 ,hogy minden karakter 7 k�pess�ggel rendelkezik, de azok teljesen elt�r�ek m�s-m�s harcos t�pusn�l.*/
	
	public boolean up,down,right,left;/*mozg�s�rt felel�s v�ltoz�k*/
	public boolean fire;/*Ez a v�ltoz� a sima l�v�st enged�lyezi a tick met�dusban,ezt eg�rsesem�nykor �ll�tjuk*/
	public boolean doublefire;/*Ez a v�ltoz� a dupla l�v�st enged�lyezi a tick met�dusban,ezt eg�rsesem�nykor �ll�tjuk*/
	
	public boolean onegunman = true;/*Melyik figura legyen kirajzolva, alap�rtelmezetten az egykezes fegy�s
	gy�ros ember*/
	public boolean twogunman = false;/*K�tkezes fegyveres fegy�s ember*/
	
	public boolean dead = false;/*halott-e a player*/
	public boolean live = true;/*�l-e m�g a player*/
	
	public String username = null;
	
	public ArrayList<Bullet> bullets = new ArrayList<Bullet>();/*A kil�tt goly�kat t�rol� lista*/
	public Bolt bolt = null;/*A vill�m t�mad�s objektum, alap�rtelmezetten null,majd konstruktorban p�ld�nyos�tom*/
	public FireBolt firebolt = null;/*Ugyan az a helyzet,mint a boltn�l*/
	
	public boolean skill0started = false;
	public boolean skill1started = false;
	public boolean skill2started = false;
	public boolean skill3started = false;
	public boolean skill4started = false;
	public boolean skill5started = false;
	public boolean skill6started = false;
	
	public String networkId;
	

	
	public int playerframe = 0;
	public int playerframePerSec = 0;
	
	public int framePerSecLimit = 4;
	
	
	public int mana;
	public int maxMana;
	
	
	public double velocityX = 0;/*velocity- sebess�g*/
    public double velocityY = 0;
    
    public int movementSpeed = 6;
    
    public Polygon polygon = null;
    public int px,py;
	
	public MonitorScreenManager monitorScreenmanager;
	
	public AbstractSkill[] skills = new AbstractSkill[7];
	
	public Inventory inventory;
	
	
	public Player(double x, double y, int width, int height, Id id,String networkId, Handler handler) {
		super(x, y, width, height, id, handler);
		monitorScreenmanager = new MonitorScreenManager(this);
		this.networkId = networkId;
		inventory = new Inventory((int)this.x-500,(int)this.y, 6, handler);
		inventory.init();
	}
	
	 public abstract void setAngle(int angle);
	 
	 public abstract void moveForward(int sx, int sy);/*el�re mozg�s�rt felel�s met�dus*/

	   public abstract void moveBackword(int sx, int sy);/*h�tra mozg�s�rt felel�s met�dus*/
	 
	   public abstract double getAngle();
	   
	   public abstract Rectangle getBounds();
	
	@Override
	public abstract void tick();

	public abstract void render(Graphics g);

	public abstract double getX();

	public abstract void setX(double x);

	public abstract double getY();

	public abstract void setY(double y);

	public abstract int getWidth();
	public abstract void setWidth(int width);

	public abstract int getHeight();

	public abstract void setHeight(int height);

	@Override
	public abstract void setHealth(int health);/*a karakter �leterej�t be�ll�t� met�dus*/
	
	public abstract boolean isDead();

	public abstract void setDead(boolean dead);

	public abstract boolean isLive();

	public abstract void setLive(boolean live);

	@Override
	public abstract void die();/*ezzel a met�dussal �lj�k meg a karaktert*/
	
	 public abstract Polygon getPolygon();
	 
	 @Override
	 public abstract Rectangle getDamagedArea();
	 
	 @Override
	public abstract Rectangle getCollisionArea();

	 /*az al�bbi met�dusok az esem�nykezel�s�rt felel�sek, a Handler ezeket a met�dusokat fogja megh�vni, amikor
	  valamilyen esem�ny keletkezik, hisz csak a karakter tudhatja, hogy egyes skillek aktiv�l�sakor mit �s mikor 
	  kell csin�lnia.*/
	 
	public abstract void keyPressed(int key);

	public abstract void keyReleased(int key);
	
	public abstract void MousePressed(MouseEvent e);
	
	public abstract void MouseReleased(int buttonkey);
	
}