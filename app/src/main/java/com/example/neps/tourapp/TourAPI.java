package com.example.neps.tourapp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class TourAPI {

    private final static String key = "9go6%2Fbmw4b1Hl11R8wYU3JfYb%2FHk3xi5zTcJrnyY6CSJKOkSFIIttkSCTCWbRE9KO4pLKyKqCzIx2x66NYsK5w%3D%3D";
    private final static String appName = "appTest";

    public static void main(String args[]) throws IOException {
        String result1 = TourAPI.getGanwonLocationList(100, 1);
        System.out.println("TourAPI.getGanwonLocationList()요청결과");
        System.out.println(result1 + "\n");

        String result2 = TourAPI.getCategoryList();
        System.out.println("TourAPI.getCategoryList()요청결과");
        System.out.println(result2);
    }

    // API에 JSON 요청
    private static String getJSON(URL url) throws IOException {
        String result;

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (InputStream in = conn.getInputStream();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            byte[] buf = new byte[1024 * 8];
            int length = 0;
            while ((length = in.read(buf)) != -1) {
                out.write(buf, 0, length);
            }
            result = new String(out.toByteArray(), "UTF-8");
        }

        conn.disconnect();
        return result;
    }

    public static String getGanwonLocationList(int numOfRows, int pageNo) throws IOException {
        String baseURL = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaCode";
        int areaCode = 32; // 강원도 지역코드
        String osName = "AND"; // 안드로이드 기반 개발

        URL url = new URL(baseURL
                + "?ServiceKey=" + key
                + "&areaCode=" + areaCode
                + "&numOfRows=" + numOfRows
                + "&pageNo=" + pageNo
                + "&MobileOS=" + osName
                + "&MobileApp=" + appName
                + "&_type=json");

        String result = getJSON(url);
        return result;
    }

    // 서비스 분류코드 조회
    public static String getCategoryList() throws IOException {
        String baseURL = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/categoryCode";
        int numOfRows = 10;
        int pageNo = 1;
        String osName = "AND";

        URL url = new URL(baseURL
                + "?ServiceKey=" + key
                + "&numOfRows=" + numOfRows
                + "&pageNo=" + pageNo
                + "&MobileOS=" + osName
                + "&MobileApp=" + appName
                + "&_type=json");

        String result = getJSON(url);
        return result;
    }

    public static String getAreaBasedList(int numOfRows, int pageNo, int sigunguCode, String cat1) throws IOException {
        String baseURL = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList";
        int areaCode = 32;
        String osName = "AND";

        URL url = new URL(baseURL
                + "?ServiceKey=" + key
                + "&areaCode=" + areaCode
                + "&sigunguCode=" + sigunguCode
                + "&cat1=" + cat1
                + "&numOfRows=" + numOfRows
                + "&pageNo=" + pageNo
                + "&MobileOS=" + osName
                + "&MobileApp=" + appName
                + "&arrange=" + "A"
                + "&listYN=" + "Y"
                + "&_type=json");

        String result = getJSON(url);
        return result;
    }

    public static String getDetailCommon(String contentId, String contentTypeId) throws IOException {
        String baseURL = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailCommon";

        String a = baseURL
                + "?ServiceKey=" + key
                + "&contentTypeId=" + contentTypeId
                + "&contentId=" + contentId
                + "&MobileOS=ETC&MobileApp=TourAPI3.0_Guide&defaultYN=Y&firstImageYN=Y&areacodeYN=Y&catcodeYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y&transGuideYN=Y&_type=json";
        System.out.println(a);


        URL url = new URL(baseURL
                + "?ServiceKey=" + key
                + "&contentTypeId=" + contentTypeId
                + "&contentId=" + contentId
                + "&MobileOS=ETC&MobileApp=TourAPI3.0_Guide&defaultYN=Y&firstImageYN=Y&areacodeYN=Y&catcodeYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y&transGuideYN=Y&_type=json"
        );
        String result = getJSON(url);
        return result;
    }
}

