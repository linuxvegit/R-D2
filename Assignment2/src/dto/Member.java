package dto;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import util.MyUtil;

public class Member {
	private int id = 0;
	private String name = "Anonymous";
	private String password = null;
	private String gender = null;
	private Date birthday = null;
	private Integer age = 0;
	private String contact = null;
	private String address = null;
	private Date joinDate = null;
	private Integer joinDays = 0;
	private String addition = null;
	private Permission writable = new Permission(191);
	private Permission readable = new Permission(253);

	public Member(int id, String name, String password, Date joinDate) {
		this.id = id;
		if (name == null || name == "")
			name = "Anonymous";
		this.name = name;
		this.password = password;
		this.joinDate = joinDate;
		this.birthday = new Date(0);
	}

	public Member(int id, String name, String password) {
		this(id, name, password, new Date());
	}

	public Member(int id) {
		this(id, "Unknown", "1234");
	}

	public static Member makeMember(ResultSet results) throws IOException {
		try {
			Member member = new Member(results.getInt("id"));
			member.setName(results.getString("name"));
			member.setPassword(results.getString("password"));
			member.setGender(results.getString("gender"));
			member.setBirthday(results.getDate("birthday"));
			member.setContact(results.getString("contact"));
			member.setAddress(results.getString("address"));
			member.setJoinDate(results.getDate("joindate"));
			member.setAddition(results.getString("addition"));
			member.setWritable(new Permission(results.getInt("writable")));
			member.setReadable(new Permission(results.getInt("readable")));
			return member;
		} catch (SQLException e) {
			throw new IOException("Database Error Occurred.");
		}
	}

	public static Member remakeMember(Member member, int userId) {
		if (member.getId() == userId) {
			member.setAge(MyUtil.yearsToNow(member.getBirthday()));
			member.setJoinDays(MyUtil.daysToNow(member.joinDate));
			member.readable = null;
			return member;
		}
		if (!member.getReadable().check(Permission.NAME))
			member.setName(null);
		if (!member.getReadable().check(Permission.PASSWORD))
			member.setPassword(null);
		if (!member.getReadable().check(Permission.GENDER))
			member.setGender(null);
		if (!member.getReadable().check(Permission.BIRTHDAY))
			member.setBirthdayNull();
		if (!member.getReadable().check(Permission.BIRTHDAY))
			member.setAge(null);
		else
			member.setAge(MyUtil.yearsToNow(member.getBirthday()));
		if (!member.getReadable().check(Permission.CONTACT))
			member.setContact(null);
		if (!member.getReadable().check(Permission.ADDRESS))
			member.setAddress(null);
		if (!member.getReadable().check(Permission.JOINDATE))
			member.setJoinDate(null);
		if (!member.getReadable().check(Permission.JOINDATE))
			member.setJoinDays(null);
		else
			member.setJoinDays(MyUtil.daysToNow(member.joinDate));
		if (!member.getReadable().check(Permission.ADDITION))
			member.setAddition(null);
		member.readable = null;
		return member;
	}

	public static boolean filterMember(Member member,
			Map<String, Boolean> orders) {
		try {
			Field[] fields = member.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				String name = field.getName();
				name = name.toLowerCase();
				if (orders.containsKey(name)) {
					if (field.get(member) == null)
						return false;
				}
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name == null || name == "")
			name = "Anonymous";
		this.name = name;
	}

	public void setNameNull() {
		this.name = null;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		if (birthday == null)
			birthday = new Date();
		this.birthday = birthday;
	}

	public void setBirthdayNull() {
		this.birthday = null;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}

	public Integer getJoinDays() {
		return joinDays;
	}

	public void setJoinDays(Integer joinDays) {
		this.joinDays = joinDays;
	}

	public String getAddition() {
		return addition;
	}

	public void setAddition(String addition) {
		this.addition = addition;
	}

	public Permission getWritable() {
		return writable;
	}

	public void setWritable(Permission writable) {
		this.writable = writable;
	}

	public Permission getReadable() {
		return readable;
	}

	public void setReadable(Permission readable) {
		this.readable = readable;
	}

	public void correction() {
		name = MyUtil.transform(name);
		gender = MyUtil.transform(gender);
		contact = MyUtil.transform(contact);
		address = MyUtil.transform(address);
		addition = MyUtil.transform(addition);
	}

	@Override
	public String toString() {
		correction();
		return "{\"id\":" + id + ", \"name\":\"" + name + "\",\"password\":\""
				+ password + "\",\"gender\":\"" + gender + "\",\"birthday\":\""
				+ ((birthday == null) ? null : birthday.getTime())
				+ "\",\"age\":" + age + ",\"contact\":\"" + contact
				+ "\",\"address\":\"" + address + "\",\"joinDate\":\""
				+ ((joinDate == null) ? null : joinDate.getTime())
				+ "\",\"joinDays\":" + joinDays + ",\"addition\":\"" + addition
				+ "\",\"writable\":\"" + writable.toValue()
				+ "\",\"readable\":\"" + readable + "\"}";
	}

	public static void main(String[] args) {
		// System.out.println(Member.remakeMember(new Member(5), 2));
		Member member = new Member(100);
		member.setReadable(new Permission(0));
		member.setGender("F");
		member = Member.remakeMember(member, 1);
		System.out.println(member);
		Map<String, Boolean> map = new LinkedHashMap<String, Boolean>();
		map.put("JoinDays", true);
		System.out.println("result: " + Member.filterMember(member, map));
	}

}
