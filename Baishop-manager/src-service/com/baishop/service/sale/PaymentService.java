package com.baishop.service.sale;

import java.io.Serializable;
import java.util.List;

import com.baishop.entity.sale.Payment;

/**
 * 支付方式服务接口
 * @author Linpn
 */
public interface PaymentService extends Serializable {
	
	/**
	 * 获取支付方式
	 * @param payId 支付方式ID
	 * @return 返回支付方式对象
	 */
	public Payment getPayment(int payId);	
	
	/**
	 * 获取支付方式列表
	 * @return 返回支付方式列表
	 */
	public List<Payment> getPaymentList();
	
	/**
	 * 获取支付方式列表
	 * @param payType 支付类别
	 * @return 返回支付方式列表
	 */
	public List<Payment> getPaymentList(int payType);
	
	/**
	 * 删除支付方式
	 * @param payId 支付方式ID
	 */
	public void delPayment(int payId);	
	
	/**
	 * 删除支付方式
	 * @param payType 支付类别
	 */
	public void delPaymentByType(int payType);	
	
	/**
	 * 添加支付方式
	 * @param payment 支付方式对象
	 */
	public void addPayment(Payment payment);	
	
	/**
	 * 修改支付方式
	 * @param payment 支付方式对象
	 */
	public void editPayment(Payment payment);
	
}
