package com.hb;

import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferStrategy;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;

import com.hb.entity.CharacterType;
import com.hb.gamestate.GameState;
import com.hb.gamestate.LoginState;
import com.hb.gamestate.MenuState;
import com.hb.gamestate.Warning;
import com.hb.graphics.ImageAssets;


public class Game extends Canvas implements Runnable,PropertyChangeListener{

	private static final long serialVersionUID = 1L;
	public static int WIDTH;
	public static int HEIGHT;
	
	public static JFrame frame;
	
	public CharacterType charactertype;
	
	
	public int tick = 0;
	public int render = 0;
	
	/*Ez az alábbi négy érték összetartozik, azért kell belõlük 2x2, mert statikus metódusban is
	 felszeretném használni az értékét.A bal felsõ sarokhoz képest hány pixellel az x,y iránxba tolódik el
	 a kijelzõ.*/
	public int BoundX = 700;
	public int BoundY = 0;
	public static int BoundXS = 700;
	public static int BoundYS = 0;
	
	public String username;

	public static  int maintime = 0;
	/*Ez a változó a játék indítása óta eltelt másodpercet tartalmazza.Egy külön szál másopercenként eggyel növeli az értékét.
	 Ez az idõzítés miatt nagyon fontos.*/
	
	private Thread thread;
	private boolean running = false;/*Szál indításnál van szerepe.*/
	
	public static ImageAssets assets = new ImageAssets();/*Ez az objektum tartalmazza az összes betöltött képet,BufferedImaget,
	statikus változóként elérhetõek ezen statikus objektumon keresztül.*/
	
	
	public GameStateList states;/*Ez az osztály lényegében a GameStateManager,ami mindíg ebben a listában
	tárolja az aktuális GameStatet,tehát ennek a listának mindíg csak egy eleme van,az aktuális GameState.*/
	
	private OneSecTimer timer = new OneSecTimer();/*Ugye ebbõl a szálból indítom majd a másodpercszámláló szálat,ezért itt 
	inicializálódik az objektuma.*/
	
	public String serverURL;
	private BufferStrategy bs;
	private Graphics g;
	
	
	
	public Game() {
		Wini ini;
		try {
			/*Az ini fájl kezelése, innen olvasom ki a szervert url-t és többi konstansokat. */
			ini = new Wini(new File("./config.ini"));
			serverURL = ini.get("Game", "serverURL", String.class);
	        boolean fullScreenMode = ini.get("Game", "fullScreenMode", boolean.class);

		} catch (InvalidFileFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();/*A képernyõ méretét kérem le*/
		/*HEIGHT = dim.height;
		WIDTH = dim.width;*/
	
		HEIGHT = 700;
		WIDTH = 600;
		
		dim = new Dimension(WIDTH,HEIGHT);

		setPreferredSize(dim);
		setMaximumSize(dim);
		setMinimumSize(dim);
		setBounds(BoundX, BoundY, WIDTH, HEIGHT);
		
		states = new GameStateList();
		
		states.registToList(this);/*Property figyelõt beállítom,tehát ha a listába változás történik,ez értesül róla*/
		states.add(new LoginState(this));/*Alapértelmezetten a fõmenü játékállás jön be,ezért a listának elõször azt adjuk elmnek*/
		initListeners(states.get(0));/*Ez az osztály a Canvas leszármazott, ezért csak ez az osztály tud közvetlenül figyelõket
		beállítani rá.Viszont azt nem tudhatja,hogy a player mire mit csinál meg a gombok.Viszont a GameState az tudja, ezért
		a GameState osztályok implementálják az összes Listenert, és ezért az aktuális GameState átadva az initListeners osztálynak
		beállítja a Canvasra az õ figyelõit,tehát az fog történni kattintásra , stb.. amit abban a GameStateban definiáltam,hisz
		mindig az aktuális GameState tudja mi történhet magával.*/
	}
	
	private void init(){
		/*Az init metódus annyit csinál,hogy az aktuális GameState init metódusát hívja meg*/
		states.get(0).init();
		/*Ezt a metódust nem használjuk, a Statek a konstruktorkban meghívják az init metódusukat.*/
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt){
		/*Ha változás történik a GameState Listában, akkor értesül ez az osztály és ez a metódus hívódik meg,
		 ez pedig azt csinálja,hogy a Canvashez az új GameState eseménykezelõit állítja be.*/
		if(evt.getPropertyName().equals("GameStateChange")){
			initListeners((GameState)evt.getNewValue(),(GameState)evt.getOldValue());
		}
	}
	
	/*Ezt nem használom.*/
	public void deleteListeners(GameState oldstate){
		KeyListener key = oldstate;
		
		removeKeyListener(key);
		
	}
	
	/*public void removeKeyListeners(){
		removeKeyListener(arg0);
	}*/
	
	public void addKeyListeners(){
		
	}
	
	private void initListeners(GameState state,GameState oldstate){
		/*A Canvashez a paraméterben kapott GameState eseménykezelõit állítja be.*/
		
		
		
		/*
					 You can consider 3 approaches:
			
			1) Save reference to your listener before adding it so you can remove it later:
			
			MouseListener ml = new MouseAdapter() {
			    public void mouseClicked(java.awt.event.MouseEvent evt) {
			        chatInputMouseClicked(evt);
			    }
			};
			chatInput.addMouseListener (ml);
			...
			chatInput.removeMouseListener (ml);
		 */
		KeyListener key = oldstate;
		MouseListener m1 = oldstate;
		MouseMotionListener m2 = oldstate;
		MouseWheelListener m3 = oldstate;
		
		removeKeyListener(key);
		removeMouseListener(m1);
		removeMouseMotionListener(m2);
		removeMouseWheelListener(m3);
		
		addKeyListener(state);
		addMouseListener(state);
		addMouseMotionListener(state);
		addMouseWheelListener(state);
	}
	
	/*Ezt nem használom*/
	private void initListeners(GameState state){
		/*A Canvashez a paraméterben kapott GameState eseménykezelõit állítja be.*/
		
		//addKeyListener(state);
		/*addMouseListener(state);
		addMouseMotionListener(state);
		addMouseWheelListener(state);*/
		
	}
	
	private synchronized void start(){
		/*Szál indítása*/
		if(running){
			return;
		}
		running = true;
		thread = new Thread(this,"Thread");
		thread.start();
	}
	
	private synchronized void stop(){
		if(!running){
			
			return;
		}
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void render(){
		
		
		bs = getBufferStrategy();
		if(bs == null){
			createBufferStrategy(3);
			return;
		}
		g = bs.getDrawGraphics();
		
		/*Az aktuális GameState render metódusát hívjuk meg*/
		states.get(0).render(g);
		
		/*Az utolsó két utasítás a bufferelt kirajzoláshoz szükséges.*/
		g.dispose();
		bs.show();
	}
	
	public void tick(){
		/*A tick metódus az aktuális GameState tick metódusát hívja meg.*/
		states.get(0).tick();
	}
	
	@Override
	public void run() {
	       boolean runFlag = true;
	       double delta = 0.016;
	      // init();
	       
	       	timer.start();
	       	
	        // convert the time to seconds
	        double nextTime = (double)System.nanoTime() / 1000000000.0;/*az idõ másodpercben*/
	        double maxTimeDiff = 0.5;
	        int skippedFrames = 1;
	        int maxSkippedFrames = 5;
	        while(runFlag){
	            // convert the time to seconds
	            double currTime = (double)System.nanoTime() / 1000000000.0;
	            if((currTime - nextTime) > maxTimeDiff){
	            	nextTime = currTime;
	            }
	            if(currTime >= nextTime){
	                // assign the time for the next update
	                nextTime += delta;
	                tick();
	               // tick++;
	                if((currTime < nextTime) || (skippedFrames > maxSkippedFrames)){
	                    render();
	                   // render++;
	                    skippedFrames = 1;
	                }else{
	                    skippedFrames++;
	                }
	            }else{
	                // calculate the time to sleep
	                int sleepTime = (int)(1000.0 * (nextTime - currTime));
	                // ébresztés ellenõrzés
	                if(sleepTime > 0)
	                {
	                    // sleep until the next update(következõ tick hívásik altatni a szálat)
	                    try
	                    {
	                        Thread.sleep(sleepTime);
	                    }
	                    catch(InterruptedException e)
	                    {
	                       //itt ne csináljon semmit
	                    }
	                }
	            }
	           
				/*System.out.println("tick: " + tick);
		        System.out.println("render: " + render);*/
	        }
	        
		stop();
	}
	
	public static int getFrameWidth(){
		return WIDTH;
	}
	
	public static int getFrameHeight(){
		return HEIGHT;
	}
	
	public static void main(String[] args) {
		
		/*A mainben rakom össze a Framet.*/
		Game game = new Game();
		
		frame = new JFrame();
		
		/*Saját egérkurzor beállítása a framehez.*/
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Cursor cursor = toolkit.createCustomCursor(toolkit.getImage(""),new Point(0,0), "KissViktorCursor");
		frame.setUndecorated(true);
		frame.setCursor(cursor);
		frame.add(game);
		frame.setBounds(BoundXS,BoundYS, WIDTH, HEIGHT);
		
		frame.pack();
		frame.setResizable(true);
		frame.setVisible(true);
		//frame.setLocationRelativeTo(null);/*emiatt középre kerül az ablak,nem másjoz viszonyítja*/
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		game.start();
	}
}