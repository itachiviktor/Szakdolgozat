package com.hb.mage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

import org.json.JSONException;
import org.json.JSONObject;

import com.hb.Collision;
import com.hb.Game;
import com.hb.Id;
import com.hb.entity.Bolt;
import com.hb.entity.CharacterType;
import com.hb.entity.DamagingText;
import com.hb.entity.Player;
import com.hb.gamestate.Handler;
import com.hb.graphics.ImageAssets;
import com.hb.math.RotateViktor;
import com.hg.muscleman.Bullet;
import com.hg.muscleman.Skill0;
import com.hg.muscleman.Skill1;
import com.hg.muscleman.Skill2;
import com.hg.muscleman.Skill3;
import com.hg.muscleman.Skill4;
import com.hg.muscleman.Skill5;
import com.hg.muscleman.Skill6;

public class Mage extends Player{
	public boolean update = true;
	//private String hashKey = "1a2s3d4f";
	private Bolt bolt;
	private Graphics2D g2d;
	private Bullet tmpB;
	private AffineTransform old;
	
	private Rectangle damagedArea = new Rectangle();
	private Rectangle collisonArea = new Rectangle();
	private Rectangle boundsRectangle = new Rectangle();
	
	private Point collisionPointHelper1 = new Point();
	private Point collisionPointHelper2 = new Point();
	private Point collisionPointHelper3 = new Point();
	private Point collisionPointHelper4 = new Point();
	
	private Point locationOnScreen;
	
	private CharacterType characterType = CharacterType.MAGE;
	
	public Mage(double x, double y, int width, int height, Id id,String networkId, Handler handler) {
		super(x, y, width, height, id,networkId, handler);
		
		angle = 0;
		this.health = 2000;
		this.maxHealth = 2000;
		
		this.maxMana = 500;
		this.mana = 450;
		polygon = getPolygon();
		px = polygon.xpoints[0];
		py = polygon.ypoints[0];
		
		skills[0] = new com.hb.mage.Skill0(this);/*trap*/
		skills[1] = new com.hb.mage.Skill1(this);/*buff*/
		skills[2] = new com.hb.mage.Skill2(this);/*jump back*/
		skills[3] = new com.hb.mage.Skill3(this);/*speed rise*/
		skills[4] = new com.hb.mage.Skill4(this);/*oseshot*/
		skills[5] = new com.hb.mage.Skill5(this);/*doubleshot*/
		skills[6] = new com.hb.mage.Skill6(this);/*explosion*/
		
	}

	@Override
	public void setAngle(int angle) {
		angle = (int) Math.toRadians(angle);
		
	}

	@Override
	public void moveForward(int sx, int sy) {
		/*Elõre mozgást megvalósító metódus.Az sx , sy a sebesség mértéke.*/
		 double xold = x;
		 double yold = y;
		 
	      x += Math.cos(Math.toRadians(angle)) * sx;
	      y += Math.sin(Math.toRadians(angle)) * sy;
	      
	      /*Az oldx, oldy azért kell,hogy tudjuk a karakter eredeti helyét, majd cos,sin-vel, elõre mozgatjuk a
	       karaktert, azaz az elõremozgatott értékek lesznek az x,y-ban, majd ütközést vizsgálunk, azaz, ha az
	       elõre mozgatott karakter ütközik valamivel, akkor oda nem léphet, azaz az x,y -nak az oldx,oldy-t kell
	       értékül adni azaz vissza az eredeti helyére.A moveBackword is ezen az elven mûködik.Annyi
	       a különbség, hogy ott a speed a fele ennek az értéknek, hátrafele lasabban tud mozogni, mint elõre.*/
	      
	      /*Ezek segéd adattagok, hogy ne new Pointot tolhaj mindig, ami pazarló a memóriára nézve.*/
	      collisionPointHelper1.setLocation((int)x - 1 , (int)y - 1);
	      collisionPointHelper2.setLocation((int)x + width + 1 , (int)y - 1);
	      collisionPointHelper3.setLocation((int)x - 1 , (int)y + height - 1);
	      collisionPointHelper4.setLocation((int)x  + width - 1 , (int)y + height - 1);
	      
	      
	      if(Collision.PlayerCBlock(
					/*new Point((int)x - 1 , (int)y - 1),
					new Point((int)x + width + 1 , (int)y - 1),
					new Point((int)x - 1 , (int)y + height - 1),
					new Point((int)x  + width - 1 , (int)y + height - 1)*/
	    		  collisionPointHelper1,collisionPointHelper2,
	    		  collisionPointHelper3,collisionPointHelper4,handler)){
	    	  
	    	  	x = xold;
				y = yold;
			}
	 
	      if(Collision.EntityCollisionEntity1(getDamagedArea(), this, handler)){
	    	  x = xold;
	    	  y = yold;
	      }
	}

	@Override
	public void moveBackword(int sx, int sy) {
		 double xold = x;
		   double yold = y;
		   
	      x -= Math.cos(Math.toRadians(angle)) * sx;
	      y -= Math.sin(Math.toRadians(angle)) * sy;
	      
	      /*Ezek segéd adattagok, hogy ne new Pointot tolhaj mindig, ami pazarló a memóriára nézve.*/
	      collisionPointHelper1.setLocation((int)x - 1 , (int)y - 1);
	      collisionPointHelper2.setLocation((int)x + width + 1 , (int)y - 1);
	      collisionPointHelper3.setLocation((int)x - 1 , (int)y + height - 1);
	      collisionPointHelper4.setLocation((int)x  + width - 1 , (int)y + height - 1);
	      
	      if(Collision.PlayerCBlock(
					/*new Point((int)x -1 , (int)y - 1),
					new Point((int)x + width + 1 , (int)y - 1),
					new Point((int)x -1 , (int)y + height - 1),
					new Point((int)x  + width -1 , (int)y + height - 1)*/
	    		  collisionPointHelper1,collisionPointHelper2,
	    		  collisionPointHelper3,collisionPointHelper4,handler)){
	    	  	x = xold;
				y = yold;
	      }
	 
	      if(Collision.EntityCollisionEntity1(getDamagedArea(), this, handler)){
	    	  x = xold;
	    	  y = yold;
	      }
	}

	@Override
	public double getAngle() {
		return angle;
	}

	@Override
	public Rectangle getBounds() {
		this.boundsRectangle.x = (int) x;
		this.boundsRectangle.y = (int) y;
		this.boundsRectangle.width = width;
		this.boundsRectangle.height = height;
	  
		return this.boundsRectangle;
		//return new Rectangle((int) x, (int) y, width, height);
	}

	@Override
	public void tick() {
		if(this.id == Id.PLAYER){

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
			
			inventory.tick();
			monitorScreenmanager.tick();
		}
		
		    
		for(int i=0;i<7;i++){
			skills[i].tick();
		}
		
	}

	@Override
	public void render(Graphics g) {
		g2d = (Graphics2D) g;
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	          RenderingHints.VALUE_ANTIALIAS_ON);
	    
	    old = g2d.getTransform();
	    
	    for(int i=0;i<7;i++){
	    	skills[i].render(g);
	    }
		
	    if(live){
	 
	    	//Point behing500px = RotateViktor.rotatePoint(new Point((int)x+width/2-300, (int)y+height/2), angle, (int)x+width/2,(int)y+height/2);
			//g2d.drawLine((int)x+width/2,(int)y+height/2,behing500px.x,behing500px.y);
	    	//g2d.drawPolygon(getPolygon());
	    	g2d.setColor(Color.white);
	    	if(id != Id.PLAYER){
	    		g2d.drawRect((int)x, (int)y - 20, width, 10);
	    		double healthpercent = (double)this.health / (double)this.maxHealth;
	    		g2d.setColor(Color.green);
	    		g2d.fillRect((int)x + 1, (int)y - 19, (int)((double)width * healthpercent), 9);	
	    		if(this.username != null){
	    			/*Az ellenfelek nevét kiírjuk, a sajátunkat nem.*/
	    			g2d.drawString(this.username,(int)x +100 ,(int)y - 10);
	    		}	
	    	}
	    	
	    	g2d.drawString("" + this.health,(int)x +30 ,(int)y - 100 );
	    	
		    g2d.rotate(Math.toRadians(angle), x + width/2,
		          y + height / 2);
		    
		    if(playerframePerSec < framePerSecLimit){
				if(playerframe<3 && playerframe>0){
					g.drawImage(ImageAssets.mage[playerframe].getBufferedImage(), (int)x, (int)y, 63,63,null);
				}
				playerframePerSec++;
			}else{
				playerframePerSec = 0;
				if(playerframe == 1){
					g.drawImage(ImageAssets.mage[playerframe].getBufferedImage(), (int)x, (int)y, 63,63,null);
					playerframe++;
					
				}else{
					playerframe = 1;
					g.drawImage(ImageAssets.mage[playerframe].getBufferedImage(), (int)x,(int) y, 63,63,null);
				}
			}
		    
		  
		   
		    g2d.setTransform(old);
		    
		  // bolt.render(g);
	    }
	    
	    if(dead){
	    	if(this.id == Id.PLAYER){
	    		System.exit(0);
	    	}
	    	
			g2d.setTransform(old);
	    }
	    
	    if(this.id == Id.PLAYER){
	    	this.monitorScreenmanager.render(g);
			this.inventory.render(g);
			g2d.drawString("" + Game.maintime,(int)x +30 ,(int)y - 100 );
	    }
	    
	    if(this.id == Id.PLAYER){
	    	updateServer();
	    }
		
	}
	
	public void updateServer(){
		if(true){
			update = false;
			
			JSONObject data = new JSONObject();
			try{
					
					data.put("id",this.networkId);
					data.put("username", handler.gsm.username);
					data.put("characterType", "MAGE");
					data.put("x", this.x);
					data.put("y", this.y);
					data.put("angle",this.angle);
					data.put("health", this.health);
					data.put("mana", this.mana);
					data.put("onegunman", this.onegunman);
					data.put("twogunman", this.twogunman);
					data.put("dead", this.dead);
					data.put("live", this.live);
					data.put("maxhealth", this.maxHealth);
					data.put("maxmana", this.maxMana);
					data.put("skill0started", this.skill0started);
					data.put("skill1started", this.skill1started);
					data.put("skill2started", this.skill2started);
					data.put("skill3started", this.skill3started);
					data.put("skill4started", this.skill4started);
					data.put("skill5started", this.skill5started);
					data.put("skill6started", this.skill6started);
					//data.put("hash", MD5(hashKey + data.toString()));
					if(this.skill0started){
						this.skill0started = false;
					}else if(this.skill1started){
						this.skill1started = false;
					}else if(this.skill2started){
						this.skill2started = false;
					}else if(this.skill3started){
						this.skill3started = false;
					}else if(this.skill4started){
						this.skill4started = false;
					}else if(this.skill5started){
						this.skill5started = false;
					}else if(this.skill6started){
						this.skill6started = false;
					}
					
					handler.socket.emit("playerMoved",data);
						
			}catch(JSONException e){
				e.getMessage();
			}
		}else{
			update = true;
		}
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public void setX(double x) {
		this.x = x;// TODO Auto-generated method stub
		
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public void setY(double y) {
		this.y = y;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public void setWidth(int width) {
		this.width = width;
		
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
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

	@Override
	public boolean isDead() {
		return dead;
	}

	@Override
	public void setDead(boolean dead) {
		this.dead = dead;
		
	}

	@Override
	public boolean isLive() {
		return live;
	}

	@Override
	public void setLive(boolean live) {
		this.live = live;
		
	}

	@Override
	public void die() {
		this.dead = true;
		this.live = false;
		
	}

	@Override
	public Polygon getPolygon() {
		return RotateViktor.rotate(new Point((int)x,(int)y), new Point((int)x + width,(int)y), new Point((int)x+width,(int)y+height) ,
				   new Point((int)x,(int)y+height),angle,(int)(x+width/2), (int)(y+height/2));
	}

	@Override
	public Rectangle getDamagedArea() {
		damagedArea.x = (int)x+8;
		 damagedArea.y = (int)y+8;
		 damagedArea.width = width-16;
		 damagedArea.height = height-16;
		 
		 return this.damagedArea;
		//return new Rectangle((int)x+8, (int)y+8, width-16, height-16);
	}

	@Override
	public Rectangle getCollisionArea() {
		this.collisonArea.x = (int)x+4;
		this.collisonArea.y = (int)y+4;
		this.collisonArea.width = width-8;
		this.collisonArea.height = height-8;
		
		return this.collisonArea;
		//return new Rectangle((int)x+4, (int)y+4, width-8, height-8);
	}

	@Override
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
			bolt.activateSkill(x + width, y -118, angle, 512, 300);
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

	@Override
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

	@Override
	public void MousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1){
			
			locationOnScreen = e.getLocationOnScreen();
			locationOnScreen.setLocation(locationOnScreen.x+handler.getVisibleArea().x, locationOnScreen.y+handler.getVisibleArea().y);
			
			
			fire = true;
		}else if(e.getButton() == MouseEvent.BUTTON3){
			doublefire = true;
		}
		
	}

	@Override
	public void MouseReleased(int buttonkey) {
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
