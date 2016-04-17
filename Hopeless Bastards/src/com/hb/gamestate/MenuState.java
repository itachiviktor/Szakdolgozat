package com.hb.gamestate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import com.hb.Game;
import com.hb.graphics.ImageAssets;
import com.hb.textfield.TextField2D;

public class MenuState extends GameState{
	/*F�men� j�t�k�ll�s*/
	
	/*El�sz�r is 3 gomb l�trehoz�sa*/
	private GameStateButton startGame;
	private GameStateButton options;
	private GameStateButton exit;
	
	private TextField2D textfield;
	
	public MenuState(Game gsm) {
		super(gsm);
		init();
	}

	@Override
	public void init() {
		/*A gombok p�ld�nyos�t�sa �gy,hogy megadjuk a koordin�t�kat, �s a nextGameState-t,hogy melyik gomb,melyik GameState-ra
		 navig�lja az alkalmaz�st.*/
		startGame = new GameStateButton(Game.WIDTH/3, Game.HEIGHT/3,300,50, GameStateId.HANDLER,this,"START GAME", gsm);
		options = new GameStateButton(Game.WIDTH/3, Game.HEIGHT/3 + 100,300,50, null,this,"OPTIONS", gsm);
		exit = new GameStateButton(Game.WIDTH/3, Game.HEIGHT/3 + 200,300,50, null,this,"EXIT", gsm);
		
		textfield = new TextField2D(" ", new Font("Times New Roman",Font.BOLD,30), 10, 600, 400, Color.red,Color.blue, Color.green, Color.yellow, gsm, true);
		
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
		
		textfield.draw(g);
		
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
		
		System.out.println("click");
		
		Point point = new Point(e.getX(),e.getY());
		startGame.setClicked(point);
		options.setClicked(point);
		exit.setClicked(point);
		
		textfield.mouseClicked(e);
		
		
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		textfield.mousePressed(e);
		
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		/*defini�lva van.*/
		textfield.mouseReleased(e);
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		/*Mi�rt kell kivonni bel�le a BoundX,Y �rt�keket?
		 Az�rt mert ha elvan tolva 0,0 sarokb�l az ablak, mondjuk 100,100 ba Bounddal, akkor
		 az eg�rkattint�st is arr�bb kell tolni, hisz ez a k�perny�n l�v� eg�rkoordin�t�t adja,
		  azaz 100,100-on van az eg�r, akkor az a canvas 0,0 pont, ez�rt �gy kaphatom meg
		  hogy hogyha kivonom bel�le.*/
		mouseMovedX = e.getXOnScreen() - gsm.BoundX;
		mouseMovedY = e.getYOnScreen() - gsm.BoundY;
		textfield.mouseDragged(e);
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseMovedX = e.getXOnScreen() - gsm.BoundX;
		mouseMovedY = e.getYOnScreen() - gsm.BoundY;
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		textfield.keyPressed(e);
		System.out.println("faszomkeykey");
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
			System.exit(0);
		}
		
		/*if(e.getKeyCode() == KeyEvent.VK_A){
			gsm.deleteListeners(this);
		}*/
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
		
	}
}
