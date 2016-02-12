package com.hb.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Random;

import com.hb.gamestate.Handler;

public class DamagingText {
	/*Az a megjelen� sz�m a k�perny�n,ami azt mutatja ,hogy kir�l mennyi sebz�st v�gezt�nk,illetve mennyit gy�gy�tottunk
	 sebz�st piros,gy�gy�t�st z�ld sz�n� sz�mk�nt v�lhetj�k felfedezni.*/
	

	private String text;/*T�rolt sz�m�rt�k.*/
	double x;
	double y;
	
	private boolean isAlive;/*Egy id� ut�n el kell t�nnie a k�perny�r�l a sz�mnak,�s ez a v�ltoz� jelzi,hogy meddig van k�perny�n.*/
	
	private boolean jump;/*az ugr� hat�s*/
	private boolean right;/*jobbra vagy ballra ugorjon el a karaktert�l a sz�m,ha jobbra akkor a right v�ltoz� �rt�ke true,
	ha ballra akkor a right �rt�ke false, teh�t k�l�n left booleant nem csin�ltam.*/
	
	/*Meddig legyen a k�perny�n*/
	private float lifeTime = 5;
	
	/*Maximum meddig ugorjon*/
	private float maxUp = 2.5f;
	
	/*Jelenleg milyen magass�gn�l j�r az ugr�sban.*/
	private float currentUp;
	
	private Handler handler;
	
	public boolean harmful;/*Ez a v�ltoz� azt jelzi,hogy �rtalmas vagy hasznos v�ltoz�sr�l van sz�.
	P�ld�ul a sebz�s a piros mert �rtalmas, viszont a gy�gy�t�s az z�ld,hizs hasznunkra v�llik.*/
	
	public DamagingText(double x,double y,String text,boolean harmful,Handler handler) {
		this.handler = handler;
		this.x = x;
		this.y = y;
		this.text = text;
		this.harmful = harmful;
		isAlive = true;
		jump = true;
		
		/*Minden damagesz�veg v�letlenszer�en ugrik jobbra vagy ballra.*/
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
			
			/*az ugr�st sz�moljuk ki*/
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
				/*A handlernek van egy list�ja amiben az �sszes t�mad�s/gy�gy�t�s sz�mot tartalmazza,amik �pp kirajzol�dnak,viszont
				 ha m�r nincs r�juk sz�ks�g,akkor at tudja mag�r�l a sz�veg,�s t�rli mag�t a handler list�j�b�l,azaz nem fog t�bb� 
				 megh�v�dni sem tick() sem render() met�dusa  (mem�ri�b�l is elt�nik).*/
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
