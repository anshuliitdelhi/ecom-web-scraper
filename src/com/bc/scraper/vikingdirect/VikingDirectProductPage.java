package com.bc.scraper.vikingdirect;

public class VikingDirectProductPage {
	String url;
	String name;
	String sku;
	String qty1;
	String bmsmQtyLast;
	String listPrice;
	String lastBmsmListPrice;
	String promoListPrice;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getQty1() {
		return qty1;
	}

	public void setQty1(String qty1) {
		this.qty1 = qty1;
	}

	public String getBmsmQtyLast() {
		return bmsmQtyLast;
	}

	public void setBmsmQtyLast(String bmsmQtyLast) {
		this.bmsmQtyLast = bmsmQtyLast;
	}

	public String getListPrice() {
		return listPrice;
	}

	public void setListPrice(String listPrice) {
		this.listPrice = listPrice;
	}

	public String getLastBmsmListPrice() {
		return lastBmsmListPrice;
	}

	public void setLastBmsmListPrice(String lastBmsmListPrice) {
		this.lastBmsmListPrice = lastBmsmListPrice;
	}

	public String getPromoListPrice() {
		return promoListPrice;
	}

	public void setPromoListPrice(String promoListPrice) {
		this.promoListPrice = promoListPrice;
	}

	@Override
	public String toString() {
		return "VikingDirectProductPage [url=" + url + ", name=" + name + ", sku=" + sku + ", qty1=" + qty1 + ", bmsmQtyLast=" + bmsmQtyLast
				+ ", listPrice=" + listPrice + ", lastBmsmListPrice=" + lastBmsmListPrice + ", promoListPrice=" + promoListPrice + "]";
	}

}
