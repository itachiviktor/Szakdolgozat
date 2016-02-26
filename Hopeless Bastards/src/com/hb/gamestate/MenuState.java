package com.hb.gamestate;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import com.hb.Game;
import com.hb.graphics.ImageAssets;



public class MenuState extends GameState{
	/*Fõmenü játékállás*/
	
	/*Elõször is 3 gomb létrehozása*/
	private GameStateButton startGame;
	private GameStateButton options;
	private GameStateButton exit;
	
	public MenuState(Game gsm) {
		super(gsm);
		init();
	}

	@Override
	public void init() {
		/*A gombok példányosítása úgy,hogy megadjuk a koordinátákat, és a nextGameState-t,hogy melyik gomb,melyik GameState-ra
		 navigálja az alkalmazást.*/
		startGame = new GameStateButton(Game.WIDTH/3, Game.HEIGHT/3,300,50, new Handler(gsm),this,"START GAME", gsm);
		options = new GameStateButton(Game.WIDTH/3, Game.HEIGHT/3 + 100,300,50, new Handler(gsm),this,"OPTIONS", gsm);
		exit = new GameStateButton(Game.WIDTH/3, Game.HEIGHT/3 + 200,300,50, null,this,"EXIT", gsm);
	}

	@Override
	public void tick() {
		/*Mivel itt nem mozdulhatunk ki a kivetíttett monitor koordinátarendszer nagyságából,
		 ezért nem kell hozzáadni semmit a mouseMovedX,Y-hoz mivel az pontosan azt a helyet fogja visszaadni,ahova az egér került.*/
		mouse = new Point(mouseMovedX,mouseMovedY);
		
		/*A 3 gomb tick metódusát hívja.*/
		startGame.tick();
		options.tick();
		exit.tick();
	}

	@Override
	public void render(Graphics g) {
		/*Háttérkép kirajzolása*/
		g.drawImage(ImageAssets.backGround, 0, 0, Game.WIDTH,Game.HEIGHT,null);
		
		/*A 3 gomb render metódusának meghívása.*/
		startGame.render(g);
		options.render(g);
		exit.render(g);
		
		/*Az egér démon kéz kirajzolása.*/
		g.setColor(Color.black);
		g.fillRect(mouse.x, mouse.y, 1, 1);
		g.drawImage(ImageAssets.cursorsprite.getBufferedImage(), mouse.x, mouse.y,40,40,null);
	}

	@Override
	public Rectangle getVisibleArea() {
		/*Mivel az õsben volt abstractként definiálva, és itt nincs rá szöükség,ezért térek vissza nullal.*/
		return null;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		/*Ha kattintás történik meghívja a 3 gomb setClicked metódusát,ami ha az egér koordinátája,akkor
		 aktiválja azt ami a gomb szerepe.*/
		
		Point point = new Point(e.getX(),e.getY());
		startGame.setClicked(point);
		options.setClicked(point);
		exit.setClicked(point);
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseMovedX = e.getXOnScreen();
		mouseMovedY = e.getYOnScreen();
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseMovedX = e.getXOnScreen();
		mouseMovedY = e.getYOnScreen();
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
			System.exit(0);
		}
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
