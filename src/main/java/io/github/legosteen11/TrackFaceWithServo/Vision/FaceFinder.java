package io.github.legosteen11.TrackFaceWithServo.Vision;

import io.github.legosteen11.TrackFaceWithServo.Main;
import jjil.algorithm.Gray8Rgb;
import jjil.algorithm.RgbAvgGray;
import jjil.core.Image;
import jjil.core.Rect;
import jjil.core.RgbImage;
import jjil.j2se.RgbImageJ2se;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wouter on 29-12-16.
 */
public class FaceFinder {
    private int minScale;
    private int maxScale;

    /**
     * Creates a new FaceFinder.
     * @param minScale I don't know what this does, just set it to 1 
     * @param maxScale I don't know what this does, just set it to 40 
     */
    public FaceFinder(int minScale, int maxScale) {
        this.minScale = minScale;
        this.maxScale = maxScale;
    }

    /**
     * Finds a face and returns the percentage the servo's should be set to.
     * @param bufferedImage BufferedImage to load
     * @return Returns an array with the percentages, the first item is X the second is Y.
     */
    public int[] findFaces(BufferedImage bufferedImage) {
        try {
            InputStream is = this.getClass().getClassLoader().getResource("faceDetectHaar.txt").openStream();
            Gray8DetectHaarMultiScale detectHaar = new Gray8DetectHaarMultiScale(is, minScale, maxScale);
            RgbImage im = RgbImageJ2se.toRgbImage(bufferedImage);
            RgbAvgGray toGray = new RgbAvgGray();
            toGray.push(im);

            List results = detectHaar.pushAndReturn(toGray.getFront());
            if (results.size() > 0) {
                Rect detectedFace = (Rect) results.get(0);
                //System.out.println("Found " + results.size() + " faces");
    
                int totalWidth = im.getWidth();
                int totalHeight = im.getHeight();
                //System.out.println("Total width of image is: " + totalWidth + ", total height is: " + totalHeight);
    
                int topLeftX = detectedFace.getTopLeft().getX();
                int topLeftY = detectedFace.getTopLeft().getY();
                int bottomRightX = detectedFace.getBottomRight().getX();
                int bottomRightY = detectedFace.getBottomRight().getY();
                //System.out.println("Top left x: " + topLeftX + ", top left y: " + topLeftY);
                //System.out.println("Bottom right x: " + bottomRightX + ", bottom right y: " + bottomRightY);
    
                int averageX = Math.round((bottomRightX + topLeftX) / 2);
                int averageY = Math.round((bottomRightY + topLeftY) / 2);
                //System.out.println("Average x of face is: " + averageX + ", average y of face is: " + averageY);
    
                int averageXPercentage = ((averageX * 100) / totalWidth);
                int averageYPercentage = ((averageY * 100) / totalHeight);
                //System.out.println("Average x position is on " + averageXPercentage + "%, average y position is on " + averageYPercentage + "%.");
    
                int servoHeightPercentage = 100 - averageYPercentage;
                //System.out.println("So that means that the height of the servo should be " + servoHeightPercentage + "%.");

                if(Main.isVerbose()) System.out.println("Found face at: " + servoHeightPercentage + "% height and " + averageXPercentage + "% width!");
    
                return new int[]{averageXPercentage, servoHeightPercentage};
            } else {
                return new int[]{-1,-1};
            }
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }
}
