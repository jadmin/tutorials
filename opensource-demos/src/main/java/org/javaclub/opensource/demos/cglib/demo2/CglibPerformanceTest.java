package org.javaclub.opensource.demos.cglib.demo2;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;

import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.beans.BeanMap;
import net.sf.cglib.beans.BulkBean;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;


public class CglibPerformanceTest {

    private static final DecimalFormat integerFormat = new DecimalFormat("#,###");

    public static void main(String args[]) {
        // testCopy();
        testReflectCopy();
    }

    public static void testCopy() {
        final int testCount = 1000 * 1000;
        CopyBean bean = getBean();

        // beanCopier测试
        final BeanCopier beanCopier = BeanCopier.create(CopyBean.class, CopyBean.class, false);
        final CopyBean beanCopierTarget = new CopyBean();
        testTemplate(new TestCallback() {

            public String getName() {
                return "BeanCopier";
            }

            public CopyBean call(CopyBean source) {
                beanCopier.copy(source, beanCopierTarget, null);
                return beanCopierTarget;
            }
        }, bean, testCount);
        // PropertyUtils测试
        final CopyBean propertyUtilsTarget = new CopyBean();
        testTemplate(new TestCallback() {

            public String getName() {
                return "PropertyUtils";
            }

            public CopyBean call(CopyBean source) {
                try {
                    PropertyUtils.copyProperties(propertyUtilsTarget, source);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return propertyUtilsTarget;
            }

        }, bean, testCount);

        // BeanUtils测试
        final CopyBean beanUtilsTarget = new CopyBean();
        testTemplate(new TestCallback() {

            public String getName() {
                return "BeanUtils";
            }

            public CopyBean call(CopyBean source) {
                try {
                    BeanUtils.copyProperties(beanUtilsTarget, source);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return beanUtilsTarget;
            }

        }, bean, testCount);

    }

    /**
     * 测试方法：针对copyBean的一个属性，进行getter,setter方法的调用，完成属性的拷贝
     * <ul>
     * <li>BulkBean</li>
     * <li>BeanMap</li>
     * <li>FastClass/FastMethod</li>
     * <li>未处理的reflect</li>
     * <li>处理的reflect</li>
     * </ul>
     */
    public static void testReflectCopy() {
        CopyBean bean = getBean();
        final int testCount = 1000 * 1000 * 100;
        final String fieldName = "integerValue";
        final String getMethodName = "getIntegerValue";
        final String setMethodName = "setIntegerValue";

        // 测试BulkBean
        final BulkBean bulkBean = BulkBean.create(bean.getClass(), new String[] { getMethodName },
                                                  new String[] { setMethodName }, new Class[] { Integer.class });
        final CopyBean bulkBeanTarget = new CopyBean();
        testTemplate(new TestCallback() {

            @Override
            public String getName() {
                return "BulkBean";
            }

            @Override
            public CopyBean call(CopyBean source) {
                Object[] result = bulkBean.getPropertyValues(source); // 先调用getter
                bulkBean.setPropertyValues(bulkBeanTarget, result); // 再调用setter
                return bulkBeanTarget;
            }

        }, bean, testCount);

        // 测试BeanMap
        final BeanMap sourceMap = BeanMap.create(bean); // 预先创建对象
        final BeanMap targetMap = BeanMap.create(new CopyBean());
        final CopyBean beanMapTarget = new CopyBean();
        testTemplate(new TestCallback() {

            @Override
            public String getName() {
                return "BeanMap";
            }

            @Override
            public CopyBean call(CopyBean source) {
                targetMap.setBean(beanMapTarget); // 将目标对象设置于beanMap
                Object obj = sourceMap.get(fieldName);
                targetMap.put(fieldName, obj);
                return beanMapTarget;
            }

        }, bean, testCount);

        // 测试FastClass
        final FastClass fastClass = FastClass.create(bean.getClass());
        final FastMethod setFastMetod = fastClass.getMethod(setMethodName, new Class[] { Integer.class });
        final FastMethod getFastMetod = fastClass.getMethod(getMethodName, new Class[] {});
        final CopyBean fastClassTarget = new CopyBean();
        testTemplate(new TestCallback() {

            @Override
            public String getName() {
                return "FastClass";
            }

            @Override
            public CopyBean call(CopyBean source) {

                try {
                    Object field = getFastMetod.invoke(source, new Object[] {});// 调用get方法
                    setFastMetod.invoke(fastClassTarget, new Object[] { field });// 调用set方法赋值
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return fastClassTarget;
            }

        }, bean, testCount);

        try {
            // 进行method对象cache，真实应用中一般都会cache method对象
            final Method getMethod = bean.getClass().getMethod(getMethodName, new Class[] {});
            final Method setMethod = bean.getClass().getMethod(setMethodName, new Class[] { Integer.class });
            // 测试未处理过的Reflect
            final CopyBean reflect1Target = new CopyBean();
            testTemplate(new TestCallback() {

                @Override
                public String getName() {
                    return "未处理过的Reflect";
                }

                @Override
                public CopyBean call(CopyBean source) {
                    try {
                        Object field = getMethod.invoke(source, new Object[] {});
                        setMethod.invoke(reflect1Target, new Object[] { field });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return reflect1Target;
                }

            }, bean, testCount);

            // 测试处理过的Reflect
            getMethod.setAccessible(true);// 设置不进行access权限检查
            setMethod.setAccessible(true);// 设置不进行access权限检查
            final CopyBean reflect2Target = new CopyBean();
            testTemplate(new TestCallback() {

                @Override
                public String getName() {
                    return "处理过的Reflect";
                }

                @Override
                public CopyBean call(CopyBean source) {
                    try {
                        Object field = getMethod.invoke(source, new Object[] {});
                        setMethod.invoke(reflect2Target, new Object[] { field });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return reflect2Target;
                }

            }, bean, testCount);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

    public static void testTemplate(TestCallback callback, CopyBean source, int count) {
        int warmup = 10;
        // 先进行预热，加载一些类，避免影响测试
        for (int i = 0; i < warmup; i++) {
            callback.call(source);
        }
        restoreJvm(); // 进行GC回收
        // 进行测试
        long start = System.nanoTime();
        for (int i = 0; i < count; i++) {
            callback.call(source);
        }
        long nscost = (System.nanoTime() - start);
        System.out.println(callback.getName() + " total cost=" + integerFormat.format(nscost) + "ns , each cost="
                           + nscost / count + "ns");
        restoreJvm();// 进行GC回收

    }

    private static CopyBean getBean() {
        CopyBean bean = new CopyBean();
        bean.setIntValue(1);
        bean.setBoolValue(false);
        bean.setFloatValue(1.0f);
        bean.setDoubleValue(1.0d);
        bean.setLongValue(1l);
        bean.setCharValue('a');
        bean.setShortValue((short) 1);
        bean.setByteValue((byte) 1);
        bean.setIntegerValue(new Integer("1"));
        bean.setBoolObjValue(new Boolean("false"));
        bean.setFloatObjValue(new Float("1.0"));
        bean.setDoubleObjValue(new Double("1.0"));
        bean.setLongObjValue(new Long("1"));
        bean.setShortObjValue(new Short("1"));
        bean.setByteObjValue(new Byte("1"));
        bean.setBigIntegerValue(new BigInteger("1"));
        bean.setBigDecimalValue(new BigDecimal("1"));
        bean.setStringValue("1");
        return bean;
    }

    private static void restoreJvm() {
        int maxRestoreJvmLoops = 10;
        long memUsedPrev = memoryUsed();
        for (int i = 0; i < maxRestoreJvmLoops; i++) {
            System.runFinalization();
            System.gc();

            long memUsedNow = memoryUsed();
            // break early if have no more finalization and get constant mem used
            if ((ManagementFactory.getMemoryMXBean().getObjectPendingFinalizationCount() == 0)
                && (memUsedNow >= memUsedPrev)) {
                break;
            } else {
                memUsedPrev = memUsedNow;
            }
        }
    }

    private static long memoryUsed() {
        Runtime rt = Runtime.getRuntime();
        return rt.totalMemory() - rt.freeMemory();
    }

    public static class CopyBean {

        private int        intValue;
        private boolean    boolValue;
        private float      floatValue;
        private double     doubleValue;
        private long       longValue;
        private char       charValue;
        private byte       byteValue;
        private short      shortValue;
        private Integer    integerValue;
        private Boolean    boolObjValue;
        private Float      floatObjValue;
        private Double     doubleObjValue;
        private Long       longObjValue;
        private Short      shortObjValue;
        private Byte       byteObjValue;
        private BigInteger bigIntegerValue;
        private BigDecimal bigDecimalValue;
        private String     stringValue;

        public int getIntValue() {
            return intValue;
        }

        public void setIntValue(int intValue) {
            this.intValue = intValue;
        }

        public float getFloatValue() {
            return floatValue;
        }

        public void setFloatValue(float floatValue) {
            this.floatValue = floatValue;
        }

        public double getDoubleValue() {
            return doubleValue;
        }

        public void setDoubleValue(double doubleValue) {
            this.doubleValue = doubleValue;
        }

        public long getLongValue() {
            return longValue;
        }

        public void setLongValue(long longValue) {
            this.longValue = longValue;
        }

        public char getCharValue() {
            return charValue;
        }

        public void setCharValue(char charValue) {
            this.charValue = charValue;
        }

        public byte getByteValue() {
            return byteValue;
        }

        public void setByteValue(byte byteValue) {
            this.byteValue = byteValue;
        }

        public short getShortValue() {
            return shortValue;
        }

        public void setShortValue(short shortValue) {
            this.shortValue = shortValue;
        }

        public Integer getIntegerValue() {
            return integerValue;
        }

        public void setIntegerValue(Integer integerValue) {
            this.integerValue = integerValue;
        }

        public Float getFloatObjValue() {
            return floatObjValue;
        }

        public void setFloatObjValue(Float floatObjValue) {
            this.floatObjValue = floatObjValue;
        }

        public Double getDoubleObjValue() {
            return doubleObjValue;
        }

        public void setDoubleObjValue(Double doubleObjValue) {
            this.doubleObjValue = doubleObjValue;
        }

        public Long getLongObjValue() {
            return longObjValue;
        }

        public void setLongObjValue(Long longObjValue) {
            this.longObjValue = longObjValue;
        }

        public Short getShortObjValue() {
            return shortObjValue;
        }

        public void setShortObjValue(Short shortObjValue) {
            this.shortObjValue = shortObjValue;
        }

        public Byte getByteObjValue() {
            return byteObjValue;
        }

        public void setByteObjValue(Byte byteObjValue) {
            this.byteObjValue = byteObjValue;
        }

        public boolean isBoolValue() {
            return boolValue;
        }

        public void setBoolValue(boolean boolValue) {
            this.boolValue = boolValue;
        }

        public Boolean getBoolObjValue() {
            return boolObjValue;
        }

        public void setBoolObjValue(Boolean boolObjValue) {
            this.boolObjValue = boolObjValue;
        }

        public BigInteger getBigIntegerValue() {
            return bigIntegerValue;
        }

        public void setBigIntegerValue(BigInteger bigIntegerValue) {
            this.bigIntegerValue = bigIntegerValue;
        }

        public BigDecimal getBigDecimalValue() {
            return bigDecimalValue;
        }

        public void setBigDecimalValue(BigDecimal bigDecimalValue) {
            this.bigDecimalValue = bigDecimalValue;
        }

        public String getStringValue() {
            return stringValue;
        }

        public void setStringValue(String stringValue) {
            this.stringValue = stringValue;
        }
    }
}

interface TestCallback {

    String getName();

    CglibPerformanceTest.CopyBean call(CglibPerformanceTest.CopyBean source);
}
