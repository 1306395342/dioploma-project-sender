package lyytest.lyy.diploma_project_sender.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class SearchOrderByIdActivity extends AppCompatActivity {
    private EditText searchOrderOid;
    private Button btn_searchorder;
    private TextView error;
    private String oid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_order_by_id);
        searchOrderOid=(EditText)findViewById(R.id.searchorderid);
        btn_searchorder=(Button)findViewById(R.id.btn_searchorder);
        error = (TextView)findViewById(R.id.errorsearchoid);


        btn_searchorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oid = searchOrderOid.getText().toString();
                if(oid.equals("")){error.setText("请输入订单号");}
                else{error.setText("");
                    showorderbyid(Integer.valueOf(oid));}
            }
        });

    }
    private void showorderbyid(int oid){
        //1、创建retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Urls.BASIC_URL2)
                //设置数据解析器
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //2、用retrofit加工出对应的接口实例对象
        ApiService api = retrofit.create(ApiService.class);
        // 3、获取适配转换Call对象
        Call<Map<String,Object>> call=api.postgetTheOrderOstatus(oid);
        //4、调用call.enqueue方法获取数据
        call.enqueue(new Callback<Map<String,Object>>() {
            @Override
            public void onResponse(Call<Map<String,Object>> call, Response<Map<String,Object>> response) {
                Map<String,Object> res = response.body();
                Double code=(Double)res.get("code");

                if(code==-1){error.setText("请输入订单号");}
                else if(code==-2){error.setText("订单号不存在");}
                else{
                    error.setText("");
                    Intent intent = new Intent(SearchOrderByIdActivity.this, TheOrderActivity.class);
                    intent.putExtra("oid",searchOrderOid.getText().toString());
                    intent.putExtra("ostatus",new Double((Double)res.get("ostatus")).intValue()+"");
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Map<String,Object>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
