package utilz;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;


public class LoadSave {
    
    public static final String PLAYER_ATLAS="adventurer_sprite_sheet.png";
    public static BufferedImage GetSpriteAtlas(String fileName){

        BufferedImage img= null;
        // InputStream is = LoadSave.class.getResourceAsStream("/res/"+ fileName); // import IMG
		try {
            img= ImageIO.read(LoadSave.class.getResourceAsStream("/"+ fileName));
		}	catch (IOException e ) {
			e.printStackTrace();
		} 
        finally {
			try {
				LoadSave.class.getResourceAsStream("/"+ fileName).close();
			} catch(IOException e ) {
				e.printStackTrace();
			}
		}		
        return img;
    }
}
