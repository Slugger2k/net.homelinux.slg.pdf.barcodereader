package net.homelinux.slg.barcode.reader;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.zxing.NotFoundException;

import net.homelinux.slg.barcode.reader.BarcodeReader;
import net.homelinux.slg.barcode.reader.ImageConverter;
import net.homelinux.slg.barcode.reader.dom.BarCode;

public class BarcodeReaderTest {

	private BarcodeReader reader;
	private ImageConverter converter;

	
	@Before
	public void init() {
		reader = new BarcodeReader();
		converter = new ImageConverter();

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
	}
	
	@Test
	public void testReadBarcode() throws NotFoundException {
		BarCode code = new BarCode();
		List<BufferedImage> convert = converter.convert("test1.pdf");
		
		for (BufferedImage bufferedImage : convert) {
			code = reader.readBarcode(bufferedImage, code);
			assertNotNull(code);
			assertEquals(code.getBarcodes().get(0).getCode(), "3014260115531");
		}
		
		assertTrue(code.getBarcodes().size() == 14);
	}

}
