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
					Entity en = player.handler.entity.get(i);
					if(en.id == Id.PLAYER){
						
						/*Ha az adott user playere a vizsg�lt elem, �s az nem az a karakter, aki a skillt
						 haszn�lta �s benne van a hat�k�rben , akkor sebz�dnie kell(k�s�bb bar�ts�gos
						 karakterekre ez nyilv�n nem fog hatni.)*/
						Player ene = (Player)player.handler.entity.get(i);

						if(!(ene.networkId.equals(player.networkId)) && ene.getDamagedArea().intersects(this.damagingArea)){
							ene.setHealth(-400);
							player.handler.damagetext.add(new DamagingText(ene.x, ene.y, String.valueOf(400),true, player.handler));
						}
					}else{
						Entity ene = player.handler.entity.get(i);
						if(ene.getDamagedArea().intersects(this.damagingArea)){
							ene.setHealth(-400);
							player.handler.damagetext.add(new DamagingText(ene.x, ene.y, String.valueOf(400),true, player.handler));
						}
					}
					
						
				}
				
				for(int i=0;i<player.handler.enemies.size();i++){
					Player en = player.handler.enemies.get(i);
					if(!(en.networkId.equals(player.networkId)) && en.getDamagedArea().intersects(this.damagingArea)){
						en.setHealth(-400);
						player.handler.damagetext.add(new DamagingText(en.x, en.y, String.valueOf(400),true, player.handler));
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
