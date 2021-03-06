package cn.wensiqun.asmsupport.issues.y2015.m06.net.oschina.i3;

import cn.wensiqun.asmsupport.client.DummyClass;
import cn.wensiqun.asmsupport.client.DummyMethod;
import cn.wensiqun.asmsupport.client.block.ForEach;
import cn.wensiqun.asmsupport.client.block.IF;
import cn.wensiqun.asmsupport.client.block.MethodBody;
import cn.wensiqun.asmsupport.client.def.var.LocVar;
import cn.wensiqun.asmsupport.client.def.var.Var;
import cn.wensiqun.asmsupport.issues.IssuesConstant;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ContinueMethodTest {
    @Test
    public void test() throws Exception {
        DummyClass dummy = new DummyClass(ContinueMethodTest.class.getPackage().getName() + ".ContinueMethodTest_");
        dummy.public_().setClassOutPutPath(IssuesConstant.classOutPutPath);
        DummyMethod dummyMethod = dummy.newMethod("getString")
                .public_().static_().return_(String.class).argTypes(List.class);

        dummyMethod.body(new MethodBody() {

            @Override
            public void body(LocVar... args) {
            	LocVar strs = args[0];
            	final Var sb = new_(StringBuilder.class).asVar();
            	for_(new ForEach(strs, getType(String.class)) {

					@Override
					public void body(final LocVar str) {
						if_(new IF(str.call("equals", val("World"))) {

							@Override
							public void body() {
								continue_();
							}
							
						});
						sb.call("append", str);
					}
            		
            	});
            	
            	sb.call("append", val("|"));
            	
            	final Var array = strs.call("toArray").asVar();
            	for_(new ForEach(array, getType(String.class)) {

					@Override
					public void body(final LocVar str) {
						if_(new IF(str.call("equals", val("World"))) {

							@Override
							public void body() {
								continue_();
							}
							
						});
						sb.call("append", str);
					}
            		
            	});
            	
            	return_(sb.call("toString"));
            }
        });
        Class<?> clazz = dummy.build();
        Method method = clazz.getMethod("getString", List.class);
        List<String> list = new ArrayList<>();
        list.add("Hello,");
        list.add("World");
        list.add("ASMSupport");
        String value = (String) method.invoke(clazz,  list);
        Assert.assertEquals("Hello,ASMSupport|Hello,ASMSupport", value);
    }
}
