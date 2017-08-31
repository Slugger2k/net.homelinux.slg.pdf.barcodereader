package net.homelinux.slg.barcode.reader.dom;

import java.util.ArrayList;
import java.util.List;

public class BarCode {

	private String fileName;
	private List<String> errors = new ArrayList<>();
	private List<BarCodeDetails> barcodes = new ArrayList<>(); 
	
	public BarCode() {
	}


	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	public List<BarCodeDetails> getBarcodes() {
		return barcodes;
	}

	public void setBarcodes(List<BarCodeDetails> barcodes) {
		this.barcodes = barcodes;
	}

}
