package com.hb;

import com.hb.entity.Entity;

public class Camera {
	public double x;
	public double y;
	
	
	public void tick(Entity player){
		/*A tick metódus beállítja a kamera koordinátáit úgy,hogy a player mindíg a kép közepén legyen.*/
		setX(-player.getX() + Game.WIDTH/2);
		setY(-player.getY() + Game.HEIGHT/2);
	}
	
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public double getX() {
		return x;
	}
	public void setY(double y) {
		this.y = y;
	}
	
}
