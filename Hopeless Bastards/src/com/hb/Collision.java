package com.hb;



import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

import com.hb.entity.Entity;
import com.hb.gamestate.Handler;
import com.hb.tile.Tile;


public class Collision {
	/*Ez az oszt�ly statikus met�dusokat tartalmaz,azaz szolg�ltat�sokat,amik arra j�k,hogy entit�sok �tk�z�s�t lehet vel�k vizsg�lni.*/
	
	/*Ez a k�t v�ltoz� az�rt kell,hogy ne kelljen mind�g �j referenciav�ltoz�t l�trehozni a ciklusokban,hanem etek kapnak
	 mind�g �j �rt�ket.Ez mem�riasp�rol�s.*/
	private static Entity en;
	private static Tile t;

	public static boolean PlayerCBlock(Point p1,Point p2,Point p3,Point p4,Handler handler){
		for(int i=0;i<handler.tile.size();i++){
			t = handler.tile.get(i);
			if(t.solid && (handler.getVisibleArea() != null && handler.getVisibleArea().intersects(t.getBounds())) ){
				if(t.contains(p1) || t.contains(p2) || t.contains(p3) || t.contains(p4)){
					return true;	
				}
			}
		}
		
		return false;
	}
	
	public static boolean EntityCollisionEntity(Point p1,Point p2,Point p3,Point p4,Entity mover,Handler handler){
		for(int i=0;i<handler.entity.size();i++){
			en = handler.entity.get(i);
			if(mover != en && (en.contains(p1) || en.contains(p2) || en.contains(p3) || en.contains(p4))){
				if(en.getId() == Id.PLAYER){
					mover.playerrelukozott = true;
				}
				return true;
			}
		}
		return false;
	}
	
	public static boolean EntityCollisionEntity1(Rectangle moveres,Entity mover,Handler handler){
		for(int i=0;i<handler.entity.size();i++){
			en = handler.entity.get(i);
			if(mover != en && moveres.intersects(en.getCollisionArea())){
				if(en.getId() == Id.PLAYER){
					mover.playerrelukozott = true;
				}
				return true;
			}
		}
		return false;
	}
	
	public static boolean EntityCollisionEntityWithPolygon(Polygon moveres,Entity mover,Handler handler){
		for(int i=0;i<handler.entity.size();i++){
			en = handler.entity.get(i);
			if(mover != en && DoesPolygonIntersectPolygon(moveres, en.getPolygon())){
				if(en.id == Id.PLAYER){
					mover.playerrelukozott = true;
				}
				return true;
			}
		}
		return false;
	}
	
	
	/*Ez a met�dus eld�nti k�t polygonr�l,hogy mettszik-e egym�st.*/
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
