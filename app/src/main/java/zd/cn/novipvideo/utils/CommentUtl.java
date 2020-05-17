package zd.cn.novipvideo.utils;

import android.content.Context;
import android.content.res.Resources;

import java.util.Formatter;

import zd.cn.novipvideo.R;

public class CommentUtl {


    public static final String WELCOME_ID = "67b05e7cc9533510d4b8d9d4d78d0ae9";

    /**
     * 将毫秒转换成时间
     * @param mills
     * @return
     */
    public static String timeToString(int mills){
        int totalSeconds = mills / 1000;// 总共的秒数
        int second = totalSeconds % 60;// 得到多余的秒数  如 1小时30分29秒 29则为得到的数
        int minutes = (totalSeconds / 60) % 60;//得到余下的分数
        int hour = totalSeconds / 3600;
        if(hour > 0){
            return new Formatter().format("%2d:%02d:%02d",hour,minutes,second).toString();
        }else {
            return new Formatter().format("%02d:%02d",minutes,second).toString();
        }

    }

    /**
     * 去除广告js代码
     */
    public static String getClearAdDivJs(Context context) {
        String js = "javascript:";
        Resources res = context.getResources();
        String[] adDivs = res.getStringArray(R.array.abId);
        for (int i = 0; i < adDivs.length; i++) {
            js += "var adDiv" + i + "= document.getElementById('" + adDivs[i] + "');if(adDiv" + i + " != null)adDiv" + i + ".parentNode.removeChild(adDiv" + i + ");";
        }
        return js;
    }
    /**
     * 修改标题
     */
    public static String setTitle(String tit){
        //利用iframe的onload事件刷新页面
        String js = "javascript:";
        js += "document.title = '"+tit+"';\n" +
                "        var iframe = document.createElement('iframe');\n" +
                "        iframe.style.visibility = 'hidden';\n" +
                "        iframe.style.width = '1px';\n" +
                "        iframe.style.height = '1px';\n" +
                "        iframe.onload = function () {\n" +
                "            setTimeout(function () {\n" +
                "                document.body.removeChild(iframe);\n" +
                "            }, 0);\n" +
                "        };\n" +
                "        document.body.appendChild(iframe);";
        return js;
    }

    public static String createFrame(String url){
        String js = "javascript:";
        js += "var fram = document.createElement('iframe');fram.style.width = '100%';fram.style.height = '260px';fram.style.backgroundColor = 'red';fram.src = 'http://api.sigujx.com/?url="+url+"';document.body.appendChild(fram);";
        AppLog.e("js---:"+js);
        return js;
    }

   public static String cre(String url){
        String js = "javascript:";
        js += " \" var iFrame;\\n\"+\n" +
                "            \"    iFrame = document.createElement(\\\"iframe\\\");\\n\"+\n" +
                "            \"    iFrame.setAttribute(\\\"src\\\", '"+url+"');\\n\"+\n" +
                "            \"    iFrame.setAttribute(\\\"height\\\", \\\"260px\\\");\\n\"+\n" +
                "            \"    iFrame.setAttribute(\\\"width\\\", \\\"100%\\\");\\n\"+\n" +
                "            \"    iFrame.setAttribute(\\\"frameborder\\\", \\\"0\\\");\\n\"+\n" +
                "            \"    document.body.appendChild(iFrame);\\n\"+\n";


        return js;
   }

}
