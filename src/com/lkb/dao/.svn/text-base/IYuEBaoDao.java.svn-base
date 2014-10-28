package com.lkb.dao;

import java.util.List;
import java.util.Map;

import com.lkb.bean.YuEBao;



public interface IYuEBaoDao {
	
	YuEBao findById(String id);
	
	void saveYuEBao(YuEBao yuEBao);
	
	void deleteYuEBao(String id);
	
	List<YuEBao> getYuEBaoByBaseUseridsource(Map map);
	
	/**返回(余额宝+邮箱账单) report页面
	 * canMap中参数  支付宝账号
	 */
	public Map<String,String> getYuEBaoPersonalStatistical(Map<String,String> canMap);
	
	void  update(YuEBao yuEBao);
	
	List<Map> getEveryAmount(Map map);
	
	List<YuEBao> getYuEBaoByAlipay(String alipayName);
	YuEBao  getMaxYuEBaoTime(YuEBao bao);
	
	public List getMaxYuEBaoAssignTime(YuEBao bao);
	 
	 void insertbatch(List vctList);
}
