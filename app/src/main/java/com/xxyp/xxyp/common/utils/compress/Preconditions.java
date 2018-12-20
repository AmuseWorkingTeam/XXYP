
package com.xxyp.xxyp.common.utils.compress;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.File;

/**
 * <li>Description : 空值检查
 */
final class Preconditions {
    /**
     * Ensures that an object reference passed as a parameter to the calling
     * method is not null.
     *
     * @param reference an object reference
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling
     * method is not null.
     *
     * @param reference an object reference
     * @param errorMessage the exception message to use if the check fails; will
     *            be converted to a string using {@link String#valueOf(Object)}
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    static <T> T checkNotNull(T reference, @Nullable Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }

    /**
     * 根据文件生成缓存文件名
     * 
     * @param file 文件
     * @return 缓存文件名
     */
    static String buildCacheName(File file, String compressFileName) {
        if (!TextUtils.isEmpty(compressFileName)) {
            return compressFileName;
        }
        if (file == null) {
            return "_cache";
        }
        return String.valueOf((file.getAbsolutePath() + file.length()).hashCode());
    }
}
