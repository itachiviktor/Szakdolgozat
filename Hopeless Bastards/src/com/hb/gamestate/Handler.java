package com.hb.gamestate;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hb.Camera;
import com.hb.Game;
import com.hb.Id;
import com.hb.entity.DamagingText;
import com.hb.entity.Entity;
import com.hb.entity.EntityList;
import com.hb.entity.Player;
import com.hb.entity.ServerPlayerList;
import com.hb.entity.TileList;
import com.hb.entity.Zombie;
import com.hb.gamestate.GameState;
import com.hb.graphics.ImageAssets;
import com.hb.items.HealthPotion;
import com.hb.items.ManaPotion;
import com.hb.tile.Tile;
import com.hb.tile.Wall;
import com.hb.tile.WallType;
import com.hg.muscleman.Muscleman;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Handler extends GameState {
	/*Ez az osztály egy játékállás, méghozzá a játéké.Tehát a karakterek itt mozogbak stb.....*/
	
	public Socket socket;
	
	public EntityList<Entity> entity = new EntityList<Entity>();/*Tartalmazza az összes mozgó entitást.*/
	public ServerPlayerList enemies = new ServerPlayerList();
	public ServerPlayerList friends = new ServerPlayerList();
	
	public TileList<Tile> tile = new TileList<Tile>();/*Tartalmazza az összes nem mozgó pályaelemet ?extends Tile bármi ami
	Tile leszármazottja*/
	public List<DamagingText> damagetext = new ArrayList<DamagingText>();
	
	private int width;
	private int height;
	
	private int cleanDeadBodies = 0;
	
	//private Random random;/*véletlen zombie generáláshoz kell*/
	
	public static Camera camera;/*ismeri a camerát, mert az követi a playert, és ennek is a handler hívja meg a render metódusát.*/

	public Player player = null;/*A Player nagyon fontos eleme a játéknak,õt irányítja a user, ezért õt kiemelten fontos
	külön ismernie a Handler osztálynak.*/
	
	
	public boolean leftClick;
	public boolean rightClick;
	
	public Handler(Game gsm) {
		super(gsm);
		init();
	}
	
	@Override
	public void init() {
		connectSocket();
		configSocketEvents();
		
		//random = new Random();
		/*a pálya elõállító metódus,ami azt a képet tartalmazza,amiben minden pixel egy pályaelem.*/
		createLevel(ImageAssets.image);
		camera = new Camera();

		/*Kiszedjük a Playert, mivel a createlevel metódusban a pályán a Player is fel van tüntetve ezért, már lérte van hozva.*/
		
	}
	
	public void render(Graphics g){
		/*A render metódus amit a fõ ciklus meghív.Ez fogja minden pályaelem render metódusát meghívni.
		 Elõször eltolja a képernyõt a camera objektum szerint úgy,hogy a player legyen középen.*/
		g.translate((int)camera.getX(),(int)camera.getY());
		
		/*Végigmegy a pályaelem listán és az összes elemenek meghívja a render metódusát(feltéve ,ha az benne van a kivetített
		 képernyõn,mert ha nincs ,akkor felesleges kirajzolni.)*/
		for(int i=0;i<tile.size();i++){
			Tile t = tile.get(i);
			if(getVisibleArea() != null && t.getBounds().intersects(getVisibleArea())){
				t.render(g);
			}
		}
		
		/*Végigmegy az entitás listán és az összes elemenek meghívja a render metódusát(feltéve ,ha az benne van a kivetített
		 képernyõn,mert ha nincs ,akkor felesleges kirajzolni.)*/
		for(int i=0;i<entity.size();i++){
			Entity e = entity.get(i);
			if(getVisibleArea() != null && e.getBounds().intersects(getVisibleArea())){
				e.render(g);
			}
		}
		
		for(int i=0;i<enemies.size();i++){
			Entity e = enemies.get(i);
			if(getVisibleArea() != null && e.getBounds().intersects(getVisibleArea())){
				e.render(g);
			}
		}
		
		for(int i=0;i<friends.size();i++){
			Entity e = friends.get(i);
			if(getVisibleArea() != null && e.getBounds().intersects(getVisibleArea())){
				e.render(g);
			}
		}
		
		for(int i=0;i<damagetext.size();i++){
			DamagingText t = damagetext.get(i);
			if(getVisibleArea() != null &&  getVisibleArea().contains(t.getCoord())){
				t.render(g);
			}
		}
		
		/*Itt kirajzolja az egeret, az õsosztályban definiált mouse Point tartalmazza.Ez az egér koordinátája.*/
		g.setColor(Color.black);
		g.fillRect(mouse.x, mouse.y, 1, 1);
		g.setColor(Color.RED);
		/*Az egér démonkéz kirajzolása.*/
		g.drawImage(ImageAssets.cursorsprite.getBufferedImage(), mouse.x, mouse.y,40,40,null);
		
	}
	
	public Rectangle getVisibleArea(){
		
		/*Ez azért kell,hogy ne rendereljük már mindent ki,csak azt ami a kivetített képernyõn lesz,ez kevés megjelenítendõ
		 dolognál nem szükséges,de ha sok minden van a képernyõn,akkor nélkülözhetetlen.*/
		for(int i=0;i<entity.size();i++){
			Entity e = entity.get(i);
			if(e.getId() == Id.PLAYER){
				/*-5 azért kell,mert a képernyõn kívül 5 pixelt vastagon még kirajzolunk frissítünk.*/
				/*10 mivel kétoldalról 5 plussz pixel*/
				return new Rectangle((int)e.getX()-(Game.getFrameWidth()/2),(int)e.getY()-(Game.getFrameHeight()/2),Game.getFrameWidth(),Game.getFrameHeight());
			}
		}
		return null;
		
	}
	
	public void tick(){
		/*A tick metódus ami minden pályaelem tick metódusát meghívja,ezt a tick metódust fogja meghívni a fõ ciklus.*/
		
		/*60 tick meghívódás után eltüntetjük a holttesteket a pályáról.*/
		if(cleanDeadBodies == 60){
			cleanDeadBodies = 0;
			/*A halott entitás akkor tûnik el,ha a holtteste eltûnik*/
			for(int i=0;i<entity.size();i++){
				if(entity.get(i).isDead()){
					addTile(new HealthPotion((int)entity.get(i).x, (int)entity.get(i).y, 32, 32,this,"buffer",1,ImageAssets.healthpotion.getBufferedImage()));
					entity.remove(entity.get(i));
				}
			}
		}
		cleanDeadBodies++;
		
		/*Meghívja az összes entitás tick metódusát.*/
		for(int i=0;i<entity.size();i++){
			entity.get(i).tick();
		}
		
		for(int i=0;i<enemies.size();i++){
			enemies.get(i).tick();
		}
		
		for(int i=0;i<friends.size();i++){
			friends.get(i).tick();
		}
		
		/*Meghívja az összes pályaelem tick metódusát,feltéve ha azok a képernyõn vannak, ezt az entitásonál azért
		 nem lehet megtenni,mert akkor a képernyõn kívül lévõ entitások nem mozognának(hisz a tick metódus mozgat), és az 
		 gáz lenne.*/
		for(int i=0;i<tile.size();i++){
			Tile t = tile.get(i);
			if(getVisibleArea() != null && t.getBounds().intersects(getVisibleArea())){
				t.tick();
			}	
		}
		
		for(int i=0;i<damagetext.size();i++){
			DamagingText t = damagetext.get(i);
			if(getVisibleArea() != null &&  getVisibleArea().contains(t.getCoord())){
				t.tick();;
			}
		}
		
		/*Camera tick metódusának meghívása.*/
		if(player != null){
			camera.tick(player);
			/*Az egér Point meghatározása.Azért kell a getVisibleAreat hozzáadni,mert a moseMovedX az az x érték,ahova az egér a
			 látható monitoron mozgott,viszont az nem biztos,hogy a player már nem mozdult el pl jobbra 2000 pixelt, ezért
			 el kell mozgatnunk annyival,hogy utolérje a kivetített képet.*/
			
			mouse = new Point(mouseMovedX + getVisibleArea().x,mouseMovedY + getVisibleArea().y);
		}
		
		
		
	}
	
	public void addEntity(Entity e){
		entity.add(e);
	}
	
	public void removeEntity(Entity e){
		entity.remove(e);
	}
	
	public void addEnemy(Player e){
		enemies.add(e);
	}
	
	public void removeEnemy(Player e){
		enemies.remove(e);
	}
	
	public void addFriend(Player e){
		friends.add(e);
	}
	
	public void removeFriend(Player e){
		friends.remove(e);
	}
	
	public void addTile(Tile e){
		tile.add(e);
	}
	
	
	public void removeTile(Tile e){
		tile.remove(e);
	}
	
	public void clearLevel(){
		entity.clear();
		tile.clear();
	}
	
	private void entityTrade(){
		/*Ez a metódus arra szolgál,hogy a lista végére tegye a Player objektumot,ami azért fontos,hogy a kirajzolásnál õt rajzolja
		 ki utoljára, ami azért fontos,hogy ne fedje el õt semmi.*/
		Entity trade = null;
		for(int i=0;i<entity.size();i++){
			if(entity.get(i).id == Id.PLAYER){
				trade = entity.get(i);
			}
		}
		
		int firstindex = entity.indexOf(trade);
		int lastindex = entity.size()-1;
		entity.set(firstindex, entity.get(lastindex));
		entity.set(lastindex, trade);
	}
	
	
	public void createLevel(BufferedImage level){
		
		/*Ez a metódus rakja össze egy kép alapján a pályát, tölti fel a listákat(entity,tile)*/
		this.width = level.getWidth();
		this.height = level.getHeight();
		
		for(int y = 0;y<height;y++){
			for(int x=0;x<width;x++){
				int pixel = level.getRGB(x,y);
	
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;
				
			
				if(red == 10 && green == 0 && blue == 0 ){
					/*fekete pixel*/
					addTile(new Wall(x*64,y*64,64,64,true,Id.WALL,WallType.ALSO,this));
				}
				else if(red == 20 && green == 0 && blue == 0 ){
					/*fekete pixel*/
					addTile(new Wall(x*64,y*64,64,64,true,Id.WALL,WallType.ALULROLKILEP,this));
				}
				else if(red == 30 && green == 0 && blue == 0 ){
					/*fekete pixel*/
					addTile(new Wall(x*64,y*64,64,64,true,Id.WALL,WallType.BAL,this));
				}
				else if(red == 40 && green == 0 && blue == 0 ){
					/*fekete pixel*/
					addTile(new Wall(x*64,y*64,64,64,true,Id.WALL,WallType.BALALSO,this));
				}
				else if(red == 50 && green == 0 && blue == 0 ){
					/*fekete pixel*/
					addTile(new Wall(x*64,y*64,64,64,true,Id.WALL,WallType.BALFELSO,this));
				}
				else if(red == 60 && green == 0 && blue == 0 ){
					/*fekete pixel*/
					addTile(new Wall(x*64,y*64,64,64,true,Id.WALL,WallType.BALLROLVEGE,this));
				}
				else if(red == 70 && green == 0 && blue == 0 ){
					/*fekete pixel*/
					addTile(new Wall(x*64,y*64,64,64,true,Id.WALL,WallType.BALROLKILEP,this));
				}
				else if(red == 80 && green == 0 && blue == 0 ){
					/*fekete pixel*/
					addTile(new Wall(x*64,y*64,64,64,true,Id.WALL,WallType.FELSO,this));
				}
				else if(red == 90 && green == 0 && blue == 0 ){
					/*fekete pixel*/
					addTile(new Wall(x*64,y*64,64,64,true,Id.WALL,WallType.FENTROLKILEP,this));
				}
				else if(red == 100 && green == 0 && blue == 0 ){
					/*fekete pixel*/
					addTile(new Wall(x*64,y*64,64,64,true,Id.WALL,WallType.FENTROLVEGE,this));
				}
				else if(red == 110 && green == 0 && blue == 0 ){
					addTile(new Wall(x*64,y*64,64,64,false,Id.WALL,WallType.FOLD,this));
				}
				else if(red == 120 && green == 0 && blue == 0 ){
					/*fekete pixel*/
					addTile(new Wall(x*64,y*64,64,64,true,Id.WALL,WallType.JOBB,this));
				}
				else if(red == 130 && green == 0 && blue == 0 ){
					/*fekete pixel*/
					addTile(new Wall(x*64,y*64,64,64,true,Id.WALL,WallType.JOBBALSO,this));
				}
				else if(red == 140 && green == 0 && blue == 0 ){
					/*fekete pixel*/
					addTile(new Wall(x*64,y*64,64,64,true,Id.WALL,WallType.JOBBFELSO,this));
				}
				
				else if(red == 255 && green == 0 && blue == 0){
					/*teljesen piros pixel*/
					addTile(new Wall(x*64,y*64,64,64,true,Id.WALL,WallType.JOBBROLKILEP,this));
				}
				else if(red == 255 && green == 119 && blue == 0){
					
					addTile(new Wall(x*64,y*64,64,64,true,Id.WALL,WallType.JOBBROLVEGE,this));
				}
				else if(red == 255 && green == 255 && blue == 0){
					/*sárga*/
					addTile(new Wall(x*64, y*64, 64, 64, true,Id.WALL, WallType.LENTROLVEGE, this));
					
				}
				else if(red == 255 && green == 0 && blue == 255){
					addTile(new Wall(x*64,y*64,64,64,false,Id.WALL,WallType.REPEDTFOLD,this));
			
				}
				
				else if(red == 100 && green == 100 && blue == 100){
					
				}
				
				else if(red == 200 && green == 200 && blue == 200){
					
				}
				
				else if(red == 0 && green == 0 && blue == 255){
					/*teljesen kék pixel*/
					
				}
				
			}
		}
		
		//addEnemy(new Muscleman(1500,1500,63,63,Id.ENEMYPLAYER,this));
		/*for(int i=0;i<25;i++){
			addEntity(new Zombie(i*68+1000, i*68+1000, 63, 63,Id.ZOMBIE,,this));
		}*/
		addTile(new ManaPotion(1200, 1300, 32, 32, this, "mana", 2, ImageAssets.manapotion.getBufferedImage()));
		
		//entityTrade();		
		/*for(int i=0;i<20;i++){
			
			addEntity(new FatMonster(i*68 + 4000, i*68 + 4000, 64, 64, Id.FATMONSTER,trade, this));
		}*/
		
	}
	
	private void configSocketEvents() {
		/*az elsõ a kapcsolódási eseményre reagál*/
		socket.on("socketID", new Emitter.Listener() {
			
			@Override
			public void call(Object... arg0) {
				JSONObject data = (JSONObject)arg0[0];
				try{
					//System.out.println("socketid");
					String id = data.getString("id");
					
					addEntity(new Muscleman(1600,1600,63,63,Id.PLAYER,id,Handler.this));
					addTile(new Wall(25*64,25*64,64,64,false,Id.WALL,WallType.FOLD,Handler.this));
					for(int i=0;i<entity.size();i++){
						if(entity.get(i).getId() == Id.PLAYER){
							player = (Player)entity.get(i);
						}
					}
					entityTrade();
					Entity trade = null;
					for(int i=0;i<entity.size();i++){
						if(entity.get(i).id == Id.PLAYER){
							trade = entity.get(i);
						}
					}
				}catch(JSONException e){
					e.getMessage();
				}
			}
		}).on("newPlayer", new Emitter.Listener() {
			
			@Override
			public void call(Object... arg0) {
				JSONObject data = (JSONObject)arg0[0];
				try{
					System.out.println("newPlayer");
					String playerId = data.getString("id");
					
					if(!playerId.equals(player.networkId)){
						enemies.add(new Muscleman(25*63,25*63, 63, 63, Id.ENEMYPLAYER, playerId, Handler.this));
					}
					
				}catch(JSONException e){
					e.getMessage();
				}
				
			}
		}).on("playerDisconnected", new Emitter.Listener() {
			
			@Override
			public void call(Object... arg0) {
				System.out.println("disconnect");
				JSONObject data = (JSONObject)arg0[0];
				try{
					String id = data.getString("id");
					/*id alapján töröljük a listából(nincs kész)*/
					enemies.removeById(id);
				}catch(JSONException e){
					e.getMessage();
				}
				
			}
		}).on("getPlayers", new Emitter.Listener() {
			
			@Override
			public void call(Object... arg0) {
				JSONArray objects = (JSONArray)arg0[0];
				
				try{
					for(int i=0;i<objects.length();i++){
						String id = objects.getJSONObject(i).getString("id");
						double x = objects.getJSONObject(i).getDouble("x");
						double y = objects.getJSONObject(i).getDouble("y");
						System.out.println("getPlayers");
						enemies.add(new Muscleman(x, y, 63, 63, Id.ENEMYPLAYER, id, Handler.this));
						
					}
				}catch(JSONException e){
					e.getMessage();
				}
				
			}
		}).on("playerMoved", new Emitter.Listener() {
			
			@Override
			public void call(Object... arg0) {
				JSONObject data = (JSONObject)arg0[0];
				
				try{
					String playerId = data.getString("id");
					
						if(enemies.getById(playerId) != null){
							Player entity = enemies.getById(playerId);
							entity.x = data.getDouble("x");
							entity.y = data.getDouble("y");
							entity.angle = data.getDouble("angle");
							entity.health = data.getInt("health");
							entity.maxHealth = data.getInt("maxhealth");
							entity.mana = data.getInt("mana");
							entity.maxMana = data.getInt("maxmana");
							entity.onegunman = data.getBoolean("onegunman");
							entity.twogunman = data.getBoolean("twogunman");
							entity.dead = data.getBoolean("dead");
							entity.live = data.getBoolean("live");
							entity.skill0started = data.getBoolean("skill0started");
							entity.skill1started = data.getBoolean("skill1started");
							entity.skill2started = data.getBoolean("skill2started");
							entity.skill3started = data.getBoolean("skill3started");
							entity.skill4started = data.getBoolean("skill4started");
							entity.skill5started = data.getBoolean("skill5started");
							entity.skill6started = data.getBoolean("skill6started");
							if(entity.skill0started){
								entity.skills[0].activateSkillByServer();
							}else if(entity.skill1started){
								entity.skills[1].activateSkillByServer();
							}else if(entity.skill2started){
								entity.skills[2].activateSkillByServer();
							}else if(entity.skill3started){
								entity.skills[3].activateSkillByServer();
							}else if(entity.skill4started){
								entity.skills[4].activateSkillByServer();
							}else if(entity.skill5started){
								entity.skills[5].activateSkillByServer();
							}else if(entity.skill6started){
								entity.skills[6].activateSkillByServer();
							}
						}
						
				}catch(JSONException e){
					e.getMessage();
				}
				
			}
		});
		
	}

	private void connectSocket() {
		try{
			socket = IO.socket("http://bastards.herokuapp.com/");
			//socket = IO.socket("http://localhost:8080");
			socket.connect();
		}catch(Exception e){
			e.getMessage();
		}
			
	}
	
	/*Az alábbiakban eseménykezelés történik.*/
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		/*A gomnyomásra a player tudja,hogy melyik skillt tolja el, ezért az õ metódusát hívom meg.*/
		if(player != null){
			player.MousePressed(e);
		}
		
		
		if(e.getButton() == MouseEvent.BUTTON1){
			leftClick = true;
			rightClick = false;
		}else if(e.getButton() == MouseEvent.BUTTON3){
			leftClick = false;
			rightClick = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		/*Itt megint csak a player tudja mit kell csinálni.*/
		if(player != null){
			player.MouseReleased(e.getButton());
		}
		
		
		if(e.getButton() == MouseEvent.BUTTON1){
			leftClick = false;
		}else if(e.getButton() == MouseEvent.BUTTON3){
			rightClick = false;
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseMovedX = e.getXOnScreen();
		mouseMovedY = e.getYOnScreen();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		/*Minden elmozdulásnál kell ,hogy hova mozdult az egér,ezzel majd a tick metódus mindíg dolgozik.*/
		mouseMovedX = e.getXOnScreen();
		mouseMovedY = e.getYOnScreen();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		/*player tudja mi történik*/
		if(player != null){
			player.keyPressed(e.getKeyCode());
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		/*player tudja mi történik*/
		if(player != null){
			player.keyReleased(e.getKeyCode());
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
}