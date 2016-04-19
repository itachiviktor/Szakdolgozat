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
	/*9 bomb�t robbant a k�zvetlen k�rnyezet�ben*/
	
	public Rectangle[] bombs = new Rectangle[8];/*bomb�k locationja*/
	private Rectangle damagingArea;/*az a ter�let ami sebz�dik a skill �lltal*/
	
	/*seg�dadattagok, a mem�ria tartal�kol�sa miatt*/
	private Player enemy;
	private Entity enem;
	private Player ene;
	private Entity en;
	
	public Skill6(Player player) {
		super(player);
		this.cdtime = 60;
	}
	
	public void activateSkillByServer(){
		this.skillStartedMainTime = Game.maintime;
		
		/*Itt kisz�moltam,hogy melyik bomb�nak mi a korrdin�t�ja,hogy pont k�rbevegy�k a playert.*/
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
		//player.monitorScreenmanager.skill6useable = false;
		player.skill6started = false;
	}
	
	public void activateSkill(){
		if(this.skillStartedMainTime + this.cdtime < Game.maintime || skillStartedMainTime == 0){
			this.skillStartedMainTime = Game.maintime;
			
			/*Itt kisz�moltam,hogy melyik bomb�nak mi a korrdin�t�ja,hogy pont k�rbevegy�k a playert.*/
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
			player.skill6started = true;
			
		}
	}
	
	public void tick(){
		if(isactivated){
		
			/*Ha aktiv�lva van a k�pess�g, akkor ugyeb�r ellen�rz�st kell v�gezn�nk(ez csak 1 adott pillanatban
			 sebez), hogy mely entit�sokat �rint a robban�s.*/
			
			/*El�sz�r az entit�sokat ellen�rizz�k v�gig.(Itt nem mozgo j�t�kososk(pl zombi), �s
			 az adott user karaktere tal�lhat�.)*/
				for(int i=0;i<player.handler.entity.size();i++){
					en = player.handler.entity.get(i);
					if(en.id == Id.PLAYER){
						
						ene = (Player)player.handler.entity.get(i);

						if(!(ene.networkId.equals(player.networkId)) && ene.getDamagedArea().intersects(this.damagingArea)){
							ene.setHealth(-400);
							player.handler.damagetext.add(new DamagingText(ene.x, ene.y, String.valueOf(400),true, player.handler));
						}
					}else{
						enem = player.handler.entity.get(i);
						if(enem.getDamagedArea().intersects(this.damagingArea)){
							enem.setHealth(-400);
							player.handler.damagetext.add(new DamagingText(enem.x, enem.y, String.valueOf(400),true, player.handler));
						}
					}
					
						
				}
				
				for(int i=0;i<player.handler.enemies.size();i++){
					enemy = player.handler.enemies.get(i);
					if(!(enemy.networkId.equals(player.networkId)) && enemy.getDamagedArea().intersects(this.damagingArea)){
						enemy.setHealth(-400);
						player.handler.damagetext.add(new DamagingText(enemy.x, enemy.y, String.valueOf(400),true, player.handler));
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
					
					frame = 0;/*az anim�ci� ujraj�ttsz�sa miatt ,ha �jra eltoljuk a skillt,akkor ne legyen arrayoutofboundsixception
					hisz a oszt�lyra mutat� referencia nem t�rl�dik.*/
					framePerSec = 0;
					animaionStillRuning = false;
				}
			}
		}
	}

	@Override
	public Rectangle getBounds() {
		/*A damaging area az a playert k�rbevev� nagy n�gyzet, az �sszes bomba egy�ttese*/
		return new Rectangle((int)player.x - 256,(int)player.y - 256,576 ,576);
	}	
}
