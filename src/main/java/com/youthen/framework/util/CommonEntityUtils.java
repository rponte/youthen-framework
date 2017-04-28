package com.youthen.framework.util;

import java.util.Date;
import java.util.Map;
import com.youthen.framework.common.DateFormatUtils;
import com.youthen.framework.common.context.SessionContext;
import com.youthen.framework.persistence.entity.CommonEntity;

/**
 * BusinessLogic annotation。
 * 
 * @author LiXin
 * @version $Revision: 1 $<br>
 *          $Date: 2014-07-14 19:26:55 $
 */
public final class CommonEntityUtils {

    public static final long MAX_VERSIONNO = 999999999999999L;

    private CommonEntityUtils() {
    }

    public static String getSystime() {
        return DateFormatUtils.format("yyyy-MM-dd HH:mm:ss", new Date());
    }

    public static Long getCompanyId() {
        if (SessionContext.getUser() != null) return SessionContext.getUser().getCompanyId();
        return null;
    }

    public static String getUpdId() {
        if (SessionContext.getUser() != null) return SessionContext.getUser().getUserId();
        return null;
    }

    public static void initializeCommonFields(final CommonEntity aEntity) {
        // aEntity.setCompanyId(CommonEntityUtils.getCompanyId());
        // aEntity.setCreateDate(new Date());
        aEntity.setVersionNo(Long.valueOf(0L));
        aEntity.setUpdId(CommonEntityUtils.getUpdId());
        aEntity.setUpdTime(CommonEntityUtils.getSystime());
    }

    public static void updateCommonFields(final CommonEntity aEntity) {
        if (aEntity.getVersionNo() == null) {
            aEntity.setVersionNo(0L);
        }
        long no = aEntity.getVersionNo().longValue();
        if (no < MAX_VERSIONNO) {
            no++;
        } else {
            no = 0L;
        }
        aEntity.setVersionNo(Long.valueOf(no));
        aEntity.setUpdId(CommonEntityUtils.getUpdId());
        aEntity.setUpdTime(CommonEntityUtils.getSystime());
    }

    @SuppressWarnings("unchecked")
    public static void putCommonFields(final Map aMap) {
        aMap.put("versionnoMax", Long.valueOf(CommonEntityUtils.MAX_VERSIONNO));
        aMap.put("updid", CommonEntityUtils.getUpdId());
        aMap.put("updtime", CommonEntityUtils.getSystime());
    }

}
