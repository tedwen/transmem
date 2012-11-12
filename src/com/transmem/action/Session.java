package com.transmem.action;

import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;

import com.transmem.data.db.*;
import com.transmem.data.tm.*;

/**
 * The Session class wraps the HttpSession object and provides easy manipulation
 * of session attributes for the action classes.
 * It is maintained by the ServletParams.
 */
public class Session
{
	public static final Logger log_ = Logger.getLogger(Session.class.getName());

	public static final String ERRORCODE = "errorcode";
	public static final String DATABASES = "databases";
	public static final String USERLANG = "UserLanguage";
	public static final String RESBUNDLE = "bundle";
	public static final String CORPUS = "corpus";
	public static final String USER = "user";
	public static final String TEMPUSER = "tuser";
	public static final String TEMPPASSWD = "temppasswd";
	public static final String PROJECTS = "projects";
	public static final String ENOUGHPROJECTS = "enoughprojects";
	public static final String SELECTED_PROJECT = "project";
	public static final String ARTICLES = "articles";
	public static final String ENOUGHARTICLES = "enougharticles";
	public static final String SELECTED_ARTICLE = "article";
	public static final String LANGPAIR = "langpair";
	public static final String PARAGRAPHS = "paragraphs";
	public static final String SELECTED_PARAGRAPH = "paragraph";
	public static final String THIS_PARAGRAPH = "thispara";
	public static final String NUM_PARAGRAPHS = "numparas";
	public static final String SENTENCES = "sentences";
	public static final String SELECTED_SENTENCE = "sentence";
	public static final String SENTENCEPAGES = "sentencepages";	//num of example sentences
	public static final String SENTENCEPAGE = "sentencepage";	//which example sentence
	public static final String TRANSLAYOUT = "translayout";
	public static final String DOMAINS = "domains";
	public static final String SELECTED_DOMAIN = "domain";
	public static final String PERMITS = "permits";	//currently only 'P','G','O'
	public static final String SELECTED_PERMIT = "permit";
	public static final String MYGROUP = "mygroup";
	public static final String MEMBERS = "members";
	public static final String GROUPLIST = "groups";
	public static final String GROUP_ORDER = "orderby";
	public static final String GROUP_OFFSET = "groupoffset";
	public static final String CORPUS_OP = "corpusop";
	public static final String CORPUS_TALLY = "corpustally";
	public static final String CORPUS_SOURCES = "corpussrcs";
	public static final String CORPUS_UNITS = "corpusunits";
	public static final String SOURCE_ID = "sourceid";

	private	HttpSession	session_;

	public Session(HttpSession session)
	{
		this.session_ = session;
	}
	
	public HttpSession getHttpSession()
	{
		return this.session_;
	}
	
	public Object getAttribute(String name)
	{
		return this.session_.getAttribute(name);
	}
	
	public void setAttribute(String name, Object value)
	{
		this.session_.setAttribute(name, value);
	}
	
	public void removeAttribute(String name)
	{
		this.session_.removeAttribute(name);
	}
	
	public void setMaxInactiveInterval(int idlesecs)
	{
		this.session_.setMaxInactiveInterval(idlesecs);
	}
	
	public void invalidate()
	{
		this.session_.invalidate();
	}
	
	public ServletContext getServletContext()
	{
		return this.session_.getServletContext();
	}

	public Corpus getCorpus()
	{
		return (Corpus)this.session_.getAttribute(CORPUS);
	}
	
	public void setCorpus(Corpus corpus)
	{
		this.session_.setAttribute(CORPUS, corpus);
	}
	
	public void removeCorpus()
	{
		this.session_.removeAttribute(CORPUS);
	}
	
	public String getUserLanguage()
	{
		return (String)this.session_.getAttribute(USERLANG);
	}
	
	public void setUserLanguage(String ulang)
	{
		this.session_.setAttribute(USERLANG, ulang);
	}
	
	public ResourceBundle getResourceBundle()
	{
		return (ResourceBundle)this.session_.getAttribute(RESBUNDLE);
	}
	
	public void setResourceBundle(ResourceBundle rb)
	{
		this.session_.setAttribute(RESBUNDLE,rb);
	}

	public void removeResourceBundle()
	{
		this.session_.removeAttribute(RESBUNDLE);
	}

	public Users getUser()
	{
		return (Users)this.session_.getAttribute(USER);
	}
	
	public void setUser(Users usr)
	{
		this.session_.setAttribute(USER, usr);
	}

	public Users getTempUser()
	{
		return (Users)this.session_.getAttribute(TEMPUSER);
	}

	public void setTempUser(Users tusr)
	{
		this.session_.setAttribute(TEMPUSER, tusr);
	}

	public void removeTempUser()
	{
		this.session_.removeAttribute(TEMPUSER);
	}
	
	public String getTempPassword()
	{
		return (String)this.session_.getAttribute(TEMPPASSWD);
	}
	public void setTempPassword(String pass)
	{
		this.session_.setAttribute(TEMPPASSWD, pass);
	}
	public void removeTempPassword()
	{
		this.session_.removeAttribute(TEMPPASSWD);
	}

	public void removeUser()
	{
		this.session_.removeAttribute(USER);
	}

	public ArrayList<Projects> getProjectList()
	{
		return (ArrayList<Projects>)this.session_.getAttribute(PROJECTS);
	}

	public void setProjectList(ArrayList<Projects> projects)
	{
		this.session_.setAttribute(PROJECTS, projects);
	}

	public void removeProjectList()
	{
		this.session_.removeAttribute(PROJECTS);
	}
	
	public String getEnoughProjects()
	{
		return (String)this.session_.getAttribute(ENOUGHPROJECTS);
	}
	public void setEnoughProjects()
	{
		this.session_.setAttribute(ENOUGHPROJECTS,"true");
	}
	public void removeEnoughProjects()
	{
		this.session_.removeAttribute(ENOUGHPROJECTS);
	}
	
	public Projects getSelectedProject()
	{
		return (Projects)this.session_.getAttribute(SELECTED_PROJECT);
	}
	
	public void setSelectedProject(Projects prj)
	{
		this.session_.setAttribute(SELECTED_PROJECT, prj);
	}
	
	public void unselectProject()
	{
		this.session_.removeAttribute(SELECTED_PROJECT);
	}

	public ArrayList<Articles> getArticleList()
	{
		return (ArrayList<Articles>)this.session_.getAttribute(ARTICLES);
	}

	public void setArticleList(ArrayList<Articles> articles)
	{
		this.session_.setAttribute(ARTICLES, articles);
	}

	public void removeArticleList()
	{
		this.session_.removeAttribute(ARTICLES);
	}
	
	public String getEnoughArticles()
	{
		return (String)this.session_.getAttribute(ENOUGHARTICLES);
	}
	public void setEnoughArticles()
	{
		this.session_.setAttribute(ENOUGHARTICLES,"true");
	}
	public void removeEnoughArticles()
	{
		this.session_.removeAttribute(ENOUGHARTICLES);
	}

	public Articles getSelectedArticle()
	{
		return (Articles)this.session_.getAttribute(SELECTED_ARTICLE);
	}
	
	public void setSelectedArticle(Articles ar)
	{
		this.session_.setAttribute(SELECTED_ARTICLE, ar);
	}
	
	public void unselectArticle()
	{
		this.session_.removeAttribute(SELECTED_ARTICLE);
	}
	
	public String getLangPair()
	{
		return (String)this.session_.getAttribute(LANGPAIR);
	}
	
	public void setLangPair(String langpair)
	{
		this.session_.setAttribute(LANGPAIR, langpair);
	}
	
	public void removeLangPair()
	{
		this.session_.removeAttribute(LANGPAIR);
	}

	public ArrayList<Paragraphs> getParagraphList()
	{
		return (ArrayList<Paragraphs>)this.session_.getAttribute(PARAGRAPHS);
	}

	public void setParagraphList(ArrayList<Paragraphs> paras)
	{
		this.session_.setAttribute(PARAGRAPHS, paras);
	}

	public void removeParagraphList()
	{
		this.session_.removeAttribute(PARAGRAPHS);
	}

	public Paragraphs getSelectedParagraph()
	{
		return (Paragraphs)this.session_.getAttribute(SELECTED_PARAGRAPH);
	}
	
	public void setSelectedParagraph(Paragraphs para)
	{
		this.session_.setAttribute(SELECTED_PARAGRAPH, para);
	}
	
	public void unselectParagraph()
	{
		this.session_.removeAttribute(SELECTED_PARAGRAPH);
	}
	
	public void setThisParagraph(String number)
	{
		this.session_.setAttribute(THIS_PARAGRAPH, number);
	}
	
	public void setNumParagraphs(String number)
	{
		this.session_.setAttribute(NUM_PARAGRAPHS, number);
	}

	public ArrayList<Sentences> getSentenceList()
	{
		return (ArrayList<Sentences>)this.session_.getAttribute(SENTENCES);
	}

	public void setSentenceList(ArrayList<Sentences> sents)
	{
		this.session_.setAttribute(SENTENCES, sents);
	}

	public void removeSentenceList()
	{
		this.session_.removeAttribute(SENTENCES);
	}
	
	public Sentences getSelectedSentence()
	{
		return (Sentences)this.session_.getAttribute(SELECTED_SENTENCE);
	}
	
	public void setSelectedSentence(Sentences sent)
	{
		this.session_.setAttribute(SELECTED_SENTENCE, sent);
	}
	
	public void unselectSentence()
	{
		this.session_.removeAttribute(SELECTED_SENTENCE);
	}
	
	public Integer getSentencePages()
	{
		return (Integer)this.session_.getAttribute(SENTENCEPAGES);
	}

	public void setSentencePages(Integer pages)
	{
		this.session_.setAttribute(SENTENCEPAGES,pages);
	}
	
	public Integer getSentencePage()
	{
		return (Integer)this.session_.getAttribute(SENTENCEPAGE);
	}

	public void setSentencePage(Integer page)
	{
		this.session_.setAttribute(SENTENCEPAGE,page);
	}

	public Integer getTranslateLayout()
	{
		return (Integer)this.session_.getAttribute(TRANSLAYOUT);
	}
	
	public void setTranslateLayout(Integer layout)
	{
		this.session_.setAttribute(TRANSLAYOUT, layout);
	}

	public ArrayList<String> getDomainList()
	{
		return (ArrayList<String>)this.session_.getAttribute(DOMAINS);
	}

	public void setDomainList(ArrayList<String> domains)
	{
		this.session_.setAttribute(DOMAINS, domains);
	}

	public String getSelectedDomain()
	{
		return (String)this.session_.getAttribute(SELECTED_DOMAIN);
	}

	public void setSelectedDomain(String domain)
	{
		this.session_.setAttribute(SELECTED_DOMAIN, domain);
	}

	public ArrayList<String> getPermitList()
	{
		return (ArrayList<String>)this.session_.getAttribute(PERMITS);
	}

	public void setPermitList(ArrayList<String> permits)
	{
		this.session_.setAttribute(PERMITS, permits);
	}

	public String getSelectedPermit()
	{
		return (String)this.session_.getAttribute(SELECTED_PERMIT);
	}

	public void setSelectedPermit(String permit)
	{
		this.session_.setAttribute(SELECTED_PERMIT, permit);
	}
	
	public Groups getMyGroup()
	{
		return (Groups)this.session_.getAttribute(MYGROUP);
	}

	public void setMyGroup(Groups mygroup)
	{
		this.session_.setAttribute(MYGROUP, mygroup);
	}
	
	public void removeMyGroup()
	{
		this.session_.removeAttribute(MYGROUP);
	}
	
	public ArrayList<Users> getMemberList()
	{
		return (ArrayList<Users>)this.session_.getAttribute(MEMBERS);
	}
	
	public void setMemberList(ArrayList<Users> members)
	{
		this.session_.setAttribute(MEMBERS, members);
	}
	
	public void removeMemberList()
	{
		this.session_.removeAttribute(MEMBERS);
	}
	
	public ArrayList<GroupRec> getGroupList()
	{
		return (ArrayList<GroupRec>)this.session_.getAttribute(GROUPLIST);
	}
	
	public void setGroupList(ArrayList<GroupRec> groups)
	{
		this.session_.setAttribute(GROUPLIST, groups);
	}
	
	public void removeGroupList()
	{
		this.session_.removeAttribute(GROUPLIST);
	}
	
	public String getGroupOrder()
	{
		return (String)this.session_.getAttribute(GROUP_ORDER);
	}
	
	public void setGroupOrder(String order)
	{
		this.session_.setAttribute(GROUP_ORDER, order);
	}
	
	public String getGroupOffset()
	{
		return (String)this.session_.getAttribute(GROUP_OFFSET);
	}
	
	public void setGroupOffset(String offset)
	{
		this.session_.setAttribute(GROUP_OFFSET, offset);
	}
	
	public String getCorpusOp()
	{
		return (String)this.session_.getAttribute(CORPUS_OP);
	}
	public void setCorpusOp(String op)
	{
		this.session_.setAttribute(CORPUS_OP, op);
	}

	public ArrayList<CorpusTally> getCorpusTally()
	{
		return (ArrayList<CorpusTally>)this.session_.getAttribute(CORPUS_TALLY);
	}
	
	public void setCorpusTally(ArrayList<CorpusTally> ct)
	{
		this.session_.setAttribute(CORPUS_TALLY, ct);
	}
	
	public void removeCorpusTally()
	{
		this.session_.removeAttribute(CORPUS_TALLY);
	}
	
	public ArrayList<Sources> getCorpusSourceList()
	{
		return (ArrayList<Sources>)this.session_.getAttribute(CORPUS_SOURCES);
	}

	public void setCorpusSourceList(ArrayList<Sources> cp)
	{
		this.session_.setAttribute(CORPUS_SOURCES, cp);
	}

	public void removeCorpusSourceList()
	{
		this.session_.removeAttribute(CORPUS_SOURCES);
	}

	public ArrayList<Transunit> getCorpusUnitList()
	{
		return (ArrayList<Transunit>)this.session_.getAttribute(CORPUS_UNITS);
	}
	
	public void setCorpusUnitList(ArrayList<Transunit> cp)
	{
		this.session_.setAttribute(CORPUS_UNITS, cp);
	}
	
	public void removeCorpusUnitList()
	{
		this.session_.removeAttribute(CORPUS_UNITS);
	}
	
	public String getSourceID()
	{
		return (String)this.session_.getAttribute(SOURCE_ID);
	}
	public void setSourceID(String sid)
	{
		this.session_.setAttribute(SOURCE_ID, sid);
	}
}
