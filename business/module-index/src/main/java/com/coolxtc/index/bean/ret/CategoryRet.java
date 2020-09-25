package com.coolxtc.index.bean.ret;

import java.util.List;

/**
 * com.qinxin.xiaotemai.bean
 *
 * @author Beta-Tan
 * @date 2018/4/12
 * Description:
 */
public class CategoryRet {


    /**
     * total : 6
     * size : 10
     * pages : 1
     * current : 1
     * records : [{"categoryId":"981005828245651458","name":"精选","image":"http://xiaotemai.oss-cn-hangzhou.aliyuncs.com/1522724984/ourrWmHh.png"},{"categoryId":"981005884235415553","name":"女装","image":"http://xiaotemai.oss-cn-hangzhou.aliyuncs.com/1522725000/u3vWqJu2.png"},{"categoryId":"981005945409339394","name":"母婴","image":"http://xiaotemai.oss-cn-hangzhou.aliyuncs.com/1522725014/TeIginhn.png"},{"categoryId":"981005993652224001","name":"内衣","image":"http://xiaotemai.oss-cn-hangzhou.aliyuncs.com/1522725027/a8PYTlx0.png"},{"categoryId":"981050409884549121","name":"男装","image":"http://xiaotemai.oss-cn-hangzhou.aliyuncs.com/1522735618/u0YLEgXR.png"},{"categoryId":"983978392211365890","name":"童装","image":"http://xiaotemai.oss-cn-hangzhou.aliyuncs.com/1523433697/WFXB42un.png"}]
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

    public static class RecordsBean {
        /**
         * categoryId : 981005828245651458
         * name : 精选
         * image : http://xiaotemai.oss-cn-hangzhou.aliyuncs.com/1522724984/ourrWmHh.png
         */

        private String categoryId;
        private String name;
        private String image;

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
