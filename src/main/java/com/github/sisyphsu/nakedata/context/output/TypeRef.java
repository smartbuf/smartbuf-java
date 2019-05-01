package com.github.sisyphsu.nakedata.context.output;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sulin
 * @since 2019-05-01 15:17:22
 */
@Data
public class TypeRef {

    private List<Field> fields = new ArrayList<>();

    @Data
    public static class Field {

        private final String name;
        private final int type;

        public Field(String name, int type) {
            this.name = name;
            this.type = type;
        }

    }

}
