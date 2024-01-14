package Utilities;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import javax.imageio.ImageIO;


public class LoadSave {
	public static final String PLAYER_ATLAS = "charmander_sprites.png";
	public static final String LEVEL_ATLAS = "tileset.png";
	public static final String MENU_BUTTON = "button_atlas.png";
	public static final String MENU_BACKGROUND = "menu_background.png";
	public static final String PAUSE_BACKGROUND = "pause_menu.png";
	public static final String SOUND_BUTTONS = "sound_buttons.png";
	public static final String URM_BUTTONS = "urm_buttons.png";
	public static final String VOLUME_BUTTONS = "volume_buttons.png";
	public static final String START_MENU_BACKGROUND = "start_menu_background.png";
	public static final String PLAYING_BACKGROUND = "game_background.png";
	public static final String LARGE_CLOUD = "big_clouds.png";
	public static final String SMALL_CLOUD = "small_clouds.png";
	public static final String PAINTER_SPRITE = "smeargle_sprites.png";
	public static final String SKUNK_SPRITE = "skuntank_sprites.png";
	public static final String BOSS_SPRITE = "boss_sprites.png";
	public static final String STAT_BAR = "health_power_bar.png";
	public static final String COMPLETE_LEVEL = "game_complete_bg.png";
	public static final String POTION_ATLAS = "potions_sprites.png";
	public static final String CONTAINER_ATLAS = "objects_sprites.png";
	public static final String TRAP_ATLAS = "trap_atlas.png";
	public static final String SHOOTER_ATLAS = "shooter_atlas.png";
	public static final String SHOOTER_BALL =  "bullet.png";
	public static final String DEATH_SCREEN = "game-lost.png";
	public static final String OPTION_MENU = "option_bg.png";
	
	public static BufferedImage GetSpriteAtlas(String fileName) {
		
		BufferedImage img = null;
		
		InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
		try {
			img = ImageIO.read(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return img;
	}
	
	public static BufferedImage[] GetLevels() {
		URL url = LoadSave.class.getResource("/Levels");
		File file = null;
		try {
			file = new File(url.toURI());
		} catch (URISyntaxException e) {

			e.printStackTrace();
		}
		
		File[] files = file.listFiles();
		File[] filesSorted = new File[files.length];
		
		for(int i = 0; i < filesSorted.length; i++)
			for(int j = 0; j < files.length; j++) {
				if(files[j].getName().equals((i + 1)+ ".png"))
					filesSorted[i] = files[j];
			}
		
		BufferedImage[] imgs = new BufferedImage[filesSorted.length];
		
		for(int i = 0; i < imgs.length; i++)
			try {
				imgs[i] = ImageIO.read(filesSorted[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		return imgs;
	}

	
	
	
}
