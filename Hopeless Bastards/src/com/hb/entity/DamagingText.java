package com.hb.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Random;

import com.hb.gamestate.Handler;

public class DamagingText {
	/*Az a megjelenõ szám a képernyõn,ami azt mutatja ,hogy kirõl mennyi sebzést végeztünk,illetve mennyit gyógyítottunk
	 sebzést piros,gyógyítást zöld színû számként vélhetjük felfedezni.*/
	

	private String text;/*Tárolt számérték.*/
	double x;
	double y;
	
	private boolean isAlive;/*Egy idõ után el kell tûnnie a képernyõröl a számnak,és ez a változó jelzi,hogy meddig van képernyõn.*/
	
	private boolean jump;/*az ugró hatás*/
	private boolean right;/*jobbra vagy ballra ugorjon el a karaktertõl a szám,ha jobbra akkor a right változó értéke true,
	ha ballra akkor a right értéke false, tehát külön left booleant nem csináltam.*/
	
	/*Meddig legyen a képernyõn*/
	private float lifeTime = 5;
	
	/*Maximum meddig ugorjon*/
	private float maxUp = 2.5f;
	
	/*Jelenleg milyen magasságnál jár az ugrásban.*/
	private float currentUp;
	
	private Handler handler;
	
	public boolean harmful;/*Ez a változó azt jelzi,hogy ártalmas vagy hasznos változásról van szó.
	Például a sebzés a piros mert ártalmas, viszont a gyógyítás az zöld,hizs hasznunkra vállik.*/
	
	public DamagingText(double x,double y,String text,boolean harmful,Handler handler) {
		this.handler = handler;
		this.x = x;
		this.y = y;
		this.text = text;
		this.harmful = harmful;
		isAlive = true;
		jump = true;
		
		/*Minden damageszöveg véletlenszerûen ugrik jobbra vagy ballra.*/
		Random ran = new Random();
		int rand = ran.nextInt(2);
		if(rand == 0){
			right = true;
		}else if(rand == 1){
			right = false;
		}
	}
	
	public void tick(){
		if(isAlive){
			if(lifeTime > 0){
				lifeTime-=0.1;
			}else if(lifeTime <= 0){
				isAlive = false;
			}
			
			/*az ugrást számoljuk ki*/
			if(jump){
				if(currentUp != maxUp){
					currentUp+=0.1;
					y-=currentUp;
				}
				if(currentUp >= maxUp){
					jump = false;
				}
				
				if(right){
					x+=currentUp * new Random().nextFloat() * maxUp ;
				}else{
					x-=currentUp * new Random().nextFloat() * maxUp ;
				}
				
			}else{
				if(currentUp != 0){
					currentUp -= 0.1;
					y+=currentUp;
				}
				if(currentUp <= 0){
					maxUp-=0.1;
					jump = true;
				}
			}
			
		}else{
			if(this != null){
				/*A handlernek van egy listája amiben az összes támadás/gyógyítás számot tartalmazza,amik épp kirajzolódnak,viszont
				 ha már nincs rájuk szükség,akkor at tudja magáról a szöveg,és törli magát a handler listájából,azaz nem fog többé 
				 meghívódni sem tick() sem render() metódusa  (memóriából is eltûnik).*/
				handler.damagetext.remove(this);
			}
		}
	}
	
	public void render(Graphics g){
		if(isAlive){
			if(harmful){
				g.setColor(Color.red);
			}else{
				g.setColor(Color.green);
			}
			g.drawString(text,(int)x, (int)y);
			g.setColor(Color.white);
		}
	}
	
	public Point getCoord(){
		return new Point((int)x,(int)y);
	}
}
