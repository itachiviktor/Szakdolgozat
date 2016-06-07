package com.hb.mage;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

import com.hb.Game;
import com.hb.Id;
import com.hb.Sound;
import com.hb.entity.AbstractSkill;
import com.hb.entity.DamagingText;
import com.hb.entity.Entity;
import com.hb.entity.Player;
import com.hb.graphics.ImageAssets;
import com.hb.math.RotateViktor;
import com.hg.muscleman.Bomb;

public class Skill0 extends AbstractSkill{

	private Sound boltSong;/*a vill�ml�s wav hangfileja*/
	public int damageValue = 250;/*Ennyi �leter�t vesz le arr�l,akit �r a t�mad�s*/
	private int manacost = 100;/*Ennyi man�t vesz le a haszn�lata a t�mad�snak*/
	
	public Skill0(Player player) {
		super(player);
		this.cdtime = 6;
		boltSong = new Sound("/lightning.wav");
	}
	
	public void activateSkillByServer(){
		
	}
	
	public void activateSkill(){
		
		
	}
	
	public void tick(){
		
	}

	public void render(Graphics g){
		
		
	}

	@Override
	public Rectangle getBounds() {
		return null;
	}	
	
	
	
}
