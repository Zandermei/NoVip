package zd.cn.novipvideo.model;

public class MovModel {

    private String vod_name;
    private String vod_actor;
    private String vod_pic;
    private String vod_remarks;//更新。。。
    private String vod_area;
    private String vod_lang;//语言种类 英
    private String vod_year;
    private String vod_content;
    private String vod_play_url;
    private VodUrl vod_url;


    public String getVod_play_url() {
        return vod_play_url;
    }

    public void setVod_play_url(String vod_play_url) {
        this.vod_play_url = vod_play_url;
    }

    public String getVod_name() {
        return vod_name;
    }

    public void setVod_name(String vod_name) {
        this.vod_name = vod_name;
    }

    public String getVod_actor() {
        return vod_actor;
    }

    public void setVod_actor(String vod_actor) {
        this.vod_actor = vod_actor;
    }

    public String getVod_pic() {
        return vod_pic;
    }

    public void setVod_pic(String vod_pic) {
        this.vod_pic = vod_pic;
    }

    public String getVod_remarks() {
        return vod_remarks;
    }

    public void setVod_remarks(String vod_remarks) {
        this.vod_remarks = vod_remarks;
    }

    public String getVod_area() {
        return vod_area;
    }

    public void setVod_area(String vod_area) {
        this.vod_area = vod_area;
    }

    public String getVod_lang() {
        return vod_lang;
    }

    public void setVod_lang(String vod_lang) {
        this.vod_lang = vod_lang;
    }

    public String getVod_year() {
        return vod_year;
    }

    public void setVod_year(String vod_year) {
        this.vod_year = vod_year;
    }

    public String getVod_content() {
        return vod_content;
    }

    public void setVod_content(String vod_content) {
        this.vod_content = vod_content;
    }

    public VodUrl getVod_url() {
        return vod_url;
    }

    public void setVod_url(VodUrl vod_url) {
        this.vod_url = vod_url;
    }

    public static class VodUrl{

        private String vod_play_url;
        private String vod_play_title;

        public String getVod_play_url() {
            return vod_play_url;
        }

        public void setVod_play_url(String vod_play_url) {
            this.vod_play_url = vod_play_url;
        }

        public String getVod_play_title() {
            return vod_play_title;
        }

        public void setVod_play_title(String vod_play_title) {
            this.vod_play_title = vod_play_title;
        }
    }


}
