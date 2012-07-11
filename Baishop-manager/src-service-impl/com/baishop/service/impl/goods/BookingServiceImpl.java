package com.baishop.service.impl.goods;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baishop.entity.goods.Booking;
import com.baishop.framework.exception.ServiceException;
import com.baishop.service.BaseService;
import com.baishop.service.goods.BookingService;

public class BookingServiceImpl extends BaseService implements BookingService {

	private static final long serialVersionUID = -7646248304018940570L;

	@Override
	public Booking getBooking(long bookingId) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("bookingId", bookingId);
			
			Booking booking = (Booking)this.getSqlMapClientShop().queryForObject("Booking.getBooking", params);
			return booking;			
		}catch(Exception e){
			throw new ServiceException(202001, e);
		}
	}

	@Override
	public List<Booking> getBookingList(boolean isDispose) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("isDispose", isDispose);
			
			@SuppressWarnings("unchecked")
			List<Booking> list = this.getSqlMapClientShop().queryForList("Booking.getBooking", params);
			return list;
		}catch(Exception e){
			throw new ServiceException(202001, e);
		}
	}

	@Override
	public List<Booking> getBookingListByUserId(long userId) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("userId", userId);
			
			@SuppressWarnings("unchecked")
			List<Booking> list = this.getSqlMapClientShop().queryForList("Booking.getBooking", params);
			return list;
		}catch(Exception e){
			throw new ServiceException(202001, e);
		}
	}

	@Override
	public List<Booking> getBookingListByGoodsId(long goodsId) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("goodsId", goodsId);
			
			@SuppressWarnings("unchecked")
			List<Booking> list = this.getSqlMapClientShop().queryForList("Booking.getBooking", params);
			return list;
		}catch(Exception e){
			throw new ServiceException(202001, e);
		}
	}

	@Override
	public void delBooking(long bookingId) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("bookingId", bookingId);
			
			this.getSqlMapClientShop().delete("Booking.delBooking", params);
		}catch(Exception e){
			throw new ServiceException(202002, e);
		}
	}

	@Override
	public void delBookingByUserId(long userId) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("userId", userId);
			
			this.getSqlMapClientShop().delete("Booking.delBooking", params);
		}catch(Exception e){
			throw new ServiceException(202002, e);
		}
	}

	@Override
	public void delBookingByGoodsId(long goodsId) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("goodsId", goodsId);
			
			this.getSqlMapClientShop().delete("Booking.delBooking", params);
		}catch(Exception e){
			throw new ServiceException(202002, e);
		}
	}

	@Override
	public void delBookingByIsDispose(boolean isDispose) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("isDispose", isDispose);
			
			this.getSqlMapClientShop().delete("Booking.delBooking", params);
		}catch(Exception e){
			throw new ServiceException(202002, e);
		}
	}

	@Override
	public void addBooking(Booking booking) {
		try{
			this.getSqlMapClientShop().insert("Booking.addBooking", booking);
		}catch(Exception e){
			throw new ServiceException(202003, e);
		}
	}

	@Override
	public void editBooking(Booking booking) {
		try{
			this.getSqlMapClientShop().update("Booking.editBooking", booking);
		}catch(Exception e){
			throw new ServiceException(202004, e);
		}
	}

}
