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
	/*Ez a v�ltoz� a j�t�k ind�t�sa �ta eltelt m�sodpercet tartalmazza.Egy k�l�n sz�l m�sopercenk�nt eggyel n�veli az �rt�k�t.
	 Ez az id�z�t�s miatt nagyon fontos.*/
	
	private Thread thread;
	private boolean running = false;/*Sz�l ind�t�sn�l van szerepe.*/
	
	public static ImageAssets assets = new ImageAssets();/*Ez az objektum tartalmazza az �sszes bet�lt�tt k�pet,BufferedImaget,
	statikus v�ltoz�k�nt el�rhet�ek ezen statikus objektumon kereszt�l.*/
	
	
	public GameStateList states;/*Ez az oszt�ly l�nyeg�ben a GameStateManager,ami mind�g ebben a list�ban
	t�rolja az aktu�lis GameStatet,teh�t ennek a list�nak mind�g csak egy eleme van,az aktu�lis GameState.*/
	
	private OneSecTimer timer = new OneSecTimer();/*Ugye ebb�l a sz�lb�l ind�tom majd a m�sodpercsz�ml�l� sz�lat,ez�rt itt 
	inicializ�l�dik az objektuma.*/
	
	
	
	public Game() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();/*A k�perny� m�ret�t k�rem le*/
		HEIGHT = dim.height;
		WIDTH = dim.width;

		
		setPreferredSize(dim);
		setMaximumSize(dim);
		setMinimumSize(dim);
		
		states = new GameStateList();
		
		states.registToList(this);/*Property figyel�t be�ll�tom,teh�t ha a list�ba v�ltoz�s t�rt�nik,ez �rtes�l r�la*/
		states.add(new MenuState(this));/*Alap�rtelmezetten a f�men� j�t�k�ll�s j�n be,ez�rt a list�nak el�sz�r azt adjuk elmnek*/
		initListeners(states.get(0));/*Ez az oszt�ly a Canvas lesz�rmazott, ez�rt csak ez az oszt�ly tud k�zvetlen�l figyel�ket
		be�ll�tani r�.Viszont azt nem tudhatja,hogy a player mire mit csin�l meg a gombok.Viszont a GameState az tudja, ez�rt
		a GameState oszt�lyok implement�lj�k az �sszes Listenert, �s ez�rt az aktu�lis GameState �tadva az initListeners oszt�lynak
		be�ll�tja a Canvasra az � figyel�it,teh�t az fog t�rt�nni kattint�sra , stb.. amit abban a GameStateban defini�ltam,hisz
		mind�g az aktu�lis GameState tudja mi t�rt�nhet mag�val.*/
	}
	
	private void init(){
		/*Az init met�dus annyit csin�l,hogy az aktu�lis GameState init met�dus�t h�vja meg*/
		states.get(0).init();
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		/*Ha v�ltoz�s t�rt�nik a GameState List�ban, akkor �rtes�l ez az oszt�ly �s ez a met�dus h�v�dik meg,
		 ez pedig azt csin�lja,hogy a Canvashez az �j GameState esem�nykezel�it �ll�tja be.*/
		if(evt.getPropertyName().equals("GameStateChange")){
			initListeners((GameState)evt.getNewValue());
		}
	}
	
	private void initListeners(GameState state){
		/*A Canvashez a param�terben kapott GameState esem�nykezel�it �ll�tja be.*/
		addKeyListener(state);
		addMouseListener(state);
		addMouseMotionListener(state);
		addMouseWheelListener(state);
	}
	
	private synchronized void start(){
		/*Sz�l ind�t�sa*/
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
		/*A fenti p�s sor bufferelt kirajzol�st enged,teh�t ami�g vmi kirajzol�dik a k�perny�re addig a buffer tartalma is
		 rajzol�dik,majd m�r csak be kell cser�lni.3 bufferb�l dolgozunk.*/
		
		/*Az aktu�lis GameState render met�dus�t h�vjuk meg*/
		states.get(0).render(g);
		
		/*Az utols� k�t utas�t�s a bufferelt kirajzol�shoz sz�ks�ges.*/
		g.dispose();
		bs.show();
	}
	
	public void tick(){
		/*A tick met�dus az aktu�lis GameState tick met�dus�t h�vja meg.*/
		states.get(0).tick();
	}
	
	@Override
	public void run() {
	       boolean runFlag = true;
	       double delta = 0.016;
	      // init();
	       
	       	timer.start();
	       	
	        // convert the time to seconds
	        double nextTime = (double)System.nanoTime() / 1000000000.0;/*az id� m�sodpercben*/
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
	                // �breszt�s ellen�rz�s
	                if(sleepTime > 0)
	                {
	                    // sleep until the next update(k�vetkez� tick h�v�sik altatni a sz�lat)
	                    try
	                    {
	                        Thread.sleep(sleepTime);
	                    }
	                    catch(InterruptedException e)
	                    {
	                       //itt ne csin�ljon semmit
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
		
		/*A mainben rakom �ssze a Framet.*/
		Game game = new Game();
		
		JFrame frame = new JFrame();
		
		/*Saj�t eg�rkurzor be�ll�t�sa a framehez.*/
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Cursor cursor = toolkit.createCustomCursor(toolkit.getImage(""),new Point(0,0), "KissViktorCursor");
		frame.setUndecorated(true);
		frame.setCursor(cursor);
		frame.add(game);
		
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);/*emiatt k�z�pre ker�l az ablak,nem m�sjoz viszony�tja*/
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.start();
	}
}