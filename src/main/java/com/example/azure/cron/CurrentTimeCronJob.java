package com.example.azure.cron;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrentTimeCronJob implements ICronExecutable {

    private static final Logger LOG = LoggerFactory.getLogger(CurrentTimeCronJob.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Override
    public void execute() {
        LOG.info("The time is now " + dateFormat.format(new Date()));
    }
}
