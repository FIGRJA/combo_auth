package org.figrja.combo_auth.config.debuglogger;

import org.apache.logging.log4j.Logger;

public class DebugAll extends LoggerMain{

    private org.apache.logging.log4j.Logger LOGGER;
    public DebugAll(Logger logger) {
        this.LOGGER = logger;
    }
    @Override
    public void info(String mes) {
        LOGGER.info(mes);
    }

    @Override
    public void debug(String mes) {
        LOGGER.info("[debug]"+mes);
    }

    @Override
    public void debugRes(String mes) {
        LOGGER.info("[debug]"+mes);
    }
}
