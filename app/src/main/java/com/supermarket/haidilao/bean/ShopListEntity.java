package com.supermarket.haidilao.bean;

import java.util.List;

/**
 * 商店列表
 */
public class ShopListEntity extends Basebean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * imageList : ["http://oss.xunxinsoft.com/upload/points/file/202001091634427.jpg","http://oss.xunxinsoft.com/upload/points/file/202001091639196.jpg"]
         * cid : 1106
         * name : 天安门
         * headImage : http://oss.xunxinsoft.com/upload/points/file/202001101426018.jpg
         * introduce : 天安门
         * contactName : 张三
         * contactPhone : 1234567
         * distance : 7643350.0
         * images : ["http://oss.xunxinsoft.com/upload/points/file/202001091634427.jpg","http://oss.xunxinsoft.com/upload/points/file/202001091639196.jpg"]
         * address : 天安门广场
         * latitude : 39.903179
         * longitude : 116.397755
         */

        private String cid;
        private String name;
        private String headImage;
        private String introduce;
        private String contactName;
        private String contactPhone;
        private float distance;
        private String images;
        private String address;
        private double latitude;
        private double longitude;
        private List<String> imageList;

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHeadImage() {
            return headImage;
        }

        public void setHeadImage(String headImage) {
            this.headImage = headImage;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public String getContactName() {
            return contactName;
        }

        public void setContactName(String contactName) {
            this.contactName = contactName;
        }

        public String getContactPhone() {
            return contactPhone;
        }

        public void setContactPhone(String contactPhone) {
            this.contactPhone = contactPhone;
        }

        public float getDistance() {
            return distance;
        }

        public void setDistance(float distance) {
            this.distance = distance;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public List<String> getImageList() {
            return imageList;
        }

        public void setImageList(List<String> imageList) {
            this.imageList = imageList;
        }
    }
}
