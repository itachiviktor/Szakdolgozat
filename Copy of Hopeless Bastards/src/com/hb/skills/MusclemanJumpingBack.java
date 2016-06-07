package com.hb.skills;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

import com.hb.Game;
import com.hb.entity.AbstractSkill;
import com.hb.entity.Player;
import com.hb.graphics.ImageAssets;
import com.hb.math.RotateViktor;

public class MusclemanJumpingBack extends AbstractSkill{
	/*Muscleman f�stteleport�l�s:L�nyege,hogy egyik helyr�l a m�sikra teleport�lunk, �s mind2 helyen a r�gin is �s az
	 �ly helyen is megjelenik egy f�st anim�ci�.*/
	
	private int oldplayerx;/*a player eredeti koordin�t�ja,ahonnan elteleport�l l�nyeg�ben*/
	private int oldplayery;
	
	public MusclemanJumpingBack(Player player) {
		super(player);
		this.cdtime = 25;
	}
	
	@Override
	public void activateSkillByServer() {
		this.skillStartedMainTime = Game.maintime;/*skillkezd�s be�ll�t�s*/
		this.oldplayerx = (int)player.x;/*player alap koordin�t�j�nak be�ll�t�sa,ahonnan elteleport�l*/
		this.oldplayery = (int)player.y;
		this.animaionStillRuning = true;
		
		/*Itt kisz�moljuk azt a pontot,ami a player k�zep�t�l 500 pixellel m�g�tte van.
		 Az a l�nyeg,hogy alaphelyzetben a player jobboldalra n�z,teh�t a m�g�tte l�v� pontot �gy kapom meg,
		 hogy az x koordin�t�j�b�l kivonok 500-at.Majd ezt a pontot annyival forgatom a player k�z�ppontja k�r�l,
		 ah�ny fokkal el van fordulva a player, �s k�sz, megvan a pontosan m�g�tte l�v� pont.*/
		Point behing500px = RotateViktor.rotatePoint(new Point((int)player.x+player.width/2-300, (int)player.y+player.height/2), player.angle, (int)player.x+player.width/2,(int)player.y+player.height/2);
		
		/*Az �j hely koordin�t�inak meghat�roz�sa.*/
		player.x = behing500px.x - player.width/2;
		player.y = behing500px.y - player.height/2;
			
		//player.monitorScreenmanager.skill2useable = false;
		player.skill2started = false;
		
	}
	
	public void activateSkill(){
		/**/
		if(this.skillStartedMainTime + this.cdtime < Game.maintime || skillStartedMainTime == 0){
			this.skillStartedMainTime = Game.maintime;/*skillkezd�s be�ll�t�s*/
			this.oldplayerx = (int)player.x;/*player alap koordin�t�j�nak be�ll�t�sa,ahonnan elteleport�l*/
			this.oldplayery = (int)player.y;
			this.animaionStillRuning = true;
			
			/*Itt kisz�moljuk azt a pontot,ami a player k�zep�t�l 500 pixellel m�g�tte van.
			 Az a l�nyeg,hogy alaphelyzetben a player jobboldalra n�z,teh�t a m�g�tte l�v� pontot �gy kapom meg,
			 hogy az x koordin�t�j�b�l kivonok 500-at.Majd ezt a pontot annyival forgatom a player k�z�ppontja k�r�l,
			 ah�ny fokkal el van fordulva a player, �s k�sz, megvan a pontosan m�g�tte l�v� pont.*/
			Point behing500px = RotateViktor.rotatePoint(new Point((int)player.x+player.width/2-300, (int)player.y+player.height/2), player.angle, (int)player.x+player.width/2,(int)player.y+player.height/2);
			
			/*Az �j hely koordin�t�inak meghat�roz�sa.*/
			player.x = behing500px.x - player.width/2;
			player.y = behing500px.y - player.height/2;
			
			player.monitorScreenmanager.skill2useable = false;
			player.skill2started = true;
		}
	}
	
	public void tick(){
		/*a szok�sosak*/
		this.timeuntilcdend = this.skillStartedMainTime + this.cdtime - (Game.maintime);
		
		if(this.skillStartedMainTime + this.cdtime < Game.maintime || skillStartedMainTime == 0){
			player.monitorScreenmanager.skill2useable = true;
		}
	}
	
	public void render(Graphics g){
		if(animaionStillRuning ){
		    	 
		    	  /*A render met�dus 60- szor h�v�dik meg m�sodpercenk�nt.A boltframePerSec v�ltoz� az�rt lett l�rtehozva,
		    	   hogy azt �ll�tsam,hogy a 60-b�l h�nyszor v�ltson k�pet, hisz m�sodpercenk�nt 60-szor
		    	   k�pet cser�lni az anim�ci�ban az t�l gyors lenne, alig l�tn�nk a k�peket.
		    	   Ami�g a bolrFramePerSec kisebb mint 4, addig ugyan azt a k�pet rajzolja ki.*/
		    	  
		    	  
		    	  /*A boltframe v�ltoz� pedig azt a sz�mot t�rolja,hogy hanyadik k�pet kell kirajzolni.Ugyanis egy anim�ci�
		    	   t�bb k�pb�l �ll, ezen k�pek egy t�mbben vannak, �s a boltframe az egy t�mbindex, ami azt mutatja,hogy
		    	   hanyadik index� k�pet kell most kirajzolni.*/
		    	  
				g.setColor(Color.red);
				g.drawLine(this.oldplayerx, this.oldplayery,  (int)player.x,(int)player.y);
				g.setColor(Color.white);
			
		    	  if(framePerSec < 4){
						if(frame < 10){
							g.drawImage(ImageAssets.shooterSmoke[frame].getBufferedImage(), this.oldplayerx, this.oldplayery, 128,128,null);
							g.drawImage(ImageAssets.shooterSmoke[frame].getBufferedImage(), (int)player.x-32,(int)player.y-32, 128,128,null);
						}
						framePerSec++;
		    	  }else{
		    		  /*Ha a boltFramePerSec nagyobb mint 4,akkor azt jelenti,hogy a k�vetkez� k�pet kell kirajzolni az 
		    		   anim�ci�ban.Ez�rt megint null�ra megy a sz�ml�l�.*/
		    		  framePerSec = 0;
		    		  
		    		  /*Ha a kirajzolt k�p m�g nem az utols� anim�ci�s k�p, akkor n�velj�k az index�rt�ket,hisz nem lesz
		    		   ArrayOutIndexException.*/
		    		  if(frame<9){
							g.drawImage(ImageAssets.shooterSmoke[frame].getBufferedImage(),this.oldplayerx,this.oldplayery, 128,128,null);
							g.drawImage(ImageAssets.shooterSmoke[frame].getBufferedImage(),(int)player.x - 32,(int)player.y-32, 128,128,null);
							frame++;
							
						}else{
							/*Viszont ,ha m�r az utols� anim�ci�s k�pkocka vet�t�si ideje is lej�rt, akkor be�ll�tom az
							 animationStillRuning met�dust falsera, azaz a render met�dus belseje nem fog v�grejat�dni.
							 Illetve a k�pkocka renderel� v�ltoz�kat alap�rtelmezettre �ll�tom,hisz ha �jra kell renderelni
							 akkor mindent el�r�l kell kezdeni.*/
							animaionStillRuning = false;
							frame = 0;
							framePerSec = 0;
						}
		    	  }
		}
	}

	@Override
	public Rectangle getBounds() {
		
		return null;
	}
	
	@Override
	public Polygon getPolygon() {
		// TODO Auto-generated method stub
		return null;
	}
}
