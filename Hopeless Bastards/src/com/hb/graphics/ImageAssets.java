package com.hb.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageAssets {
	/*Ez az osztály tartalmazza az összes képre hivatkozó BufferedImage objektumot(statikus)*/
	
	private static SpriteSheet sheet32;
	private static SpriteSheet sheet128;
	private static SpriteSheet cursorspritesheet;
	private static SpriteSheet skills64;
	private static SpriteSheet skills128;
	private static SpriteSheet muscleulti192x192;
	private static SpriteSheet skillimages52x52px;
	
	public static Sprite wall[] = new Sprite[20];
	public static Sprite grass;
	public static Sprite[] player = new Sprite[11];
	public static Sprite box;
	public static Sprite water;
	public static Sprite flower;
	public static Sprite waterflower;
	public static Sprite waterplant;
	public static Sprite healthpotion;
	public static Sprite manapotion;
	public static Sprite earth;
	public static Sprite cursorsprite;
	public static Sprite[] greenfika = new Sprite[5];
	public static Sprite[] zombie = new Sprite[10];
	public static Sprite[] fatmonster = new Sprite[11];
	public static Sprite[] bleeding = new Sprite[6];
	public static Sprite[] guns = new Sprite[13];
	public static Sprite[] muscleman = new Sprite[7];
	public static Sprite[] musclemanpoewrbuff = new Sprite[10];
	public static Sprite[] gunfiredlight = new Sprite[3];
	public static Sprite[] shooterSmoke = new Sprite[10];
	public static Sprite[] muscleulti = new Sprite[30];
	public static Sprite[] musclemanskillimages = new Sprite[7];
	public static Sprite musclemantrap;
	public static Sprite[] musclemantrapfire = new Sprite[9];
	
	public static BufferedImage image;
	public static BufferedImage backGround;
	public static BufferedImage startGameNonclicked;
	public static BufferedImage startGameClicked;
	public static BufferedImage witch;
	public static BufferedImage bullet;
	public static BufferedImage[] bolt = new BufferedImage[10];
	public static BufferedImage[] firebolt = new BufferedImage[29];
	public static BufferedImage healthbar;
	public static BufferedImage health;
	public static BufferedImage mana;
	public static BufferedImage skillbar;
	public static BufferedImage skillwait;
	public static BufferedImage genos;
	
	
	
	public ImageAssets() {
		/*A kosntruktorban meghívom az init metódust , ami az összes BufferedImage és Sheet adattagnak értéket ad.
		 A Game fõ szál fog egy Imageassets objektumot létrehozni,és annak lesznek statikus publikus adattagjai amik álltal
		elérhetõ lesz az összes kép.Lényegében ennek az osztálynak semmi metódusa nincs.Csak tárolási osztály.*/
		init();
	}
	
	private void init(){
		sheet32 = new SpriteSheet("/spritesheet.png");
		sheet128 = new SpriteSheet("/spritesheet128.png");
		cursorspritesheet = new SpriteSheet("/cursor.png");
		skills64 = new SpriteSheet("/skills64px.png");
		skills128 = new SpriteSheet("/skills128px.png");
		muscleulti192x192 = new SpriteSheet("/muscleexplosion.png");
		skillimages52x52px = new SpriteSheet("/skillimages52x52.png");
		
		musclemantrap = new Sprite(sheet32,1,1,32,32);
		
		for(int i=0;i<musclemantrapfire.length;i++){
			musclemantrapfire[i] = new Sprite(skills64, i+1,13 , 64, 64);
		}
		
		int count = 0;
		for(int i=0;i<6;i++){
			for(int j=0;j<5;j++){
				muscleulti[count] = new Sprite(muscleulti192x192,j+1,i+1,192,192);
				count++;
			}
		}
		
		for(int i=0;i<7;i++){
			musclemanskillimages[i] = new Sprite(skillimages52x52px,i+1,1,52,52);
		}
		
		for(int i=0;i<10;i++){
			shooterSmoke[i] = new Sprite(skills128,i+1,2,128,128);
		}
		
		for(int i=0;i<3;i++){
			gunfiredlight[i] = new Sprite(skills64,i+1,12,64,64);
		}
		
		for(int i=0;i<10;i++){
			musclemanpoewrbuff[i] = new Sprite(skills128,i+1,1,128,128);
		}
		
		for(int i=0;i<13;i++){
			guns[i] = new Sprite(skills64, i+1, 10, 64,64);
		}
		
		for(int i=0;i<7;i++){
			muscleman[i] = new Sprite(skills64, i+1, 11, 64,64);
		}
		
		for(int i=0;i<6;i++){
			bleeding[i] = new Sprite(skills64, i+1, 8, 64,64);
		}
		for(int i=0;i<11;i++){
			fatmonster[i]= new Sprite(skills64,i+1,7,64,64);
		}
		
		for(int i=0;i<10;i++){
			zombie[i] = new Sprite(skills64,i+1,5,64,64);
		}
		
		
		 for(int i=0;i<20;i++){
			 wall[i] = new Sprite(skills64,i+1,9,64,64);
		 }
		 cursorsprite = new Sprite(cursorspritesheet, 1, 1, 50,50);
		 grass = new Sprite(sheet32,1,1,32,32);/*oszlop - sor*/ 
		 earth = new Sprite(sheet32,3,1,32,32);
		 box = new Sprite(sheet32,2,1,32,32);
		 healthpotion = new Sprite(skills64,1,1,64,64);
		 
		 water = new Sprite(sheet32,6,1,32,32);
		 flower = new Sprite(sheet32,9,1,32,32);
		 waterflower = new Sprite(sheet32,7,1,32,32);
		 waterplant = new Sprite(sheet32,8,1,32,32);
		 manapotion = new Sprite(skills64,2,1,64,64);
		 
		 for(int i=0;i<5;i++){
			 greenfika[i] = new Sprite(sheet128,i+1,2,128,128);
		 }
		 
		 for(int i=0;i<11;i++){
			 player[i] = new Sprite(skills64,i+1,6,64,64);
		 }
		 
		try {
			image = ImageIO.read(getClass().getResource("/level.png"));
			backGround = ImageIO.read(getClass().getResource("/mainMenuImage.jpg"));
			startGameNonclicked = ImageIO.read(getClass().getResource("/startGameNonclicked.png"));
			startGameClicked = ImageIO.read(getClass().getResource("/startGameClicked.png"));
			witch = ImageIO.read(getClass().getResource("/kid_shoot_0002.png"));
			bullet = ImageIO.read(getClass().getResource("/shotbullet.png"));
			healthbar = ImageIO.read(getClass().getResource("/healthbar.png"));
			health = ImageIO.read(getClass().getResource("/health.png"));
			mana = ImageIO.read(getClass().getResource("/mana.png"));
			skillbar = ImageIO.read(getClass().getResource("/skillbar.png"));
	
			skillwait = ImageIO.read(getClass().getResource("/skillwaiting.png"));
			genos = ImageIO.read(getClass().getResource("/genosos.png"));
		
			
			
			for(int i=0;i<10;i++){
				String path = "/bolt" + (i+1) + ".png";
				bolt[i] = ImageIO.read(getClass().getResource(path));
			}
			
			for(int i=0;i<29;i++){
				String path = "/flamethrower_00" + (i+1) + ".png";
				firebolt[i] = ImageIO.read(getClass().getResource(path));
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
