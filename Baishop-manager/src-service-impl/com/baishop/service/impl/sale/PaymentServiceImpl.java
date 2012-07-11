package com.baishop.service.impl.sale;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baishop.entity.sale.Payment;
import com.baishop.framework.exception.ServiceException;
import com.baishop.service.BaseService;
import com.baishop.service.sale.PaymentService;

public class PaymentServiceImpl extends BaseService implements PaymentService {

	private static final long serialVersionUID = 8496879176033107796L;

	@Override
	public Payment getPayment(int payId) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("payId", payId);
			
			Payment payment = (Payment) this.getSqlMapClientShop().queryForObject("Payment.getPayment", params);		
			return payment;
		}catch(Exception e){
			throw new ServiceException(203001, e);
		}
	}

	@Override
	public List<Payment> getPaymentList() {
		try{
			@SuppressWarnings("unchecked")
			List<Payment> list = this.getSqlMapClientShop().queryForList("Payment.getPayment");		
			return list;
		}catch(Exception e){
			throw new ServiceException(203001, e);
		}
	}

	@Override
	public List<Payment> getPaymentList(int payType) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("payType", payType);
			
			@SuppressWarnings("unchecked")
			List<Payment> list = this.getSqlMapClientShop().queryForList("Payment.getPayment", params);		
			return list;
		}catch(Exception e){
			throw new ServiceException(203001, e);
		}
	}

	@Override
	public void delPayment(int payId) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("payId", payId);
			
			this.getSqlMapClientShop().delete("Payment.delPayment", params);
		}catch(Exception e){
			throw new ServiceException(203002, e);
		}
	}

	@Override
	public void delPaymentByType(int payType) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("payType", payType);
			
			this.getSqlMapClientShop().delete("Payment.delPayment", params);
		}catch(Exception e){
			throw new ServiceException(203002, e);
		}
	}

	@Override
	public void addPayment(Payment payment) {
		try{
			this.getSqlMapClientShop().insert("Payment.addPayment", payment);
		}catch(Exception e){
			throw new ServiceException(203003, e);
		}
	}

	@Override
	public void editPayment(Payment payment) {
		try{
			this.getSqlMapClientShop().update("Payment.editPayment", payment);
		}catch(Exception e){
			throw new ServiceException(203004, e);
		}
	}

}
