package com.dkha.common.result.systemcode;


/**
 * @Author Spring
 * @Since 2018/8/15 11:55
 * @Description 系统通用返回码 约束如下：
 * 操作成功 统一返回code 0
 * 错误操作或者特殊状态码 统一返回非0 错误码默认采用60001
 */
public enum SystemCode {

    WX_GET_CONFIG_ERROR(40000, "获取微信参数错误"),
    WX_UPLOAD_ERROR(40001, "获取微信参数错误"),

    /**
     * -----------------------------统一异常捕获（50***）异常码定义-------------------------------------
     */

    INTERNAL_PROGRAM_ERROR(50000, "程序内部错误，操作失败"),
    DATA_ACCESS_EXCEPTION(50001, "数据库操作失败"),
    COMMUNICATIONS_EXCEPTION(50002, "数据库连接中断"),
    CONSTRAINT_VIOLATION_EXCEPTION(50002, "对象已经存在，请勿重复操作"),
    DATA_INTEGRITY_VIOLATION_EXCEPTION(50003, "对象已经存在，请勿重复操作"),
    MYSQL_INTEGRITY_CONSTRAINT_VIOLATION_EXCEPTION(50004, "对象已经存在，请勿重复操作"),
    NULL_POINTER_EXCEPTION(50005, "调用了未经初始化的对象或者是不存在的对象"),
    IO_EXCEPTION(50006, "IO异常"),
    CLASS_NOT_FOUND_EXCEPTION(50007, "指定的类不存在"),
    ARITHMETIC_EXCEPTION(50008, "数学运算异常"),
    ARRAY_INDEX_OUT_OF_BOUNDS_EXCEPTION(50009, "数组下标越界"),
    ILLEGAL_ARGUMENT_EXCEPTION(50010, "参数错误或非法"),
    ClassCastException(50011, "类型强制转换错误"),
    SQL_EXCEPTION(50013, "操作数据库异常"),
    SECURITY_EXCEPTION(50012, "违背安全原则异常"),
    NO_SUCH_METHOD_EXCEPTION(50014, "方法未找到异常"),
    INTERNAL_ERROR(50015, "内部错误"),
    CONNECT_EXCEPTION(50016, "服务器连接异常"),
    CANCELLATION_EXCEPTION(50017, "任务已被取消的异常"),
    API_EXCEPTION(50018, "阿里服务器错误"),
    PARSE_EXCEPTION(50019, "日期格式错误"),
    DATA_OVER_LONG(50020, "数据超长"),
    SYNC_REQUEST_NOT_RESPONSE(50021, "同步网络请求无数据返回"),
    SERVER_INNER_ERROR(50022, "服务器内部异常"),

    /**-----------------------------统一异常捕获（50***）异常码定义-------------------------------------*/


    /**
     * -----------------------------参数异常（51***）异常码定义-------------------------------------
     */

    ParaIsNull(51002, "参数为空"),
    paraNotRight(51003, "参数非法"),

    /**
     * -----------------------------公共操作成功、失败（60***）异常码定义-------------------------------------
     */

    HANDLER_SUCCESS(0, "操作成功"),
    HANDLER_FAILED(60001, "操作失败"),
    SAVE_SUCCESS(60002, "新增成功"),
    SAVE_SUCCESS_(60017, "保存成功"),
    SAVE_FAILED(60003, "新增失败"),
    DELETE_SUCCESS(60004, "删除成功"),
    DELETE_FAILED(60005, "删除失败"),
    UPDATE_SUCCESS(60006, "修改成功"),
    UPDATE_FAILED(60007, "修改失败"),
    SET_SUCCESS(60008, "设置成功"),
    SET_FAILED(60009, "设置失败"),
    NO_DATA(60010, "无对应数据"),
    SYNC_SUCCESS(60011, "同步成功"),
    SYNC_FAILED(60012, "同步失败"),
    SYNC_DATA_IS_NULL(60013, "同步数据为空"),
    SYNC_DATA_NOT_ALL_SUCCESS(60014, "同步数据部分成功"),
    FIND_SUCCESS(60015, "查询成功"),
    FIND_FAILED(60016, "查询失败"),
    COMMODITY_NUM_NOT_ENOUGH_FOR_ADD(60017, "商品数量不足"),

    COMMODITY_NUM_NOT_ENOUGH_FOR_REDUCE(60018, "商品数量不足"),

    COMMODITY_NOT_ENABLE(60019, "商品已下架"),
    EXCEL_ERROR_DATA(60020, "excel错误数据"),

    ORDER_INVALID(61000, "订单已无效"),

    WX_PAY_ERROR(62000, "微信支付异常"),

    /**
     * -----------------------------登录成功、失败（70***）异常码定义-------------------------------------
     */

    LOGIN_SUCCESS(70000,"登录成功"),
    LOGIN_FAILED(70001,"登录失败"),
    LOGIN_FAILED_USERNAME(70002,"用户名不存在"),
    LOGIN_FAILED_PASSWORD(70003,"密码错误"),
    LOGIN_FAILED_USERNAME_OR_PASSWORD(70004,"用户名或密码错误"),
    LOGIN_FAILED_SESSION_TIMEOUT(70005,"重新登录"),
    LOGIN_ERROR_USER(70006,"用户状态异常"),
    ACCOUNT_ENABLE(70007, "账号已停用"),
    ACCOUNT_CAN_NOT_EMPTY(70008, "账号不能为空"),
    PASSWORD_CAN_NOT_EMPTY(70009, "密码不能为空"),
    LOGIN_LIMIT(70010, "账号已锁定，请联系主管部门管理人员初始化登录密码！"),

    TOKEN_ERROR(71000, "token无效"),
    TOKEN_EXPIRED(71001, "token已过期"),
    SESSION_EXPIRE(71002, "账号已过期，请重新登录!"),
    VERIFICATION_CODE_ERROR(71003, "图形验证码错误!"),
    SINGLE_EQUIPMENT_ERROR(71004, "当前账号已在其他设备登录!"),
    SHORT_MESSAGE_CODE_ERROR(71005, "短信验证码错误！"),
    SHORT_MESSAGE_CAN_NOT_NULL(71006, "短信验证码不能为空!"),
    SHORT_MESSAGE_EXPIRED(71007, "短信验证码已过期!"),
    SIGNATURE_ERROR(71008, "签名错误!"),
    PHONE_ALREADY_REGISTER(71009, "当前手机号已注册!"),
    USER_NOT_FOLLOW(72000, "当前用户未关注微信公众号."),
    ACCOUNT_WAIT_REVIEW(72001, "账号待审核."),
    ACCOUNT_REVIEW_NOT_PASS(72002, "账号审核未通过."),
    NOT_AUTHORIZATION(72003, "当前用户暂无权限."),

    REDIS_ERROR(80001, "redis错误"),
    DATA_CAN_NOT_EMPTY(80002, "数据不能为空!"),
    LABEL_ERROR(80003, "查不到身份信息，请到人社局录入!"),
    LABELCONLL_ERROR(80004, "岗位已经收藏过了!");

    public Integer code;
    public String des;

    SystemCode(Integer code, String des) {
        this.code = code;
        this.des = des;
    }

    public static SystemCode get(Integer code) {
        for (SystemCode errorCode : SystemCode.values()) {
            if (errorCode.code.toString().equals(code.toString())) {
                return errorCode;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "code:" + code + ", des:" + des;
    }

}
