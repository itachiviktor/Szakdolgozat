package com.hg.muscleman;

import java.awt.Graphics;
import java.awt.Rectangle;
import com.hb.Game;
import com.hb.entity.AbstractSkill;
import com.hb.entity.Player;

public class Skill5 extends AbstractSkill{
	/*kétgolyós lövés*/
	
	public Skill5(Player player) {
		super(player);

	    this.cdtime = 4;
	}

	@Override
	public void activateSkill() {
		if(this.skillStartedMainTime + this.cdtime < Game.maintime || skillStartedMainTime == 0){
			this.skillStartedMainTime = Game.maintime;
			
			this.animaionStillRuning = true;
			   player.bullets.add(new Bullet(player.x+player.width+10,player.y+42, player.angle, 65, 6,1,player.handler,player));/*jobboldali fegyóból*/
	           player.bullets.add(new Bullet(player.x+player.width+10,player.y+21, player.angle, 65, 6,0,player.handler,player));/*baloldali fegyóból*/
 			
			player.hudmanager.skill5useable = false;
		}	
	}

	@Override
	public void render(Graphics g) {
		
	}
	
	public Rectangle getBounds(){
		return null;
	}

 
   public void tick(){
	   this.timeuntilcdend = this.skillStartedMainTime + this.cdtime - (Game.maintime);
		
		if(this.skillStartedMainTime + this.cdtime < Game.maintime || skillStartedMainTime == 0){
			player.hudmanager.skill5useable = true;
		}
   }

}
