package com.autopai.common.utils.common;

import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具集
 */
public class StringUtils {
	/**
	 * 字符串是否为空
	 * 
	 * @param str
	 * @return true 空
	 */
	public static boolean isEmpty(String str) {
		if (str == null || str.length() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 字符串trim后是否为空
	 * 
	 * @param str
	 * @return true 空
	 */
	public static boolean isEmptyByTrim(String str) {
		if (str == null || str.trim().length() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 验证手机号
	 * 
	 * @param mobile
	 * @return
	 */
	public static boolean isMobile(String mobile) {
		if (isEmptyByTrim(mobile)) {
			return false;
		}
		return Pattern.compile("^[1][3,4,5,8,7][0-9]{9}$").matcher(mobile).matches();
	}

	/**
	 * 验证邮箱
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		if (isEmptyByTrim(email)) {
			return false;
		}
		return Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*").matcher(email).matches();
	}

	/**
	 * 字符串转换,当为null的时候,返回""
	 * @param str
	 * @return
     */
	public static String getString(String str){
		if (isEmpty(str)){
			return "";
		}
		return str;
	}

	/**
	 * 从editText里获取对应的数据
	 * @param editText
	 * @return
	 */
	public static String getStringFromEditView(EditText editText){
		return editText.getText().toString().trim();
	}

	/**
	 * @param checkData
	 * 判断对象是否为空
	 * @return
	 */

	public static boolean isObjectDataNull(Object checkData) {
		if ("".equals(checkData) || checkData == null
				|| "null".equals(checkData) || checkData == null) {
			return true;
		}
		return false;
	}

	/**
	 * 判断两个对象是否相等
	 */
	public static boolean isTwoStringEqual(String aString, String bString) {
		if (aString.equals(bString) || bString == aString) {
			return true;
		}
		return false;
	}


	// check Url是否合法
	public static boolean checkUrl1(String url) {
		Pattern pattern = Pattern
				.compile("([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?",
						Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(url);
		return matcher.matches();
	}

	// check email
	public static boolean checkEmail(String emailText) {
		Pattern pattern = Pattern
				.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		Matcher matcher = pattern.matcher(emailText);
		return matcher.matches();
	}

	// check phone
	public static boolean checkPhoneNum(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * check the password: character _ number
	 * @return
	 */
	public static boolean checkPassword(String emailText) {
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9_]\\w{5,19}$");
		Matcher matcher = pattern.matcher(emailText);
		return matcher.matches();
	}


	// QQ处理
	public static boolean isNiceqq(String content) {
		return content.matches("[1-9]\\d{4,}");
	}


	public static boolean isVideo(String pInput) {
		// 文件名称为空的场合
		if (isObjectDataNull(pInput)) {
			// 返回不和合法
			return false;
		}
		// 获得文件后缀名
		String tmpName = pInput.substring(pInput.lastIndexOf(".") + 1,
				pInput.length());
		// 声明图片后缀名数组

		String imgeArray[] = { "3gp", "mov", "wmv", "asf", "rm", "rmvb", "mpg",
				"mpeg", "mpe", "dat", "vob", "dv", "wmv", "asf", "asx", "mov",
				"avi", "mkv", "mp4", "m4v", "flv" };
		// 遍历名称数组
		for (int i = 0, len = imgeArray.length; i < len; i++) {
			// 判断符合全部类型的场合
			if (imgeArray[i].equals(tmpName.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	/* byte[]转Int */
	public static int bytesToInt(byte[] bytes) {
		int addr = bytes[0] & 0xFF;
		addr |= ((bytes[1] << 8) & 0xFF00);
		addr |= ((bytes[2] << 16) & 0xFF0000);
		addr |= ((bytes[3] << 25) & 0xFF000000);
		return addr;

	}

	/* Int转byte[] */
	public static byte[] intToByte(int i) {
		byte[] abyte0 = new byte[4];
		abyte0[0] = (byte) (0xff & i);
		abyte0[1] = (byte) ((0xff00 & i) >> 8);
		abyte0[2] = (byte) ((0xff0000 & i) >> 16);
		abyte0[3] = (byte) ((0xff000000 & i) >> 24);
		return abyte0;
	}

	/**
	 * @param str
	 * @return
	 */
	/*public static String getRsaString(String str){
		String enRsaStr="";
		try {
			RSAUtil rsa = new RSAUtil();
			RSAPublicKey pubKey = rsa.getRSAPublicKey();
			RSAPrivateKey priKey = rsa.getRSAPrivateKey();
			byte[] enRsaBytes = rsa.encrypt(pubKey,str.getBytes("UTF-8"));
			enRsaStr = new String(enRsaBytes, "UTF-8");

//			content.getBytes("utf-8")；
			System.out.println("加密后==" + enRsaStr);
			System.out.println("解密后==" + new String(rsa.decrypt(priKey, rsa.encrypt(pubKey,str.getBytes()))));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return enRsaStr;
	}*/


}
