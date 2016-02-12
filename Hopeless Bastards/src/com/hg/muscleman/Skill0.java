package com.hg.muscleman;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.hb.Game;
import com.hb.entity.AbstractSkill;
import com.hb.entity.Player;

public class Skill0 extends AbstractSkill{
	/*Muscleman skill0.Bombalerakás.*/

	public Skill0(Player player) {
		super(player);
		/*Az õsben definiált változónak értékadás.*/
		this.cdtime = 5;
	}

	/*Ez a metódus hívódik meg aktiváláskor.*/
	@Override
	public void activateSkill() {
		/*Csak akkor aktiválódik a bombalerakás skill, ha a jelenlegi játékidõ nagyobb,mint a skillkezdés és a skillcd idõ
		 összeadva, azaz ha lejárt a cd,csak akkor aktiválhatom újra a skillt.Illetve hs a skillstartedtime == 0, az azt jelenti
		 hogy mióta megy a játék,azaz inicializálva lett a skill, azóta még nem volt használva,tehát cd sem lehet rajta.*/
		if(this.skillStartedMainTime + this.cdtime < Game.maintime || skillStartedMainTime == 0){
			this.skillStartedMainTime = Game.maintime;/*A skillkezdési idõt beállítom a játék fõidejére*/
			isactivated = true;/*aktívnak tekintjük innentõl a skillt*/
			player.hudmanager.skill0useable = false;/*A skillbaron elszürkítjük a képet, ami a skillt képviseli*/
			player.handler.tile.add(new Bomb((int)player.x,(int) player.y, 32, 32, player.handler));/*Ugyebár ez a skill pályaelemet 
			használ, azaz letesz egy bombát arra a koordinátára, ahol a skill elnyomásának pillanatában állt a player.*/
		}
	}

	@Override
	public void tick() {
		/*A tick metódus kiszámolja,hogy hány másodperc van a cd lejárásáig,ez azért kell,hogy a hudmanager
		 kitudja írni visszaszámlálás formájában minden másopercben.*/
		
		
		this.timeuntilcdend = this.skillStartedMainTime + this.cdtime - (Game.maintime);
		/*Illetve mindíg megnézi,hogy a cd lejárt-e már,mertha igen ,akkor a hudmanagernek a változóját
		 truera kell állítani,hogy kivilágosítsa a skillképet.*/
		if(this.skillStartedMainTime + this.cdtime < Game.maintime || skillStartedMainTime == 0){
			player.hudmanager.skill0useable = true;/*Ezzel látható,hogy az a baj,hogy ha 10 percig nem használom a skillt,
			akkor 10 percig elérhetõ,és folymatosan mindíg truere állítja az értéket, ez pazarló lehet,javítani kellene
			valami propertychangelistenert tudnék elképzelni.*/
		}
		
	}

	@Override
	public void render(Graphics g) {
		/*A bomba renderelést maga a bomb objektum végzi*/
	}

	@Override
	public Rectangle getBounds() {
		return null;/*A bomba tudja a saját rectanglejét*/
	}
	
}
