package com.tendyron.routewifi.core.app.url;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.NetworkInterfaceAddress;
import jpcap.PacketReceiver;
import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;

import java.io.IOException;
import java.util.*;

/**
 * Created by Neo on 2017/1/4.
 * <p>
 * IOS系统应用下载地址获取器
 */
public class IosIpaUrlFetcher {

    private static Integer id;
    private NetworkInterface dev = null;
    private volatile JpcapCaptor captor = null;
    private static IosIpaUrlFetcher fetcher = null;

    private volatile static boolean fetching;

    private IosIpaUrlFetcher(int devId) {
        id = devId;
        dev = JpcapCaptor.getDeviceList()[devId];
    }

    public static synchronized IosIpaUrlFetcher getInstance(int devId) {
        if (fetching && id != null && id != devId) {
            throw new IllegalStateException("device: " + deviceList().get(id) + " is in used.");
        }
        if (fetcher == null) {
            fetcher = new IosIpaUrlFetcher(devId);
        }
        return fetcher;
    }

    public void fetch(FetchHandler handler) {
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
                    handler.handle(tcpPacket.data);
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

    public boolean isFetching() {
        return fetching;
    }
}




