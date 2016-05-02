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
	
	/*Ez az ost�ly egy gomb oszt�ly a Canvasan.Olyan mint Framekn�l a JButton.*/
	
	private static final long serialVersionUID = 1L;
	
	public GameState nextGamestate;/*Ha a gombra kattintanak,akkor ezt a GameState objektumot hozza el� a j�t�k,azaz behozza a j�t�kot,
	vagy az ption menut.*/
	public GameStateId nextGameState;
	public GameState actualGameState;/*Az aktu�lis j�t�khelyzetet is ismernie kell.*/
	
	public Game gsm;/*Ismeri a f� ciklust futtat� oszt�lyt is*/
	
	public boolean heldover;/*A gomb fel� van-e tartva az eg�r*/
	
	public boolean clicked;/*A gombra kattintottak-e*/
	
	public int width;
	public int height;
	
	public Sound mousehover;
	
	public boolean movingButton;/*Ez a v�ltoz� mondja meg azt, hogy az adott gomb mozg� vagy sem.
	Mozg� azt jelenti, hogy ha fel� vissz�k az egeret elmozdul.*/
	
	public boolean initialized = false;
	public boolean soundplaying = false;/*Alap�rtelmezetten ne j�tsza le a gomb soundot*/
	
	/*Az al�bbi fontrendereres holmik az�rt kellenek,hogy form�zott sz�veget tudjunk kirajzolni a k�perny�re.*/
	public FontRenderContext frc;
	
	public Font fontstyle;
	public String text;
	public TextLayout visualizer;/*megjelen�t�*/
	
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
		/*a bet�m�retet dinamikusan hat�rozom meg a gombm�ret magass�g�nak viszonylat�ban.*/
		fontstyle = new Font("Arial",Font.PLAIN,height-6);
		setBounds(x,y,width,height);
		mousehover = new Sound("/buttonhover.wav");
		
	}
	
	public void tick(){
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
			/*Ha a gomb fel� vitt�k az egeret,akkor */
			g.drawImage(ImageAssets.startGameNonclicked, x , y,this.width,this.height,null);
			
			/*Az initialized booleannak az a szerepe,hogy ha ez a v�ltoz� hamis,akkor az frc objektumnak meg kell adni
			  a graphics getFontRenderContextus�t, viszont ezt el�g egyszer, ez�rt nem kell minden render megh�v�skor
			  megcsin�lni.Ciszont az a baj,hogy ezt nem lehet konstruktorban int�zni,mert kell hozz� a Graphics objektum,
			  ez�rt az els� megh�v�skor ennek �rt�ket adunk,azt�n m�r nem kell,ez�rt van ez az initialized boolean, ami arra 
			  vonatkozik,hogy az frc FontRenderContextnek van-e �rt�k adva.*/
			if(!initialized){
				frc = ((Graphics2D) g).getFontRenderContext();
				visualizer = new TextLayout(text,fontstyle,frc);
				initialized = true;
			}
			g.setColor(new Color(207,208,210));
			/*Ezzel rajzoljuk ki a sz�veget a gombra, viszont itt m�rett�l f�gg�en kell �ll�tani,
			 a sz�veg pontja az els� bet� bal als� sarka.Azt kell valahogy kisz�molni m�retar�nyosan.
			 */
			visualizer.draw((Graphics2D) g, x + 2, y + height - 3);

		}else{
			/*Teh�t ha az eg�rgombot a gomb fel� vissz�k, akkor csak akkor mozog el, ha ez
			 mozg�gombk�nt van defini�lva.*/
			
			
			/*Ha mozg�gomb van akkor x ment�n 20-val elmozd�tjuk ha eg�lrgomb felette vcan.*/
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
				
				/*Ha mozg�gomb van akkor x ment�n 20-val elmozd�tjuk ha eg�lrgomb felette vcan.*/
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
		/*Ez be�ll�tja a clicked booleant.A kapott pontr�l eld�nti,hogy rajta van-e a gombkock�n.Ez a param�terk�nt kapott 
		 Point objektum majd mindig az eg�rkurzor koordin�t�ja lesz.*/
		if(getBounds().contains(point)){
			clicked = true;
		}else{
			clicked = false;
		}
	}
}