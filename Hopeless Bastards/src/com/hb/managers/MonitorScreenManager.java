package com.hb.managers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;




import com.hb.Game;
import com.hb.entity.Player;
import com.hb.graphics.ImageAssets;

public class MonitorScreenManager {
	/*Ez az osztály a játékon belül a felsõ és alsó területekért felelõs,hogy azok is mindíg a playerrel motogjanak.
	 Itt a healthbarra és a skillbarra érdemes gondolni.Még az inventory nincs itt,de ide kell ,hogy kerüljön.*/
	
	private Player player;
	
	/*a player 7 skillképének helyét meghatározó rectangle*/
	public Rectangle skill0;
	public Rectangle skill1;
	public Rectangle skill2;
	public Rectangle skill3;
	public Rectangle skill4;
	public Rectangle skill5;
	public Rectangle skill6;
	
	/*Alapértelmezetten minden skill használhatóra állítom,azaz világosképen jelenik meg,nem használtskillképen.*/
	public boolean skill0useable = true;
	public boolean skill1useable = true;
	public boolean skill2useable = true;
	public boolean skill3useable = true;
	public boolean skill4useable = true;
	public boolean skill5useable = true;
	public boolean skill6useable = true;

	/*a cd másodperc betûtípúsának beállítása*/
	private Font betutipus = new Font("Arial",Font.PLAIN,20);
	
	
	public MonitorScreenManager(Player player) {
		this.player = player;
		/*Itt beállítom a skillképek helyzetét, a rectangleben, a rectangle objektum fogja képviselni a helyüket.
		 Ebbe az a jó,hogy minden képernyõn ugyan oda teszi.*/
		skill0 = new Rectangle((int)player.x - Game.WIDTH/2 + (Game.WIDTH-1140)/2 + 267, (int)player.y - Game.HEIGHT/2 + Game.HEIGHT - 160 + 82, 52,52);
		skill1 = new Rectangle((int)player.x - Game.WIDTH/2 + (Game.WIDTH-1140)/2 + 326, (int)player.y - Game.HEIGHT/2 + Game.HEIGHT - 160 + 82, 52,52);
		skill2 = new Rectangle((int)player.x - Game.WIDTH/2 + (Game.WIDTH-1140)/2 + 385, (int)player.y - Game.HEIGHT/2 + Game.HEIGHT - 160 + 82, 52,52);
		skill3 = new Rectangle((int)player.x - Game.WIDTH/2 + (Game.WIDTH-1140)/2 + 445, (int)player.y - Game.HEIGHT/2 + Game.HEIGHT - 160 + 82, 52,52);
		skill4 = new Rectangle((int)player.x - Game.WIDTH/2 + (Game.WIDTH-1140)/2 + 506, (int)player.y - Game.HEIGHT/2 + Game.HEIGHT - 160 + 82, 52,52);
		skill5 = new Rectangle((int)player.x - Game.WIDTH/2 + (Game.WIDTH-1140)/2 + 565, (int)player.y - Game.HEIGHT/2 + Game.HEIGHT - 160 + 82, 52,52);
		skill6 = new Rectangle((int)player.x - Game.WIDTH/2 + (Game.WIDTH-1140)/2 + 626, (int)player.y - Game.HEIGHT/2 + Game.HEIGHT - 160 + 82, 52,52);
	
	}
	
	public void tick(){
		/*Újraszámolja a skillképek helyzetét,hisz a player pozícuójával együtt kell haladnia neki is.*/
		skill0.setLocation((int)player.x - Game.WIDTH/2 + (Game.WIDTH-1140)/2 + 267, (int)player.y - Game.HEIGHT/2 + Game.HEIGHT - 160 + 82);
		skill1.setLocation((int)player.x - Game.WIDTH/2 + (Game.WIDTH-1140)/2 + 326, (int)player.y - Game.HEIGHT/2 + Game.HEIGHT - 160 + 82);
		skill2.setLocation((int)player.x - Game.WIDTH/2 + (Game.WIDTH-1140)/2 + 385, (int)player.y - Game.HEIGHT/2 + Game.HEIGHT - 160 + 82);
		skill3.setLocation((int)player.x - Game.WIDTH/2 + (Game.WIDTH-1140)/2 + 445, (int)player.y - Game.HEIGHT/2 + Game.HEIGHT - 160 + 82);
		skill4.setLocation((int)player.x - Game.WIDTH/2 + (Game.WIDTH-1140)/2 + 506, (int)player.y - Game.HEIGHT/2 + Game.HEIGHT - 160 + 82);
		skill5.setLocation((int)player.x - Game.WIDTH/2 + (Game.WIDTH-1140)/2 + 565, (int)player.y - Game.HEIGHT/2 + Game.HEIGHT - 160 + 82);
		skill6.setLocation((int)player.x - Game.WIDTH/2 + (Game.WIDTH-1140)/2 + 626, (int)player.y - Game.HEIGHT/2 + Game.HEIGHT - 160 + 82);
		
	}
	public void render(Graphics g){
		
		/*A cd timer betûtípusa és színének beállítása*/
		g.setFont(betutipus);
		g.setColor(Color.white);
		
		/*healthbar és skillbar kirajzolása*/
		g.drawImage(ImageAssets.healthbar,(int)player.x - Game.WIDTH/2,(int) player.y - Game.HEIGHT/2, 300,200,null);
		g.drawImage(ImageAssets.skillbar,(int)player.x - Game.WIDTH/2 + (Game.WIDTH-1140)/2, (int)player.y - Game.HEIGHT/2 + Game.HEIGHT - 160, 1140,160,null);
		
		
		/*Mindegyik skillre megnézzük,hogy használható-e, mert ha igen akkor csak szimplán kirajzoljuk.*/
		if(skill0useable){
			g.drawImage(ImageAssets.musclemanskillimages[0].getBufferedImage(),skill0.x,skill0.y,skill0.width,skill0.height,null);
		}else{
			/*Ha a skillen jelenleg cd van,akkor problémásabb a kirajzolás.Kirajzolunk a skillképet, majd felé egy szürkitett
			 átláttszós képet,majd felé a feliratot*/
			g.drawImage(ImageAssets.musclemanskillimages[0].getBufferedImage(),skill0.x,skill0.y,skill0.width,skill0.height,null);
			g.drawImage(ImageAssets.skillwait,skill0.x,skill0.y,skill0.width,skill0.height,null);
			
			/*Ez itt azért fontos,hogy hány jegyû a szám,hogy állíthassuk,hogy mindíg a skillkép közepén legyen.*/
			if(player.skills[0].timeuntilcdend < 100 && player.skills[0].timeuntilcdend > 9){
				g.drawString("" + player.skills[0].timeuntilcdend,skill0.x+14 ,skill0.y+34);
			}else if(player.skills[0].timeuntilcdend < 10){
				g.drawString("" + player.skills[0].timeuntilcdend,skill0.x+20 ,skill0.y+34);
			}else{
				g.drawString("" + player.skills[0].timeuntilcdend,skill0.x+8 ,skill0.y+34);
			}
		}
		
		if(skill1useable){
			g.drawImage(ImageAssets.musclemanskillimages[1].getBufferedImage(),skill1.x,skill1.y,skill1.width,skill1.height,null);
		}else{
			g.drawImage(ImageAssets.musclemanskillimages[1].getBufferedImage(),skill1.x,skill1.y,skill1.width,skill1.height,null);
			g.drawImage(ImageAssets.skillwait,skill1.x,skill1.y,skill1.width,skill1.height,null);
			
			
			/*Ez itt azért fontos,hogy hány jegyû a szám,hogy állíthassuk,hogy mindíg a skillkép közepén legyen.*/
			if(player.skills[1].timeuntilcdend < 100 && player.skills[1].timeuntilcdend > 9){
				g.drawString("" + player.skills[1].timeuntilcdend,skill1.x+14 ,skill1.y+34);
			}else if(player.skills[1].timeuntilcdend < 10){
				g.drawString("" + player.skills[1].timeuntilcdend,skill1.x+20 ,skill1.y+34);
			}else{
				g.drawString("" + player.skills[1].timeuntilcdend,skill1.x+8 ,skill1.y+34);
			}
			
		}
		
		if(skill2useable){
			g.drawImage(ImageAssets.musclemanskillimages[2].getBufferedImage(),skill2.x,skill2.y,skill2.width,skill2.height,null);
		}else{
			g.drawImage(ImageAssets.musclemanskillimages[2].getBufferedImage(),skill2.x,skill2.y,skill2.width,skill2.height,null);
			g.drawImage(ImageAssets.skillwait,skill2.x,skill2.y,skill2.width,skill2.height,null);
			
			/*Ez itt azért fontos,hogy hány jegyû a szám,hogy állíthassuk,hogy mindíg a skillkép közepén legyen.*/
			if(player.skills[2].timeuntilcdend < 100 && player.skills[2].timeuntilcdend > 9){
				g.drawString("" + player.skills[2].timeuntilcdend,skill2.x+14 ,skill2.y+34);
			}else if(player.skills[2].timeuntilcdend < 10){
				g.drawString("" + player.skills[2].timeuntilcdend,skill2.x+20 ,skill2.y+34);
			}else{
				g.drawString("" + player.skills[2].timeuntilcdend,skill2.x+8 ,skill2.y+34);
			}
		}
		
		if(skill3useable){
			g.drawImage(ImageAssets.musclemanskillimages[3].getBufferedImage(),skill3.x,skill3.y,skill3.width,skill3.height,null);
		}else{
			g.drawImage(ImageAssets.musclemanskillimages[3].getBufferedImage(),skill3.x,skill3.y,skill3.width,skill3.height,null);
			g.drawImage(ImageAssets.skillwait,skill3.x,skill3.y,skill3.width,skill3.height,null);
			
			/*Ez itt azért fontos,hogy hány jegyû a szám,hogy állíthassuk,hogy mindíg a skillkép közepén legyen.*/
			if(player.skills[3].timeuntilcdend < 100 && player.skills[3].timeuntilcdend > 9){
				g.drawString("" + player.skills[3].timeuntilcdend,skill3.x+14 ,skill3.y+34);
			}else if(player.skills[3].timeuntilcdend < 10){
				g.drawString("" + player.skills[3].timeuntilcdend,skill3.x+20 ,skill3.y+34);
			}else{
				g.drawString("" + player.skills[3].timeuntilcdend,skill3.x+8 ,skill3.y+34);
			}
		}
		
		if(skill4useable){
			g.drawImage(ImageAssets.musclemanskillimages[4].getBufferedImage(),skill4.x,skill4.y,skill4.width,skill4.height,null);
		}else{
			g.drawImage(ImageAssets.musclemanskillimages[4].getBufferedImage(),skill4.x,skill4.y,skill4.width,skill4.height,null);
			g.drawImage(ImageAssets.skillwait,skill4.x,skill4.y,skill4.width,skill4.height,null);
			
			/*Ez itt azért fontos,hogy hány jegyû a szám,hogy állíthassuk,hogy mindíg a skillkép közepén legyen.*/
			if(player.skills[4].timeuntilcdend < 100 && player.skills[4].timeuntilcdend > 9){
				g.drawString("" + player.skills[4].timeuntilcdend,skill4.x+14 ,skill4.y+34);
			}else if(player.skills[4].timeuntilcdend < 10){
				g.drawString("" + player.skills[4].timeuntilcdend,skill4.x+20 ,skill4.y+34);
			}else{
				g.drawString("" + player.skills[4].timeuntilcdend,skill4.x+8 ,skill4.y+34);
			}
			
		}
		
		if(skill5useable){
			g.drawImage(ImageAssets.musclemanskillimages[5].getBufferedImage(),skill5.x,skill5.y,skill5.width,skill5.height,null);
		}else{
			g.drawImage(ImageAssets.musclemanskillimages[5].getBufferedImage(),skill5.x,skill5.y,skill5.width,skill5.height,null);
			g.drawImage(ImageAssets.skillwait,skill5.x,skill5.y,skill5.width,skill5.height,null);
			
			/*Ez itt azért fontos,hogy hány jegyû a szám,hogy állíthassuk,hogy mindíg a skillkép közepén legyen.*/
			if(player.skills[5].timeuntilcdend < 100 && player.skills[5].timeuntilcdend > 9){
				g.drawString("" + player.skills[5].timeuntilcdend,skill5.x+14 ,skill5.y+34);
			}else if(player.skills[5].timeuntilcdend < 10){
				g.drawString("" + player.skills[5].timeuntilcdend,skill5.x+20 ,skill5.y+34);
			}else{
				g.drawString("" + player.skills[5].timeuntilcdend,skill5.x+8 ,skill5.y+34);
			}
		}
		
		if(skill6useable){
			g.drawImage(ImageAssets.musclemanskillimages[6].getBufferedImage(),skill6.x,skill6.y,skill6.width,skill6.height,null);
		}else{
			g.drawImage(ImageAssets.musclemanskillimages[6].getBufferedImage(),skill6.x,skill6.y,skill6.width,skill6.height,null);
			g.drawImage(ImageAssets.skillwait,skill6.x,skill6.y,skill6.width,skill6.height,null);
			
			/*Ez itt azért fontos,hogy hány jegyû a szám,hogy állíthassuk,hogy mindíg a skillkép közepén legyen.*/
			if(player.skills[6].timeuntilcdend < 100 && player.skills[6].timeuntilcdend > 9){
				g.drawString("" + player.skills[6].timeuntilcdend,skill6.x+14 ,skill6.y+34);
			}else if(player.skills[6].timeuntilcdend < 10){
				g.drawString("" + player.skills[6].timeuntilcdend,skill6.x+20 ,skill6.y+34);
			}else{
				g.drawString("" + player.skills[6].timeuntilcdend,skill6.x+8 ,skill6.y+34);
			}
		}
	
		/*a helth és manacsík a player értékeihez való mozhatása(kicxsinyítáése)*/
		g.drawImage(ImageAssets.health,(int)(player.x - Game.WIDTH/2 + 120),(int) player.y - Game.HEIGHT/2 + 43, 151 * player.health/player.maxHealth,32,null);
		g.drawImage(ImageAssets.mana,(int)(player.x - Game.WIDTH/2 + 101),(int) player.y - Game.HEIGHT/2 + 88, 141*player.mana/player.maxMana,26,null);
		
	}
}