package com.cyl.music_hnust.Json;

import android.util.Log;

import com.cyl.music_hnust.bean.Comment;
import com.cyl.music_hnust.bean.Dynamic;
import com.cyl.music_hnust.bean.Location;
import com.cyl.music_hnust.bean.User;
import com.cyl.music_hnust.utils.MusicInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonParsing {
    public static List<MusicInfo> getmusicId(String json) throws JSONException {
        List<MusicInfo> result;
        MusicInfo map;
        int length;

        result = new ArrayList<MusicInfo>();
        JSONObject Jsonobject = new JSONObject(json);


        JSONArray jsonArray = Jsonobject.getJSONArray("song_list");//里面有一个数组数据，可以用getJSONArray获取数组
        if (jsonArray.length() > 8) {
            length = 8;
        } else {
            length = jsonArray.length();
        }
        for (int i = 0; i < length; i++) {
            JSONObject item = jsonArray.getJSONObject(i); // 得到每个对象

            String lrcpath = "http://tingapi.ting.baidu.com";
            String song_id = item.getString("song_id");
            String album_title = item.getString("album_title");
            String author = item.getString("author");
            String Lrcpath = lrcpath+item.getString("lrclink");

            Log.e("JsonParsing", "======" + song_id + album_title + author);
            map = new MusicInfo(); // 存放到MAP里面

            map.setId(song_id);
            map.setArtist(author);
            map.setLrcPath(Lrcpath);
            map.setAlbum(album_title);
            result.add(map);
        }

        return result;


    }

    public static MusicInfo getMusicInfo(String json, MusicInfo songinfo) throws JSONException {

        // TODO Auto-generated method stub
        JSONObject Jsonobject = new JSONObject(json);

        String songLink = Jsonobject.getString("songLink");
        String songName = Jsonobject.getString("songName");
        String artistName = Jsonobject.getString("artistName");
//        String lrcLink = Jsonobject.getString("lrcLink");
        String songPicSmall = Jsonobject.getString("songPicRadio");
        String size = Jsonobject.getString("size");
        String time = Jsonobject.getString("time");

        Log.e("jsonparsing",songLink+songName+artistName+songPicSmall+size+time+"===");


        songinfo.setSize(size);
        songinfo.setPath(songLink);
        songinfo.setAlbumPic(songPicSmall);
        songinfo.setName(songName);
        songinfo.setArtist(artistName);
        songinfo.setTime(time);

        return songinfo;

    }

    /**
     * "cur_user": "1112",
     * "secretDetail": [
     * {
     * "user_id": "5",
     * "secret_id": "1",
     * "secret_content": "感觉很诚恳 是好事不需要发誓 那么幼稚 本以为可以 就这样随你反正我也无处可去我怕太负责任的人因为他随时会牺牲爱不爱都可以 我怎样都依你连借口 我都帮你寻与其在你不要的世界里不如痛快把你忘记这道理谁都懂 说容易 爱透了还要嘴硬我宁愿 留在你方圆几里",
     * "secret_time": "2016-03-17 23:25:00",
     * "secret_agreeNum": "0",
     * "secret_replyNum": "0",
     * "secret_status": "0",
     * "report_num": "0",
     * "user_name": "张三",
     * "user_img": "http://hcyl.sinaapp."
     * "class": "detail-0",
     * "isAgree": 0
     * },
     */
    public static List<Dynamic> getDynamic(JSONObject Jsonobject) throws JSONException {
        List<Dynamic> result;

        result = new ArrayList<>();


        JSONArray secretDetail = Jsonobject.getJSONArray("secretDetail");//里面有一个数组数据，可以用getJSONArray获取数组
        for (int i = 0; i < secretDetail.length(); i++) {
            JSONObject item = secretDetail.getJSONObject(i); // 得到每个对象

            String secret_id = item.getString("secret_id");
            String secret_content = item.getString("secret_content");
            String secret_time = item.getString("secret_time");
            String secret_agreeNum = item.getString("secret_agreeNum");
            String secret_replyNum = item.getString("secret_replyNum");
            int isAgree =item.getInt("isAgree");
            String user_id = item.getString("user_id");
            String user_name = item.getString("user_name");
            String user_img = item.getString("user_img");


            User user1 = new User();
            user1.setUser_id(user_id);
            user1.setUser_name(user_name);
            user1.setUser_img(user_img);


            Dynamic dynamic = new Dynamic();
            dynamic.setUser(user1);
            dynamic.setDynamic_id(secret_id);
            dynamic.setContent(secret_content);
            dynamic.setTime(secret_time);
            dynamic.setComment(Integer.parseInt(secret_replyNum));
            dynamic.setLove(Integer.parseInt(secret_agreeNum));
            if ( isAgree ==1){
                dynamic.setMyLove(true);
            }else {
                dynamic.setMyLove(false);
            }


            result.add(dynamic);
        }
        return result;
    }
    public static List<Location> getLocation(JSONObject Jsonobject) throws JSONException {
        List<Location> result;

        result = new ArrayList<>();


        JSONArray nearLocation = Jsonobject.getJSONArray("nearLocation");//里面有一个数组数据，可以用getJSONArray获取数组
        for (int i = 0; i < nearLocation.length(); i++) {
            JSONObject item = nearLocation.getJSONObject(i); // 得到每个对象

            String user_song = item.getString("user_song");
            String location_time = item.getString("location_time");
            double location_longitude = item.getDouble("location_longitude");
            double location_latitude = item.getDouble("location_latitude");
            JSONArray user = item.getJSONArray("user");
            JSONObject itemuser = user.getJSONObject(0);
            String user_id = itemuser.getString("user_id");
            String user_name = itemuser.getString("user_name");
            String user_img = itemuser.getString("user_img");
            String signature = itemuser.getString("signature");
            String user_email = itemuser.getString("user_email");

            Log.e("位置","location_longitude:"+location_longitude+"location_latitude"+location_latitude);
            User user1 =new User();
            user1.setUser_img(user_img);
            user1.setUser_email(user_email);
            user1.setSignature(signature);
            user1.setUser_id(user_id);
            user1.setUser_name(user_name);


            Location location = new Location();
            location.setUser(user1);
            location.setLocation_time(location_time);
            location.setLocation_latitude(location_latitude);
            location.setLocation_longitude(location_longitude);
            location.setUser_song(user_song);


            result.add(location);
        }
        return result;
    }

    /**
     *  "comment": [
     {
     "user_id": "10",
     "secret_id": "5",
     "comment": "假如没如果",
     "comment_time": "2016-03-18 12:51:54",
     "img_id": null,
     "user_name": "张三"
     }
     ],
      * @param Jsonobject
     * @return
     * @throws JSONException
     */
    public static List<Comment> getComment(JSONObject Jsonobject) throws JSONException {
        List<Comment> result;

        result = new ArrayList<>();

        JSONArray comment = Jsonobject.getJSONArray("comment");//里面有一个数组数据，可以用getJSONArray获取数组
        for (int i = 0; i < comment.length(); i++) {
            JSONObject item = comment.getJSONObject(i); // 得到每个对象

            String user_name = item.getString("user_name");
            String comment_time = item.getString("comment_time");
            String comment1 = item.getString("comment");

            Comment comment2 = new Comment();
            comment2.setTime(comment_time);
            comment2.setCommentContent(comment1);
            User user =new User();
            user.setUser_name(user_name);
            comment2.setUser(user);

            result.add(comment2);
        }
        return result;
    }
    public static User Userinfo(String json) {

        User user =new User();
        /**
         * 得到json数据
         *{
         "status": "success",
         "data": {
         "num": "1305030212",
         "name": "蔡永龙",
         "sex": "男",
         "college": "计算机科学与工程学院",
         "major": "信息安全",
         "class"
         }
         }
         */

        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            Log.e("status", status);
            //解析数组
            JSONObject item = jsonObject.getJSONObject("data");

            String num = item.getString("num");    //获取对象中的一个值
            String name = item.getString("name");    //获取对象中的一个值
            String sex = item.getString("sex");    //获取对象中的一个值
            String college = item.getString("college");    //获取对象中的一个值
            String major = item.getString("major");    //获取对象中的一个值
            String class1 = item.getString("class");    //获取对象中的一个值

            user.setUser_id(num);
            user.setUser_name(name);
            user.setUser_sex(sex);
            user.setUser_college(college);
            user.setUser_major(major);
            user.setUser_class(class1);


        } catch (JSONException e) {
            e.printStackTrace();
            return user;
        }
        return user;
    }
    public static String getUserimg(String json) {

        String imgurl = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("userinfo");
            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
            imgurl = jsonObject1.getString("user_img");    //获取对象中的一个值

        } catch (JSONException e) {
            e.printStackTrace();

            return imgurl;
        }
        return imgurl;
    }



}
