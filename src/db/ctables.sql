
CREATE TABLE T_LangCodes
(
	F_Code		CHAR(2)	NOT NULL,
	F_Name		VARCHAR(16),
	F_Family		VARCHAR(24),
	PRIMARY KEY (F_Code)
);
GRANT SELECT ON TABLE T_LangCodes TO GROUP "Users";
GRANT ALL ON TABLE T_LangCodes TO GROUP "Admins";
INSERT INTO T_LangCodes(F_Code,F_Name,F_Family) VALUES('EN','English','GERMANIC');
INSERT INTO T_LangCodes(F_Code,F_Name,F_Family) VALUES('ZH','Chinese','ASIAN');
INSERT INTO T_LangCodes(F_Code,F_Name,F_Family) VALUES('FR','French','ROMANCE');
INSERT INTO T_LangCodes(F_Code,F_Name,F_Family) VALUES('DE','German','GERMANIC');
INSERT INTO T_LangCodes(F_Code,F_Name,F_Family) VALUES('ES','Spanish','ROMANCE');
INSERT INTO T_LangCodes(F_Code,F_Name,F_Family) VALUES('IT','Italian','ROMANCE');
INSERT INTO T_LangCodes(F_Code,F_Name,F_Family) VALUES('JA','Japanese','ASIAN');

CREATE TABLE T_Languages
(
	F_PairID		CHAR(4)	NOT NULL,
	F_SourceCode		CHAR(2)	NOT NULL,
	F_TargetCode		CHAR(2)	NOT NULL,
	F_DisplayCode		VARCHAR(8)	NOT NULL,
	F_TableName		VARCHAR(31)	NOT NULL,
	F_Sentences		int8 DEFAULT 0,
	PRIMARY KEY (F_PairID),
	FOREIGN KEY (F_SourceCode) REFERENCES T_LangCodes (F_Code),
	FOREIGN KEY (F_TargetCode) REFERENCES T_LangCodes (F_Code)
);
GRANT SELECT ON TABLE T_Languages TO GROUP "Users";
GRANT ALL ON TABLE T_Languages TO GROUP "Admins";
INSERT INTO T_Languages(F_PairID,F_SourceCode,F_TargetCode,F_DisplayCode,F_TableName) VALUES('ENZH','EN','ZH','EN-ZH','T_ENZH');
INSERT INTO T_Languages(F_PairID,F_SourceCode,F_TargetCode,F_DisplayCode,F_TableName) VALUES('ZHEN','ZH','EN','ZH-EN','T_ENZH');

CREATE TABLE T_Domains
(
	F_DomainCode		CHAR(2)	NOT NULL,
	F_DomainName		VARCHAR(16),
	F_ParentDomain		CHAR(2),
	PRIMARY KEY (F_DomainCode)
);
GRANT SELECT ON TABLE T_Domains TO GROUP "Users";
GRANT ALL ON TABLE T_Domains TO GROUP "Admins";
INSERT INTO T_Domains(F_DomainCode,F_DomainName) VALUES('00','General');

CREATE SEQUENCE S_Corpora INCREMENT 1 MINVALUE 1 START 1;
GRANT SELECT,UPDATE ON SEQUENCE S_Corpora TO GROUP "Users";
GRANT ALL ON SEQUENCE S_Corpora TO GROUP "Admins";

CREATE TABLE T_Corpora
(
	F_CorpusID		int4	NOT NULL DEFAULT nextval('S_Corpora'),
	F_LangPair		CHAR(4)	NOT NULL,
	F_Domain		CHAR(2)	NOT NULL,
	F_Public		int4 DEFAULT 0,
	F_Group		int4 DEFAULT 0,
	F_Private		int4 DEFAULT 0,
	F_Total		int4 DEFAULT 0,
	PRIMARY KEY (F_CorpusID)
);
GRANT SELECT,UPDATE ON TABLE T_Corpora TO GROUP "Users";
GRANT ALL ON TABLE T_Corpora TO GROUP "Admins";
INSERT INTO T_Corpora(F_LangPair,F_Domain,F_Public,F_Group,F_Private,F_Total) VALUES('ENZH','00',100,50,10,160);
INSERT INTO T_Corpora(F_LangPair,F_Domain,F_Public,F_Group,F_Private,F_Total) VALUES('ENZH','IT',1000,500,100,1600);

CREATE SEQUENCE S_Sources INCREMENT -1 START -1000;
GRANT SELECT,UPDATE ON SEQUENCE S_Sources TO GROUP "Users";
GRANT ALL ON SEQUENCE S_Sources TO GROUP "Admins";

CREATE TABLE T_Sources
(
	F_SourceID		int4	NOT NULL,
	F_Name		VARCHAR(80),
	F_Date		TIMESTAMP DEFAULT now(),
	F_Format		CHAR(3) DEFAULT '   ',
	F_Owner		int4 DEFAULT 0,
	F_LangPair		CHAR(4) DEFAULT 'ENZH',
	F_Sentences		int4 DEFAULT 0,
	F_Domain		CHAR(2) DEFAULT '00',
	F_Permit		int4 DEFAULT 0,
	PRIMARY KEY (F_SourceID)
);
GRANT SELECT,INSERT,DELETE ON TABLE T_Sources TO GROUP "Users";
GRANT ALL ON TABLE T_Sources TO GROUP "Admins";
INSERT INTO T_Sources(F_SourceID,F_Name) VALUES(0,'Unknown');

CREATE SEQUENCE S_ENZH INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1;
GRANT SELECT,UPDATE ON SEQUENCE S_ENZH TO GROUP "Users";
GRANT ALL ON SEQUENCE S_ENZH TO GROUP "Admins";

CREATE TABLE T_ENZH
(
	F_SID		int8	NOT NULL DEFAULT nextval('S_ENZH'),
	F_Source		VARCHAR(2000),
	F_Target		VARCHAR(2000),
	F_Domain		CHAR(2) DEFAULT '00',
	F_Owner		int4 DEFAULT 0,
	F_Permit		int4 DEFAULT 0,
	F_From		int4 DEFAULT 0,
	PRIMARY KEY (F_SID)
);
CREATE INDEX ENZH_Domain_Index ON T_ENZH (F_Domain);
CREATE INDEX ENZH_Permit_Index ON T_ENZH (F_Permit);
GRANT SELECT,INSERT,DELETE ON TABLE T_ENZH TO GROUP "Users";
GRANT ALL ON TABLE T_ENZH TO GROUP "Admins";

CREATE TABLE T_ENZHX
(
	F_Word		VARCHAR(64)	NOT NULL,
	F_SID		int8	NOT NULL,
	F_Offset		int2 DEFAULT 0,
	PRIMARY KEY (F_Word,F_SID),
	FOREIGN KEY (F_SID) REFERENCES T_ENZH (F_SID) ON DELETE CASCADE
);
GRANT SELECT,INSERT,DELETE ON TABLE T_ENZHX TO GROUP "Users";
GRANT ALL ON TABLE T_ENZHX TO GROUP "Admins";

CREATE TABLE T_ZHENX
(
	F_Word		VARCHAR(64)	NOT NULL,
	F_SID		int8	NOT NULL,
	F_Offset		int2 DEFAULT 0,
	PRIMARY KEY (F_Word,F_SID),
	FOREIGN KEY (F_SID) REFERENCES T_ENZH (F_SID) ON DELETE CASCADE
);
GRANT SELECT,INSERT,DELETE ON TABLE T_ZHENX TO GROUP "Users";
GRANT ALL ON TABLE T_ZHENX TO GROUP "Admins";

CREATE TABLE T_Roles
(
	F_RoleID		CHAR(1)	NOT NULL,
	F_RoleName		VARCHAR(16),
	F_Level		int4,
	PRIMARY KEY (F_RoleID)
);
GRANT SELECT ON TABLE T_Roles TO GROUP "Users";
GRANT ALL ON TABLE T_Roles TO GROUP "Admins";
INSERT INTO T_Roles(F_RoleID,F_RoleName,F_Level) VALUES('U','Basic user',1);
INSERT INTO T_Roles(F_RoleID,F_RoleName,F_Level) VALUES('A','Advanced user',10);
INSERT INTO T_Roles(F_RoleID,F_RoleName,F_Level) VALUES('S','Super user',100);
INSERT INTO T_Roles(F_RoleID,F_RoleName,F_Level) VALUES('G','Group leader',1000);
INSERT INTO T_Roles(F_RoleID,F_RoleName,F_Level) VALUES('M','Manager',10000);

CREATE SEQUENCE S_Groups INCREMENT 1 MINVALUE 1 START 1;
GRANT SELECT,UPDATE ON SEQUENCE S_Groups TO GROUP "Users";
GRANT ALL ON SEQUENCE S_Groups TO GROUP "Admins";

CREATE TABLE T_Groups
(
	F_GroupID		int4	NOT NULL DEFAULT nextval('S_Groups'),
	F_GroupName		VARCHAR(16),
	F_Leader		int4 DEFAULT 0,
	F_CreateDate		TIMESTAMP DEFAULT now(),
	F_Publicity		TEXT DEFAULT 3,
	F_Points		int4 DEFAULT 0,
	F_Members		int4 DEFAULT 0,
	F_Desc		VARCHAR(1024),
	PRIMARY KEY (F_GroupID)
);
CREATE INDEX Groups_Name_Index ON T_Groups (F_GroupName);
GRANT SELECT,INSERT,UPDATE,DELETE ON TABLE T_Groups TO GROUP "Users";
GRANT ALL ON TABLE T_Groups TO GROUP "Admins";
INSERT INTO T_Groups(F_GroupID,F_GroupName,F_Desc) VALUES(0,'Public','Everybody is in this group by default.');
INSERT INTO T_Groups(F_GroupName,F_Leader,F_Desc) VALUES('First Group',1,'My first group');

CREATE SEQUENCE S_Users INCREMENT 1 MINVALUE 1 START 1;
GRANT SELECT,UPDATE ON SEQUENCE S_Users TO GROUP "Users";
GRANT ALL ON SEQUENCE S_Users TO GROUP "Admins";

CREATE TABLE T_Users
(
	F_UserID		int4	NOT NULL DEFAULT nextval('S_Users'),
	F_Username		VARCHAR(32)	NOT NULL	UNIQUE,
	F_Realname		VARCHAR(32)	NOT NULL,
	F_Sex		CHAR(1) DEFAULT 'M',
	F_Password		VARCHAR(32)	NOT NULL,
	F_Regdate		TIMESTAMP DEFAULT now(),
	F_Birthday		TIMESTAMP,
	F_Membership		CHAR(1) DEFAULT 'T',
	F_Role		CHAR(1) DEFAULT 'U',
	F_Points		int4 DEFAULT 100,
	F_Credits		int4 DEFAULT 0,
	F_Email		VARCHAR(80)	NOT NULL,
	F_Mobile		VARCHAR(80),
	F_IdType		CHAR(2),
	F_IdNumber		VARCHAR(20),
	F_Question		VARCHAR(128),
	F_Answer		VARCHAR(16),
	F_State		CHAR(1) DEFAULT 'A',
	F_Work		int4 DEFAULT 0,
	F_Group		int4 DEFAULT 0,
	F_LastVisit		TIMESTAMP DEFAULT now(),
	F_VisitCount		int4 DEFAULT 0,
	F_Shared		int4 DEFAULT 0,
	PRIMARY KEY (F_UserID),
	FOREIGN KEY (F_Role) REFERENCES T_Roles (F_RoleID),
	FOREIGN KEY (F_Group) REFERENCES T_Groups (F_GroupID)
);
CREATE INDEX UserNameIndex ON T_Users (F_Username);
CREATE INDEX UserGroupIndex ON T_Users (F_Group);
GRANT SELECT,INSERT,UPDATE ON TABLE T_Users TO GROUP "Users";
GRANT ALL ON TABLE T_Users TO GROUP "Admins";
INSERT INTO T_Users(F_Username,F_Realname,F_Password,F_Role,F_Group,F_Email,F_Question,F_Answer) VALUES('TedWen','Ted','81DC9BDB52D04DC20036DBD8313ED055','G',1,'tedwen@nesc.ac.uk','Where are you?','Edinburgh');
INSERT INTO T_Users(F_Username,F_Realname,F_Password,F_Group,F_Email,F_Question,F_Answer) VALUES('Alex','Alex','E10ADC3949BA59ABBE56E057F20F883E',1,'alex@allwinworld.com','1+1=?','2');
INSERT INTO T_Users(F_Username,F_Realname,F_Password,F_Group,F_Email,F_Question,F_Answer) VALUES('Kristy','Kristy','E10ADC3949BA59ABBE56E057F20F883E',1,'kristy@allwinworld.com','1+1=?','2');
INSERT INTO T_Users(F_Username,F_Realname,F_Password,F_Email,F_Question,F_Answer) VALUES('test','test','098F6BCD4621D373CADE4E832627B4F6','test@allwinworld.com','1+1=?','2');

CREATE SEQUENCE S_Preferences INCREMENT 1 MINVALUE 1 START 10;
GRANT SELECT,UPDATE ON SEQUENCE S_Preferences TO GROUP "Users";
GRANT ALL ON SEQUENCE S_Preferences TO GROUP "Admins";

CREATE TABLE T_Preferences
(
	F_PreID		int4	NOT NULL DEFAULT nextval('S_Preferences'),
	F_User		int4	NOT NULL,
	F_Item		VARCHAR(10)	NOT NULL,
	F_Value		VARCHAR(10)	NOT NULL,
	PRIMARY KEY (F_PreID),
	FOREIGN KEY (F_User) REFERENCES T_Users (F_UserID)
);
CREATE INDEX prefuserindex ON T_Preferences (F_User);
GRANT SELECT,INSERT,UPDATE,DELETE ON TABLE T_Preferences TO GROUP "Users";
GRANT ALL ON TABLE T_Preferences TO GROUP "Admins";

CREATE TABLE T_FileFormats
(
	F_FormatID		CHAR(3)	NOT NULL,
	F_FormatName		VARCHAR(32),
	PRIMARY KEY (F_FormatID)
);
GRANT SELECT ON TABLE T_FileFormats TO GROUP "Users";
GRANT ALL ON TABLE T_FileFormats TO GROUP "Admins";
INSERT INTO T_FileFormats(F_FormatID,F_FormatName) VALUES('TXT','Plain text');
INSERT INTO T_FileFormats(F_FormatID,F_FormatName) VALUES('HTM','HTML web page');
INSERT INTO T_FileFormats(F_FormatID,F_FormatName) VALUES('DOC','Word document');
INSERT INTO T_FileFormats(F_FormatID,F_FormatName) VALUES('PDF','Adobe PDF');

CREATE SEQUENCE S_Projects INCREMENT 1 MINVALUE 1 START 10;
GRANT SELECT,UPDATE ON SEQUENCE S_Projects TO GROUP "Users";
GRANT ALL ON SEQUENCE S_Projects TO GROUP "Admins";

CREATE TABLE T_Projects
(
	F_ProjectID		int4	NOT NULL DEFAULT nextval('S_Projects'),
	F_ProjectName		VARCHAR(128)	NOT NULL,
	F_LangPair		CHAR(4)	NOT NULL,
	F_Creator		int4	NOT NULL,
	F_CreateDate		TIMESTAMP DEFAULT now(),
	F_Progress		float4 DEFAULT 0,
	F_Articles		int4 DEFAULT 0,
	PRIMARY KEY (F_ProjectID),
	FOREIGN KEY (F_LangPair) REFERENCES T_Languages (F_PairID),
	FOREIGN KEY (F_Creator) REFERENCES T_Users (F_UserID)
);
CREATE INDEX project_creatorindex ON T_Projects (F_Creator);
GRANT SELECT,INSERT,UPDATE,DELETE ON TABLE T_Projects TO GROUP "Users";
GRANT ALL ON TABLE T_Projects TO GROUP "Admins";
INSERT INTO T_Projects(F_ProjectID,F_ProjectName,F_LangPair,F_Creator,F_Articles) VALUES(1,'Test Project 1','ENZH',1,1);

CREATE SEQUENCE S_Articles INCREMENT 1 MINVALUE 1 START 10;
GRANT SELECT,UPDATE ON SEQUENCE S_Articles TO GROUP "Users";
GRANT ALL ON SEQUENCE S_Articles TO GROUP "Admins";

CREATE TABLE T_Articles
(
	F_ArticleID		int4	NOT NULL DEFAULT nextval('S_Articles'),
	F_Project		int4	NOT NULL,
	F_Title		VARCHAR(256)	NOT NULL,
	F_Sentences		int4 DEFAULT 0,
	F_LangPair		CHAR(4)	NOT NULL,
	F_Translator		int4	NOT NULL,
	F_UploadDate		TIMESTAMP DEFAULT now(),
	F_Paragraph		int4 DEFAULT -1,
	F_StartDate		TIMESTAMP DEFAULT now(),
	F_Progress		float4 DEFAULT 0,
	F_FileFormat		CHAR(3) DEFAULT 'TXT',
	F_Filename		VARCHAR(128),
	PRIMARY KEY (F_ArticleID),
	FOREIGN KEY (F_Translator) REFERENCES T_Users (F_UserID),
	FOREIGN KEY (F_FileFormat) REFERENCES T_FileFormats (F_FormatID),
	FOREIGN KEY (F_Project) REFERENCES T_Projects (F_ProjectID) ON DELETE CASCADE,
	FOREIGN KEY (F_LangPair) REFERENCES T_Languages (F_PairID)
);
CREATE INDEX idx_articles_project ON T_Articles (F_Project);
CREATE INDEX idx_articles_translator ON T_Articles (F_Translator);
GRANT SELECT,INSERT,UPDATE,DELETE ON TABLE T_Articles TO GROUP "Users";
GRANT ALL ON TABLE T_Articles TO GROUP "Admins";
INSERT INTO T_Articles(F_ArticleID,F_Project,F_Title,F_LangPair,F_Translator,F_Filename) VALUES(1,1,'Artice I for project 1','ENZH',1,'test.txt');

CREATE SEQUENCE S_Paragraphs INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 10;
GRANT SELECT,UPDATE ON SEQUENCE S_Paragraphs TO GROUP "Users";
GRANT ALL ON SEQUENCE S_Paragraphs TO GROUP "Admins";

CREATE TABLE T_Paragraphs
(
	F_ParagraphID		int8	NOT NULL DEFAULT nextval('S_Paragraphs'),
	F_Article		int4	NOT NULL,
	F_StartPos		int4,
	F_EndPos		int4,
	PRIMARY KEY (F_ParagraphID),
	FOREIGN KEY (F_Article) REFERENCES T_Articles (F_ArticleID) ON DELETE CASCADE
);
CREATE INDEX paragraph_article_index ON T_Paragraphs (F_Article);
GRANT SELECT,INSERT,UPDATE,DELETE ON TABLE T_Paragraphs TO GROUP "Users";
GRANT ALL ON TABLE T_Paragraphs TO GROUP "Admins";
INSERT INTO T_Paragraphs(F_ParagraphID,F_Article,F_StartPos,F_EndPos) VALUES(1,1,1,100);

CREATE SEQUENCE S_Sentences INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 10;
GRANT SELECT,UPDATE ON SEQUENCE S_Sentences TO GROUP "Users";
GRANT ALL ON SEQUENCE S_Sentences TO GROUP "Admins";

CREATE TABLE T_Sentences
(
	F_SentenceID		int8	NOT NULL DEFAULT nextval('S_Sentences'),
	F_Article		int4	NOT NULL,
	F_Paragraph		int8	NOT NULL,
	F_Sequence		float4	NOT NULL,
	F_StartPos		int4,
	F_EndPos		int4,
	F_Sentence		VARCHAR(1024)	NOT NULL,
	F_Translation		VARCHAR(1024),
	PRIMARY KEY (F_SentenceID),
	FOREIGN KEY (F_Article) REFERENCES T_Articles (F_ArticleID) ON DELETE CASCADE
);
CREATE INDEX sentence_article_index ON T_Sentences (F_Article);
CREATE INDEX sentence_paragraph_index ON T_Sentences (F_Paragraph);
GRANT SELECT,INSERT,UPDATE,DELETE ON TABLE T_Sentences TO GROUP "Users";
GRANT ALL ON TABLE T_Sentences TO GROUP "Admins";
INSERT INTO T_Sentences(F_Article,F_Paragraph,F_Sequence,F_Sentence) VALUES(1,1,1,'JavaScript is not Java.');
INSERT INTO T_Sentences(F_Article,F_Paragraph,F_Sequence,F_Sentence) VALUES(1,1,1.5,'Java was developed at Sun Microsystems.');
INSERT INTO T_Sentences(F_Article,F_Paragraph,F_Sequence,F_Sentence) VALUES(1,1,2,'JavaScript was developed at Netscape.');
INSERT INTO T_Sentences(F_Article,F_Paragraph,F_Sequence,F_Sentence) VALUES(1,1,3,'Java applications are independent of a Web page whereas JavaScript programs are embedded in a Web page and must be run in a browser window.');
INSERT INTO T_Sentences(F_Article,F_Paragraph,F_Sequence,F_Sentence) VALUES(1,1,4,'Java is a strongly typed language with strict guidelines while JavaScript is loosely typed and flexible.');
INSERT INTO T_Sentences(F_Article,F_Paragraph,F_Sequence,F_Sentence) VALUES(1,1,5,'Java data types must be declared.');
INSERT INTO T_Sentences(F_Article,F_Paragraph,F_Sequence,F_Sentence) VALUES(1,1,6,'JavaScript types such as variables, parameters, and function return types do not have to be declared.');
INSERT INTO T_Sentences(F_Article,F_Paragraph,F_Sequence,F_Sentence) VALUES(1,1,7,'Java programs are compiled.');
INSERT INTO T_Sentences(F_Article,F_Paragraph,F_Sequence,F_Sentence) VALUES(1,1,8,'JavaScript programs are interpreted by a JavaScript engine that lives in the browser.');
