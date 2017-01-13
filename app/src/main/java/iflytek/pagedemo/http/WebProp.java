package iflytek.pagedemo.http;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target( { java.lang.annotation.ElementType.FIELD })
public @interface WebProp {
	/**
	 *web模型属性， 支持String int float long 等等类型，
	 * 支持将Jason对象之间转换为对象，
	 * 转换策略是如果存在webprop则依据prop来，如果存在则依照属性名来。
	 * 与属性isSame互斥
	 * @return
	 */
	public abstract String name() default "";

	/**
	 *支持将对象之间转换为RequestParams的属性，转换策略是，存在webprop才转换，不存在不转换
	 *与属性isSame互斥
	 * */
	public abstract String reqname() default "";
	
	
	/**
	 * 是否字段名和json名，RequestParams值一模一样。
	 * 此属性和name, reqname互斥，
	 * 与属性isSame互斥
	 * */
	public abstract boolean isSame() default true;
	
	/**
	 * 是否忽略属性值，如果为true则不参与json拼接和其他字符串的反射
	 * */
	public abstract boolean isIgnore() default false;

}