package com.transmem.data.tm;

/**
 * Example
 *
 * A pair of sentences composed of a source-language sentence or phrase and a target-language translation.
 */
public class Example
{
	private	String	source_;
	private	String	target_;
	
	public Example(String src, String tgt)
	{
		this.source_ = src;
		this.target_ = tgt;
	}
	
	public String getSource()
	{
		return this.source_;
	}
	
	public void setSource(String src)
	{
		this.source_ = src;
	}
	
	public String getTarget()
	{
		return this.target_;
	}
	
	public void setTarget(String tgt)
	{
		this.target_ = tgt;
	}
}
