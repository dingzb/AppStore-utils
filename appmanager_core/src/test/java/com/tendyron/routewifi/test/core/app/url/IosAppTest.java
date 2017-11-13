package com.tendyron.routewifi.test.core.app.url;

import com.tendyron.routewifi.core.app.App;
import com.tendyron.routewifi.core.app.url.IosDownloadUrlFetcher;
import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import org.junit.Test;

/**
 * Created by Neo on 2017/1/4.
 * for Ios app test
 */
public class IosAppTest {

    @Test
    public void getUrlTest() {

        NetworkInterface[] nis = JpcapCaptor.getDeviceList();

        IosDownloadUrlFetcher fetcher = IosDownloadUrlFetcher.getInstance(2);

        App app = new App();
        app.setName("ryl");

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(200000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                fetcher.stopFetch();
            }
        }).start();


            fetcher.fetch(version -> {
                System.out.println(version.getDownloadUrl());
            });

    }
}
