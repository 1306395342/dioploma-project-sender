package lyytest.lyy.diploma_project_sender.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lyytest.lyy.diploma_project_sender.MainActivity;
import lyytest.lyy.diploma_project_sender.R;
import lyytest.lyy.diploma_project_sender.service.ApiService;
import lyytest.lyy.diploma_project_sender.util.Urls;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageButton personalinfo,order,queryorder;
    private TextView welcomeuser;
    private SharedPreferences sp;
    private Intent intent;
    private LocationClient mlc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mlc=new LocationClient(this);
        mlc.registerLocationListener(new MyLocationListener());


        setContentView(R.layout.activity_home_page);
        personalinfo = (ImageButton)findViewById(R.id.userinfo);
        order = (ImageButton)findViewById(R.id.order);
        welcomeuser = (TextView)findViewById(R.id.welcomeuser);
        queryorder=(ImageButton)findViewById(R.id.queryorder);

        sp = getSharedPreferences("data",MODE_PRIVATE);
        welcomeuser.setText("欢迎您 "+sp.getString("susername","")+" Kitty物流快递员");
        personalinfo.setOnClickListener(this);
        order.setOnClickListener(this);
        queryorder.setOnClickListener(this);

        //权限问题
        List<String> permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(HomePageActivity.this, Manifest.
                permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(HomePageActivity.this, Manifest
                .permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(HomePageActivity.this, Manifest
                .permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!permissionList.isEmpty()){
            String [] permissions =permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(HomePageActivity.this,permissions,1);
        }else{
            requestLocation();
        }

    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.userinfo:
                intent = new Intent(HomePageActivity.this, PersonalInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.order:
                intent = new Intent(HomePageActivity.this, OrderActivity.class);
                startActivity(intent);
                break;
            case R.id.queryorder:
                intent = new Intent(HomePageActivity.this, SearchOrderByIdActivity.class);
                startActivity(intent);
                break;
        }

    }

    private void requestLocation(){
        initLocation();
        mlc.start();;
    }
    public void initLocation(){
        LocationClientOption locationClientOption =new LocationClientOption();
        locationClientOption.setScanSpan(6000);//5s一次
        locationClientOption.setIsNeedAddress(true);
        mlc.setLocOption(locationClientOption);
    }

    public void onRequestPermissionsResult(int requestCode,  String[] permissions, int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0){
                    for (int result:grantResults){
                        if (result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this, "必须统一所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                }else
                {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            StringBuilder currentPosition = new StringBuilder();
            String longitude = bdLocation.getLongitude()+"";
            String latitude = bdLocation.getLatitude()+"";

            currentPosition.append(bdLocation.getCountry());
            currentPosition.append(bdLocation.getProvince());
            currentPosition.append(bdLocation.getCity());
            currentPosition.append(bdLocation.getDistrict());
            currentPosition.append(bdLocation.getStreet());

           saveLocation(longitude,latitude,currentPosition.toString(),sp.getInt("sid",-1));

        }

    }
  private void saveLocation(final String longitude, final String latitude, final String location, int sid){
      //1、创建retrofit
      Retrofit retrofit = new Retrofit.Builder()
              .baseUrl(Urls.BASIC_URL2)
              //设置数据解析器
              .addConverterFactory(GsonConverterFactory.create())
              .build();
      //2、用retrofit加工出对应的接口实例对象
      ApiService api = retrofit.create(ApiService.class);
      // 3、获取适配转换Call对象
      Call<Map<String,Object>> call=api.postsaveLocation(longitude,latitude,location,sid);
      //4、调用call.enqueue方法获取数据
      call.enqueue(new Callback<Map<String,Object>>() {
          @Override
          public void onResponse(Call<Map<String,Object>> call, Response<Map<String,Object>> response) {
              Toast.makeText(getApplicationContext(),"获取地址成功/"+"经度: "+longitude+" 纬度: "+latitude+" 位置: "+location,Toast.LENGTH_SHORT).show();
          }

          @Override
          public void onFailure(Call<Map<String,Object>> call, Throwable t) {
              Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
          }
      });
  }
    protected void onPause() {
        super.onPause();
        mlc.stop();
    }
    protected  void onDestroy(){
        super.onDestroy();
        mlc.stop();
    }
    protected  void onStop() {
        super.onStop();
        mlc.stop();
    }
    protected void onRestart(){
        super.onRestart();
        mlc.start();
    }
}
