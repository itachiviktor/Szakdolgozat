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
	/*Fõmenü játékállás*/
	
	/*Elõször is 3 gomb létrehozása*/
	private GameStateButton startGame;
	private GameStateButton options;
	private GameStateButton exit;

	
	
	private Point mousePoint;/*ez egy segéd referencia, hogy ne kelljen minden tick metódusban
	új Point objektumot létrehozni,hanem ennek változtatom az értékét, és ezt adom értékül
	a mousenak(ami Point típus egyébként.)*/
	
	private Point point;
	
	public MenuState(Game gsm) {
		super(gsm);
		warning = new Warning(100, 100, 300, 100, gsm, this);
		init();
	}

	@Override
	public void init() {
		
		
		/*A gombok példányosítása úgy,hogy megadjuk a koordinátákat, és a nextGameState-t,hogy melyik gomb,melyik GameState-ra
		 navigálja az alkalmazást.*/
		
		/*Úgy definiálom a gombokat, hogy a funkciót megvalósító tick metódus miatt névtelen beágyazott
		 osztály , így ha a gomb mást csinál, mást hoz elõ, tudom személyreszabni.*/
		startGame = new GameStateButton(Game.WIDTH/3, Game.HEIGHT/3,300,50, GameStateId.HANDLER,this,"START GAME", gsm,true){
			@Override
			public void tick() {
				/*a gombot körülvevõ négyszög beállítása*/
				setBounds(x,y,width,height);
				
				/*Ha a gomb területe tartalmazza az aktuális GameStatetõl lekérdezett mouse Point -ot, ami az egér hovamutatása,
				 akkor ez azt jelenti hogy a gomb felett van az egér, azaz a heldover legyen igaz, és játtszuk le a gomb songot.*/
				if(getBounds().contains(actualGameState.mouse)){
					heldover = true;
					soundplaying = true;
				}else{
					/*Ha az egér nincs a gomb felett akkor mindíg false-ra állítjuk a heldover értékét.*/
					heldover = false;
					
				}
				
				if(soundplaying){
					/*Ha a fenti kódban úgy alakult,hogy az egér a gomb felett van,akkor a sound booleant igazra álítottuk,és itt
					 mehet a lejáttszás.*/
					mousehover.play();
					soundplaying = false;
				}
				
				if(clicked){
					/*Ha rákattintottak a gomra, akkor elnavigál a következõ GameStatera, azaz a listába felveszi
					 a nextGameState objektumot, ami pl. a Start Game gombnál a Handler GameState lesz.*/
					
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
		/*Mivel itt nem mozdulhatunk ki a kivetíttett monitor koordinátarendszer nagyságából,
		 ezért nem kell hozzáadni semmit a mouseMovedX,Y-hoz mivel az pontosan azt a helyet fogja visszaadni,ahova az egér került.*/
		mousePoint.setLocation(mouseMovedX,mouseMovedY);
		mouse = mousePoint;
		
		/*A 3 gomb tick metódusát hívja.*/
		startGame.tick();
		options.tick();
		exit.tick();
		
		warning.tick();
	}

	@Override
	public void render(Graphics g) {
		/*Háttérkép kirajzolása*/
		g.drawImage(ImageAssets.menuBackGround, 0, 0, Game.WIDTH,Game.HEIGHT,null);
		
		/*A 3 gomb render metódusának meghívása.*/
		startGame.render(g);
		options.render(g);
		exit.render(g);
		
		warning.render(g);
	
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
		
		point.setLocation(e.getX(),e.getY());/*ez a megoldás is memóriaspórolás, az alábbi megoldásnak.*/
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
		/*definiálva van.*/
		
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		/*Miért kell kivonni belõle a BoundX,Y értékeket?
		 Azért mert ha elvan tolva 0,0 sarokból az ablak, mondjuk 100,100 ba Bounddal, akkor
		 az egérkattintást is arrébb kell tolni, hisz ez a képernyõn lévõ egérkoordinátát adja,
		  azaz 100,100-on van az egér, akkor az a canvas 0,0 pont, ezért úgy kaphatom meg
		  hogy hogyha kivonom belõle.*/
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
