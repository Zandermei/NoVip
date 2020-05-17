package zd.cn.novipvideo.utils;

import java.util.HashMap;
import java.util.Map;

public class UrlParams {

    public static Map<String,String> buildPostParams(String url){
        Map<String,String> map = new HashMap<>();
        map.put("url",url);
        map.put("cb","jQuery18202441015452723463_1564105264544");//(长度40  加上“）”括号 41位)
        map.put("_","1564105264780");
        return map;
    }
}
