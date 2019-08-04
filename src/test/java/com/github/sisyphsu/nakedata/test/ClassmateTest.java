package com.github.sisyphsu.nakedata.test;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * @author sulin
 * @since 2019-07-05 15:06:19
 */
public class ClassmateTest {

    private static final TypeResolver typeResolver = new TypeResolver();

    @Test
    public void testCollection() {
        Object obj = new ArrayList<Integer>();
        ResolvedType type = typeResolver.resolve(obj.getClass());
        System.out.println(type);
        for (ResolvedType parameter : type.getTypeParameters()) {
            System.out.println(parameter);
        }
        System.out.println(type.getErasedType());
    }

}
