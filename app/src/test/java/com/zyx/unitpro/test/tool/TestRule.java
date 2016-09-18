package com.zyx.unitpro.test.tool;

import android.util.Log;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.powermock.api.support.SafeExceptionRethrower;
import org.powermock.classloading.ClassloaderExecutor;
import org.powermock.core.MockRepository;
import org.powermock.modules.junit4.rule.PowerMockClassloaderExecutor;
import org.powermock.tests.utils.MockPolicyInitializer;
import org.powermock.tests.utils.impl.MockPolicyInitializerImpl;
import org.robolectric.shadows.ShadowLog;

/**
 * Created by zhangyouxuan on 2016/5/23.
 */
public class TestRule implements MethodRule {

    private static ClassloaderExecutor classloaderExecutor;
    private static Class<?> previousTargetClass;
    private static MockPolicyInitializer mockPolicyInitializer;
    private static boolean isPowerMock ;

    public TestRule(boolean isPowerMock){
        this.isPowerMock = isPowerMock;
    }

    @Override
    public Statement apply(final Statement base, final FrameworkMethod method, final Object target) {
       if(isPowerMock){
           if (classloaderExecutor == null || previousTargetClass != target.getClass()) {
               final Class<?> testClass = target.getClass();
               mockPolicyInitializer = new MockPolicyInitializerImpl(testClass);
               classloaderExecutor = PowerMockClassloaderExecutor.forClass(testClass, mockPolicyInitializer);
               previousTargetClass = target.getClass();
           }
           return new PowerMockStatement(base, classloaderExecutor, mockPolicyInitializer, method);
       }else{
           return new Statement() {
               @Override
               public void evaluate() throws Throwable {
                   purePrintLog(base,method,target);
               }
           };
       }
    }

    private void purePrintLog(final Statement base, final FrameworkMethod method, Object target) throws Throwable {
        {
            /***starting(method);***/
            ShadowLog.stream = System.out;
            try {
                base.evaluate();
                /***succeeded(method);***/
            } catch (Throwable t) {
                /***failed(t, method);***/
                try {
                    Log.e("@" + method.getName() + "--->",method.getAnnotation(Purpose.class).desc());
                } catch (NullPointerException e) {

                } finally {

                    throw t;
                }

            } finally {
                /***finished(method);***/
            }
        }
    }


    class PowerMockStatement extends Statement {
        private final Statement fNext;
        private final ClassloaderExecutor classloaderExecutor;
        private final MockPolicyInitializer mockPolicyInitializer;
        private FrameworkMethod method;

        public PowerMockStatement(final Statement base, final ClassloaderExecutor classloaderExecutor, final MockPolicyInitializer mockPolicyInitializer, final FrameworkMethod method) {
            fNext = base;
            this.classloaderExecutor = classloaderExecutor;
            this.mockPolicyInitializer = mockPolicyInitializer;
            this.method = method;
        }

        @Override
        public void evaluate() throws Throwable {
            ShadowLog.stream = System.out;
            classloaderExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        // Re-executes the policy method that might initialize mocks that
                        // were cleared after the previous statement.
                        // This fixes https://github.com/jayway/powermock/issues/581
                        mockPolicyInitializer.refreshPolicies(getClass().getClassLoader());
                        fNext.evaluate();
                    } catch (Throwable t) {
                        try {
                            Log.e("@" + method.getName() + "--->",method.getAnnotation(Purpose.class).desc());
                        } catch (NullPointerException e) {

                        }
                        SafeExceptionRethrower.safeRethrow(t);
                    } finally {
                        // Clear the mock repository after each test
                        MockRepository.clear();
                    }
                }
            });
        }
    }

}
