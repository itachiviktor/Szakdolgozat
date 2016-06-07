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
	/*Muscleman füstteleportálás:Lényege,hogy egyik helyrõl a másikra teleportálunk, és mind2 helyen a régin is és az
	 úly helyen is megjelenik egy füst animáció.*/
	
	private int oldplayerx;/*a player eredeti koordinátája,ahonnan elteleportál lényegében*/
	private int oldplayery;
	
	public MusclemanJumpingBack(Player player) {
		super(player);
		this.cdtime = 25;
	}
	
	@Override
	public void activateSkillByServer() {
		this.skillStartedMainTime = Game.maintime;/*skillkezdés beállítás*/
		this.oldplayerx = (int)player.x;/*player alap koordinátájának beállítása,ahonnan elteleportál*/
		this.oldplayery = (int)player.y;
		this.animaionStillRuning = true;
		
		/*Itt kiszámoljuk azt a pontot,ami a player közepétõl 500 pixellel mögötte van.
		 Az a lényeg,hogy alaphelyzetben a player jobboldalra néz,tehát a mögötte lévõ pontot úgy kapom meg,
		 hogy az x koordinátájából kivonok 500-at.Majd ezt a pontot annyival forgatom a player középpontja körül,
		 ahány fokkal el van fordulva a player, és kész, megvan a pontosan mögötte lévõ pont.*/
		Point behing500px = RotateViktor.rotatePoint(new Point((int)player.x+player.width/2-300, (int)player.y+player.height/2), player.angle, (int)player.x+player.width/2,(int)player.y+player.height/2);
		
		/*Az új hely koordinátáinak meghatározása.*/
		player.x = behing500px.x - player.width/2;
		player.y = behing500px.y - player.height/2;
			
		//player.monitorScreenmanager.skill2useable = false;
		player.skill2started = false;
		
	}
	
	public void activateSkill(){
		/**/
		if(this.skillStartedMainTime + this.cdtime < Game.maintime || skillStartedMainTime == 0){
			this.skillStartedMainTime = Game.maintime;/*skillkezdés beállítás*/
			this.oldplayerx = (int)player.x;/*player alap koordinátájának beállítása,ahonnan elteleportál*/
			this.oldplayery = (int)player.y;
			this.animaionStillRuning = true;
			
			/*Itt kiszámoljuk azt a pontot,ami a player közepétõl 500 pixellel mögötte van.
			 Az a lényeg,hogy alaphelyzetben a player jobboldalra néz,tehát a mögötte lévõ pontot úgy kapom meg,
			 hogy az x koordinátájából kivonok 500-at.Majd ezt a pontot annyival forgatom a player középpontja körül,
			 ahány fokkal el van fordulva a player, és kész, megvan a pontosan mögötte lévõ pont.*/
			Point behing500px = RotateViktor.rotatePoint(new Point((int)player.x+player.width/2-300, (int)player.y+player.height/2), player.angle, (int)player.x+player.width/2,(int)player.y+player.height/2);
			
			/*Az új hely koordinátáinak meghatározása.*/
			player.x = behing500px.x - player.width/2;
			player.y = behing500px.y - player.height/2;
			
			player.monitorScreenmanager.skill2useable = false;
			player.skill2started = true;
		}
	}
	
	public void tick(){
		/*a szokásosak*/
		this.timeuntilcdend = this.skillStartedMainTime + this.cdtime - (Game.maintime);
		
		if(this.skillStartedMainTime + this.cdtime < Game.maintime || skillStartedMainTime == 0){
			player.monitorScreenmanager.skill2useable = true;
		}
	}
	
	public void render(Graphics g){
		if(animaionStillRuning ){
		    	 
		    	  /*A render metódus 60- szor hívódik meg másodpercenként.A boltframePerSec változó azért lett lértehozva,
		    	   hogy azt állítsam,hogy a 60-ból hányszor váltson képet, hisz másodpercenként 60-szor
		    	   képet cserélni az animációban az túl gyors lenne, alig látnánk a képeket.
		    	   Amiíg a bolrFramePerSec kisebb mint 4, addig ugyan azt a képet rajzolja ki.*/
		    	  
		    	  
		    	  /*A boltframe változó pedig azt a számot tárolja,hogy hanyadik képet kell kirajzolni.Ugyanis egy animáció
		    	   több képbõl áll, ezen képek egy tömbben vannak, és a boltframe az egy tömbindex, ami azt mutatja,hogy
		    	   hanyadik indexü képet kell most kirajzolni.*/
		    	  
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
		    		  /*Ha a boltFramePerSec nagyobb mint 4,akkor azt jelenti,hogy a következõ képet kell kirajzolni az 
		    		   animációban.Ezért megint nullára megy a számláló.*/
		    		  framePerSec = 0;
		    		  
		    		  /*Ha a kirajzolt kép még nem az utolsó animációs kép, akkor növeljük az indexértéket,hisz nem lesz
		    		   ArrayOutIndexException.*/
		    		  if(frame<9){
							g.drawImage(ImageAssets.shooterSmoke[frame].getBufferedImage(),this.oldplayerx,this.oldplayery, 128,128,null);
							g.drawImage(ImageAssets.shooterSmoke[frame].getBufferedImage(),(int)player.x - 32,(int)player.y-32, 128,128,null);
							frame++;
							
						}else{
							/*Viszont ,ha már az utolsó animációs képkocka vetítési ideje is lejárt, akkor beállítom az
							 animationStillRuning metódust falsera, azaz a render metódus belseje nem fog végrejatódni.
							 Illetve a képkocka renderelõ változókat alapértelmezettre állítom,hisz ha újra kell renderelni
							 akkor mindent elõrõl kell kezdeni.*/
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
