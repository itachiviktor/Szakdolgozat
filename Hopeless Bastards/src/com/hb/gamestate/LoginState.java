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

import com.hb.Game;
import com.hb.graphics.ImageAssets;
import com.hb.textfield.TextField2D;

public class LoginState extends GameState{

	private TextField2D username;
	private TextField2D password;
	
	private GameStateButton login;
	
	
	
	private Point mousePoint;/*ez egy seg�d referencia, hogy ne kelljen minden tick met�dusban
	�j Point objektumot l�trehozni,hanem ennek v�ltoztatom az �rt�k�t, �s ezt adom �rt�k�l
	a mousenak(ami Point t�pus egy�bk�nt.)*/
	
	private Point point;
	
	
	public LoginState(Game gsm) {
		super(gsm);
		warning = new Warning(100, 100, 300, 100, gsm, LoginState.this);
		
		init();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		/*Ha kattint�s t�rt�nik megh�vja a 3 gomb setClicked met�dus�t,ami ha az eg�r koordin�t�ja,akkor
		 aktiv�lja azt ami a gomb szerepe.*/
		
		point.setLocation(e.getX(),e.getY());/*ez a megold�s is mem�riasp�rol�s, az al�bbi megold�snak.*/
		//Point point = new Point(e.getX(),e.getY());
		
		login.setClicked(point);
		/*a warningon l�v� gomb esem�nykezel�j�t itt adom meg.*/
		warning.understand.setClicked(point);
		
		username.mouseClicked(e);
		password.mouseClicked(e);
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		username.mousePressed(e);
		password.mousePressed(e);
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		username.mouseReleased(e);
		password.mouseReleased(e);
		
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
		username.mouseDragged(e);
		password.mouseDragged(e);
		
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
		username.keyPressed(e);
		password.keyPressed(e);
		
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
			System.exit(0);
		}
		
		/*if(e.getKeyCode() == KeyEvent.VK_A){
			gsm.deleteListeners(this);
		}*/
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}

	@Override
	public Rectangle getVisibleArea() {
		/*Mivel az �sben volt abstractk�nt defini�lva, �s itt nincs r� sz��ks�g,ez�rt t�rek vissza nullal.*/
		return null;
	}

	@Override
	public void init() {
		username = new TextField2D(" ", new Font("Times New Roman",Font.BOLD,30), Game.WIDTH/3, 200, 400, Color.black,new Color(149, 165, 186), Color.black, Color.yellow, gsm, true);
		password = new TextField2D(" ", new Font("Times New Roman",Font.BOLD,30), Game.WIDTH/3, 250, 400, Color.black,new Color(149, 165, 186), Color.black, Color.yellow, gsm, true);
		
		login = new GameStateButton(Game.WIDTH/3+20, 300,80,30, GameStateId.MENUSTATE,this,"LOGIN", gsm,false){
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
					
					if(nextGameState == GameStateId.MENUSTATE){
						
						if(username.getText() == null){
							gsm.username = "alma";
						}else{
							gsm.username = username.getText();
						}
						
						if(password.getText() == null){
							gsm.username = "alma";
						}else{
							gsm.username = username.getText();
						}
						
						try {
							Class.forName("com.mysql.jdbc.Driver");
							System.out.println("driver found");
						} catch (ClassNotFoundException e) {
							System.out.println("driver not found");
						}
						String query = "SELECT * FROM Users WHERE username=? AND password=?";
						String url = "jdbc:mysql://sql8.freemysqlhosting.net:3306/sql8117357";
						try {
							Connection conn = DriverManager.getConnection(url,"sql8117357","bx69XF2WqN");
							PreparedStatement stmt = conn.prepareStatement(query);
			                stmt.setString(1,username.getText());
			                stmt.setString(2, password.getText());
			                ResultSet rs = stmt.executeQuery();
			                
			                boolean hasMatch = false;
			                while(rs.next()){
			                	hasMatch = true;
			                	gsm.states.add(new MenuState(gsm));
			                }
			                if(hasMatch == false){
			                	/*Ha nincs tal�lat az adatb�zisban, azaz nincs ilyen felhaszn�l�,
			                	 akkor a warningot fel kell dobni.*/
			                	LoginState.this.warning.activateWarning();
			                }
						} catch (SQLException e) {
							System.out.println(e.getMessage());
						}
						/*ez az�rt a v�g�re kell, mert el�sz�r megn�zz�k, hogy adatb�zisban van e ilyen user,
						 mert ha nincs , akkor ide el sem jut, hisz ez kiv�lt egy esem�nyt
						 (propertylistener), hogy az �j gamestate j�jj�n be,ott viszont a handler m�r
						 elkezd csatlakozni a nodejs-es szerverhez, azt pedig addig ne tegye
						 am�g nem vagyunk biztosak abban, hogy adatb�zisban van olyan user amit a cs�v� megadott.*/
						
					}else{
						System.exit(0);
					}
					
					clicked = false;
				}
			}
		};
		
		mousePoint = new Point();
		point = new Point();
	}

	@Override
	public void tick() {
		/*Mivel itt nem mozdulhatunk ki a kivet�ttett monitor koordin�tarendszer nagys�g�b�l,
		 ez�rt nem kell hozz�adni semmit a mouseMovedX,Y-hoz mivel az pontosan azt a helyet fogja visszaadni,ahova az eg�r ker�lt.*/
		mousePoint.setLocation(mouseMovedX,mouseMovedY);
		mouse = mousePoint;
		
		login.tick();
		
		
		warning.tick();
		
		
		
	}

	@Override
	public void render(Graphics g) {
		/*H�tt�rk�p kirajzol�sa*/
		g.drawImage(ImageAssets.backGround, 0, 0, Game.WIDTH,Game.HEIGHT,null);
		
		login.render(g);
		
		
		username.draw(g);
		
		password.draw(g);
		
		
		warning.render(g);
		
		
		
		/*Az eg�r d�mon k�z kirajzol�sa.*/
		g.setColor(Color.black);
		g.fillRect(mouse.x, mouse.y, 1, 1);
		g.drawImage(ImageAssets.cursorsprite.getBufferedImage(), mouse.x, mouse.y,40,40,null);
		
	}
	
}
