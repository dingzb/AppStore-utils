package com.tendyron.routewifi.core.app.download;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * Created by Neo on 2017/1/11.
 */
public class Downloader {
    private String url;
    private long currentLength = 0;
    private long existLength;
    private long length = 0;
    private String signature = "";
    private String filePath;
    private boolean stop = true;


    public String getUrl() {
        return url;
    }

    public long getLength() {
        return length;
    }

    public String getSignature() {
        return signature;
    }

    public long getCurrentLength() {
        return currentLength;
    }

    public long getExistLength() {
        return existLength;
    }

    public Downloader(String url, String filePath) {
        this.url = url;
        this.filePath = filePath;
    }

    public void pause() {
        stop = true;
    }

    public boolean stop() {
        if (!stop) {
            stop = true;
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        File target = new File(filePath);

        return !target.exists() || target.delete();
    }

    public boolean start() {
        stop = false;
        HttpClientBuilder builder = HttpClients.custom().setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
        final HttpClient client = builder.build();

        HttpHead head = new HttpHead(url);
        HttpResponse headInfo = null;
        try {
            headInfo = client.execute(head);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        for (Header header : headInfo.getHeaders("Content-Length")) {
            length = Long.parseLong(header.getValue());
        }
        for (Header header : headInfo.getHeaders("ETag")) {
            signature = header.getValue();
        }

        if (length == 0) {
            System.out.println("0 length file.");
            return false;
        }

        File target = new File(filePath);

        if (target.exists()) {
            existLength = target.length();
        }
        if (existLength == length) {
            return false;
        }
        Thread down = new Thread(() -> {
            HttpGet get = new HttpGet(url);
            get.setHeader("Range", "bytes=" + existLength + "-");
            try {
                HttpResponse source = client.execute(get);
                RandomAccessFile raf = new RandomAccessFile(filePath, "rw");
                raf.seek(existLength);
                InputStream is = source.getEntity().getContent();
                byte[] bs = new byte[1024];
                int len = 0;
                while ((len = is.read(bs)) != -1 && !stop) {
                    raf.write(bs, 0, len);
                    currentLength += len;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        down.start();
        return true;
    }
}