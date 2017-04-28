package com.youthen.framework.util;

public enum PropertyKeys implements IPropertyKeys {
        FILE_UPLOAD_MAX_SIZE_DEFAULT("file.upload.max.size.default"),
        FILE_UPLOAD_MAX_SIZE("file.upload.max.size"),
        FILE_DOWNLOAD_MAX_SIZE_DEFAULT("file.download.max.size.default"),
        FILE_DOWNLOAD_MAX_SIZE("file.download.max.size"),
        FILE_DOWNLOAD_CHARSET("file.download.charset"),
        UNAVAILABLE_FATAL_COUNT("unavailable.fatal.count"),
        ACCOUNT_LIMIT_DEFAULT("account.limit.default"),
        PASSWORD_LIMIT_DEFAULT("password.limit.default"),
        PASSWORD_LOCK_ERROR_COUNT("password.lock.error.count"),
        RANGE_DELIMITER_QUOTE("range.delimiter.quote"),
        RANGE_DELIMITER_EXTENT("range.delimiter.extent"),
        RANGE_DELIMITER_BLANK("range.delimiter.blank"),
        RANGE_DELIMITER_DELIMITER("range.delimiter.delimiter"),
        NULL_VALUE_CHAR("null.value.char");

    private String keyName;

    private PropertyKeys(final String aKeyName) {
        this.keyName = aKeyName;
    }

    @Override
    public String toString() {
        return this.keyName;
    }
}
