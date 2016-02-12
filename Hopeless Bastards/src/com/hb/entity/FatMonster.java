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

public class FatMonster extends Entity{
	/*Ugyan úgy mûködik mint a Zombie , ezeknek valami közös õst kelesz majd definiálni. */
	
	private Player player = null;
	private double velocityX;
	private double velocityY;
	private int moveframePerSec = 0;
	private int moveframe = 0;
	private boolean dead= false;
	private boolean live = true;
	private double angle = 0;
	private boolean bleeding = false;
	public int maxHealth = 1000;
	private int bleedingframePerSec = 0;
	private int bleedingframe = 0;
	private Random doublerandom;
	private Random intrandom;
	private double speed;
	
	private Timer timer;
	private boolean attackable = true;
	private boolean attacking = false;
	
	private int attackframe=9;
	private int attackframePerSec=0;

	
	public FatMonster(double x, double y, int width, int height, Id id,Entity trade,
			Handler handler) {
		super(x, y, width, height, id, handler);
		timer = new Timer();
		doublerandom = new Random();
		intrandom = new Random();
		this.player = (Player)trade;
		this.health = 1000;
		speed = doublerandom.nextDouble() + intrandom.nextInt(1)+1;
	}

	@Override
	public void render(Graphics g) {
		
		if(live){
			Graphics2D g2d = (Graphics2D) g;
		    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		          RenderingHints.VALUE_ANTIALIAS_ON);
		    
		    AffineTransform old = g2d.getTransform();
		   // g2d.drawRect((int)x, (int)y, width, height);
		   // g2d.drawRect((int)x+8, (int)y+8, width-16, height-16);
		    g2d.rotate(Math.toRadians(angle), x + width/2,
			          y + height / 2);
		    
		    if(!attacking){
		    	if(moveframePerSec < 10){
					if(moveframe<4){
						g.drawImage(ImageAssets.fatmonster[moveframe].getBufferedImage(), (int)x, (int)y, 63,63,null);
						
					}
					moveframePerSec++;
				}else{
					moveframePerSec = 0;
					if(moveframe<3){
						g.drawImage(ImageAssets.fatmonster[moveframe].getBufferedImage(), (int)x, (int)y, 63,63,null);
						moveframe++;
						
					}else{
						moveframe = 0;
						g.drawImage(ImageAssets.fatmonster[moveframe].getBufferedImage(), (int)x,(int) y, 63,63,null);
					}
				}
		    }else{
		    	if(attackframePerSec < 10){
					if(attackframe > 8 && attackframe < 11){
						g.drawImage(ImageAssets.fatmonster[attackframe].getBufferedImage(), (int)x, (int)y, 63,63,null);
					}
					attackframePerSec++;
				}else{
					attackframePerSec = 0;
					if(attackframe > 8 && attackframe < 10){
						g.drawImage(ImageAssets.fatmonster[attackframe].getBufferedImage(), (int)x, (int)y, 63,63,null);
						attackframe++;
						
					}else{
						attacking = false;
						attackframe = 9;
						attackframePerSec = 0;
						g.drawImage(ImageAssets.fatmonster[attackframe].getBufferedImage(), (int)x,(int) y, 63,63,null);
					}
				}
		    	
		    	
		    }
			 g2d.setTransform(old);
			 
			 g.drawPolygon(getPolygon());
			 
			// g.drawRect((int)x, (int)y-10, width,10);
			 g.setColor(Color.GREEN);
			 
			 g.fillRect((int)x + 1, (int)y - 9, width*this.health/this.maxHealth-1, 9);
			 g.setColor(Color.WHITE);
			 if(bleeding){
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
		}
		
		if(dead){
			if(moveframePerSec < 4){
				if(moveframe < 9 && moveframe > 3){
					g.drawImage(ImageAssets.fatmonster[moveframe].getBufferedImage(), (int)x, (int)y, 63,63,null);
					
				}
				moveframePerSec++;
			}else{
				
					moveframe = 8;
					g.drawImage(ImageAssets.fatmonster[moveframe].getBufferedImage(), (int)x,(int) y, 63,63,null);
			
			}
		}
		

	}

	@Override
	public void tick() {
		if(!dead){

			double newX = player.x+player.width/2;
	        double newY = player.y+player.height/2;
	        double distX = newX - this.x+this.width/2;
	        double distY = newY - this.y+this.height/2;
	         double length = Math.sqrt(((distX * distX) + (distY * distY)));
	         if(length >= 1){
	             this.velocityX = speed*distX/length;
	             this.velocityY = speed*distY/length;
	         }else{
	             this.velocityX = 0;
	             this.velocityY = 0;
	         }
	     
		 
		    
		    double xold = x;
			double yold = y;
	        
		    x += this.velocityX;
	        y += this.velocityY;
	        
		    if(Collision.EntityCollisionEntity1(getDamagedArea(),this,handler)){
		    	  	x = xold;
					y = yold;
			}
		    Rectangle rect = new Rectangle((int)x-4,(int)y-4,width+8,height+8);
			if(rect.intersects(player.getBounds())){
				if(attackable){
					if(player.health - 50 > 0){
						attacking = true;
						attackable = false;
						player.setHealth(-50);
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
		    
		
		    	if(getBounds().intersects(handler.getVisibleArea())){
		    		Point a = new Point((int)width,(int)height/2);
					Point b = new Point((int)(player.x - x),(int)(player.y - y));
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
	
	 public Polygon getPolygon(){
		   return RotateViktor.rotate(new Point((int)x,(int)y), new Point((int)x + width,(int)y), new Point((int)x+width,(int)y+height) ,
				   new Point((int)x,(int)y+height),angle,(int)(x+width/2), (int)(y+height/2));
	 }
	
	@Override
	public void die() {
		
		this.dead = true;
		this.live = false;
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
		
		return new Rectangle((int)x+8, (int)y+8, width-16, height-16);
	}

	@Override
	public Rectangle getCollisionArea() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
