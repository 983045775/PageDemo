package iflytek.pagedemo.http;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target( { java.lang.annotation.ElementType.FIELD })
public @interface ModelProp {
	/**
	 * 是否忽略属性值，如果为true则不参与json拼接和其他字符串的反射
	 * */
	boolean isIgnore() default false;

}