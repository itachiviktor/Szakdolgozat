package com.hg.muscleman;

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
import com.hb.entity.AbstractSkill;
import com.hb.entity.Bolt;
import com.hb.entity.DamagingText;
import com.hb.entity.Entity;
import com.hb.gamestate.Handler;
import com.hb.graphics.ImageAssets;
import com.hb.inventory.Inventory;
import com.hb.managers.HUDmanager;
import com.hb.math.RotateViktor;
import com.hb.skills.FireBolt;

public class Muscleman extends Entity{
	/*Ez lesz majd egy Player lesz�rmazott, a Player oszt�lyt abstract oszt�lly� kell alak�tani.*/
	private boolean up,down,right,left;/*mozg�s�rt felel�s v�ltoz�k*/
	private boolean fire;/*Ez a v�ltoz� a sima l�v�st enged�lyezi a tick met�dusban,ezt eg�rsesem�nykor �ll�tjuk*/
	private boolean doublefire;/*Ez a v�ltoz� a dupla l�v�st enged�lyezi a tick met�dusban,ezt eg�rsesem�nykor �ll�tjuk*/
	
	private boolean onegunman = true;/*Melyik figura legyen kirajzolva, alap�rtelmezetten az egykezes fegy�s
	gy�ros ember*/
	private boolean twogunman = false;/*K�tkezes fegyveres fegy�s ember*/
	
	private boolean dead = false;/*halott-e a player*/
	private boolean live = true;/*�l-e m�g a player*/
	
	public ArrayList<Bullet> bullets = new ArrayList<Bullet>();/*A kil�tt goly�kat t�rol� lista*/
	public Bolt bolt = null;/*A vill�m t�mad�s objektum, alap�rtelmezetten null,majd konstruktorban p�ld�nyos�tom*/
	public FireBolt firebolt = null;/*Ugyan az a helyzet,mint a boltn�l*/
	
	public double angle;
	
	private int playerframe = 0;
	private int playerframePerSec = 0;
	
	public int framePerSecLimit = 4;
	
	public int maxHealth = 1000;
	
	public int mana = 450;
	public int maxMana = 500;
	
	/*Erre a kett�re csak egyenl�re van sz�ks�g, ugyan �gy nem lesz sz�ks�g r�juk mint a boltn�l sem,mert oszt�lyszinten el lesz
	 int�zve.*/
	private int fireboltframe = 0;
	private int fireboltframePerSec = 0;
	
	public double velocityX = 0;
    public double velocityY = 0;
    
    public int movementSpeed = 6;
    
    public Polygon polygon = null;
    public int px,py;
	
	public HUDmanager hudmanager;
	
	public AbstractSkill[] skills = new AbstractSkill[7];
	
	public Inventory inventory;
	
	
	public Muscleman(int x, int y, int width, int height, Id id, Handler handler) {
		super(x, y, width, height, id, handler);
		//hudmanager = new HUDmanager(this);
		angle = 0;
		this.health = 1000;
		polygon = getPolygon();
		px = polygon.xpoints[0];
		py = polygon.ypoints[0];
		
		
		//skills[0] = new Skill0(this);/*trap*/
		//skills[1] = new Skill1(this);/*buff*/
		//skills[2] = new Skill2(this);/*jump back*/
		//skills[3] = new Skill3(this);/*speed rise*/
		//skills[4] = new Skill4(this);/*oseshot*/
		//skills[5] = new Skill5(this);/*doubleshot*/
		//skills[6] = new Skill6(this);/*explosion*/
		
		inventory = new Inventory((int)this.x-500,(int)this.y, 6, handler);
		
		inventory.init();
		
	}
	
	 public void setAngle(int aa) {
	      angle = Math.toRadians(aa);
	 }
	 
	 public void moveForward(int sx, int sy) {
		 
		 double xold = x;
		 double yold = y;
		 
	      x += Math.cos(Math.toRadians(angle)) * sx;
	      y += Math.sin(Math.toRadians(angle)) * sy;
	      if(Collision.PlayerCBlock(
					new Point((int)x -1 , (int)y - 1),
					new Point((int)x + width + 1 , (int)y - 1),
					new Point((int)x -1 , (int)y + height - 1),
					new Point((int)x  + width -1 , (int)y + height - 1),handler)){
	    	  	x = xold;
				y = yold;
			}
	 
	      if(Collision.EntityCollisionEntity1(getDamagedArea(), this, handler)){
	    	  x = xold;
				y = yold;
	      }
	   }

	   public void moveBackword(int sx, int sy) {
		   double xold = x;
		   double yold = y;
		   
	      x -= Math.cos(Math.toRadians(angle)) * sx;
	      y -= Math.sin(Math.toRadians(angle)) * sy;
	      
	      if(Collision.PlayerCBlock(
					new Point((int)x -1 , (int)y - 1),
					new Point((int)x + width + 1 , (int)y - 1),
					new Point((int)x -1 , (int)y + height - 1),
					new Point((int)x  + width -1 , (int)y + height - 1),handler)){
	    	  	x = xold;
				y = yold;
				
	      }
	 
	      if(Collision.EntityCollisionEntity1(getDamagedArea(), this, handler)){
	    	  x = xold;
				y = yold;
	      }
	   }
	 
	   public double getAngle() {
	      return angle;
	   }
	   
	   public Rectangle getBounds() {
		      return new Rectangle((int) x, (int) y, width, height);
	   }
	
	@Override
	public void tick(){
		
		inventory.tick();
		
		 for(int i = 0; i < bullets.size(); i++) {
		       Bullet tmpB = bullets.get(i);

		       tmpB.moveForward(15);
		       tmpB.tick();
		 }
		if(up){
			moveForward(movementSpeed, movementSpeed);
		}
		if(down){
			moveBackword(movementSpeed-3,movementSpeed-3);
			
		}
		if(left){
			angle -=2;
			if (angle > 360) {
			       angle = 0;
			    } else if (angle < 0) {
			       angle = 360;

			    }
			
			polygon = getPolygon();
			px = polygon.xpoints[0];
			py = polygon.ypoints[0];
		}
		if(right){
			angle +=2;
			if (angle > 360) {
			       angle = 0;
			    } else if (angle < 0) {
			       angle = 360;
			    }
		}
		    
		/*if(firebolt != null){
		    firebolt.tick();
		}*/
		    
		for(int i=0;i<7;i++){
			skills[i].tick();
		}
		
		hudmanager.tick();
	}

	public void render(Graphics g){
		
		Graphics2D g2d = (Graphics2D) g;
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	          RenderingHints.VALUE_ANTIALIAS_ON);
	    
	    
	    AffineTransform old = g2d.getTransform();
	    g2d.drawString("" + Game.maintime,(int)x +30 ,(int)y - 100 );
	    
	  
	    
	    for(int i=0;i<7;i++){
	    	skills[i].render(g);
	    }
		   
	    if(live){
	    	/*if(skills[2].isactivated){
	    		skills[2].render(g2d);
	    	}*/
	    
	    	Point behing500px = RotateViktor.rotatePoint(new Point((int)x+width/2-300, (int)y+height/2), angle, (int)x+width/2,(int)y+height/2);
			g2d.drawLine((int)x+width/2,(int)y+height/2,behing500px.x,behing500px.y);
	    	g2d.drawPolygon(getPolygon());
		    g2d.rotate(Math.toRadians(angle), x + width/2,
		          y + height / 2);
		    
		    if(playerframePerSec < framePerSecLimit){
				if(playerframe<4){
					g.drawImage(ImageAssets.muscleman[playerframe].getBufferedImage(), (int)x, (int)y, 63,63,null);
				}
				playerframePerSec++;
			}else{
				playerframePerSec = 0;
				if(playerframe<3){
					g.drawImage(ImageAssets.muscleman[playerframe].getBufferedImage(), (int)x, (int)y, 63,63,null);
					playerframe++;
					
				}else{
					playerframe = 0;
					g.drawImage(ImageAssets.muscleman[playerframe].getBufferedImage(), (int)x,(int) y, 63,63,null);
				}
			}
		    
		   if(onegunman){
			   g.drawImage(ImageAssets.muscleman[5].getBufferedImage(), (int)x,(int) y, 63,63,null);
			   g.drawImage(ImageAssets.guns[6].getBufferedImage(), (int)x + width/3,(int) y, 63,63,null);
		   }else if(twogunman){
			   g.drawImage(ImageAssets.muscleman[6].getBufferedImage(), (int)x,(int) y, 63,63,null);
			   g.drawImage(ImageAssets.guns[12].getBufferedImage(), (int)x + width/3,(int) y, 63,63,null);
		   }
		   
		    g2d.setTransform(old);

		    // drawing the bullets
		    
		    for (int i = 0; i < bullets.size(); i++) {
		       Bullet tmpB = (Bullet) bullets.get(i);
		      
		    	   if(tmpB.startShoot){
		    		   
		    		   if(tmpB.pipe == 2){
		    			   /*pipe == 2 azaz k�z�pr�l j�n a goly�*/
		    			   g2d.rotate(Math.toRadians(this.angle), (int)this.x + this.width/2,
						 	          (int)this.y + this.height / 2);
				    	   if(tmpB.bulletframePerSec < 4){
								if(tmpB.bulletframe < 3){
									g.drawImage(ImageAssets.gunfiredlight[tmpB.bulletframe].getBufferedImage(), (int)x + width, (int)y, 64,64,null);
								}
								tmpB.bulletframePerSec++;
				    	  }else{
				    		  tmpB.bulletframePerSec = 0;
				    		  if(tmpB.bulletframe<2){
									g.drawImage(ImageAssets.gunfiredlight[tmpB.bulletframe].getBufferedImage(), (int)x + width, (int)y, 64,64,null);
									tmpB.bulletframe++;
									
								}else{
									tmpB.startShoot = false;
								}
				    	  }
				    	  g2d.setTransform(old);
		    		   }else if(tmpB.pipe == 1){
		    			   /*pipe == 1 azaz jobbr�l j�n a goly�*/
		    			   g2d.rotate(Math.toRadians(this.angle), (int)this.x + this.width/2,
						 	          (int)this.y + this.height / 2);
				    	   if(tmpB.bulletframePerSec < 4){
								if(tmpB.bulletframe < 3){
									g.drawImage(ImageAssets.gunfiredlight[tmpB.bulletframe].getBufferedImage(), (int)x + width-20, (int)y+5, 64,64,null);
								}
								tmpB.bulletframePerSec++;
				    	  }else{
				    		  tmpB.bulletframePerSec = 0;
				    		  if(tmpB.bulletframe<2){
									g.drawImage(ImageAssets.gunfiredlight[tmpB.bulletframe].getBufferedImage(), (int)x + width-20, (int)y+5, 64,64,null);
									tmpB.bulletframe++;
									
								}else{
									tmpB.startShoot = false;
								}
				    	  }
				    	  g2d.setTransform(old);
		    		   }else if(tmpB.pipe == 0){
		    			   /*pipe == 0 azaz ballr�l j�n a goly�*/
		    			   g2d.rotate(Math.toRadians(this.angle), (int)this.x + this.width/2,
						 	          (int)this.y + this.height / 2);
				    	   if(tmpB.bulletframePerSec < 4){
								if(tmpB.bulletframe < 3){
									g.drawImage(ImageAssets.gunfiredlight[tmpB.bulletframe].getBufferedImage(), (int)x + width-20, (int)y-13, 64,64,null);
								}
								tmpB.bulletframePerSec++;
				    	  }else{
				    		  tmpB.bulletframePerSec = 0;
				    		  if(tmpB.bulletframe<2){
									g.drawImage(ImageAssets.gunfiredlight[tmpB.bulletframe].getBufferedImage(), (int)x + width-20, (int)y-13, 64,64,null);
									tmpB.bulletframe++;
									
								}else{
									tmpB.startShoot = false;
								}
				    	  }
				    	  g2d.setTransform(old);
		    		   }
			    	  
			       }
		       
		      
		       
		       g2d.rotate(Math.toRadians(tmpB.getAngle()), tmpB.getX() + tmpB.getWidth()/2,
		 	          tmpB.getY() + tmpB.getHeight() / 2);
		       g2d.drawImage(ImageAssets.bullet,(int) tmpB.getX(), (int) tmpB.getY(), (int)tmpB.getWidth(),
		               (int)tmpB.getHeight(), null);
		       g2d.setTransform(old);
		      
		    }
		    // in case you have other things to rotate
		    g2d.setTransform(old);
		    
		   //bolt.render(g);
		   
		    
		    if(firebolt != null){
		    	g2d.drawPolygon(firebolt.getPolygon());
		    	  g2d.rotate(Math.toRadians(angle), x + width/2,
				          y + height / 2);
				  
		    	  if(fireboltframePerSec < 4){
						if(fireboltframe < 29){
							g.drawImage(ImageAssets.firebolt[fireboltframe], (int)x + width, (int)y-240, 512,512,null);
						}
						fireboltframePerSec++;
		    	  }else{
		    		  fireboltframePerSec = 0;
		    		  if(fireboltframe<28){
							g.drawImage(ImageAssets.firebolt[fireboltframe], (int)x + width, (int)y-240, 512,512,null);
							fireboltframe++;
							
						}else{
							firebolt = null;
							fireboltframe = 0;
							fireboltframePerSec = 0;
						}
		    	  }
		    	 
		    	g2d.setTransform(old);
		    }
	    }
	    
	    if(dead){
	    	 g2d.rotate(Math.toRadians(angle), x + width/2,
			          y + height / 2);
	    	 if(playerframePerSec < 11 && playerframePerSec > 8 ){
					if(playerframe<4){
						g.drawImage(ImageAssets.player[playerframe].getBufferedImage(), (int)x, (int)y, 63,63,null);
						
					}
					playerframePerSec++;
				}else{
					playerframePerSec = 9;
					if(playerframePerSec < 10 && playerframePerSec > 8){
						g.drawImage(ImageAssets.player[playerframe].getBufferedImage(), (int)x, (int)y, 63,63,null);
						playerframe++;
						
					}else{
						playerframe = 0;
						g.drawImage(ImageAssets.player[playerframe].getBufferedImage(), (int)x,(int) y, 63,63,null);
					}
				}
	    	   g2d.setTransform(old);
	    	   for (int i = 0; i < bullets.size(); i++) {
			       Bullet tmpB = (Bullet) bullets.get(i);
			                      //playing with bullet colors
			     
			       g2d.rotate(Math.toRadians(angle), tmpB.getX() + tmpB.getWidth()/2,
			 	          tmpB.getY() + tmpB.getHeight() / 2);
			       g2d.drawImage(ImageAssets.bullet,(int) tmpB.getX(), (int) tmpB.getY(), (int)tmpB.getWidth(),
			               (int)tmpB.getHeight(), null);
			       g2d.setTransform(old);
			    }
			    // in case you have other things to rotate
			    g2d.setTransform(old);
	    }
	    
		this.hudmanager.render(g);
		this.inventory.render(g);
		
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public void setHealth(int health) {
		this.health += health;
		if(this.health <= 0){
			die();
		}
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
	public void die() {
		this.dead = true;
		this.live = false;
	}
	
	 public Polygon getPolygon(){
		   return RotateViktor.rotate(new Point((int)x,(int)y), new Point((int)x + width,(int)y), new Point((int)x+width,(int)y+height) ,
				   new Point((int)x,(int)y+height),angle,(int)(x+width/2), (int)(y+height/2));
	 }
	 
	 @Override
	 public Rectangle getDamagedArea() {
		return new Rectangle((int)x+8, (int)y+8, width-16, height-16);
	 }
	 
	 @Override
		public Rectangle getCollisionArea() {
			
		 return new Rectangle((int)x+4, (int)y+4, width-8, height-8);
		}

	public void keyPressed(int key) {
		
		if(key == KeyEvent.VK_W){
			up = true;
		}
		else if(key == KeyEvent.VK_A){
			left = true;
		}
		else if(key == KeyEvent.VK_S){
			down = true;
		}
		else if(key == KeyEvent.VK_D){
			right = true;
		}else if(key == KeyEvent.VK_ESCAPE){
			System.exit(0);
		}else if(key == KeyEvent.VK_R){
			//bolt.activateSkill(x + width, y -118, angle, 512, 300);
		}else if(key == KeyEvent.VK_T){
			if(firebolt == null){
				//firebolt = new FireBolt(x + width, y -100, angle, 512, 150, handler, this);
			}
		}else if(key == KeyEvent.VK_F){
			this.skills[1].activateSkill();
		}else if(key == KeyEvent.VK_X){
			this.skills[2].activateSkill();
		}else if(key == KeyEvent.VK_Q){
			this.skills[3].activateSkill();
		}else if(key == KeyEvent.VK_H){
			this.skills[6].activateSkill();
		}else if(key == KeyEvent.VK_P){
			handler.damagetext.add(new DamagingText((float)x,(float) y, "120",true,handler));
		}else if(key == KeyEvent.VK_0){
			this.skills[0].activateSkill();
		}else if(key == KeyEvent.VK_O){
			inventory.toggle();
		}else if(key == KeyEvent.VK_M){
			//inventory.additem(new Item("healthpoti", 1, ImageAssets.box.getBufferedImage()),new Random().nextInt(24));
		}
	}

	public void keyReleased(int key) {
		
		if(key == KeyEvent.VK_W){
			up = false;
		}else if(key == KeyEvent.VK_A){
			left = false;
		}else if(key == KeyEvent.VK_S){
			down = false;
		}else if(key == KeyEvent.VK_D){
			right = false;
		}
	}
	
	public void MousePressed(MouseEvent e){
		if(e.getButton() == MouseEvent.BUTTON1){
			
			Point p = e.getLocationOnScreen();
			p.setLocation(p.x+handler.getVisibleArea().x, p.y+handler.getVisibleArea().y);
			
			
			fire = true;
		}else if(e.getButton() == MouseEvent.BUTTON3){
			doublefire = true;
		}
	}
	
	public void MouseReleased(int buttonkey){
		if(buttonkey == MouseEvent.BUTTON1){
			skills[4].activateSkill();
		    onegunman = true;
		    twogunman = false;
		}else if(buttonkey == MouseEvent.BUTTON3){
			skills[5].activateSkill();
			onegunman = false;
		    twogunman = true;
		}
	}
	
}

