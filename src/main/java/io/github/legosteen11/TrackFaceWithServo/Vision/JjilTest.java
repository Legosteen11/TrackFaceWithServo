package io.github.legosteen11.TrackFaceWithServo.Vision;

import jjil.algorithm.Gray8Rgb;
import jjil.algorithm.RgbAvgGray;
import jjil.core.Image;
import jjil.core.Rect;
import jjil.core.RgbImage;
import jjil.j2se.RgbImageJ2se;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * Created by wouter on 28-12-16.
 */
public class JjilTest {
    public void findFaces(BufferedImage bi, int minScale, int maxScale, File output) {
        try {
            InputStream is  = this.getClass().getClassLoader().getResource("faceDetectHaar.txt").openStream();
            Gray8DetectHaarMultiScale detectHaar = new Gray8DetectHaarMultiScale(is, minScale, maxScale);
            RgbImage im = RgbImageJ2se.toRgbImage(bi);
            RgbAvgGray toGray = new RgbAvgGray();
            toGray.push(im);
            
            List results = detectHaar.pushAndReturn(toGray.getFront());
            Rect detectedFace = (Rect) results.get(0);
            System.out.println("Found " + results.size() + " faces");
            
            int totalWidth = im.getWidth();
            int totalHeight = im.getHeight();
            System.out.println("Total width of image is: " + totalWidth + ", total height is: " + totalHeight);
            
            int topLeftX = detectedFace.getTopLeft().getX();
            int topLeftY = detectedFace.getTopLeft().getY();
            int bottomRightX = detectedFace.getBottomRight().getX();
            int bottomRightY = detectedFace.getBottomRight().getY();
            System.out.println("Top left x: " + topLeftX + ", top left y: " + topLeftY);
            System.out.println("Bottom right x: " + bottomRightX + ", bottom right y: " + bottomRightY);
            
            int averageX = Math.round((bottomRightX + topLeftX) / 2);
            int averageY = Math.round((bottomRightY + topLeftY) / 2);
            System.out.println("Average x of face is: " + averageX + ", average y of face is: " + averageY);
 
            int averageXPercentage = ((averageX * 100) / totalWidth);
            int averageYPercentage = ((averageY * 100) / totalHeight);
            System.out.println("Average x position is on " + averageXPercentage + "%, average y position is on " + averageYPercentage + "%.");
            
            int servoHeightPercentage = 100 - averageYPercentage;
            System.out.println("So that means that the height of the servo should be " + servoHeightPercentage + "%.");
            Image i = detectHaar.getFront();
            Gray8Rgb g2rgb = new Gray8Rgb();
            g2rgb.push(i);
            RgbImageJ2se conv = new RgbImageJ2se();
            conv.toFile((RgbImage)g2rgb.getFront(), output.getCanonicalPath());
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        // step #1 - read source image
        BufferedImage bi = ImageIO.read(new File("/home/wouter/Pictures/lul.jpg"));
        // onto following steps...
        new JjilTest().findFaces(bi, 1, 40, new File("/home/wouter/Pictures/result_new.jpg")); // change as needed
    }
}
