package com.hb.tile;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;





import com.hb.Id;
import com.hb.gamestate.Handler;

public abstract class Tile {
	/*Ez is egy olyan abstract oszt�ly mint az Entity,hogy ebb�l sz�rmazzanak le,itt defini�lok k�z�s dolgokat.*/
	
	public int x;
	public int y;
	public int width;
	public int height;
	public boolean solid;/*�t lehet-e menni a testen vagy sem,azaz kell-e �tk�z�st vizsg�lni vele vagy sem.*/

	public Id id;/*az azonos�t�ja a fajon bel�l*/
	
	public Handler handler;
	
	private Rectangle boundTile;
	private Rectangle containCheckHelper;/*ez a referencia m�dosul, �s ezt adjuk vissza, nem mindig egy
	�j referenci�t hozunk l�tre a mem�ri�ba.*/
	
	public Tile(int x,int y,int width,int height,boolean solid,Id id,Handler handler) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.solid = solid;
		this.id = id;
		this.handler = handler;
		
		this.boundTile = new Rectangle();
		this.containCheckHelper = new Rectangle();
	}
	
	public abstract void render(Graphics g);
	
	public abstract void tick();
	
	public void die(){/*Elt�nhetnek a p�lyaelemek is*/
		handler.removeTile(this);
	}
	public boolean contains(Point point){/*Tartalmaz�s vizsg�lat, egy pontra*/
		//Rectangle rect = new Rectangle(x,y,width,height);
		
		containCheckHelper.x = x;
		containCheckHelper.y = y;
		containCheckHelper.width = width;
		containCheckHelper.height = height;
		
		return containCheckHelper.contains(point);
		//return rect.contains(point);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isSolid() {
		return solid;
	}

	public void setSolid(boolean solid) {
		this.solid = solid;
	}

	public Id getId(){
		return this.id;
	}
	
	public Rectangle getBounds(){
		boundTile.x = getX();
		boundTile.y = getY();
		boundTile.width = width;
		boundTile.height = height;
		return this.boundTile;
		//return new Rectangle(getX(),getY(),width,height);
	}	
}