package com.transmem.nlp;

public interface ILinguist
{
	public String[] segment(String sent) throws LanguageException;
	public String[] filter(String[] words) throws LanguageException;
	public String[] stem(String[] words) throws LanguageException;
	public String[] indexkeys(String sent) throws LanguageException;
}
