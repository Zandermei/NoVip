package zd.cn.novipvideo.model;

import java.util.List;

/**
 * Created by Administrator on 2019/7/26 0026.
 */

public class VideoNameModel {
    private String videoOneName;//电影名称

    private List<VideoList> videoList;


    public List<VideoList> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<VideoList> videoList) {
        this.videoList = videoList;
    }

    public static class VideoList{
        private String videoHd;
        private String videoUrl;

        public String getVideoHd() {
            return videoHd;
        }

        public void setVideoHd(String videoHd) {
            this.videoHd = videoHd;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }
    }

    public String getVideoOneName() {
        return videoOneName;
    }

    public void setVideoOneName(String videoOneName) {
        this.videoOneName = videoOneName;
    }
}
