package com.baishop.service.impl.sale;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baishop.entity.sale.Users;
import com.baishop.entity.sale.UsersCredits;
import com.baishop.framework.exception.ServiceException;
import com.baishop.service.BaseService;
import com.baishop.service.sale.UsersCreditsService;

public class UsersCreditsServiceImpl extends BaseService implements UsersCreditsService {

	private static final long serialVersionUID = 85332353472190384L;

	@Override
	public UsersCredits getCredits(Users user, int creditsId) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("creditsId", creditsId);
			
			UsersCredits credits = (UsersCredits)this.getSqlMapClientSlave(user.getSlaveDatabase()).queryForObject("UsersCredits.getCredits", params);
			return credits;			
		}catch(Exception e){
			throw new ServiceException(702001, e);
		}
	}

	@Override
	public List<UsersCredits> getCreditsList(Users user) {
		try{
			@SuppressWarnings("unchecked")
			List<UsersCredits> list = this.getSqlMapClientSlave(user.getSlaveDatabase()).queryForList("UsersCredits.getCredits");
			return list;			
		}catch(Exception e){
			throw new ServiceException(702001, e);
		}
	}

	@Override
	public void delCredits(Users user, int creditsId) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("creditsId", creditsId);
			
			this.getSqlMapClientSlave(user.getSlaveDatabase()).delete("UsersCredits.delCredits", params);
		}catch(Exception e){
			throw new ServiceException(702002, e);
		}
	}

	@Override
	public void addCredits(Users user, UsersCredits credits) {
		try{
			this.getSqlMapClientSlave(user.getSlaveDatabase()).insert("UsersCredits.addCredits", credits);
		}catch(Exception e){
			throw new ServiceException(702003, e);
		}
	}

	@Override
	public void editCredits(Users user, UsersCredits credits) {
		try{
			this.getSqlMapClientSlave(user.getSlaveDatabase()).update("UsersCredits.editCredits", credits);
		}catch(Exception e){
			throw new ServiceException(702004, e);
		}
	}

}
