package com.hb.skills;

import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;

import com.hb.Game;
import com.hb.entity.AbstractSkill;
import com.hb.entity.Player;

public class MusclemanDoubleShot extends AbstractSkill{
/*kétgolyós lövés*/
	
	public MusclemanDoubleShot(Player player) {
		super(player);

	    this.cdtime = 4;
	}
	
	@Override
	public void activateSkillByServer() {
		this.skillStartedMainTime = Game.maintime;
		
		this.animaionStillRuning = true;
		   player.bullets.add(new Bullet(player.x+player.width+10,player.y+42, player.angle, 65, 6,1,player.handler,player));/*jobboldali fegyóból*/
           player.bullets.add(new Bullet(player.x+player.width+10,player.y+21, player.angle, 65, 6,0,player.handler,player));/*baloldali fegyóból*/
			
		//player.monitorScreenmanager.skill5useable = false;
		player.skill5started = false;
		
	}

	@Override
	public void activateSkill() {
		if(this.skillStartedMainTime + this.cdtime < Game.maintime || skillStartedMainTime == 0){
			this.skillStartedMainTime = Game.maintime;
			
			this.animaionStillRuning = true;
			   player.bullets.add(new Bullet(player.x+player.width+10,player.y+42, player.angle, 65, 6,1,player.handler,player));/*jobboldali fegyóból*/
	           player.bullets.add(new Bullet(player.x+player.width+10,player.y+21, player.angle, 65, 6,0,player.handler,player));/*baloldali fegyóból*/
 			
			player.monitorScreenmanager.skill5useable = false;
			player.skill5started = true;
		}	
	}

	@Override
	public void render(Graphics g) {
		
	}
	
	public Rectangle getBounds(){
		return null;
	}
	
	@Override
	public Polygon getPolygon() {
		// TODO Auto-generated method stub
		return null;
	}

 
   public void tick(){
	   this.timeuntilcdend = this.skillStartedMainTime + this.cdtime - (Game.maintime);
		
		if(this.skillStartedMainTime + this.cdtime < Game.maintime || skillStartedMainTime == 0){
			player.monitorScreenmanager.skill5useable = true;
		}
   }
}
