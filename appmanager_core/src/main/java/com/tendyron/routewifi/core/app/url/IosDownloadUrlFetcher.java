package com.tendyron.routewifi.core.app.url;

import com.tendyron.routewifi.core.app.SubVersion;
import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.NetworkInterfaceAddress;
import jpcap.PacketReceiver;
import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Neo on 2017/1/4.
 * <p>
 * IOS系统应用下载地址获取器
 */
@Deprecated
public class IosDownloadUrlFetcher {

    private static Integer id;
    private NetworkInterface dev = null;
    private volatile JpcapCaptor captor = null;
    private static IosDownloadUrlFetcher fetcher = null;

    private volatile static boolean fetching;

    private IosDownloadUrlFetcher(int devId) {
        id = devId;
        dev = JpcapCaptor.getDeviceList()[devId];
    }

    public static synchronized IosDownloadUrlFetcher getInstance(int devId) {
        if (fetching && id != null && id != devId) {
            throw new IllegalStateException("device: " + deviceList().get(id) + " is in used.");
        }
        if (fetcher == null) {
            fetcher = new IosDownloadUrlFetcher(devId);
        }
        return fetcher;
    }

    public void fetch(Consumer<SubVersion> consumer) {
        if (fetching) {
            throw new IllegalStateException("device is fetching.");
        }
        fetching = true;
        try {
            captor = JpcapCaptor.openDevice(dev, 2000, false, 20);
            captor.setFilter("tcp", false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        captor.loopPacket(-1, new PacketReceiver() {
            @Override
            public void receivePacket(Packet packet) {
                if (packet instanceof TCPPacket) {
                    TCPPacket tcpPacket = (TCPPacket) packet;
                    SubVersion version = parser(tcpPacket.data);
                    if (version != null) {
                        consumer.accept(version);
                    }
                }
            }
        });
    }

    public void stopFetch() {
        if (fetching) {
            captor.close();
            fetching = false;
        } else {
            throw new IllegalStateException("not in fetch.");
        }
    }

    /**
     * 获取设备列表
     *
     * @return 设备列表
     */
    public static List<Map<String, Object>> deviceList() {
        NetworkInterface[] nis = JpcapCaptor.getDeviceList();
        List<Map<String, Object>> devices = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < nis.length; i++) {
            Map<String, Object> device = new HashMap<>();
            device.put("id", i);
            device.put("name", nis[i].name);
            device.put("description", nis[i].description);
            device.put("datalink_name", nis[i].datalink_name);
            device.put("datalink_description", nis[i].datalink_description);

            StringBuilder macSb = new StringBuilder();

            for (byte b : nis[i].mac_address) {
                macSb.append(String.format("%02x", b & 0xff).toUpperCase()).append(":");
            }

            device.put("mac_address", macSb.substring(0, macSb.length() - 1));
            List<Map<String, Object>> addresss = new ArrayList<>();
            for (NetworkInterfaceAddress addr : nis[i].addresses) {
                Map<String, Object> address = new HashMap<>();
                address.put("address", addr.address);
                address.put("subnet", addr.subnet);
                address.put("broadcast", addr.broadcast);
                address.put("destination", addr.destination);
                addresss.add(address);
            }
            device.put("addresss", addresss);
            devices.add(device);
        }
        return devices;
    }

    private SubVersion parser(byte[] data) {

        SubVersion version = null;
        String line = new String(data);

        Pattern isHttpP = Pattern.compile("(?:GET|HEAD|PUT|DELETE|PSOT|OPTIONS)\\s(?<url>/[^\\s]+(?:pre-thinned.+\\.signed\\.dpkg\\.ipa\\?)[^\\s]+)\\s(?:(?<protocol>HTTP(?:S?))/(?:[^\\s]+))(?:\\r?\\n)(?:Host:\\s)(?<host>[^\\s]+)(?:(?:\\r?\\n).*)*(?:User-Agent:\\s).*(?:iOS)/(?<ios>[^\\s]+).*(?:model)/(?<model>[^\\s]+)");
        Matcher isHttpM = isHttpP.matcher(line);

        if (isHttpM.find()) {
            version = new SubVersion();
            version.setDownloadUrl(isHttpM.group("protocol").toLowerCase() + "://" + isHttpM.group("host") + isHttpM.group("url"));
            version.setIosVersion(isHttpM.group("ios"));
            version.setModel(isHttpM.group("model"));
            version.setCaptureTime(new Date());
            version.setSignature(DigestUtils.md5Hex(version.toString()));
        }
        return version;
    }

    public boolean isFetching() {
        return fetching;
    }
}




