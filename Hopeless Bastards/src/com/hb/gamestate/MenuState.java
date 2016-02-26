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
	/*F�men� j�t�k�ll�s*/
	
	/*El�sz�r is 3 gomb l�trehoz�sa*/
	private GameStateButton startGame;
	private GameStateButton options;
	private GameStateButton exit;
	
	public MenuState(Game gsm) {
		super(gsm);
		init();
	}

	@Override
	public void init() {
		/*A gombok p�ld�nyos�t�sa �gy,hogy megadjuk a koordin�t�kat, �s a nextGameState-t,hogy melyik gomb,melyik GameState-ra
		 navig�lja az alkalmaz�st.*/
		startGame = new GameStateButton(Game.WIDTH/3, Game.HEIGHT/3,300,50, new Handler(gsm),this,"START GAME", gsm);
		options = new GameStateButton(Game.WIDTH/3, Game.HEIGHT/3 + 100,300,50, new Handler(gsm),this,"OPTIONS", gsm);
		exit = new GameStateButton(Game.WIDTH/3, Game.HEIGHT/3 + 200,300,50, null,this,"EXIT", gsm);
	}

	@Override
	public void tick() {
		/*Mivel itt nem mozdulhatunk ki a kivet�ttett monitor koordin�tarendszer nagys�g�b�l,
		 ez�rt nem kell hozz�adni semmit a mouseMovedX,Y-hoz mivel az pontosan azt a helyet fogja visszaadni,ahova az eg�r ker�lt.*/
		mouse = new Point(mouseMovedX,mouseMovedY);
		
		/*A 3 gomb tick met�dus�t h�vja.*/
		startGame.tick();
		options.tick();
		exit.tick();
	}

	@Override
	public void render(Graphics g) {
		/*H�tt�rk�p kirajzol�sa*/
		g.drawImage(ImageAssets.backGround, 0, 0, Game.WIDTH,Game.HEIGHT,null);
		
		/*A 3 gomb render met�dus�nak megh�v�sa.*/
		startGame.render(g);
		options.render(g);
		exit.render(g);
		
		/*Az eg�r d�mon k�z kirajzol�sa.*/
		g.setColor(Color.black);
		g.fillRect(mouse.x, mouse.y, 1, 1);
		g.drawImage(ImageAssets.cursorsprite.getBufferedImage(), mouse.x, mouse.y,40,40,null);
	}

	@Override
	public Rectangle getVisibleArea() {
		/*Mivel az �sben volt abstractk�nt defini�lva, �s itt nincs r� sz��ks�g,ez�rt t�rek vissza nullal.*/
		return null;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		/*Ha kattint�s t�rt�nik megh�vja a 3 gomb setClicked met�dus�t,ami ha az eg�r koordin�t�ja,akkor
		 aktiv�lja azt ami a gomb szerepe.*/
		
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
