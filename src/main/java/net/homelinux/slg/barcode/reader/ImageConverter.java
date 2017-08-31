package net.homelinux.slg.barcode.reader;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

public class ImageConverter {

	public ImageConverter() {
	}

	/**
	 * Converts a PDF-file into a collection of BufferedImages <br>
	 * 
	 * @param
	 * @return List
	 */
	public List<BufferedImage> convert(String file) {
		try {
			File sourceFile = new File(file);
			List<BufferedImage> images = new ArrayList<>();

			if (sourceFile.exists()) {
				PDDocument document = PDDocument.load(file);
				@SuppressWarnings("unchecked")
				List<PDPage> list = document.getDocumentCatalog().getAllPages();

				for (PDPage page : list) {
					BufferedImage image = page.convertToImage();
					images.add(image);
				}
				document.close();

				return images;
			} else {
				return null;
			}

		} catch (Exception e) {
			return null;
		}
	}

	public BufferedImage rotate(BufferedImage bufferedImage) {
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.PI / 2, bufferedImage.getWidth() / 2, bufferedImage.getHeight() / 2);

		AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
		BufferedImage bufferedImageOut = op.filter(bufferedImage, null);

		return replaceColor(bufferedImageOut);
	}

	/**
	 * Replace transparent Color with black Color.<br>
	 * Transperency comes from rotation a non Quarad frame for 90°
	 * 
	 * @param
	 * @return BufferedImage
	 */
	private BufferedImage replaceColor(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage newImage = new BufferedImage(width, height, image.getType());
		int color;
		int black = -16777216;

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				color = image.getRGB(i, j);
				if ((color>>24) == 0x00 ) {
					newImage.setRGB(i, j, black);
				} else {
					newImage.setRGB(i, j, color);
				}
			}
		}

		return newImage;
	}

}
