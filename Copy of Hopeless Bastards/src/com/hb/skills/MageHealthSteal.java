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
		
		
		/*A tick metódus kiszámolja,hogy hány másodperc van a cd lejárásáig,ez azért kell,hogy a MonitorScreenManager
		 kitudja írni visszaszámlálás formájában minden másopercben.*/
		
		
		this.timeuntilcdend = this.skillStartedMainTime + this.cdtime - (Game.maintime);
		/*Illetve mindíg megnézi,hogy a cd lejárt-e már,mertha igen ,akkor a hudmanagernek a változóját
		 truera kell állítani,hogy kivilágosítsa a skillképet.*/
		if(this.skillStartedMainTime + this.cdtime < Game.maintime || skillStartedMainTime == 0){
			player.monitorScreenmanager.skill1useable = true;/*Ezzel látható,hogy az a baj,hogy ha 10 percig nem használom a skillt,
			akkor 10 percig elérhetõ,és folymatosan mindíg truere állítja az értéket, ez pazarló lehet,javítani kellene
			valami propertychangelistenert tudnék elképzelni.*/
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
