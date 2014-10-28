package com.lkb.daoImp;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lkb.bean.Order;
import com.lkb.bean.PayInfo;
import com.lkb.bean.YuEBao;
import com.lkb.dao.IYuEBaoDao;


@Service
public class YuEBaoDaoImp {

	@Resource
	private IYuEBaoDao yuEBaoDao;
	
	public YuEBao findById(String yuEBaoId){
		return yuEBaoDao.findById(yuEBaoId);
	}
	
	public void saveYuEBao(YuEBao yuEBao){
		yuEBaoDao.saveYuEBao(yuEBao);
	}
	
	public void deleteYuEBao(String yuEBaoId){
		yuEBaoDao.deleteYuEBao(yuEBaoId);
	}
	
	public List<YuEBao> getYuEBaoByBaseUseridsource(Map map){
		return yuEBaoDao.getYuEBaoByBaseUseridsource(map);
	}
	/**返回(余额宝+邮箱账单) report页面
	 * canMap中参数  支付宝账号
	 */
	public Map<String,String> getYuEBaoPersonalStatistical(Map<String,String> canMap){
		return yuEBaoDao.getYuEBaoPersonalStatistical(canMap);
	}
		
	public void  update(YuEBao yuEBao){
		yuEBaoDao.update(yuEBao);
	}
	
	public List<Map> getEveryAmount(Map map){
		return yuEBaoDao.getEveryAmount(map);
	}
	
	public List<YuEBao> getYuEBaoByAlipay(String alipayName){
		return yuEBaoDao.getYuEBaoByAlipay(alipayName);
	}
	public YuEBao  getMaxYuEBaoTime(YuEBao bao){
		return yuEBaoDao.getMaxYuEBaoTime(bao);
	}
	public List getMaxYuEBaoAssignTime(YuEBao bao){
		return yuEBaoDao.getMaxYuEBaoAssignTime(bao);
	}
	public void insertbatch(List vctList){		
		yuEBaoDao.insertbatch(vctList);
	}
}
