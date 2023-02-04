package com.example.hosptial;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

//DeleteRequest클래스는 특정한 userID를 받아서 이 웹사이트에 "userID"에 userID값을 parameters로 매칭 시킨뒤에 전송하도록 유저삭제가 실행되도록 해주는 소스코딩문
public class DeleteRequest extends StringRequest {

    final static private String URL = "http://cinhwa.cafe24.com//Delete.php";
    private Map<String, String> parameters;

    //생성자 만든다.(위에 주소 웹사이트에 POST방식으로 보내서 요청한다는 뜻이다.)
    public DeleteRequest(String userID, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        //초기화
        parameters = new HashMap<>();
        parameters.put("userID", userID);//"userID"를 실제 userID를 매칭시켜서 전송할수 있다.
    }

    @Override
    public Map<String,String> getParams() {
        return parameters;
    }
}



