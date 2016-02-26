package com.hb.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import com.hb.Collision;
import com.hb.Id;
import com.hb.gamestate.Handler;
import com.hb.graphics.ImageAssets;
import com.hb.math.RotateViktor;

public class Zombie extends Entity{
	private Player player = null;
	
	private double velocityX;
	private double velocityY;
	private int moveframePerSec = 0;
	private int moveframe = 0;
	
	private int attackframe=4;
	private int attackframePerSec=0;
	
	private int bleedingframePerSec = 0;
	private int bleedingframe = 0;
	
	private Timer timer;
	private boolean attackable = true;
	private boolean attacking = false;
	
	public int maxHealth = 1000;
	private boolean dead= false;
	private boolean live = true;
	private double angle = 0;
	private boolean bleeding = false;/*Ez a változó ahhoz kell, hogy a karakter vérzõ animációt le kell-e rajta
	játtszani.*/
	
	private double speed;
	private Random doublerandom;
	private Random intrandom;
	private double newX;
	private double newY;
	private double distX;
	private double distY;
	private double length;
	private double xold;
	private double yold;
	
	private Point left = new Point();
	private Point right = new Point();

	public Zombie(double x, double y, int width, int height, Id id,Entity trade,
			Handler handler) {
		super(x, y, width, height, id, handler);
		timer = new Timer();
		doublerandom = new Random();
		intrandom = new Random();
		this.player = (Player)trade;
		this.health = 1000;
		speed = doublerandom.nextDouble() + intrandom.nextInt(2)+1;
		
	}

	@Override
	public void render(Graphics g) {
		
		if(live){
			Graphics2D g2d = (Graphics2D) g;
		    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		          RenderingHints.VALUE_ANTIALIAS_ON);
		    
		    AffineTransform old = g2d.getTransform();
	
		  // g2d.drawPolygon(getPolygon());
	
		    g2d.rotate(Math.toRadians(angle), x + width/2,
			          y + height / 2);
		    
		    if(!attacking){
		    	/*Ha támadhat a zombie,akkor a támadó animáció lejáttszása.*/
		    	if(moveframePerSec < 4){
					if(moveframe<4){
						g.drawImage(ImageAssets.zombie[moveframe].getBufferedImage(), (int)x, (int)y, 63,63,null);
						
					}
					moveframePerSec++;
				}else{
					moveframePerSec = 0;
					if(moveframe<3){
						g.drawImage(ImageAssets.zombie[moveframe].getBufferedImage(), (int)x, (int)y, 63,63,null);
						moveframe++;
						
					}else{
						moveframe = 0;
						g.drawImage(ImageAssets.zombie[moveframe].getBufferedImage(), (int)x,(int) y, 63,63,null);
					}
				}
				
		    }else{
		    	/*Ha nem támad, akkor a mozgó animációt rendereli.*/
		    	if(attackframePerSec < 10){
					if(attackframe > 3 && attackframe < 6){
						g.drawImage(ImageAssets.zombie[attackframe].getBufferedImage(), (int)x, (int)y, 63,63,null);
					}
					attackframePerSec++;
				}else{
					attackframePerSec = 0;
					if(attackframe > 3 && attackframe < 5){
						g.drawImage(ImageAssets.zombie[attackframe].getBufferedImage(), (int)x, (int)y, 63,63,null);
						attackframe++;
						
					}else{
						attacking = false;
						attackframe = 4;
						attackframePerSec = 0;
						g.drawImage(ImageAssets.zombie[attackframe].getBufferedImage(), (int)x,(int) y, 63,63,null);
					}
				}
		    }
		    g2d.setTransform(old);
		    
		    
		    
			 g.setColor(Color.red);
			 g.drawRect(getCollisionArea().x,getCollisionArea().y, getCollisionArea().width,getCollisionArea().height);
		  //  g.drawPolygon(getPolygon());
			 g.setColor(Color.GREEN);
			 
			 g.fillRect((int)x + 1, (int)y - 9, width*this.health/this.maxHealth-1, 9);
			 g.setColor(Color.WHITE);
			 
			 
			 if(bleeding){
				 /*Ha elkapja egy támadás, akkor vérzik abba az irányba, ahonnan találat érte.*/
				 g2d.rotate(Math.toRadians(angle), x + width/2,
				          y + height / 2);
				if(bleedingframePerSec < 4){
					if(bleedingframe<6){
						g.drawImage(ImageAssets.bleeding[bleedingframe].getBufferedImage(), (int)x-width/2, (int)y-height/2, 128,128,null);
						
					}
					bleedingframePerSec++;
				}else{
					bleedingframePerSec = 0;
					if(bleedingframe<5){
						g.drawImage(ImageAssets.bleeding[bleedingframe].getBufferedImage(), (int)x-width/2, (int)y-height/2, 128,128,null);
						bleedingframe++;
						
					}else{
						bleedingframe = 0;
						bleedingframePerSec = 0;
						bleeding = false;
					}
			}
				 g2d.setTransform(old);
			 }
		}else if(dead){
			if(moveframePerSec < 10){
				if(moveframe < 10 && moveframe > 5){
					g.drawImage(ImageAssets.zombie[moveframe].getBufferedImage(), (int)x, (int)y, 63,63,null);
				}
				moveframePerSec++;
			}else{
					moveframe = 9;
					g.drawImage(ImageAssets.zombie[moveframe].getBufferedImage(), (int)x,(int) y, 63,63,null);
				
			}
		}
	}

	@Override
	public void tick() {
		
		if(!dead){
			/*Ha nem halott, akkor mozgást és forgást számol neki.*/
			 if(getBounds().intersects(handler.getVisibleArea())){
		    		Point a = new Point((int)width,(int)height/2);
					Point b = new Point((int)(player.x + player.width/2 - (x + width/2)),(int)(player.y + player.height/2- (y + width/2)));
					double scalarszorzat = a.x * b.x + a.y * b.y;
					double alength = Math.sqrt(a.x*a.x + a.y*a.y);
					double blength = Math.sqrt(b.x*b.x + b.y*b.y);
					
					
					if( b.y < 0){
						angle =180 + 180 -  Math.toDegrees(Math.acos(scalarszorzat/(alength*blength)));
					}else{
						angle = Math.toDegrees(Math.acos(scalarszorzat/(alength*blength)));
					}
					

					if (angle > 360) {
					       angle = 0;
					} else if (angle < 0) {
					       angle = 360;
					}
		    	}
			
			boolean menjtovabb = false;
			
			
			newX = player.x+player.width/2;
	        newY = player.y+player.height/2;
	   
	        distX = newX - this.x+this.width/2;
	        distY = newY - this.y+this.height/2;
	        length = Math.sqrt(((distX * distX) + (distY * distY)));
	        if(length >= 1){
	             this.velocityX = speed*distX/length;
	             this.velocityY = speed*distY/length;
	        }else{
		        this.velocityX = 0;
		        this.velocityY = 0;
	        }
	        
	        xold = x;
			yold = y;
	        
		    x += this.velocityX;
	        y += this.velocityY;
	
	        if(/*Collision.EntityCollisionEntityWithPolygon(getPolygon(), this, handler*/
	        		Collision.EntityCollisionEntity1(getCollisionArea(), this, handler)){
	        	
	        	x = xold;
				y = yold;
				menjtovabb = true;
				if(this.collideplayer){
	        		menjtovabb = false;
	        	}
	        }else{
	        	
	        	menjtovabb = false;
	        }
	        
	        if(menjtovabb){
	        	/*ha elõre nem mehet, 90 fokkal elforgatva megnézzük,hogy mozoghat-e.*/
	        	  left = RotateViktor.rotatePoint(new Point((int)newX,(int)newY), 90, (int)this.x+this.width/2, (int)this.y+this.height/2);
	        	  distX = left.x - this.x+this.width/2;
	  	        distY = left.y - this.y+this.height/2;
	  	        length = Math.sqrt(((distX * distX) + (distY * distY)));
		        if(length >= 1){
		             this.velocityX = speed*distX/length;
		             this.velocityY = speed*distY/length;
		        }else{
			        this.velocityX = 0;
			        this.velocityY = 0;
		        }
		        
		        xold = x;
				yold = y;
		        
			    x += this.velocityX;
		        y += this.velocityY;
		        
			
		        if(/*Collision.EntityCollisionEntityWithPolygon(getPolygon(), this, handler*/
		        		Collision.EntityCollisionEntity1(getCollisionArea(), this, handler)){
		        	x = xold;
					y = yold;
					if(this.collideplayer){
		        		menjtovabb = false;
		        	}
					
		        }else{
		        	menjtovabb = false;
		        }
		        
	        }
	        
	        if(menjtovabb){
	        	/*Ha az elõzõ két irányba sem mozoghat,akkor megnézi,hogy -90 fokkal elfordulva mozoghat-e elõre, ha nem
	        	 akkor abszolút nem mozoghat a zombie.*/
	        	  right = RotateViktor.rotatePoint(new Point((int)newX,(int)newY), -90, (int)this.x+this.width/2, (int)this.y+this.height/2);
	        	  distX = right.x - this.x+this.width/2;
	  	        distY = right.y - this.y+this.height/2;
	  	        length = Math.sqrt(((distX * distX) + (distY * distY)));
		        if(length >= 1){
		             this.velocityX = speed*distX/length;
		             this.velocityY = speed*distY/length;
		        }else{
			        this.velocityX = 0;
			        this.velocityY = 0;
		        }
		        
		        xold = x;
				yold = y;
		        
			    x += this.velocityX;
		        y += this.velocityY;
		        
			
		        if(/*Collision.EntityCollisionEntityWithPolygon(getPolygon(), this, handler*/
		        		Collision.EntityCollisionEntity1(getCollisionArea(), this, handler)){
		        	x = xold;
					y = yold;
					if(this.collideplayer){
		        		menjtovabb = false;
		        	}
					
		        }else{
		        	menjtovabb = false;
		        }
		        
	        }
	        
	        
	        
	     
	        
	        
	         
	        
	        
	        
	        
	        
	        
	        
	        
	      
	        
	        
		 
		   
		   
			if(getBounds().intersects(player.getBounds())){
				if(attackable){
					if(player.health - 200 > 0){
						attacking = true;
						attackable = false;
						player.setHealth(-200);
						handler.damagetext.add(new DamagingText(player.x, player.y, String.valueOf(200),true, handler));
						//attackable = false;
						timer.schedule(new TimerTask() {
				            @Override
				            public void run() {
				                attackable=true;
				            }
				    }, 2000);
				    }
				}
				
			}
	        
	       
		}
	}
	
	@Override
	public void setHealth(int health) {
		this.health += health;
		bleeding = true;
		if(this.health <= 0){
			die();
		}
	}
	
	@Override
	public void die() {
		
		this.dead = true;
		this.live = false;
	}
	
	 public Polygon getPolygon(){
		   return RotateViktor.rotate(new Point((int)x+9,(int)y+9), new Point((int)x + width-9,(int)y+9), new Point((int)x+width-9,(int)y+height-9) ,
				   new Point((int)x+9,(int)y+height-9),angle,(int)(x+width/2), (int)(y+height/2));
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	@Override
	public Rectangle getDamagedArea() {
		
		return new Rectangle((int)x+16, (int)y+16, width-32, height-32);
	}

	@Override
	public Rectangle getCollisionArea() {
		
		return new Rectangle((int)x+8, (int)y+8, width-16, height-16);
	}
}
