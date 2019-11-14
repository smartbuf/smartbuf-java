package com.github.sisyphsu.smartbuf.converter.codec;

import com.github.sisyphsu.smartbuf.converter.CodecFactory;
import com.github.sisyphsu.smartbuf.reflect.XTypeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.instrument.IllegalClassFormatException;
import java.net.BindException;
import java.net.ConnectException;
import java.net.ProtocolException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * @author sulin
 * @since 2019-08-04 18:59:00
 */
@SuppressWarnings("ALL")
public class ThrowableCodecTest {

    private ThrowableCodec codec = new ThrowableCodec();

    @BeforeEach
    void setUp() {
        codec.setFactory(CodecFactory.Instance);
    }

    @Test
    public void test() {
        IllegalStateException exception = new IllegalStateException("test");

        Map map = codec.toMap(exception);
        Throwable throwable1 = codec.toThrowable(map, XTypeUtils.toXType(IllegalStateException.class));
        assert exception.getClass() == throwable1.getClass();
        assert exception.getCause() == throwable1.getCause();
        assert Objects.equals(exception.getMessage(), throwable1.getMessage());
        assert Arrays.equals(exception.getStackTrace(), throwable1.getStackTrace());

        Throwable throwable2 = codec.toThrowable(map, XTypeUtils.toXType(Throwable.class));
        assert exception.getClass() == throwable2.getClass();
        assert exception.getCause() == throwable2.getCause();
        assert Objects.equals(exception.getMessage(), throwable2.getMessage());
        assert Arrays.equals(exception.getStackTrace(), throwable2.getStackTrace());

        assert codec.toThrowable(map, XTypeUtils.toXType(RuntimeException.class)).getClass() == RuntimeException.class;

        map.put("type", "12314124123");
        Throwable throwable3 = codec.toThrowable(map, XTypeUtils.toXType(Throwable.class));
        assert throwable3.getClass() == Throwable.class;

        map.put("type", Object.class.getName());
        Throwable throwable4 = codec.toThrowable(map, XTypeUtils.toXType(Throwable.class));
        assert throwable4.getClass() == Throwable.class;

        map.remove(ThrowableCodec.E_TYPE);
        map.remove(ThrowableCodec.E_MSG);
        map.remove(ThrowableCodec.E_STACKS);
        codec.toThrowable(map, XTypeUtils.toXType(Throwable.class));
    }

    @Test
    public void testStackTraceElement() {
        Exception exception = new Exception();

        StackTraceElement element = exception.getStackTrace()[0];
        StackTraceElement newElement = codec.toStackTraceElement(codec.toMap(element));
        System.out.println(element);
        System.out.println(newElement);
        assert element.equals(newElement);
        Map elementMap = codec.toMap(element);
        elementMap.remove("line");
        assert codec.toStackTraceElement(elementMap).getLineNumber() == -1;
    }

    @Test
    public void testCreateThrowable() {
        NullPointerException npe = new NullPointerException("null");

        assert ThrowableCodec.createThrowable(AbstractMethodError.class, "", npe) instanceof AbstractMethodError;
        assert ThrowableCodec.createThrowable(ArithmeticException.class, "", npe) instanceof ArithmeticException;
        assert ThrowableCodec.createThrowable(IndexOutOfBoundsException.class, "", npe) instanceof IndexOutOfBoundsException;
        assert ThrowableCodec.createThrowable(NullPointerException.class, "", npe) instanceof NullPointerException;
        assert ThrowableCodec.createThrowable(RuntimeException.class, "", npe) instanceof RuntimeException;
        assert ThrowableCodec.createThrowable(AssertionError.class, "", npe) instanceof AssertionError;
        assert ThrowableCodec.createThrowable(ClassCastException.class, "", npe) instanceof ClassCastException;
        assert ThrowableCodec.createThrowable(ClassCircularityError.class, "", npe) instanceof ClassCircularityError;
        assert ThrowableCodec.createThrowable(ClassNotFoundException.class, "", npe) instanceof ClassNotFoundException;
        assert ThrowableCodec.createThrowable(CloneNotSupportedException.class, "", npe) instanceof CloneNotSupportedException;
        assert ThrowableCodec.createThrowable(Exception.class, "", npe) instanceof Exception;
        assert ThrowableCodec.createThrowable(ExceptionInInitializerError.class, "", npe) instanceof ExceptionInInitializerError;
        assert ThrowableCodec.createThrowable(IllegalAccessError.class, "", npe) instanceof IllegalAccessError;
        assert ThrowableCodec.createThrowable(ReflectiveOperationException.class, "", npe) instanceof ReflectiveOperationException;
        assert ThrowableCodec.createThrowable(LinkageError.class, "", npe) instanceof LinkageError;
        assert ThrowableCodec.createThrowable(IllegalMonitorStateException.class, "", npe) instanceof IllegalMonitorStateException;
        assert ThrowableCodec.createThrowable(IllegalArgumentException.class, "", npe) instanceof IllegalArgumentException;
        assert ThrowableCodec.createThrowable(IllegalStateException.class, "", npe) instanceof IllegalStateException;
        assert ThrowableCodec.createThrowable(IllegalThreadStateException.class, "", npe) instanceof IllegalThreadStateException;
        assert ThrowableCodec.createThrowable(IncompatibleClassChangeError.class, "", npe) instanceof IncompatibleClassChangeError;
        assert ThrowableCodec.createThrowable(InstantiationError.class, "", npe) instanceof InstantiationError;
        assert ThrowableCodec.createThrowable(InterruptedException.class, "", npe) instanceof InterruptedException;
        assert ThrowableCodec.createThrowable(NegativeArraySizeException.class, "", npe) instanceof NegativeArraySizeException;
        assert ThrowableCodec.createThrowable(NoClassDefFoundError.class, "", npe) instanceof NoClassDefFoundError;
        assert ThrowableCodec.createThrowable(NoSuchFieldError.class, "", npe) instanceof NoSuchFieldError;
        assert ThrowableCodec.createThrowable(NoSuchFieldException.class, "", npe) instanceof NoSuchFieldException;
        assert ThrowableCodec.createThrowable(NoSuchMethodError.class, "", npe) instanceof NoSuchMethodError;
        assert ThrowableCodec.createThrowable(NoSuchMethodException.class, "", npe) instanceof NoSuchMethodException;
        assert ThrowableCodec.createThrowable(NumberFormatException.class, "", npe) instanceof NumberFormatException;
        assert ThrowableCodec.createThrowable(OutOfMemoryError.class, "", npe) instanceof OutOfMemoryError;
        assert ThrowableCodec.createThrowable(SecurityException.class, "", npe) instanceof SecurityException;
        assert ThrowableCodec.createThrowable(StackOverflowError.class, "", npe) instanceof StackOverflowError;
        assert ThrowableCodec.createThrowable(ThreadDeath.class, "", npe) instanceof ThreadDeath;
        assert ThrowableCodec.createThrowable(TypeNotPresentException.class, "", npe) instanceof TypeNotPresentException;
        assert ThrowableCodec.createThrowable(UnknownError.class, "", npe) instanceof UnknownError;
        assert ThrowableCodec.createThrowable(UnsatisfiedLinkError.class, "", npe) instanceof UnsatisfiedLinkError;
        assert ThrowableCodec.createThrowable(UnsupportedClassVersionError.class, "", npe) instanceof UnsupportedClassVersionError;
        assert ThrowableCodec.createThrowable(UnsupportedOperationException.class, "", npe) instanceof UnsupportedOperationException;
        assert ThrowableCodec.createThrowable(VerifyError.class, "", npe) instanceof VerifyError;
        assert ThrowableCodec.createThrowable(IllegalClassFormatException.class, "", npe) instanceof IllegalClassFormatException;
        assert ThrowableCodec.createThrowable(BindException.class, "", npe) instanceof BindException;
        assert ThrowableCodec.createThrowable(ConnectException.class, "", npe) instanceof ConnectException;
        assert ThrowableCodec.createThrowable(ProtocolException.class, "", npe) instanceof ProtocolException;
        assert ThrowableCodec.createThrowable(UnknownHostException.class, "", npe) instanceof UnknownHostException;

        assert ThrowableCodec.createThrowable(CauseException.class, null, null) instanceof CauseException;
        assert ThrowableCodec.createThrowable(EmptyException.class, null, null) instanceof EmptyException;

        try {
            ThrowableCodec.createThrowable(Object.class, null, null);
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }

        try {
            ThrowableCodec.createThrowable(InvalidException.class, null, null);
        } catch (Exception e) {
            assert e instanceof UnsupportedOperationException;
        }
    }

    public static class CauseException extends Exception {
        public CauseException(Throwable cause) {
            super(cause);
        }
    }

    public static class EmptyException extends Exception {
        public EmptyException() {
        }
    }

    public static class InvalidException extends Exception {
        private final long time;

        public InvalidException(String message, Throwable cause, long time) {
            super(message, cause);
            this.time = time;
        }
    }

}
