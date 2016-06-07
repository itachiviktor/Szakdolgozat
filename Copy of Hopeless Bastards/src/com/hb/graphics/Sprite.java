package com.hb.graphics;

import java.awt.image.BufferedImage;

public class Sprite {
	/*Ez az osztály egy résszelete a nagy képnek.A nagykép a SpriteSheet, és ez egy Sheet belõle.Azaz Van a nagyképen 10 kis figura
	 ,akkor ez egy kis figura kép lesz.*/
	public SpriteSheet sheet;
	public BufferedImage image;
	
	public Sprite(SpriteSheet sheet,int x,int y,int width,int height) {
		image = sheet.getSprite(x, y,width,height);
	}
	/*A sprite is egy Buffered image egyébként csak nagyból kicsit konvertál, de ennyi.*/
	public BufferedImage getBufferedImage(){
		return image;
	}
}
