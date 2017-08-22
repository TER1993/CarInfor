package com.speedata.carinfor.utils;

import android.app.ProgressDialog;
import android.content.Context;

import java.util.regex.Pattern;

public class ProgressDialogUtils {
	private static ProgressDialog mProgressDialog;

	/**
	 * 显示ProgressDialog
	 * @param context
	 * @param message
	 */
	public static void showProgressDialog(Context context, CharSequence message) {
		if (mProgressDialog == null) {
			mProgressDialog = ProgressDialog.show(context, "", message);
		} else {
			mProgressDialog.setMessage(message);
			mProgressDialog.show();
		}
	}

	/**
	 * 关闭ProgressDialog
	 */
	public static void dismissProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}

	/**
	 * 判断输入是否是数字
	 *
	 * @param str 输入
	 * @return true/false
	 */

	public static boolean isNumeric(String str) {

		Pattern pattern = Pattern.compile("[0-9]*");

		return pattern.matcher(str).matches();

	}
}
