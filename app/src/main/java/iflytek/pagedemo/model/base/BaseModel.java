package iflytek.pagedemo.model.base;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import iflytek.pagedemo.http.ModelProp;
import iflytek.pagedemo.http.RequestParams;
import iflytek.pagedemo.http.WebProp;

import static android.text.TextUtils.isEmpty;


/**
 * 模型基类
 *
 */

public class BaseModel<T> {
    public Class<T> modelClass;
    protected List<Field> allFields;

    @SuppressWarnings("unchecked")
    public BaseModel() {
        this.modelClass = ((Class<T>) ((ParameterizedType) this
                .getClass().getGenericSuperclass()).getActualTypeArguments()[0]);

        this.allFields = Arrays.asList(modelClass.getDeclaredFields());

    }

    /**
     * 将对象类型转换为网络请求参数的类型，需要WebProp注解 转换策略是，存在webprop才转换，不存在不转换
     */
    public RequestParams getRequestParams() {
        RequestParams params = new RequestParams();
        String u = "";
        for (Field field : this.allFields) {
            WebProp webprop = null;
            if (field.isAnnotationPresent(WebProp.class)) {

                try {
                    webprop = (WebProp) field.getAnnotation(WebProp.class);
                    if (webprop != null && webprop.isIgnore())
                        continue;
                    field.setAccessible(true);
                    String fieldValue = String.valueOf(field.get(this));
                    if (webprop.isSame() && isEmpty(webprop.reqname()))
                        params.put(field.getName(), fieldValue);
                    else
                        params.put(webprop.reqname(), fieldValue);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }

        return params;
        // return u;
    }

    /**
     * 将json对象的字段信息赋值给当前对象,目前 支持int,string类型,如果没有写WebProp 就默认字段名称为获取json的key
     * 如果存在webprop则依据prop来，如果存在则依照属性名来。 add by ljm
     */
    @SuppressWarnings("unchecked")
    public void setValueWithJson(JSONObject obj) {
        for (Field field : this.allFields) {
            WebProp webprop = null;
            ModelProp mp = null;
            try {
                webprop = (WebProp) field.getAnnotation(WebProp.class);
                mp = (ModelProp) field.getAnnotation(ModelProp.class);
                String objkey = field.getName();
                if (webprop != null) {
                    if (!webprop.isSame() || !TextUtils.isEmpty(webprop.name())) {
                        objkey = webprop.name();
                    }
                }

                if (mp != null && mp.isIgnore()) {
                    continue;
                }

                field.setAccessible(true);
                Class<?> t = field.getType();
                if (t == Integer.TYPE || t == Integer.class) {
                    field.set(this, obj.optInt(objkey));
                } else if (t == String.class) {
                    field.set(this, obj.optString(objkey));
                } else if (t == Boolean.TYPE || t == Boolean.class) {
                    field.set(this, obj.optBoolean(objkey));
                } else if (t.getSuperclass() == Enum.class) {
                    Class<Enum> t2 = (Class<Enum>) t;
                    field.set(this, Enum.valueOf(t2, obj.optString(objkey)));
                }
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }

    }

    /**
     * 将当前对象转换为json对象
     */
    public JSONObject getJsonObject() {
        JSONObject json = new JSONObject();
        for (Field field : this.allFields) {
            if (Modifier.isStatic(field.getModifiers()) && isIgnoreStatic()) {
                continue;
            }

            WebProp webprop = null;
            ModelProp mp = null;
            try {
                webprop = (WebProp) field.getAnnotation(WebProp.class);
                mp = (ModelProp) field.getAnnotation(ModelProp.class);
                String objkey = field.getName();
                if (webprop != null) {
                    if (webprop.isIgnore())
                        continue;
                    if (!webprop.isSame() || !TextUtils.isEmpty(webprop.name())) {
                        objkey = webprop.name();
                    }
                }

                if (mp != null && mp.isIgnore()) {
                    continue;
                }

                field.setAccessible(true);
                Class<?> t = field.getType();
                if (t == Integer.TYPE || t == Integer.class) {
                    json.put(objkey, field.getInt(this));
                } else if (t == String.class) {
                    json.put(objkey, field.get(this).toString());
                } else if (t == Boolean.TYPE || t == Boolean.class) {
                    json.put(objkey, field.getBoolean(this));
                } else if (t.getSuperclass() == Enum.class) {
                    json.put(objkey, field.get(this).toString());
                }

                if (List.class.isAssignableFrom(t)) {
                    //类型只支持List<BaseModel>
                    Type type = field.getGenericType();
                    if (type instanceof ParameterizedType) {
                        Class clazz = (Class) ((ParameterizedType) type).getActualTypeArguments()
                                [0];
                        if (clazz.getSuperclass() == BaseModel.class) {
                            // 遍历 对象，
                            List<BaseModel<?>> mo = (List<BaseModel<?>>) field.get(this);
                            JSONArray jsonArray = new JSONArray();
                            for (BaseModel<?> b : mo) {
                                jsonArray.put(b.getJsonObject());
                            }
                            json.put(objkey, jsonArray);
                        }
                    }

                }
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
        return json;
    }

    public boolean isIgnoreStatic() {
        return true;
    }

    /**
     * 对象序列化为字符串
     */
    public String serialize() {
        try {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    byteArrayOutputStream);
            objectOutputStream.writeObject(this);
            String serStr = byteArrayOutputStream.toString("ISO-8859-1");// 必须是ISO-8859-1
            serStr = java.net.URLEncoder.encode(serStr, "UTF-8");// 编码后字符串不是乱码（不编也不影响功能）
            objectOutputStream.close();
            byteArrayOutputStream.close();
            return serStr;

        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 字符串 反序列化为 对象
     */
    public static Object unSerialize(String serStr) {
        try {
            String redStr = java.net.URLDecoder.decode(serStr, "UTF-8");
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                    redStr.getBytes("ISO-8859-1"));
            ObjectInputStream objectInputStream = new ObjectInputStream(
                    byteArrayInputStream);
            Object obj = objectInputStream.readObject();
            objectInputStream.close();
            byteArrayInputStream.close();
            return obj;

        } catch (Exception e) {
            return null;
        }

    }
}
