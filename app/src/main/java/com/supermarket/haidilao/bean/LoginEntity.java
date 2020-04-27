package com.supermarket.haidilao.bean;

public class LoginEntity extends Basebean {

    /**
     * data : {"uid":1004}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * uid : 1004
         */

        private long uid;

        public long getUid() {
            return uid;
        }

        public void setUid(long uid) {
            this.uid = uid;
        }
    }
}
