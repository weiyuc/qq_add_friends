package com.ywzlp.addfriend.authc;

/**
 * 接口地址，由于webqq加好友的接口别屏蔽了，这里调用qq空间添加好友的接口
 * @author yuwei
 *
 */
public enum Apis {
	 	/**
	 	 * 获取登录二维码
	 	 */
	 	GET_QR_CODE(
	            "http://ptlogin2.qq.com/ptqrshow?appid=549000912&e=2&l=M&s=3&d=72&v=4&daid=5&t="
	    ),
	    /**
	     * 验证二维码
	     */
	    VERIFY_QR_CODE(
	            "http://ptlogin2.qq.com/ptqrlogin?u1=http://qzs.qq.com/qzone/v5/loginsucc.html?para=izone&ptqrtoken={1}&ptredirect=0&h=1&t=1&g=1&from_ui=1&ptlang=2052&action=0-0-1487334944739&js_ver=10196&js_type=1&login_sig={2}&pt_uistyle=40&aid=549000912&daid=5&"
	    ),
	    /**
	     * qq空间地址
	     */
	    QQ_ZONE(
	    		"http://user.qzone.qq.com/{1}"
	    ),
	    PINGD(
	    		"http://pinghot.qq.com/pingd?dm=v8friends.qzone.qq.com.hot&url=/{1}&tt=-&hottag=click_addqqfriends.click_ff_addsure.8&hotx=9999&hoty=9999&rand={2}"
	    ),
	    CODECGI(
	    		"http://c.isdspeed.qq.com/code.cgi?uin={1}&key=domain,cgi,type,code,time,rate&r={2}&1_1=w.qzone.qq.com&1_2=/cgi-bin/tfriend/friend_authfriend.cgi?qzfl&1_3=1&1_4=1&1_5=466&1_6=1&2_1=r.qzone.qq.com&2_2=/cgi-bin/tfriend/friend_getgroupinfo.cgi?qzfl&2_3=1&2_4=1&2_5=246&2_6=1"
	    ),
	    /**
	     * 添加好友接口
	     */
	    ADD_FRIEND(
	    		"http://w.qzone.qq.com/cgi-bin/tfriend/friend_addfriend.cgi?g_tk={1}"
	    ),
	    /**
	     * 获取好友列表接口
	     */
	    GET_FRIENDS(
	    		"https://h5.qzone.qq.com/proxy/domain/base.qzone.qq.com/cgi-bin/right/get_entryright.cgi?uin={1}&rd={2}&ver=1&fupdate=1&g_tk={3}"
	    );
	
	    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36";

	    private String url;

	    Apis(String url) {
	        this.url = url;
	    }

	    public String getUrl() {
	        return url;
	    }
	    
	    public String buildUrl(Object... params) {
	        int i = 1;
	        String url = this.url;
	        for (Object param : params) {
	            url = url.replace("{" + i++ + "}", param.toString());
	        }
	        return url;
	    }

}
