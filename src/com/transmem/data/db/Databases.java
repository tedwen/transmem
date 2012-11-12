package com.transmem.data.db;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Databases contains a list of DataSource references and provides
 * java.sql.Connection objects from the DataSource pool specified
 * by a category and read/write operation.
 * In this way, an action handler can get different connections from
 * different physical databases for reading and writing. For example,
 * a database can be used for updating user-related tables while
 * another is only used for reading (which can be a replica).
 * There can be multiple categories that separate different databases
 * for different uses, for example, the USER database can be different
 * from CORPUS database and they can be installed on different servers
 * for better performance and scalability.
 * The datasource objects are created during initiation of the Transmem
 * servlet and stored in the ServletContext as application-wide variable.
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jan. 2007
 */
public class Databases
{
	public static final String CATEGORY_MAIN	= "MAIN";
	public static final String CATEGORY_USER	= "USER";
	public static final String CATEGORY_CORPUS	= "CORPUS";

	private Logger log_ = Logger.getLogger(Databases.class.getName());

	private	Map<String, DataSource>	dsmap_;	///DataSource hash map for read-only databases
	private	Map<String, DataSource>	dswmap_;	///DataSource hash map for write-only databases

	public Databases()
	{
		log_.entering("Databases","Databases");
		dsmap_ = new HashMap<String, DataSource>();
		dswmap_ = new HashMap<String, DataSource>();
		log_.exiting("Databases","Databases");
	}

	/**
	 * Add a datasource to the read or write list with the category string as the key.
	 * Algorithm:
	 *	If WRITE is specified, then save it in write map; else in read map;
	 *	Multiple categories can be set, but must be separated by spaces. Any category string is
	 *	acceptable and converted to uppercase.
	 * @param ds - shared DataSource object
	 * @param cat - category string such as USER, CORPUS
	 * @param readwrite - a string containing words separated by spaces such as "READwrite".
	 */
	public void addDataSource(DataSource ds, String category, String readwrite)
	{
		log_.entering("Databases","addDataSource",category);
		String[] cats = category.toUpperCase().split(" ");
		readwrite = readwrite.toUpperCase();
		boolean forWrite = (readwrite.indexOf("WRITE")>=0);
		boolean forRead = (readwrite.indexOf("READ")>=0);
		for (int i=0; i<cats.length; i++)
		{
			String cat = cats[i].trim();
			if (cat.length()<1) continue;
			if (forWrite) {
				dswmap_.put(cat, ds);
				log_.info("DS '"+cat+"' forWrite put in dswmap");
			} 
			if (forRead) {
				dsmap_.put(cat, ds);
				log_.info("DS '"+cat+"' forRead put in dsmap");
			}
		}
		log_.exiting("Databases","addDataSource",category);
	}

	/**
	 * Get the java.sql.Connection from the DataSource maps according to 
	 * the specified category and read/write tag.
	 * Algorithm:
	 *	if forWrite then search write list for the category;
	 *	if not found then search read list, and if the category not found, use default MAIN category;
	 *	if for Readonly, then search read list, if not found by the given category, use MAIN category.
	 *
	 * @param cat - category string, can be USER, CORPUS, or others defined in web.xml
	 * @param forWrite - true if want updateable database and false for read-only.
	 * @return java.sql.Connection
	 */
	public Connection getConnection(String cat, boolean forWrite) throws SQLException
	{
		log_.entering("Databases","getConnection",cat);
		cat = cat.toUpperCase();
		if (forWrite)
			if (dswmap_.containsKey(cat)) {
				log_.info("Databases.getConnection("+cat+") return from dswmap_");
				return dswmap_.get(cat).getConnection();
			}
		if (dsmap_.containsKey(cat)) {
			log_.info("Databases.getConnection("+cat+") return from dsmap_");
			return dsmap_.get(cat).getConnection();
		} else {
			log_.info("Databases.getConnection("+cat+") return from dsmap_ as default");
			return dsmap_.get(CATEGORY_MAIN).getConnection();
		}
	}
	
	/**
	 * Get the Connection of a category for reading only.
	 * @param cat - category string
	 * @return java.sql.Connection
	 */
	public Connection getConnection(String cat) throws SQLException
	{
		return getConnection(cat, false);
	}
}
