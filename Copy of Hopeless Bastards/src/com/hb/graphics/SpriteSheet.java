package com.hb.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {
	/*Ez az oszt�ly egy nagy k�p,amin karakterek vannak.*/
	private BufferedImage sheet;
	
	public SpriteSheet(String path) {
		try {
			sheet = ImageIO.read(getClass().getResource(path));
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public BufferedImage getSprite(int x , int y,int width,int height){
		/*Ez a metd�dus a BufefredImager�l kivesz egy r�szk�pet, �s visszaadja BufferedImagek�nt,teh�t
		 nem kell 10 k�l�nb�z� k�p ahhoz,hogy 10 playert bet�ltsek, el�g egy k�pre r�rakni, �s ezzel a r�szk�p m�dszerrel 
		 kiv�laszthatom �ket.*/
		return sheet.getSubimage(x*width - width, y*height - height, width, height);
	}
}
