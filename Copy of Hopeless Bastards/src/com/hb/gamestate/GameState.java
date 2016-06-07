package com.hb.gamestate;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;

import com.hb.Game;


public abstract class GameState implements MouseListener, MouseMotionListener, MouseWheelListener,KeyListener{
/*J�t�k�ll�s(j�t�k,f�men�,opci� men� stb..)
 Ebb�l az oszt�lyb�l sz�rmazik az �sszes,teh�t itt defini�lom a k�z�s dolgokat.
 Ez az oszt�ly implement�lja a Listenereket,hisz a j�tkban a katint�sra m�shogy kell reag�lni,mint a f�men�n, ez�rt
 minden GameState mag�nak k�l�n meghat�rozza az esem�nykezel�ket, �s A Canvasnak mag�t a GameStatetet �t lehet adni mint Listenert*/
	public Game gsm;
	
	public int mouseMovedX;/*az eg�r x mozdul�sa*/
	public int mouseMovedY;/*az eg�r y mozdul�sa*/
	
	public Point mouse = new Point(0,0);/*Az eg�r melyik pontra mutat*/
	
	/*Minden gamestatenak van egy warning joptionpanehez hasonl� eszk�ze.A boolean azt mutatja, hogy
	 kikell-e rajzolni a figyelmeztet�st vagy sem.Ha warning van, akkor semmi m�s esem�nyre nem 
	 reag�lunk, csak a warning elfogad�s gombj�ra.*/
	public Warning warning;
	public boolean isWarningSituation = false;
	
	public GameState(Game gsm) {
		this.gsm = gsm;

	}
	
	public abstract Rectangle getVisibleArea();
	
	public abstract void init();
	
	public abstract void tick();
	
	public abstract void render(Graphics g);
		
	

}
