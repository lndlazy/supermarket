package com.supermarket.haidilao.bean;

import java.util.List;

public class InfoEntity extends Basebean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * setId : 1
         * type : 0
         * title : 启动页
         * content :
         * image : http://oss.xunxinsoft.com/img/mp_share_img.png
         * jumpType : 0
         * jumpId :
         * pkId : 0
         * url :
         * createTime : 2019-12-26 17:14:26
         */

        private int setId;
        private int type;
        private String title;
        private String content;
        private String image;
        private int jumpType;
        private String jumpId;
        private int pkId;
        private String url;
        private String createTime;

        public int getSetId() {
            return setId;
        }

        public void setSetId(int setId) {
            this.setId = setId;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getJumpType() {
            return jumpType;
        }

        public void setJumpType(int jumpType) {
            this.jumpType = jumpType;
        }

        public String getJumpId() {
            return jumpId;
        }

        public void setJumpId(String jumpId) {
            this.jumpId = jumpId;
        }

        public int getPkId() {
            return pkId;
        }

        public void setPkId(int pkId) {
            this.pkId = pkId;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
