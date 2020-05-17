package zd.cn.novipvideo.widget.theme;


import zd.cn.novipvideo.widget.AliyunVodPlayerView;
import zd.cn.novipvideo.widget.control.ControlView;
import zd.cn.novipvideo.widget.guide.GuideView;
import zd.cn.novipvideo.widget.quality.QualityView;
import zd.cn.novipvideo.widget.speed.SpeedView;
import zd.cn.novipvideo.widget.tipsview.ErrorView;
import zd.cn.novipvideo.widget.tipsview.NetChangeView;
import zd.cn.novipvideo.widget.tipsview.ReplayView;
import zd.cn.novipvideo.widget.tipsview.TipsView;

/*
 * Copyright (C) 2010-2018 Alibaba Group Holding Limited.
 */

/**
 * 主题的接口。用于变换UI的主题。
 * 实现类有{@link ErrorView}，{@link NetChangeView} , {@link ReplayView} ,{@link ControlView},
 * {@link GuideView} , {@link QualityView}, {@link SpeedView} , {@link TipsView},
 * {@link AliyunVodPlayerView}
 */

public interface ITheme {
    /**
     * 设置主题
     * @param theme 支持的主题
     */
    void setTheme(AliyunVodPlayerView.Theme theme);
}
