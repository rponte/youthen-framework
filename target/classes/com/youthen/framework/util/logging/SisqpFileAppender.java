package com.youthen.framework.util.logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Writer;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.LoggingEvent;
import com.youthen.framework.common.StringUtils;
import com.youthen.framework.common.context.SessionContext;
import com.youthen.framework.common.fields.FieldSupportedMessage;
import com.youthen.framework.common.security.AuthenticatedUser;

/**
 * BusinessLogic annotation。
 * 
 * @author LiXin
 * @version $Revision: 1 $<br>
 *          $Date: 2014-07-14 19:26:55 $
 */
public class SisqpFileAppender extends FileAppender {

    static final String MESSAGE_KEY_TRIGGER_EVENT_NOT_SET = "XFW72005";

    static final String MESSAGE_KEY_IO_ERROR = "XFW72010";

    static final String BIND_COMPANY_CD = "%COMPANY_CD";
    static final String MDC_COMPANY_CD = "companyCode";
    static final String BIND_COMPANY_CD_NONE_KAICHA_CD = "other";
    static final String BIND_COMPANY_CD_NONE_FILEPATH = "noname.log";
    static final String ENCODING_DEFAULT = "UTF-8";

    private Layout defaultLayout;
    private String defaultFileName;
    private boolean defaultAppend;
    private boolean defaultBufferdIO;
    private int defaultBufferSize;

    public SisqpFileAppender() {
    }

    public SisqpFileAppender(final Layout aLayout, final String aFilename,
            final boolean aAppend, final boolean aBufferedIO, final int aBufferSize) throws IOException {
        this.setAppenderSettings(aLayout, aFilename, aAppend, aBufferedIO, aBufferSize);
    }

    public SisqpFileAppender(final Layout aLayout, final String aFilename, final boolean aAppend) throws IOException {
        this(aLayout, aFilename, aAppend, false, 8 * 1024);
    }

    public SisqpFileAppender(final Layout aLayout, final String aFilename) throws IOException {
        this(aLayout, aFilename, true, false, 8 * 1024);
    }

    @Override
    public void activateOptions() {
        if (StringUtils.isNotEmpty(this.fileName)) {
            try {
                this
                        .saveDefaultParameter(this.layout, this.fileName, this.fileAppend, this.bufferedIO,
                                this.bufferSize);

                this.setFile(this.createFileName(this.fileName), this.fileAppend, this.bufferedIO, this.bufferSize);
            } catch (final IOException e) {
                this.errorHandler.error(
                        new FieldSupportedMessage(MESSAGE_KEY_TRIGGER_EVENT_NOT_SET).format(this.name).getMesg(),
                        e, ErrorCode.FILE_OPEN_FAILURE);
            }
        } else {
            LogLog.warn(new FieldSupportedMessage(MESSAGE_KEY_TRIGGER_EVENT_NOT_SET).format(this.name).getMesg());
        }
    }

    @Override
    public synchronized void setFile(
            final String aFileName, final boolean aAppend, final boolean aBufferedIO, final int aBufferSize)
            throws IOException {

        if (aBufferedIO) {
            this.setImmediateFlush(false);
        }

        this.reset();

        Writer fw = this.createWriter(this.createFileOutputStream(aFileName, aAppend));

        if (aBufferedIO) {
            fw = new BufferedWriter(fw, aBufferSize);
        }
        this.setQWForFiles(fw);

        this.reverseDefaultParameter();
        this.writeHeader();
    }

    @Override
    protected void reset() {
        this.closeFile();
        this.closeWriter();
        this.qw = null;
    }

    @Override
    public void append(final LoggingEvent aEvent) {

        try {
            this.setWriter(this.createWriter(this.createFileOutputStream(this.createFileName(this.fileName, aEvent),
                    this.fileAppend)));

            if (this.checkEntryConditions()) {
                this.reverseDefaultParameter();

                this.subAppend(aEvent);
            }
        } catch (final IOException e) {
            this.errorHandler.error(
                    new FieldSupportedMessage(MESSAGE_KEY_IO_ERROR).format(this.createFileName(this.fileName, aEvent))
                            .getMesg(),
                    e, ErrorCode.FILE_OPEN_FAILURE);
        }
    }

    protected void setAppenderSettings(final Layout aLayout, final String aFileName, final boolean aAppend,
            final boolean aBufferedIO, final int aBufferSize) {
        this.layout = aLayout;
        if (aFileName != null) {
            this.fileName = aFileName.trim();
        }
        this.fileAppend = aAppend;
        this.setBufferedIO(aBufferedIO);
        this.bufferSize = aBufferSize;
    }

    private void saveDefaultParameter(final Layout aLayout, final String aFileName, final boolean aAppend,
            final boolean aBufferedIO, final int aBufferSize) {
        this.defaultLayout = aLayout;
        this.defaultFileName = aFileName;
        this.defaultAppend = aAppend;
        this.defaultBufferdIO = aBufferedIO;
        this.defaultBufferSize = aBufferSize;
    }

    protected void reverseDefaultParameter() {
        this.layout = this.defaultLayout;
        this.fileName = this.defaultFileName;
        this.fileAppend = this.defaultAppend;
        this.bufferedIO = this.defaultBufferdIO;
        this.bufferSize = this.defaultBufferSize;
    }

    String createFileName(final String aPreFileName) {
        return this.createFileName(aPreFileName, null);
    }

    String createFileName(final String aPreFileName, final LoggingEvent aEvent) {
        String result;
        if (StringUtils.isNotEmpty(aPreFileName)) {
            String companyCode = null;
            if (SessionContext.isAuthenticated()) {
                final AuthenticatedUser user = SessionContext.getUser();
                if (user != null) {
                    companyCode = SessionContext.getUser().getCompanyCode();
                }
            } else if (aEvent != null && aEvent.getMDC(MDC_COMPANY_CD) != null) {
                companyCode = String.valueOf(aEvent.getMDC(MDC_COMPANY_CD));
            }
            if (StringUtils.isNotEmpty(companyCode)) {
                result = aPreFileName.replaceAll(BIND_COMPANY_CD, companyCode);
            } else {
                result = aPreFileName.replaceAll(BIND_COMPANY_CD, BIND_COMPANY_CD_NONE_KAICHA_CD);
            }
        } else {
            result = BIND_COMPANY_CD_NONE_FILEPATH;
        }

        return result;
    }

    protected FileOutputStream createFileOutputStream(final String aFileName, final boolean aAppend)
            throws IOException {
        FileOutputStream result = null;

        try {
            result = new FileOutputStream(aFileName, aAppend);
        } catch (final FileNotFoundException ex) {
            final String parentName = new File(aFileName).getParent();
            if (parentName != null) {
                final File parentDir = new File(parentName);
                if (!parentDir.exists() && parentDir.mkdirs()) {
                    result = new FileOutputStream(aFileName, aAppend);
                } else {
                    throw ex;
                }
            } else {
                throw ex;
            }
        }
        return result;
    }

    @Override
    public String getEncoding() {
        if (this.encoding != null) {
            return this.encoding;
        }
        return ENCODING_DEFAULT;
    }

}
