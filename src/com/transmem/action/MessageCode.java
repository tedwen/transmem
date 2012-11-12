package com.transmem.action;

public interface MessageCode
{
	public static final int ERRCODE_APP = 999;

	public static final String ERR_NOT_LOGIN = "$ERR_NOT_LOGIN";	//user not login
	public static final String ERR_NO_PROJECT = "$ERR_NO_PROJECT"; //project not selected
	public static final String ERR_NO_ARTICLE = "$ERR_NO_ARTICLE";	//article not selected
	public static final String ERR_NO_PARAS = "$ERR_NO_PARAGRAPHS";	//no paragraphs in this article
	public static final String ERR_UNSUPPORTED_FILE_FORMAT = "$UNSUPPORTED_FILE_FORMAT";
	public static final String ERR_DB_CONNECT = "$ERR_DB_CONNECT";
	public static final String ERR_PROJECT_PARAMS = "$ERR_PROJECT_PARAMS"; //no projname or langpair
	public static final String ERR_NULL_PARAM = "$ERR_NULL_PARAM";	//param null or empty
	public static final String ERR_PARAM_FORMAT = "$PARAMETER_NUMBER_ERROR"; //param not number

	public static final String ERR_INVALID_USERPASS = "$ERR_INVALID_USERPASS";
	public static final String ERR_SERVER_FAULT = "$ERR_SERVER_FAULT";
	public static final String ERR_PROJECT_ID = "$ERR_INVALID_PROJECT_ID";
	public static final String ERR_PROJECT_QUERY = "$ERR_PROJECT_QUERY";
	public static final String ERR_NULL_ATTRIBUTE = "$ERR_NULL_ATTRIBUTE";
	public static final String ERR_INDEX_OUTOF_BOUNDS = "$INDEX_OUTOF_BOUNDS";
	public static final String ERR_INSERT_ARTICLE = "$INSERT_ARTICLE_FAILURE";
	public static final String ERR_INSERT_PROJECT = "$INSERT_PROJECT_FAILURE";
	public static final String ERR_DELETE_ARTICLE = "$DELETE_ARTICLE_FAILURE";
	public static final String ERR_DELETE_PROJECT = "$DELETE_PROJECT_FAILURE";
	public static final String ERR_INSERT_SENTENCE = "$INSERT_SENTENCE_FAILURE";
	public static final String ERR_DELETE_SENTENCE = "$DELETE_SENTENCE_FAILURE";
	public static final String ERR_UPDATE_SENTENCE = "$UPDATE_SENTENCE_FAILURE";
	public static final String ERR_CORPUS_INIT = "$CORPUS_INIT_FAILURE";
	public static final String ERR_NO_EXAMPLES = "$NO_EXAMPLES_FOUND";
	public static final String ERR_INVALID_LINKPAGE = "$INVALID_LINK_PAGE";
}
