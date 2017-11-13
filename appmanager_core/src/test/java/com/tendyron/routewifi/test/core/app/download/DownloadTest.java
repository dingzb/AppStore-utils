package com.tendyron.routewifi.test.core.app.download;

import com.tendyron.routewifi.core.app.download.Downloader;

/**
 * Created by Neo on 2017/1/4.
 */
public class DownloadTest {

    //    @Test
    public static void main(String[] args) {
        Downloader downloader = new Downloader("http://iosapps.itunes.apple.com/apple-assets-us-std-000001/Purple122/v4/a5/aa/27/a5aa27ab-b13a-d092-cf61-7494e281a21e/pre-thinned5183027807652751947.lc.5022582760204164.QPHNWNBWJUHAC.signed.dpkg.ipa?accessKey=1484421077_53120645567584976_Dts5loib49UeI9G0Uisp1mzIM2ZU1ngp6Tau3gVX2%2BNXKhjMa7Rd84cwWAx0szVl42cglsRkNPwKw4wsphJRZic7XRsYdQY5SwJnYQ69TzPcsdlCmLJGmtzgBEjlAQPEqjRNeWNJnmRD6d4WAe7nVrWECDF4tNVkoW6C5oD31hwkeI90VDzQRRyDOvZxgxWWp51J4o%2F05gjmfB6d2BqyPla55K4hq4dKkwEV9MM0GP%2BNIeTInICo8A9m4RiWo4Tdy3JAMCRlMHBsDFlDEOEmtw%3D%3D", "d:\\bba.ipa");
        if (downloader.start()) {
            long total = downloader.getLength();
            long exist = downloader.getExistLength();
            long current = 0;

            System.out.println(downloader.getSignature());
            System.out.println(current + exist + "(" + current + "/" + exist + ")/" + total);

            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            downloader.pause();
            System.out.println("pause");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            downloader.stop();
            System.out.println("stop");

//            do {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                current = downloader.getCurrentLength();
//                System.out.println(current + exist + "(" + current + "/" + exist + ")/" + total);
//            } while (total > (exist + current));
        }
    }
}
