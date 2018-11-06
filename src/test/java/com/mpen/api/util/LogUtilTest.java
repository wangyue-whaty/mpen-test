package com.mpen.api.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mpen.TestBase;
import com.mpen.api.common.Constants;
import com.mpen.api.util.LogUtil.Detail;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LogUtilTest extends TestBase {

	@Test
	public void logTest() {
		final String[] headers = {
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36",
				"",
				"Mozilla/5.0 (Linux; U; Android 4.3; zh-cn; R8007 Build/JLS36C) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30",
				"Mozilla/5.0 (iPhone; CPU iPhone OS 9_3 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Mobile/13E230 WMall/3.0" };
		final String[] results = { "Windows", Constants.UNKNOW, "Android", "Mac OS X (iPhone)" };
		for (int i = 0; i < headers.length; i++) {
			final MockHttpServletRequest request = new MockHttpServletRequest();
			request.addHeader("user-version", headers[i]);
			final Detail detail = new LogUtil.Detail(request);
			Assert.assertEquals(detail.app.code, results[i]);
		}
	}
}
