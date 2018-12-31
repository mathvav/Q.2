package net.karlshaffer.q.utility;

import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageUtility {

    public static byte[] prepareProfileImageForStorage(BufferedImage image) {

        int minDimension = Math.min(image.getHeight(), image.getWidth());

        int width = minDimension;
        int height = minDimension;

        BufferedImage croppedImage = Scalr.crop(image, (image.getWidth() - width) / 2, (image.getHeight() - height) / 2, width, height);
        BufferedImage scaledImage = Scalr.resize(croppedImage, 250);

        byte[] toReturn = null;

        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            ImageIO.write(scaledImage, "jpg", output);
            output.flush();

            toReturn = output.toByteArray();
        } catch (IOException ioe) {
            //TODO: Handle this somehow.
            System.out.println(ioe);
        }

        return toReturn;
    }

}
