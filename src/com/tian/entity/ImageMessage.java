package com.tian.entity;


public class ImageMessage extends BaseMessage {
	private Image Image;

	public Image getImage() {
		return Image;
	}

	public void setImage(Image image) {
		this.Image = image;
	}
	public static Image imageMenu() {
		Image image = new Image();
		image.setMediaId("Nu_TxqEXN8uPcqvwPKhCZfKyQAIdY4BpyZpaYkMW3h6XR2Ao9bgFLUbUdTiN5JU2");
		return image;
	}
	
}
