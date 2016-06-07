package com.hb.skills;

import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import com.hb.Game;
import com.hb.entity.AbstractSkill;
import com.hb.entity.Player;

public class MageHealthSteal extends AbstractSkill{
	
	public MageHealthSteal(Player player) {
		super(player);
		this.cdtime = 2;
	}

	@Override
	public void activateSkill() {
		if((this.skillStartedMainTime + this.cdtime < Game.maintime || skillStartedMainTime == 0)){
			this.skillStartedMainTime = Game.maintime;
			
			
			if(player.selectedPlayer != null){
				player.selectedPlayer.setHealth(-100);
				player.setHealth(100);
			}
			
			player.monitorScreenmanager.skill1useable = false;
			player.skill1started = true;
		}	
		
	}

	@Override
	public void activateSkillByServer() {
		this.skillStartedMainTime = Game.maintime;
		
		
		if(player.selectedPlayer != null){
			player.selectedPlayer.setHealth(-100);
			player.setHealth(100);
		}
		
		//player.monitorScreenmanager.skill4useable = false;
		player.skill1started = false;
		
	}

	@Override
	public void tick() {
		
		
		/*A tick met�dus kisz�molja,hogy h�ny m�sodperc van a cd lej�r�s�ig,ez az�rt kell,hogy a MonitorScreenManager
		 kitudja �rni visszasz�ml�l�s form�j�ban minden m�sopercben.*/
		
		
		this.timeuntilcdend = this.skillStartedMainTime + this.cdtime - (Game.maintime);
		/*Illetve mind�g megn�zi,hogy a cd lej�rt-e m�r,mertha igen ,akkor a hudmanagernek a v�ltoz�j�t
		 truera kell �ll�tani,hogy kivil�gos�tsa a skillk�pet.*/
		if(this.skillStartedMainTime + this.cdtime < Game.maintime || skillStartedMainTime == 0){
			player.monitorScreenmanager.skill1useable = true;/*Ezzel l�that�,hogy az a baj,hogy ha 10 percig nem haszn�lom a skillt,
			akkor 10 percig el�rhet�,�s folymatosan mind�g truere �ll�tja az �rt�ket, ez pazarl� lehet,jav�tani kellene
			valami propertychangelistenert tudn�k elk�pzelni.*/
		}
		
	}

	@Override
	public void render(Graphics g) {
		
	}

	@Override
	public Polygon getPolygon() {
	
		return null;
	}

	@Override
	public Rectangle getBounds() {
		
		return null;
	}

}
