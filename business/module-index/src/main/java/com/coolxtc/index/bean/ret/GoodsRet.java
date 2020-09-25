package com.coolxtc.index.bean.ret;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * com.qinxin.xiaotemai.bean
 *
 * @author Beta-Tan
 * @date 2018/4/13
 * Description:
 */
public class GoodsRet {


    public static final String QUERY_TYPE_SYNTHESIZE = "synthesize";
    public static final String QUERY_TYPE_SALE = "sale";
    public static final String QUERY_TYPE_PRICE = "price";
    public static final String QUERY_TYPE_TICKET = "ticket";


    public static final String ORDER_TYPE_ASC = "asc";
    public static final String ORDER_TYPE_DESC = "desc";
    public static final String ORDER_TYPE_DEFAULT = "-1";
    public static final int TAO_BAO_TYPE = 1;
    public static final int PIN_DUO_DUO_TYPE = 2;
    /**
     * total : 2949
     * size : 2
     * pages : 1475
     * current : 1
     * records : [{"goodsId":"985795914595631105","title":"nordic cab母婴亲子车自行车拖挂车山地车装备滑雪车儿童双人推车","image":"http://img.alicdn.com/tfscom/i1/405641506/TB2hCq5i.R1BeNjy0FmXXb0wVXa_!!405641506.jpg","defaultPrice":5999,"couponPrice":100,"costPrice":5899,"saveMoney":5899,"sale":0,"goodsStore":1},{"goodsId":"985795854168293378","title":"[商场同款]Npaia恩派雅2018新款女装时尚连帽毛领反季羽绒服","image":"http://img.alicdn.com/tfscom/i1/2687728294/TB1TKewlHSYBuNjSspiXXXNzpXa_!!0-item_pic.jpg","defaultPrice":5959,"couponPrice":300,"costPrice":5659,"saveMoney":5659,"sale":0,"goodsStore":1}]
     */

    private int total;
    private int size;
    private int pages;
    private int current;
    private List<RecordsBean> records;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public List<RecordsBean> getRecords() {
        return records;
    }

    public void setRecords(List<RecordsBean> records) {
        this.records = records;
    }

    public static class RecordsBean implements MultiItemEntity {
        /**
         * goodsId : 985795914595631105
         * title : nordic cab母婴亲子车自行车拖挂车山地车装备滑雪车儿童双人推车
         * image : http://img.alicdn.com/tfscom/i1/405641506/TB2hCq5i.R1BeNjy0FmXXb0wVXa_!!405641506.jpg
         * defaultPrice : 5999.0
         * couponPrice : 100.0
         * costPrice : 5899.0
         * saveMoney : 5899.0
         * sale : 0
         * goodsStore : 1
         */

        private String goodsId;
        private String title;
        private String image;
        private String sysCategory;
        private String defaultPrice;
        private String couponPrice;
        private String costPrice;
        private String saveMoney;
        private int sale;
        private int goodsStore;
        private int platformType;
        private String numIid;

        public String getNumIid() {
            return numIid;
        }

        public void setNumIid(String numIid) {
            this.numIid = numIid;
        }

        public String getSysCategory() {
            return sysCategory;
        }

        public void setSysCategory(String sysCategory) {
            this.sysCategory = sysCategory;
        }

        public int getPlatformType() {
            return platformType;
        }

        public void setPlatformType(int platformType) {
            this.platformType = platformType;
        }

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getDefaultPrice() {
            return defaultPrice;
        }

        public void setDefaultPrice(String defaultPrice) {
            this.defaultPrice = defaultPrice;
        }

        public String getCouponPrice() {
            return couponPrice;
        }

        public void setCouponPrice(String couponPrice) {
            this.couponPrice = couponPrice;
        }

        public String getCostPrice() {
            return costPrice;
        }

        public void setCostPrice(String costPrice) {
            this.costPrice = costPrice;
        }

        public String getSaveMoney() {
            return saveMoney;
        }

        public void setSaveMoney(String saveMoney) {
            this.saveMoney = saveMoney;
        }

        public int getSale() {
            return sale;
        }

        public void setSale(int sale) {
            this.sale = sale;
        }

        public int getGoodsStore() {
            return goodsStore;
        }

        public void setGoodsStore(int goodsStore) {
            this.goodsStore = goodsStore;
        }

        @Override
        public int getItemType() {
            return 4;
        }
    }


}
