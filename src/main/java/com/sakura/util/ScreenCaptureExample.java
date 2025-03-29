package com.sakura.util;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ScreenCaptureExample {

    private static volatile boolean capturing = false;
    private static final CountDownLatch capturingLatch = new CountDownLatch(1);

    public static void main(String[] args) {
        try {
            startCapture();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void startCapture() throws IOException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        FFmpegFrameGrabber grabber = createFFmpegFrameGrabber(screenSize);
        FFmpegFrameRecorder recorder = createFFmpegFrameRecorder(grabber, screenSize);

        try {
            grabber.start();
            recorder.start();

            capturing = true;
            System.out.println("开始捕获屏幕...");
            Thread captureThread = new Thread(() -> {
                try {
                    captureFrames(grabber, recorder);
                } catch (FFmpegFrameRecorder.Exception | FFmpegFrameGrabber.Exception e) {
                    throw new RuntimeException(e);
                }
            });
            captureThread.start();

            // 主线程执行其他任务
            doSomethingElse();

            // 主线程结束时通知捕获线程关闭
            stopCapture();
        } finally {
            // 确保资源最终被关闭
            grabber.release();
            recorder.release();
        }
    }

    public static void stopCapture() {
        capturing = false;
        System.out.println("结束捕获屏幕...");
        capturingLatch.countDown(); // 通知捕获线程捕获应停止
    }

    private static void captureFrames(FFmpegFrameGrabber grabber, FFmpegFrameRecorder recorder) throws FFmpegFrameRecorder.Exception, FFmpegFrameGrabber.Exception {
        while (capturing) {
            Frame frame = grabber.grab();
            if (frame != null) {
                recorder.record(frame);
            }
        }
        recorder.stop();
        grabber.stop();
        capturingLatch.countDown(); // 通知主线程捕获已完成
    }

    private static FFmpegFrameGrabber createFFmpegFrameGrabber(Dimension screenSize) throws FrameGrabber.Exception {
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber("dshow");
        grabber.setFormat("dshow");

        // 设置固定的帧率
        grabber.setFrameRate(30);

        // 设置分辨率
        grabber.setImageWidth(screenSize.width);
        grabber.setImageHeight(screenSize.height);

        // 设置设备 ID
        // 注意: 这里需要替换成您实际使用的虚拟设备名称
        grabber.setOption("video_device", "screen-capture-recorder"); // 替换为您实际的设备名称

        return grabber;
    }

    private static FFmpegFrameRecorder createFFmpegFrameRecorder(FrameGrabber grabber, Dimension screenSize) throws IOException {
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder("D:/screenRecording/output.mp4", screenSize.width, screenSize.height);
        recorder.setFormat("mp4");

        // 使用H.264编码器
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);

        // 设置比特率为 2000 kbps (2 Mbps)
        recorder.setVideoBitrate(2000 * 1000);

        // 设置帧率为 30 fps
        recorder.setFrameRate(30);

        // 设置预设(preset)为 medium
        recorder.setOption("preset", "medium");

        // 设置 CRF (Constant Rate Factor)，值越低质量越高，一般建议范围 18-28
        recorder.setOption("crf", "23");

        recorder.setVideoQuality(0.01f); // 这个设置通常不需要，因为我们使用了 CRF

        return recorder;
    }

    private static void doSomethingElse() {
        // 主线程执行其他任务
        try {
            // 模拟主线程执行其他任务
            Thread.sleep(60000); // 模拟等待60秒
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Main thread interrupted.");
        }
    }
}
