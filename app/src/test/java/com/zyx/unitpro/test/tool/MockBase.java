package com.zyx.unitpro.test.tool;

import com.zyx.unitpro.BuildConfig;

import org.junit.Rule;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

/**
 * Created by zhangyouxuan on 2016/7/26.
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)//,manifest = "src/test/AndroidMainifest.xml"
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
public class MockBase {

    @Rule
    public TestRule testRule = new TestRule(true);

}
