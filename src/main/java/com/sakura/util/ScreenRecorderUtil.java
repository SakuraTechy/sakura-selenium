package com.sakura.util;

import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.ffmpeg.global.avcodec;

import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
@Slf4j
public class ScreenRecorderUtil {

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static volatile boolean recording = false;

    public static void main(String[] args) {
        try {
            startRecording();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (FFmpegFrameRecorder.Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void startRecording() throws Exception, FFmpegFrameRecorder.Exception {
        System.setProperty("java.awt.headless", "false");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        FrameGrabber grabber = createFFmpegFrameGrabber(screenSize);
        FFmpegFrameRecorder recorder = createFFmpegFrameRecorder(grabber);

        try {
            grabber.start();
            recorder.start();

            recording = true;
            log.info("开始录制屏幕");
            executor.submit(() -> {
                try {
                    recordFrames(grabber, recorder);
                } catch (FFmpegFrameRecorder.Exception | Exception e) {
                    throw new RuntimeException(e);
                }
            });

            // 在这里可以添加用户界面或者其他逻辑来控制录制的开始和停止
            // 例如，你可以在这里监听一个按键事件来停止录制
            // 为了演示，这里简单地使用一个线程睡眠来模拟用户操作
            try {
                TimeUnit.SECONDS.sleep(10); // 模拟用户等待60秒后停止录制
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Recording thread interrupted.");
            }
        } finally {
            stopRecording();
        }
    }

    public static void stopRecording() {
        recording = false;
    }

    private static void recordFrames(FrameGrabber grabber, FFmpegFrameRecorder recorder) throws Exception, FFmpegFrameRecorder.Exception {
        while (recording) {
//            log.info("录制屏幕中");
            // 获取屏幕捕捉的一帧
            Frame frame = grabber.grabFrame();
            // 将这帧放到录制
            if (frame != null) {
                recorder.record(frame);
            }
        }
        if(!recording){
            log.info("结束屏幕录制");
            recorder.stop();
            grabber.stop();
            // 确保资源最终被关闭
            grabber.release();
            recorder.release();
        }
        // 等待录制线程结束
        executor.shutdown();
        try {
            // 给录制线程一些时间来优雅地结束
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                // 如果录制线程没有在5秒内结束，则强制取消
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            // 如果主线程被中断，确保中断状态被恢复
            Thread.currentThread().interrupt();
            // 强制取消录制线程
            executor.shutdownNow();
        }
    }

    private static FFmpegFrameGrabber createFFmpegFrameGrabber(Dimension screenSize) throws Exception {
        FFmpegFrameGrabber grabber;
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("windows")) {
            grabber = new FFmpegFrameGrabber("desktop");
            grabber.setFormat("gdigrab");
//            grabber = new FFmpegFrameGrabber("avfoundation");
//            grabber.setFormat("avfoundation");
//            grabber = new FFmpegFrameGrabber("dshow");
//            grabber.setFormat("dshow");
        } else {
            grabber = new FFmpegFrameGrabber("x11grab");
            grabber.setFormat("x11grab");
        }
        // 设置固定的帧率
        grabber.setFrameRate(30);
        // 增加探针大小为10MB
        grabber.setOption("probesize", "10000000");
        // 增加缓冲区大小10MB
        grabber.setOption("bufsize", "10000000");
        // 设置分辨率
        grabber.setImageWidth(screenSize.width);
        grabber.setImageHeight(screenSize.height);

        grabber.setOption("offset_x", "0");
        grabber.setOption("offset_y", "0");
//        // 设置设备 ID
//        grabber.setDeviceId("video=\"screen-capture-recorder\""); // 这里的设备名需要替换为您实际使用的设备名
//        // 设置设备 ID
//        grabber.setDeviceIndex(0); // 对于屏幕捕获，设备索引通常是 0
        // 设置设备 ID
        grabber.setOption("list_devices", "true"); // 列出可用的设备
        grabber.setOption("framerate", "30"); // 设置帧率
        grabber.setOption("video_device", "0"); // 设备索引通常是 0 对于屏幕捕获
        grabber.setOption("pixel_format", "bgr24"); // 设置像素格式
        grabber.setOption("format", "avfoundation"); // 设置格式
        grabber.setOption("video_size", screenSize.width + "x" + screenSize.height); // 设置视频大小
        return grabber;
    }

    private static FFmpegFrameRecorder createFFmpegFrameRecorder(FrameGrabber grabber) {
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder("D:/screenRecoding/output.mp4", grabber.getImageWidth(), grabber.getImageHeight());
        recorder.setFormat("mp4");
        // 使用H.264编码器
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        // 设置比特率为 5000 kbps (2 Mbps)
        recorder.setVideoBitrate(5000 * 1000);
        // 设置帧率为 30 fps
        recorder.setFrameRate(30);
        // 设置预设(preset)为 medium
        recorder.setOption("preset", "medium");
        // 设置 CRF (Constant Rate Factor)，值越低质量越高，一般建议范围 18-28
        recorder.setOption("crf", "23");
        // 这个设置通常不需要，因为我们使用了 CRF
        recorder.setVideoQuality(0.01f);
        return recorder;
    }
}
