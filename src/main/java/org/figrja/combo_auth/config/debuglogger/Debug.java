package org.figrja.combo_auth.config.debuglogger;

import org.apache.logging.log4j.Logger;

public class Debug extends LoggerMain{
    private Logger LOGGER;
    public Debug(Logger logger) {
        this.LOGGER = logger;
    }

    @Override
    public void info(String mes) {
        LOGGER.info(mes);
    }

    @Override
    public void debug(String mes) {
        LOGGER.info("[combo_auth debug] "+mes);
    }

    @Override
    public void debugRes(String mes) {
        return;
    }
}
