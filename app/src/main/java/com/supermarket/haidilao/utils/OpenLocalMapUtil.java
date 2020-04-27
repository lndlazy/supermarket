package com.supermarket.haidilao.utils;

/**
 * Created by linaidao on 2019/1/14.
 */

public class OpenLocalMapUtil {

    public static String getBaiduMapUri(String originLat, String originLon, String originName, String desLat, String desLon, String destination, String region, String src) {
        String uri = "intent://map/direction?origin=latlng:%1$s,%2$s|name:%3$s" +
                "&destination=latlng:%4$s,%5$s|name:%6$s&mode=driving&region=%7$s&src=%8$s#Intent;" +
                "scheme=bdapp;package=com.baidu.BaiduMap;end";
        return String.format(uri, originLat, originLon, originName, desLat, desLon, destination, region, src);
    }


    /**
     * 获取打开高德地图应用uri
     * style
     * 导航方式(0 速度快; 1 费用少; 2 路程短; 3 不走高速；4 躲避拥堵；5
     * 不走高速且避免收费；6 不走高速且躲避拥堵；
     * 7 躲避收费和拥堵；8 不走高速躲避收费和拥堵)
     */
    public static String getGdMapUri(String appName, String slat, String slon, String sname, String dlat, String dlon, String dname) {
        String newUri = "amapuri://route/plan/?sid=&slat=&slon=&sname=&did=&dlat=" +
                dlat + "&dlon=" +dlon +"&dname="+ dname + "&dev=0&t=0";
//        String newUri = "androidamap://navi?sourceApplication=%1$s&poiname=%2$s&lat=%3$s&lon=%4$s&dev=1&style=2";
        return String.format(newUri, appName, dname, dlat, dlon);
    }
}
