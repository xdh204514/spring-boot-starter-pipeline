package com.jdl.xdh.pipeline.common;

import java.util.Objects;

/**
 * 基础枚举
 *
 * @author xudehui1
 * @date 2023/11/29 11:05
 */
public interface BaseEnum<T extends Enum<T> & BaseEnum<T>> {

    /**
     * 获取code码存入数据库
     *
     * @return 获取编码
     */
    String getCode();

    /**
     * 获取编码名称，便于维护
     *
     * @return 获取编码名称
     */
    String getName();

    /**
     * 根据code码获取枚举
     *
     * @param cls  enum class
     * @param code enum code
     * @return get enum
     */
    static <T extends Enum<T> & BaseEnum<T>> T parseByCode(Class<T> cls, String code) {
        for (T t : cls.getEnumConstants()) {
            if (Objects.equals(t.getCode(), code)) {
                return t;
            }
        }
        return null;
    }

}