package com.hb.managers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;




import com.hb.Game;
import com.hb.entity.Player;
import com.hb.graphics.ImageAssets;

public class MonitorScreenManager {
	/*Ez az oszt�ly a j�t�kon bel�l a fels� �s als� ter�letek�rt felel�s,hogy azok is mind�g a playerrel motogjanak.
	 Itt a healthbarra �s a skillbarra �rdemes gondolni.M�g az inventory nincs itt,de ide kell ,hogy ker�lj�n.*/
	
	private Player player;
	
	/*a player 7 skillk�p�nek hely�t meghat�roz� rectangle*/
	public Rectangle skill0;
	public Rectangle skill1;
	public Rectangle skill2;
	public Rectangle skill3;
	public Rectangle skill4;
	public Rectangle skill5;
	public Rectangle skill6;
	
	/*Alap�rtelmezetten minden skill haszn�lhat�ra �ll�tom,azaz vil�gosk�pen jelenik meg,nem haszn�ltskillk�pen.*/
	public boolean skill0useable = true;
	public boolean skill1useable = true;
	public boolean skill2useable = true;
	public boolean skill3useable = true;
	public boolean skill4useable = true;
	public boolean skill5useable = true;
	public boolean skill6useable = true;

	/*a cd m�sodperc bet�t�p�s�nak be�ll�t�sa*/
	private Font betutipus = new Font("Arial",Font.PLAIN,20);
	
	
	public MonitorScreenManager(Player player) {
		this.player = player;
		/*Itt be�ll�tom a skillk�pek helyzet�t, a rectangleben, a rectangle objektum fogja k�pviselni a hely�ket.
		 Ebbe az a j�,hogy minden k�perny�n ugyan oda teszi.*/
		skill0 = new Rectangle((int)player.x - Game.WIDTH/2 + (Game.WIDTH-1140)/2 + 267, (int)player.y - Game.HEIGHT/2 + Game.HEIGHT - 160 + 82, 52,52);
		skill1 = new Rectangle((int)player.x - Game.WIDTH/2 + (Game.WIDTH-1140)/2 + 326, (int)player.y - Game.HEIGHT/2 + Game.HEIGHT - 160 + 82, 52,52);
		skill2 = new Rectangle((int)player.x - Game.WIDTH/2 + (Game.WIDTH-1140)/2 + 385, (int)player.y - Game.HEIGHT/2 + Game.HEIGHT - 160 + 82, 52,52);
		skill3 = new Rectangle((int)player.x - Game.WIDTH/2 + (Game.WIDTH-1140)/2 + 445, (int)player.y - Game.HEIGHT/2 + Game.HEIGHT - 160 + 82, 52,52);
		skill4 = new Rectangle((int)player.x - Game.WIDTH/2 + (Game.WIDTH-1140)/2 + 506, (int)player.y - Game.HEIGHT/2 + Game.HEIGHT - 160 + 82, 52,52);
		skill5 = new Rectangle((int)player.x - Game.WIDTH/2 + (Game.WIDTH-1140)/2 + 565, (int)player.y - Game.HEIGHT/2 + Game.HEIGHT - 160 + 82, 52,52);
		skill6 = new Rectangle((int)player.x - Game.WIDTH/2 + (Game.WIDTH-1140)/2 + 626, (int)player.y - Game.HEIGHT/2 + Game.HEIGHT - 160 + 82, 52,52);
	
	}
	
	public void tick(){
		/*�jrasz�molja a skillk�pek helyzet�t,hisz a player poz�cu�j�val egy�tt kell haladnia neki is.*/
		skill0.setLocation((int)player.x - Game.WIDTH/2 + (Game.WIDTH-1140)/2 + 267, (int)player.y - Game.HEIGHT/2 + Game.HEIGHT - 160 + 82);
		skill1.setLocation((int)player.x - Game.WIDTH/2 + (Game.WIDTH-1140)/2 + 326, (int)player.y - Game.HEIGHT/2 + Game.HEIGHT - 160 + 82);
		skill2.setLocation((int)player.x - Game.WIDTH/2 + (Game.WIDTH-1140)/2 + 385, (int)player.y - Game.HEIGHT/2 + Game.HEIGHT - 160 + 82);
		skill3.setLocation((int)player.x - Game.WIDTH/2 + (Game.WIDTH-1140)/2 + 445, (int)player.y - Game.HEIGHT/2 + Game.HEIGHT - 160 + 82);
		skill4.setLocation((int)player.x - Game.WIDTH/2 + (Game.WIDTH-1140)/2 + 506, (int)player.y - Game.HEIGHT/2 + Game.HEIGHT - 160 + 82);
		skill5.setLocation((int)player.x - Game.WIDTH/2 + (Game.WIDTH-1140)/2 + 565, (int)player.y - Game.HEIGHT/2 + Game.HEIGHT - 160 + 82);
		skill6.setLocation((int)player.x - Game.WIDTH/2 + (Game.WIDTH-1140)/2 + 626, (int)player.y - Game.HEIGHT/2 + Game.HEIGHT - 160 + 82);
		
	}
	public void render(Graphics g){
		
		/*A cd timer bet�t�pusa �s sz�n�nek be�ll�t�sa*/
		g.setFont(betutipus);
		g.setColor(Color.white);
		
		/*healthbar �s skillbar kirajzol�sa*/
		g.drawImage(ImageAssets.healthbar,(int)player.x - Game.WIDTH/2,(int) player.y - Game.HEIGHT/2, 300,200,null);
		g.drawImage(ImageAssets.skillbar,(int)player.x - Game.WIDTH/2 + (Game.WIDTH-1140)/2, (int)player.y - Game.HEIGHT/2 + Game.HEIGHT - 160, 1140,160,null);
		
		
		/*Mindegyik skillre megn�zz�k,hogy haszn�lhat�-e, mert ha igen akkor csak szimpl�n kirajzoljuk.*/
		if(skill0useable){
			g.drawImage(ImageAssets.musclemanskillimages[0].getBufferedImage(),skill0.x,skill0.y,skill0.width,skill0.height,null);
		}else{
			/*Ha a skillen jelenleg cd van,akkor probl�m�sabb a kirajzol�s.Kirajzolunk a skillk�pet, majd fel� egy sz�rkitett
			 �tl�ttsz�s k�pet,majd fel� a feliratot*/
			g.drawImage(ImageAssets.musclemanskillimages[0].getBufferedImage(),skill0.x,skill0.y,skill0.width,skill0.height,null);
			g.drawImage(ImageAssets.skillwait,skill0.x,skill0.y,skill0.width,skill0.height,null);
			
			/*Ez itt az�rt fontos,hogy h�ny jegy� a sz�m,hogy �ll�thassuk,hogy mind�g a skillk�p k�zep�n legyen.*/
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
			
			
			/*Ez itt az�rt fontos,hogy h�ny jegy� a sz�m,hogy �ll�thassuk,hogy mind�g a skillk�p k�zep�n legyen.*/
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
			
			/*Ez itt az�rt fontos,hogy h�ny jegy� a sz�m,hogy �ll�thassuk,hogy mind�g a skillk�p k�zep�n legyen.*/
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
			
			/*Ez itt az�rt fontos,hogy h�ny jegy� a sz�m,hogy �ll�thassuk,hogy mind�g a skillk�p k�zep�n legyen.*/
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
			
			/*Ez itt az�rt fontos,hogy h�ny jegy� a sz�m,hogy �ll�thassuk,hogy mind�g a skillk�p k�zep�n legyen.*/
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
			
			/*Ez itt az�rt fontos,hogy h�ny jegy� a sz�m,hogy �ll�thassuk,hogy mind�g a skillk�p k�zep�n legyen.*/
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
			
			/*Ez itt az�rt fontos,hogy h�ny jegy� a sz�m,hogy �ll�thassuk,hogy mind�g a skillk�p k�zep�n legyen.*/
			if(player.skills[6].timeuntilcdend < 100 && player.skills[6].timeuntilcdend > 9){
				g.drawString("" + player.skills[6].timeuntilcdend,skill6.x+14 ,skill6.y+34);
			}else if(player.skills[6].timeuntilcdend < 10){
				g.drawString("" + player.skills[6].timeuntilcdend,skill6.x+20 ,skill6.y+34);
			}else{
				g.drawString("" + player.skills[6].timeuntilcdend,skill6.x+8 ,skill6.y+34);
			}
		}
	
		/*a helth �s manacs�k a player �rt�keihez val� mozhat�sa(kicxsiny�t��se)*/
		g.drawImage(ImageAssets.health,(int)(player.x - Game.WIDTH/2 + 120),(int) player.y - Game.HEIGHT/2 + 43, 151 * player.health/player.maxHealth,32,null);
		g.drawImage(ImageAssets.mana,(int)(player.x - Game.WIDTH/2 + 101),(int) player.y - Game.HEIGHT/2 + 88, 141*player.mana/player.maxMana,26,null);
		
	}
}