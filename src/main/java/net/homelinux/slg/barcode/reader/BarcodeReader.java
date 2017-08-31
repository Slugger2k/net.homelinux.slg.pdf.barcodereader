package net.homelinux.slg.barcode.reader;

import java.awt.image.BufferedImage;
import java.util.Hashtable;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.GenericMultipleBarcodeReader;
import com.google.zxing.oned.MultiFormatOneDReader;

import net.homelinux.slg.barcode.reader.dom.BarCode;
import net.homelinux.slg.barcode.reader.dom.BarCodeDetails;

public class BarcodeReader {
	/**
	 * Read a barcodes from a image<br>
	 * 
	 * @param image
	 * @param barcode
	 * @return barcode
	 * @throws NotFoundException
	 */
	public BarCode readBarcode(BufferedImage image, BarCode barcode) throws NotFoundException {
		BinaryBitmap bitmap = null;

		int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
		RGBLuminanceSource source = new RGBLuminanceSource(image.getWidth(), image.getHeight(), pixels);
		bitmap = new BinaryBitmap(new HybridBinarizer(source));

		Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
		hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);

		Reader multiFormatReader = new MultiFormatOneDReader(hints);
		GenericMultipleBarcodeReader reader = new GenericMultipleBarcodeReader(multiFormatReader);
		Result[] results = null;

		results = reader.decodeMultiple(bitmap, hints);

		if (results != null) {
			for (Result result : results) {
				BarCodeDetails bd = new BarCodeDetails();
				bd.setType(result.getBarcodeFormat().name());
				bd.setCode(result.getText());
				barcode.getBarcodes().add(bd);
			}
		}

		return barcode;
	}
}
