<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ page pageEncoding="utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="/common/domain.jsp" flush="true"/> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>个人信用报告</title>
<link href="/${ctx }/css/ke.css" rel="stylesheet" type="text/css" />
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script type="text/javascript" src="/${ctx }/js/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="/${ctx }/js/jquery-migrate-1.2.1.min.js"></script>
</head>

<body times="${phonetime}">
<div class="main">
   <h1>个人信用报告</h1>
   <div class="info">
       <dl>
          <dd>报告编号：</dd>
          <dt>${user.markId}</dt>
          <dd>报告时间：</dd>  
          <dt><fmt:formatDate value="${user.modifyDate}"  type="both"/></dt>
          <div class="clear"></div>
       </dl>
       <dl>
          <dd>姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：</dd>  
          <dt>${user.realName}</dt>
          <dd>性&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;别：</dd>  
          <dt>${identify.sex}</dt>
          <div class="clear"></div>
       </dl>
       <dl>
          <dd>证件类型：</dd>  
          <dt>身份证	</dt>
          <dd>证件号码：</dd>  
          <dt>${user.idcard} </dt>
          <dd style="width:130px">是否实名认证：</dd>  
          <dt>${isRealName}</dt>
          <div class="clear"></div>
       </dl>
       <dl>
          <dd>常用手机：</dd>  
          <dt>${phones}	</dt>
          <div class="clear"></div>
       </dl>
   </div>
   
   <div class="box">
        <div class="tit">总体评价</div>
        <div class="com">
             <div class="pjia">
                <ul>
                    <li style="width:190px;">建议审批通过：<br /><b>${reportMap.content}</b></li>
                    <li style="width:420px;">建议额度：<br /><p>${reportMap.applyMoney}<span>元</span></p></li>
                    <li style="width:280px;">总评分：<br /><p>${reportMap.fullscore}</p></li>
                    <li style="width:235px; border:0;">等级：<br /><em>${reportMap.passstatus}</em></li>
                    <div class="clear"></div>
                </ul>
             </div>
             <div class="xin">
                   <dl>
                      <dd>信 用 水 平：</dd>
                      <dt class="l_m">优于<b>${reportMap.level}<em>%</em></b>申请用户</dt>
                      <div class="clear"></div>
                   </dl>
                   <dl>
                      <dd>信息完整度：</dd>
                      <dt><div class="pros"><span style="width:${reportMap.complete}%;"></span></div> ${reportMap.complete}%</dt>
                      <div class="clear"></div>
                   </dl>
            </div>
        </div>
   </div>
   
   <div class="box">
        <div class="tit">基本信息</div>
        <div class="com">
          <dl class="bas_in">
             
                <dd>出生年月</dd>
                <dt>${identify.birthday}</dt>
                <dd>年龄</dd>
                <dt>${identify.age}</dt>
                <dd>户籍</dd>
                <dt>${identify.city}</dt>
                
                
           		 <div class="clear"></div>
             </dl>
             <dl class="bas_in">
             <dd>学历</dd>
              <dt>${somethingMap.xueli}</dt>
              <dd>职业</dd>
              <dt>${somethingMap.zhiye}</dt>
              <dd>工作地</dd>
              <dt>${workLocation}</dt>
                
                <div class="clear"></div>
             </dl>
             <dl class="bas_in">
                <dd>单位名称</dd>
                <dt>${somethingMap.pUnit}</dt>
                <dd>总工龄</dd>
                <dt>${workmap.allwork }</dt>
                <dd>目前公司工龄</dd>
                <dt>${workmap.nowwork }</dt>
                <div class="clear"></div>
          </dl>

                

          
          <dl class="bas_in">
              <dd>婚姻状况</dd>
                <dt><!-- 已婚 --></dt>
              <dd>是否有子女</dd>
                <dt><!-- 有子女 --></dt>
                 <dd>健康状况</dd>
                <dt></dt>
               
                <div class="clear"></div>
          </dl> 
          <dl class="bas_in">
              <dd>公共记录</dd>
              <dt ><a href="#" style="text-decoration:underline;">欠税记录、民事判决记录</a></dt>
                <div class="clear"></div>
          </dl> 
          
           
     </div>
   </div>
   
   <div class="box">
        <div class="tit">金融信息</div>
        <div class="com">
          <table width="100%" border="0" cellspacing="1" cellpadding="0" class="tab">
                <tr>
                  <th>&nbsp;</th>
                  <th>数据收集长度</th>
                  <th>最高金额</th>
                  <th>最低金额</th>
                  <th>月平均金额</th>
                  <th>近一年月平均金额</th>
                </tr>
                
               	  <c:if test="${not empty incomeMap}">
               	  
               	  <tr>
                  <td>
                     <b>收入</b>
                     <p></p>
                  </td> 
				  <td>${incomeMap.last}</td>
                  <td>${incomeMap.max}</td>
                  <td>${incomeMap.min}</td>
                  <td>${incomeMap.avg}</td>
                  <td>${incomeMap.avgLasgYear}</td>
                  </tr>  
               
			 </c:if>
			 
                <tr>
                  <td>
                     <b>支出</b>
                     <p></p>
                  </td>
                  <td>${jinMap.days}</td>
                  <td>${jinMap.maxmoney}</td>
                  <td>${jinMap.minmoney}</td>
                  <td>${jinMap.everyMonth}</td>
                  <td>${jinMap.recenteveryMonth}</td>
                </tr>
                
        </table>
        
       <div class="tu">
          <h2>收入支出金额图</h2>
		  <div id="incomeColumnChart" style="width:1130px;height:570px;"></div>
        </div> 

	 <table width="100%" border="0" cellspacing="1" cellpadding="0" class="tab">
                <tr>
                  <th>&nbsp;</th>
                  <th>数据收集时长</th>
                  <th>最高余额</th>
                  <th>最低余额</th>
                  <th>月平均余额</th>
                  <th>近一年月平均余额</th>
                </tr>
               <tr>
                  <td>
                     <b>储蓄</b>
                     <p></p>
                  </td>
                  <td>${yuebaoMap.dataLength }</td>
                  <td>${yuebaoMap.maxAmount*30 }</td>
                  <td>${yuebaoMap.minAmount*30 }</td>
                  <td>${yuebaoMap.monAmount*30 }</td>
                  <td>${yuebaoMap.yearAmount*30 }</td>
                </tr>
              <!--   <tr>
                  <td>
                     <b>贷款</b>
                     <p></p>
                  </td>
                  <td>2013.6-2014.6</td>
                  <td>3300</td>
                  <td>900</td>
                  <td>2990</td>
                  <td>2990</td>
                </tr> -->
        </table>
        
        <div class="tu">
          <h2>储蓄贷款余额图</h2>
		  <div id="savingsColumnChart" style="width:1130px;height:570px;"></div>
        </div>  
        
        <div class="assets">
           <h2>资产</h2>
           <dl>
              <dd>房产</dd>
              <dt>
              <c:if test="${user.hourse eq 1}">完全产权房</c:if>
              <c:if test="${user.hourse eq 2}">按揭购房</c:if>
              <c:if test="${user.hourse eq 3}">经济适用房</c:if>
              <c:if test="${user.hourse eq 4}">租房</c:if>
              <c:if test="${user.hourse eq 5}">和亲戚合住</c:if>
              <c:if test="${user.hourse eq 6}">无房</c:if>
							
              </dt>
              <div class="clear"></div>
           </dl>
           <dl>
              <dd>车</dd>
              <dt>
              <c:if test="${user.cars eq 1}">完全产权营运车</c:if>
              <c:if test="${user.cars eq 2}">按揭营运车</c:if>
              <c:if test="${user.cars eq 3}">完全产权轿车</c:if>
              <c:if test="${user.cars eq 4}">按揭轿车</c:if>
              <c:if test="${user.cars eq 5}">无</c:if>
							
              </dt>
              <div class="clear"></div>
           </dl>
           <p>注：金额以人民币计算，精确到元</p>
        </div>
     </div>
   </div>
   
   <!--  <div class="box"> 
        <div class="tit">信贷信息-征信</div>
        <div class="com">
          <table width="100%" border="0" cellspacing="1" cellpadding="0" class="tab">
                <tr>
                   <th style="width:482px;">&nbsp;</th>
                  <th>资产处置信息</th>
                  <th>保证人代偿信息</th>
                </tr>
                <tr>
                  <td>笔数</td>
                  <td>1</td>
                  <td>2</td>
                </tr>
          </table>
          
          <table width="100%" border="0" cellspacing="1" cellpadding="0" class="tab">
                <tr>
                   <th>&nbsp;</th>
                  <th>信用卡</th>
                  <th>住房贷款</th>
                  <th>其他贷款</th>
                </tr>
                <tr>
                  <td>账户数</td>
                  <td>7</td>
                  <td>0</td>
                  <td>0</td>
                </tr>
                <tr>
                  <td>未结算/未销户账户数</td>
                  <td>4</td>
                  <td>0</td>
                  <td>0</td>
                </tr>
                <tr>
                  <td>发生过逾期的账户数</td>
                  <td>0</td>
                  <td>0</td>
                  <td>0</td>
                </tr>
                <tr>
                  <td>发生过90天以上逾期的账户数</td>
                  <td>0</td>
                  <td>0</td>
                  <td>0</td>
                </tr>
                <tr>
                  <td>为他人担保笔数</td>
                  <td>0</td>
                  <td>0</td>
                  <td>0</td>
                </tr>
          </table>
          <p><a href="#" style="text-decoration:underline;">征信报告</a></p>
        
     </div>
   </div> -->
   
   
   <div class="box">
        <div class="tit">交易信息</div>
        <div class="com">
          <h2>网购</h2>
          <table width="100%" border="0" cellspacing="1" cellpadding="0" class="tab">
                <tr>
                  <th>网站</th>
                  <th>总消费金额</th>
                  <th>总消费笔数</th>
                  <th>注册时间</th>
                  <th>最大付款额</th>
                  <th>6个月以上交易金额</th>
                  <th>6个月以上交易频次</th>
                  <th>月均消费</th>
                </tr>
                
                
                 <c:if test="${not empty paylist}">
			   <c:forEach var="pay" items="${paylist}" varStatus="status"> 
			  <tr>
                  <td>${pay.domainName}</td>
                  <td>${pay.allmoney}</td>
                  <td>${pay.ordercount}</td>
                  <td>${pay.days}天</td>
                  <td>${pay.maxmoney}</td>
                  <td>${pay.sixmoney}</td>
                  <td>${pay.sixcount}</td>
                  <td>${pay.pingjun}</td>
                </tr>
			  </c:forEach>
			  </c:if>
			  
                
               
          </table>
        
          <div class="tu">
            <h2>网购消费金额图</h2>
			<div id="totalColumnChart" style="width:1130px;height:300px;"></div>
		
            <div class="line"></div>
          </div>
          
          <h2>网络支付信息</h2>
          <table width="100%" border="0" cellspacing="1" cellpadding="0" class="tab">
                <tr>
                  <th>&nbsp;</th>
                  <th>总金额</th>
                  <th>总笔数</th>
                  <th>最大付款额</th>
                  <th>代付金额</th>
                  <th>代付次数</th>
                  <th>最后使用时间</th>
                  <th>注册时间</th>
                </tr>
                <tr>
                  <td>${payMap.domain}</td>
                  <td>${payMap.allamount}</td>
                  <td>${payMap.sumcount}</td>
                  <td>${payMap.hamount}</td>
                  <td>${payMap.amount}</td>
                  <td>${payMap.paycount}</td>
                  <td>${payMap.recentDay}</td>
                  <td>${payMap.registerDate}</td>
                </tr>
          </table>
          
           <div class="tu">
            <h3>网络支付金额图</h3>
            <div id="totalColumnChart1" style="width:1130px;height:300px;"></div>
          
            <div class="line"></div>
          </div>
          
          <h2>话费</h2>
          <table width="100%" border="0" cellspacing="1" cellpadding="0" class="tab">
                <tr>
                  <th>手机号码</th>
                  <th>归属地</th>
                  <th>月平均花费</th>
                  <th>话费余额</th>
                  <th>使用时间</th>
                  <th>最后使用时间</th>
                </tr>
                
                <c:if test="${not empty phoneBillList}">
			   <c:forEach var="phoneBill" items="${phoneBillList}" varStatus="status"> 
			 
                <tr>
                  <td>${phoneBill.teleno}</td>
                  <td>${phoneBill.local}</td>
                  <td>${phoneBill.avg}</td>
                  <td>${phoneBill.cAllBalance}</td>
                  <td>${phoneBill.days}天</td>
                  <td>${phoneBill.latest}</td>
                </tr>
                </c:forEach>
                </c:if>
          </table>
          
          <div class="tu">
            <h3>话费消费金额图</h3>
            <div id="totalColumnChart2" style="width:1130px;height:300px;"></div>
            <div class="line"></div>
          </div>
 
     </div>
   </div>
   
    <!-- <div class="box">
          <div class="tit">意向信息</div>
          <div class="com">
          <table width="100%" border="0" cellspacing="1" cellpadding="0" class="tab">
                <tr>
                  <th>&nbsp;</th>
                  <th>关键词</th>
                </tr>
                <tr>
                  <td>借贷意向</td>
                  <td>中国建设银行房贷、招商银行信贷</td>
                </tr>
                <tr>
                  <td>理财意向</td>
                  <td>股票、互联网理财、银行理财产品</td>
                </tr>
                <tr>
                  <td>兴趣关注</td>
                  <td>购物：鞋子，上衣；家庭，宝宝</td>
                </tr>
          </table>
           <h2>借贷意向活跃度</h2>
          <div class="tu">
              <div class="pros2"><span style="width:45%;"></span></div> 45%
          </div>
          <div class="tu">
            <h3>借贷意向图</h3>
                    <img src="img/biao_r11_c1.jpg" width="1130" height="462" />
					<div id="totalPieChart" style="width:1130px;height:462px;"></div>
                    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="site">
                        <tr>
                          <th>关注站点</th>
                          <th>点击次数</th>
                        </tr>
                        <tr>
                          <td class="site_l">1  中国建设银行房贷</td>
                          <td>
                             <div class="pros3"><span style="width:78%"><p>11.72%</p></span></div>
                          </td>
                        </tr>
                        <tr>
                          <td class="site_l">2  招商银行信贷</td>
                          <td>
                              <div class="pros4"><span style="width:67.08%"><p>5.08%</p></span></div>
                          </td>
                        </tr>
                        <tr>
                          <td class="site_l">3  民生银行信贷</td>
                          <td><div class="pros4"><span style="width:52.08%"><p>5.08%</p></span></div></td>
                        </tr>
                        <tr>
                          <td class="site_l">4  广发银行信贷</td>
                          <td><div class="pros4"><span style="width:38.08%"><p>5.08%</p></span></div></td>
                        </tr>
                        <tr>
                          <td class="site_l">5  平安银行信贷</td>
                          <td><div class="pros4"><span style="width:24.08%"><p>5.08%</p></span></div></td>
                        </tr>
                        <tr>
                          <td class="site_l">6  中国工商银行信贷</td>
                          <td><div class="pros4"><span style="width:17.08%"><p>5.08%</p></span></div></td>
                        </tr>
                        <tr>
                          <td class="site_l">7  中国建设银行信贷</td>
                          <td><div class="pros4"><span style="width:5.08%"><p>5.08%</p></span></div></td>
                        </tr>
                     </table>
          </div>
          
          
           <h2>理财意向活跃度</h2>
          <div class="tu">
              <div class="pros2"><span style="width:28%;"></span></div> 28%
          </div>
          
           <div class="tu">
            <h3>理财意向图</h3>
                    <img src="img/biao_r11_c1.jpg" width="1130" height="462" />
					<div id="totalPieChart1" style="width:1130px;height:462px;"></div>
                    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="site">
                        <tr>
                          <th>关注站点</th>
                          <th>点击次数</th>
                        </tr>
                        <tr>
                          <td class="site_l">1 东方财富股票</td>
                          <td>
                             <div class="pros3"><span style="width:27%"><p>27%</p></span></div>
                          </td>
                        </tr>
                        <tr>
                          <td class="site_l">2  东方财富基金</td>
                          <td>
                              <div class="pros4"><span style="width:9.08%"><p>9.08%</p></span></div>
                          </td>
                        </tr>
                        <tr>
                          <td class="site_l">3  新浪财经</td>
                          <td><div class="pros4"><span style="width:6.08%"><p>6.08%</p></span></div></td>
                        </tr>
                        <tr>
                          <td class="site_l">4  工商银行</td>
                          <td><div class="pros4"><span style="width:6.08%"><p>6.08%</p></span></div></td>
                        </tr>
                        <tr>
                          <td class="site_l">5  东方财富行情中心</td>
                          <td><div class="pros4"><span style="width:5.55%"><p>5.55%</p></span></div></td>
                        </tr>
                        <tr>
                          <td class="site_l">6  农业银行</td>
                          <td><div class="pros4"><span style="width:5.28%"><p>5.28%</p></span></div></td>
                        </tr>
                        <tr>
                          <td class="site_l">7  和讯股票</td>
                          <td><div class="pros4"><span style="width:3.13%"><p>3.13%</p></span></div></td>
                        </tr>
                         <tr>
                          <td class="site_l">8  其他</td>
                          <td><div class="pros4"><span style="width:38%"><p>38%</p></span></div></td>
                        </tr>
                     </table>
          </div>
          
          <h2>兴趣关注活跃度</h2>
          <div class="tu">
              <div class="pros2"><span style="width:22%;"></span></div> 22%
          </div>
           <div class="tu">
            <h3>兴趣关注图</h3>
                    <img src="img/biao_r11_c1.jpg" width="1130" height="462" />
					<div id="totalPieChart2" style="width:1130px;height:462px;"></div>
                    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="site">
                        <tr>
                          <th>关注站点</th>
                          <th>点击次数</th>
                        </tr>
                        <tr>
                          <td class="site_l">1  女性时尚</td>
                          <td>
                             <div class="pros3"><span style="width:63%"><p>63%</p></span></div>
                          </td>
                        </tr>
                        <tr>
                          <td class="site_l">2  宝宝用品</td>
                          <td>
                              <div class="pros4"><span style="width:12.9%"><p>12.9%</p></span></div>
                          </td>
                        </tr>
                        <tr>
                          <td class="site_l">3  苹果4S</td>
                          <td><div class="pros4"><span style="width:8.4%"><p>8.4%</p></span></div></td>
                        </tr>
                        <tr>
                          <td class="site_l">4  日本动漫</td>
                          <td><div class="pros4"><span style="width:6.5%"><p>6.5%</p></span></div></td>
                        </tr>
                        <tr>
                          <td class="site_l">5  西餐礼仪</td>
                          <td><div class="pros4"><span style="width:5.7%"><p>5.7%</p></span></div></td>
                        </tr>
                        <tr>
                          <td class="site_l">6  大乐透</td>
                          <td><div class="pros4"><span style="width:4%"><p>4%</p></span></div></td>
                        </tr>
          
                     </table>
          </div>
          
          </div>
   </div> -->
   
   
          
 <!--   <div class="box">
        <div class="tit">欺诈风险</div>
        <div class="com">
             <div id="cheatPieChart" style="width:1130px;height:410px;"></div>
        </div>
   </div> -->
   
   <div class="box">
        <div class="tit">重要联系人</div>
        <div class="com">
              <table width="100%" border="0" cellspacing="1" cellpadding="0" class="tab">
                <tr>
                  <th>&nbsp;</th>
                  <th>号码</th>
                  <th>归属地</th>
                  <th>联系次数</th>
                  <th>联系时间</th>
                  <th>主叫次数</th>
                  <th>被叫次数</th>
                </tr>
             <c:if test="${not empty phoneList}">
			   <c:forEach var="phone" items="${phoneList}" varStatus="status"> 
			  <tr>
			    <td>${status.index + 1}</td>
			    <c:set var="phonenum" value="${phone.phone}" />
			    <c:set var="length" value="${fn:length(phonenum)}"/>
			    <td>${fn:substring(phonenum, 0, length-4)}****</td>
			    <td>${phone.place}</td>
			    <td>${phone.total}</td>   
			   
			    <td>
			   <script >
			    function formatSeconds(value) { 
				   var theTime = parseInt(value);// 秒
				   var theTime1 = 0;// 分
				   var theTime2 = 0;// 小时
				  // alert(theTime);
				   if(theTime > 60) {
				      theTime1 = parseInt(theTime/60);
				      theTime = parseInt(theTime%60);
				      // alert(theTime1+"-"+theTime);
				      if(theTime1 > 60) {
				         theTime2 = parseInt(theTime1/60);
				         theTime1 = parseInt(theTime1%60);
				       }
				   }
				       var result = ""+parseInt(theTime)+"秒";
				       if(theTime1 > 0) {
				       result = ""+parseInt(theTime1)+"分"+result;
				       }
				       if(theTime2 > 0) {
				       result = ""+parseInt(theTime2)+"小时"+result;
				       }
				       return result;
				   }
			    document.write(formatSeconds(${phone.totaltimes}));
			    </script>  
			    
			    </td>
			    <td>${phone.zhujiao}</td>	
			    <td>${phone.beijiao}</td>				
			  </tr>
			  </c:forEach>
			  </c:if>
       
				 
          </table>
        
        </div>
   </div>
   
   <div>
</div>
<script type="text/javascript" src="/${ctx }/js/highcharts.js"></script>
<script>
(function(){

	/*生成柱状图*/
	var createColumnChart = function(wrapper,needLegend,xData,yData){
		$(wrapper).highcharts({
			chart: {
				type: 'column'
			},
			title: {
				text: ''
			},
			xAxis: {
				categories: xData
			},
			yAxis: {
				title: {
					text: ''
				}
			},
			colors: ['#FF942C', '#00BBFF', '#fff', '#910000', '#1aadce', '#492970', '#f28f43', '#77a1e5', '#c42525', '#000'],
			credits: {
				enabled: false
			},
			tooltip:{
				enabled: false
			},
			navigator : {
				enabled : true
			},
			legend: {
				enabled: needLegend,
				itemStyle : {color:'#606060'}
				
			},
			plotOptions: {
				 column: {
					stacking: 'normal',
					dataLabels: {
					   enabled: true
					}
				 }
			},
			series: yData
		});
	};
	
	/*创建饼状图*/
	var creatPieChart = function(wrapper,data){
		var chart;
		$(wrapper).highcharts({
			chart: {
				plotBackgroundColor: null,
				plotBorderWidth: null,
				plotShadow: false
			},
			title: {
				text: ''
			},
			tooltip: {
				enabled: false
			},
			credits: {
				enabled: false
			},
			legend: {
				enabled: true,
				itemStyle : {color:'#606060'},
				layout : 'vertical',
				align: 'right',
				verticalAlign: 'top'
			},
			plotOptions: {
				pie: {
					dataLabels: {
						enabled: true,
						format: '<b>{point.name}</b>: {point.percentage:.1f} %'
					},
					showInLegend: true
				}
			},
			colors: ['#FF8021', '#A8EB50', '#5FCCF3', '#5FCCF3', '#4F67C9', '#492970', '#f28f43', '#77a1e5', '#c42525', '#a6c96a'],
			series: [{
				type: 'pie',
				data: data
			}]
		}); 

	};
	
	/*收入支出金额图*/
	createColumnChart($('#incomeColumnChart'),true,
			jQuery.parseJSON(${phonetime}),
		[{
			name : '收入',
			data: jQuery.parseJSON(${shebaovalues})
		},{
			name : '支出',
			data: jQuery.parseJSON(${ordervalues21})
		}] 
	);
	
	/*储蓄贷款金额图*/
	createColumnChart($('#savingsColumnChart'),true,
		jQuery.parseJSON(${yuebaotime}),
		[{
			name : '储蓄',
			data:  jQuery.parseJSON(${yuebaovalues})
		}
		/* ,{
			name : '贷款',
			data: [-3100, -3080, -3060, -3040, -3020,-3000, -2980, -2960, -2940, -2920, -2900, -2880]
		} */
		]
		
	);
	
	/*总消费金额图*/
	createColumnChart($('#totalColumnChart'),false,
		jQuery.parseJSON(${paytimes2}),
		[{
			data: jQuery.parseJSON(${ordervalues2})
		}]
	);
	
	createColumnChart($('#totalColumnChart1'),false,
		jQuery.parseJSON(${paytimes}),
		[{
			data: jQuery.parseJSON(${payvalues})
		}]
	);
	
	createColumnChart($('#totalColumnChart2'),false,
		jQuery.parseJSON(${phonetime}),
		[{
			data: jQuery.parseJSON(${phonevalues})
		}]
	);
	
	
	/*总消费金额饼状图*/
	creatPieChart($('#totalPieChart'),
		[
			['银行信贷', 16.8],
			['消费贷',12.8],
			['房贷',  8.5],
			['车贷',  6.2],
			['其他贷款',2.7]
		]
	);
	/*总消费金额饼状图*/
	creatPieChart($('#totalPieChart1'),
		[
			['股票', 44.3],
			['互联网理财',16.1],
			['银行理财产品',  13],
			['信托',  6.9],
			['基金',6.4],
			['私募',6],
			['黄金外汇',5.7],
			['期货',1.6]
		]
	);
	/*总消费金额饼状图*/
	creatPieChart($('#totalPieChart2'),
		[
			['购物', 79],
			['家庭',10],
			['生活资讯',  3.5],
			['保健',  2.9],
			['游戏',2.2],
			['社交',2]
		]
	);
	
	/*欺诈意向饼状图*/
	creatPieChart($('#cheatPieChart'),
		[
			['低风险',  50]
	
		]
	);
})();
</script>
</body>
</html>

