package com.hg.muscleman;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.hb.Game;
import com.hb.entity.AbstractSkill;
import com.hb.entity.Player;

public class Skill0 extends AbstractSkill{
	/*Muscleman skill0.Bombalerak�s.*/

	public Skill0(Player player) {
		super(player);
		/*Az �sben defini�lt v�ltoz�nak �rt�kad�s.*/
		this.cdtime = 5;
	}

	/*Ez a met�dus h�v�dik meg aktiv�l�skor.*/
	@Override
	public void activateSkill() {
		/*Csak akkor aktiv�l�dik a bombalerak�s skill, ha a jelenlegi j�t�kid� nagyobb,mint a skillkezd�s �s a skillcd id�
		 �sszeadva, azaz ha lej�rt a cd,csak akkor aktiv�lhatom �jra a skillt.Illetve hs a skillstartedtime == 0, az azt jelenti
		 hogy mi�ta megy a j�t�k,azaz inicializ�lva lett a skill, az�ta m�g nem volt haszn�lva,teh�t cd sem lehet rajta.*/
		if(this.skillStartedMainTime + this.cdtime < Game.maintime || skillStartedMainTime == 0){
			this.skillStartedMainTime = Game.maintime;/*A skillkezd�si id�t be�ll�tom a j�t�k f�idej�re*/
			isactivated = true;/*akt�vnak tekintj�k innent�l a skillt*/
			player.hudmanager.skill0useable = false;/*A skillbaron elsz�rk�tj�k a k�pet, ami a skillt k�pviseli*/
			player.handler.tile.add(new Bomb((int)player.x,(int) player.y, 32, 32, player.handler));/*Ugyeb�r ez a skill p�lyaelemet 
			haszn�l, azaz letesz egy bomb�t arra a koordin�t�ra, ahol a skill elnyom�s�nak pillanat�ban �llt a player.*/
		}
	}

	@Override
	public void tick() {
		/*A tick met�dus kisz�molja,hogy h�ny m�sodperc van a cd lej�r�s�ig,ez az�rt kell,hogy a hudmanager
		 kitudja �rni visszasz�ml�l�s form�j�ban minden m�sopercben.*/
		
		
		this.timeuntilcdend = this.skillStartedMainTime + this.cdtime - (Game.maintime);
		/*Illetve mind�g megn�zi,hogy a cd lej�rt-e m�r,mertha igen ,akkor a hudmanagernek a v�ltoz�j�t
		 truera kell �ll�tani,hogy kivil�gos�tsa a skillk�pet.*/
		if(this.skillStartedMainTime + this.cdtime < Game.maintime || skillStartedMainTime == 0){
			player.hudmanager.skill0useable = true;/*Ezzel l�that�,hogy az a baj,hogy ha 10 percig nem haszn�lom a skillt,
			akkor 10 percig el�rhet�,�s folymatosan mind�g truere �ll�tja az �rt�ket, ez pazarl� lehet,jav�tani kellene
			valami propertychangelistenert tudn�k elk�pzelni.*/
		}
		
	}

	@Override
	public void render(Graphics g) {
		/*A bomba renderel�st maga a bomb objektum v�gzi*/
	}

	@Override
	public Rectangle getBounds() {
		return null;/*A bomba tudja a saj�t rectanglej�t*/
	}
	
}
