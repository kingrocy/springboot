package com.yunhui.springboot.bean;

/**
 * @Author: Yun
 * @Description:
 * @Date: Created in 2018-05-16 17:43
 */
public class Item {

    private Integer itemId;
    private Double itemPrice;
    private Long itemSales;

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Long getItemSales() {
        return itemSales;
    }

    public void setItemSales(Long itemSales) {
        this.itemSales = itemSales;
    }
}
