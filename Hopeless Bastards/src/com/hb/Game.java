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
	
	/*Ez az al�bbi n�gy �rt�k �sszetartozik, az�rt kell bel�l�k 2x2, mert statikus met�dusban is
	 felszeretn�m haszn�lni az �rt�k�t.A bal fels� sarokhoz k�pest h�ny pixellel az x,y ir�nxba tol�dik el
	 a kijelz�.*/
	public int BoundX = 700;
	public int BoundY = 0;
	public static int BoundXS = 700;
	public static int BoundYS = 0;
	
	public String username;

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
	
	public String serverURL;
	private BufferStrategy bs;
	private Graphics g;
	
	
	
	public Game() {
		Wini ini;
		try {
			/*Az ini f�jl kezel�se, innen olvasom ki a szervert url-t �s t�bbi konstansokat. */
			ini = new Wini(new File("./config.ini"));
			serverURL = ini.get("Game", "serverURL", String.class);
	        boolean fullScreenMode = ini.get("Game", "fullScreenMode", boolean.class);

		} catch (InvalidFileFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();/*A k�perny� m�ret�t k�rem le*/
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
		
		states.registToList(this);/*Property figyel�t be�ll�tom,teh�t ha a list�ba v�ltoz�s t�rt�nik,ez �rtes�l r�la*/
		states.add(new LoginState(this));/*Alap�rtelmezetten a f�men� j�t�k�ll�s j�n be,ez�rt a list�nak el�sz�r azt adjuk elmnek*/
		initListeners(states.get(0));/*Ez az oszt�ly a Canvas lesz�rmazott, ez�rt csak ez az oszt�ly tud k�zvetlen�l figyel�ket
		be�ll�tani r�.Viszont azt nem tudhatja,hogy a player mire mit csin�l meg a gombok.Viszont a GameState az tudja, ez�rt
		a GameState oszt�lyok implement�lj�k az �sszes Listenert, �s ez�rt az aktu�lis GameState �tadva az initListeners oszt�lynak
		be�ll�tja a Canvasra az � figyel�it,teh�t az fog t�rt�nni kattint�sra , stb.. amit abban a GameStateban defini�ltam,hisz
		mindig az aktu�lis GameState tudja mi t�rt�nhet mag�val.*/
	}
	
	private void init(){
		/*Az init met�dus annyit csin�l,hogy az aktu�lis GameState init met�dus�t h�vja meg*/
		states.get(0).init();
		/*Ezt a met�dust nem haszn�ljuk, a Statek a konstruktorkban megh�vj�k az init met�dusukat.*/
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt){
		/*Ha v�ltoz�s t�rt�nik a GameState List�ban, akkor �rtes�l ez az oszt�ly �s ez a met�dus h�v�dik meg,
		 ez pedig azt csin�lja,hogy a Canvashez az �j GameState esem�nykezel�it �ll�tja be.*/
		if(evt.getPropertyName().equals("GameStateChange")){
			initListeners((GameState)evt.getNewValue(),(GameState)evt.getOldValue());
		}
	}
	
	/*Ezt nem haszn�lom.*/
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
		/*A Canvashez a param�terben kapott GameState esem�nykezel�it �ll�tja be.*/
		
		
		
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
	
	/*Ezt nem haszn�lom*/
	private void initListeners(GameState state){
		/*A Canvashez a param�terben kapott GameState esem�nykezel�it �ll�tja be.*/
		
		//addKeyListener(state);
		/*addMouseListener(state);
		addMouseMotionListener(state);
		addMouseWheelListener(state);*/
		
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
		
		
		bs = getBufferStrategy();
		if(bs == null){
			createBufferStrategy(3);
			return;
		}
		g = bs.getDrawGraphics();
		
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
		
		frame = new JFrame();
		
		/*Saj�t eg�rkurzor be�ll�t�sa a framehez.*/
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Cursor cursor = toolkit.createCustomCursor(toolkit.getImage(""),new Point(0,0), "KissViktorCursor");
		frame.setUndecorated(true);
		frame.setCursor(cursor);
		frame.add(game);
		frame.setBounds(BoundXS,BoundYS, WIDTH, HEIGHT);
		
		frame.pack();
		frame.setResizable(true);
		frame.setVisible(true);
		//frame.setLocationRelativeTo(null);/*emiatt k�z�pre ker�l az ablak,nem m�sjoz viszony�tja*/
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		game.start();
	}
}