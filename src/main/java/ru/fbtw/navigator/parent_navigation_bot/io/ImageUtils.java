package ru.fbtw.navigator.parent_navigation_bot.io;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;


public class ImageUtils {

	public static Image imageFromBase64(String base64Image) throws IOException {
		byte[] buffer = Base64.decodeBase64(base64Image);
		ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer);
		BufferedImage image = ImageIO.read(inputStream);


		return SwingFXUtils.toFXImage(image,null);
	}
}
