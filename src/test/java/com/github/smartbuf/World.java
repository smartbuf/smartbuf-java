package com.github.smartbuf;

import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialClob;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.*;
import java.nio.charset.Charset;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.time.*;
import java.util.List;
import java.util.Queue;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.regex.Pattern;

/**
 * @author sulin
 * @since 2019-10-26 19:38:11
 */
@Data
public class World {

    private Array  array  = new Array();
    private Awt    awt    = new Awt();
    private Atomic atomic = new Atomic();
    private Buffer buffer = new Buffer();
    private Coll   coll   = new Coll();
    private IO     io     = new IO();
    private Net    net    = new Net();
    private Time   time   = new Time();
    private Util   util   = new Util();
    private Sql    sql    = new Sql();

    private Map<String, Double> map = new HashMap<>();

    @Data
    public static class Array {
        private boolean[] booleans   = new boolean[]{false, true, false, true, true, true, true, false, false, true};
        private Boolean[] booleanArr = new Boolean[]{false, true, false, true, true, true, true, false, false, true};
        private byte[]    bytes      = RandomUtils.nextBytes(1024);
        private Byte[]    byteArr    = new Byte[]{1, 2, 3, 4, 5, 6, -1, -2, 0, Byte.MIN_VALUE, Byte.MAX_VALUE};
        private short[]   shorts     = new short[]{0, 10, Short.MIN_VALUE, Short.MAX_VALUE};
        private Short[]   shortArr   = new Short[]{0, 10, Short.MIN_VALUE, Short.MAX_VALUE};
        private int[]     ints       = new int[]{0, 100, Integer.MIN_VALUE, Integer.MAX_VALUE};
        private Integer[] intArr     = new Integer[]{0, 100, Integer.MIN_VALUE, Integer.MAX_VALUE};
        private long[]    longs      = new long[]{0, 10000, -10000, Long.MIN_VALUE, Long.MAX_VALUE};
        private Long[]    longArr    = new Long[]{0L, 10000L, -10000L, Long.MIN_VALUE, Long.MAX_VALUE};
        private float[]   floats     = new float[]{0.0f, Float.MIN_VALUE, Float.MAX_VALUE};
        private Float[]   floatArr   = new Float[]{0.0f, Float.MIN_VALUE, Float.MAX_VALUE};
        private double[]  doubles    = new double[]{0.0, Double.MIN_VALUE, Double.MAX_VALUE};
        private Double[]  doubleArr  = new Double[]{0.0, Double.MIN_VALUE, Double.MAX_VALUE};
        private Object[]  objectArr  = new Object[]{1, 1.0f, 1.0, 1L};
    }

    @Data
    public static class Atomic {
        private AtomicBoolean           ab          = new AtomicBoolean(false);
        private AtomicInteger           ai          = new AtomicInteger(RandomUtils.nextInt());
        private AtomicLong              al          = new AtomicLong(RandomUtils.nextLong());
        private AtomicIntegerArray      intArr      = new AtomicIntegerArray(new int[]{0, Integer.MIN_VALUE, Integer.MAX_VALUE});
        private AtomicLongArray         longArr     = new AtomicLongArray(new long[]{0L, Long.MIN_VALUE, Long.MAX_VALUE});
        private AtomicReference<String> strRef      = new AtomicReference<>(RandomStringUtils.randomAlphanumeric(64));
        private DoubleAdder             doubleAdder = new DoubleAdder();
        private LongAdder               longAdder   = new LongAdder();

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Atomic atomic = (Atomic) o;
            return Objects.equals(ab.get(), atomic.ab.get()) &&
                Objects.equals(ai.get(), atomic.ai.get()) &&
                Objects.equals(al.get(), atomic.al.get()) &&
                Objects.equals(intArr.length(), atomic.intArr.length()) &&
                Objects.equals(longArr.length(), atomic.longArr.length()) &&
                Objects.equals(strRef.get(), atomic.strRef.get()) &&
                Objects.equals(doubleAdder.doubleValue(), atomic.doubleAdder.doubleValue()) &&
                Objects.equals(longAdder.longValue(), atomic.longAdder.longValue());
        }
    }

    @Data
    public static class Awt {
        private Font      font      = Font.getFont(Font.MONOSPACED);
        private Color     color     = Color.BLUE;
        private Point     point     = new Point(1280, 720);
        private Dimension dimension = new Dimension(1920, 1080);
        private Rectangle rectangle = new Rectangle(0, 0, 720, 480);
    }

    @Data
    public static class Buffer {
        private ByteBuffer   bb = ByteBuffer.wrap(new byte[]{0, 1, -1, Byte.MIN_VALUE, Byte.MAX_VALUE});
        private ShortBuffer  sb = ShortBuffer.wrap(new short[]{0, 1, -1, Short.MIN_VALUE, Short.MAX_VALUE});
        private IntBuffer    ib = IntBuffer.wrap(new int[]{0, 1, -1, Integer.MIN_VALUE, Integer.MAX_VALUE});
        private LongBuffer   lb = LongBuffer.wrap(new long[]{0L, 1L, -1L, Long.MIN_VALUE, Long.MAX_VALUE});
        private FloatBuffer  fb = FloatBuffer.wrap(new float[]{0.0f, Float.MIN_VALUE, Float.MAX_VALUE});
        private DoubleBuffer db = DoubleBuffer.wrap(new double[]{0.0, Double.MIN_VALUE, Double.MAX_VALUE});
        private CharBuffer   cb = CharBuffer.wrap(Arrays.copyOf("hello world".toCharArray(), 11));

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Buffer buffer = (Buffer) o;
            return Arrays.equals(bb.array(), buffer.bb.array()) &&
                Arrays.equals(sb.array(), buffer.sb.array()) &&
                Arrays.equals(ib.array(), buffer.ib.array()) &&
                Arrays.equals(lb.array(), buffer.lb.array()) &&
                Arrays.equals(fb.array(), buffer.fb.array()) &&
                Arrays.equals(db.array(), buffer.db.array()) &&
                Arrays.equals(cb.array(), buffer.cb.array());
        }
    }

    @Data
    public static class Coll {
        private Set<String>    set    = Collections.singleton("hello world");
        private List<String>   list   = Arrays.asList("", "10000", "hello", "world");
        private Queue<String>  queue  = new LinkedList<>(Arrays.asList("hello world", null));
        private Vector<String> vector = new Vector<>(Arrays.asList("hello world", "", null));
        private Stack<String>  stack  = new Stack<>();

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coll coll = (Coll) o;
            return Arrays.equals(set.toArray(), coll.set.toArray()) &&
                Arrays.equals(list.toArray(), coll.list.toArray()) &&
                Arrays.equals(queue.toArray(), coll.queue.toArray()) &&
                Arrays.equals(vector.toArray(), coll.vector.toArray()) &&
                Arrays.equals(stack.toArray(), coll.stack.toArray());
        }
    }

    @Data
    public static class IO {
        private Charset     charset  = Charset.defaultCharset();
        private InputStream is       = new ByteArrayInputStream(new byte[]{0, 1, -1, Byte.MIN_VALUE, Byte.MAX_VALUE});
        private Readable    readable = CharBuffer.wrap("hello world");

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IO io = (IO) o;
            return Objects.equals(charset, io.charset);
        }
    }

    @Data
    public static class Lang {
        private Class            cls    = Date.class;
        private Thread.State     state  = Thread.State.BLOCKED;
        private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        private BigDecimal bd = BigDecimal.ONE;
        private BigInteger bi = BigInteger.TEN;

        private boolean   bool  = true;
        private byte      b     = Byte.MIN_VALUE;
        private short     s     = Short.MIN_VALUE;
        private int       i     = Integer.MIN_VALUE;
        private long      l     = Long.MIN_VALUE;
        private float     f     = Float.MIN_VALUE;
        private double    d     = Double.MIN_VALUE;
        private char      c     = Character.MIN_VALUE;
        private Character c2    = 'x';
        private Boolean   bool2 = null;
        private Byte      b2    = Byte.MIN_VALUE;
        private Short     s2    = Short.MIN_VALUE;
        private Integer   i2    = Integer.MIN_VALUE;
        private Long      l2    = Long.MIN_VALUE;
        private Float     f2    = Float.MIN_VALUE;
        private Double    d2    = Double.MIN_VALUE;
    }

    @Data
    public static class Str {
        private String                  str = "hello world";
        private StringBuffer            sb1 = new StringBuffer("hello1");
        private StringBuilder           sb2 = new StringBuilder("hello2");
        private StringCharacterIterator sci = new StringCharacterIterator("hello world");
    }

    @Data
    public static class Net {
        private URL               url;
        private URI               uri;
        private InetAddress       addr;
        private InetSocketAddress socketAddr;

        public Net() {
            try {
                url = new URL("https://github.com/");
                uri = new URI("/test");
                addr = InetAddress.getByName("8.8.8.8");
                socketAddr = InetSocketAddress.createUnresolved("127.0.0.1", 80);
            } catch (Exception e) {
                throw new RuntimeException("net error: ", e);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Net net = (Net) o;
            return Objects.equals(url, net.url) &&
                Objects.equals(uri, net.uri) &&
                Objects.equals(addr, net.addr) &&
                Objects.equals(socketAddr.getHostString(), net.socketAddr.getHostString()) &&
                Objects.equals(socketAddr.getPort(), net.socketAddr.getPort());
        }
    }

    @Data
    public static class Time {
        private Date           date     = new Date();
        private Calendar       calendar = Calendar.getInstance();
        private ZoneId         zoneId   = ZoneId.systemDefault();
        private Instant        instant  = Instant.now();
        private LocalDateTime  ldt      = LocalDateTime.now();
        private Duration       duration = Duration.ofMillis(System.currentTimeMillis());
        private Period         period   = Period.of(1, 2, 3);
        private LocalTime      lt       = LocalTime.of(1, 2, 3);
        private OffsetTime     ot       = OffsetTime.of(1, 2, 3, 4, ZoneOffset.UTC);
        private OffsetDateTime odt      = OffsetDateTime.now();
        private LocalDate      ld       = LocalDate.now();

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Time time = (Time) o;
            return Objects.equals(date, time.date) &&
                Objects.equals(calendar.getTime(), time.calendar.getTime()) &&
                Objects.equals(zoneId.getId(), time.zoneId.getId()) &&
                Objects.equals(instant.toEpochMilli(), time.instant.toEpochMilli()) &&
                Objects.equals(ldt, time.ldt) &&
                Objects.equals(duration, time.duration) &&
                Objects.equals(period, time.period) &&
                Objects.equals(lt, time.lt) &&
                Objects.equals(ot, time.ot) &&
                Objects.equals(odt, time.odt) &&
                Objects.equals(ld, time.ld);
        }
    }

    @Data
    public static class Util {
        private BitSet   bitSet   = BitSet.valueOf(new byte[]{0, Byte.MIN_VALUE, Byte.MAX_VALUE});
        private Currency currency = Currency.getInstance(Locale.CHINA);
        private Locale   locale   = Locale.CHINA;
        private UUID     uuid     = UUID.randomUUID();
        private TimeZone zone     = TimeZone.getDefault();
        private Pattern  pattern  = Pattern.compile("\\d+");

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Util util = (Util) o;
            return Objects.equals(bitSet, util.bitSet) &&
                Objects.equals(currency, util.currency) &&
                Objects.equals(locale, util.locale) &&
                Objects.equals(uuid, util.uuid) &&
                Objects.equals(zone, util.zone) &&
                Objects.equals(pattern.pattern(), util.pattern.pattern());
        }
    }

    @Data
    public static class Sql {
        private Blob          blob;
        private Clob          clob;
        private java.sql.Date date;
        private java.sql.Time time;
        private Timestamp     timestamp;

        public Sql() {
            try {
                blob = new SerialBlob("hello world".getBytes());
                clob = new SerialClob("hello world".toCharArray());
                date = new java.sql.Date(System.currentTimeMillis());
                time = new java.sql.Time(24 * 3600000);
                timestamp = new Timestamp(System.currentTimeMillis());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Sql sql = (Sql) o;
            return Objects.equals(date, sql.date) &&
                Objects.equals(time, sql.time) &&
                Objects.equals(timestamp, sql.timestamp);
        }

    }

}
