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

	private Sound boltSong;/*a villámlás wav hangfileja*/
	public int damageValue = 250;/*Ennyi életerõt vesz le arról,akit ér a támadás*/
	private int manacost = 100;/*Ennyi manát vesz le a használata a támadásnak*/
	
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
