--
-- PostgreSQL database dump
--

SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

--
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'Standard public schema';


--
-- Name: plpgsql; Type: PROCEDURAL LANGUAGE; Schema: -; Owner: postgres
--

CREATE PROCEDURAL LANGUAGE plpgsql;


SET search_path = public, pg_catalog;

--
-- Name: countwords(character varying[]); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION countwords(words character varying[]) RETURNS integer
    AS $$
DECLARE
    n int4;
BEGIN
    n = length(words[0]);
    n = n + length(words[1]);
    RETURN n;
END;
$$
    LANGUAGE plpgsql;


ALTER FUNCTION public.countwords(words character varying[]) OWNER TO postgres;

--
-- Name: s_articles; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE s_articles
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.s_articles OWNER TO postgres;

--
-- Name: s_articles; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('s_articles', 10, true);


--
-- Name: s_corpora; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE s_corpora
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.s_corpora OWNER TO postgres;

--
-- Name: s_corpora; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('s_corpora', 1, true);


--
-- Name: s_enzh; Type: SEQUENCE; Schema: public; Owner: tm
--

CREATE SEQUENCE s_enzh
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.s_enzh OWNER TO tm;

--
-- Name: s_enzh; Type: SEQUENCE SET; Schema: public; Owner: tm
--

SELECT pg_catalog.setval('s_enzh', 13, true);


--
-- Name: s_groups; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE s_groups
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.s_groups OWNER TO postgres;

--
-- Name: s_groups; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('s_groups', 1, true);


--
-- Name: s_paragraphs; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE s_paragraphs
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.s_paragraphs OWNER TO postgres;

--
-- Name: s_paragraphs; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('s_paragraphs', 13, true);


--
-- Name: s_preferences; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE s_preferences
    START WITH 10
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.s_preferences OWNER TO postgres;

--
-- Name: s_preferences; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('s_preferences', 10, false);


--
-- Name: s_projects; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE s_projects
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.s_projects OWNER TO postgres;

--
-- Name: s_projects; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('s_projects', 10, true);


--
-- Name: s_sentences; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE s_sentences
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.s_sentences OWNER TO postgres;

--
-- Name: s_sentences; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('s_sentences', 22, true);


--
-- Name: s_sources; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE s_sources
    START WITH -1000
    INCREMENT BY -1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.s_sources OWNER TO postgres;

--
-- Name: s_sources; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('s_sources', -1000, false);


--
-- Name: s_users; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE s_users
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.s_users OWNER TO postgres;

--
-- Name: s_users; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('s_users', 4, true);


SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: t_articles; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE t_articles (
    f_articleid integer DEFAULT nextval('s_articles'::regclass) NOT NULL,
    f_project integer NOT NULL,
    f_title character varying(256) NOT NULL,
    f_sentences integer DEFAULT 0,
    f_langsrc character(2) NOT NULL,
    f_langdst character(2) NOT NULL,
    f_translator integer NOT NULL,
    f_uploaddate timestamp without time zone DEFAULT now(),
    f_paragraph integer DEFAULT -1,
    f_startdate timestamp without time zone DEFAULT now(),
    f_progress real DEFAULT 0,
    f_fileformat character(3) DEFAULT 'TXT'::bpchar,
    f_filename character varying(128)
);


ALTER TABLE public.t_articles OWNER TO postgres;

--
-- Name: t_corpora; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE t_corpora (
    f_corpusid integer DEFAULT nextval('s_corpora'::regclass) NOT NULL,
    f_langsrc character(2) NOT NULL,
    f_langdst character(2) NOT NULL,
    f_domain character(2) NOT NULL,
    f_public integer DEFAULT 0,
    f_group integer DEFAULT 0,
    f_private integer DEFAULT 0,
    f_total integer DEFAULT 0
);


ALTER TABLE public.t_corpora OWNER TO postgres;

--
-- Name: t_domains; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE t_domains (
    f_domaincode character(2) NOT NULL,
    f_domainname character varying(16),
    f_parentdomain character(2)
);


ALTER TABLE public.t_domains OWNER TO postgres;

--
-- Name: t_enzh; Type: TABLE; Schema: public; Owner: tm; Tablespace: 
--

CREATE TABLE t_enzh (
    f_sid bigint DEFAULT nextval('s_enzh'::regclass) NOT NULL,
    f_source character varying(1024),
    f_target character varying(1024),
    f_domain character(2) DEFAULT '00'::bpchar,
    f_owner integer DEFAULT 0,
    f_permit integer DEFAULT 0,
    f_from integer DEFAULT 0
);


ALTER TABLE public.t_enzh OWNER TO tm;

--
-- Name: t_enzh_enx; Type: TABLE; Schema: public; Owner: tm; Tablespace: 
--

CREATE TABLE t_enzh_enx (
    f_word character varying(64) NOT NULL,
    f_sid bigint NOT NULL,
    f_offset smallint DEFAULT 0
);


ALTER TABLE public.t_enzh_enx OWNER TO tm;

--
-- Name: t_enzh_zhx; Type: TABLE; Schema: public; Owner: tm; Tablespace: 
--

CREATE TABLE t_enzh_zhx (
    f_word character varying(64) NOT NULL,
    f_sid bigint NOT NULL,
    f_offset smallint DEFAULT 0
);


ALTER TABLE public.t_enzh_zhx OWNER TO tm;

--
-- Name: t_fileformats; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE t_fileformats (
    f_formatid character(3) NOT NULL,
    f_formatname character varying(32)
);


ALTER TABLE public.t_fileformats OWNER TO postgres;

--
-- Name: t_groups; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE t_groups (
    f_groupid integer DEFAULT nextval('s_groups'::regclass) NOT NULL,
    f_groupname character varying(16),
    f_leader integer DEFAULT 0,
    f_createdate timestamp without time zone DEFAULT now(),
    f_publicity text DEFAULT 3,
    f_points integer DEFAULT 0,
    f_members integer DEFAULT 0,
    f_desc character varying(1024)
);


ALTER TABLE public.t_groups OWNER TO postgres;

--
-- Name: t_languages; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE t_languages (
    f_code character(2) NOT NULL,
    f_name character varying(16)
);


ALTER TABLE public.t_languages OWNER TO postgres;

--
-- Name: t_paragraphs; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE t_paragraphs (
    f_paragraphid bigint DEFAULT nextval('s_paragraphs'::regclass) NOT NULL,
    f_article integer NOT NULL,
    f_startpos integer,
    f_endpos integer
);


ALTER TABLE public.t_paragraphs OWNER TO postgres;

--
-- Name: t_parameters; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE t_parameters (
    f_name character varying(32) NOT NULL,
    f_value character varying(128),
    f_type character varying(10)
);


ALTER TABLE public.t_parameters OWNER TO postgres;

--
-- Name: t_preferences; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE t_preferences (
    f_preid integer DEFAULT nextval('s_preferences'::regclass) NOT NULL,
    f_user integer NOT NULL,
    f_item character varying(10) NOT NULL,
    f_value character varying(10) NOT NULL
);


ALTER TABLE public.t_preferences OWNER TO postgres;

--
-- Name: t_projects; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE t_projects (
    f_projectid integer DEFAULT nextval('s_projects'::regclass) NOT NULL,
    f_projectname character varying(128) NOT NULL,
    f_langsrc character(2) NOT NULL,
    f_langdst character(2) NOT NULL,
    f_creator integer NOT NULL,
    f_createdate timestamp without time zone DEFAULT now(),
    f_progress real DEFAULT 0,
    f_articles integer DEFAULT 0
);


ALTER TABLE public.t_projects OWNER TO postgres;

--
-- Name: t_roles; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE t_roles (
    f_roleid character(1) NOT NULL,
    f_rolename character varying(16),
    f_level integer
);


ALTER TABLE public.t_roles OWNER TO postgres;

--
-- Name: t_sentences; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE t_sentences (
    f_sentenceid bigint DEFAULT nextval('s_sentences'::regclass) NOT NULL,
    f_article integer NOT NULL,
    f_paragraph bigint NOT NULL,
    f_sequence real NOT NULL,
    f_startpos integer,
    f_endpos integer,
    f_sentence character varying(1024) NOT NULL,
    f_translation character varying(1024)
);


ALTER TABLE public.t_sentences OWNER TO postgres;

--
-- Name: t_sources; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE t_sources (
    f_sourceid integer DEFAULT nextval('s_sources'::regclass) NOT NULL,
    f_name character varying(80),
    f_date timestamp without time zone DEFAULT now(),
    f_format character(3) DEFAULT '   '::bpchar,
    f_owner integer DEFAULT 0,
    f_langsrc character(2) DEFAULT 'EN'::bpchar,
    f_langdst character(2) DEFAULT 'ZH'::bpchar,
    f_sentences integer DEFAULT 0,
    f_domain character(2) DEFAULT '00'::bpchar,
    f_permit integer DEFAULT 0
);


ALTER TABLE public.t_sources OWNER TO postgres;

--
-- Name: t_users; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE t_users (
    f_userid integer DEFAULT nextval('s_users'::regclass) NOT NULL,
    f_username character varying(32) NOT NULL,
    f_realname character varying(32) NOT NULL,
    f_sex character(1) DEFAULT 'M'::bpchar,
    f_password character varying(32) NOT NULL,
    f_regdate timestamp without time zone DEFAULT now(),
    f_birthday timestamp without time zone,
    f_userip character varying(40),
    f_membership character(1) DEFAULT 'T'::bpchar,
    f_role character(1) DEFAULT 'U'::bpchar,
    f_points integer DEFAULT 100,
    f_credits integer DEFAULT 0,
    f_email character varying(80) NOT NULL,
    f_mobile character varying(80),
    f_idtype character(2),
    f_idnumber character varying(20),
    f_question character varying(128),
    f_answer character varying(16),
    f_state character(1) DEFAULT 'A'::bpchar,
    f_work integer DEFAULT 0,
    f_group integer DEFAULT 0,
    f_lastvisit timestamp without time zone DEFAULT now(),
    f_visitcount integer DEFAULT 0,
    f_shared integer DEFAULT 0
);


ALTER TABLE public.t_users OWNER TO postgres;

--
-- Data for Name: t_articles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY t_articles (f_articleid, f_project, f_title, f_sentences, f_langsrc, f_langdst, f_translator, f_uploaddate, f_paragraph, f_startdate, f_progress, f_fileformat, f_filename) FROM stdin;
10	10	Article I	13	EN	ZH	1	2007-06-19 20:16:40.359	1	2007-06-19 20:16:40.359	100	TXT	\N
\.


--
-- Data for Name: t_corpora; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY t_corpora (f_corpusid, f_langsrc, f_langdst, f_domain, f_public, f_group, f_private, f_total) FROM stdin;
1	EN	ZH	00	13	0	0	0
\.


--
-- Data for Name: t_domains; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY t_domains (f_domaincode, f_domainname, f_parentdomain) FROM stdin;
00	General	\N
\.


--
-- Data for Name: t_enzh; Type: TABLE DATA; Schema: public; Owner: tm
--

COPY t_enzh (f_sid, f_source, f_target, f_domain, f_owner, f_permit, f_from) FROM stdin;
1	  Technique is of no use unless it is combined with musical knowledge and understanding.	除非是和音乐方面的知识和悟性结合起来，单纯的技巧没有任何用处。	00	1	0	10
2	  Singers and instrumentalists have to be able to get every note perfectly in tune.	歌手和乐器演奏者必须使所有的音符完全相同协调。	00	1	0	10
3	This problem of getting clear texture is one that confronts student conductors: they have to learn to know every note of the music and how it should sound, and they have to aim at controlling these sounds with fanatical but selfless authority.	如何得到乐章清晰的纹理是学生指挥们所面临的难题：他们必须学会了解音乐中的每一个音及其发音之道。他们还必须致力于以热忱而又客观的权威去控制这些音符。	00	1	0	10
4	A painter hangs his or her finished picture on a wall, and everyone can see it.	画家将已完成的作品挂在墙上，每个人都可以观赏到。	00	1	0	10
5	Great artists are those who are so thoroughly at home in the language of music that they can enjoy performing works written in any century.	艺术家之所以伟大在于他们对音乐语言驾轻就熟，以致于可以满怀喜悦地演出写于任何时代的作品。	00	1	0	10
6	 Pianists are spared this particular anxiety, for the notes are already there, waiting for them, and it is the piano tuner's responsibility to tune the instrument for them.	钢琴家们则不用操这份心，因为每个音符都已在那里等待着他们了, 给钢琴调音是调音师的职责。	00	1	0	10
7	 A composer writes a work, but no one can hear it until it is performed.	作曲家写完了一部作品，得由演奏者将其演奏出来，其他人才能得以欣赏。	00	1	0	10
8	 Professional singers and players have great responsibilities, for the composer is utterly dependent on them.	因为作曲家是如此完全地依赖于职业歌手和职业演奏者，所以职业歌手和职业演奏者肩上的担子可谓不轻。	00	1	0	10
9	But they have their own difficulties: the hammers that hit the strings have to be coaxed not to sound like percussion, and each overlapping tone has to sound clear.	但调音师们也有他们的难处：他们必须耐心地调理敲击琴弦的音锤，不能让音锤发出的声音象是打击乐器，而且每个交叠的音都必须要清晰。	00	1	0	10
10	 A student of music needs as long and as arduous a training to become a performer as a medical student needs to become a doctor.	一名学音乐的学生要想成为一名演奏者，需要经受长期的、严格的训练，就象一名医科的学生要成为一名医生一样。	00	1	0	10
11	 Most training is concerned with technique, for musicians have to have the muscular proficiency of an athlete or a ballet dancer.	绝大多数的训练是技巧性的。 音乐家们控制肌肉的熟练程度，必须达到与运动员或巴蕾舞演员相当的水平。	00	1	0	10
12	 Singers practice breathing every day, as their vocal chords would be inadequate without controlled muscular support.	歌手们每天都练习吊嗓子，因为如果不能有效地控制肌肉的话，他们的声带将不能满足演唱的要求。	00	1	0	10
13	String players practice moving the fingers of the left hand up and down, while drawing the bow to and fro with the right arm -- two entirely different movements.	弦乐器的演奏者练习的则是在左手的手指上下滑动的同时，用右手前后拉动琴弓--两个截然不同的动作。	00	1	0	10
\.


--
-- Data for Name: t_enzh_enx; Type: TABLE DATA; Schema: public; Owner: tm
--

COPY t_enzh_enx (f_word, f_sid, f_offset) FROM stdin;
techniqu	1	0
is	1	1
of	1	2
no	1	3
use	1	4
unless	1	5
it	1	6
combin	1	7
with	1	8
music	1	9
knowledg	1	10
and	1	11
understand	1	12
singer	2	0
and	2	1
instrumentalist	2	2
have	2	3
to	2	4
be	2	5
abl	2	6
get	2	7
everi	2	8
note	2	9
perfect	2	10
in	2	11
tune	2	12
this	3	0
problem	3	1
of	3	2
get	3	3
clear	3	4
textur	3	5
is	3	6
one	3	7
that	3	8
confront	3	9
student	3	10
conductor	3	11
they	3	12
have	3	13
to	3	14
learn	3	15
know	3	16
everi	3	17
note	3	18
the	3	19
music	3	20
and	3	21
how	3	22
it	3	23
should	3	24
sound	3	25
aim	3	26
at	3	27
control	3	28
these	3	29
with	3	30
fanat	3	31
but	3	32
selfless	3	33
author	3	34
a	4	0
painter	4	1
hang	4	2
his	4	3
or	4	4
her	4	5
finish	4	6
pictur	4	7
on	4	8
wall	4	9
and	4	10
everyon	4	11
can	4	12
see	4	13
it	4	14
great	5	0
artist	5	1
are	5	2
those	5	3
who	5	4
so	5	5
thorough	5	6
at	5	7
home	5	8
in	5	9
the	5	10
languag	5	11
of	5	12
music	5	13
that	5	14
they	5	15
can	5	16
enjoy	5	17
perform	5	18
work	5	19
written	5	20
ani	5	21
centuri	5	22
pianist	6	0
are	6	1
spare	6	2
this	6	3
particular	6	4
anxieti	6	5
for	6	6
the	6	7
note	6	8
alreadi	6	9
there	6	10
wait	6	11
them	6	12
and	6	13
it	6	14
is	6	15
piano	6	16
tuner	6	17
respons	6	18
to	6	19
tune	6	20
instrument	6	21
a	7	0
compos	7	1
write	7	2
work	7	3
but	7	4
no	7	5
one	7	6
can	7	7
hear	7	8
it	7	9
until	7	10
is	7	11
perform	7	12
profession	8	0
singer	8	1
and	8	2
player	8	3
have	8	4
great	8	5
respons	8	6
for	8	7
the	8	8
compos	8	9
is	8	10
utter	8	11
depend	8	12
on	8	13
them	8	14
but	9	0
they	9	1
have	9	2
their	9	3
own	9	4
difficulti	9	5
the	9	6
hammer	9	7
that	9	8
hit	9	9
string	9	10
to	9	11
be	9	12
coax	9	13
not	9	14
sound	9	15
like	9	16
percuss	9	17
and	9	18
each	9	19
overlap	9	20
tone	9	21
has	9	22
clear	9	23
a	10	0
student	10	1
of	10	2
music	10	3
need	10	4
as	10	5
long	10	6
and	10	7
arduous	10	8
train	10	9
to	10	10
becom	10	11
perform	10	12
medic	10	13
doctor	10	14
most	11	0
train	11	1
is	11	2
concern	11	3
with	11	4
techniqu	11	5
for	11	6
musician	11	7
have	11	8
to	11	9
the	11	10
muscular	11	11
profici	11	12
of	11	13
an	11	14
athlet	11	15
or	11	16
a	11	17
ballet	11	18
dancer	11	19
singer	12	0
practic	12	1
breath	12	2
everi	12	3
day	12	4
as	12	5
their	12	6
vocal	12	7
chord	12	8
would	12	9
be	12	10
inadequ	12	11
without	12	12
control	12	13
muscular	12	14
support	12	15
string	13	0
player	13	1
practic	13	2
move	13	3
the	13	4
finger	13	5
of	13	6
left	13	7
hand	13	8
up	13	9
and	13	10
down	13	11
while	13	12
draw	13	13
bow	13	14
to	13	15
fro	13	16
with	13	17
right	13	18
arm	13	19
two	13	20
entir	13	21
differ	13	22
movement	13	23
\.


--
-- Data for Name: t_enzh_zhx; Type: TABLE DATA; Schema: public; Owner: tm
--

COPY t_enzh_zhx (f_word, f_sid, f_offset) FROM stdin;
除非	1	0
除非是	1	1
是和	1	2
音乐	1	3
方面	1	4
面的	1	5
知识	1	6
悟性	1	7
结合	1	8
结合起来	1	9
合起	1	10
合起来	1	11
起来	1	12
单纯	1	13
纯的	1	14
技巧	1	15
没有	1	16
任何	1	17
何用	1	18
用处	1	19
歌手	2	0
和乐	2	1
乐器	2	2
演奏	2	3
演奏者	2	4
必须	2	5
使	2	6
所有	2	7
所有的	2	8
有的	2	9
音符	2	10
完全	2	11
完全相同	2	12
相同	2	13
协调	2	14
如何	3	0
得到	3	1
乐章	3	2
清晰	3	3
纹理	3	4
学生	3	5
指挥	3	6
所	3	7
面临	3	8
难题	3	9
他	3	10
他们	3	11
必须	3	12
学会	3	13
会了	3	14
了解	3	15
音乐	3	16
中的	3	17
每一	3	18
每一个	3	19
一个	3	20
音	3	21
及其	3	22
发音	3	23
之道	3	24
还必须	3	25
致力	3	26
致力于	3	27
热忱	3	28
而又	3	29
客观	3	30
权威	3	31
控制	3	32
这些	3	33
音符	3	34
画家	4	0
将	4	1
已完	4	2
完成	4	3
作品	4	4
挂在	4	5
墙上	4	6
每个	4	7
每个人	4	8
个人	4	9
都可	4	10
都可以	4	11
可以	4	12
观赏	4	13
赏到	4	14
艺术	5	0
艺术家	5	1
之所以	5	2
所以	5	3
伟大	5	4
大	5	5
在于	5	6
他	5	7
他们	5	8
音乐	5	9
语言	5	10
驾轻就熟	5	11
以致	5	12
以致于	5	13
致于	5	14
可以	5	15
满怀	5	16
喜悦	5	17
地	5	18
演出	5	19
写	5	20
写于	5	21
任何	5	22
何时	5	23
时代	5	24
作品	5	25
钢琴	6	0
钢琴家	6	1
则不	6	2
不用	6	3
操	6	4
这份	6	5
心	6	6
因为	6	7
每个	6	8
音符	6	9
都已	6	10
已在	6	11
在那	6	12
在那里	6	13
那里	6	14
等待	6	15
等待着	6	16
待着	6	17
他	6	18
他们	6	19
给	6	20
琴调	6	21
调音	6	22
调音师	6	23
职责	6	24
作曲	7	0
作曲家	7	1
写完	7	2
完了	7	3
一部	7	4
作品	7	5
得	7	6
得由	7	7
演奏	7	8
演奏者	7	9
将其	7	10
奏出	7	11
奏出来	7	12
出来	7	13
其他	7	14
其他人	7	15
他	7	16
他人	7	17
人才	7	18
才能	7	19
得以	7	20
欣赏	7	21
因为	8	0
作曲	8	1
作曲家	8	2
如此	8	3
完全	8	4
地	8	5
依赖	8	6
依赖于	8	7
赖于	8	8
职业	8	9
歌手	8	10
演奏	8	11
演奏者	8	12
所以	8	13
肩上	8	14
上的	8	15
担子	8	16
可谓	8	17
不轻	8	18
调音	9	0
调音师	9	1
也有	9	2
他	9	3
他们	9	4
他们的	9	5
难处	9	6
必须	9	7
耐心	9	8
心地	9	9
调理	9	10
敲击	9	11
琴弦	9	12
音锤	9	13
锤	9	14
不能	9	15
能让	9	16
音	9	17
发出	9	18
出的	9	19
声音	9	20
象是	9	21
打击	9	22
打击乐器	9	23
乐器	9	24
而且	9	25
每个	9	26
交叠	9	27
都必须	9	28
须要	9	29
清晰	9	30
一名	10	0
名学	10	1
音乐	10	2
乐的	10	3
学生	10	4
要想	10	5
想成	10	6
成为	10	7
演奏	10	8
演奏者	10	9
需要	10	10
经受	10	11
长	10	12
长期	10	13
严格	10	14
训练	10	15
就象	10	16
名医	10	17
医科	10	18
要	10	19
医生	10	20
一样	10	21
绝大	11	0
绝大多数	11	1
大	11	2
大多	11	3
大多数	11	4
多数	11	5
数的	11	6
训练	11	7
技巧	11	8
技巧性	11	9
性的	11	10
音乐	11	11
音乐家	11	12
控制	11	13
肌肉	11	14
熟练	11	15
熟练程度	11	16
程度	11	17
必须	11	18
达到	11	19
运动	11	20
运动员	11	21
动员	11	22
巴蕾	11	23
蕾舞	11	24
舞	11	25
演员	11	26
相当	11	27
水平	11	28
歌手	12	0
每天	12	1
都	12	2
练习	12	3
吊嗓	12	4
吊嗓子	12	5
嗓子	12	6
因为	12	7
如果	12	8
不能	12	9
能有	12	10
有效	12	11
有效地	12	12
控制	12	13
肌肉	12	14
的话	12	15
他	12	16
他们	12	17
他们的	12	18
声带	12	19
将不	12	20
满足	12	21
演唱	12	22
唱的	12	23
要求	12	24
弦乐	13	0
弦乐器	13	1
乐器	13	2
演奏	13	3
演奏者	13	4
练习	13	5
则是	13	6
是在	13	7
左	13	8
左手	13	9
手指	13	10
上下	13	11
下	13	12
下滑	13	13
滑动	13	14
动的	13	15
同时	13	16
用	13	17
右	13	18
右手	13	19
前	13	20
前后	13	21
后	13	22
拉动	13	23
琴弓	13	24
两个	13	25
截然	13	26
截然不同	13	27
不同	13	28
不同的	13	29
动作	13	30
\.


--
-- Data for Name: t_fileformats; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY t_fileformats (f_formatid, f_formatname) FROM stdin;
TXT	Plain text
HTM	HTML web page
DOC	Word document
PDF	Adobe PDF
\.


--
-- Data for Name: t_groups; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY t_groups (f_groupid, f_groupname, f_leader, f_createdate, f_publicity, f_points, f_members, f_desc) FROM stdin;
0	Public	0	2007-06-19 09:16:14.859	3	0	0	Everybody is in this group by default.
1	First Group	1	2007-06-19 09:16:14.859	3	0	0	My first group
\.


--
-- Data for Name: t_languages; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY t_languages (f_code, f_name) FROM stdin;
EN	English
ZH	Chinese
FR	French
DE	German
ES	Spanish
IT	Italian
JA	Japanese
RU	Russian
DA	Danish
NL	Dutch
FI	Finnish
NO	Norwegian
PT	Portuguese
SV	Swedish
KO	Korean
TH	Thai
\.


--
-- Data for Name: t_paragraphs; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY t_paragraphs (f_paragraphid, f_article, f_startpos, f_endpos) FROM stdin;
10	10	0	801
11	10	801	1224
12	10	1224	1472
13	10	1472	1702
\.


--
-- Data for Name: t_parameters; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY t_parameters (f_name, f_value, f_type) FROM stdin;
PointsPerQuery	1	int
PointsPerShare	1	int
\.


--
-- Data for Name: t_preferences; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY t_preferences (f_preid, f_user, f_item, f_value) FROM stdin;
\.


--
-- Data for Name: t_projects; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY t_projects (f_projectid, f_projectname, f_langsrc, f_langdst, f_creator, f_createdate, f_progress, f_articles) FROM stdin;
10	ç¬¬ä¸é¡¹ç®	EN	ZH	1	2007-06-19 20:08:56.75	100	1
\.


--
-- Data for Name: t_roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY t_roles (f_roleid, f_rolename, f_level) FROM stdin;
U	Basic user	1
A	Advanced user	10
S	Super user	100
G	Group leader	1000
M	Manager	10000
\.


--
-- Data for Name: t_sentences; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY t_sentences (f_sentenceid, f_article, f_paragraph, f_sequence, f_startpos, f_endpos, f_sentence, f_translation) FROM stdin;
10	10	10	0	0	81	A painter hangs his or her finished picture on a wall, and everyone can see it.	画家将已完成的作品挂在墙上，每个人都可以观赏到。
11	10	10	1	81	153	 A composer writes a work, but no one can hear it until it is performed.	作曲家写完了一部作品，得由演奏者将其演奏出来，其他人才能得以欣赏。
12	10	10	2	153	262	 Professional singers and players have great responsibilities, for the composer is utterly dependent on them.	因为作曲家是如此完全地依赖于职业歌手和职业演奏者，所以职业歌手和职业演奏者肩上的担子可谓不轻。
13	10	10	3	262	390	 A student of music needs as long and as arduous a training to become a performer as a medical student needs to become a doctor.	一名学音乐的学生要想成为一名演奏者，需要经受长期的、严格的训练，就象一名医科的学生要成为一名医生一样。
14	10	10	4	390	519	 Most training is concerned with technique, for musicians have to have the muscular proficiency of an athlete or a ballet dancer.	绝大多数的训练是技巧性的。 音乐家们控制肌肉的熟练程度，必须达到与运动员或巴蕾舞演员相当的水平。
15	10	10	5	519	636	 Singers practice breathing every day, as their vocal chords would be inadequate without controlled muscular support.	歌手们每天都练习吊嗓子，因为如果不能有效地控制肌肉的话，他们的声带将不能满足演唱的要求。
16	10	10	6	636	801	String players practice moving the fingers of the left hand up and down, while drawing the bow to and fro with the right arm -- two entirely different movements.	弦乐器的演奏者练习的则是在左手的手指上下滑动的同时，用右手前后拉动琴弓--两个截然不同的动作。
17	10	11	0	801	884	  Singers and instrumentalists have to be able to get every note perfectly in tune.	歌手和乐器演奏者必须使所有的音符完全相同协调。
18	10	11	1	884	1056	 Pianists are spared this particular anxiety, for the notes are already there, waiting for them, and it is the piano tuner's responsibility to tune the instrument for them.	钢琴家们则不用操这份心，因为每个音符都已在那里等待着他们了, 给钢琴调音是调音师的职责。
19	10	11	2	1056	1224	But they have their own difficulties: the hammers that hit the strings have to be coaxed not to sound like percussion, and each overlapping tone has to sound clear.	但调音师们也有他们的难处：他们必须耐心地调理敲击琴弦的音锤，不能让音锤发出的声音象是打击乐器，而且每个交叠的音都必须要清晰。
20	10	12	0	1224	1472	This problem of getting clear texture is one that confronts student conductors: they have to learn to know every note of the music and how it should sound, and they have to aim at controlling these sounds with fanatical but selfless authority.	如何得到乐章清晰的纹理是学生指挥们所面临的难题：他们必须学会了解音乐中的每一个音及其发音之道。他们还必须致力于以热忱而又客观的权威去控制这些音符。
21	10	13	0	1472	1560	  Technique is of no use unless it is combined with musical knowledge and understanding.	除非是和音乐方面的知识和悟性结合起来，单纯的技巧没有任何用处。
22	10	13	1	1560	1702	Great artists are those who are so thoroughly at home in the language of music that they can enjoy performing works written in any century.	艺术家之所以伟大在于他们对音乐语言驾轻就熟，以致于可以满怀喜悦地演出写于任何时代的作品。
\.


--
-- Data for Name: t_sources; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY t_sources (f_sourceid, f_name, f_date, f_format, f_owner, f_langsrc, f_langdst, f_sentences, f_domain, f_permit) FROM stdin;
10	Article I	2007-06-19 22:10:20.25	   	1	EN	ZH	0	00	0
\.


--
-- Data for Name: t_users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY t_users (f_userid, f_username, f_realname, f_sex, f_password, f_regdate, f_birthday, f_userip, f_membership, f_role, f_points, f_credits, f_email, f_mobile, f_idtype, f_idnumber, f_question, f_answer, f_state, f_work, f_group, f_lastvisit, f_visitcount, f_shared) FROM stdin;
2	Alex	Alex	M	E10ADC3949BA59ABBE56E057F20F883E	2007-06-19 09:16:15.187	\N	\N	T	U	100	0	alex@allwinworld.com	\N	\N	\N	1+1=?	2	A	0	1	2007-06-19 09:16:15.187	0	0
3	Kristy	Kristy	M	E10ADC3949BA59ABBE56E057F20F883E	2007-06-19 09:16:15.187	\N	\N	T	U	100	0	kristy@allwinworld.com	\N	\N	\N	1+1=?	2	A	0	1	2007-06-19 09:16:15.187	0	0
4	test	test	M	098F6BCD4621D373CADE4E832627B4F6	2007-06-19 09:16:15.203	\N	\N	T	U	100	0	test@allwinworld.com	\N	\N	\N	1+1=?	2	A	0	0	2007-06-19 09:16:15.203	0	0
1	TedWen	Ted	M	81DC9BDB52D04DC20036DBD8313ED055	2007-06-19 09:16:15.156	\N	\N	T	G	91	0	tedwen@nesc.ac.uk	\N	\N	\N	Where are you?	Edinburgh	A	0	1	2007-06-20 00:07:55.203	24	0
\.


--
-- Name: t_articles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY t_articles
    ADD CONSTRAINT t_articles_pkey PRIMARY KEY (f_articleid);


--
-- Name: t_corpora_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY t_corpora
    ADD CONSTRAINT t_corpora_pkey PRIMARY KEY (f_corpusid);


--
-- Name: t_domains_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY t_domains
    ADD CONSTRAINT t_domains_pkey PRIMARY KEY (f_domaincode);


--
-- Name: t_enzh_enx_pkey; Type: CONSTRAINT; Schema: public; Owner: tm; Tablespace: 
--

ALTER TABLE ONLY t_enzh_enx
    ADD CONSTRAINT t_enzh_enx_pkey PRIMARY KEY (f_word, f_sid);


--
-- Name: t_enzh_pkey; Type: CONSTRAINT; Schema: public; Owner: tm; Tablespace: 
--

ALTER TABLE ONLY t_enzh
    ADD CONSTRAINT t_enzh_pkey PRIMARY KEY (f_sid);


--
-- Name: t_enzh_zhx_pkey; Type: CONSTRAINT; Schema: public; Owner: tm; Tablespace: 
--

ALTER TABLE ONLY t_enzh_zhx
    ADD CONSTRAINT t_enzh_zhx_pkey PRIMARY KEY (f_word, f_sid);


--
-- Name: t_fileformats_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY t_fileformats
    ADD CONSTRAINT t_fileformats_pkey PRIMARY KEY (f_formatid);


--
-- Name: t_groups_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY t_groups
    ADD CONSTRAINT t_groups_pkey PRIMARY KEY (f_groupid);


--
-- Name: t_languages_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY t_languages
    ADD CONSTRAINT t_languages_pkey PRIMARY KEY (f_code);


--
-- Name: t_paragraphs_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY t_paragraphs
    ADD CONSTRAINT t_paragraphs_pkey PRIMARY KEY (f_paragraphid);


--
-- Name: t_parameters_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY t_parameters
    ADD CONSTRAINT t_parameters_pkey PRIMARY KEY (f_name);


--
-- Name: t_preferences_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY t_preferences
    ADD CONSTRAINT t_preferences_pkey PRIMARY KEY (f_preid);


--
-- Name: t_projects_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY t_projects
    ADD CONSTRAINT t_projects_pkey PRIMARY KEY (f_projectid);


--
-- Name: t_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY t_roles
    ADD CONSTRAINT t_roles_pkey PRIMARY KEY (f_roleid);


--
-- Name: t_sentences_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY t_sentences
    ADD CONSTRAINT t_sentences_pkey PRIMARY KEY (f_sentenceid);


--
-- Name: t_sources_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY t_sources
    ADD CONSTRAINT t_sources_pkey PRIMARY KEY (f_sourceid);


--
-- Name: t_users_f_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY t_users
    ADD CONSTRAINT t_users_f_username_key UNIQUE (f_username);


--
-- Name: t_users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY t_users
    ADD CONSTRAINT t_users_pkey PRIMARY KEY (f_userid);


--
-- Name: enzh_domain_index; Type: INDEX; Schema: public; Owner: tm; Tablespace: 
--

CREATE INDEX enzh_domain_index ON t_enzh USING btree (f_domain);


--
-- Name: enzh_permit_index; Type: INDEX; Schema: public; Owner: tm; Tablespace: 
--

CREATE INDEX enzh_permit_index ON t_enzh USING btree (f_permit);


--
-- Name: groups_name_index; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX groups_name_index ON t_groups USING btree (f_groupname);


--
-- Name: idx_articles_project; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_articles_project ON t_articles USING btree (f_project);


--
-- Name: idx_articles_translator; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_articles_translator ON t_articles USING btree (f_translator);


--
-- Name: paragraph_article_index; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX paragraph_article_index ON t_paragraphs USING btree (f_article);


--
-- Name: prefuserindex; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX prefuserindex ON t_preferences USING btree (f_user);


--
-- Name: project_creatorindex; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX project_creatorindex ON t_projects USING btree (f_creator);


--
-- Name: sentence_article_index; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX sentence_article_index ON t_sentences USING btree (f_article);


--
-- Name: sentence_paragraph_index; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX sentence_paragraph_index ON t_sentences USING btree (f_paragraph);


--
-- Name: usergroupindex; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX usergroupindex ON t_users USING btree (f_group);


--
-- Name: usernameindex; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX usernameindex ON t_users USING btree (f_username);


--
-- Name: t_articles_f_fileformat_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_articles
    ADD CONSTRAINT t_articles_f_fileformat_fkey FOREIGN KEY (f_fileformat) REFERENCES t_fileformats(f_formatid);


--
-- Name: t_articles_f_langdst_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_articles
    ADD CONSTRAINT t_articles_f_langdst_fkey FOREIGN KEY (f_langdst) REFERENCES t_languages(f_code);


--
-- Name: t_articles_f_langsrc_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_articles
    ADD CONSTRAINT t_articles_f_langsrc_fkey FOREIGN KEY (f_langsrc) REFERENCES t_languages(f_code);


--
-- Name: t_articles_f_project_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_articles
    ADD CONSTRAINT t_articles_f_project_fkey FOREIGN KEY (f_project) REFERENCES t_projects(f_projectid) ON DELETE CASCADE;


--
-- Name: t_articles_f_translator_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_articles
    ADD CONSTRAINT t_articles_f_translator_fkey FOREIGN KEY (f_translator) REFERENCES t_users(f_userid);


--
-- Name: t_enzh_enx_f_sid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: tm
--

ALTER TABLE ONLY t_enzh_enx
    ADD CONSTRAINT t_enzh_enx_f_sid_fkey FOREIGN KEY (f_sid) REFERENCES t_enzh(f_sid) ON DELETE CASCADE;


--
-- Name: t_enzh_zhx_f_sid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: tm
--

ALTER TABLE ONLY t_enzh_zhx
    ADD CONSTRAINT t_enzh_zhx_f_sid_fkey FOREIGN KEY (f_sid) REFERENCES t_enzh(f_sid) ON DELETE CASCADE;


--
-- Name: t_paragraphs_f_article_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_paragraphs
    ADD CONSTRAINT t_paragraphs_f_article_fkey FOREIGN KEY (f_article) REFERENCES t_articles(f_articleid) ON DELETE CASCADE;


--
-- Name: t_preferences_f_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_preferences
    ADD CONSTRAINT t_preferences_f_user_fkey FOREIGN KEY (f_user) REFERENCES t_users(f_userid);


--
-- Name: t_projects_f_creator_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_projects
    ADD CONSTRAINT t_projects_f_creator_fkey FOREIGN KEY (f_creator) REFERENCES t_users(f_userid);


--
-- Name: t_projects_f_langdst_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_projects
    ADD CONSTRAINT t_projects_f_langdst_fkey FOREIGN KEY (f_langdst) REFERENCES t_languages(f_code);


--
-- Name: t_projects_f_langsrc_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_projects
    ADD CONSTRAINT t_projects_f_langsrc_fkey FOREIGN KEY (f_langsrc) REFERENCES t_languages(f_code);


--
-- Name: t_sentences_f_article_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_sentences
    ADD CONSTRAINT t_sentences_f_article_fkey FOREIGN KEY (f_article) REFERENCES t_articles(f_articleid) ON DELETE CASCADE;


--
-- Name: t_sources_f_langdst_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_sources
    ADD CONSTRAINT t_sources_f_langdst_fkey FOREIGN KEY (f_langdst) REFERENCES t_languages(f_code);


--
-- Name: t_sources_f_langsrc_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_sources
    ADD CONSTRAINT t_sources_f_langsrc_fkey FOREIGN KEY (f_langsrc) REFERENCES t_languages(f_code);


--
-- Name: t_users_f_group_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_users
    ADD CONSTRAINT t_users_f_group_fkey FOREIGN KEY (f_group) REFERENCES t_groups(f_groupid);


--
-- Name: t_users_f_role_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_users
    ADD CONSTRAINT t_users_f_role_fkey FOREIGN KEY (f_role) REFERENCES t_roles(f_roleid);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- Name: s_articles; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE s_articles FROM PUBLIC;
REVOKE ALL ON SEQUENCE s_articles FROM postgres;
GRANT ALL ON SEQUENCE s_articles TO postgres;
GRANT SELECT,UPDATE ON SEQUENCE s_articles TO "Users";
GRANT ALL ON SEQUENCE s_articles TO "Admins";


--
-- Name: s_corpora; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE s_corpora FROM PUBLIC;
REVOKE ALL ON SEQUENCE s_corpora FROM postgres;
GRANT ALL ON SEQUENCE s_corpora TO postgres;
GRANT SELECT,UPDATE ON SEQUENCE s_corpora TO "Users";
GRANT ALL ON SEQUENCE s_corpora TO "Admins";


--
-- Name: s_groups; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE s_groups FROM PUBLIC;
REVOKE ALL ON SEQUENCE s_groups FROM postgres;
GRANT ALL ON SEQUENCE s_groups TO postgres;
GRANT SELECT,UPDATE ON SEQUENCE s_groups TO "Users";
GRANT ALL ON SEQUENCE s_groups TO "Admins";


--
-- Name: s_paragraphs; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE s_paragraphs FROM PUBLIC;
REVOKE ALL ON SEQUENCE s_paragraphs FROM postgres;
GRANT ALL ON SEQUENCE s_paragraphs TO postgres;
GRANT SELECT,UPDATE ON SEQUENCE s_paragraphs TO "Users";
GRANT ALL ON SEQUENCE s_paragraphs TO "Admins";


--
-- Name: s_preferences; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE s_preferences FROM PUBLIC;
REVOKE ALL ON SEQUENCE s_preferences FROM postgres;
GRANT ALL ON SEQUENCE s_preferences TO postgres;
GRANT SELECT,UPDATE ON SEQUENCE s_preferences TO "Users";
GRANT ALL ON SEQUENCE s_preferences TO "Admins";


--
-- Name: s_projects; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE s_projects FROM PUBLIC;
REVOKE ALL ON SEQUENCE s_projects FROM postgres;
GRANT ALL ON SEQUENCE s_projects TO postgres;
GRANT SELECT,UPDATE ON SEQUENCE s_projects TO "Users";
GRANT ALL ON SEQUENCE s_projects TO "Admins";


--
-- Name: s_sentences; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE s_sentences FROM PUBLIC;
REVOKE ALL ON SEQUENCE s_sentences FROM postgres;
GRANT ALL ON SEQUENCE s_sentences TO postgres;
GRANT SELECT,UPDATE ON SEQUENCE s_sentences TO "Users";
GRANT ALL ON SEQUENCE s_sentences TO "Admins";


--
-- Name: s_sources; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE s_sources FROM PUBLIC;
REVOKE ALL ON SEQUENCE s_sources FROM postgres;
GRANT ALL ON SEQUENCE s_sources TO postgres;
GRANT SELECT,UPDATE ON SEQUENCE s_sources TO "Users";
GRANT ALL ON SEQUENCE s_sources TO "Admins";


--
-- Name: s_users; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE s_users FROM PUBLIC;
REVOKE ALL ON SEQUENCE s_users FROM postgres;
GRANT ALL ON SEQUENCE s_users TO postgres;
GRANT SELECT,UPDATE ON SEQUENCE s_users TO "Users";
GRANT ALL ON SEQUENCE s_users TO "Admins";


--
-- Name: t_articles; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE t_articles FROM PUBLIC;
REVOKE ALL ON TABLE t_articles FROM postgres;
GRANT ALL ON TABLE t_articles TO postgres;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE t_articles TO "Users";
GRANT ALL ON TABLE t_articles TO "Admins";


--
-- Name: t_corpora; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE t_corpora FROM PUBLIC;
REVOKE ALL ON TABLE t_corpora FROM postgres;
GRANT ALL ON TABLE t_corpora TO postgres;
GRANT SELECT,INSERT,UPDATE ON TABLE t_corpora TO "Users";
GRANT ALL ON TABLE t_corpora TO "Admins";


--
-- Name: t_domains; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE t_domains FROM PUBLIC;
REVOKE ALL ON TABLE t_domains FROM postgres;
GRANT ALL ON TABLE t_domains TO postgres;
GRANT SELECT ON TABLE t_domains TO "Users";
GRANT ALL ON TABLE t_domains TO "Admins";


--
-- Name: t_fileformats; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE t_fileformats FROM PUBLIC;
REVOKE ALL ON TABLE t_fileformats FROM postgres;
GRANT ALL ON TABLE t_fileformats TO postgres;
GRANT SELECT ON TABLE t_fileformats TO "Users";
GRANT ALL ON TABLE t_fileformats TO "Admins";


--
-- Name: t_groups; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE t_groups FROM PUBLIC;
REVOKE ALL ON TABLE t_groups FROM postgres;
GRANT ALL ON TABLE t_groups TO postgres;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE t_groups TO "Users";
GRANT ALL ON TABLE t_groups TO "Admins";


--
-- Name: t_languages; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE t_languages FROM PUBLIC;
REVOKE ALL ON TABLE t_languages FROM postgres;
GRANT ALL ON TABLE t_languages TO postgres;
GRANT SELECT ON TABLE t_languages TO "Users";
GRANT ALL ON TABLE t_languages TO "Admins";


--
-- Name: t_paragraphs; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE t_paragraphs FROM PUBLIC;
REVOKE ALL ON TABLE t_paragraphs FROM postgres;
GRANT ALL ON TABLE t_paragraphs TO postgres;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE t_paragraphs TO "Users";
GRANT ALL ON TABLE t_paragraphs TO "Admins";


--
-- Name: t_parameters; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE t_parameters FROM PUBLIC;
REVOKE ALL ON TABLE t_parameters FROM postgres;
GRANT ALL ON TABLE t_parameters TO postgres;
GRANT SELECT ON TABLE t_parameters TO "Users";
GRANT ALL ON TABLE t_parameters TO "Admins";


--
-- Name: t_preferences; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE t_preferences FROM PUBLIC;
REVOKE ALL ON TABLE t_preferences FROM postgres;
GRANT ALL ON TABLE t_preferences TO postgres;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE t_preferences TO "Users";
GRANT ALL ON TABLE t_preferences TO "Admins";


--
-- Name: t_projects; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE t_projects FROM PUBLIC;
REVOKE ALL ON TABLE t_projects FROM postgres;
GRANT ALL ON TABLE t_projects TO postgres;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE t_projects TO "Users";
GRANT ALL ON TABLE t_projects TO "Admins";


--
-- Name: t_roles; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE t_roles FROM PUBLIC;
REVOKE ALL ON TABLE t_roles FROM postgres;
GRANT ALL ON TABLE t_roles TO postgres;
GRANT SELECT ON TABLE t_roles TO "Users";
GRANT ALL ON TABLE t_roles TO "Admins";


--
-- Name: t_sentences; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE t_sentences FROM PUBLIC;
REVOKE ALL ON TABLE t_sentences FROM postgres;
GRANT ALL ON TABLE t_sentences TO postgres;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE t_sentences TO "Users";
GRANT ALL ON TABLE t_sentences TO "Admins";


--
-- Name: t_sources; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE t_sources FROM PUBLIC;
REVOKE ALL ON TABLE t_sources FROM postgres;
GRANT ALL ON TABLE t_sources TO postgres;
GRANT SELECT,INSERT,DELETE ON TABLE t_sources TO "Users";
GRANT ALL ON TABLE t_sources TO "Admins";


--
-- Name: t_users; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE t_users FROM PUBLIC;
REVOKE ALL ON TABLE t_users FROM postgres;
GRANT ALL ON TABLE t_users TO postgres;
GRANT SELECT,INSERT,UPDATE ON TABLE t_users TO "Users";
GRANT ALL ON TABLE t_users TO "Admins";


--
-- PostgreSQL database dump complete
--

