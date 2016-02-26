package com.hg.muscleman;

import java.awt.Graphics;
import java.awt.Rectangle;
import com.hb.Game;
import com.hb.entity.AbstractSkill;
import com.hb.entity.Player;


public class Skill3 extends AbstractSkill{
	/*Muscleman movement speed faster(sebességnövelés)*/
	
	public Skill3(Player player) {
		super(player);
		this.cdtime = 15;
		this.secWhileActive = 10;
	}
	
	public void activateSkill(){
		if(this.skillStartedMainTime + this.cdtime < Game.maintime || skillStartedMainTime == 0){
			this.skillStartedMainTime = Game.maintime;
		
			this.animaionStillRuning = true;
			/*a skillnek az a lényege,hogy sebességet növeljünk,és ez azt takarja,hogy nagyobb távot tesz meg a player,és
			 gyorsabban változik az animációképcsere ami azt jelenti,hogy gyorsabb mozgást szimulál a playernek.
			 A player alapból 4 framepersecet tol, ezt lecsökkentve 2-re dupla olyan gyorsnak tûnik az 
			 animációs mozgás.A player alapból 6 movementspeedet tol, ezt megnöveljük 10-re*/
			player.framePerSecLimit = 2;
			player.movementSpeed=10;
			
			isactivated = true;
			player.monitorScreenmanager.skill3useable = false;
			
		}
	}
	
	public void tick(){
		if(isactivated){
			if(Game.maintime - this.secWhileActive - skillStartedMainTime <= 0){
				
			}else{
				isactivated = false;
				/*alapértelmezett player tulajdonságok visszaállítása.*/
				player.framePerSecLimit = 4;
				player.movementSpeed=6;
			}
		}
		
		this.timeuntilcdend = this.skillStartedMainTime + this.cdtime - (Game.maintime);
		
		if(this.skillStartedMainTime + this.cdtime < Game.maintime || skillStartedMainTime == 0){
			player.monitorScreenmanager.skill3useable = true;
		}
	}

	public void render(Graphics g){
		
	}

	@Override
	public Rectangle getBounds() {
		return null;
	}

}
