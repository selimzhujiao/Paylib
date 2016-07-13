package com.liumengfan.paylib;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

public final class PayForMoneyUTIL extends com.fph.appnavigationlib.eventbus.EventType{

	// 商户PID
	public static final String PARTNER = "2088711803967262";
	// 商户收款账号
	public static final String SELLER = "service@fangpinhui.com";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMYtZcJ1Jspfq1AzYrEtcioNn5HmBnDiA4Yteoc/q3mk0prDcxCQbU8LJjC2a6MYQ13CNs7xOMtmvb+nyDH5WSLAbJon7OW6r4pBTq1HW6Oj416SvSXIOWayjNNjt0uTYqxIxZUJ4aIhTXznp0dBBNUoFdwEIxzDPIgPlkBJLyOtAgMBAAECgYEAhheBmyoZrSU+jW23gZr2fG3rYpT6FrdXGxr6pTj3lb9ooT9ORA71Z15r4NoNyrVIH9ChtTSS8U37Zgro8qd9H00X00gqafDpj26PQiRlWnTcMtFSVGD74+ZDiQ90U8CEhBI/xnnDZD3lmMRMqVHBAzDp1qDiqtL6BSCc9Ex8TQECQQDv9zdnEftGzm8+hOM2lRHQj8JgOU8F+1VHmCDQH6/NSCQmpPmxa6FR0MyJN4GpsVllsnCQI4Q48EWsLtd7MPJVAkEA02tckI9A79FmnTCvHnL7h1xu557S6AUMXwslppbOCmimNNskLlR8HYcuNdr6wiv3nV2aaCrxCwgu1kx0npqz+QJBANa77RII30kPdjEGDaVMZHK0Eoa1Aegibr/wfHyBvnn+sor6Wo7P974VENNBnoBRuePteBkkDzA8orxf9mfA9zkCQFBnWECx+mQ6IuhNB4xw9i+4e4Ui5drPcLEEsED2wU08F1mqrcvg3YLtUimxKgSGbBkw6/QJwLoNVqKxWPiopSECQQCYNRfATLVfRqGW+LvR2g3uxnqBzFqhhe9WkzsDhbGGMttRHrd5zjpjwIEWCF6JyOp/H3K3iADVKKzfCNO3Jmha";
	// 支付宝公钥
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCda0o2uh4PQr04M+wOfF+ry+KHjiGdcJyWC13yyz/L18fGKMEQfJ/xF7dkbB6cgRr/kQgL6TKnrkitDc6G/8OGBLJWfyxKsuSrt++PbHj9QCxzh/TUVvNEpUeJACD3d8cxodV0BhbdL8csgf00zFsll3KIRc0gRoWdOPlp30zb3wIDAQAB";
	private static final int SDK_PAY_FLAG = 1;

	public final static String TAG = "PayForMoney";
	

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */

	/**
	 * 原生的H5（手机网页版支付切natvie支付） 【对应页面网页支付按钮】
	 * 
	 * @param v
	 */
//	public void h5Pay(View v) {
//		Intent intent = new Intent(context, H5PayDemoActivity.class);
//		Bundle extras = new Bundle();
//		/**
//		 * url是测试的网站，在app内部打开页面是基于webview打开的，demo中的webview是H5PayDemoActivity，
//		 * demo中拦截url进行支付的逻辑是在H5PayDemoActivity中shouldOverrideUrlLoading方法实现，
//		 * 商户可以根据自己的需求来实现
//		 */
//		String url = "http://m.meituan.com";
//		// url可以是一号店或者美团等第三方的购物wap站点，在该网站的支付过程中，支付宝sdk完成拦截支付
//		extras.putString("url", url);
//		intent.putExtras(extras);
//		context.startActivity(intent);
//
//	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public static void getOrderInfo(String subject, String body, String price,
			String time, String OutTradeNo,final Activity activity,String notify_url) {
		
		String  notify_urls = notify_url;

		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + OutTradeNo + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + notify_urls
				+ "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
//		orderInfo += "&it_b_pay=\"30m\"";
		orderInfo += "&it_b_pay=\"+"+time+"\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";
		
		String sign = sign(orderInfo);
		/**
		 * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
		 */
		try {
			/**
			 * 仅需对sign 做URL编码
			 */
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		/**
		 * 完整的符合支付宝参数规范的订单信息
		 */
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(activity);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo, true);
				PayResult payResult = new PayResult(result);
			
				
				String resultInfo = payResult.getResult();// 同步返回需要验证的信息

				String resultStatus = payResult.getResultStatus();
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					activity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							
							Payresultevent.create(1,"支付成功").postEvent(TAG);;
						}
					});
					
//					Toast.makeText(context, "支付成功",
//							Toast.LENGTH_SHORT).show();
				} else {
					// 判断resultStatus 为非"9000"则代表可能支付失败
					// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						activity.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								
								Payresultevent.create(2,"支付结果确认中").postEvent(TAG);
							}
						});
//						Toast.makeText(context, "支付结果确认中",
//								Toast.LENGTH_SHORT).show();

					} else {
						activity.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								
								Payresultevent.create(0,"支付失败").postEvent(TAG);
							}
						});
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
//						Toast.makeText(context, "支付失败",
//								Toast.LENGTH_SHORT).show();

					}
				}
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	public static String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public static String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public static  String getSignType() {
		return "sign_type=\"RSA\"";
	}

}
