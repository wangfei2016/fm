package com.xff.basecore.common.swagger;

import com.xff.basecore.common.enume.HttpCodeEnum;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

public class SwaggerResultUtil<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(
            value = "响应码",
            required = true
    )
    private Integer code = 200;
    @ApiModelProperty("消息提示")
    private String msg;
    @ApiModelProperty("业务码")
    private String bizCode;
    @ApiModelProperty(
            value = "查询数据长度",
            required = true
    )
    private Object dataSize = 0;
    @ApiModelProperty(
            value = "数据",
            required = true
    )
    private T data;

    public SwaggerResultUtil() {
    }

    public SwaggerResultUtil(T data) {
        this.data = data;
    }

    public SwaggerResultUtil(T data, int status) {
        this.data = data;
        this.code = status;
    }

    public SwaggerResultUtil(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public SwaggerResultUtil(Integer code, String bizCode, String msg) {
        this.code = code;
        this.bizCode = bizCode;
        this.msg = msg;
    }

    public SwaggerResultUtil(Integer code, String bizCode, String msg, T data) {
        this.code = code;
        this.bizCode = bizCode;
        this.msg = msg;
        this.data = data;
    }

    public SwaggerResultUtil<T> data(T data) {
        this.data = data;
        return this;
    }

    public SwaggerResultUtil<T> msg(String msg) {
        this.msg = msg;
        return this;
    }

    public SwaggerResultUtil<T> bizCode(String bizCode) {
        this.bizCode = bizCode;
        return this;
    }

    public SwaggerResultUtil<T> dataSize(Object dataSize) {
        this.dataSize = dataSize;
        return this;
    }

    public void setDefError(Exception ex) {
        this.msg = ex.getMessage();
        this.code = 510;
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getBizCode() {
        return this.bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public Object getDataSize() {
        return this.dataSize;
    }

    public void setDataSize(Object dataSize) {
        this.dataSize = dataSize;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public SwaggerResultUtil<T> success() {
        this.code = HttpCodeEnum.SUCCESS.getCode();
        this.msg = HttpCodeEnum.SUCCESS.getMsg();
        return this;
    }

    public SwaggerResultUtil<T> success(T data) {
        this.data = data;
        return this.success();
    }

    public SwaggerResultUtil<T> error(int code) {
        return this.error(code, (String)null, (T)null);
    }

    public SwaggerResultUtil<T> error(int code, String msg) {
        return this.error(code, msg, (T)null);
    }

    public SwaggerResultUtil<T> error(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        return this;
    }

    public static <T> SwaggerResultUtil<T> errorParams() {
        return (new SwaggerResultUtil()).error(HttpCodeEnum.SWAGGER_ERROR.getCode(), HttpCodeEnum.SWAGGER_ERROR.getMsg());
    }

    public static <T> SwaggerResultUtil<T> resultError(int code, String msg) {
        return (new SwaggerResultUtil()).error(code, msg);
    }

    public static <T> SwaggerResultUtil<T> resultSuccess() {
        return (new SwaggerResultUtil()).success();
    }

    public static <T> SwaggerResultUtil<T> resultSuccess(Object data) {
        return (new SwaggerResultUtil()).success(data);
    }

    private static SwaggerResultUtil.DataBuilder status(HttpCodeEnum httpCode) {
        return new SwaggerResultUtil.DefaultBuilder(httpCode.getCode(), httpCode.getMsg());
    }

    public static SwaggerResultUtil.DataBuilder status(int status) {
        return new SwaggerResultUtil.DefaultBuilder(status);
    }

    public static SwaggerResultUtil.DataBuilder okbuild() {
        return status(200);
    }

    public static <T> SwaggerResultUtil<T> ok() {
        SwaggerResultUtil.DataBuilder builder = okbuild();
        return builder.build();
    }

    public static <T> SwaggerResultUtil<T> ok(T data) {
        SwaggerResultUtil.DataBuilder builder = okbuild();
        return builder.data(data);
    }

    public static <T> SwaggerResultUtil<T> okCustomer(String msg) {
        SwaggerResultUtil.DataBuilder builder = status(200);
        return builder.msg(msg);
    }

    public static <T> SwaggerResultUtil<T> okCustomer(String msg, T data) {
        SwaggerResultUtil.DataBuilder builder = status(200);
        return builder.build(200, msg, data);
    }

    public static <T> SwaggerResultUtil<T> errorCustomer() {
        return (new SwaggerResultUtil.DefaultBuilder(HttpCodeEnum.SWAGGER_ERROR.getCode())).msg(HttpCodeEnum.SWAGGER_ERROR.getMsg());
    }

    public static <T> SwaggerResultUtil<T> errorCustomer(int code) {
        return (new SwaggerResultUtil.DefaultBuilder(code)).build();
    }

    public static <T> SwaggerResultUtil<T> errorCustomer(HttpCodeEnum httpCodeEnum) {
        return (new SwaggerResultUtil.DefaultBuilder(httpCodeEnum.getCode())).msg(httpCodeEnum.getMsg());
    }

    public static <T> SwaggerResultUtil<T> errorCustomer(int code, String msg) {
        return (new SwaggerResultUtil.DefaultBuilder(code)).msg(msg);
    }

    public static <T> SwaggerResultUtil<T> errorCustomer(int code, String msg, T data) {
        return (new SwaggerResultUtil.DefaultBuilder(code)).msg(msg);
    }

    public static <T> SwaggerResultUtil<T> errorParam() {
        SwaggerResultUtil.DataBuilder builder = status(HttpCodeEnum.SWAGGER_ERROR);
        return builder.build();
    }

    public static <T> SwaggerResultUtil<T> errorParam(String msg) {
        SwaggerResultUtil.DataBuilder builder = status(HttpCodeEnum.SWAGGER_ERROR);
        return builder.msg(msg);
    }

    public static <T> SwaggerResultUtil<T> error400(String msg) {
        SwaggerResultUtil.DataBuilder builder = status(HttpCodeEnum.BAD_REQUIRED_ERROR);
        return builder.msg(msg);
    }

    public static <T> SwaggerResultUtil<T> error400() {
        SwaggerResultUtil.DataBuilder builder = status(HttpCodeEnum.BAD_REQUIRED_ERROR);
        return builder.build();
    }

    public static <T> SwaggerResultUtil<T> error500(String msg) {
        SwaggerResultUtil.DataBuilder builder = status(HttpCodeEnum.INTERNAL_SERVER_ERROR);
        return builder.msg(msg);
    }

    public static <T> SwaggerResultUtil<T> error500() {
        SwaggerResultUtil.DataBuilder builder = status(HttpCodeEnum.INTERNAL_SERVER_ERROR);
        return builder.build();
    }

    public static <T> SwaggerResultUtil<T> errorBadRequest() {
        SwaggerResultUtil.DataBuilder builder = status(HttpCodeEnum.BAD_REQUIRED_ERROR);
        return builder.build();
    }

    public static <T> SwaggerResultUtil<T> errorBadRequest(String msg) {
        SwaggerResultUtil.DataBuilder builder = status(HttpCodeEnum.BAD_REQUIRED_ERROR);
        return builder.msg(msg);
    }

    public static int getCode8(int xtxym, int fwid, int ycbm) {
        return Integer.valueOf(xtxym + String.valueOf(fwid) + ycbm);
    }

    public static <T> SwaggerResultUtil<T> errorParams(int fwid, int ycbm) {
        return (new SwaggerResultUtil()).error(getCode8(HttpCodeEnum.SWAGGER_ERROR.getCode(), fwid, ycbm), HttpCodeEnum.SWAGGER_ERROR.getMsg());
    }

    public static <T> SwaggerResultUtil<T> resultError(int code, String msg, int fwid, int ycbm) {
        return (new SwaggerResultUtil()).error(getCode8(code, fwid, ycbm), msg);
    }

    public static <T> SwaggerResultUtil<T> errorCustomer(int fwid, int ycbm) {
        return (new SwaggerResultUtil.DefaultBuilder(getCode8(HttpCodeEnum.SWAGGER_ERROR.getCode(), fwid, ycbm))).msg(HttpCodeEnum.SWAGGER_ERROR.getMsg());
    }

    public static <T> SwaggerResultUtil<T> errorCustomer(HttpCodeEnum httpCodeEnum, int fwid, int ycbm) {
        return (new SwaggerResultUtil.DefaultBuilder(getCode8(HttpCodeEnum.BAD_REQUIRED_ERROR.getCode(), fwid, ycbm))).msg(httpCodeEnum.getMsg());
    }

    public static <T> SwaggerResultUtil<T> errorCustomer(int code, String msg, int fwid, int ycbm) {
        return (new SwaggerResultUtil.DefaultBuilder(getCode8(code, fwid, ycbm))).msg(msg);
    }

    public static <T> SwaggerResultUtil<T> errorCustomer(int code, String msg, T data, int fwid, int ycbm) {
        return (new SwaggerResultUtil.DefaultBuilder(getCode8(code, fwid, ycbm))).msg(msg);
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof SwaggerResultUtil)) {
            return false;
        } else {
            SwaggerResultUtil<?> other = (SwaggerResultUtil)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label71: {
                    Object this$code = this.getCode();
                    Object other$code = other.getCode();
                    if (this$code == null) {
                        if (other$code == null) {
                            break label71;
                        }
                    } else if (this$code.equals(other$code)) {
                        break label71;
                    }

                    return false;
                }

                Object this$msg = this.getMsg();
                Object other$msg = other.getMsg();
                if (this$msg == null) {
                    if (other$msg != null) {
                        return false;
                    }
                } else if (!this$msg.equals(other$msg)) {
                    return false;
                }

                label57: {
                    Object this$bizCode = this.getBizCode();
                    Object other$bizCode = other.getBizCode();
                    if (this$bizCode == null) {
                        if (other$bizCode == null) {
                            break label57;
                        }
                    } else if (this$bizCode.equals(other$bizCode)) {
                        break label57;
                    }

                    return false;
                }

                Object this$dataSize = this.getDataSize();
                Object other$dataSize = other.getDataSize();
                if (this$dataSize == null) {
                    if (other$dataSize != null) {
                        return false;
                    }
                } else if (!this$dataSize.equals(other$dataSize)) {
                    return false;
                }

                Object this$data = this.getData();
                Object other$data = other.getData();
                if (this$data == null) {
                    if (other$data == null) {
                        return true;
                    }
                } else if (this$data.equals(other$data)) {
                    return true;
                }

                return false;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof SwaggerResultUtil;
    }

    public int hashCode() {
        //int PRIME = true;
        int result = 1;
        Object $code = this.getCode();
        result = result * 59 + ($code == null ? 43 : $code.hashCode());
        Object $msg = this.getMsg();
        result = result * 59 + ($msg == null ? 43 : $msg.hashCode());
        Object $bizCode = this.getBizCode();
        result = result * 59 + ($bizCode == null ? 43 : $bizCode.hashCode());
        Object $dataSize = this.getDataSize();
        result = result * 59 + ($dataSize == null ? 43 : $dataSize.hashCode());
        Object $data = this.getData();
        result = result * 59 + ($data == null ? 43 : $data.hashCode());
        return result;
    }

    public String toString() {
        return "SwaggerResultUtil(code=" + this.getCode() + ", msg=" + this.getMsg() + ", bizCode=" + this.getBizCode() + ", dataSize=" + this.getDataSize() + ", data=" + this.getData() + ")";
    }

    private static class DefaultBuilder implements SwaggerResultUtil.DataBuilder {
        private int code;
        private String msg;

        public DefaultBuilder(int code) {
            this.code = code;
        }

        public DefaultBuilder(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public <T> SwaggerResultUtil<T> build() {
            return this.data((T)null);
        }

        public <T> SwaggerResultUtil<T> build(int status) {
            return new SwaggerResultUtil((Object)null, status);
        }

        public <T> SwaggerResultUtil<T> data(T data) {
            return new SwaggerResultUtil(this.code, (String)null, this.msg, data);
        }

        public <T> SwaggerResultUtil<T> msg(String msg) {
            return new SwaggerResultUtil(this.code, (String)null, msg, (Object)null);
        }

        public <T> SwaggerResultUtil<T> build(int status, String msg) {
            return new SwaggerResultUtil(status, (String)null, msg, (Object)null);
        }

        public <T> SwaggerResultUtil<T> build(int status, String msg, T data) {
            return new SwaggerResultUtil(status, (String)null, msg, data);
        }
    }

    public interface DataBuilder {
        <T> SwaggerResultUtil<T> build();

        <T> SwaggerResultUtil<T> build(int status);

        <T> SwaggerResultUtil<T> build(int status, String msg);

        <T> SwaggerResultUtil<T> build(int status, String msg, T data);

        <T> SwaggerResultUtil<T> msg(String msg);

        <T> SwaggerResultUtil<T> data(T data);
    }
}
