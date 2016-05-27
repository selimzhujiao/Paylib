package com.liumengfan.paylib;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WeiXinPayActivity extends Activity {

	private Button button1;
	private Button button2;
	private IWXAPI api;
	private  String url = "http://wxpay.weixin.qq.com/pub_v2/app/app_pay.php?plat=android";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.wei_xin_pay_layout);
		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		api = WXAPIFactory.createWXAPI(this, "wx6601ce8ae4bc2d4e");
		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				

				Toast.makeText(WeiXinPayActivity.this, "获取订单中...",
						Toast.LENGTH_SHORT).show();
				ASTY asy = new ASTY();
				asy.execute(url);
			}
		});
		button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
				Toast.makeText(WeiXinPayActivity.this,
						String.valueOf(isPaySupported), Toast.LENGTH_SHORT)
						.show();
			}
		});

		super.onCreate(savedInstanceState);
	}
	
	private class ASTY extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String content = null;
				byte[] buf = Util.httpGet(url);
				if (buf != null && buf.length > 0) {
					 content = new String(buf);
					
				}
			return content;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			String content = result;
			try {
			if (content != null) {
				Log.e("get server pay params:", content);
				JSONObject json = new JSONObject(content);
				if (null != json && !json.has("retcode")) {
					PayReq req = new PayReq();
					// req.appId = "wxf8b4f85f3a794e77"; //
					// 测试用appId
					req.appId = json.getString("appid");
					req.partnerId = json.getString("partnerid");
					req.prepayId = json.getString("prepayid");
					req.nonceStr = json.getString("noncestr");
					req.timeStamp = json.getString("timestamp");
					req.packageValue = json
							.getString("package");
					req.sign = json.getString("sign");
					req.extData = "app data"; // optional
					Toast.makeText(WeiXinPayActivity.this,
							"正常调起支付", Toast.LENGTH_SHORT)
							.show();
					// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
					api.sendReq(req);
				} else {
					Log.d("PAY_GET",
							"返回错误" + json.getString("retmsg"));
					Toast.makeText(WeiXinPayActivity.this,
							"返回错误" + json.getString("retmsg"),
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Log.d("PAY_GET", "服务器请求错误");
				Toast.makeText(WeiXinPayActivity.this,
						"服务器请求错误", Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			Log.e("PAY_GET", "异常：" + e.getMessage());
			Toast.makeText(WeiXinPayActivity.this,
					"异常：" + e.getMessage(), Toast.LENGTH_SHORT)
					.show();
		}
			
			super.onPostExecute(result);
		}
		
	}

}
