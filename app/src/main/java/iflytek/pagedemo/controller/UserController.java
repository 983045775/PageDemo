package iflytek.pagedemo.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import iflytek.pagedemo.model.UserInfo;

/**
 * Controller层  不需要做如何显示页面,只是控制获取数据
 */

public class UserController {

    private static UserController instance;

    private UserController() {
    }

    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }

    public void getContentDatas(Responseback callback) {

        /**假设网络获取到的json*/
        String json = "{\"list\":[{\"id\":\"1\",\"name\":\"张一\"},{\"id\":\"2\",\"name\":\"张二\"}," +
                "{\"id\":\"3\",\"name\":\"张三\"},{\"id\":\"4\",\"name\":\"张四\"},{\"id\":\"5\"," +
                "\"name\":\"张五\"},{\"id\":\"6\",\"name\":\"张六\"},{\"id\":\"7\",\"name\":\"张七\"}]}";

        List<UserInfo> list = parseUsersFromWS(json);
        //返回成功数据
        callback.onSuccess(list);
    }

    //数据回调
    public interface Responseback {
        void onSuccess(List<UserInfo> resultList);
    }

    /**
     * 解析json
     */
    public static ArrayList<UserInfo> parseUsersFromWS(String jsonstr) {
        ArrayList<UserInfo> userList = new ArrayList<>();
        UserInfo info;
        JSONObject jsonObj, json;
        try {
            jsonObj = new JSONObject(jsonstr);
            JSONArray mlist = jsonObj.optJSONArray("list");
            for (int i = 0; i < mlist.length(); i++) {
                json = mlist.getJSONObject(i);
                info = new UserInfo();
                info.name = json.getString("name");
                info.id = json.getString("id");
                userList.add(info);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userList;
    }

}
