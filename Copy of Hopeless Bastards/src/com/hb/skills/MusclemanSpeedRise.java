package com.hb.skills;

import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;

import com.hb.Game;
import com.hb.entity.AbstractSkill;
import com.hb.entity.Player;

public class MusclemanSpeedRise extends AbstractSkill{
	
	public MusclemanSpeedRise(Player player) {
		super(player);
		this.cdtime = 15;
		this.secWhileActive = 10;
	}
	
	@Override
	public void activateSkillByServer() {
		this.skillStartedMainTime = Game.maintime;
		
		this.animaionStillRuning = true;
		/*a skillnek az a l�nyege,hogy sebess�get n�velj�nk,�s ez azt takarja,hogy nagyobb t�vot tesz meg a player,�s
		 gyorsabban v�ltozik az anim�ci�k�pcsere ami azt jelenti,hogy gyorsabb mozg�st szimul�l a playernek.
		 A player alapb�l 4 framepersecet tol, ezt lecs�kkentve 2-re dupla olyan gyorsnak t�nik az 
		 anim�ci�s mozg�s.A player alapb�l 6 movementspeedet tol, ezt megn�velj�k 10-re*/
		player.framePerSecLimit = 2;
		player.movementSpeed=10;
		
		isactivated = true;
		//player.monitorScreenmanager.skill3useable = false;
		player.skill3started = false;
		
	}
	
	public void activateSkill(){
		if(this.skillStartedMainTime + this.cdtime < Game.maintime || skillStartedMainTime == 0){
			this.skillStartedMainTime = Game.maintime;
		
			this.animaionStillRuning = true;
			/*a skillnek az a l�nyege,hogy sebess�get n�velj�nk,�s ez azt takarja,hogy nagyobb t�vot tesz meg a player,�s
			 gyorsabban v�ltozik az anim�ci�k�pcsere ami azt jelenti,hogy gyorsabb mozg�st szimul�l a playernek.
			 A player alapb�l 4 framepersecet tol, ezt lecs�kkentve 2-re dupla olyan gyorsnak t�nik az 
			 anim�ci�s mozg�s.A player alapb�l 6 movementspeedet tol, ezt megn�velj�k 10-re*/
			player.framePerSecLimit = 2;
			player.movementSpeed=10;
			
			isactivated = true;
			player.monitorScreenmanager.skill3useable = false;
			player.skill3started = true;
			
		}
	}
	
	public void tick(){
		if(isactivated){
			if(Game.maintime - this.secWhileActive - skillStartedMainTime <= 0){
				
			}else{
				isactivated = false;
				/*alap�rtelmezett player tulajdons�gok vissza�ll�t�sa.*/
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
	
	@Override
	public Polygon getPolygon() {
		// TODO Auto-generated method stub
		return null;
	}
}
