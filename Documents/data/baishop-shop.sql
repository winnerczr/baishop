/*
SQLyog Ultimate v9.62 
MySQL - 5.5.20 : Database - baishop-shop
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`baishop-shop` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `baishop-shop`;

/*Table structure for table `bai_booking` */

DROP TABLE IF EXISTS `bai_booking`;

CREATE TABLE `bai_booking` (
  `booking_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '缺货登记表的自增id',
  `user_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '用户id，bai_users的user_id',
  `email` varchar(255) NOT NULL DEFAULT '' COMMENT '邮箱',
  `consignee` varchar(255) NOT NULL DEFAULT '' COMMENT '联系人姓名，默认取bai_user_address的consignee',
  `phone` varchar(255) NOT NULL DEFAULT '' COMMENT '联系人电话，默认取bai_user_address的mobile或telephone',
  `goods_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '缺货登记的商品id，商品表bai_goods的goods_id',
  `goods_desc` varchar(1000) NOT NULL DEFAULT '' COMMENT '缺货登记时留的订购描述',
  `goods_number` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '订购数量',
  `booking_time` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '缺货登记的时间',
  `is_dispose` tinyint(3) NOT NULL DEFAULT '0' COMMENT '是否已处理',
  `dispose_user` varchar(255) NOT NULL DEFAULT '' COMMENT '处理该缺货登记的管理员用户名',
  `dispose_time` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '处理的时间',
  `dispose_note` varchar(1000) NOT NULL DEFAULT '' COMMENT '处理时管理员留的备注',
  PRIMARY KEY (`booking_id`),
  KEY `idx_booking_is_dispose` (`is_dispose`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='缺货登记的订购和处理记录表';

/*Data for the table `bai_booking` */

/*Table structure for table `bai_brands` */

DROP TABLE IF EXISTS `bai_brands`;

CREATE TABLE `bai_brands` (
  `brand_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '品牌自增id',
  `brand_name` varchar(255) NOT NULL DEFAULT '' COMMENT '品牌名称',
  `brand_logo` varchar(255) NOT NULL DEFAULT '' COMMENT '上传的该品牌公司logo图片',
  `brand_desc` varchar(2000) NOT NULL DEFAULT '' COMMENT '品牌描述',
  `sort` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '品牌在前台页面的显示顺序，数字越大越靠后',
  `is_show` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '该品牌是否可见，枚举103：1，可见；0，不可见',
  PRIMARY KEY (`brand_id`),
  KEY `idx_sort` (`sort`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='商品的品牌定义表';

/*Data for the table `bai_brands` */

insert  into `bai_brands`(`brand_id`,`brand_name`,`brand_logo`,`brand_desc`,`sort`,`is_show`) values (1,'百鞋诚','','',1,0),(2,'奥康','','',2,0),(3,'康耐','','',3,0);

/*Table structure for table `bai_category` */

DROP TABLE IF EXISTS `bai_category`;

CREATE TABLE `bai_category` (
  `cate_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '类别自增id',
  `cate_parent` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '父类别id',
  `cate_path` varchar(255) NOT NULL DEFAULT '' COMMENT '树的路径',
  `cate_name` varchar(255) NOT NULL DEFAULT '' COMMENT '类别名称',
  `cate_desc` varchar(255) NOT NULL DEFAULT '' COMMENT '描述（备注）',
  `leaf` tinyint(3) NOT NULL DEFAULT '0' COMMENT '是否是叶子节点',
  `sort` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '类目的排序',
  PRIMARY KEY (`cate_id`),
  KEY `idx_cate_parent` (`cate_parent`),
  KEY `idx_sort` (`sort`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='商品的类型定义表';

/*Data for the table `bai_category` */

insert  into `bai_category`(`cate_id`,`cate_parent`,`cate_path`,`cate_name`,`cate_desc`,`leaf`,`sort`) values (1,0,'','流行男鞋','',0,12),(2,1,'','低帮鞋','',0,12),(3,1,'','高帮鞋','',0,13),(4,1,'','帆布鞋','',0,14),(5,1,'','靴子','',0,15),(6,1,'','凉鞋','',0,16),(7,1,'','拖鞋','',0,17),(8,1,'','奥康','',1,18),(9,1,'','意尔康','',1,19),(10,4,'','凡客','',1,410),(11,4,'','百鞋诚','',1,411),(12,0,'','时尚女鞋','',0,1),(14,12,'','低帮鞋','',0,1214),(15,12,'','高帮鞋','',0,1215),(16,12,'','帆布鞋','',0,1216),(17,12,'','靴子','',0,1217),(18,12,'','凉鞋','',0,1218),(19,12,'','拖鞋','',0,1219),(20,14,'','奥康','',1,1420),(21,14,'','意尔康','',1,1421),(22,16,'','凡客','',1,1622),(23,16,'','百鞋诚','',1,1623),(24,17,'','达芙妮','',0,1724),(25,17,'','香奈尔','',0,1725);

/*Table structure for table `bai_goods` */

DROP TABLE IF EXISTS `bai_goods`;

CREATE TABLE `bai_goods` (
  `goods_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '商品的自增id',
  `cate_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '商品所属类别id，bai_category的cate_id，选择了某种类别将会决定当前商品的属性',
  `brand_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '品牌id，bai_brand 的 brand_id',
  `type_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '店铺自定义分类id，bai_shop_type表的type_id',
  `goods_sn` varchar(255) NOT NULL DEFAULT '' COMMENT '商品的唯一货号',
  `goods_name` varchar(255) NOT NULL DEFAULT '' COMMENT '商品的名称',
  `goods_image` varchar(2000) NOT NULL DEFAULT '' COMMENT '商品图片URL',
  `goods_number` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '商品库存数量',
  `click_count` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '商品点击数',
  `sell_count` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '商品总销量',
  `weight` decimal(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '商品的重量，以千克为单位',
  `market_price` decimal(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '市场售价',
  `shop_price` decimal(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '本店售价',
  `goods_brief` varchar(255) NOT NULL DEFAULT '' COMMENT '商品的简短描述',
  `goods_desc` longtext COMMENT '商品的详细描述',
  `create_time` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '商品的添加时间',
  `update_time` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '最近一次更新商品的时间',
  `publish_time` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '最近一次发布商品的时间',
  `seller_note` varchar(255) NOT NULL DEFAULT '' COMMENT '商品的商家备注，仅商家可见',
  `is_real` tinyint(3) NOT NULL DEFAULT '1' COMMENT '是否是实物，1，是；0，否；比如虚拟卡就为0，不是实物',
  `is_on_sale` tinyint(3) NOT NULL DEFAULT '1' COMMENT '该商品是否上架销售，1，是；0，否',
  `has_invoice` tinyint(3) NOT NULL DEFAULT '0' COMMENT '是否有发票；0，否；1，是',
  `has_warranty` tinyint(3) NOT NULL DEFAULT '0' COMMENT '是否有保修；0，否；1，是',
  `is_delete` tinyint(3) NOT NULL DEFAULT '0' COMMENT '商品是否已经删除，0，否；1，已删除',
  PRIMARY KEY (`goods_id`),
  UNIQUE KEY `uqe_goods_sn` (`goods_sn`),
  KEY `idx_goods_type_id` (`type_id`),
  KEY `idx_goods_cate_id` (`cate_id`),
  KEY `idx_goods_brand_id` (`brand_id`),
  KEY `idx_goods_is_delete` (`is_delete`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='商品表';

/*Data for the table `bai_goods` */

insert  into `bai_goods`(`goods_id`,`cate_id`,`brand_id`,`type_id`,`goods_sn`,`goods_name`,`goods_image`,`goods_number`,`click_count`,`sell_count`,`weight`,`market_price`,`shop_price`,`goods_brief`,`goods_desc`,`create_time`,`update_time`,`publish_time`,`seller_note`,`is_real`,`is_on_sale`,`has_invoice`,`has_warranty`,`is_delete`) values (1,8,2,0,'4080dfb1-40a3-4a4b-97cc-a7eab030c3ac','我的商品','http://img.taobao.com/bao/uploaded/i2/T1dm4tXX4fXXa3.Ana_121708.jpg_sum.jpg',9000,0,0,'0.00','0.00','102.00','','​','2011-12-06 12:18:00','2012-01-11 21:28:28','2012-01-11 21:28:28','',1,1,0,0,0),(2,20,2,0,'4080dfb1-40a3-4a4b-97cc-a7eab030c3ad','我的商品2','http://img.taobao.com/bao/uploaded/i7/T1xCJtXjdpXXcrG7_a_121804.jpg_sum.jpg',222,0,0,'0.00','0.00','333.00','',NULL,'2011-12-06 12:18:00','2011-12-06 12:18:00','2011-12-06 12:18:00','',1,1,0,0,0),(21,21,2,0,'5635909B-0AF0-4043-9DBC-840EED9FD088','adsfsdfdsf','',1,0,0,'1.00','1.00','1.00','','​','2012-01-30 15:53:48','1000-01-01 00:00:00','2012-01-30 15:53:48','',1,0,0,0,1);

/*Table structure for table `bai_goods_gifts` */

DROP TABLE IF EXISTS `bai_goods_gifts`;

CREATE TABLE `bai_goods_gifts` (
  `gifts_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '配件的自增id',
  PRIMARY KEY (`gifts_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='赠品与配件表';

/*Data for the table `bai_goods_gifts` */

/*Table structure for table `bai_goods_imgs` */

DROP TABLE IF EXISTS `bai_goods_imgs`;

CREATE TABLE `bai_goods_imgs` (
  `imgs_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '图片ID',
  `goods_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '商品ID',
  `path` varchar(2000) NOT NULL DEFAULT '' COMMENT '图片URL',
  `sort` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '图片显示的顺序',
  PRIMARY KEY (`imgs_id`),
  KEY `idx_goods_id` (`goods_id`),
  KEY `idx_sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='商品图片表';

/*Data for the table `bai_goods_imgs` */

/*Table structure for table `bai_goods_props` */

DROP TABLE IF EXISTS `bai_goods_props`;

CREATE TABLE `bai_goods_props` (
  `goods_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '商品ID',
  `props_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '属性ID',
  `props_value` varchar(2000) NOT NULL DEFAULT '' COMMENT '属性值',
  PRIMARY KEY (`goods_id`,`props_id`),
  KEY `idx_goods_id` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='商品属性表';

/*Data for the table `bai_goods_props` */

/*Table structure for table `bai_payment` */

DROP TABLE IF EXISTS `bai_payment`;

CREATE TABLE `bai_payment` (
  `pay_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `pay_type` tinyint(3) NOT NULL DEFAULT '0' COMMENT '306：0，货到付款；1，网银支付；2，支付平台；',
  `pay_name` varchar(255) NOT NULL DEFAULT '' COMMENT '支付方式名称',
  `pay_desc` varchar(255) NOT NULL DEFAULT '' COMMENT '支付方式描述',
  `pay_config` varchar(255) NOT NULL DEFAULT '' COMMENT '支付方式的配置信息，包括商户号和密钥什么的',
  `sort` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '支付方式在页面的显示顺序',
  `enabled` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否可用。枚举100：0，否；1，是',
  PRIMARY KEY (`pay_id`),
  KEY `idx_pay_type` (`pay_type`),
  KEY `idx_sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='支付方式配置信息表';

/*Data for the table `bai_payment` */

/*Table structure for table `bai_properties` */

DROP TABLE IF EXISTS `bai_properties`;

CREATE TABLE `bai_properties` (
  `props_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '属性自增id',
  `cate_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '类别id',
  `props_name` varchar(255) NOT NULL DEFAULT '' COMMENT '属性名称',
  `input_type` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '属性输入类型; 枚举201： 0，不可输入；1，文本输入；2，单选输入；3，多选输入',
  `input_values` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '属性默认值；当input_type为1是字符串，为2或3时，格式为JSON字符趾，例如：“[[0,’选择’],[1,’选择’]]” 表示单选值或多选值。',
  `sort` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '属性显示的顺序',
  PRIMARY KEY (`props_id`),
  KEY `idx_props_cate_id` (`cate_id`),
  KEY `idx_sort` (`sort`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='商品类型中的属性定义表';

/*Data for the table `bai_properties` */

insert  into `bai_properties`(`props_id`,`cate_id`,`props_name`,`input_type`,`input_values`,`sort`) values (1,1,'颜色',0,0,11),(2,1,'尺码',0,0,12),(3,1,'款式',0,0,13),(4,1,'材质',0,0,14),(5,12,'颜色',0,0,125),(6,12,'尺码',0,0,126),(7,12,'款式',0,0,127),(8,12,'材质',0,0,128),(9,12,'跟高',0,0,129);

/*Table structure for table `bai_shop_type` */

DROP TABLE IF EXISTS `bai_shop_type`;

CREATE TABLE `bai_shop_type` (
  `type_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '类别自增id',
  `type_parent` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '父类别id',
  `type_path` varchar(255) NOT NULL DEFAULT '' COMMENT '树的路径',
  `type_name` varchar(255) NOT NULL DEFAULT '' COMMENT '类别名称',
  `type_logo` varchar(255) NOT NULL DEFAULT '' COMMENT '上传的该类别的logo图片',
  `type_desc` varchar(255) NOT NULL DEFAULT '' COMMENT '描述（备注）',
  PRIMARY KEY (`type_id`),
  KEY `idx_type_parent` (`type_parent`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='店铺自定义分类定义表';

/*Data for the table `bai_shop_type` */

/*Table structure for table `bai_users` */

DROP TABLE IF EXISTS `bai_users`;

CREATE TABLE `bai_users` (
  `user_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户的自增id',
  `username` varchar(255) NOT NULL DEFAULT '' COMMENT '用户名，必须为邮箱',
  `password` varchar(255) NOT NULL DEFAULT '' COMMENT '密码',
  `nicename` varchar(255) NOT NULL DEFAULT '' COMMENT '昵称',
  `email` varchar(255) NOT NULL DEFAULT '' COMMENT '邮箱',
  `mobile` varchar(255) NOT NULL DEFAULT '' COMMENT '手机',
  `telephone` varchar(255) NOT NULL DEFAULT '' COMMENT '家庭电话',
  `sex` tinyint(3) NOT NULL DEFAULT '0' COMMENT '性别，枚举104：0，保密；1，男；2，女',
  `birthday` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '生日日期',
  `pay_points` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '消费积分',
  `rank_points` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '会员等级积分',
  `user_rank_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '会员等级id，取值bai_user_rank',
  `address_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '收货信息id，取值表 bai_user_address',
  `reg_time` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '注册时间',
  `last_update_time` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '最后一次登录时间',
  `last_login_ip` varchar(255) NOT NULL DEFAULT '' COMMENT '最后一次登录ip',
  `visit_count` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '登录次数',
  `is_subscribe` tinyint(3) NOT NULL DEFAULT '1' COMMENT '是否订阅，枚举100：1，是；0，否',
  `enable` tinyint(3) NOT NULL DEFAULT '0' COMMENT '是否启用，枚举102：1，启用；0，禁用',
  `slave_database` varchar(255) NOT NULL DEFAULT '' COMMENT '用户数据所在从库（slave）位置，值为BaseService类中sqlMapClientTemplateSlaves属性的key值，一般该值会设为从库名',
  PRIMARY KEY (`user_id`),
  KEY `uqe_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='用户信息表';

/*Data for the table `bai_users` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
