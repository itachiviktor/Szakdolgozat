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
	
	
	
	private Point mousePoint;/*ez egy segéd referencia, hogy ne kelljen minden tick metódusban
	új Point objektumot létrehozni,hanem ennek változtatom az értékét, és ezt adom értékül
	a mousenak(ami Point típus egyébként.)*/
	
	private Point point;
	
	
	public LoginState(Game gsm) {
		super(gsm);
		warning = new Warning(100, 100, 300, 100, gsm, LoginState.this);
		
		init();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		/*Ha kattintás történik meghívja a 3 gomb setClicked metódusát,ami ha az egér koordinátája,akkor
		 aktiválja azt ami a gomb szerepe.*/
		
		point.setLocation(e.getX(),e.getY());/*ez a megoldás is memóriaspórolás, az alábbi megoldásnak.*/
		//Point point = new Point(e.getX(),e.getY());
		
		login.setClicked(point);
		/*a warningon lévõ gomb eseménykezelõjét itt adom meg.*/
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
		/*Miért kell kivonni belõle a BoundX,Y értékeket?
		 Azért mert ha elvan tolva 0,0 sarokból az ablak, mondjuk 100,100 ba Bounddal, akkor
		 az egérkattintást is arrébb kell tolni, hisz ez a képernyõn lévõ egérkoordinátát adja,
		  azaz 100,100-on van az egér, akkor az a canvas 0,0 pont, ezért úgy kaphatom meg
		  hogy hogyha kivonom belõle.*/
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
		/*Mivel az õsben volt abstractként definiálva, és itt nincs rá szöükség,ezért térek vissza nullal.*/
		return null;
	}

	@Override
	public void init() {
		username = new TextField2D(" ", new Font("Times New Roman",Font.BOLD,30), Game.WIDTH/3, 200, 400, Color.black,new Color(149, 165, 186), Color.black, Color.yellow, gsm, true);
		password = new TextField2D(" ", new Font("Times New Roman",Font.BOLD,30), Game.WIDTH/3, 250, 400, Color.black,new Color(149, 165, 186), Color.black, Color.yellow, gsm, true);
		
		login = new GameStateButton(Game.WIDTH/3+20, 300,80,30, GameStateId.MENUSTATE,this,"LOGIN", gsm,false){
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
			                	/*Ha nincs találat az adatbázisban, azaz nincs ilyen felhasználó,
			                	 akkor a warningot fel kell dobni.*/
			                	LoginState.this.warning.activateWarning();
			                }
						} catch (SQLException e) {
							System.out.println(e.getMessage());
						}
						/*ez azért a végére kell, mert elõször megnézzük, hogy adatbázisban van e ilyen user,
						 mert ha nincs , akkor ide el sem jut, hisz ez kivált egy eseményt
						 (propertylistener), hogy az új gamestate jöjjön be,ott viszont a handler már
						 elkezd csatlakozni a nodejs-es szerverhez, azt pedig addig ne tegye
						 amíg nem vagyunk biztosak abban, hogy adatbázisban van olyan user amit a csávó megadott.*/
						
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
		/*Mivel itt nem mozdulhatunk ki a kivetíttett monitor koordinátarendszer nagyságából,
		 ezért nem kell hozzáadni semmit a mouseMovedX,Y-hoz mivel az pontosan azt a helyet fogja visszaadni,ahova az egér került.*/
		mousePoint.setLocation(mouseMovedX,mouseMovedY);
		mouse = mousePoint;
		
		login.tick();
		
		
		warning.tick();
		
		
		
	}

	@Override
	public void render(Graphics g) {
		/*Háttérkép kirajzolása*/
		g.drawImage(ImageAssets.backGround, 0, 0, Game.WIDTH,Game.HEIGHT,null);
		
		login.render(g);
		
		
		username.draw(g);
		
		password.draw(g);
		
		
		warning.render(g);
		
		
		
		/*Az egér démon kéz kirajzolása.*/
		g.setColor(Color.black);
		g.fillRect(mouse.x, mouse.y, 1, 1);
		g.drawImage(ImageAssets.cursorsprite.getBufferedImage(), mouse.x, mouse.y,40,40,null);
		
	}
	
}
