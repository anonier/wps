package com.web.wps.enums;

public interface Enums {

    enum enums {

        ZERO(0),

        ONE(1),

        TWO(2);

        public final Integer value;

        enums(Integer value) {
            this.value = value;
        }
    }

    enum logs {

        CREATE("create"),

        MODIFY("modify");

        public final String value;

        logs(String value) {
            this.value = value;
        }
    }
}