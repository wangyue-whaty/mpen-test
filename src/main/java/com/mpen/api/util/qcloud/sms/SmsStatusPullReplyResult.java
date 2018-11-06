package com.mpen.api.util.qcloud.sms;

import java.util.ArrayList;

/**
 * 引自腾讯云，github地址：https://github.com/qcloudsms/qcloudsms 
 *
 */
public class SmsStatusPullReplyResult {
	public class Reply{
		String nationcode;
		String mobile;
		String text;
		String sign;
		long time;
		public String toString(){
			return String.format(
					"nationcode:%s\t"
					+"mobile:%s\t"
					+"text:%s\t"
					+"sign:%s\t"
					+"time:%d\n",
					nationcode, 
					mobile,
					text,
					sign,
					time
			  );
		}
	}
	
	int result;
	String errmsg;
	int count;
	ArrayList<Reply> replys;
	
	public String toString() {
			return String.format("SmsStatusReplyResult:\n"
					+"result:%d\n"
					+"errmsg:%s\n"
					+"count:%d\n"
					+"replys:%s\n",
					result, 
					errmsg,
					count,
					replys.toString()
		  );
	}
}

