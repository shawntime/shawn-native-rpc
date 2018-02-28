package com.shawntime.rpc.provider.service;

import com.shawntime.rpc.core.serialize.Formatter;
import com.shawntime.rpc.core.serialize.Parse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by shma on 2018/2/12.
 */
public abstract class AbstractRpcProvider {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractRpcProvider.class);

    protected Formatter formatter;

    protected Parse parse;

    public AbstractRpcProvider() {
        super();
    }

    public AbstractRpcProvider(Formatter formatter, Parse parse) {
        this.formatter = formatter;
        this.parse = parse;
    }

    public abstract void start();

    public Formatter getFormatter() {
        return formatter;
    }

    public void setFormatter(Formatter formatter) {
        this.formatter = formatter;
    }

    public Parse getParse() {
        return parse;
    }

    public void setParse(Parse parse) {
        this.parse = parse;
    }
}
