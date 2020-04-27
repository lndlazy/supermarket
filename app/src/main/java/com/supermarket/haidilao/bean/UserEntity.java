package com.supermarket.haidilao.bean;

public class UserEntity extends Basebean {


    /**
     * data : {"uid":1004,"realName":"lee","phone":"17600100541","headImage":"http://oss.xunxinsoft.com/upload/pparty/file/head_defult.png"}
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
         * realName : lee
         * phone : 17600100541
         * headImage : http://oss.xunxinsoft.com/upload/pparty/file/head_defult.png
         */

        private Long uid;
        private String realName;
        private String phone;
        private String headImage;

        public Long getUid() {
            return uid;
        }

        public void setUid(Long uid) {
            this.uid = uid;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getHeadImage() {
            return headImage;
        }

        public void setHeadImage(String headImage) {
            this.headImage = headImage;
        }
    }
}
