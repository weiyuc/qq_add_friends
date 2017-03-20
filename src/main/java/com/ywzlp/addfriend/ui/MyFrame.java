package com.ywzlp.addfriend.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ywzlp.addfriend.authc.CallBack;
import com.ywzlp.addfriend.authc.QQSession;
import com.ywzlp.addfriend.task.AddFriends;
import com.ywzlp.addfriend.task.TaskExecutor;

/**
 * @author yuwei
 *
 */
public class MyFrame  extends JFrame {

	private static final long serialVersionUID = 6526808765902537684L;
	
	private static final Logger log = LoggerFactory.getLogger(MyFrame.class);
	
	private JFrame myFrame;
	private LogTextArea logTextArea;
	private JButton addAcctBtn;
	private JButton loginBtn;
	private JScrollPane scrollPane;
	
	private AddFriends addFriends;
	
	public MyFrame() {
		this.setSize(1000, 600);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setLayout(new BorderLayout());

		logTextArea = new LogTextArea();
		logTextArea.setRows(25);
		logTextArea.setColumns(100);
		
		scrollPane = new JScrollPane(logTextArea);
		JPanel header = new JPanel();
		header.setLayout(new GridLayout(0,2));
		
		addAcctBtn = new ChooseFileButton();
		addAcctBtn.setSize(new Dimension(300, 20));
		loginBtn = new JButton("登录账号");
		loginBtn.setSize(new Dimension(300, 20));
		loginBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new QQSession(myFrame, new CallBack() {
					@Override
					public void onLoginSuccess(QQSession session) {
						addFriends.submit(session);
					}
				});
			}
		});
		
		header.add(addAcctBtn, BorderLayout.NORTH);
		header.add(loginBtn, BorderLayout.SOUTH);
		this.add(header, BorderLayout.NORTH);
		this.add(scrollPane, BorderLayout.CENTER); 	
		this.setVisible(true);
		init();
	}
	
	private void init() {
		logVersion();
		log.info("正在初始化任务调度池...");
		addFriends = new TaskExecutor();
		log.info("任务调度池初始化完毕...");
	}
	
	private void logVersion() {
		log.info("-------------version: 1.0 for zlp------------");
		log.info("-------------author: 余伟 --------------------");
		log.info("-------------联系 : 15902773283---------------");
	}
	
}
