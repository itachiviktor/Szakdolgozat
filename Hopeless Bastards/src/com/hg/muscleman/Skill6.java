package com.hg.muscleman;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.hb.Game;
import com.hb.Id;
import com.hb.entity.AbstractSkill;
import com.hb.entity.DamagingText;
import com.hb.entity.Entity;
import com.hb.entity.Player;
import com.hb.graphics.ImageAssets;

public class Skill6 extends AbstractSkill{
	/*9 bombát robbant a közvetlen környezetében*/
	
	public Rectangle[] bombs = new Rectangle[8];/*bombák locationja*/
	private Rectangle damagingArea;/*az a terület ami sebzõdik a skill álltal*/
	
	public Skill6(Player player) {
		super(player);
		this.cdtime = 60;
	}
	
	public void activateSkill(){
		if(this.skillStartedMainTime + this.cdtime < Game.maintime || skillStartedMainTime == 0){
			this.skillStartedMainTime = Game.maintime;
			
			/*Itt kiszámoltam,hogy melyik bombának mi a korrdinátája,hogy pont körbevegyék a playert.*/
			bombs[0] = new Rectangle((int)player.x -256, (int)player.y - 256, 192,192);
			bombs[1] = new Rectangle((int)player.x -256 + 192, (int)player.y - 256, 192,192);
			bombs[2] = new Rectangle((int)player.x -256 + 384, (int)player.y - 256, 192,192);
			bombs[3] = new Rectangle((int)player.x -256 + 384, (int)player.y - 256 + 192, 192,192);
			bombs[4] = new Rectangle((int)player.x -256 + 384, (int)player.y - 256 + 384, 192,192);
			bombs[5] = new Rectangle((int)player.x -256 + 192, (int)player.y - 256 + 384, 192,192);
			bombs[6] = new Rectangle((int)player.x -256 , (int)player.y - 256 + 384, 192,192);
			bombs[7] = new Rectangle((int)player.x -256, (int)player.y - 256 + 192, 192,192);
		
		
			this.animaionStillRuning = true;
			damagingArea = getBounds();
			
			isactivated = true;
			player.monitorScreenmanager.skill6useable = false;
			
		}
	}
	
	public void tick(){
		if(isactivated){
		
				for(int i=0;i<player.handler.entity.size();i++){
					Entity en = player.handler.entity.get(i);
					if(en.getId()!= Id.PLAYER && en.getDamagedArea().intersects(this.damagingArea)){
						en.setHealth(-1000);
						player.handler.damagetext.add(new DamagingText(en.x, en.y, String.valueOf(200),true, player.handler));
					}	
				}
		
				isactivated = false;
			
		}
		this.timeuntilcdend = this.skillStartedMainTime + this.cdtime - (Game.maintime);
		
		if(this.skillStartedMainTime + this.cdtime < Game.maintime || skillStartedMainTime == 0){
			player.monitorScreenmanager.skill6useable = true;
		}
	}

	public void render(Graphics g){
		
		if(animaionStillRuning){
			g.drawRect((int)player.x + player.width, (int)player.y + player.height, 192,192);
		    if(framePerSec < 3){
				if(frame<30){
					for(int i=0;i<bombs.length;i++){
						g.drawImage(ImageAssets.muscleulti[frame].getBufferedImage(),  bombs[i].x,bombs[i].y,bombs[i].width,bombs[i].height,null);
					}
				}
				framePerSec++;
			}else{
				framePerSec = 0;
				if(frame<29){
					
					for(int i=0;i<bombs.length;i++){
						g.drawImage(ImageAssets.muscleulti[frame].getBufferedImage(),  bombs[i].x,bombs[i].y,bombs[i].width,bombs[i].height,null);
					}
					frame++;
					
				}else{
					
					frame = 0;/*az animáció ujrajáttszása miatt ,ha újra eltoljuk a skillt,akkor ne legyen arrayoutofboundsixception
					hisz a osztályra mutató referencia nem törlõdik.*/
					framePerSec = 0;
					animaionStillRuning = false;
				}
			}
		}
	}

	@Override
	public Rectangle getBounds() {
		/*A damaging area az a playert körbevevõ nagy négyzet, az összes bomba együttese*/
		return new Rectangle((int)player.x - 256,(int)player.y - 256,576 ,576);
	}	
}
