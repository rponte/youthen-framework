package com.youthen.framework.test;

import java.util.List;
import javax.annotation.Resource;
import mockit.internal.startup.Startup;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.util.Assert;
import com.opensymphony.xwork2.Action;
import com.youthen.framework.util.cache.CacheNodeGroup;
import com.youthen.framework.util.cache.CacheUtils;

/**
 * BusinessLogic annotation。
 * 
 * @author LiXin
 * @version $Revision: 1 $<br>
 *          $Date: 2014-07-14 19:26:55 $
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(defaultRollback = false)
public class BaseTest implements InitializingBean {

    public static final String CONTEXT_CONFIGURATION_FW_TEST = "classpath:applicationContext-framework-test.xml";

    public static final String CONTEXT_CONFIGURATION = "classpath:applicationContext-test.xml";

    /**
     * @see Action#SUCCESS
     */
    public static final String SUCCESS = Action.SUCCESS;

    /**
     * @see Action#INPUT
     */
    public static final String INPUT = Action.INPUT;

    /**
     * @see Action#ERROR
     */
    public static final String ERROR = Action.ERROR;

    /**
     * @see Action#LOGIN
     */
    public static final String LOGIN = Action.LOGIN;

    /**
     * @see Action#NONE
     */
    public static final String NONE = Action.NONE;

    private static final String DEFAULT_KAISHA_CD = "000";

    private static int sNumberOfValidTestMethods;

    private static int sCallCount;

    private final String kaishaCd;

    private BaseTestContextHelper baseTestContextHelper;

    public BaseTest() {
        this.kaishaCd = DEFAULT_KAISHA_CD;
    }

    public BaseTest(final String aCompanyCode) {
        this.kaishaCd = aCompanyCode;
    }

    /**
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        // buildupSessionContext("", "", new String[] {});

    }

    @BeforeClass
    public static final void setUpBeforeClass() {
        Startup.initializeIfNeeded();

        sNumberOfValidTestMethods = 0;
        sCallCount = 0;
    }

    @Before
    public final void setUp() throws Exception {
        try {
            if (sCallCount == 0) {
                sNumberOfValidTestMethods = countValidTestMethod();
                if (sNumberOfValidTestMethods == 0) {
                    return;
                }
                if (clearCacheBeforeSetUpOnce()) {
                    // CacheUtils.removeCascade(CacheNodeGroup.COMPANIES);
                    // CacheUtils.removeCascade(CacheNodeGroup.COMMON);
                    setUpOnce();
                }
            }

            if (clearCacheBeforeSetUpEachTime()) {
                CacheUtils.removeCascade(CacheNodeGroup.COMPANIES);
                CacheUtils.removeCascade(CacheNodeGroup.COMMON);
            }
            setUpEachTime();
        } finally {
            sCallCount++;
        }
    }

    protected void setUpOnce() throws Exception {
        // do nothing
    }

    protected void setUpEachTime() throws Exception {
        // do nothing
    }

    @After
    public final void tearDown() throws Exception {
        tearDownEachTime();
        if (sCallCount == sNumberOfValidTestMethods) {
            tearDownOnce();
        }
    }

    protected void tearDownOnce() throws Exception {
        // do nothing
    }

    protected boolean clearCacheBeforeSetUpOnce() {
        return true;
    }

    protected void tearDownEachTime() throws Exception {
        // do nothing
    }

    protected boolean clearCacheBeforeSetUpEachTime() {
        return true;
    }

    private int countValidTestMethod() throws Exception {
        Runner testRunner;
        testRunner = new SpringJUnit4ClassRunner(getClass());
        final Description description = testRunner.getDescription();
        return testRunner.testCount() - getIgnoreCount(description);
    }

    private int getIgnoreCount(final Description aDescription) {
        int ignoreCount = 0;
        final List<Description> children = aDescription.getChildren();
        for (final Description child : children) {
            if (child.getAnnotation(Ignore.class) != null) {
                ignoreCount++;
            }
        }
        return ignoreCount;
    }

    public final void buildupSessionContext(final String aUserid, final String aPassword,
            final String[] aAuths) {

        Assert
                .isTrue(
                        this.baseTestContextHelper != null,
                        "applicationContext没有baseTestContextHelper的定义。请定义BaseTestContextHelper。");

        this.baseTestContextHelper.buildupSessionContext(aUserid, this.kaishaCd, aPassword, aAuths);
    }

    /**
     * setter for baseTestContextHelper.
     * 
     * @param aBaseTestContextHelper baseTestContextHelper
     */
    @Resource
    public void setBaseTestContextHelper(final BaseTestContextHelper aBaseTestContextHelper) {
        this.baseTestContextHelper = aBaseTestContextHelper;
    }
}
