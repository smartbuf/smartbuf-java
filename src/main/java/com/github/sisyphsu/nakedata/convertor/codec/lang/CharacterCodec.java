package com.github.sisyphsu.nakedata.convertor.codec.lang;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.codec.Codec;

/**
 * Character's codec
 *
 * @author sulin
 * @since 2019-05-13 18:12:57
 */
public class CharacterCodec extends Codec {

    /**
     * Convert String to Character
     *
     * @param s String
     * @return Character
     */
    @Converter
    public Character toCharacter(String s) {
        if (s == null)
            return null;
        if (s.length() != 1)
            throw new RuntimeException("Convert String to Character failed: " + s);
        return s.charAt(0);
    }

    /**
     * Convert Character to String
     *
     * @param c Character
     * @return String
     */
    @Converter
    public String toString(Character c) {
        return c == null ? null : c.toString();
    }

    /**
     * Convert Integer to Character
     *
     * @param i Integer
     * @return Character
     */
    @Converter
    public Character toCharacter(Integer i) {
        return i == null ? null : (char) i.intValue();
    }

    /**
     * Convert Character to Integer
     *
     * @param c Character
     * @return Integer
     */
    @Converter
    public Integer toInteger(Character c) {
        return c == null ? null : (int) c;
    }

    /**
     * Convert char[] to Character[]
     *
     * @param arr char[]
     * @return Character[]
     */
    @Converter
    public Character[] convert(char[] arr) {
        if (arr == null) {
            return null;
        }
        Character[] result = new Character[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i];
        }
        return result;
    }

    /**
     * Convert Character[] to char[]
     *
     * @param arr Character[]
     * @return char[]
     */
    @Converter
    public char[] convert(Character[] arr) {
        if (arr == null) {
            return null;
        }
        char[] result = new char[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i];
        }
        return result;
    }

}
