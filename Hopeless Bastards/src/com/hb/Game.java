package com.hb;

import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JFrame;
import com.hb.gamestate.GameState;
import com.hb.gamestate.MenuState;
import com.hb.graphics.ImageAssets;


public class Game extends Canvas implements Runnable,PropertyChangeListener{

	private static final long serialVersionUID = 1L;
	public static int WIDTH;
	public static int HEIGHT;
	
	public int tick =0;
	public int render = 0;

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
	
	
	
	public Game() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();/*A képernyõ méretét kérem le*/
		HEIGHT = dim.height;
		WIDTH = dim.width;

		
		setPreferredSize(dim);
		setMaximumSize(dim);
		setMinimumSize(dim);
		
		states = new GameStateList();
		
		states.registToList(this);/*Property figyelõt beállítom,tehát ha a listába változás történik,ez értesül róla*/
		states.add(new MenuState(this));/*Alapértelmezetten a fõmenü játékállás jön be,ezért a listának elõször azt adjuk elmnek*/
		initListeners(states.get(0));/*Ez az osztály a Canvas leszármazott, ezért csak ez az osztály tud közvetlenül figyelõket
		beállítani rá.Viszont azt nem tudhatja,hogy a player mire mit csinál meg a gombok.Viszont a GameState az tudja, ezért
		a GameState osztályok implementálják az összes Listenert, és ezért az aktuális GameState átadva az initListeners osztálynak
		beállítja a Canvasra az õ figyelõit,tehát az fog történni kattintásra , stb.. amit abban a GameStateban definiáltam,hisz
		mindíg az aktuális GameState tudja mi történhet magával.*/
	}
	
	private void init(){
		/*Az init metódus annyit csinál,hogy az aktuális GameState init metódusát hívja meg*/
		states.get(0).init();
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		/*Ha változás történik a GameState Listában, akkor értesül ez az osztály és ez a metódus hívódik meg,
		 ez pedig azt csinálja,hogy a Canvashez az új GameState eseménykezelõit állítja be.*/
		if(evt.getPropertyName().equals("GameStateChange")){
			initListeners((GameState)evt.getNewValue());
		}
	}
	
	private void initListeners(GameState state){
		/*A Canvashez a paraméterben kapott GameState eseménykezelõit állítja be.*/
		addKeyListener(state);
		addMouseListener(state);
		addMouseMotionListener(state);
		addMouseWheelListener(state);
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
		
		
		BufferStrategy bs = getBufferStrategy();
		if(bs == null){
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		/*A fenti pás sor bufferelt kirajzolást enged,tehát amiíg vmi kirajzolódik a képernyõre addig a buffer tartalma is
		 rajzolódik,majd már csak be kell cserélni.3 bufferbõl dolgozunk.*/
		
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
		
		JFrame frame = new JFrame();
		
		/*Saját egérkurzor beállítása a framehez.*/
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Cursor cursor = toolkit.createCustomCursor(toolkit.getImage(""),new Point(0,0), "KissViktorCursor");
		frame.setUndecorated(true);
		frame.setCursor(cursor);
		frame.add(game);
		
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);/*emiatt középre kerül az ablak,nem másjoz viszonyítja*/
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.start();
	}
}