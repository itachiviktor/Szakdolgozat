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
	/*Ez az osztály az abstract Entitybõl származik, viszont továbbra is abstract marad.Itt definiálok mindent
	 amit egy karakternek tudnia kell.A Player egy közös õs, és leszármazott például a mágus,a harcos, 
	 amelyeket a felhasználó kedvérirányíthat.A funkcióik közösek, viszont a megvalósítás eltérõ, elég arra gondolni
	 ,hogy minden karakter 7 képességgel rendelkezik, de azok teljesen eltérõek más-más harcos típusnál.*/
	
	public boolean up,down,right,left;/*mozgásért felelõs változók*/
	public boolean fire;/*Ez a változó a sima lövést engedélyezi a tick metódusban,ezt egérseseménykor állítjuk*/
	public boolean doublefire;/*Ez a változó a dupla lövést engedélyezi a tick metódusban,ezt egérseseménykor állítjuk*/
	
	public boolean onegunman = true;/*Melyik figura legyen kirajzolva, alapértelmezetten az egykezes fegyós
	gyúros ember*/
	public boolean twogunman = false;/*Kétkezes fegyveres fegyós ember*/
	
	public boolean dead = false;/*halott-e a player*/
	public boolean live = true;/*él-e még a player*/
	
	public String username = null;
	
	public ArrayList<Bullet> bullets = new ArrayList<Bullet>();/*A kilõtt golyókat tároló lista*/
	public Bolt bolt = null;/*A villám támadás objektum, alapértelmezetten null,majd konstruktorban példányosítom*/
	public FireBolt firebolt = null;/*Ugyan az a helyzet,mint a boltnál*/
	
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
	
	
	public double velocityX = 0;/*velocity- sebesség*/
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
	 
	 public abstract void moveForward(int sx, int sy);/*elõre mozgásért felelõs metódus*/

	   public abstract void moveBackword(int sx, int sy);/*hátra mozgásért felelõs metódus*/
	 
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
	public abstract void setHealth(int health);/*a karakter életerejét beállító metódus*/
	
	public abstract boolean isDead();

	public abstract void setDead(boolean dead);

	public abstract boolean isLive();

	public abstract void setLive(boolean live);

	@Override
	public abstract void die();/*ezzel a metódussal öljük meg a karaktert*/
	
	 public abstract Polygon getPolygon();
	 
	 @Override
	 public abstract Rectangle getDamagedArea();
	 
	 @Override
	public abstract Rectangle getCollisionArea();

	 /*az alábbi metódusok az eseménykezelésért felelõsek, a Handler ezeket a metódusokat fogja meghívni, amikor
	  valamilyen esemény keletkezik, hisz csak a karakter tudhatja, hogy egyes skillek aktiválásakor mit és mikor 
	  kell csinálnia.*/
	 
	public abstract void keyPressed(int key);

	public abstract void keyReleased(int key);
	
	public abstract void MousePressed(MouseEvent e);
	
	public abstract void MouseReleased(int buttonkey);
	
}