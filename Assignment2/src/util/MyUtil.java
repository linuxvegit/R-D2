package util;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import dto.Member;

public class MyUtil {
	private static String digitals = "1234567890zxcvbnmlkjhgfdsaqwertyuiopASDFGHJKLPMONIBUVYCTXRZEWQ";

	public static int yearsToNow(Date date) {
		if (date == null)
			return 0;
		Calendar now = Calendar.getInstance();
		Calendar then = Calendar.getInstance();
		then.setTime(date);
		return now.get(Calendar.YEAR) - then.get(Calendar.YEAR);
	}

	public static int daysToNow(Date date) {
		if (date == null)
			return 0;
		Date now = new Date();
		return (int) ((now.getTime() - date.getTime()) / (1000 * 60 * 60 * 24));
	}

	public static LinkedHashMap<String, Boolean> parseOrders(String str) {
		LinkedHashMap<String, Boolean> orders = new LinkedHashMap<String, Boolean>();
		if (str == null)
			return orders;
		if (str.trim() == "")
			return orders;
		String[] ordersStr = str.split(",");
		System.out.println(ordersStr.length);
		for (int i = 0; i < ordersStr.length; i++) {
			String[] order = ordersStr[i].split("=");
			String key = order[0].trim().toLowerCase();
			boolean value = (order[1].trim().toLowerCase().equals("asc")) ? true
					: false;

			if (key.equals("joindays")) {
				key = "joindate";
				value = !value;
			}
			if (key.equals("age")) {
				key = "birthday";
				value = !value;
			}
			if (orders.containsKey(key))
				continue;

			orders.put(key, value);
		}
		return orders;
	}

	public static String genPassword() {
		long time = new Date().getTime();
		Random random = new Random();
		int length = random.nextInt(6) + 6;
		StringBuffer password = new StringBuffer();
		for (int i = 0; i < length; i++) {
			password.append(digitals.charAt(random.nextInt(digitals.length())));
		}
		return password.toString();

	}

	public static String passwordHash(String password) {
		return MD5Util.MD5(password);
	}

	public static Date genDate(String str) {
		Date date = null;
		if (str == null)
			return null;
		try {
			date = new Date(Long.parseLong(str));
		} catch (NumberFormatException e) {
			return null;// TODO: handle exception
		}
		return date;
	}

	public static String genResult(String str) {
		return "{\"operation\":\"process\",\"data\":" + str + "}";
	}

	public static String transform(String s) {
		if (s == null)
			return null;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
			case '\"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '/':
				sb.append("\\/");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			default:
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		String s = "";
		System.out.println(parseOrders(s));
	}

}
