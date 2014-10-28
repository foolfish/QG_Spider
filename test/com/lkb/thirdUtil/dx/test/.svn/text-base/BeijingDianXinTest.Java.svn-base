import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Collection;

import org.junit.Test;
import org.junit.Before;

import com.lkb.bean.DianXinDetail;
import com.lkb.bean.User;
import com.lkb.debug.DebugUtil;
import com.lkb.robot.Spider;
import com.lkb.robot.SpiderManager;
import com.lkb.thirdUtil.dx.AbstractDianXinController;
import com.lkb.thirdUtil.dx.ShangHaiDianXin;
import com.lkb.util.DateUtils;
import com.lkb.util.httpclient.CUtil;
import java.text.DecimalFormat; 

//ʾ������
public class BeiJingDianXinTest etends DianXinTelTest {

	//��д�ú���
	public void setDxandNoandPwd(String phoneNo,String password){
		setNoandPwd(phoneNo,password);
		dx=new BeiJingDianXin(spider, null, phoneNo, password, "2345", null);
		
	}
	
	@Before()
	public void setBefore(){
		//name,balance,idcard,addresse,email
		 String[] personInfo = new String[]{"�ź�", "9", null, null, null};
		//ts(yyyy/MM/dd-HH:mm��,RecevierPhone,fee,duratio
		String[] detailInfo = new String[]{"2014/08/10-12:25,13569775,0.3,10", "2014/08/11-08:09,10000,0,30"};
		
		//�˵���ϢcTime,brand,ztcjbf; //���ײͻ�� bdthf; ����ͨ����ldxsf; ������ʾ�� mythf; ����ͨ����
		String[] telListInfo=new String[]{"2014/07���Ϻ����ţ�10��5��1��3",};
		setDxandNoandPwd("13244445555","345678");
		initializeInfo(personInfo, detailInfo,telListInfo);
	}
}