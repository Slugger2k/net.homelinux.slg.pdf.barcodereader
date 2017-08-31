package net.homelinux.slg.barcode.reader;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.homelinux.slg.barcode.reader.ImageConverter;

public class ImageConverterTest {

	private ImageConverter converter;

	@Before
	public void testInit() {
		String[] loggers = { "org.apache.pdfbox.util.PDFStreamEngine",
				"org.apache.pdfbox.util.operator.pagedrawer.Invoke",
				"org.apache.pdfbox.pdmodel.font.PDSimpleFont",
				"org.apache.pdfbox.pdmodel.font.PDFont",
				"org.apache.pdfbox.pdmodel.font.FontManager",
				"org.apache.pdfbox.pdfparser.PDFObjectStreamParser",
				"org.apache.fontbox.ttf.TTFSubFont" };

		for (String logger : loggers) {
			org.apache.log4j.Logger logpdfengine = org.apache.log4j.Logger.getLogger(logger);
			logpdfengine.setLevel(org.apache.log4j.Level.OFF);
		}
		converter = new ImageConverter();	
	}
	
	@Test
	public void testConvert() {
		List<BufferedImage> convert = converter.convert("test1.pdf");
		assertNotNull(convert);
		assertEquals(2, convert.size());
	}

	@Test
	public void testRotate() {
		List<BufferedImage> convert = converter.convert("test1.pdf");
		
		for (BufferedImage bufferedImage : convert) {
			BufferedImage rotate = converter.rotate(bufferedImage);
			assertNotNull(rotate);
		}
	}
}
