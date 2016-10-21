package ass2.spec;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import javax.imageio.ImageIO;

import org.json.JSONObject;

//	by Shin
public class MapGenerator {
    
    public static final String INPUT = "./tests/mapXL.png";
    public static final String OUTPUT = "./tests/mapXL.json";
    
    public static void main(String[] args) {
        MapGenerator map = new MapGenerator();
        map.makeTerrain();
    }
    
    public void makeTerrain() {

        try {
            
            System.out.println("STARTING");
            System.out.println("READING FILE...");
            
            File input = new File(INPUT);
            BufferedImage image = ImageIO.read(input);

            File output = new File(OUTPUT);
            
            if(!output.exists()) {
                output.createNewFile();
            }
            
            FileWriter fw = new FileWriter(output.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            
            double[] altitude = new double[image.getWidth()*image.getHeight()];
            
            int count = 0;
            for(int j = 0; j < image.getHeight(); j++) {
                for(int i = 0; i < image.getWidth(); i++) {
                    Color c = new Color(image.getRGB(i, j));
                    altitude[count] = c.getRed();
                    count++;
                 }
            }
            
            // PUT
            JSONObject obj = new JSONObject();
            
            obj.put("width", image.getWidth());
            obj.put("depth", image.getHeight());
            obj.put("sunlight", new int[] {0,1,1});         // temp doesn't actually use it yet
            obj.put("altitude", altitude);
            
            bw.write(obj.toString(4));
            System.out.println("FINSHED.");
            bw.close();
            
        } catch (Exception e) {}
    }
    
}