package com.hb.graphics;

import java.awt.image.BufferedImage;

public class Sprite {
	/*Ez az oszt�ly egy r�sszelete a nagy k�pnek.A nagyk�p a SpriteSheet, �s ez egy Sheet bel�le.Azaz Van a nagyk�pen 10 kis figura
	 ,akkor ez egy kis figura k�p lesz.*/
	public SpriteSheet sheet;
	public BufferedImage image;
	
	public Sprite(SpriteSheet sheet,int x,int y,int width,int height) {
		image = sheet.getSprite(x, y,width,height);
	}
	/*A sprite is egy Buffered image egy�bk�nt csak nagyb�l kicsit konvert�l, de ennyi.*/
	public BufferedImage getBufferedImage(){
		return image;
	}
}
