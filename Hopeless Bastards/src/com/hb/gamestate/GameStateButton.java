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
	public GameStateId nextGameState;
	public GameState actualGameState;/*Az aktuális játékhelyzetet is ismernie kell.*/
	
	public Game gsm;/*Ismeri a fõ ciklust futtató osztályt is*/
	
	public boolean heldover;/*A gomb felé van-e tartva az egér*/
	
	public boolean clicked;/*A gombra kattintottak-e*/
	
	public int width;
	public int height;
	
	public Sound mousehover;
	
	public boolean movingButton;/*Ez a változó mondja meg azt, hogy az adott gomb mozgó vagy sem.
	Mozgó azt jelenti, hogy ha felé visszük az egeret elmozdul.*/
	
	public boolean initialized = false;
	public boolean soundplaying = false;/*Alapértelmezetten ne játsza le a gomb soundot*/
	
	/*Az alábbi fontrendereres holmik azért kellenek,hogy formázott szöveget tudjunk kirajzolni a képernyõre.*/
	public FontRenderContext frc;
	
	public Font fontstyle;
	public String text;
	public TextLayout visualizer;/*megjelenítõ*/
	
	public GameStateButton(int x,int y,int width,int height,GameStateId nextGameState,GameState actualGameState,String text,Game gsm,boolean movingButton) {
		this.actualGameState = actualGameState;
		//this.nextGamestate = nextGamestate;
		this.nextGameState = nextGameState;
		this.gsm = gsm;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = text;
		this.movingButton = movingButton;
		/*a betûméretet dinamikusan határozom meg a gombméret magasságának viszonylatában.*/
		fontstyle = new Font("Arial",Font.PLAIN,height-6);
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
			
			
			if(nextGameState == GameStateId.HANDLER){
				gsm.states.add(new Handler(gsm));
			}else{
				System.exit(0);
			}
			
			clicked = false;
		}
	}
	
	public void render(Graphics g){
		
		if(!heldover){
			/*Ha a gomb felé vittük az egeret,akkor */
			g.drawImage(ImageAssets.startGameNonclicked, x , y,this.width,this.height,null);
			
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
			/*Ezzel rajzoljuk ki a szöveget a gombra, viszont itt mérettõl függöen kell állítani,
			 a szöveg pontja az elsõ betû bal alsó sarka.Azt kell valahogy kiszámolni méretarányosan.
			 */
			visualizer.draw((Graphics2D) g, x + 2, y + height - 3);

		}else{
			/*Tehát ha az egérgombot a gomb felé visszük, akkor csak akkor mozog el, ha ez
			 mozgógombként van definiálva.*/
			
			
			/*Ha mozgógomb van akkor x mentén 20-val elmozdítjuk ha egélrgomb felette vcan.*/
				if(movingButton){
					g.drawImage(ImageAssets.startGameClicked, x + 20, y,this.width,this.height,null);
				}else{
					g.drawImage(ImageAssets.startGameClicked, x, y,this.width,this.height,null);
				}
				
				if(!initialized){
					frc = ((Graphics2D) g).getFontRenderContext();
					visualizer = new TextLayout(text,fontstyle,frc);
					initialized = true;
				}
				
				g.setColor(new Color(28,32,35));
				
				/*Ha mozgógomb van akkor x mentén 20-val elmozdítjuk ha egélrgomb felette vcan.*/
				if(movingButton){
					visualizer.draw((Graphics2D) g, x + 2 + 20, y + height - 3);
				}else{
					visualizer.draw((Graphics2D) g, x + 2, y + height - 3);
				}
				
				
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
		 Point objektum majd mindig az egérkurzor koordinátája lesz.*/
		if(getBounds().contains(point)){
			clicked = true;
		}else{
			clicked = false;
		}
	}
}