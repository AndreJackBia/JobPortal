package com.jobportal.seekers;

import org.lightcouch.Document;

public class PdfDocument extends Document {
	
	private String id;
	
	public String get_id() {
		return id;
	}

	public void set_id(String _id) {
		this.id = _id;
	}
}
