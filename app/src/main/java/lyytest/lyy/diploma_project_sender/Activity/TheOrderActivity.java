package lyytest.lyy.diploma_project_sender.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import lyytest.lyy.diploma_project_sender.R;
import lyytest.lyy.diploma_project_sender.service.ApiService;
import lyytest.lyy.diploma_project_sender.util.Urls;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TheOrderActivity extends AppCompatActivity {
    private String oid,ostatus;
    private TextView update_time,end_time,ostatust,srealname,sphone,accept_time,oidt,to_where,from_where;
    private Button finish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_order);
        finish = (Button)findViewById(R.id.finish);
        update_time=(TextView)findViewById(R.id.update_time);
        end_time = (TextView)findViewById(R.id.end_time);
        ostatust = (TextView)findViewById(R.id.ostatuso);
        srealname = (TextView)findViewById(R.id.srealname);
        sphone = (TextView)findViewById(R.id.sphone);
        accept_time=(TextView)findViewById(R.id.accept_time);
        oidt = (TextView)findViewById(R.id.oidt);
        to_where=(TextView)findViewById(R.id.to_where) ;
        from_where=(TextView)findViewById(R.id.from_where);

        oid=getIntent().getStringExtra("oid");
        ostatus=getIntent().getStringExtra("ostatus").toString();

        getTheOrderInformation(oid);

        if(Integer.valueOf(ostatus)==0){
            ostatust.setText("状态:已接受,未配送");
        }
        else if(Integer.valueOf(ostatus)==1){
            finish.setVisibility(View.VISIBLE);
            ostatust.setText("状态:已接受,开始配送");
            finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog aldg;
                    AlertDialog.Builder adBd=new AlertDialog.Builder(TheOrderActivity.this);
                    adBd.setTitle("订单"+oid);
                    adBd.setMessage("确定要完成此订单吗？");
                    adBd.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                          finishOrder(oid);
                        }
                    });
                    adBd.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    aldg=adBd.create();
                    aldg.show();
                }
            });
        }
        else if(Integer.valueOf(ostatus)==2){
            ostatust.setText("状态:已完成");
        }
        else if(Integer.valueOf(ostatus)==-1){
            ostatust.setText("状态:已退款");
        }
        else if(Integer.valueOf(ostatus)==-2){
            ostatust.setText("状态:退款申请中");
        }


    }

    private void finishOrder(String oid){
        //1、创建retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Urls.BASIC_URL2)
                //设置数据解析器
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //2、用retrofit加工出对应的接口实例对象
        ApiService api = retrofit.create(ApiService.class);
        // 3、获取适配转换Call对象
        Call<Map<String,Object>> call=api.finishorder(Integer.valueOf(oid));
        //4、调用call.enqueue方法获取数据
        call.enqueue(new Callback<Map<String,Object>>() {
            @Override
            public void onResponse(Call<Map<String,Object>> call, Response<Map<String,Object>> response) {
                Map<String,Object> res = response.body();
                Double code=(Double)res.get("code");
                if(code==1) {
                    Toast.makeText(getApplicationContext(),"此订单已完成",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String,Object>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getTheOrderInformation(String oid){
        //1、创建retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Urls.BASIC_URL2)
                //设置数据解析器
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //2、用retrofit加工出对应的接口实例对象
        ApiService api = retrofit.create(ApiService.class);
        // 3、获取适配转换Call对象
        Call<Map<String,Object>> call=api.postgetTheOrderInformation(Integer.valueOf(oid));
        //4、调用call.enqueue方法获取数据
        call.enqueue(new Callback<Map<String,Object>>() {
            @Override
            public void onResponse(Call<Map<String,Object>> call, Response<Map<String,Object>> response) {
                Map<String,Object> res = response.body();
                String update_timet = res.get("update_time")+"";
                String end_timet = res.get("end_time")+"";
                if(update_timet.equals("-1")){
                    update_timet="仍在接受期间";
                }
                if(end_timet.equals("-1")){
                    end_timet="仍未送到";
                }
                oidt.setText("订单号: "+getIntent().getStringExtra("oid"));
                srealname.setText("收件人名字: "+res.get("urealname"));
                sphone.setText("收件人手机号: "+res.get("uphone"));
                accept_time.setText("申请快递时刻: "+res.get("accept_time"));
                update_time.setText("开始配送时刻: "+update_timet);
                end_time.setText("订单完成时刻: "+end_timet);
                from_where.setText("起始地: "+res.get("from_where"));
                to_where.setText("出发地: "+res.get("to_where"));
                Log.v("lyyyyyy: ",res.toString());
            }

            @Override
            public void onFailure(Call<Map<String,Object>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
