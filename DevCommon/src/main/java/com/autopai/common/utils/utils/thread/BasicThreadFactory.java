package com.autopai.common.utils.utils.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public class BasicThreadFactory implements ThreadFactory {
    /**
     * A counter for the threads created by this factory.
     */
    private final AtomicLong threadCounter;

    /**
     * Stores the wrapped factory.
     */
    private final ThreadFactory wrappedFactory;

    /**
     * Stores the uncaught exception handler.
     */
    private final Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    /**
     * Stores the naming pattern for newly created threads.
     */
    private final String namingPattern;

    /**
     * Stores the priority.
     */
    private final Integer priority;

    /**
     * Stores the daemon status flag.
     */
    private final Boolean daemonFlag;

    private BasicThreadFactory(final Builder builder) {
        if (builder.wrappedFactory == null) {
            wrappedFactory = Executors.defaultThreadFactory();
        } else {
            wrappedFactory = builder.wrappedFactory;
        }

        namingPattern = builder.namingPattern;
        priority = builder.priority;
        daemonFlag = builder.daemonFlag;
        uncaughtExceptionHandler = builder.exceptionHandler;

        threadCounter = new AtomicLong();
    }


    public final ThreadFactory getWrappedFactory() {
        return wrappedFactory;
    }

    public final String getNamingPattern() {
        return namingPattern;
    }


    public final Boolean getDaemonFlag() {
        return daemonFlag;
    }


    public final Integer getPriority() {
        return priority;
    }


    public final Thread.UncaughtExceptionHandler getUncaughtExceptionHandler() {
        return uncaughtExceptionHandler;
    }


    public long getThreadCount() {
        return threadCounter.get();
    }


    @Override
    public Thread newThread(final Runnable r) {
        final Thread t = getWrappedFactory().newThread(r);
        initializeThread(t);

        return t;
    }

    /**
     * Initializes the specified thread. This method is called by
     * {@link #newThread(Runnable)} after a new thread has been obtained from
     * the wrapped thread factory. It initializes the thread according to the
     * options set for this factory.
     *
     * @param t the thread to be initialized
     */
    private void initializeThread(final Thread t) {

        if (getNamingPattern() != null) {
            final Long count = Long.valueOf(threadCounter.incrementAndGet());
            t.setName(String.format(getNamingPattern(), count));
        }

        if (getUncaughtExceptionHandler() != null) {
            t.setUncaughtExceptionHandler(getUncaughtExceptionHandler());
        }

        if (getPriority() != null) {
            t.setPriority(getPriority().intValue());
        }

        if (getDaemonFlag() != null) {
            t.setDaemon(getDaemonFlag().booleanValue());
        }
    }

    public static class Builder implements com.autopai.common.utils.utils.thread.Builder<BasicThreadFactory> {

        /**
         * The wrapped factory.
         */
        private ThreadFactory wrappedFactory;

        /**
         * The uncaught exception handler.
         */
        private Thread.UncaughtExceptionHandler exceptionHandler;

        /**
         * The naming pattern.
         */
        private String namingPattern;

        /**
         * The priority.
         */
        private Integer priority;

        /**
         * The daemon flag.
         */
        private Boolean daemonFlag;


        public Builder wrappedFactory(final ThreadFactory factory) {
            notNull(factory, "Wrapped ThreadFactory must not be null!");

            wrappedFactory = factory;
            return this;
        }


        public Builder namingPattern(final String pattern) {
            notNull(pattern, "Naming pattern must not be null!");

            namingPattern = pattern;
            return this;
        }


        public Builder daemon(final boolean f) {
            daemonFlag = Boolean.valueOf(f);
            return this;
        }


        public Builder priority(final int prio) {
            priority = Integer.valueOf(prio);
            return this;
        }


        public Builder uncaughtExceptionHandler(
                final Thread.UncaughtExceptionHandler handler) {
            notNull(handler, "Uncaught exception handler must not be null!");

            exceptionHandler = handler;
            return this;
        }


        public void reset() {
            wrappedFactory = null;
            exceptionHandler = null;
            namingPattern = null;
            priority = null;
            daemonFlag = null;
        }


        @Override
        public BasicThreadFactory build() {
            final BasicThreadFactory factory = new BasicThreadFactory(this);
            reset();
            return factory;
        }
    }

    public static <T> T notNull(final T object, final String message, final Object... values) {
        if (object == null) {
            throw new NullPointerException(String.format(message, values));
        }
        return object;
    }

}
