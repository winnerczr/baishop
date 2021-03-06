package com.baishop.entity.goods;

import java.io.Serializable;

public class GoodsProps implements Serializable {

	private static final long serialVersionUID = -7048986462794284967L;

	/**
     * This field was generated by Apache iBATIS ibator.
     * This field corresponds to the database column bai_goods_props.goods_id
     *
     * @ibatorgenerated Thu Oct 13 18:37:07 CST 2011
     */
    private Long goodsId;

    /**
     * This field was generated by Apache iBATIS ibator.
     * This field corresponds to the database column bai_goods_props.props_id
     *
     * @ibatorgenerated Thu Oct 13 18:37:07 CST 2011
     */
    private Integer propsId;
    
    /**
     * This field was generated by Apache iBATIS ibator.
     * This field corresponds to the database column bai_goods_props.props_value
     *
     * @ibatorgenerated Thu Oct 13 18:37:07 CST 2011
     */
    private String propsValue;


    /**
     * This method was generated by Apache iBATIS ibator.
     * This method returns the value of the database column bai_goods_props.goods_id
     *
     * @return the value of bai_goods_props.goods_id
     *
     * @ibatorgenerated Thu Oct 13 18:37:07 CST 2011
     */
    public Long getGoodsId() {
        return goodsId;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method sets the value of the database column bai_goods_props.goods_id
     *
     * @param goodsId the value for bai_goods_props.goods_id
     *
     * @ibatorgenerated Thu Oct 13 18:37:07 CST 2011
     */
    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method returns the value of the database column bai_goods_props.props_id
     *
     * @return the value of bai_goods_props.props_id
     *
     * @ibatorgenerated Thu Oct 13 18:37:07 CST 2011
     */
    public Integer getPropsId() {
        return propsId;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method sets the value of the database column bai_goods_props.props_id
     *
     * @param propsId the value for bai_goods_props.props_id
     *
     * @ibatorgenerated Thu Oct 13 18:37:07 CST 2011
     */
    public void setPropsId(Integer propsId) {
        this.propsId = propsId;
    }
    

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method returns the value of the database column bai_goods_props.props_value
     *
     * @return the value of bai_goods_props.props_value
     *
     * @ibatorgenerated Thu Oct 13 18:37:07 CST 2011
     */
    public String getPropsValue() {
        return propsValue;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method sets the value of the database column bai_goods_props.props_value
     *
     * @param propsValue the value for bai_goods_props.props_value
     *
     * @ibatorgenerated Thu Oct 13 18:37:07 CST 2011
     */
    public void setPropsValue(String propsValue) {
        this.propsValue = propsValue;
    }
    
}