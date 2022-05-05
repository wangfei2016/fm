package com.xff.basecore.common.swagger;

import java.io.Serializable;

public interface IResultCode extends Serializable {
    int getCode();

    String getMsg();
}
