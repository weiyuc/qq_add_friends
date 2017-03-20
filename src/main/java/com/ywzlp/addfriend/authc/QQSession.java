package com.ywzlp.addfriend.authc;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.Dimension;
import java.io.File;
import java.util.Objects;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.ywzlp.addfriend.ui.ImageLabel;

import net.dongliu.requests.Client;
import net.dongliu.requests.HeadOnlyRequestBuilder;
import net.dongliu.requests.Response;
import net.dongliu.requests.Session;
import net.dongliu.requests.struct.Cookie;

/**
 * qq会话
 * 
 * @author yuwei
 *
 */
public class QQSession {

	private static final Logger LOGGER = LoggerFactory.getLogger(QQSession.class);
	
	private volatile boolean isStop;

	private Client client;

	private Session session;

	/**
	 * QR码的token
	 */
	private String qrsig;

	/**
	 * qq号
	 */
	private long uin;

	/**
	 * 生成token的key
	 */
	private String p_skey;

	/**
	 * token
	 */
	private int g_tk;
	
	private JFrame myFrame;
	
	private JDialog dialog;
	
	private CallBack callback;
	
	public QQSession(JFrame myFrame, CallBack callback) {
		this.client = Client.pooled().maxPerRoute(5).maxTotal(10).build();
		this.session = client.session();
		this.myFrame = myFrame;
		this.callback = callback;
		login();
	}
	
	/**
	 * 登录
	 */
	private void login() {
		//step 1
		getQRCode();
		
		//异步校验
		new Thread(new Runnable() {
			@Override
			public void run() {
				//step 2
				String url = verifyQRCode();
				//step 3
				if (url != null) {
					checkSig(url);
				}
			}
		}).start();
		dialog.setModalityType(ModalityType.APPLICATION_MODAL);
		dialog.setVisible(true);
	}

	/**
	 * 获取二维码
	 */
	private void getQRCode() {
		
		String filePath = System.getProperty("user.dir") + File.separator + "qrcode.png";
		
		Response<File> response = session.get(Apis.GET_QR_CODE.getUrl() + Math.random())
				.addHeader("User-Agent", Apis.USER_AGENT).file(filePath);

		for (Cookie cookie : response.getCookies()) {
			if (Objects.equals(cookie.getName(), "qrsig")) {
				qrsig = cookie.getValue();
				break;
			}
		}
		LOGGER.info("请打开手机QQ并扫描二维码");
		
		this.closeDialog();
		dialog = new JDialog(myFrame);
		dialog.setTitle("请打开手机QQ并扫描二维码");
		dialog.setSize(220, 200);
		dialog.setLocationRelativeTo(null);
		ImageLabel label = new ImageLabel(filePath);
		label.setPreferredSize(new Dimension(label.getImgWidth(), label.getImgHeight()));
		dialog.add(label, BorderLayout.CENTER);
		dialog.addWindowListener(new WindowListener() {

			@Override
			public void windowDeactivated(WindowEvent e) {
				LOGGER.debug("windowDeactivated");
			}

			@Override
			public void windowOpened(WindowEvent e) {
				LOGGER.debug("windowOpened");
			}

			@Override
			public void windowClosing(WindowEvent e) {
				QQSession.this.close();
			}

			@Override
			public void windowClosed(WindowEvent e) {
				LOGGER.debug("windowClosed");
			}

			@Override
			public void windowIconified(WindowEvent e) {
				LOGGER.debug("windowIconified");
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				LOGGER.debug("windowDeiconified");
			}

			@Override
			public void windowActivated(WindowEvent e) {
				LOGGER.debug("windowActivated");
			}
			
		});
	}

	private String verifyQRCode() {
		LOGGER.debug("等待扫描二维码");
		try {
			while (!isStop) {
				Thread.sleep(2000);
				Response<String> response = get(Apis.VERIFY_QR_CODE, hash33(qrsig), qrsig);
				String result = response.getBody();
				if (result.contains("成功")) {
					for (String content : result.split("','")) {
						if (content.startsWith("http")) {
							this.closeDialog();
							LOGGER.info("正在登录，请稍后");
							return content;
						}
					}
				} else if (result.contains("已失效")) {
					LOGGER.info("二维码已失效，尝试重新获取二维码");
					getQRCode();
				}
			}
		} catch (InterruptedException e) {
			//ignore
		}
		return null;
	}
	
	/**
	 * 校验
	 */
	private void checkSig(String url) {
		Response<String> response = session.get(url)
				.addHeader("User-Agent", Apis.USER_AGENT)
				.text();
		for (Cookie cookie : response.getCookies()) {
			if (cookie.getName().equals("uin")) {
				String p_uin = String.valueOf(cookie.getValue());
				uin = Long.valueOf(p_uin.substring(1, p_uin.length()));
			}
			if (cookie.getName().equals("p_skey")) {
				p_skey = cookie.getValue();
				g_tk = hash5381(p_skey);
			}
		}
		LOGGER.info(uin + " 登录成功");
		callback.onLoginSuccess(this);
	}
	
	public boolean addFriend(long friendQQ) {
		JSONObject formData = new JSONObject();
		formData.put("sid", 0);
		formData.put("ouin", friendQQ);
		formData.put("fuin", friendQQ);
		formData.put("uin", uin);
		formData.put("fupdate", 1);
		formData.put("rd", Math.random());
		//验证消息
		formData.put("strmsg", "熟人介绍");
		//组id
		formData.put("groupId", 1);
		formData.put("flag", 0);
		formData.put("chat", "");
		formData.put("key", "");
		formData.put("im", 0);
		formData.put("from", 8);
		formData.put("from_source", 8);
		String response = post(Apis.ADD_FRIEND, formData).getBody();
		
		if (response.contains("申请已发送")) {
			return true;
		} else if (response.contains("频率控制")) {
			LOGGER.warn("{} 号添加好友  {} 时受频率控制，好友将重返队列...", uin, friendQQ);
			return false;
		} else if (response.contains("服务器繁忙")) {
			LOGGER.warn("{} 号添加好友  {} 时服务器繁忙，好友将重返队列...", uin, friendQQ);
			return false;
		} else if (response.contains("操作过于频繁")) {
			LOGGER.warn("{} 号添加好友  {} 时操作过于频繁，好友将重返队列...", uin, friendQQ);
			return false;
		}
		LOGGER.warn(response);
		return true;
	}

	// 发送get请求
	public Response<String> get(Apis url, Object... params) {
		HeadOnlyRequestBuilder request = session.get(url.buildUrl(params))
				.addHeader("User-Agent", Apis.USER_AGENT);
		return request.text();
	}

	// 发送post请求
	public Response<String> post(Apis url, JSONObject r) {
		return session.post(url.buildUrl(g_tk))
				.addHeader("User-Agent", Apis.USER_AGENT)
				.addCookie("Loading", "Yes")
				.addCookie("QZ_FE_WEBP_SUPPORT", "1")
				.addCookie("__Q_w_s__QZN_TodoMsgCnt", "1")
				.addCookie("__Q_w_s_hat_seed", "1")
				.addCookie("cpu_performance_v8", "52")
				.forms(r)
				.text();
	}

	/**
	 * 生成ptqrtoken
	 */
	private int hash33(String s) {
		int e = 0, i = 0, n = s.length();
		for (; n > i; ++i)
			e += (e << 5) + s.charAt(i);
		return 2147483647 & e;
	}

	/**
	 * 生成g_tk
	 * 
	 * @return
	 */
	public int hash5381(String str) {
		int hash = 5381;
		for (int i = 0, len = str.length(); i < len; ++i) {
			hash += (hash << 5) + str.charAt(i);
		}
		return hash & 2147483647;
	}
	
	public void closeDialog() {
		if (dialog != null) {
			dialog.setVisible(false);
			dialog.dispose();
			dialog = null;
		}
	}
	
	public long getUid() {
		return this.uin;
	}

	public void close() {
		LOGGER.info("正在关闭 {} 连接...", uin);
		this.closeDialog();
		this.session = null;
		this.client = null;
		this.isStop = true;
	}
	
}
