package com.zyx.unitpro.test.tool;

import com.zyx.unitpro.BuildConfig;

import org.junit.Rule;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

/**
 * Created by zhangyouxuan on 2016/7/26.
 */

//@RunWith(PowerMockRunner.class)
@RunWith(RobolectricGradleTestRunner.class)
//@PowerMockRunnerDelegate(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)//,application = TestMusicApplication.class
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
public class RoboBase {

    @Rule
    public TestRule testRule = new TestRule(false);

}
