package com.hb.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {
	/*Ez az osztály egy nagy kép,amin karakterek vannak.*/
	private BufferedImage sheet;
	
	public SpriteSheet(String path) {
		try {
			sheet = ImageIO.read(getClass().getResource(path));
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public BufferedImage getSprite(int x , int y,int width,int height){
		/*Ez a metdódus a BufefredImagerõl kivesz egy részképet, és visszaadja BufferedImageként,tehát
		 nem kell 10 különbözö kép ahhoz,hogy 10 playert betöltsek, elég egy képre rárakni, és ezzel a részkép módszerrel 
		 kiválaszthatom õket.*/
		return sheet.getSubimage(x*width - width, y*height - height, width, height);
	}
}
