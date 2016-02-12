package com.hb.tile;

import java.awt.Graphics;
import com.hb.Id;
import com.hb.gamestate.Handler;
import com.hb.graphics.ImageAssets;


public class Nonsolid extends Tile{

	/*Ez az osztály olyan pályaelemeket tartalmaz, amik nem viszgálnak ütközést*/
	
	public Nonsolid(int x, int y, int width, int height, boolean solid, Id id,
			Handler handler) {
		super(x, y, width, height, solid, id, handler);

	}

	@Override
	public void render(Graphics g) {
		if(id == Id.BOX){
			g.drawImage(ImageAssets.box.getBufferedImage(),x,y,width,height,null);
		}else if(id == Id.EARTH){
			g.drawImage(ImageAssets.earth.getBufferedImage(),x,y,width,height,null);
		}
	}

	@Override
	public void tick() {

	}
	
}
