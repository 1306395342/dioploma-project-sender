package lyytest.lyy.diploma_project_sender;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;

import java.util.Map;

import lyytest.lyy.diploma_project_sender.Activity.HomePageActivity;
import lyytest.lyy.diploma_project_sender.service.ApiService;
import lyytest.lyy.diploma_project_sender.util.Urls;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText username;
    private EditText password;
    private Button bt_login;
    private TextView error;
    private String usernamet;
    private String passwordt,timestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        error=(TextView)findViewById(R.id.error);
        bt_login=(Button)findViewById(R.id.btn_login);
        bt_login.setOnClickListener(this);
    }

    public void login(String username,String password){
        timestamp =(System.currentTimeMillis())+"";
        //1、创建retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Urls.BASIC_URL2)
                //设置数据解析器
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //2、用retrofit加工出对应的接口实例对象
        ApiService api = retrofit.create(ApiService.class);
        // 3、获取适配转换Call对象
        Call<Map<String,Object>> call=api.postLogin(username,password,timestamp);
        //4、调用call.enqueue方法获取数据
        call.enqueue(new Callback<Map<String,Object>>() {
            @Override
            public void onResponse(Call<Map<String,Object>> call, Response<Map<String,Object>> response) {
                Map<String,Object> res = response.body();
                Double code=(Double)res.get("code");
                if(code==-1){
                    error.setText("用户名或密码错误！");
                }
                else{
                    error.setText("");
                    SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
                    editor.putInt("sIsLogin",1);
                    editor.putInt("sfirstLogin",1);
                    editor.putString("stoken",res.get("stoken").toString());
                    editor.putString("susername",res.get("susername").toString());
                    editor.putString("sphone",res.get("sphone").toString());
                    editor.putInt("sid",new Double((Double) res.get("sid")).intValue());
                    editor.putString("srealname",res.get("srealname").toString());
                    editor.apply();
                    Toast.makeText(getApplicationContext(),"登陆成功",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Map<String,Object>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    //click事件判定
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                usernamet = username.getText().toString();
                passwordt = password.getText().toString();
                login(usernamet,passwordt);
                break;
        }
    }

}
