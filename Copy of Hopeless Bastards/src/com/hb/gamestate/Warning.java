package com.hb.gamestate;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.hb.Game;

public class Warning extends Rectangle{
	/*Minden gamestatenak van egy ilyen változója.Ha gáz van, akkor láthatóvá teszi.*/
	
	/*Két konstruktor van az osztályhoz, az egyik ha képpel szeretnénk megjeleníteni az ablakot, másik, ha
	 az ablak csak szimplán egy színû.*/
	
	
	/*erre a gomra kattintva fogadja el a user , hogy gáz van.*/
	public GameStateButton understand;
	private Game gsm;
	private GameState actualGameState;
	private Color color = null;
	
	private BufferedImage image = null;
	
	private String warningMessage = null;
	
	private int stringCoordinateX;
	private int stringCoordinateY;
	
	public Warning(int x,int y,int width, int height,Game gsm,GameState actualGameState,Color color,String warningMessage) {
		this.color = color;
		
		init(x,y,width,height,gsm,actualGameState,warningMessage);
		
	}
	
	public Warning(int x,int y,int width, int height,Game gsm,GameState actualGameState,BufferedImage image,String warningMessage) {
		this.image = image;
		
		init(x,y,width,height,gsm,actualGameState,warningMessage);
		
	}
	
	
	private void init(int x,int y,int width, int height,Game gsm,GameState actualGameState,String warningMessage){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		setBounds(x, y, width, height);
		this.gsm = gsm;
		this.warningMessage = warningMessage;
		/*A befoglaló gamestate változója, hogy éppen megkell-e jelenítenie ezt a warningot vagy sem.*/
		actualGameState.isWarningSituation = false;
		this.actualGameState = actualGameState;
		
		understand = new GameStateButton(220, 220,135,25,null,null,"UNDERSTAND", gsm,false){
			@Override
			public void tick() {
				/*a gombot körülvevõ négyszög beállítása*/
				setBounds(x,y,width,height);
				
				/*Ha a gomb területe tartalmazza az aktuális GameStatetõl lekérdezett mouse Point -ot, ami az egér hovamutatása,
				 akkor ez azt jelenti hogy a gomb felett van az egér, azaz a heldover legyen igaz, és játtszuk le a gomb songot.*/
				if(getBounds().contains(Warning.this.actualGameState.mouse)){
					heldover = true;
				}else{
					/*Ha az egér nincs a gomb felett akkor mindíg false-ra állítjuk a heldover értékét.*/
					heldover = false;
				}
				
				if(clicked){
					/*Ha rákattintanak a megértem gombra, akkor beállítom a befoglaló gamestateban, hogy
					 már nem kell a warningot megjelenítenie.*/
					clicked = false;
					Warning.this.actualGameState.isWarningSituation = false;
				}
			}
		};
	}
	
	public void activateWarning(){
		actualGameState.isWarningSituation = true;
	}
	
	public void tick(){
		if(actualGameState.isWarningSituation){
			setBounds(x,y,width,height);
			
			understand.tick();
		}
	}
	
	public void render(Graphics g){
		if(actualGameState.isWarningSituation){
			if(this.color != null){
				g.setColor(this.color);
				g.fillRect(this.x, this.y, this.width, this.height);
				
				/*stringCoordinateek kiszámolása.*/
				
				g.drawString(this.warningMessage, this.x, this.y);
				
			}else if(this.image != null){
				g.drawImage(this.image, this.x, this.y, this.width, this.height,null);
				g.drawString(this.warningMessage, this.x, this.y);
				
			}	
			understand.render(g);
		}
	}
}
