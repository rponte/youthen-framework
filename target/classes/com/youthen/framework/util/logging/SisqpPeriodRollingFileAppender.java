package com.youthen.framework.util.logging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import org.apache.log4j.Layout;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;
import com.youthen.framework.common.StringUtils;
import com.youthen.framework.common.fields.FieldSupportedMessage;

/**
 * BusinessLogic annotation。
 * 
 * @author LiXin
 * @version $Revision: 1 $<br>
 *          $Date: 2014-07-14 19:26:55 $
 */
public class SisqpPeriodRollingFileAppender extends SisqpFileAppender {

    static final int TOP_OF_TROUBLE = -1;
    static final int TOP_OF_MINUTE = 0;
    static final int TOP_OF_HOUR = 1;
    static final int HALF_DAY = 2;
    static final int TOP_OF_DAY = 3;
    static final int TOP_OF_WEEK = 4;
    static final int TOP_OF_MONTH = 5;

    static final TimeZone GMT_TIME_ZONE = TimeZone.getTimeZone("GMT");

    private static final String MESSAGE_KEY_OPTION_IS_INSUFFICIENT = "XFW72006";
    private static final String MESSAGE_KEY_RENAME_ERROR = "XFW72011";
    private static final String MESSAGE_KEY_SAME_NAME_FILE_DELETE_ERROR = "XFW72012";

    String datePattern = "'.'yyyy-MM-dd";
    Date lastModified;

    private final Date now = new Date();
    private SimpleDateFormat sdf;
    private final RollingCalendar rc = new RollingCalendar();

    public SisqpPeriodRollingFileAppender() {
    }

    public SisqpPeriodRollingFileAppender(final Layout aLayout, final String aFilename, final String aDatePattern)
            throws IOException {
        super(aLayout, aFilename);
        this.datePattern = aDatePattern;
        this.activateOptions();
    }

    public void setDatePattern(final String aDatePattern) {
        this.datePattern = aDatePattern;
    }

    public String getDatePattern() {
        return this.datePattern;
    }

    @Override
    public void activateOptions() {
        super.activateOptions();
        if (this.datePattern != null && this.fileName != null) {
            this.now.setTime(System.currentTimeMillis());
            this.sdf = new SimpleDateFormat(this.datePattern);
            final int type = this.computeCheckPeriod();
            this.rc.setType(type);
        } else {
            LogLog.error(new FieldSupportedMessage(MESSAGE_KEY_TRIGGER_EVENT_NOT_SET).format(this.name).getMesg());
        }
    }

    int computeCheckPeriod() {
        final RollingCalendar rollingCalendar = new RollingCalendar(GMT_TIME_ZONE, Locale.ENGLISH);
        final Date epoch = new Date(0);
        if (this.datePattern != null) {
            for (int i = TOP_OF_MINUTE; i <= TOP_OF_MONTH; i++) {
                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.datePattern);
                simpleDateFormat.setTimeZone(GMT_TIME_ZONE); // do all date formatting in GMT
                final String r0 = simpleDateFormat.format(epoch);
                rollingCalendar.setType(i);
                final Date next = new Date(rollingCalendar.getNextCheckMillis(epoch));
                final String r1 = simpleDateFormat.format(next);
                if (r0 != null && r1 != null && !r0.equals(r1)) {
                    return i;
                }
            }
        }
        return TOP_OF_TROUBLE; // Deliberately head for trouble...
    }

    @Override
    protected void subAppend(final LoggingEvent aEvent) {
        this.rollOver(aEvent);
        super.subAppend(aEvent);
    }

    private void rollOver(final LoggingEvent aEvent) {
        boolean result = true;

        if (StringUtils.isNotEmpty(this.datePattern)) {
            if (this.sdf == null) {
                this.sdf = new SimpleDateFormat(this.datePattern);
            }

            final long currentTime = System.currentTimeMillis();
            this.now.setTime(currentTime);
            final String datedPeriod = this.sdf.format(this.now);

            final String currentFileName = this.createFileName(this.fileName, aEvent);
            this.setLastModified(currentFileName);
            final long nextCheckPeriod = this.rc.getNextCheckMillis(this.getLastModified());
            final String scheculedPeriod = this.sdf.format(this.getLastModified());

            if (currentTime >= nextCheckPeriod && !scheculedPeriod.equals(datedPeriod)) {

                final String scheduledFileName = currentFileName + scheculedPeriod;

                this.closeFile();

                final File scheduledFile = new File(scheduledFileName);
                result = this.checkExistsFile(scheduledFile);

                if (result) {
                    final File currentFile = new File(currentFileName);
                    result = this.doRolling(scheduledFileName, scheduledFile, currentFileName, currentFile);
                }

            } else {
                result = true;
            }

        } else {
            this.errorHandler.error(
                    new FieldSupportedMessage(MESSAGE_KEY_OPTION_IS_INSUFFICIENT).format("DatePattern", this.name)
                            .getMesg());
            result = false;
        }

        if (!result) {
            LogLog.error("rollOver() failed.");
        }
    }

    private boolean checkExistsFile(final File aFile) {
        if (aFile.exists()) {
            if (!aFile.delete()) {
                LogLog
                        .error(new FieldSupportedMessage(MESSAGE_KEY_SAME_NAME_FILE_DELETE_ERROR).format(aFile)
                                .getMesg());
                return false;
            }
        }
        return true;
    }

    private boolean doRolling(final String aScheduledFileName, final File aScheduledFile,
            final String aCurrentFileName, final File aCurrentFile) {

        boolean result;

        // if (aCurrentFile.renameTo(aScheduledFile)) {
        if (this.copy(aCurrentFile, aScheduledFile)) {
            LogLog.debug(aCurrentFileName + " -> " + aScheduledFileName);

            try {
                this.setFile(aCurrentFileName, false, this.bufferedIO, this.bufferSize);
                result = true;
            } catch (final IOException e) {
                this.errorHandler.error(new FieldSupportedMessage(MESSAGE_KEY_IO_ERROR).format(aCurrentFileName)
                        .getMesg());
                result = false;
            }

        } else {
            LogLog.error(
                    new FieldSupportedMessage(MESSAGE_KEY_RENAME_ERROR).format(aCurrentFileName, aScheduledFileName)
                            .getMesg());
            result = false;
        }
        return result;
    }

    /**
     * 。
     * 
     * @param aCurrentFile
     * @param aScheduledFile
     * @return
     */
    private boolean copy(final File aCurrentFile, final File aScheduledFile) {
        try {
            final FileInputStream fosFrom = new FileInputStream(aCurrentFile);
            final FileOutputStream fosTo = new FileOutputStream(aScheduledFile);
            final byte[] buffer = new byte[2048];
            int c;
            while ((c = fosFrom.read(buffer)) > 0) {
                fosTo.write(buffer, 0, c);
            }
            fosFrom.close();
            fosTo.close();
            return true;
        } catch (final Exception e) {
            return false;
        }
    }

    private void setLastModified(final String aFileName) {
        this.lastModified = new Date(new File(aFileName).lastModified());
    }

    private Date getLastModified() {
        return this.lastModified;
    }

}

class RollingCalendar extends GregorianCalendar {

    private static final long serialVersionUID = -8117995866845214348L;

    private int type = SisqpPeriodRollingFileAppender.TOP_OF_TROUBLE;

    RollingCalendar() {
        super();
    }

    RollingCalendar(final TimeZone aTimeZone, final Locale aLocale) {
        super(aTimeZone, aLocale);
    }

    void setType(final int aType) {
        this.type = aType;
    }

    public long getNextCheckMillis(final Date aNow) {
        return this.getNextCheckDate(aNow).getTime();
    }

    public Date getNextCheckDate(final Date aNow) {
        this.setTime(aNow);

        switch (this.type) {
            case SisqpPeriodRollingFileAppender.TOP_OF_MINUTE:
                this.set(Calendar.SECOND, 0);
                this.set(Calendar.MILLISECOND, 0);
                this.add(Calendar.MINUTE, 1);
                break;
            case SisqpPeriodRollingFileAppender.TOP_OF_HOUR:
                this.set(Calendar.MINUTE, 0);
                this.set(Calendar.SECOND, 0);
                this.set(Calendar.MILLISECOND, 0);
                this.add(Calendar.HOUR_OF_DAY, 1);
                break;
            case SisqpPeriodRollingFileAppender.HALF_DAY:
                this.set(Calendar.MINUTE, 0);
                this.set(Calendar.SECOND, 0);
                this.set(Calendar.MILLISECOND, 0);
                final int hour = this.get(Calendar.HOUR_OF_DAY);
                if (hour < 12) {
                    this.set(Calendar.HOUR_OF_DAY, 12);
                } else {
                    this.set(Calendar.HOUR_OF_DAY, 0);
                    this.add(Calendar.DAY_OF_MONTH, 1);
                }
                break;
            case SisqpPeriodRollingFileAppender.TOP_OF_DAY:
                this.set(Calendar.HOUR_OF_DAY, 0);
                this.set(Calendar.MINUTE, 0);
                this.set(Calendar.SECOND, 0);
                this.set(Calendar.MILLISECOND, 0);
                this.add(Calendar.DATE, 1);
                break;
            case SisqpPeriodRollingFileAppender.TOP_OF_WEEK:
                this.set(Calendar.DAY_OF_WEEK, this.getFirstDayOfWeek());
                this.set(Calendar.HOUR_OF_DAY, 0);
                this.set(Calendar.MINUTE, 0);
                this.set(Calendar.SECOND, 0);
                this.set(Calendar.MILLISECOND, 0);
                this.add(Calendar.WEEK_OF_YEAR, 1);
                break;
            case SisqpPeriodRollingFileAppender.TOP_OF_MONTH:
                this.set(Calendar.DATE, 1);
                this.set(Calendar.HOUR_OF_DAY, 0);
                this.set(Calendar.MINUTE, 0);
                this.set(Calendar.SECOND, 0);
                this.set(Calendar.MILLISECOND, 0);
                this.add(Calendar.MONTH, 1);
                break;
            default:
                throw new IllegalStateException("Unknown periodicity type.");
        }
        return this.getTime();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + this.type;
        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object aObj) {
        if (this == aObj) {
            return true;
        }
        if (!super.equals(aObj)) {
            return false;
        }
        if (!(aObj instanceof RollingCalendar)) {
            return false;
        }
        final RollingCalendar other = (RollingCalendar) aObj;
        if (this.type != other.type) {
            return false;
        }
        return true;
    }

}
