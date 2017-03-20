package com.ywzlp.addfriend;

import javax.swing.UIManager;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import com.ywzlp.addfriend.ui.MyFrame;

/**
 * @author yuwei
 */
public class Application {

	public static void main(String[] args) {
		try {
			BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
	        org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
			UIManager.put("RootPane.setupButtonVisible", false);
			new MyFrame();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
