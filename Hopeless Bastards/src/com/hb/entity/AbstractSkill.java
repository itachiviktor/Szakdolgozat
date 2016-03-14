package com.hb.entity;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class AbstractSkill {
	/*Ez az oszt�ly arra szolg�l,hogy defini�ljon k�z�s elemeket, ami minden skillben benne kell,hogy legyen.
	 Ez az oszt�ly ez�rt is j�, mert a skill referencia v�ltoz�knak ez lesz a statikus t�pusa,�gy a 7 skillt egy 7 elem�
	 AbstractSkill t�mbben fogom t�rolni.*/
	

	public Player player;/*Minden skill ismeri a playert,ami haszn�lja �t*/
	
	public int framePerSec = 0;/*A skill anim�ci�j�hoz sz�ks�ges v�ltoz�k*/
	public int frame = 0;
	
	public int skillStartedMainTime = 0;/*Ezt null�ra �ll�tom,ez az az id�, ami a skill kezd�si ideje lesz*/
	
	public int timeuntilcdend;/*Ebbe az id�t t�rolom m�sodpercben,ami m�g h�travan a cd lej�r�s�ig,azaz am�g �jra
	haszn�lhatok egy k�pess�get*/
	
	public int cdtime;/*cd ideje m�sodpercben*/
	
	public int secWhileActive;/*Egy skill ak�r hosszabb ideig is akt�v lehet pl.buff, de a l�v�s csak adott pillanatban
	akt�v*/
	
	public boolean isactivated = false;/*aktiv�lva van-e a skill, ez az�rt fontos , mert a player objektumban
	minden skillre van egy objektum, �s ez a v�ltoz� jelzi,hogy a skill m�k�dik-e vagy sem.*/
	
	public boolean animaionStillRuning = false;/*Ez a v�ltoz� am�g igaz, addig kell kirajzolni a k�pess�ghez tartoz� anim�ci�t.*/
	
	
	public AbstractSkill(Player player) {
		this.player = player;
	}
	
	public abstract void activateSkill();/*Minden skillnek kell lennie egy aktiv�l� met�dusnak*/
	
	public abstract void activateSkillByServer();
	
	public abstract void tick();
	
	public abstract void render(Graphics g);
	
	public abstract Rectangle getBounds();/*Minden skill vissza kell tudjon adni egy k�r�lvev� t�glalapot,de pl van aminek nincs
	az egyszer�en nullt ad vissza, pl a gyors fut�snak nincs anim�ci�ja, sem ter�leti sebz�se, az csak null returnnel oldja meg.*/
}
