package com.hb.gamestate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.hb.Game;
import com.hb.graphics.ImageAssets;
import com.hb.textfield.TextField2D;

public class MenuState extends GameState{
	/*F�men� j�t�k�ll�s*/
	
	/*El�sz�r is 3 gomb l�trehoz�sa*/
	private GameStateButton startGame;
	private GameStateButton options;
	private GameStateButton exit;

	
	
	private Point mousePoint;/*ez egy seg�d referencia, hogy ne kelljen minden tick met�dusban
	�j Point objektumot l�trehozni,hanem ennek v�ltoztatom az �rt�k�t, �s ezt adom �rt�k�l
	a mousenak(ami Point t�pus egy�bk�nt.)*/
	
	private Point point;
	
	public MenuState(Game gsm) {
		super(gsm);
		warning = new Warning(100, 100, 300, 100, gsm, this);
		init();
	}

	@Override
	public void init() {
		
		
		/*A gombok p�ld�nyos�t�sa �gy,hogy megadjuk a koordin�t�kat, �s a nextGameState-t,hogy melyik gomb,melyik GameState-ra
		 navig�lja az alkalmaz�st.*/
		
		/*�gy defini�lom a gombokat, hogy a funkci�t megval�s�t� tick met�dus miatt n�vtelen be�gyazott
		 oszt�ly , �gy ha a gomb m�st csin�l, m�st hoz el�, tudom szem�lyreszabni.*/
		startGame = new GameStateButton(Game.WIDTH/3, Game.HEIGHT/3,300,50, GameStateId.HANDLER,this,"START GAME", gsm,true){
			@Override
			public void tick() {
				/*a gombot k�r�lvev� n�gysz�g be�ll�t�sa*/
				setBounds(x,y,width,height);
				
				/*Ha a gomb ter�lete tartalmazza az aktu�lis GameStatet�l lek�rdezett mouse Point -ot, ami az eg�r hovamutat�sa,
				 akkor ez azt jelenti hogy a gomb felett van az eg�r, azaz a heldover legyen igaz, �s j�ttszuk le a gomb songot.*/
				if(getBounds().contains(actualGameState.mouse)){
					heldover = true;
					soundplaying = true;
				}else{
					/*Ha az eg�r nincs a gomb felett akkor mind�g false-ra �ll�tjuk a heldover �rt�k�t.*/
					heldover = false;
					
				}
				
				if(soundplaying){
					/*Ha a fenti k�dban �gy alakult,hogy az eg�r a gomb felett van,akkor a sound booleant igazra �l�tottuk,�s itt
					 mehet a lej�ttsz�s.*/
					mousehover.play();
					soundplaying = false;
				}
				
				if(clicked){
					/*Ha r�kattintottak a gomra, akkor elnavig�l a k�vetkez� GameStatera, azaz a list�ba felveszi
					 a nextGameState objektumot, ami pl. a Start Game gombn�l a Handler GameState lesz.*/
					
						gsm.states.add(new Handler(gsm));
					
					clicked = false;
				}
			}
		};
		options = new GameStateButton(Game.WIDTH/3, Game.HEIGHT/3 + 100,300,50, null,this,"OPTIONS", gsm,true);
		exit = new GameStateButton(Game.WIDTH/3, Game.HEIGHT/3 + 200,300,50, null,this,"EXIT", gsm,true);
		
		mousePoint = new Point();
		point = new Point();
		
		
		
	}

	@Override
	public void tick() {
		/*Mivel itt nem mozdulhatunk ki a kivet�ttett monitor koordin�tarendszer nagys�g�b�l,
		 ez�rt nem kell hozz�adni semmit a mouseMovedX,Y-hoz mivel az pontosan azt a helyet fogja visszaadni,ahova az eg�r ker�lt.*/
		mousePoint.setLocation(mouseMovedX,mouseMovedY);
		mouse = mousePoint;
		
		/*A 3 gomb tick met�dus�t h�vja.*/
		startGame.tick();
		options.tick();
		exit.tick();
		
		warning.tick();
	}

	@Override
	public void render(Graphics g) {
		/*H�tt�rk�p kirajzol�sa*/
		g.drawImage(ImageAssets.menuBackGround, 0, 0, Game.WIDTH,Game.HEIGHT,null);
		
		/*A 3 gomb render met�dus�nak megh�v�sa.*/
		startGame.render(g);
		options.render(g);
		exit.render(g);
		
		warning.render(g);
	
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
		
		point.setLocation(e.getX(),e.getY());/*ez a megold�s is mem�riasp�rol�s, az al�bbi megold�snak.*/
		//Point point = new Point(e.getX(),e.getY());
		
		startGame.setClicked(point);
		options.setClicked(point);
		exit.setClicked(point);
		
	
		
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		/*defini�lva van.*/
		
		
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
