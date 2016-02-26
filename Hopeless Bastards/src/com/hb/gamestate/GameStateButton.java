package com.hb.gamestate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;

import com.hb.Game;
import com.hb.Sound;
import com.hb.graphics.ImageAssets;

public class GameStateButton extends Rectangle{
	
	/*Ez az ostály egy gomb osztály a Canvasan.Olyan mint Frameknél a JButton.*/
	
	private static final long serialVersionUID = 1L;
	
	public GameState nextGamestate;/*Ha a gombra kattintanak,akkor ezt a GameState objektumot hozza elõ a játék,azaz behozza a játékot,
	vagy az ption menut.*/
	public GameState actualGameState;/*Az aktuális játékhelyzetet is ismernie kell.*/
	
	private Game gsm;/*Ismeri a fõ ciklust futtató osztályt is*/
	
	private boolean heldover;/*A gomb felé van-e tartva az egér*/
	
	private boolean clicked;/*A gombra kattintottak-e*/
	
	public int width;
	public int height;
	
	private Sound mousehover;
	
	private boolean initialized = false;
	private boolean soundplaying = false;/*Alapértelmezetten ne játsza le a gomb soundot*/
	
	/*Az alábbi fontrendereres holmik azért kellenek,hogy formázott szöveget tudjunk kirajzolni a képernyõre.*/
	private FontRenderContext frc;
	
	private Font fontstyle = new Font("Arial",Font.PLAIN,25);
	private String text;
	private TextLayout visualizer;/*megjelenítõ*/
	
	public GameStateButton(int x,int y,int width,int height,GameState nextGamestate,GameState actualGameState,String text,Game gsm) {
		this.actualGameState = actualGameState;
		this.nextGamestate = nextGamestate;
		this.gsm = gsm;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = text;
		setBounds(x,y,width,height);
		mousehover = new Sound("/buttonhover.wav");
		
	}
	
	public void tick(){
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
			if(nextGamestate != null){
				gsm.states.add(nextGamestate);
			}else{
				System.exit(0);
			}
			
			clicked = false;
		}
	}
	
	public void render(Graphics g){
		if(!heldover){
			/*Ha a gomb felé vittük az egeret,akkor */
			g.drawImage(ImageAssets.startGameNonclicked, x , y,300,50,null);
			
			/*Az initialized booleannak az a szerepe,hogy ha ez a változó hamis,akkor az frc objektumnak meg kell adni
			  a graphics getFontRenderContextusát, viszont ezt elég egyszer, ezért nem kell minden render meghíváskor
			  megcsinálni.Ciszont az a baj,hogy ezt nem lehet konstruktorban intézni,mert kell hozzá a Graphics objektum,
			  ezért az elsõ meghíváskor ennek értéket adunk,aztán már nem kell,ezért van ez az initialized boolean, ami arra 
			  vonatkozik,hogy az frc FontRenderContextnek van-e érték adva.*/
			if(!initialized){
				frc = ((Graphics2D) g).getFontRenderContext();
				visualizer = new TextLayout(text,fontstyle,frc);
				initialized = true;
			}
			g.setColor(new Color(207,208,210));
			visualizer.draw((Graphics2D) g, x + 2, y + 33);

		}else{
			
			g.drawImage(ImageAssets.startGameClicked, x + 20, y,300,50,null);
			if(!initialized){
				frc = ((Graphics2D) g).getFontRenderContext();
				visualizer = new TextLayout(text,fontstyle,frc);
				initialized = true;
			}
			
			g.setColor(new Color(28,32,35));
			visualizer.draw((Graphics2D) g, x + 2 + 20, y + 33);
			
			heldover = false;			
		}
	}

	public boolean isHeldover() {
		return heldover;
	}

	public void setHeldover(boolean heldover) {
		this.heldover = heldover;
	}

	public boolean isClicked() {
		return clicked;
	}

	public void setClicked(Point point) {
		/*Ez beállítja a clicked booleant.A kapott pontról eldönti,hogy rajta van-e a gombkockán.Ez a paraméterként kapott 
		 Point objektum majd mindíg az egérkurzor koordinátája lesz.*/
		if(getBounds().contains(point)){
			clicked = true;
		}else{
			clicked = false;
		}
	}
}