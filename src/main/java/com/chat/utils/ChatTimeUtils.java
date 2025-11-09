package com.chat.utils;

public class ChatTimeUtils {

	@SuppressWarnings("unused")
	public String nvl(String val, String defaultval) {
		String result = val;
		if (val.trim().isEmpty()) {
			result = defaultval;
		} else if (val.trim().length() <= 0) {
			result = defaultval;
		} else if (val == null) {
			result = defaultval;
		}
		return result;
	}

	@SuppressWarnings("unused")
	public boolean cvl(String val) {
		boolean result = true;
		if (val.trim().isEmpty()) {
			result = false;
		} else if (val.length() <= 0) {
			result = false;
		} else if (val == null) {
			result = false;
		}

		return result;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
