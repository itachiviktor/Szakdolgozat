package com.hb.mage;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

import com.hb.Game;
import com.hb.entity.AbstractSkill;
import com.hb.entity.Player;
import com.hb.graphics.ImageAssets;

public class Skill1 extends AbstractSkill{
	
	private Graphics2D g2d;
	private AffineTransform old;
	
	public Skill1(Player player) {
		super(player);
		this.cdtime = 10;
		this.secWhileActive = 10;
	}

	@Override
	public void activateSkill() {
		/*skillaktiválás*/
		if(this.skillStartedMainTime + this.cdtime < Game.maintime || skillStartedMainTime == 0){
			this.skillStartedMainTime = Game.maintime;
			isactivated = true;
			player.monitorScreenmanager.skill1useable = false;
			player.skill1started = true;
		}
		
	}

	@Override
	public void activateSkillByServer() {
		this.skillStartedMainTime = Game.maintime;
		isactivated = true;
		//player.monitorScreenmanager.skill1useable = false;
		player.skill1started = false;
		
	}

	@Override
	public void tick() {
		/*a szokásosak*/
		this.timeuntilcdend = this.skillStartedMainTime + this.cdtime - (Game.maintime);
		
		if(this.skillStartedMainTime + this.cdtime < Game.maintime || skillStartedMainTime == 0){
			player.monitorScreenmanager.skill2useable = true;
		}
		
	}

	@Override
	public void render(Graphics g) {
		/*Csak akkor rajzolja ki a buffot a player alá,ha acitválva van a skill, és még tart a 10 másodperc.*/
		if(Game.maintime - this.secWhileActive - skillStartedMainTime <= 0 && this.isactivated){
			
			g2d = (Graphics2D) g;
		    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		          RenderingHints.VALUE_ANTIALIAS_ON);
		    
		    old = g2d.getTransform();
		    /*a player közepe körül forgatunk*/
		    
		    /*Animáljuk a buffot*/
		    if(framePerSec < 5){
				if(frame<15){
					g.drawImage(ImageAssets.mageIceBlock[frame].getBufferedImage(),  (int)player.x - player.width/2, (int)player.y - player.height/2, 128,128,null);
				}
				framePerSec++;
			}else{
				framePerSec = 0;
				if(frame<15){
					g.drawImage(ImageAssets.mageIceBlock[frame].getBufferedImage(),  (int)player.x - player.width/2, (int)player.y - player.height/2, 128,128,null);
					frame++;
					
				}else{
					frame = 0;
					g.drawImage(ImageAssets.mageIceBlock[frame].getBufferedImage(),  (int)player.x - player.width/2, (int)player.y - player.height/2, 128,128,null);
				}
			}
		    g2d.setTransform(old);
		}
		
	}

	@Override
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return null;
	}
}
