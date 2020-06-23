package zd.cn.novipvideo.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2019/7/15 0015.
 */

public class Url {
    public static String[] urlJson = {
            "http://jiexi.380k.com/?url=",
            "http://api.baiyug.vip/index.php?url=",
            "https://jx40.net/url=",
            "http://yun.360dy.wang/jx.php?url=",
            "https://play.fo97.cn/?url=",
            "https://www.bavei.com/vip/?url=",
            "https://jx.618g.com/?url=",
            "https://z1.m1907.cn/?jx=",
            "http://vip.wandhi.com/?v=",
            "http://jx.618ge.com/?url=",
            };

    //http://jiexi.380k.com/?url
    // http://api.baiyug.vip/index.php?url
    // https://jx40.net/url




//http://jx.aeidu.cn/index.php?url=
// 星座  key 6e2837e9daf6ed49ea3abec71305432a
    public static String[] videoUrl = {"https://3g.v.qq.com/", "https://youku.com/", "https://m.iqiyi.com/",
            "http://m.le.com/","https://m.mgtv.com", "https://m.tv.sohu.com/"};

    public static String searchUrl = "https://yz.m.sm.cn/s?from=wm716726&q=";//搜索文字

    public static String[] loadUrl = {"http://hao.uc.cn/?uc_param_str=dnfrpfbivecpbtnt",
            "http://m.uczzd.cn/webview/newslist?app=mixia1-iflow&zzd_from=mixia1-iflow&uc_param_str=dndsfrvesvntnwpfgi&uc_biz_str=S%253Acustom%257CC%253Azzd_list&is_hide_top=1&is_hide_bottom=1",
            "https://m.baidu.com",
            "https://cpu.baidu.com/1033/ea192ece?sub_chn=1062&from=list&scid=29279"};
    public static String sigUrl = "https://www.qiushibaike.com/text/";//搞笑
    public static String xzUrl = "http://web.juhe.cn:8080/constellation/getAll";



    //搜索结果
    public static String searchUrl_video = "http://ht.ka61b.cn/meiju/app/cms/public/?service=App.Mov.SearchVod";



/**
 * https://vip.rkesb.cn/vip/?url=//89解析
 * "http://tv.wandhi.com/go.html?url=",玩的嗨
 * http://yun.baiyug.cn/vip/index.php?url=
 *https://z1.m1907.cn/api/v/?z=d48dca120c2cb81c11cfa6eca509066d&jx=
 * https://www.myxin.top/jx/api/?url=
 *  "http://jx.618ge.com/?url=",
 "https://www.myxin.top/jx/api/?url=",
 "https://jx.618g.com/?url="};
 *http://v.ain19.com/?v=https://v.qq.com/x/cover/vbb35hm6m6da1wc.html
 *  <option value="https://z1.m1907.cn/?jx=">默认线路(腾讯 爱奇艺 优酷 乐视 芒果 搜狐)</option>
 <option value="https://api.flvsp.com/?url=">综合 + B站解析</option>
 <option value="https://www.bavei.com/vip/?url=">默认线路(腾讯 爱奇艺 优酷 乐视 芒果 搜狐) backup1</option>
 <option value="https://api.100bx.top/?url=">优酷优先，其他可以尝试</option>
 <option value="http://api.smq1.com/?url=">iqiyi</option>
 http://api.smq1.com/api.php?url=https://v.qq.com/x/cover/xbd1y6fvwl3maoz.html&cb=jQuery18202441015452723463_1564105264544&_=1564105264780
 <option value="https://vip.bljiex.com/?v=">解析，❀搜索❀</option>
 *
 */

/**
 * 获取电影列表
 */
public static Map<String, String> buildGetMovParams(String searchMsg) {
    Map<String, String> map = new HashMap<>();
    try {
        map.put("key", URLEncoder.encode(searchMsg,"utf-8"));
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }
    return map;
}



}