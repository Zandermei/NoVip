package zd.cn.novipvideo.model;

/**
 * 广告参数
 */
public class AdParams {

    private int adId;//广告id
    private String picture;//广告图片
    private String picUrl;//广告链接

    public int getAdId() {
        return adId;
    }

    public void setAdId(int adId) {
        this.adId = adId;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
