package com.lkb.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.codehaus.jackson.map.util.JSONPObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

public class JsonTest {

	@Test
	public void test() {
/*
 * 历史账单
 * */
		/*
		String content="var QueryYH102010002_Data={\"rspPublicArgs\":{\"beginDate\":\"20140201\",\"bizInfo\":[\"019_11012000100\"],\"cityCode\":\"\",\"endDate\":\"20140228\",\"netType\":\"01\",\"payType\":\"\",\"provinceCode\":\"\",\"queryBeginEndDate\":\"2014-03-01至2014-03-31\",\"queryDate\":\"2014年04月09 17:11:50\",\"rspArgs\":{\"CRBT\":\"\",\"MMSpointTopoint\":\"\",\"SMSpointTopoint\":\"\",\"TGAcallFee\":\"\",\"TGAroam\":\"\",\"VPTGAcallFee\":\"\",\"VPTGAroam\":\"\",\"VPdomesticCallFee\":\"\",\"VPinternationalCallFee\":\"\",\"VPinternationalRoam\":\"\",\"YAxisMaxValue\":\"84.0\",\"alarmClockService\":\"\",\"available\":\"\",\"basicMonthlyFee\":\"\",\"basicPackage\":\"\",\"callFee\":\"\",\"callerJX\":\"\",\"callers\":\"\",\"columnFixList\":[],\"columnList\":[{\"color\":\"32CD32\",\"name\":\"月固定费\",\"value\":\"14.00\"},{\"color\":\"32CD32\",\"name\":\"语音通话费\",\"value\":\"6.63\"},{\"color\":\"32CD32\",\"name\":\"增值业务费\",\"value\":\"0.10\"}],\"customName\":\"王婷\",\"discountFee\":\"20.73\",\"domesticCallFee\":\"\",\"exceptionMsg\":\"\",\"fixFeeSum\":\"\",\"historyfee\":[],\"increaseBussinessFee\":\"\",\"increaseFen\":\"\",\"increasePackage\":\"\",\"infoMMS\":\"\",\"informedSecretary\":\"\",\"instantMessaging\":\"\",\"internationalCallFee\":\"\",\"internationalRoam\":\"\",\"itemList\":[{\"fee\":\"14.00\",\"name\":\"月固定费\"},{\"fee\":\"6.63\",\"name\":\"语音通话费\"},{\"fee\":\"0.10\",\"name\":\"增值业务费\"}],\"list\":[{\"fee\":\"14.00\",\"style\":\"bold\",\"name\":\"月固定费\"},{\"fee\":\"9.00\",\"style\":\"\",\"name\":\"--基本月租费\"},{\"fee\":\"5.00\",\"style\":\"\",\"name\":\"--来电显示\"},{\"fee\":\"6.63\",\"style\":\"bold\",\"name\":\"语音通话费\"},{\"fee\":\"6.63\",\"style\":\"\",\"name\":\"--本地通话费\"},{\"fee\":\"0.10\",\"style\":\"bold\",\"name\":\"增值业务费\"},{\"fee\":\"0.10\",\"style\":\"\",\"name\":\"--点对点短信\"}],\"listObjectClassName\":\"list.get(0).className=java.util.HashMap\",\"map\":null,\"mapKeys\":\"\",\"month\":\"2014年02月01日 至 2014年02月28日\",\"monthFixedFee\":\"\",\"notDisturbService\":\"\",\"obj\":null,\"onepass\":\"\",\"packageType\":\"\",\"payTotal\":\"0.00\",\"personalisedRing\":\"\",\"phoneEmail\":\"\",\"phoneMusic\":\"\",\"phoneNet\":\"\",\"phoneNewPaper\":\"\",\"phoneTV\":\"\",\"preMonthInterral\":\"\",\"printDateForView\":\"2014年04月09日\",\"proviceName\":\"山西\",\"queryDateForView\":\"\",\"queryMonth\":\"2014-04-09\",\"queryType\":\"undefined\",\"set\":[],\"str2\":\"\",\"str7\":\"\",\"substituteFee\":\"\",\"sumFee\":\"20.73\",\"superpositionPackage\":\"\",\"taocanyuliang\":\"\",\"tecip01007DTO\":null,\"tecip01008DTO\":null,\"tecip01018DTO\":[],\"tecip2000111DTO\":null,\"telnavigation\":\"\",\"time\":{\"date\":9,\"day\":3,\"hours\":17,\"minutes\":11,\"month\":3,\"seconds\":50,\"time\":1397034710575,\"timezoneOffset\":-480,\"year\":114},\"totalFee\":\"\",\"totalIntegral\":\"\",\"unicomNICAM\":\"\",\"unicomSMS\":\"\",\"unicomSecretary\":\"\",\"useIntegral\":\"\",\"usedFen\":\"\",\"usedIntegral\":\"\",\"videophoneFee\":\"\"},\"rspCode\":\"00\",\"rspDesc\":\"OK\",\"templateId\":\"2\",\"userNumber\":\"13134633106\"}};QUERY_YH102010002.processData(QueryYH102010002_Data);";
		String[] str=content.split("var QueryYH102010002_Data=");
		content=str[1];
		str=content.split(";QUERY_YH102010002.");
		content=str[0];
		System.out.println(content);
		JSONObject json;
		try {
			json=new JSONObject(content);
			JSONObject js=json.getJSONObject("rspPublicArgs");
			String name=js.getJSONObject("rspArgs").getString("customName");
			System.out.println("name:"+name);
			String cDate=js.getString("beginDate");
			System.out.println("cDate:"+js.getString("beginDate"));
			String teleno=js.getString("userNumber");
			System.out.println("teleno:"+teleno);
			String dependCycle=js.getJSONObject("rspArgs").getString("month");
			System.out.println("dependCycle:"+dependCycle);
			String cAllPay=js.getJSONObject("rspArgs").getString("sumFee");
			System.out.println("cAllPay:"+cAllPay);
			String ygdf="";
			String jbyzf="";
			String ldxsf="";
			String yythf="";
			String bdthf="";
			String zzywf="";
			String ddddx="";
			String ltzx="";
			JSONArray jsa=js.getJSONObject("rspArgs").getJSONArray("list");
			for(int i=0;i<jsa.length();i++){
				JSONObject jso=jsa.getJSONObject(i);
				if("月固定费".equals(jso.getString("name"))){
					ygdf=jso.getString("fee");
				}
				else if("--基本月租费".equals(jso.getString("name"))){
					jbyzf=jso.getString("fee");
				}
				else if("--来电显示".equals(jso.getString("name"))){
					ldxsf=jso.getString("fee");
				}
				else if("语音通话费".equals(jso.getString("name"))){
					yythf=jso.getString("fee");
				}
				else if("--本地通话费".equals(jso.getString("name"))){
					bdthf=jso.getString("fee");
				}
				else if("增值业务费".equals(jso.getString("name"))){
					zzywf=jso.getString("fee");
				}
				else if("--点对点短信".equals(jso.getString("name"))){
					ddddx=jso.getString("fee");
				}
				else if("--联通在信".equals(jso.getString("name"))){
					ltzx=jso.getString("fee");
				}
			}
			System.out.println("月固定费："+ygdf);
			System.out.println("基本月租费：："+jbyzf);
			System.out.println("来电显示费：："+ldxsf);
			System.out.println("语音通话费：："+yythf);
			System.out.println("本地通话费：："+bdthf);
			System.out.println("增值业务费：："+zzywf);
			System.out.println("点对点短信：："+ddddx);
			System.out.println("联通在信：："+ltzx);
			//System.out.columnListprintln(name);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		/*
		 * 当月账单
		 */
		String content="null,\"acctInfoBean\":null,\"balanceReportResult\":{\"realfee\":\"\",\"iteminfo\":[{\"fee\":\"14.00\",\"integrateitem\":\"月固定费\",\"balance\":null,\"upperacctitemcode\":null,\"integrateitemcode\":\"80100\",\"integrateitemcodetype\":null,\"receivedfee\":null},{\"fee\":\"0.78\",\"integrateitem\":\"语音通话费\",\"balance\":null,\"upperacctitemcode\":null,\"integrateitemcode\":\"80200\",\"integrateitemcodetype\":null,\"receivedfee\":null},{\"fee\":\"9.00\",\"integrateitem\":\"基本月租费\",\"balance\":null,\"upperacctitemcode\":\"80100\",\"integrateitemcode\":\"80101\",\"integrateitemcodetype\":null,\"receivedfee\":null},{\"fee\":\"5.00\",\"integrateitem\":\"来电显示\",\"balance\":null,\"upperacctitemcode\":\"80100\",\"integrateitemcode\":\"80102\",\"integrateitemcodetype\":null,\"receivedfee\":null},{\"fee\":\"0.78\",\"integrateitem\":\"本地通话费\",\"balance\":null,\"upperacctitemcode\":\"80200\",\"integrateitemcode\":\"80201\",\"integrateitemcodetype\":null,\"receivedfee\":null}],\"totalfee\":\"0.00\",\"totalcreditvalue\":null,\"availablecreditvalue\":null,\"acctbalance\":\"39.06\",\"balance\":\"24.28\",\"acctbalancelist\":[{\"acctbalance\":\"39.06\",\"acctbalanceunavailable\":\"0.00\",\"ayavailablelist\":[{\"prepayavailable\":\"39.06\",\"promavailable\":\"0.00\"}],\"unavailablelist\":[{\"prepayunavailable\":\"0.00\",\"promunavailable\":\"0.00\"}]}],\"balancereportscheme\":\"B\",\"specialfee\":\"0.00\",\"acctbalancemark\":\"1\",\"chargefee\":null,\"realtimemark\":\"1\",\"availablecreditmark\":\"0\",\"totalcreditmark\":\"0\",\"rspcode\":\"0000\",\"rspdesc\":\"OK\",\"rspts\":\"20140407214932\",\"reqsign\":null,\"rspsign\":\"Qi79fBM0gzchZR55/SftJmN/gf8VLCu503KiJXnIpsQKdkhcecFrO2Y91iEZvkYmF0Gx5ySMsdFi5zR9HTrzkQ==\",\"trxid\":\"GWAY2014040721493120401501768\",\"resultMap\":null,\"busiorder\":\"BUSI2014040721493102811306381\",\"errMessage\":null,\"exceptionContent\":null},\"realfeestr\":null,\"ywgw\":null},\"queryBeginEndDate\":null}};Query_YH102010001.processData(QUERYYX102010001_Data);";
		String[] str=content.split(":null,\"balanceReportResult\":");
		content=str[1];
		str=content.split(";Query_YH102010001.processData");
		content=str[0];
		String name="";
		System.out.println("name:"+name);
		String cDate="";
		System.out.println("cDate:"+cDate);
		String teleno="";
		System.out.println("teleno:"+teleno);
		String dependCycle="";
		System.out.println("dependCycle:"+dependCycle);
		String cAllPay="";
		System.out.println("cAllPay:"+cAllPay);
		String ygdf="";
		String jbyzf="";
		String ldxsf="";
		String yythf="";
		String bdthf="";
		String zzywf="";
		String ddddx="";
		String ltzx="";
		System.out.println(content);
		try {
			JSONObject json=new JSONObject(content);
			JSONArray jsa=json.getJSONArray("iteminfo");
			for(int i=0;i<jsa.length();i++){
				JSONObject jso=jsa.getJSONObject(i);
				if("月固定费".equals(jso.getString("integrateitem"))){
					ygdf=jso.getString("fee");
				}
				else if("基本月租费".equals(jso.getString("integrateitem"))){
					jbyzf=jso.getString("fee");
				}
				else if("来电显示".equals(jso.getString("integrateitem"))){
					ldxsf=jso.getString("fee");
				}
				else if("语音通话费".equals(jso.getString("integrateitem"))){
					yythf=jso.getString("fee");
				}
				else if("本地通话费".equals(jso.getString("integrateitem"))){
					bdthf=jso.getString("fee");
				}
				else if("增值业务费".equals(jso.getString("integrateitem"))){
					zzywf=jso.getString("fee");
				}
				else if("点对点短信".equals(jso.getString("integrateitem"))){
					ddddx=jso.getString("fee");
				}
				else if("联通在信".equals(jso.getString("integrateitem"))){
					ltzx=jso.getString("fee");
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("月固定费："+ygdf);
		System.out.println("基本月租费：："+jbyzf);
		System.out.println("来电显示费：："+ldxsf);
		System.out.println("语音通话费：："+yythf);
		System.out.println("本地通话费：："+bdthf);
		System.out.println("增值业务费：："+zzywf);
		System.out.println("点对点短信：："+ddddx);
		System.out.println("联通在信：："+ltzx);
	}
	
	public static void main(String[] args){
		
		BigDecimal ss = new BigDecimal("234");
		String zhujiao=ss.toString();
	}

}
