package com.youthen.framework.test;

public class Test extends User implements MyInterface {

    public String str;

    private int a;

    /**
     * コンストラクタ。
     * 
     * @param aAge
     */
    public Test(final int aAge) {
        super(aAge);
    }

    /**
     * @see com.youthen.framework.test.MyInterface#paint()
     */
    @Override
    public void paint() {
    }

    private void test() {
    }
}
