/**
 * @author christian_mueller '17
 * 
 *
 * Simple cli-tool for reading barcode from a PDF-file.<br>
 * Standart-Output format is JSON<br>
 * <br>
 * {"fileName":"test.pdf",<br>
 *  "errors":[],<br>
 *  "barcodes":[<br>
 *          {"code":"42091710","type":"CODE_128"},<br>
 *   		{"code":"1Z30V9856818022487","type":"CODE_128"}<br>
 *  ]}<br>
 * <br>
 * 
 */
package net.homelinux.slg.barcode.reader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.NotFoundException;

import net.homelinux.slg.barcode.reader.dom.BarCode;

public class Main {

	public static void main(String[] args) throws JsonProcessingException {
		Main m = new Main();
		ImageConverter con = new ImageConverter();
		BarCode code = new BarCode();

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

		String fileName = args[0];
		code.setFileName(fileName);

		if (new File(fileName).exists()) {
			List<BufferedImage> convert = con.convert(fileName);

			if (convert != null) {
				int page = 0;
				for (BufferedImage image : convert) {
					page++;
					try {
						code = m.read(image, code);
					} catch (NotFoundException e) {
						code.getErrors().add("No Barcode found in page " + page);
					}
				}
			} else {
				// Convert not work
				code.getErrors().add("Can not convert file");
			}
		} else {
			// File not exits
			code.getErrors().add("File not found");
		}

		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(code);
		System.out.println(jsonInString);
	}

	/**
	 * Read Barcode from BufferedImage<br>
	 * If no barcode is found, it trys to rotate the image 3 times 90° right and
	 * read again <br>
	 * 
	 * @param image
	 * @param barcode
	 * @return barcode
	 * @throws NotFoundException
	 */
	private BarCode read(BufferedImage image, BarCode barcode) throws NotFoundException {
		BarcodeReader reader = new BarcodeReader();

		try {
			barcode = reader.readBarcode(image, barcode);
		} catch (NotFoundException e) {
			BufferedImage rotate = new ImageConverter().rotate(image);
			try {
				barcode = reader.readBarcode(rotate, barcode);
			} catch (NotFoundException e1) {
				BufferedImage rotate2 = new ImageConverter().rotate(rotate);
				try {
					barcode = reader.readBarcode(rotate2, barcode);
				} catch (NotFoundException e2) {
					BufferedImage rotate3 = new ImageConverter().rotate(rotate2);
					try {
						barcode = reader.readBarcode(rotate3, barcode);
					} catch (NotFoundException e3) {
						throw e3;
					}
				}
			}
		}

		return barcode;
	}

}
