package com.helper.aviator;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorLong;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sz_qiuhf@163.com
 **/
public class AddFunction extends AbstractFunction {

    public static void main(String[] args) {
        int[] a = {1, 20};
        Map<String, Object> env = new HashMap<>(2);
        env.put("a", a);

        System.out.println("AviatorEvaluator.execute(\"1 + 2 + 3\") = " + AviatorEvaluator.execute("1 + 2 + 3"));
        System.out.println("AviatorEvaluator.execute(\"a[1] + 100\", env) = " + AviatorEvaluator.execute("a[1] + 100", env));
        System.out.println("AviatorEvaluator.execute(\"'a[1]=' + a[1]\", env) = " + AviatorEvaluator.execute("'a[1]=' + a[1]", env));

// 求数组长度
        System.out.println("AviatorEvaluator.execute(\"count(a)\", env) = " + AviatorEvaluator.execute("count(a)", env));

// 求数组总和
        System.out.println("AviatorEvaluator.execute(\"reduce(a, +, 0)\", env) = " + AviatorEvaluator.execute("reduce(a, +, 0)", env));

// 检测数组每个元素都在 0 <= e < 10 之间
        System.out.println("AviatorEvaluator.execute(\"seq.every(a, seq.and(seq.ge(0), seq.lt(10)))\", env) = " + AviatorEvaluator.execute("seq.every(a, seq.and(seq.ge(0), seq.lt(10)))", env));

// Lambda 求和
        System.out.println("AviatorEvaluator.execute(\"reduce(a, lambda(x,y) -> x + y end, 0)\", env) = " + AviatorEvaluator.execute("reduce(a, lambda(x,y) -> x + y end, 0)", env));

// 导入 String 类实例方法作为自定义函数
//        AviatorEvaluator.addInstanceFunctions("s", String.class);
        AviatorEvaluator.execute("string.indexOf('hello', 'l')");
//        AviatorEvaluator.execute("string.replaceAll('hello', 'l', 'x')");

// 导入静态方法作为自定义函数
//        AviatorEvaluator.addStaticFunctions("sutil", StringUtils.class);
//        AviatorEvaluator.execute("sutil.isBlank('hello')");

// 启用基于反射的 function missing 机制，调用任意 public 实例方法，无需导入
//        AviatorEvaluator.setFunctionMissing(JavaMethodReflectionFunctionMissing.getInstance());
// 调用 String#indexOf
//        System.out.println(AviatorEvaluator.execute("indexOf('hello world', 'w')"));
// 调用 Long#floatValue
//        System.out.println(AviatorEvaluator.execute("floatValue(3)"));
// 调用 BigDecimal#add
//        System.out.println(AviatorEvaluator.execute("add(3M, 4M)"));
    }

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        // 编写业务代码
        Number number1 = arg1.numberValue(env);
        Number number2 = arg2.numberValue(env);
        return AviatorLong.valueOf(Math.addExact(number1.longValue(), number2.longValue()));
    }

    @Override
    public String getName() {
        return "AddFunction";
    }
}
