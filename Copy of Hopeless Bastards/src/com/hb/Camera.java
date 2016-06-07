package com.hb;

import com.hb.entity.Entity;

public class Camera {
	public double x;
	public double y;
	
	
	public void tick(Entity player){
		/*A tick met�dus be�ll�tja a kamera koordin�t�it �gy,hogy a player mind�g a k�p k�zep�n legyen.*/
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
