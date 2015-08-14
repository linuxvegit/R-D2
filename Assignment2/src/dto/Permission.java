package dto;

import java.util.Arrays;
import java.util.BitSet;

public class Permission {
	private static final int LEN = 8;
	public static final int NAME = 0;
	public static final int PASSWORD = 1;
	public static final int GENDER = 2;
	public static final int BIRTHDAY = 3;
	public static final int CONTACT = 4;
	public static final int ADDRESS = 5;
	public static final int JOINDATE = 6;
	public static final int ADDITION = 7;

	private boolean[] set = new boolean[8];

	public Permission(int num) {
		for (int i = 0; i < LEN; i++) {
			set[i] = (num & (1 << i)) != 0;
		}
	}

	public Permission() {
		this(0);
	}

	public void set(int n) {
		if (n < 0 || n >= LEN)
			return;
		set[n] = true;
	}

	public void reset(int n) {
		if (n < 0 || n >= LEN)
			return;
		set[n] = false;
	}

	public boolean check(int n) {
		return set[n];
	}

	public int toValue() {
		int result = 0;
		for (int i = 0; i < LEN; i++) {
			result += (1 << i) * (set[i] ? 1 : 0);
		}
		return result;
	}

	@Override
	public String toString() {
		return "Permission [set=" + Arrays.toString(set) + "]";
	}

	public static void main(String[] args) {
		Permission p = new Permission(0);
		// p.set(Permission.NAME);
		// p.set(Permission.PASSWORD);
		System.out.println(p.check(Permission.NAME));
	}
}
