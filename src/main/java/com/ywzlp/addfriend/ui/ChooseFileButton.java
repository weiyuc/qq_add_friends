package com.ywzlp.addfriend.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ywzlp.addfriend.task.TaskPool;

/**
 * @author yuwei
 *
 */
public class ChooseFileButton extends JButton {

	private static final Logger log = LoggerFactory.getLogger(ChooseFileButton.class);
	
	private static final long serialVersionUID = 4356824924777609710L;

	protected ChooseFileButton() {
		super("导入账号");
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser jfc = new JFileChooser();
				jfc.setCurrentDirectory(new File(System.getProperty("user.dir")));
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jfc.showDialog(new JLabel(), "选择");
				
				File file = jfc.getSelectedFile();
				if (file != null) {
					BufferedReader br = null;
					try {
						FileReader fr = new FileReader(file);
						br = new BufferedReader (fr);
						String line = null;
						//去重
						Set<Long> nums = new HashSet<>();
						while((line = br.readLine()) != null) {
							if (line.trim().length() > 0) {
								nums.add(Long.valueOf(line.trim()));
							}
						}
						if (nums.size() > 0) {
							TaskPool.submit(nums);
						}
					} catch (Exception e2) {
						log.info("导入文件[" + file.getName() + "]发生异常,请检查文件格式");
					} finally {
						if (br != null) {
							try {
								br.close();
							} catch (IOException e1) {
								//ignore
							}
						}
					}
				}
			}
		});
	}

}
