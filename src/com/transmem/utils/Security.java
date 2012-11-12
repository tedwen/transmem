package com.transmem.utils;

import java.security.MessageDigest;

public class Security
{
	/**
	 * Converts a byte to hex digit and writes to the supplied buffer
	 */
	public static void byte2hex(byte b, StringBuffer buf)
	{
		char[] hexChars = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
		int high = ((b & 0xf0) >> 4);
		int low = (b & 0x0f);
		buf.append(hexChars[high]);
		buf.append(hexChars[low]);
	}
	
	/**
	 * Converts a byte array to hex string
	 * @param bytes - bytes to convert to hex string
	 * @return string of hex digits
	 */
	public static String fromBytes(byte[] bytes)
	{
		int len = bytes.length;
		StringBuffer buf = new StringBuffer(len*2);
		for (int i=0; i<len; i++)
		{
			byte2hex(bytes[i], buf);
		}
		return buf.toString();
	}
	
	public static String md5(String str)
	{
		String result;
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] passbytes = md.digest(str.getBytes());
			result = fromBytes(passbytes);
		}
		catch (Exception x)
		{
			result = str;
			System.err.println(x.toString());
		}
		return result;
	}
	
	public static void main(String[] args)
	{
		if (args.length < 1)
		{
			System.out.println("Security <string>");
		} else {
			System.out.println(Security.md5(args[0]));
		}
	}
}
