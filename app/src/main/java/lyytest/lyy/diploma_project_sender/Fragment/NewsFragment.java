package lyytest.lyy.diploma_project_sender.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lyytest.lyy.diploma_project_sender.Activity.TheOrderActivity;
import lyytest.lyy.diploma_project_sender.R;
import lyytest.lyy.diploma_project_sender.adapter.ContactInfo;
import lyytest.lyy.diploma_project_sender.adapter.MyAdapter;
import lyytest.lyy.diploma_project_sender.service.ApiService;
import lyytest.lyy.diploma_project_sender.util.Urls;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class NewsFragment extends Fragment {
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    List<ContactInfo> mList = new ArrayList<>();
    private SharedPreferences sp;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_layout,container,false);
        recyclerView =view.findViewById(R.id.card_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new MyAdapter(mList);
        //为RecyclerView对象指定我们创建得到的layoutManager
        recyclerView.setLayoutManager(layoutManager);

        //发请求,初始化mList
        sp = getActivity().getSharedPreferences("data",MODE_PRIVATE);
        showorder(sp.getInt("sid",-1),0);


        //点击卡片事件
        adapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView ordernum =(TextView) view.findViewById(R.id.title);
                String ordernumt = ordernum.getText().toString().trim().replaceAll("\\D+","");
               Intent  intent = new Intent(getActivity(), TheOrderActivity.class);
               intent.putExtra("oid",ordernumt);
               intent.putExtra("ostatus","0");
                startActivity(intent);
            }
        });

        return view;
    }

    private void  showorder(int sid,int orderstatus){
        //1、创建retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Urls.BASIC_URL2)
                //设置数据解析器
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //2、用retrofit加工出对应的接口实例对象
        ApiService api = retrofit.create(ApiService.class);
        // 3、获取适配转换Call对象
        Call< List<Map<String,Object>>> call=api.postShowSenderOrder(orderstatus,sid);
        //4、调用call.enqueue方法获取数据
        call.enqueue(new Callback<List<Map<String,Object>>>() {
            @Override
            public void onResponse(Call< List<Map<String,Object>>> call, Response< List<Map<String,Object>>> response) {
                List<Map<String,Object>> res = response.body();
                Log.v("lyyyyyy0",res.toString().toString());
                mList.clear();
                for(int i=0;i<res.size();i++){
                    ContactInfo element=new ContactInfo(new Double((Double)res.get(i).get("oid")).intValue()+""
                            , res.get(i).get("urealname")+"", res.get(i).get("uphone")+"",
                            res.get(i).get("to_where")+"");
                    mList.add(element);
                }

                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call< List<Map<String,Object>>> call, Throwable t) {
                Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }


}
