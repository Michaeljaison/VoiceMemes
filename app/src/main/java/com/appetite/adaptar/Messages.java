package com.appetite.adaptar;

/**
 * Message is a Custom Object to encapsulate message information/fields
 * 
 * @author Adil Soomro
 *
 */
public class Messages 
{
	String ID;
	String NAME;
	String URL;
	String ACTOR;
	
	public Messages(String id,String name,String actor)
	{
		super();
		ID = id;
		NAME = name;
		ACTOR=actor;
		URL = "http://offersforoffer.com/voicememes/memes/"+id+".mp3";
	}

	public String geturl()
	{
		return URL;
	}
	
	public String getname()
	{
		return NAME;
	}
	public String getactor()
	{
		return ACTOR;
	}
	public String getid()
	{
		return ID;
	}
}
