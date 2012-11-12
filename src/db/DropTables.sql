---Warning: "F_Creator" not defined in table "T_Users" in query "select count(*) from T_Projects where F_Creator=?1"
---Warning: "F_Creator" not defined in table "T_Users" in query "select count(*) from T_Articles A,T_Projects P where P.F_Creator=?1 and A.F_Project=P.F_ProjectID"
---Warning: "F_Translator" not defined in table "T_Users" in query "select count(*) from T_Articles where F_Translator=?1"
---Warning: "F_GroupID" not defined in table "T_Users" in query "select F_GroupName from T_Groups where F_GroupID=?1"
---Warning: "F_Translator" not defined in table "T_Users" in query "select distinct F_LangPair from T_Articles where F_Translator=?1"
---Warning: "F_Translator" not defined in table "T_Projects" in query "select distinct P.* from T_Projects P left join T_Articles A on A.F_Translator=?1 and A.F_Project=P.F_ProjectID where P.F_Creator=?1 order by F_ProjectID"
---Warning: "F_Article" not defined in table "T_Articles" in query "select count(*) from T_Sentences where F_Article=?1"
---Warning: "F_Article" not defined in table "T_Articles" in query "select count(*) from T_Sentences where F_Article=?1 and F_Translation is not null"
---Warning: "F_Article" not defined in table "T_Articles" in query "update T_Articles set F_Sentences=(select count(*) from T_Sentences where F_Article=?1) where F_ArticleID=?1"
---Warning: "F_Progress" not defined in table "T_Sentences" in query "update T_Articles set F_Progress=?2 where F_ArticleID=?1"
---Warning: "F_ArticleID" not defined in table "T_Sentences" in query "update T_Articles set F_Progress=?2 where F_ArticleID=?1"
---Warning: check update T_Users set F_Group=0 where F_Group=?1
---Warning: check update T_Articles set F_Paragraph=?2 where F_ArticleID=?1
---Warning: check update T_Articles set F_Progress=?2 where F_ArticleID=?1
DROP TABLE T_Sentences;
DROP TABLE T_Paragraphs;
DROP TABLE T_Articles;
DROP TABLE T_Projects;
DROP TABLE T_FileFormats;
DROP TABLE T_Preferences;
DROP TABLE T_Users;
DROP TABLE T_Groups;
DROP TABLE T_Roles;
DROP TABLE T_ZHENX;
DROP TABLE T_ENZHX;
DROP TABLE T_ENZH;
DROP TABLE T_Sources;
DROP TABLE T_Corpora;
DROP TABLE T_Domains;
DROP TABLE T_Languages;
DROP TABLE T_LangCodes;
DROP SEQUENCE S_Corpora;
DROP SEQUENCE S_Sources;
DROP SEQUENCE S_ENZH;
DROP SEQUENCE S_Groups;
DROP SEQUENCE S_Users;
DROP SEQUENCE S_Preferences;
DROP SEQUENCE S_Projects;
DROP SEQUENCE S_Articles;
DROP SEQUENCE S_Paragraphs;
DROP SEQUENCE S_Sentences;
