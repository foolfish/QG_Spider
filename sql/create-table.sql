#用户
CREATE TABLE t_user (
  id  VARCHAR(50) PRIMARY KEY,
  loginName VARCHAR(50) DEFAULT NULL,
  loginPassword VARCHAR(50) DEFAULT NULL,
  userName VARCHAR(100) DEFAULT NULL,
  memberLevel VARCHAR(30) DEFAULT NULL,
  buyLevel VARCHAR(30) DEFAULT NULL,
  memberType VARCHAR(30) DEFAULT NULL,
  petName VARCHAR(30) DEFAULT NULL,
  phone VARCHAR(300) DEFAULT NULL,
  email VARCHAR(30) DEFAULT NULL,
  realName VARCHAR(30) DEFAULT NULL,
  idcard VARCHAR(30) DEFAULT NULL,
  addr VARCHAR(300) DEFAULT NULL,
  sex  VARCHAR(20) DEFAULT NULL,
  usersource VARCHAR(30) DEFAULT NULL,
  usersource2 VARCHAR(30) DEFAULT NULL,
  secretLevel VARCHAR(30) DEFAULT NULL,	
  taobaoName VARCHAR(30) DEFAULT NULL,	
  identifyTime VARCHAR(30) DEFAULT NULL,			
  major VARCHAR(50) DEFAULT NULL,		
  registerDate datetime DEFAULT NULL,	
  isRealName VARCHAR(30) DEFAULT NULL,	
  isProtected VARCHAR(30) DEFAULT NULL,	
  isPhone VARCHAR(30) DEFAULT NULL,
  birthday datetime DEFAULT NULL,
  redstar VARCHAR(30) DEFAULT NULL,
  live VARCHAR(30) DEFAULT NULL,
  parentId VARCHAR(50) DEFAULT NULL,
  modifyDate DATETIME DEFAULT NULL,
  markId VARCHAR(50) DEFAULT NULL,
  fixphone VARCHAR(300) DEFAULT NULL,
  packageName VARCHAR(50) DEFAULT NULL,
  ssNo VARCHAR(30) DEFAULT NULL,
  workerNature VARCHAR(30) DEFAULT NULL,
  jobTitle VARCHAR(30) DEFAULT NULL,
  pUnit VARCHAR(50) DEFAULT NULL,
  sspComNo VARCHAR(30) DEFAULT NULL,
  hcatagory VARCHAR(30) DEFAULT NULL,
  cardstatus VARCHAR(30) DEFAULT NULL,
  carePerson VARCHAR(30) DEFAULT NULL,
  paySalary VARCHAR(30) DEFAULT NULL,
  payStatus VARCHAR(30) DEFAULT NULL,
  cardType VARCHAR(50) DEFAULT NULL,
  cardNo VARCHAR(100) DEFAULT NULL,
  merry VARCHAR(50) DEFAULT NULL,
  yBalance DECIMAL(10,2) DEFAULT NULL,
  qq VARCHAR(30) DEFAULT NULL,
  yincome DECIMAL(10,2) DEFAULT NULL,
  entranceDate datetime DEFAULT NULL,
  graduateDate datetime DEFAULT NULL,
  eduType VARCHAR(30) DEFAULT NULL,
  eduRecord VARCHAR(30) DEFAULT NULL,
  eduSchool VARCHAR(50) DEFAULT NULL,
  schoolPlace VARCHAR(30) DEFAULT NULL,
  specialty VARCHAR(30) DEFAULT NULL,
  eduForm VARCHAR(30) DEFAULT NULL,
  certificateId VARCHAR(50) DEFAULT NULL,
  eduConclusion VARCHAR(30) DEFAULT NULL,
  shebaolocation VARCHAR(30) DEFAULT NULL,
  hourse int(3) DEFAULT 6,
  cars int(3) DEFAULT 5,
  phoneRemain DECIMAL(10,2) DEFAULT '0'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户基本信息';


#订单
CREATE TABLE t_order (
   id  VARCHAR(50) PRIMARY KEY,
  baseUserId VARCHAR(50) DEFAULT NULL,
  productNames VARCHAR(300) DEFAULT NULL,
  orderId VARCHAR(40),
  receiver VARCHAR(30) DEFAULT NULL,
  money DECIMAL(10,2) DEFAULT NULL,
  buyway VARCHAR(30) DEFAULT NULL,
  buyTime datetime DEFAULT NULL,
  orderstatus VARCHAR(30) DEFAULT NULL,
  ordersource  VARCHAR(30) DEFAULT NULL,
  recevierAddr  VARCHAR(100) DEFAULT NULL,
  recevierFixPhone VARCHAR(30) DEFAULT NULL,
  recevierTelephone VARCHAR(30) DEFAULT NULL,
  recevierEmail VARCHAR(50) DEFAULT NULL,
  recevierPost VARCHAR(20)  DEFAULT NULL

);


#子订单
CREATE TABLE t_orderitem (
  id  VARCHAR(50) PRIMARY KEY,
  orderTId VARCHAR(50) DEFAULT NULL,
  productName VARCHAR(300) DEFAULT NULL,
  productId  VARCHAR(50) DEFAULT NULL,
  price DECIMAL(10,2) DEFAULT NULL,
  num int(3) DEFAULT 1,
  productType VARCHAR(20)  DEFAULT NULL,
  merchant  VARCHAR(50)  DEFAULT NULL
);



#支付信息
CREATE TABLE t_payinfo (
   id  VARCHAR(50) PRIMARY KEY,
  baseUserId VARCHAR(50) DEFAULT NULL,
  payTime datetime DEFAULT NULL,
  tradeType VARCHAR(300) DEFAULT NULL,
  tradeNoType VARCHAR(30) DEFAULT NULL,
  amount DECIMAL(10,2) DEFAULT NULL,
  tradeNo VARCHAR(60) DEFAULT NULL,
  receiverName VARCHAR(30) DEFAULT NULL,
  status VARCHAR(30) DEFAULT NULL,
  source  VARCHAR(30) DEFAULT NULL
);

#社保信息
CREATE TABLE t_shebao (
  id  VARCHAR(50) PRIMARY KEY,
  payTime DATETIME DEFAULT NULL,
  payFeedBase DECIMAL(10,2) DEFAULT '0.00',
  payMedBase DECIMAL(10,2)  DEFAULT '0.00',
  payUmemplyBase DECIMAL(10,2)  DEFAULT '0.00',
  payInjuryBase DECIMAL(10,2) DEFAULT '0.00',
  payFeedPerson DECIMAL(10,2)  DEFAULT '0.00',
  payMedPerson DECIMAL(10,2)  DEFAULT '0.00',
  payUmemplyPerson DECIMAL(10,2)  DEFAULT '0.00',
  source  VARCHAR(30) DEFAULT NULL,
  baseUserId VARCHAR(50) DEFAULT NULL,
  payInjuryPerson  DECIMAL(10,2)  DEFAULT '0.00',
  payFeedCom  DECIMAL(10,2)  DEFAULT '0.00',
  payMedCom DECIMAL(10,2)  DEFAULT '0.00',
  payUmemplyCom DECIMAL(10,2)  DEFAULT '0.00',
  payInjuryCom DECIMAL(10,2)  DEFAULT '0.00',
  payFeedAll DECIMAL(10,2) DEFAULT '0.00',
  payMedAll DECIMAL(10,2)  DEFAULT '0.00',
  payUmemplyAll DECIMAL(10,2)  DEFAULT '0.00',
  payInjuryAll DECIMAL(10,2) DEFAULT '0.00',
  payCompany VARCHAR(30) DEFAULT NULL	
);

#中国移动手机话费数据
CREATE TABLE t_mobiletel (
  id  VARCHAR(50) PRIMARY KEY,
  baseUserId  VARCHAR(50),
  cTime datetime DEFAULT NULL,
  cName VARCHAR(50) DEFAULT NULL,
  teleno VARCHAR(50) DEFAULT NULL,
  brand VARCHAR(50) DEFAULT NULL,
  dependCycle VARCHAR(50) DEFAULT NULL,
  cAllPay DECIMAL(10,2) DEFAULT NULL,
  cAllBalance DECIMAL(10,2) DEFAULT NULL,
  tcgdf DECIMAL(10,2) DEFAULT NULL,
  tcwdxf DECIMAL(10,2) DEFAULT NULL,
  tcwyytxf DECIMAL(10,2) DEFAULT NULL,
  tryf DECIMAL(10,2) DEFAULT NULL,
  ipgnctdx DECIMAL(10,2) DEFAULT NULL,
  sstgndq DECIMAL(10,2) DEFAULT NULL,
  dgddwl DECIMAL(10,2) DEFAULT NULL,
  jtyhtc DECIMAL(10,2) DEFAULT NULL,
  ipbdth DECIMAL(10,2) DEFAULT NULL,
  ipctth DECIMAL(10,2) DEFAULT NULL,
  ctth DECIMAL(10,2) DEFAULT NULL,
  bdth DECIMAL(10,2) DEFAULT NULL,
  myth DECIMAL(10,2) DEFAULT NULL,
  gndxtx DECIMAL(10,2) DEFAULT NULL,
  ydsjllqb DECIMAL(10,2) DEFAULT NULL,
  ydsjllsj DECIMAL(10,2) DEFAULT NULL,
  ydsj3y DECIMAL(10,2) DEFAULT NULL,
  llzdsdb10 DECIMAL(10,2) DEFAULT NULL,
  cl5 DECIMAL(10,2) DEFAULT NULL,
  iscm int(3) DEFAULT 0
);



#电信手机话费数据
CREATE TABLE t_dianxintel (
  id  VARCHAR(50) PRIMARY KEY,
  baseUserId  VARCHAR(50),
  cTime datetime DEFAULT NULL,
  cName VARCHAR(50) DEFAULT NULL,
  teleno VARCHAR(50) DEFAULT NULL,
  brand VARCHAR(50) DEFAULT NULL,
  dependCycle VARCHAR(50) DEFAULT NULL,
  cAllPay DECIMAL(10,2) DEFAULT NULL,
  ztcjbf DECIMAL(10,2) DEFAULT NULL,
  bdthf DECIMAL(10,2) DEFAULT NULL
);



#中国联通手机话费数据
CREATE TABLE t_unicomtel (
  id  VARCHAR(50) PRIMARY KEY,
  baseUserId  VARCHAR(50),
  cTime datetime DEFAULT NULL,
  cName VARCHAR(50) DEFAULT NULL,
  teleno VARCHAR(50) DEFAULT NULL,
  dependCycle VARCHAR(50) DEFAULT NULL,
  cAllPay DECIMAL(10,2) DEFAULT NULL,
  jbyzf DECIMAL(10,2) DEFAULT NULL,
  bdthf DECIMAL(10,2) DEFAULT NULL,
   ctthf DECIMAL(10,2) DEFAULT NULL,
   mythf DECIMAL(10,2) DEFAULT NULL,
   dxtxf DECIMAL(10,2) DEFAULT NULL,
   zzywf DECIMAL(10,2) DEFAULT NULL,
   dsf DECIMAL(10,2) DEFAULT NULL,
   tff DECIMAL(10,2) DEFAULT NULL,
   qtf DECIMAL(10,2) DEFAULT NULL,
  iscm int(3) DEFAULT 0
);

#移动话费详单
CREATE TABLE t_mobiledetail (
  id  VARCHAR(50) PRIMARY KEY,
  cTime datetime DEFAULT NULL,
  tradeAddr VARCHAR(50) DEFAULT NULL,
  tradeWay VARCHAR(50) DEFAULT NULL,
  recevierPhone VARCHAR(50) DEFAULT NULL,
  tradeTime int(5) DEFAULT 0,
  taocan VARCHAR(50) DEFAULT NULL,
  onlinePay DECIMAL(10,2) DEFAULT NULL,
  phone VARCHAR(30) DEFAULT NULL,
  iscm  int(3) DEFAULT 0
);

#联通话费详单
CREATE TABLE t_unicomdetail (
  id  VARCHAR(50) PRIMARY KEY,
  businessType VARCHAR(20) DEFAULT NULL,
  cTime datetime DEFAULT NULL,
  tradeTime int(8) DEFAULT 0,
  callType VARCHAR(20) DEFAULT NULL,
  recevierPhone VARCHAR(30) DEFAULT NULL,
  tradeAddr VARCHAR(20) DEFAULT NULL,
  tradeType VARCHAR(20) DEFAULT NULL,
  basePay DECIMAL(10,2) DEFAULT NULL,
  ldPay DECIMAL(10,2) DEFAULT NULL,
  otherPay DECIMAL(10,2) DEFAULT NULL,
  totalPay DECIMAL(10,2) DEFAULT NULL,
  phone VARCHAR(30) DEFAULT NULL,
  reductionPay DECIMAL(10,2) DEFAULT NULL,
  iscm  int(3) DEFAULT 0
);

	
#招商银行大额转账通知单数据
CREATE TABLE t_cmbtransferbill(
  id  VARCHAR(50) PRIMARY KEY,
  baseUserId  VARCHAR(50),
  transferTime datetime DEFAULT NULL,
  cardNo VARCHAR(50) DEFAULT NULL,
  operation VARCHAR(30) DEFAULT NULL,
  pname VARCHAR(30) DEFAULT NULL,
  amount DECIMAL(15,2) DEFAULT NULL
 );
 
 
 #中国工商银行客户对账单
 CREATE TABLE t_icbccheckbill(
  id  VARCHAR(50) PRIMARY KEY,
  baseUserId  VARCHAR(50),
  cardHolder  VARCHAR(50),
  cardNo  VARCHAR(50),
  repaymentDate datetime DEFAULT NULL,
  period  VARCHAR(100),
  BillGenerationDate datetime DEFAULT NULL,
  previousBalance DECIMAL(15,2) DEFAULT NULL,
  currentIncome DECIMAL(15,2) DEFAULT NULL,
  currentExpenses DECIMAL(15,2) DEFAULT NULL,
  currentBalance DECIMAL(15,2) DEFAULT NULL,
  personalIntegration DECIMAL(15,2) DEFAULT NULL
 );
 
 
 #中国工商银行客户对账单明细条目
  CREATE TABLE t_icbccheckbillitem(
  id  VARCHAR(50) PRIMARY KEY,
  checkBillId VARCHAR(50) DEFAULT NULL,
  cardNo  VARCHAR(50),
  tradeDate datetime DEFAULT NULL,
  tradeKeepDate datetime DEFAULT NULL,
  tradeType  VARCHAR(50),
  tradePlace  VARCHAR(100),
 
  tradeAmount DECIMAL(15,2) DEFAULT NULL,
  tradeCurrency VARCHAR(50),
  tradeMode  VARCHAR(50)
 );	
	
 
 #中国人民银行征信中心报告概要
 CREATE TABLE t_zhengxinsummary(
  id  VARCHAR(50) PRIMARY KEY,
  baseUserId  VARCHAR(50),
  state VARCHAR(50),
  xinyNum  int(3) ,
  zhufNum  int(3),
  qitNum  int(3)
 );	
 
 
  #中国人民银行征信中心报告明细信息
 CREATE TABLE t_zhengxindetail(
  id  VARCHAR(50) PRIMARY KEY,
  baseUserId  VARCHAR(50),
  cType  VARCHAR(50),
  cRecord VARCHAR(400)
 );	
 

 #电信话费详单
CREATE TABLE t_dianxindetail (
  id  VARCHAR(50) PRIMARY KEY,
  tradeType VARCHAR(20) DEFAULT NULL,
  cTime datetime DEFAULT NULL,
  tradeTime int(8) DEFAULT 0,
  callWay VARCHAR(20) DEFAULT NULL,
  recevierPhone VARCHAR(30) DEFAULT NULL,
  tradeAddr VARCHAR(20) DEFAULT NULL,
  basePay DECIMAL(10,2) DEFAULT NULL,
  longPay DECIMAL(10,2) DEFAULT NULL,
  infoPay DECIMAL(10,2) DEFAULT NULL,
  otherPay DECIMAL(10,2) DEFAULT NULL,
  allPay DECIMAL(10,2) DEFAULT NULL,
  phone VARCHAR(30) DEFAULT NULL,
  iscm  int(3) DEFAULT 0
);
  

#余额宝
CREATE TABLE t_yuebao (
  id  VARCHAR(50) PRIMARY KEY,
  cTime datetime DEFAULT NULL,
  alipayName VARCHAR(50) DEFAULT NULL,
  amountType VARCHAR(50) DEFAULT NULL,
  amount DECIMAL(10,2) DEFAULT NULL
);


#手机归属地
CREATE TABLE t_phonenum (
  phone VARCHAR(20) DEFAULT NULL,
  province VARCHAR(20) DEFAULT NULL,
  city VARCHAR(20) DEFAULT NULL,
  ptype VARCHAR(10) DEFAULT NULL
);


#报警表
CREATE TABLE t_warning (
  id  VARCHAR(50) PRIMARY KEY,
  cTime datetime DEFAULT NULL,
  currentId VARCHAR(50) DEFAULT NULL,
  flag  int(3) DEFAULT 0,
  ptype VARCHAR(50) DEFAULT NULL
);

#身份证所在地
CREATE TABLE t_identifyLocation (
  id  VARCHAR(50) PRIMARY KEY,
  city VARCHAR(50) DEFAULT NULL
);
	
#一二线城市列表
CREATE TABLE t_ot_city (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(255) DEFAULT NULL,
  type int(11) DEFAULT '2',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8;


#985和211大学列表
CREATE TABLE t_university (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(255) DEFAULT NULL COMMENT '大学',
  type tinyint(11) DEFAULT '0' COMMENT '是否985 0为否.1为是',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=119 DEFAULT CHARSET=utf8;

#表格
CREATE TABLE t_cell (
  phone  VARCHAR(50) PRIMARY KEY,
  lable VARCHAR(50) DEFAULT NULL,
  sex VARCHAR(50) DEFAULT NULL,
  age VARCHAR(300) DEFAULT NULL,
  income  VARCHAR(50) DEFAULT NULL,
  baby  VARCHAR(50)  DEFAULT NULL,
  xq VARCHAR(600) DEFAULT NULL,
  domain VARCHAR(2000) DEFAULT NULL,
  keyword  VARCHAR(10000) DEFAULT NULL
);


######################### 2014-06-26####################
#t_mobiletel 新增字段 by 梁涛 
alter table t_mobiletel  add ldxsf decimal(10,2);
alter table t_mobiletel  add mgtjhyf decimal(10,2);

######################### 2014-07-03####################
#t_mobiletel 新增字段 by 梁涛 
alter table t_payinfo  add alipayName VARCHAR(50) DEFAULT NULL;


######################### 2014-07-06####################
#t_dianxintel 新增字段 by 梁涛 
alter table t_dianxintel  add ldxsf decimal(10,2);
alter table t_dianxintel  add mythf decimal(10,2);

#邀请码
CREATE TABLE t_yaoqingma ( 
  random varchar(20) DEFAULT NULL,
  userId varchar(50) DEFAULT NULL
) ;


#申请邮箱
CREATE TABLE t_applyEmail ( 
  id INTEGER UNSIGNED  NOT NULL AUTO_INCREMENT,
  email varchar(50) DEFAULT NULL,
  ctype int(2) DEFAULT '1',
  applyTime datetime DEFAULT NULL,   
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

######################### 2014-07-06####################
#t_applyEmail 新增字段 by zyl 
alter table t_applyEmail  add realname varchar(20) DEFAULT NULL;
alter table t_applyEmail  add job varchar(50) DEFAULT NULL;
alter table t_applyEmail  add teleno varchar(50) DEFAULT NULL;

#新房	
CREATE TABLE t_new_hourse (
  id int(11) NOT NULL AUTO_INCREMENT,
  city varchar(20) DEFAULT NULL,
  hname varchar(40) DEFAULT NULL,
  htype varchar(40) DEFAULT NULL,
  hfix varchar(20) DEFAULT NULL,
  hlocation varchar(50) DEFAULT NULL,
  havg  DECIMAL(10,2) DEFAULT NULL, 
  PRIMARY KEY (id)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

#二手房
CREATE TABLE t_old_hourse (
  id int(11) NOT NULL AUTO_INCREMENT,
  city varchar(20) DEFAULT NULL,
  hname varchar(40) DEFAULT NULL,
   hlocation varchar(50) DEFAULT NULL, 
   havg  DECIMAL(10,2) DEFAULT NULL, 
   hall  DECIMAL(10,2) DEFAULT NULL,
   hsize  DECIMAL(10,2) DEFAULT NULL,
   hst varchar(40) DEFAULT NULL,
   hfloor int(5) DEFAULT 0,
   hfloors int(5) DEFAULT 0,
   hdirection varchar(40) DEFAULT NULL,
   hyear int(10) DEFAULT 0,
  PRIMARY KEY (id)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

#租房
CREATE TABLE t_lease_house (
  id int(11) NOT NULL AUTO_INCREMENT,
  city varchar(20) DEFAULT NULL,
  h_loc1 varchar(30) DEFAULT NULL,
  h_loc2 varchar(30) DEFAULT NULL,
  hname varchar(40) DEFAULT NULL,
  hlocation varchar(50) DEFAULT NULL, 
  hlocation2 varchar(50) DEFAULT NULL, 
  havg  DECIMAL(10,2) DEFAULT NULL,
  hsize  DECIMAL(10,2) DEFAULT NULL,
  hst varchar(40) DEFAULT NULL,
  h_degree varchar(40) DEFAULT NULL,
   hfloor int(5) DEFAULT 0,
   hfloors int(5) DEFAULT 0,
   hdirection varchar(40) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

#add by zyl20140804
alter table t_order change baseUserId loginName varchar(50) not null;

######################### 2014-08-7####################
#t_mobiletel 新增字段 by 梁涛 
alter table t_mobiletel  add zzywf decimal(10,2);

#add by zyl20140810
alter table t_zhengxinsummary change baseUserId loginName varchar(50) not null;
alter table t_zhengxindetail change baseUserId loginName varchar(50) not null;


#抓取记录
CREATE TABLE t_parse (
  id int(11) NOT NULL AUTO_INCREMENT,          
  userId varchar(50) DEFAULT NULL,
  loginName varchar(50) DEFAULT NULL,  
  usersource varchar(30) DEFAULT NULL,
  modifyTime datetime DEFAULT NULL,
  status int(2) DEFAULT 1,
  PRIMARY KEY (id)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


#add by zyl20140810
alter table t_shebao change baseUserId loginName varchar(50) not null;


#add by zyl20140820
alter table t_payinfo drop column baseUserId; 
#add by wjg20140827
alter table t_cell add column cTime datetime NULL; 

#add by wjg20140901
 #电信短信记录
CREATE TABLE t_telcommessage (
  id  VARCHAR(50) PRIMARY KEY,
  businessType VARCHAR(20) DEFAULT NULL,
  sentTime datetime DEFAULT NULL,
  recevierPhone VARCHAR(30) DEFAULT NULL, 
  allPay DECIMAL(10,2) DEFAULT NULL,
  phone VARCHAR(30) DEFAULT NULL,
  createTs datetime DEFAULT NULL
);

#add by 梁涛20140909
 #移动短信记录
CREATE TABLE t_mobilemessage (
  id  VARCHAR(50) PRIMARY KEY,
  sentTime datetime DEFAULT NULL,
  sentAddr VARCHAR(50) DEFAULT NULL,
  tradeWay VARCHAR(50) DEFAULT NULL,
  recevierPhone VARCHAR(30) DEFAULT NULL, 
  allPay DECIMAL(10,2) DEFAULT NULL,
  phone VARCHAR(30) DEFAULT NULL,
  createTs datetime DEFAULT NULL
);


#add by 赵玉龙20140914
#联通短信记录
CREATE TABLE t_unicommessage (
  id  VARCHAR(50) PRIMARY KEY,
  sentTime datetime DEFAULT NULL,
  tradeType VARCHAR(20) DEFAULT NULL,
  recevierPhone VARCHAR(30) DEFAULT NULL, 
  allPay DECIMAL(10,2) DEFAULT NULL,
  phone VARCHAR(30) DEFAULT NULL,
  createTs datetime DEFAULT NULL
);

#add by 刘博譞20141008
#联通流量记录
CREATE TABLE t_unicomflow (
  id varchar(50) NOT NULL,
  startTime datetime DEFAULT NULL,
  tradeType varchar(20) DEFAULT NULL,
  tradeAddr varchar(20) DEFAULT NULL,
  allFlow decimal(10,0) DEFAULT NULL,
  allPay decimal(10,2) DEFAULT NULL,
  phone varchar(30) DEFAULT NULL,
  createTs datetime DEFAULT NULL,
  PRIMARY KEY (id)
)ENGINE=InnoDB  DEFAULT CHARSET=utf8;

#add by 刘博譞20141008
#联通流量账单记录
CREATE TABLE t_unicomflowbill (
  id varchar(50) NOT NULL,
  baseUserId varchar(50) DEFAULT NULL,
  cTime datetime DEFAULT NULL,
  cName varchar(50) DEFAULT NULL,
  teleno varchar(50) DEFAULT NULL,
  dependCycle varchar(50) DEFAULT NULL,
  cAllFlow decimal(10,0) DEFAULT NULL,
  cAllPay decimal(10,2) DEFAULT NULL,
  iscm int(3) DEFAULT '0',
  PRIMARY KEY (id)
)ENGINE=InnoDB  DEFAULT CHARSET=utf8;

#add by 孙寅杰20141008
#电信流量账单
CREATE TABLE `t_dianxinflow` (
  `id` varchar(50)  NOT NULL,
  `phone` varchar(30) NOT NULL COMMENT '手机号',
  `dependCycle` varchar(50) DEFAULT NULL COMMENT '起始日期',
  `queryMonth` datetime DEFAULT NULL COMMENT '查询月份',
  `allFlow` decimal(10,2) DEFAULT '0.00' COMMENT '总流量（KB）',
  `allTime` decimal(10,2) DEFAULT '0.00' COMMENT '总时长（秒）',
  `allPay` decimal(10,2) DEFAULT '0.00' COMMENT '总费用（元）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


#add by 孙寅杰20141008
#电信流量详单

CREATE TABLE `t_dianxinflowdetail` (
  `id` varchar(50) NOT NULL,
  `phone` varchar(30) NOT NULL COMMENT '手机号',
  `iscm` int(3) DEFAULT '0' COMMENT '是否当月保存',
  `beginTime` datetime DEFAULT NULL COMMENT '开始时间',
  `tradeTime` int(8) DEFAULT '0' COMMENT '时长（秒）',
  `flow` decimal(10,2) DEFAULT '0.00' COMMENT '流量（KB）',
  `netType` varchar(20) DEFAULT NULL COMMENT '网络类型',
  `location` varchar(20) DEFAULT NULL COMMENT '上网地市',
  `business` varchar(20) DEFAULT NULL COMMENT '使用业务',
  `fee` decimal(10,2) DEFAULT '0.00' COMMENT '费用（元）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#add by 马延龙20141008
#移动详单
CREATE TABLE `t_mobileonlinelist` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `phone` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `cTime` datetime DEFAULT NULL COMMENT '起始时间',
  `tradeAddr` varchar(30) COLLATE utf8_bin DEFAULT NULL COMMENT '通信地点',
  `onlineType` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '上网方式(CMNET/CMWAP)',
  `onlineTime` bigint(50) DEFAULT NULL COMMENT '时长',
  `totalFlow` bigint(50) DEFAULT NULL COMMENT '总流量(KB)',
  `cheapService` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '套餐优惠',
  `communicationFees` decimal(15,2) DEFAULT NULL COMMENT '通信费',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


#add by 马延龙20141008
#移动账单
CREATE TABLE `t_mobileonlinebill` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `phone` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `monthly` datetime DEFAULT NULL,
  `totalFlow` bigint(50) DEFAULT NULL COMMENT '数据总流量',
  `freeFlow` bigint(50) DEFAULT NULL COMMENT '免费数据流量',
  `chargeFlow` bigint(50) DEFAULT NULL COMMENT '收费数据流量',
  `trafficCharges` decimal(15,2) DEFAULT NULL COMMENT '通信费',
  `iscm` int(3) DEFAULT '0' COMMENT '是否当月',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

#add by 赵玉龙20141019
#流量账单
alter table t_mobileonlinelist modify column cheapService varchar(300) ;

-- ----------------------------
#add by 王雪飞20141022
#丢失的记录的辅助表,以后数据也比较大
-- ----------------------------
DROP TABLE IF EXISTS `t_lostcontent`;
CREATE TABLE `t_lostcontent` (
  `id` varchar(64) NOT NULL,
  `url` varchar(100) DEFAULT NULL COMMENT '单独地址,非必填,根据实际情况适用',
  `userSource` varchar(32) DEFAULT NULL,
  `loginName` varchar(90) DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  `errorCode` tinyint(4) DEFAULT NULL COMMENT '更新是否正常0正常1错误',
  `code` bigint(20) DEFAULT NULL COMMENT '主要的比较参数,时间可转换成long进行比较',
  `flag` varchar(1) DEFAULT NULL COMMENT '备用字段',
  `type` int(3) DEFAULT NULL COMMENT '自定义标示位,某个类中,自己设置的查询条件,必填',
  `verify` varchar(64) DEFAULT NULL COMMENT '校验,备用字段可存放关键参数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


#add by 王雪飞20141027
alter table t_lostcontent add column count tinyint(4) NULL ; 


