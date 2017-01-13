package iflytek.pagedemo.model;

import java.io.Serializable;

import iflytek.pagedemo.model.base.BaseModel;

/**
 * Model层  不需要做如何显示页面,也不用控制获取数据,是持有一些数据对象
 */


public class UserInfo extends BaseModel<UserInfo> implements Serializable {


    /**
     * 这里如果你希望字段和json中的不一致还要获取数据,需要用注解 @WebProp(name = "id")
     * 这里如果你希望字段不参与json拼接和其他字符串的反射,需要用注释@ModelProp(isIgnore = true)
     *
     */
    public String id;
    public String name;
}
