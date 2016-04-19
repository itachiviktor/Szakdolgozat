package com.hg.muscleman;

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
	/*Muscleman bullet buffer*/

	private int angle = 0;/*A buffer folymatosan forog a player körül.*/
	
	private Graphics2D g2d;
	private AffineTransform old;
	
	public Skill1(Player player) {
		super(player);
		this.secWhileActive = 10;/*10 másodpercig aktív a buffer*/
		this.cdtime = 20;/*20 sec cd ban rajta*/
	}
	
	public void activateSkillByServer(){
		this.skillStartedMainTime = Game.maintime;
		isactivated = true;
		//player.monitorScreenmanager.skill1useable = false;
		player.skill1started = false;
		
	}
	
	public void activateSkill(){
		/*skillaktiválás*/
		if(this.skillStartedMainTime + this.cdtime < Game.maintime || skillStartedMainTime == 0){
			this.skillStartedMainTime = Game.maintime;
			isactivated = true;
			player.monitorScreenmanager.skill1useable = false;
			player.skill1started = true;
		}
	}
	
	@Override
	public void tick() {
		/*Ha aktiválva van a skill*/
		if(isactivated){
			/*Ha még tart a skill, azaz legfeljebb 10 másodperc telt el a skilleltolás óta,akkor hozzáadunk a szöghöz.*/
			if(Game.maintime - this.secWhileActive - skillStartedMainTime <= 0){
				if(angle == 360){
					angle = 0;
				}else{
					angle = angle+2;
				}
			}else{
				/*Ha már letelt a 10 sec,akkor az isactivated változó legyen hamis*/
				isactivated = false;
			}
		}
		/*A cd-bõl hátralévõ idõt itt is folymatosan számoljuk,pedig ez hiba,csak akkor kellene, ha tényleg szükséges.*/
		this.timeuntilcdend = this.skillStartedMainTime + this.cdtime - (Game.maintime);
		
		/*Illetve mindíg megnézi,hogy a cd lejárt-e már,mertha igen ,akkor a hudmanagernek a változóját
		 truera kell állítani,hogy kivilágosítsa a skillképet.*/
		if(this.skillStartedMainTime + this.cdtime < Game.maintime || skillStartedMainTime == 0){
			player.monitorScreenmanager.skill1useable = true;
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
		    g2d.rotate(Math.toRadians(angle), (int)player.x + player.width/2 ,(int)player.y + player.height/2);
		    /*Animáljuk a buffot*/
		    if(framePerSec < 5){
				if(frame<10){
					g.drawImage(ImageAssets.musclemanpoewrbuff[frame].getBufferedImage(),  (int)player.x - player.width/2, (int)player.y - player.height/2, 128,128,null);
				}
				framePerSec++;
			}else{
				framePerSec = 0;
				if(frame<10){
					g.drawImage(ImageAssets.musclemanpoewrbuff[frame].getBufferedImage(),  (int)player.x - player.width/2, (int)player.y - player.height/2, 128,128,null);
					frame++;
					
				}else{
					frame = 0;
					g.drawImage(ImageAssets.musclemanpoewrbuff[frame].getBufferedImage(),  (int)player.x - player.width/2, (int)player.y - player.height/2, 128,128,null);
				}
			}
		    g2d.setTransform(old);
		}
	}

	@Override
	public Rectangle getBounds() {
		/*Amelyik skill nullt ad vissza erre a felüldefiniálandó metódusra, annak nincs felülete.*/
		return null;
	}
}
