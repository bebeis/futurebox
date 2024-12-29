package com.stdev.futurebox.util;

public class UuidGenerator {
    public static String generateUuid() {
        return java.util.UUID.randomUUID().toString();
    }
}
