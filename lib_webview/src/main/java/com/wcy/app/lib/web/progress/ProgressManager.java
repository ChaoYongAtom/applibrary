package com.wcy.app.lib.web.progress;

public interface ProgressManager<T extends BaseProgressSpec> {


    T offer();
}
