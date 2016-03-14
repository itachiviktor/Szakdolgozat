package com.hb.entity;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class AbstractSkill {
	/*Ez az osztály arra szolgál,hogy definiáljon közös elemeket, ami minden skillben benne kell,hogy legyen.
	 Ez az osztály ezért is jó, mert a skill referencia változóknak ez lesz a statikus típusa,így a 7 skillt egy 7 elemû
	 AbstractSkill tömbben fogom tárolni.*/
	

	public Player player;/*Minden skill ismeri a playert,ami használja õt*/
	
	public int framePerSec = 0;/*A skill animációjához szükséges változók*/
	public int frame = 0;
	
	public int skillStartedMainTime = 0;/*Ezt nullára állítom,ez az az idõ, ami a skill kezdési ideje lesz*/
	
	public int timeuntilcdend;/*Ebbe az idõt tárolom másodpercben,ami még hátravan a cd lejárásáig,azaz amíg újra
	használhatok egy képességet*/
	
	public int cdtime;/*cd ideje másodpercben*/
	
	public int secWhileActive;/*Egy skill akár hosszabb ideig is aktív lehet pl.buff, de a lövés csak adott pillanatban
	aktív*/
	
	public boolean isactivated = false;/*aktiválva van-e a skill, ez azért fontos , mert a player objektumban
	minden skillre van egy objektum, és ez a változó jelzi,hogy a skill mûködik-e vagy sem.*/
	
	public boolean animaionStillRuning = false;/*Ez a változó amíg igaz, addig kell kirajzolni a képességhez tartozó animációt.*/
	
	
	public AbstractSkill(Player player) {
		this.player = player;
	}
	
	public abstract void activateSkill();/*Minden skillnek kell lennie egy aktiváló metódusnak*/
	
	public abstract void activateSkillByServer();
	
	public abstract void tick();
	
	public abstract void render(Graphics g);
	
	public abstract Rectangle getBounds();/*Minden skill vissza kell tudjon adni egy körülvevõ téglalapot,de pl van aminek nincs
	az egyszerûen nullt ad vissza, pl a gyors futásnak nincs animációja, sem területi sebzése, az csak null returnnel oldja meg.*/
}
