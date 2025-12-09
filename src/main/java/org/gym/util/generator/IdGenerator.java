package org.gym.util.generator;

import java.util.concurrent.atomic.AtomicLong;

public final class IdGenerator {
    private static final AtomicLong GENERATOR = new AtomicLong(System.currentTimeMillis());

    private IdGenerator() {

    }

    public static Long generateId() {
        return GENERATOR.incrementAndGet();
    }

}


