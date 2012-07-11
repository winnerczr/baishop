package com.baishop.service.goods;

import java.io.Serializable;
import java.util.List;

import com.baishop.entity.goods.Booking;

/**
 * 缺货登记服务接口
 * @author Linpn
 */
public interface BookingService extends Serializable {
	
	/**
	 * 获取缺货登记
	 * @param bookingId 缺货登记ID
	 * @return 返回缺货登记对象
	 */
	public Booking getBooking(long bookingId);	
	
	/**
	 * 获取缺货登记列表
	 * @param isDispose 是否处理
	 * @return 返回缺货登记列表
	 */
	public List<Booking> getBookingList(boolean isDispose);
	
	/**
	 * 获取缺货登记列表
	 * @param userId 用户id
	 * @return 返回缺货登记列表
	 */
	public List<Booking> getBookingListByUserId(long userId);
	
	/**
	 * 获取缺货登记列表
	 * @param goodsId 缺货登记的商品id
	 * @return 返回缺货登记列表
	 */
	public List<Booking> getBookingListByGoodsId(long goodsId);
	
	/**
	 * 删除缺货登记
	 * @param bookingId 缺货登记ID
	 */
	public void delBooking(long bookingId);	
	
	/**
	 * 删除缺货登记
	 * @param userId 用户id
	 */
	public void delBookingByUserId(long userId);	
	
	/**
	 * 删除缺货登记
	 * @param goodsId 缺货登记的商品id
	 */
	public void delBookingByGoodsId(long goodsId);	
	
	/**
	 * 删除缺货登记
	 * @param isDispose 是否处理
	 */
	public void delBookingByIsDispose(boolean isDispose);	
	
	/**
	 * 添加缺货登记
	 * @param booking 缺货登记对象
	 */
	public void addBooking(Booking booking);	
	
	/**
	 * 修改缺货登记
	 * @param booking 缺货登记对象
	 */
	public void editBooking(Booking booking);
	
}
