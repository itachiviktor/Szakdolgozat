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
	public GameState actualGameState;/*Az aktu�lis j�t�khelyzetet is ismernie kell.*/
	
	private Game gsm;/*Ismeri a f� ciklust futtat� oszt�lyt is*/
	
	private boolean heldover;/*A gomb fel� van-e tartva az eg�r*/
	
	private boolean clicked;/*A gombra kattintottak-e*/
	
	public int width;
	public int height;
	
	private Sound mousehover;
	
	private boolean initialized = false;
	private boolean soundplaying = false;/*Alap�rtelmezetten ne j�tsza le a gomb soundot*/
	
	/*Az al�bbi fontrendereres holmik az�rt kellenek,hogy form�zott sz�veget tudjunk kirajzolni a k�perny�re.*/
	private FontRenderContext frc;
	
	private Font fontstyle = new Font("Arial",Font.PLAIN,25);
	private String text;
	private TextLayout visualizer;/*megjelen�t�*/
	
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
			/*Ha a gomb fel� vitt�k az egeret,akkor */
			g.drawImage(ImageAssets.startGameNonclicked, x , y,300,50,null);
			
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
		/*Ez be�ll�tja a clicked booleant.A kapott pontr�l eld�nti,hogy rajta van-e a gombkock�n.Ez a param�terk�nt kapott 
		 Point objektum majd mind�g az eg�rkurzor koordin�t�ja lesz.*/
		if(getBounds().contains(point)){
			clicked = true;
		}else{
			clicked = false;
		}
	}
}