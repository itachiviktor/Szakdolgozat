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
/*Játékállás(játék,fõmenü,opció menü stb..)
 Ebból az osztályból származik az összes,tehát itt definiálom a közös dolgokat.
 Ez az osztály implementálja a Listenereket,hisz a játkban a katintásra máshogy kell reagálni,mint a fõmenün, ezért
 minden GameState magának külön meghatározza az eseménykezelõket, és A Canvasnak magát a GameStatetet át lehet adni mint Listenert*/
	public Game gsm;
	
	public int mouseMovedX;/*az egér x mozdulása*/
	public int mouseMovedY;/*az egér y mozdulása*/
	
	public Point mouse = new Point(0,0);/*Az egér melyik pontra mutat*/
	
	/*Minden gamestatenak van egy warning joptionpanehez hasonló eszköze.A boolean azt mutatja, hogy
	 kikell-e rajzolni a figyelmeztetést vagy sem.Ha warning van, akkor semmi más eseményre nem 
	 reagálunk, csak a warning elfogadás gombjára.*/
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
