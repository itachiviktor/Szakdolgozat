package com.hb.math;

import java.awt.Point;
import java.awt.Polygon;

public class RotateViktor {
	
private static Point[] p;
private static Polygon poly;
private static Point rotatePointHelper = new Point();


/*Ez egy általam készített matematikai osztály,amely forgatási számolásokat lát el,statikus szolgálltatásként.*/
	
	public static Polygon rotate(Point p1,Point p2,Point p3,Point p4,double angle,int dx,int dy){
		/*Ez a metódus kap 4 pontot és egy kapott fokkal az utolsó két paraméter mentén elforgatja */
		
		p = new Point[4];
		p[0] = p1;
		p[1] = p2;
		p[2] = p3;
		p[3] = p4;
		
	     
	     for(int i=0;i<p.length;i++){
	   	   p[i].x-=dx;
	   	   p[i].y-=dy;
	      }
	      
	      for(int i=0;i<p.length;i++){
	   	   p[i].setLocation(p[i].getX() * Math.cos(Math.toRadians(angle)) - p[i].getY() * Math.sin(Math.toRadians(angle)),
	   			   p[i].getX() * Math.sin(Math.toRadians(angle)) + p[i].getY() * Math.cos(Math.toRadians(angle)));
	      }

	      for(int i=0;i<p.length;i++){
	   	   p[i].x+=dx;
	   	   p[i].y+=dy;
	      }
	      int[] x = {(int) p[0].getX(), (int) p[1].getX(), (int)p[2].getX(), (int) p[3].getX()};
		  int[] y = {(int) p[0].getY(), (int) p[1].getY(), (int)p[2].getY(), (int) p[3].getY()};
		  
	      poly = new Polygon(x, y, x.length);
	      return poly;
	}
	
	public static Point rotatePoint(Point p,double angle,int dx,int dy){
		/*a dx, dy az ,hogy mely pont körül forgatmo a pontot*/
		p.x-=dx;
		p.y-=dy;
		 p.setLocation(p.getX() * Math.cos(Math.toRadians(angle)) - p.getY() * Math.sin(Math.toRadians(angle)),
	   			   p.getX() * Math.sin(Math.toRadians(angle)) + p.getY() * Math.cos(Math.toRadians(angle)));
		 
		 p.x+=dx;
	   	 p.y+=dy;
		rotatePointHelper.setLocation(p.x,p.y);
		return rotatePointHelper;
		//return new Point(p.x,p.y);
	}
}
