package io.github.legosteen11.TrackFaceWithServo.Vision;

import io.github.legosteen11.TrackFaceWithServo.Main;
import jjil.algorithm.Gray8Rgb;
import jjil.algorithm.RgbAvgGray;
import jjil.core.Image;
import jjil.core.RgbImage;
import jjil.j2se.RgbImageJ2se;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * Created by wouter on 28-12-16.
 */
public class JjilTest {
    public static void findFaces(BufferedImage bi, int minScale, int maxScale, File output) {
        try {
            InputStream is  = new FileInputStream("/home/wouter/Pictures/faceDetectHaar.txt");
            Gray8DetectHaarMultiScale detectHaar = new Gray8DetectHaarMultiScale(is, minScale, maxScale);
            RgbImage im = RgbImageJ2se.toRgbImage(bi);
            RgbAvgGray toGray = new RgbAvgGray();
            toGray.push(im);
            List results = detectHaar.pushAndReturn(toGray.getFront());
            System.out.println("Found "+results.size()+" faces");
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
        BufferedImage bi = ImageIO.read(new File("/home/wouter/Pictures/testImage.jpg"));
        // onto following steps...
        findFaces(bi, 1, 40, new File("/home/wouter/Pictures/result.jpg")); // change as needed
    }
}
