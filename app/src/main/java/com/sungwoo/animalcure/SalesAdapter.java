package com.sungwoo.animalcure;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.CustomViewHolder> {

    private ArrayList<SalesItem> arrayList; //판매 목록 리스트를 담고 있다.
    private Context context;
    private DatabaseReference databaseReference; //파이어베이스 데이터 베이스


    public SalesAdapter(ArrayList<SalesItem> arrayList, Context context) { //생성자를 구성
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public SalesAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_sales_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        databaseReference = FirebaseDatabase.getInstance().getReference("SalesItem");

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SalesAdapter.CustomViewHolder holder, int position) {
        holder.iv_profile.setImageResource(R.drawable.medical);
        holder.tv_product.setText(arrayList.get(position).getTv_product());
        holder.tv_price.setText(arrayList.get(position).getTv_price());
        holder.tv_date.setText(arrayList.get(position).getTv_date());
    }

    @Override
    public int getItemCount() {//리스트 아이템의 전체 갯수
        return arrayList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_profile;
        TextView tv_product;
        TextView tv_price;
        TextView tv_date;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.iv_profile = itemView.findViewById(R.id.iv_profile);
            this.tv_product = itemView.findViewById(R.id.tv_product);
            this.tv_price = itemView.findViewById(R.id.tv_price);
            this.tv_date = itemView.findViewById(R.id.tv_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int curPos = getAdapterPosition(); //현재 클릭한 아이템의 포지션 (위치.. 0 부터 시작)

                    final Dialog dialogConfirm = new Dialog(context, R.style.Theme_AppCompat_Light_Dialog_Alert);
                    dialogConfirm.setContentView(R.layout.dialog_confirm_password);

                    final EditText et_pass = dialogConfirm.findViewById(R.id.et_pass);
                    Button btn_confirm = dialogConfirm.findViewById(R.id.btn_confirm);
                    btn_confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (et_pass.getText().length() == 0) {
                                //현재 입력필드에 값을 안 적은 상황이라면..
                                Toast.makeText(context, "비어있는 입력필드가 존재합니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            if (et_pass.getText().toString().equals(arrayList.get(curPos).getTv_password())) {
                                // 게시글 비밀번호 입력필드에 입력한 값이랑 내가 클릭한 게시글의 비밀번호가 일치하다면..
                                final Dialog dialogMod = new Dialog(context, R.style.Theme_AppCompat_Light_Dialog_Alert);
                                dialogMod.setContentView(R.layout.dialog_modify);

                                final EditText et_product = dialogMod.findViewById(R.id.et_product); //상품명
                                final EditText et_price = dialogMod.findViewById(R.id.et_price); //가격
                                final EditText et_date = dialogMod.findViewById(R.id.et_date);  // 날짜

                                Button btn_modify = dialogMod.findViewById(R.id.btn_modify); //수정완료 버튼
                                btn_modify.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        if (et_product.getText().length() == 0 || et_price.getText().length() == 0 || et_date.getText().length() == 0) {
                                            //현재 입력필드에 값을 안 적은 상황이라면..
                                            Toast.makeText(context, "비어있는 입력필드가 존재합니다.", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        final SalesItem item = new SalesItem();
                                        item.setTv_product(et_product.getText().toString());
                                        item.setTv_price(et_price.getText().toString());
                                        item.setTv_date(et_date.getText().toString());
                                        item.setTv_password(arrayList.get(curPos).getTv_password());

                                        databaseReference.orderByChild("tv_product").equalTo(arrayList.get(curPos).getTv_product()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    databaseReference.child(snapshot.getKey()).setValue(item);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                        arrayList.set(curPos, item); //리스트에 있는 데이터를 수정.
                                        notifyItemChanged(curPos);   // 수정 완료 후 새로 고침

                                        dialogMod.dismiss(); //다이얼로그 닫기

                                    }
                                });

                                Button btn_delete = dialogMod.findViewById(R.id.btn_delete);
                                btn_delete.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        databaseReference.orderByChild("tv_product").equalTo(arrayList.get(curPos).getTv_product()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    databaseReference.child(snapshot.getKey()).setValue(null);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                        arrayList.remove(curPos);

                                        notifyItemRemoved(curPos);
                                        notifyItemRangeRemoved(curPos, arrayList.size());

                                        dialogMod.dismiss();
                                    }
                                });

                                dialogMod.show(); //다이얼로그 활성화

                            } else {

                                Toast.makeText(context, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                            }

                            dialogConfirm.dismiss(); //다이얼로그 종료

                        }
                    });
                    dialogConfirm.show(); //다이얼로그 활성화
                }
            });
        }
    }
}
