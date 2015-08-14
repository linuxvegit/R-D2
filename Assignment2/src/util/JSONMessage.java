package util;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dto.Member;
import dto.Permission;

public class JSONMessage {

	public static final String SUCCESS_CODE = "0";

	public static final String ERROR_CODE = "-1";

	private String success;

	private String msg;

	private Object data;

	public JSONMessage() {

	}

	public JSONMessage(String success, String msg) {
		super();
		this.success = success;
		this.msg = msg;
	}

	public boolean isSuccess() {
		return success == SUCCESS_CODE;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public void setSuccessResult(String msg) {
		this.success = "0";
		this.msg = msg;
	}

	public void setFaildResult(String msg) {
		this.success = "-1";
		this.msg = msg;
	}

	public static JSONMessage genSuccessJsonMsg(String msg) {
		return genJsonMsg(SUCCESS_CODE, msg);
	}

	public static JSONMessage genSuccessJsonMsg() {
		return genSuccessJsonMsg("");
	}

	public static JSONMessage genErrorJsonMsg(String msg) {
		return genJsonMsg(ERROR_CODE, msg);
	}

	public static JSONMessage genErrorJsonMsg() {
		return genErrorJsonMsg("");
	}

	public static JSONMessage genJsonMsg(String code, String msg) {
		return new JSONMessage(code, msg);
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String toJSON() {
		StringBuffer json = new StringBuffer("{");
		if (data != null) {
			json.append("\"data\":");
			if (data instanceof List && (!((List) data).isEmpty())) {
				json.append("[");
				List<Member> list = (List<Member>) data;
				for (Member member : list) {
					json.append(member.toString());
					json.append(",");
				}
				json.setCharAt(json.length() - 1, ']');
			} else {
				json.append(data);
			}
			json.append(",");
		}

		json.append("\"msg\":\"" + msg + "\",");
		json.append("\"success\":" + success + "}");
		return json.toString();
	}

	public static void main(String[] args) {
		Member member = new Member(1, "First", "123456");
		member.setGender("M");
		member.setBirthday(new Date(1234567));
		member.setContact("15951754068");
		member.setAddress("China");
		member.setJoinDate(new Date());
		member.setAddition("This is my addition");
		List<Member> list = new ArrayList<Member>();
		list.add(Member.remakeMember(member, 1));
		for (int i = 2; i < 11; i++) {
			member = new Member(i, "name" + i, "123456");
			list.add(Member.remakeMember(member, 1));
		}
		JSONMessage msg = JSONMessage.genSuccessJsonMsg("Search: Success!");
		msg.setData(list);
		System.out.println(msg.toJSON());
	}
}
