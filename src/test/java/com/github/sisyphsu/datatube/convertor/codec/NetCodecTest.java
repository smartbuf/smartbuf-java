package com.github.sisyphsu.datatube.convertor.codec;

import com.github.sisyphsu.datatube.convertor.CodecFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * @author sulin
 * @since 2019-08-04 18:58:14
 */
public class NetCodecTest {

    private NetCodec codec = new NetCodec();

    @BeforeEach
    void setUp() {
        codec.setFactory(CodecFactory.Instance);
    }

    @Test
    public void test() throws Exception {
        InetSocketAddress address = new InetSocketAddress("www.bing.com", 80);
        assert address.equals(codec.toInetSocketAddress(codec.toString(address)));
        assert codec.toInetSocketAddress("www.bing.com").getPort() == 0;

        assert codec.toInetAddress("/127.0.0.1") != null;

        InetAddress inetAddress = InetAddress.getByName("www.bing.com");
        assert inetAddress.equals(codec.toInetAddress(codec.toString(inetAddress)));

        Inet4Address inet4Address = (Inet4Address) Inet4Address.getByName("www.bing.com");
        assert inet4Address.equals(codec.toInetAddress(codec.toString(inet4Address)));

        Inet6Address inet6Address = (Inet6Address) Inet6Address.getByName("www.neu6.edu.cn");
        assert inet6Address.equals(codec.toInetAddress(codec.toString(inet6Address)));

        // auto split hostname by /
        assert inetAddress.equals(codec.toInetAddress(inetAddress.toString()));

        String uri1 = "http://java.sun.com/j2se/1.3/";
        String uri2 = "../../../demo/jfc/SwingSet2/src/SwingSet2.java";
        String uri3 = "file:///~/calendar";
        String uri4 = "docs/guide/collections/designfaq.html#28";
        assert uri1.equals(codec.toString(codec.toURI(uri1)));
        assert uri2.equals(codec.toString(codec.toURI(uri2)));
        assert uri3.equals(codec.toString(codec.toURI(uri3)));
        assert uri4.equals(codec.toString(codec.toURI(uri4)));

        String url = "https://www.javatpoint.com/URL-class";
        assert url.equals(codec.toString(codec.toURL(url)));
    }

}
