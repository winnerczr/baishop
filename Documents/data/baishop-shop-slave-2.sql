/*
SQLyog Ultimate v9.62 
MySQL - 5.5.20 : Database - baishop-shop-slave-2
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`baishop-shop-slave-2` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `baishop-shop-slave-2`;

/*Table structure for table `bai_order` */

DROP TABLE IF EXISTS `bai_order`;

CREATE TABLE `bai_order` (
  `order_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '订单表的自增id',
  `order_sn` varchar(255) NOT NULL DEFAULT '' COMMENT '订单号，唯一',
  `user_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '用户id，bai_users的user_id',
  `order_status` tinyint(3) NOT NULL DEFAULT '0' COMMENT '订单状态。枚举301： 0，未确认；1，已确认；2，已完成；3，已取消；4，退货；5，无效',
  `shipping_status` tinyint(3) NOT NULL DEFAULT '0' COMMENT '商品配送情况。枚举302： 0，未发货； 1，已发货；2，已收货；3，备货中',
  `pay_status` tinyint(3) NOT NULL DEFAULT '0' COMMENT '支付状态。枚举303： 0，未付款；1，付款中；2，已付款',
  `consignee` varchar(255) NOT NULL DEFAULT '' COMMENT '收货人的名字',
  `email` varchar(255) NOT NULL DEFAULT '' COMMENT '收货人的email',
  `country` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '收货人的国家，bai_city的city_id',
  `province` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '收货人的省份，bai_city的city_id',
  `city` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '收货人的城市，bai_city的city_id',
  `district` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '收货人的地区，bai_city的city_id',
  `address` varchar(255) NOT NULL DEFAULT '' COMMENT '收货人的详细地址',
  `zipcode` varchar(255) NOT NULL DEFAULT '' COMMENT '收货人的邮编',
  `mobile` varchar(255) NOT NULL DEFAULT '' COMMENT '收货人的手机',
  `telephone` varchar(255) NOT NULL DEFAULT '' COMMENT '收货人的电话',
  `best_time` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '收货人的最佳收货时间',
  `pay_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '用户选择的支付方式的id，取值表bai_payment',
  `pay_name` varchar(255) NOT NULL DEFAULT '' COMMENT '用户选择的支付方式的名称',
  `shipping_time` tinyint(3) NOT NULL DEFAULT '0' COMMENT '配送时间方式。枚举307： 0,工作日、双休日与假日均可送货; 1,只有双休日、假日送货（工作日不用送货）; 2,只有工作日送货（双休日、假日不用送） 写字楼/商用地址客户选择; 3,学校地址（该地址白天没人，请尽量安排其他时间送货） 特别安排可能超出预计送货天数',
  `how_oos` tinyint(3) NOT NULL DEFAULT '0' COMMENT '缺货处理方式。  枚举304：0,等待所有商品备齐后再发； 1,取消订单；2,与店主协商',
  `inv_type` tinyint(3) NOT NULL DEFAULT '0' COMMENT '发票类型。 枚举305：0，个人；1，企业',
  `inv_payee` varchar(255) NOT NULL DEFAULT '' COMMENT '发票抬头',
  `inv_content` varchar(255) NOT NULL DEFAULT '' COMMENT '发票内容',
  `inv_tax` decimal(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '发票税额',
  `goods_amount` decimal(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '商品总金额',
  `shipping_fee` decimal(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '配送费用',
  `insure_fee` decimal(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '保价费用',
  `integral_money` decimal(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '使用积分金额',
  `discount_money` decimal(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '折扣金额',
  `order_amount` decimal(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '应付款金额',
  `create_time` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '订单生成时间',
  `confirm_time` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '订单确认时间',
  `pay_time` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '订单支付时间',
  `invoice_number` varchar(255) NOT NULL DEFAULT '' COMMENT '发货单号',
  `buyer_note` varchar(1000) NOT NULL DEFAULT '' COMMENT '买家给客户的留言',
  `seller_note` varchar(1000) NOT NULL DEFAULT '' COMMENT '商家给买家的留言',
  `is_delete` tinyint(3) NOT NULL DEFAULT '0' COMMENT '商品是否已经删除，枚举100： 1，是；0，否；',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='订单信息表';

/*Data for the table `bai_order` */

/*Table structure for table `bai_order_goods` */

DROP TABLE IF EXISTS `bai_order_goods`;

CREATE TABLE `bai_order_goods` (
  `order_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '订单id',
  `goods_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '商品id',
  `goods_name` varchar(255) NOT NULL DEFAULT '' COMMENT '商品的名称',
  `goods_sn` varchar(255) NOT NULL DEFAULT '' COMMENT '商品的唯一货号',
  `goods_number` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '商品库存数量',
  `market_price` decimal(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '市场售价',
  `shop_price` decimal(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '本店售价',
  `goods_attr` varchar(2000) NOT NULL DEFAULT '' COMMENT '购买该商品时所选择的属性',
  `is_real` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '是否是实物。枚举100：1，是；0，否',
  `is_real_send` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '当不是实物时，是否已发货。 枚举100：1，是；0，否',
  `is_gift` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否参加优惠活动。 枚举100：1，是；0，否'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='订单的商品信息表';

/*Data for the table `bai_order_goods` */

/*Table structure for table `bai_order_option` */

DROP TABLE IF EXISTS `bai_order_option`;

CREATE TABLE `bai_order_option` (
  `option_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '订单商品信息自增id',
  `order_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '被操作的订单号',
  `action_user` varchar(255) NOT NULL DEFAULT '' COMMENT '操作该次的人员',
  `order_status` tinyint(3) NOT NULL DEFAULT '0' COMMENT '订单状态。 枚举301： 0，未确认；1，已确认；2，已完成；3，已取消；4，退货；5，无效',
  `shipping_status` tinyint(3) NOT NULL DEFAULT '0' COMMENT '商品配送情况。枚举302： 0，未发货； 1，已发货；2，已收货；3，备货中',
  `pay_status` tinyint(3) NOT NULL DEFAULT '0' COMMENT '支付状态。 枚举303： 0，未付款；1，付款中；2，已付款',
  `option_note` varchar(255) NOT NULL DEFAULT '' COMMENT '操作备注',
  `option_time` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '操作时间',
  PRIMARY KEY (`option_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='订单操作记录的商品信息表';

/*Data for the table `bai_order_option` */

/*Table structure for table `bai_users_address` */

DROP TABLE IF EXISTS `bai_users_address`;

CREATE TABLE `bai_users_address` (
  `address_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `user_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '用户id，bai_users的user_id',
  `consignee` varchar(255) NOT NULL DEFAULT '' COMMENT '收货人的名字',
  `email` varchar(255) NOT NULL DEFAULT '' COMMENT '收货人的email',
  `country` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '收货人的国家',
  `province` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '收货人的省份',
  `city` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '收货人的城市',
  `district` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '收货人的地区',
  `address` varchar(255) NOT NULL DEFAULT '' COMMENT '收货人的详细地址',
  `zipcode` varchar(255) NOT NULL DEFAULT '' COMMENT '收货人的邮编',
  `mobile` varchar(255) NOT NULL DEFAULT '' COMMENT '收货人的手机',
  `telephone` varchar(255) NOT NULL DEFAULT '' COMMENT '收货人的电话',
  `best_time` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '收货人的最佳收货时间',
  PRIMARY KEY (`address_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='用户收货地址信息表';

/*Data for the table `bai_users_address` */

/*Table structure for table `bai_users_credits` */

DROP TABLE IF EXISTS `bai_users_credits`;

CREATE TABLE `bai_users_credits` (
  `credits_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '信用等级ID',
  `credits_name` varchar(255) NOT NULL DEFAULT '' COMMENT '信用等级名称',
  `credits_logo` varchar(255) NOT NULL DEFAULT '' COMMENT '信用等级logo图片',
  `min_points` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '该等级的最低积分',
  `max_points` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '该等级的最高积分',
  `discount` tinyint(3) unsigned NOT NULL DEFAULT '10' COMMENT '该会员等级的商品折扣',
  PRIMARY KEY (`credits_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='信用等级配置信息';

/*Data for the table `bai_users_credits` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
