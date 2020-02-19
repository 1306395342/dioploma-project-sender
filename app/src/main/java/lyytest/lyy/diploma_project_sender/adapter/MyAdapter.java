package lyytest.lyy.diploma_project_sender.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import lyytest.lyy.diploma_project_sender.R;

import static lyytest.lyy.diploma_project_sender.adapter.ContactInfo.EMAIL_PREFIX;
import static lyytest.lyy.diploma_project_sender.adapter.ContactInfo.NAME_PREFIX;
import static lyytest.lyy.diploma_project_sender.adapter.ContactInfo.SURNAME_PREFIX;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ContactViewHolder> {

    //MyAdapter的成员变量contactInfoList, 这里被我们用作数据的来源
    private List<ContactInfo> contactInfoList;

    //MyAdapter的构造器
    public MyAdapter(List<ContactInfo> contactInfoList) {
        this.contactInfoList = contactInfoList;
    }

    //重写3个抽象方法
//onCreateViewHolder()方法 返回我们自定义的 ContactViewHolder对象
    @Override
    public ContactViewHolder onCreateViewHolder
    (ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.card_view, parent, false);
        return new ContactViewHolder(itemView);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder holder, int position) {

//contactInfoList中包含的都是ContactInfo类的对象
//通过其get()方法可以获得其中的对象
        ContactInfo ci = contactInfoList.get(position);

//将viewholder中hold住的各个view与数据源进行绑定(bind)
        holder.vName.setText(NAME_PREFIX + ci.sendername);
        holder.vSurname.setText(SURNAME_PREFIX + ci.destination);
        holder.vEmail.setText(EMAIL_PREFIX + ci.senderphone);
        holder.vTitle.setText("订单号： " + ci.ordernum);

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, pos);
                }
            });}

        }

    //此方法返回列表项的数目

    public int getItemCount() {
        return contactInfoList.size();
    }


        class ContactViewHolder extends RecyclerView.ViewHolder {
            //create the viewHolder class

            protected TextView vName;
            protected TextView vSurname;
            protected TextView vEmail;
            protected TextView vTitle;

            public ContactViewHolder(View itemView) {
                super(itemView);
                vName = itemView.findViewById(R.id.text_name);
                vSurname = itemView.findViewById(R.id.text_surname);
                vEmail = itemView.findViewById(R.id.text_email);
                vTitle = itemView.findViewById(R.id.title);
            }

        }
    }

