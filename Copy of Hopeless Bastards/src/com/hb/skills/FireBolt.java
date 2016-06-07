package com.hb.skills;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import com.hb.entity.Player;
import com.hb.gamestate.Handler;
import com.hb.math.RotateViktor;

public class FireBolt {
	private double x, y, a; // x,y and angle
	   private int w, h; //width and height 
	   private Handler handler;
	   private Player player;
	   private int damageValue = 500;
	 
	   private boolean damagedYet = false;
	   
	   public FireBolt(double x, double y, double a, int w, int h,Handler handler,Player player) {
		   this.player = player;
		      this.x = x;
		      this.y = y;
		      this.a = a;
		      this.w = w;
		      this.h = h;
		      this.handler = handler;
	   }
	   
	   public double getX() {
		      return x;
		   }

		   public double getY() {
		      return y;
		   }

		   public double getA() {
		      return a;
		   }

		   public int getW() {
		      return w;
		   }

		   public int getH() {
		      return h;
		   }

		   // setting the values
		   public void setA(double aa) {

		      this.a = aa;
		   }

		   public void setX(double x) {
		      this.x = x;
		   }

		   public void setY(double y) {
		      this.y = y;
		   }
		   public void setW(int w) {
		      this.w = w;
		   }

		   public void setH(int h) {
		      this.h = h;
		   }

		   public Rectangle getBounds(){
				return new Rectangle((int)x,(int)y,(int)w,(int)h);
		   }
		   
		   public Polygon getPolygon(){
			   return RotateViktor.rotate(new Point((int)x,(int)y), new Point((int)x + w,(int)y), new Point((int)x+w,(int)y+h) ,
					   new Point((int)x,(int)y+h),a,(int)(player.x+player.width/2), (int)(player.y+player.height/2));
		   }
		   public void tick(){
			   if(!damagedYet){
				   for(int i=0;i<handler.entity.size();i++){
					   if(handler.entity.get(i).getClass() != Player.class && getPolygon().intersects(handler.entity.get(i).getBounds())){
						   handler.entity.get(i).setHealth(-this.damageValue);
						
					   }
				   }
				   damagedYet = true;
			   }
			   
		   }
		   
}
