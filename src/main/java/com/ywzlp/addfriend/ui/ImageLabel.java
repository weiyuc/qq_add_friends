package com.ywzlp.addfriend.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * @author yuwei
 *
 */
public class ImageLabel extends javax.swing.JLabel {

	private static final long serialVersionUID = 1L;
	private Image image;
	private int imgWidth;
	private int imgHeight;
	
	public ImageLabel(String imgPath) {
		try {
			image = ImageIO.read(new FileInputStream(imgPath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setImgWidth(/*image.getWidth(this)*/200);
		setImgHeight(/*image.getHeight(this)*/200);
		Icon icon = new ImageIcon(image);
		setIcon(icon);
		setBackground(Color.BLACK);
	}

	public int getImgWidth() {
		return imgWidth;
	}

	public void setImgWidth(int imgWidth) {
		this.imgWidth = imgWidth;
	}

	public int getImgHeight() {
		return imgHeight;
	}

	public void setImgHeight(int imgHeight) {
		this.imgHeight = imgHeight;
	}

	@Override
	public void paintComponent(Graphics g) {
		if (null == image) {
			return;
		}
		g.drawImage(image, 30, 10, 150, 150, this);
	}
}
