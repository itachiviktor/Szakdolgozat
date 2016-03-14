package com.hg.muscleman;

import java.awt.Graphics;
import java.awt.Rectangle;
import com.hb.Game;
import com.hb.entity.AbstractSkill;
import com.hb.entity.Player;


public class Skill4 extends AbstractSkill{
	 /*A sima egygolyós lövés.Annyit csinál az osztály,hogy létrehoz egy golyót, az pedig már magától mozog
	  a Bullet osztály vezérli saját egyedeit.*/
		public Skill4(Player player) {
			super(player);
	
		    this.cdtime = 1;
		}
		
		@Override
		public void activateSkillByServer() {
			this.skillStartedMainTime = Game.maintime;
			
			this.animaionStillRuning = true;
			player.bullets.add(new Bullet(player.x+player.width+player.width/3,player.y+29, player.angle, 65, 6,2,player.handler,player));/*közép egykezes fegyóból*/
 			
			//player.monitorScreenmanager.skill4useable = false;
			player.skill4started = false;
			
		}

		@Override
		public void activateSkill() {
			if(this.skillStartedMainTime + this.cdtime < Game.maintime || skillStartedMainTime == 0){
				this.skillStartedMainTime = Game.maintime;
				
				this.animaionStillRuning = true;
				player.bullets.add(new Bullet(player.x+player.width+player.width/3,player.y+29, player.angle, 65, 6,2,player.handler,player));/*közép egykezes fegyóból*/
	 			
				player.monitorScreenmanager.skill4useable = false;
				player.skill4started = true;
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
				player.monitorScreenmanager.skill4useable = true;
			}
	   }
}
