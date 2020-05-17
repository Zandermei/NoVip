package zd.cn.novipvideo.model;

/**
 * Created by zdd on 2019/7/23 0023.
 * 解析得到视频的信息
 */

public class VideoList {

    private String videoName;//电影 分辨率 HD  高清，720p
    private String videoUrl;//电影url

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
