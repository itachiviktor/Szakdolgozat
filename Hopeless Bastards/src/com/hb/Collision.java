package com.hb;



import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

import com.hb.entity.Entity;
import com.hb.gamestate.Handler;
import com.hb.tile.Tile;


public class Collision {
	/*Ez az osztály statikus metódusokat tartalmaz,azaz szolgáltatásokat,amik arra jók,hogy entitások ütközését lehet velük vizsgálni.*/
	
	/*Ez a két változó azért kell,hogy ne kelljen mindíg új referenciaváltozót létrehozni a ciklusokban,hanem etek kapnak
	 mindíg új értéket.Ez memóriaspórolás.*/
	private static Entity entity;
	private static Tile tile;

	public static boolean PlayerCBlock(Point p1,Point p2,Point p3,Point p4,Handler handler){
		for(int i=0;i<handler.tile.size();i++){
			tile = handler.tile.get(i);
			if(tile.solid && (handler.getVisibleArea() != null && handler.getVisibleArea().intersects(tile.getBounds())) ){
				if(tile.contains(p1) || tile.contains(p2) || tile.contains(p3) || tile.contains(p4)){
					return true;	
				}
			}
		}
		
		return false;
	}
	
	public static boolean EntityCollisionEntity(Point p1,Point p2,Point p3,Point p4,Entity mover,Handler handler){
		for(int i=0;i<handler.entity.size();i++){
			entity = handler.entity.get(i);
			if(mover != entity && (entity.contains(p1) || entity.contains(p2) || entity.contains(p3) || entity.contains(p4))){
				if(entity.getId() == Id.PLAYER){
					mover.collideplayer = true;
				}
				return true;
			}
		}
		return false;
	}
	
	public static boolean EntityCollisionEntity1(Rectangle moveres,Entity mover,Handler handler){
		for(int i=0;i<handler.entity.size();i++){
			entity = handler.entity.get(i);
			if(mover != entity && moveres.intersects(entity.getCollisionArea())){
				if(entity.getId() == Id.PLAYER){
					mover.collideplayer = true;
				}
				return true;
			}
		}
		return false;
	}
	
	public static boolean EntityCollisionEntityWithPolygon(Polygon moveres,Entity mover,Handler handler){
		for(int i=0;i<handler.entity.size();i++){
			entity = handler.entity.get(i);
			if(mover != entity && DoesPolygonIntersectPolygon(moveres, entity.getPolygon())){
				if(entity.id == Id.PLAYER){
					mover.collideplayer = true;
				}
				return true;
			}
		}
		return false;
	}
	
	
	/*Ez a metódus eldönti két polygonról,hogy mettszik-e egymást.*/
	private static boolean DoesPolygonIntersectPolygon(Polygon p1, Polygon p2) {
		Point p; 
		for(int i = 0; i < p2.npoints;i++) {
			p = new Point(p2.xpoints[i],p2.ypoints[i]); 
			if(p1.contains(p)) return true; 
		} 
		
		for(int i = 0; i < p1.npoints;i++) { 
			p = new Point(p1.xpoints[i],p1.ypoints[i]); 
			if(p2.contains(p))
				return true; 
			} 
		return false; 
	}
	
}
