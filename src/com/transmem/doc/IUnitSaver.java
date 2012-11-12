package com.transmem.doc;

/**
 * Interface for saving a translation unit (a pair of sentences from a source and a target language) into the database.
 * 
 * The interface implementer should prepare for saving when a start() method is received such as open a connection.
 * And clean up when a end() method is called. Properties can be set through the setProperty method, and whenever
 * a pair of sentence is ready for save, the saveUnit method is called.
 */
public interface IUnitSaver
{
	public void start();
	public void end();
	public void setProperty(String key, String value);
	public void saveUnit(String src, String dst) throws java.sql.SQLException;
}
