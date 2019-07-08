package com.github.sisyphsu.nakedata.convertor.codec.lang;

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
    public Character toCharacter(String s) {
        if (s == null)
            return null;
        if (s.length() != 1)
            throw new RuntimeException("Convert String to Character failed: " + s);
        return s.charAt(0);
    }

    /**
     * Convert char[] to Character[]
     *
     * @param arr char[]
     * @return Character[]
     */
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
