package com.tendyron.routewifi.core.app.url;

import jpcap.PacketReceiver;
import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Neo on 2017/1/6.
 */
public class Receiver implements PacketReceiver {
    public void receivePacket(Packet packet) {
        if (packet instanceof TCPPacket) {
            TCPPacket tcpPacket = (TCPPacket) packet;

            HttpRequestEntity request = new HttpRequestEntity();

            String requestLine = new String(tcpPacket.data);

            Pattern isHttpP = Pattern.compile("(GET|HEAD|PUT|DELETE|PSOT|OPTIONS)\\s.+\\sHTTP.+");
            Matcher isHttpM = isHttpP.matcher(requestLine);

            if (isHttpM.find()) {
//                        request = new HttpRequestEntity();
                String mup = isHttpM.group();
                String[] field = mup.split(" ");
                request.setMethod(field[0]);
                request.setUri(field[1]);
                request.setProtocol(field[2]);

                System.out.println(mup);

                Pattern hostP = Pattern.compile("Host:\\s.+");
                Matcher hostM = hostP.matcher(requestLine);
                if (hostM.find()) {
                    String host = hostM.group();
                    request.setHost(host.substring("Host: ".length(), host.length()));
                    System.out.println(host);
                }

                Pattern userAgentP = Pattern.compile("User-Agent:\\s.+");
                Matcher userAgentM = userAgentP.matcher(requestLine);
                if (userAgentM.find()) {
                    String userAgent = userAgentM.group();
                    request.setUserAgent(userAgent.substring("User-Agent: ".length(), userAgent.length()));
                }
            }
            if (request.getHost() != null) {
                System.out.println(request);
            }
        }
    }
}
