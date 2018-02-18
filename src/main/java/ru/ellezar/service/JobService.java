package ru.ellezar.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

public abstract class JobService {

    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public abstract void execute(LocalDateTime execOnTime, Callable call);

}
