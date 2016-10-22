package ass2.spec;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Random;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONObject;

// by Shin
public class MapGenerator {
    
    public static final String INPUT = "./tests/map.jpg";
    public static final String OUTPUT = "./tests/map.json";
    
    public static final int NUM_TREES = 20;
    
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
            
            Random rand = new Random();
            
            for (int i = 0; i < NUM_TREES; i++) {
            	JSONObject pts = new JSONObject();
            	
            	int x = rand.nextInt(image.getWidth()-3);
            	int z = rand.nextInt(image.getHeight()-3);
            	
            	pts.put("x", x);
            	pts.put("z", z);
            	obj.append("trees", pts);
            }
            
            
            bw.write(obj.toString(4));
            System.out.println("FINSHED.");
            bw.close();
            
        } catch (Exception e) {}
    }
    
}