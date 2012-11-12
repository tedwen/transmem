package com.transmem.nlp;

public class LanguageException extends Exception
{
	private static final long serialVersionUID = 100;
	
	public static int NullPointerException = 1;

	private int code_;

	public LanguageException(String message)
	{
		super(message);
		this.code_ = 0;
	}

	public LanguageException(int code, String message)
	{
		super(message);
		this.code_ = code;
	}
	
	public int getCode()
	{
		return this.code_;
	}
}
