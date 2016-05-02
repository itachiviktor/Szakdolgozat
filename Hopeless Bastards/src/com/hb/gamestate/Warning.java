package com.hb.gamestate;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.hb.Game;

public class Warning extends Rectangle{
	/*Minden gamestatenak van egy ilyen v�ltoz�ja.Ha g�z van, akkor l�that�v� teszi.*/
	
	
	/*erre a gomra kattintva fogadja el a user , hogy g�z van.*/
	public GameStateButton understand;
	private Game gsm;
	private GameState actualGameState;
	
	public Warning(int x,int y,int width, int height,Game gsm,GameState actualGameState) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		setBounds(x, y, width, height);
		this.gsm = gsm;
		/*A befoglal� gamestate v�ltoz�ja, hogy �ppen megkell-e jelen�tenie ezt a warningot vagy sem.*/
		actualGameState.isWarningSituation = false;
		this.actualGameState = actualGameState;
		
		understand = new GameStateButton(220, 220,135,25,null,null,"UNDERSTAND", gsm,false){
			@Override
			public void tick() {
				/*a gombot k�r�lvev� n�gysz�g be�ll�t�sa*/
				setBounds(x,y,width,height);
				
				/*Ha a gomb ter�lete tartalmazza az aktu�lis GameStatet�l lek�rdezett mouse Point -ot, ami az eg�r hovamutat�sa,
				 akkor ez azt jelenti hogy a gomb felett van az eg�r, azaz a heldover legyen igaz, �s j�ttszuk le a gomb songot.*/
				if(getBounds().contains(Warning.this.actualGameState.mouse)){
					heldover = true;
				}else{
					/*Ha az eg�r nincs a gomb felett akkor mind�g false-ra �ll�tjuk a heldover �rt�k�t.*/
					heldover = false;
				}
				
				if(clicked){
					/*Ha r�kattintanak a meg�rtem gombra, akkor be�ll�tom a befoglal� gamestateban, hogy
					 m�r nem kell a warningot megjelen�tenie.*/
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
			g.setColor(Color.black);
			g.fillRect(this.x, this.y, this.width, this.height);
			understand.render(g);
		}
		
	}
}
