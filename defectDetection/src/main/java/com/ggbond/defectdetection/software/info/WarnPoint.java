package com.ggbond.defectdetection.software.info;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WarnPoint {
    WarningsEnum value();
}
