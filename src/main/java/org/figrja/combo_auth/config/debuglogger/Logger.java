package org.figrja.combo_auth.config.debuglogger;

public class Logger extends LoggerMain{

    private org.apache.logging.log4j.Logger LOGGER;
    public Logger(org.apache.logging.log4j.Logger logger) {
        this.LOGGER = logger;
    }
    @Override
    public void info(String mes) {
        LOGGER.info(mes);
    }

    @Override
    public void debug(String mes) {
        return;
    }

    @Override
    public void debugRes(String mes) {
        return;
    }
}
