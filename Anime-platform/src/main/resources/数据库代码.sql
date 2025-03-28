-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: anime_platform
-- ------------------------------------------------------
-- Server version	8.0.34

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `animes`
--

DROP TABLE IF EXISTS `animes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `animes` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '动漫ID',
  `title` varchar(255) NOT NULL COMMENT '标题',
  `description` text COMMENT '描述',
  `category_id` int DEFAULT NULL COMMENT '类别ID',
  `video_url` varchar(255) DEFAULT NULL COMMENT '视频URL',
  `thumbnail_url` varchar(255) DEFAULT NULL COMMENT '缩略图URL',
  `release_date` date DEFAULT NULL COMMENT '发行日期',
  `view_count` int DEFAULT '0' COMMENT '观看次数',
  `is_vip_only` tinyint(1) DEFAULT '0' COMMENT '是否仅限VIP观看',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `animes_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `animes`
--

LOCK TABLES `animes` WRITE;
/*!40000 ALTER TABLE `animes` DISABLE KEYS */;
INSERT INTO `animes` VALUES (1,'这个公司有我喜欢的人','讲述了在糖果制造商会计部工作的认真男人立石真直和在糖果制造商企划部工作的好胜女孩三谷结衣的故事。',3,'http://localhost:8080/video/fb5b7445-76aa-4892-8203-3e950eee22a8.mp4','http://localhost:8080/image/eb7f3bd4-edf1-48d3-b44b-b529509c1a11.jpg','1997-10-20',10307,0,'2025-03-18 13:55:24'),(2,'我推的孩子','妇产科医生雨宫吾郎是少女偶像星野爱的“单推人”（粉丝），在工作中遇到了一名秘密前来问诊、怀有双胞胎的16岁孕妇，而她竟是星野爱本人！吾郎大受打击，但还是尊重星野爱想要秘密产子的决定，作为医生帮助着她。在双子出生之夜，吾郎被星野爱的一名“私生饭”（极端粉丝）杀害，之后竟然转生成了星野爱的儿子星野爱久爱海（阿库亚）。双子中的另一位名为星野瑠美衣（露比），是一位憧憬星野爱却遗憾因病离世的女孩的转世。两兄妹逐渐长大，分别展现出演员和偶像的天赋，也逐渐接触到演艺圈的一些内幕。',5,'http://localhost:8080/video/021dd216-c52e-44c8-8a83-b50cfad91535.mp4','http://localhost:8080/image/1afb533a-2d76-47af-9387-27cb51635dc5.jpg','2002-10-03',8021,0,'2025-03-18 13:55:24'),(4,'坂本日常','曾有一名最强杀手，名叫坂本太郎。这个受到所有恶棍畏惧，受到所有杀手仰慕的男人——在某一天，坠入了爱河。坂本对在便利店工作的葵一见钟情，很快就从杀手界引退。他在结婚后生下女儿，在一个祥和的城市开着私营商店，变得和以前判若两人，严重发福了。\n为了守护心爱的家人与安稳的日常，前传奇杀手迎战接连袭来的刺客。\n日常与非日常交织的硬核战斗剧，就此开幕。',1,'http://localhost:8080/video/ee645486-ec7b-4eb2-a08f-55528bc31727.mp4','http://localhost:8080/image/81016e27-fd2b-4096-9993-b37edc74b96d.webp','2025-03-20',15,0,'2025-03-19 09:12:29'),(10,'地缚少年花子君','海鸥学园流传着七大不可思议的奇妙传言。旧校舍3楼的女生厕所的第3个隔间里有一位名为“花子”的幽灵，传说那位“花子”可以实现到访者的任何愿望。而八寻宁宁为了实现自己的愿望，将可能性都寄托在了这个学校的怪谈上…新锐作家あいだいろ为您奉上的温馨厕所喜剧！',5,'http://localhost:8080/video/8c4cdf86-bdfa-46f3-9aac-d50d24a57211.mp4','http://localhost:8080/image/e8b0de3b-dc4c-4b7d-a16b-fb499db130b1.webp','1999-10-20',2000020,1,'2025-03-19 14:33:41'),(11,'关于我转生成史莱姆这档事','三上悟过着不起眼的人生，在随机杀人魔肆虐下结束了三十七年生涯，看似如此。\n当他苏醒时，不仅眼睛看不见，就连耳朵也听不到。面对一连串突发状况，他意识到自己投胎转世成“史莱姆”。\n尽管变成最弱魔物让他颇有怨言，三上悟还是决定要快乐地过史莱姆生活，没想到却碰上天灾级魔物“暴风龙维尔德拉”，命运就此出现巨大转折。\n维尔德拉将他命名为“利姆鲁”，正要展开史莱姆式的异世界新生活时，却被卷入哥布林对牙狼族的纷争之中，最后还莫名其妙当上魔物大王。\n能夺取对手能力的“捕食者”以及精通世界真理的“大贤者”，有这两项特殊技能当武器，最强的史莱姆传说正式展开。',5,'http://localhost:8080/video/4152a92f-f267-44a1-a38b-fcdfd0a41ec7.mp4','http://localhost:8080/image/47ab1503-ad69-443e-a727-de69b5014a05.webp','2014-04-07',1200003,0,'2025-03-19 14:33:41'),(12,'我独自升级','10年前，世界与其他次元链接的“门”被打开，各种魔物不断出现，于是乎能力各异的猎魔者也随之出现，被称为“猎人”。程肖宇是一名实力最弱的E级猎人，在一次挑战任务中，遇到了可怕的隐藏挑战。生死存亡之际，他居然获得了升级系统！在系统的利用下，他能成为最强猎人吗？',1,'http://localhost:8080/video/94d1d477-4d06-4281-a600-a565eee01fbd.mp4','http://localhost:8080/image/7fbd7763-a0be-412c-989f-82f943e31d00.webp','2011-04-03',1800006,0,'2025-03-19 14:33:41'),(13,'香格里拉边境','你是为了什么而玩游戏的？如果世上有100部神作，那么也存在着1000部粪作这是喜爱粪作，同时也被粪作所爱的男人“阳务乐郎”，向粪作的对立面——神作《香格里拉边境》发起挑战的故事。\n该作品通过每个人都能拥有的游戏体验来描绘出一种新的幻想世界，为沉浸在过去记忆中的大人和生活在科技最前沿的年轻人都带来了新的冒险与刺激。\n一名“粪作猎人”向神作发起挑战，至高无上的“游戏x幻想”的冒险故事，就此开幕。',1,'http://localhost:8080/video/dcb0493b-9cd0-44ad-8a27-ed95801d3202.mp4','http://localhost:8080/image/71044dec-6f90-4b87-b82e-5aa12984f5ba.webp','2025-03-19',4,0,'2025-03-20 13:24:12'),(14,'紫罗兰永恒花园','“自动手记人偶”这个名字名噪一时的时候，已经是相当一段时间前的事了。奥兰德博士发明了用于书写记录人声话语的机械。当初他仅仅为了爱妻而制造的这一机械，不知何时已普及至全世界，甚至设立了提供借出服务的机关。\n若是客人您有所期望的话，我可以去往任何地方。我是，自动手记人偶服务人员，薇尔莉特·伊芙加登。\n有着如同从故事书中出现般的样貌，金发碧眼的女子，保持着无机质的美，以玲珑剔透的声音如此说到。',3,'http://localhost:8080/video/722619a2-08aa-44c9-9c25-b1b1c1435811.mp4','http://localhost:8080/image/4c1aeff5-51cd-4041-8f5c-34cdead980bf.webp','2025-02-25',9,0,'2025-03-20 13:47:17');
/*!40000 ALTER TABLE `animes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '类别ID',
  `name` varchar(50) NOT NULL COMMENT '类别名称',
  `description` text COMMENT '描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'动作','包含各种动作冒险类动漫'),(2,'科幻','包含未来科技和幻想世界的动漫'),(3,'爱情','讲述甜蜜而感人的恋爱故事33'),(5,'喜剧','包含幽默风趣的动漫'),(7,'治愈','治愈动漫'),(8,'运动','运动动漫'),(9,'泡面','轻松一类的弹幕'),(11,'热血','热血动漫');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment_likes`
--

DROP TABLE IF EXISTS `comment_likes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment_likes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `comment_id` int NOT NULL COMMENT '评论ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_comment_user` (`comment_id`,`user_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='评论点赞';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment_likes`
--

LOCK TABLES `comment_likes` WRITE;
/*!40000 ALTER TABLE `comment_likes` DISABLE KEYS */;
INSERT INTO `comment_likes` VALUES (8,1,1,'2025-03-26 17:59:16');
/*!40000 ALTER TABLE `comment_likes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comments` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `anime_id` int DEFAULT NULL COMMENT '动漫ID',
  `user_id` int DEFAULT NULL COMMENT '用户ID',
  `content` text NOT NULL COMMENT '内容',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `comments_ibfk_1` (`anime_id`),
  KEY `comments_ibfk_2` (`user_id`),
  CONSTRAINT `comments_ibfk_1` FOREIGN KEY (`anime_id`) REFERENCES `animes` (`id`) ON DELETE CASCADE,
  CONSTRAINT `comments_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comments`
--

LOCK TABLES `comments` WRITE;
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;
INSERT INTO `comments` VALUES (14,4,1,'23423','2025-03-24 15:36:18'),(16,1,2,'werqwerwq','2025-03-24 16:35:41'),(17,1,2,'rwqrwq','2025-03-24 16:35:42'),(18,1,2,'rewqrqw','2025-03-24 16:35:43'),(19,1,1,'222','2025-03-24 17:00:44'),(22,10,1,'hhh','2025-03-26 15:03:41');
/*!40000 ALTER TABLE `comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `danmaku`
--

DROP TABLE IF EXISTS `danmaku`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `danmaku` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '弹幕ID',
  `anime_id` int DEFAULT NULL COMMENT '动漫ID',
  `user_id` int DEFAULT NULL COMMENT '用户ID',
  `content` text NOT NULL COMMENT '内容',
  `time` float DEFAULT NULL COMMENT '时间戳',
  `color` varchar(7) DEFAULT '#FFFFFF' COMMENT '颜色',
  `font_size` tinyint DEFAULT '25' COMMENT '字体大小',
  `type` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `isMe` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `danmaku_ibfk_1` (`anime_id`),
  KEY `danmaku_ibfk_2` (`user_id`),
  CONSTRAINT `danmaku_ibfk_1` FOREIGN KEY (`anime_id`) REFERENCES `animes` (`id`) ON DELETE CASCADE,
  CONSTRAINT `danmaku_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `danmaku`
--

LOCK TABLES `danmaku` WRITE;
/*!40000 ALTER TABLE `danmaku` DISABLE KEYS */;
INSERT INTO `danmaku` VALUES (1,1,2,'精彩的一集！',2,'#FF0000',25,'top','2025-03-18 13:55:24',0),(2,2,3,'我喜欢这个情节！',3,'#FFFF00',30,'top','2025-03-18 13:55:24',0),(4,1,1,'111111',4,'#FFFF00',NULL,NULL,'2025-03-23 14:15:14',0),(5,1,1,'12312421',5,'#FFFFFF',25,NULL,'2025-03-25 13:40:52',0),(6,1,2,'呜呜呜呜',2,'#FFFFFF',25,NULL,'2025-03-25 13:41:22',0),(7,1,2,'2131',1,'#FFFFFF',25,NULL,'2025-03-25 13:41:33',0),(8,1,1,'我袋米',10.0433,'#FFFFFF',25,'scroll','2025-03-25 16:21:15',0),(9,1,1,'你好啊',4.29244,'#FFFFFF',25,'scroll','2025-03-25 16:22:47',0),(10,1,1,'我问问',10.8349,'#FFFFFF',25,'scroll','2025-03-25 16:23:46',0),(11,1,1,'啦啦啦啦啦',5.21627,'#FFFFFF',25,'scroll','2025-03-25 16:25:59',0),(18,12,1,'哈哈哈哈',22.6051,'#FFFFFF',25,'scroll','2025-03-27 16:33:33',0);
/*!40000 ALTER TABLE `danmaku` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `favorites`
--

DROP TABLE IF EXISTS `favorites`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `favorites` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `user_id` int DEFAULT NULL COMMENT '用户ID',
  `anime_id` int DEFAULT NULL COMMENT '动漫ID',
  `favorited_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  PRIMARY KEY (`id`),
  KEY `fk_user_id` (`user_id`),
  KEY `fk_favorites_anime_id` (`anime_id`),
  CONSTRAINT `fk_favorites_anime_id` FOREIGN KEY (`anime_id`) REFERENCES `animes` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `favorites`
--

LOCK TABLES `favorites` WRITE;
/*!40000 ALTER TABLE `favorites` DISABLE KEYS */;
INSERT INTO `favorites` VALUES (1,2,1,'2025-03-18 13:55:24'),(2,3,2,'2025-03-18 13:55:24'),(13,1,2,'2025-03-22 09:37:44'),(18,1,4,'2025-03-22 17:04:56'),(21,1,10,'2025-03-23 20:44:09'),(26,1,1,'2025-03-24 11:07:52');
/*!40000 ALTER TABLE `favorites` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_comments`
--

DROP TABLE IF EXISTS `post_comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_comments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `post_id` int NOT NULL COMMENT '帖子ID',
  `user_id` int NOT NULL COMMENT '评论人ID',
  `content` text NOT NULL COMMENT '评论内容',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  `like_count` int NOT NULL DEFAULT '0' COMMENT '点赞数',
  `parent_id` int DEFAULT NULL COMMENT '父评论ID',
  PRIMARY KEY (`id`),
  KEY `idx_post_id` (`post_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='帖子评论';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_comments`
--

LOCK TABLES `post_comments` WRITE;
/*!40000 ALTER TABLE `post_comments` DISABLE KEYS */;
INSERT INTO `post_comments` VALUES (1,5,1,'123','2025-03-26 17:11:49',0,1,NULL),(2,5,1,'你好','2025-03-26 17:59:33',1,0,NULL),(3,5,1,'你好','2025-03-26 18:46:52',0,0,NULL),(4,5,1,'111','2025-03-26 18:49:24',1,0,NULL),(5,5,1,'你好','2025-03-26 19:04:13',1,0,NULL),(6,5,1,'你好','2025-03-26 19:06:24',1,0,NULL),(7,5,2,'nihao ','2025-03-26 19:07:57',1,0,NULL),(8,5,2,'nmihoa','2025-03-26 19:10:45',0,0,NULL),(9,5,2,'你好','2025-03-26 19:13:59',0,0,1),(10,8,1,'sfs','2025-03-26 19:52:28',0,0,NULL);
/*!40000 ALTER TABLE `post_comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_likes`
--

DROP TABLE IF EXISTS `post_likes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_likes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `post_id` int NOT NULL COMMENT '帖子ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_post_user` (`post_id`,`user_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='帖子点赞';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_likes`
--

LOCK TABLES `post_likes` WRITE;
/*!40000 ALTER TABLE `post_likes` DISABLE KEYS */;
INSERT INTO `post_likes` VALUES (1,5,1,'2025-03-26 16:58:50');
/*!40000 ALTER TABLE `post_likes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `posts`
--

DROP TABLE IF EXISTS `posts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `posts` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL COMMENT '帖子标题',
  `content` text NOT NULL COMMENT '帖子内容',
  `category` varchar(50) NOT NULL COMMENT '帖子分类',
  `user_id` int NOT NULL COMMENT '发帖人ID',
  `view_count` int NOT NULL DEFAULT '0' COMMENT '浏览量',
  `like_count` int NOT NULL DEFAULT '0' COMMENT '点赞数',
  `comment_count` int NOT NULL DEFAULT '0' COMMENT '评论数',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_category` (`category`),
  KEY `idx_created_at` (`created_at`),
  FULLTEXT KEY `idx_post_content` (`title`,`content`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='论坛帖子';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `posts`
--

LOCK TABLES `posts` WRITE;
/*!40000 ALTER TABLE `posts` DISABLE KEYS */;
INSERT INTO `posts` VALUES (2,'123','123','讨论',1,2,0,0,'2025-03-26 16:13:52',NULL,1),(3,'123124','123123','讨论',1,3,0,0,'2025-03-26 16:16:32',NULL,0),(4,'123','123','讨论',1,10,0,0,'2025-03-26 16:16:41',NULL,0),(5,'123','12345','讨论',1,64,1,4,'2025-03-26 16:20:22','2025-03-26 19:17:20',0),(6,'fasfa','sdfasef','公告',1,2,0,0,'2025-03-26 19:17:49',NULL,0),(7,'sfs','fsfs','讨论',1,6,0,0,'2025-03-26 19:17:59',NULL,0),(8,'dasfa','asdfas','分享',2,11,0,1,'2025-03-26 19:19:44',NULL,0);
/*!40000 ALTER TABLE `posts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reports`
--

DROP TABLE IF EXISTS `reports`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reports` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '举报ID',
  `user_id` int DEFAULT NULL COMMENT '用户ID',
  `reported_user_id` int DEFAULT NULL COMMENT '被举报用户ID',
  `reason` text NOT NULL COMMENT '原因',
  `reported_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '举报时间',
  `comment_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_reports_user_id` (`user_id`),
  KEY `fk_reports_reported_user_id` (`reported_user_id`),
  CONSTRAINT `fk_reports_reported_user_id` FOREIGN KEY (`reported_user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_reports_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reports`
--

LOCK TABLES `reports` WRITE;
/*!40000 ALTER TABLE `reports` DISABLE KEYS */;
INSERT INTO `reports` VALUES (1,2,3,'该用户发布了不当内容','2025-03-18 13:55:24',NULL),(2,3,2,'骚扰行为','2025-03-18 13:55:24',NULL),(4,1,2,'pl','2025-03-24 16:51:02',NULL),(7,1,2,'sdafa','2025-03-24 17:09:32',NULL),(8,1,2,'1111','2025-03-24 17:48:10',17),(9,1,2,'12312','2025-03-24 17:48:48',18);
/*!40000 ALTER TABLE `reports` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL COMMENT '密码',
  `role` enum('admin','user') DEFAULT 'user' COMMENT '角色（管理员或用户）',
  `is_active` tinyint(1) DEFAULT '1' COMMENT '是否激活',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `is_vip` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin','admin@examp11le.com','123456','admin',1,'2025-03-18 13:55:24',1),(2,'user1','user1@example.com','123','user',1,'2025-03-18 13:55:24',1),(3,'user2','user2@example.com','123','user',0,'2025-03-18 13:55:24',0),(6,'newuser','newuser@example.com','securepassword123','user',1,'2025-03-18 20:05:35',1),(7,'wabhd','111111@example.com','123','user',1,'2025-03-19 14:31:52',0),(8,'sfsfsfs','111qeqeq11@example.com','123','user',1,'2025-03-19 14:34:33',0),(9,'asdfasd','qq626367249@gmail.com','123','user',1,'2025-03-19 17:29:39',0),(16,'112321','1111@qq.com12312','1231','user',1,'2025-03-19 19:13:46',0),(20,'sjvlkaj','2131321@ewrw','12345','user',1,'2025-03-19 19:28:39',0);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `watch_history`
--

DROP TABLE IF EXISTS `watch_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `watch_history` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '观看历史ID',
  `user_id` int DEFAULT NULL COMMENT '用户ID',
  `anime_id` int DEFAULT NULL COMMENT '动漫ID',
  `watched_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '观看时间',
  PRIMARY KEY (`id`),
  KEY `watch_history_ibfk_2` (`anime_id`),
  KEY `watch_history_ibfk_1` (`user_id`),
  CONSTRAINT `watch_history_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `watch_history_ibfk_2` FOREIGN KEY (`anime_id`) REFERENCES `animes` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=501 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `watch_history`
--

LOCK TABLES `watch_history` WRITE;
/*!40000 ALTER TABLE `watch_history` DISABLE KEYS */;
INSERT INTO `watch_history` VALUES (48,2,13,'2025-03-22 10:53:12'),(97,2,2,'2025-03-22 20:19:39'),(169,2,10,'2025-03-23 21:27:38'),(279,2,1,'2025-03-24 16:35:37'),(453,1,13,'2025-03-26 10:16:36'),(471,1,12,'2025-03-27 16:33:10'),(475,1,11,'2025-03-27 17:16:50'),(487,1,10,'2025-03-27 18:52:11'),(495,1,14,'2025-03-27 19:19:25'),(497,1,1,'2025-03-27 19:28:48'),(499,1,4,'2025-03-27 19:40:35'),(500,1,2,'2025-03-27 19:41:13');
/*!40000 ALTER TABLE `watch_history` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-28 11:09:35
