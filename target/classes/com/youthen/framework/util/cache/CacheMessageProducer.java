package com.youthen.framework.util.cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component(value = "cacheMessageProducer")
@Scope(value = "singleton")
final class CacheMessageProducer {

    private static final Log LOG = LogFactory.getLog(CacheMessageProducer.class);

    private CacheMessageProducer() {
    }

    void start() {

    }

    void send(final String aMsg) {
    }
}
