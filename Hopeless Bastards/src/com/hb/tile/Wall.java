package com.hb.tile;

import java.awt.Graphics;
import com.hb.Id;
import com.hb.gamestate.Handler;
import com.hb.graphics.ImageAssets;


public class Wall extends Tile{
	/*Az elválasztófal*/
	
	WallType walltype;/*Milyen fal,szélsõ alsó stb.. ez csak a kirajzolásnál lényeg,funkciójuk ugyan az*/
	
	public Wall(int x, int y, int width, int height, boolean solid, Id id,WallType walltype,Handler handler) {
		super(x, y, width, height, solid, id, handler);
		this.walltype = walltype;
	}

	@Override
	public void render(Graphics g) {
		/*A típus alapján rendereli ki*/
		if(walltype == WallType.FELSO){
			g.drawImage(ImageAssets.wall[13].getBufferedImage(),x,y,width,height,null);
		}else if(walltype == WallType.ALSO){
			g.drawImage(ImageAssets.wall[13].getBufferedImage(),x,y,width,height,null);
		}else if(walltype == WallType.JOBBFELSO){
			g.drawImage(ImageAssets.wall[11].getBufferedImage(),x,y,width,height,null);
		}else if(walltype == WallType.JOBBALSO){
			g.drawImage(ImageAssets.wall[10].getBufferedImage(),x,y,width,height,null);
		}else if(walltype == WallType.BALALSO){
			g.drawImage(ImageAssets.wall[9].getBufferedImage(),x,y,width,height,null);
		}else if(walltype == WallType.BALFELSO){
			g.drawImage(ImageAssets.wall[12].getBufferedImage(),x,y,width,height,null);
		}else if(walltype == WallType.JOBB){
			g.drawImage(ImageAssets.wall[0].getBufferedImage(),x,y,width,height,null);
		}else if(walltype == WallType.BAL){
			g.drawImage(ImageAssets.wall[0].getBufferedImage(),x,y,width,height,null);
		}else if(walltype == WallType.FOLD){
			g.drawImage(ImageAssets.wall[14].getBufferedImage(),x,y,width,height,null);
		}else if(walltype == WallType.REPEDTFOLD){
			g.drawImage(ImageAssets.wall[15].getBufferedImage(),x,y,width,height,null);
		}else if(walltype == WallType.ALULROLKILEP){
			g.drawImage(ImageAssets.wall[7].getBufferedImage(),x,y,width,height,null);
		}else if(walltype == WallType.FENTROLKILEP){
			g.drawImage(ImageAssets.wall[8].getBufferedImage(),x,y,width,height,null);
		}else if(walltype == WallType.JOBBROLKILEP){
			g.drawImage(ImageAssets.wall[5].getBufferedImage(),x,y,width,height,null);
		}else if(walltype == WallType.BALROLKILEP){
			g.drawImage(ImageAssets.wall[6].getBufferedImage(),x,y,width,height,null);
		}else if(walltype == WallType.JOBBROLVEGE){
			g.drawImage(ImageAssets.wall[3].getBufferedImage(),x,y,width,height,null);
		}else if(walltype == WallType.LENTROLVEGE){
			g.drawImage(ImageAssets.wall[1].getBufferedImage(),x,y,width,height,null);
		}else if(walltype == WallType.FENTROLVEGE){
			g.drawImage(ImageAssets.wall[4].getBufferedImage(),x,y,width,height,null);
		}else if(walltype == WallType.BALLROLVEGE){
			g.drawImage(ImageAssets.wall[2].getBufferedImage(),x,y,width,height,null);
		}
	}

	@Override
	public void tick() {

	}

}