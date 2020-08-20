package com.sungwoo.animalcure;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragSales extends Fragment { //프래그먼트는 액티비티의 자식 개념이라고 생각 하면 된다.

    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<SalesItem> arrayList;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference databaseReference; //파이어베이스 데이터 베이스

    private Button btn_add; // 아이템 추가 버튼

    public static FragSales newInstance(){
        FragSales fragSales = new FragSales();
        return fragSales;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_sales,container,false);

        btn_add = view.findViewById(R.id.btn_add);
        recyclerView = view.findViewById(R.id.rv_sales);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());

        databaseReference = FirebaseDatabase.getInstance().getReference("SalesItem");

        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 db의 데이터들을 가지고 오는곳
                arrayList.clear(); //기존 배열리스트가 존재하지 않게 초기화
                //파이어베이스 db의 데이터들을 가지고 오는 곳
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    SalesItem salesItem =snapshot.getValue(SalesItem.class);
                    arrayList.add(salesItem);
                }
                adapter = new SalesAdapter(arrayList, getContext()); //어댑터를 생성하면서 리스트를 어뎁터에 넘긴다.
                recyclerView.setAdapter(adapter); //리사이클러뷰에 커스텀 어댑터를 연결(장착)

                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        SalesItem salesItem = new SalesItem();
//        salesItem.setTv_product("양재동");
//        salesItem.setTv_price("10000원");
//        salesItem.setTv_date("2020-03-04");
//        arrayList.add(salesItem);
//
//        SalesItem salesItem2 = new SalesItem();
//        salesItem2.setTv_product("마우스");
//        salesItem2.setTv_price("10000원");
//        salesItem2.setTv_date("2020-03-04");
//        arrayList.add(salesItem2);
//
//        SalesItem salesItem3 = new SalesItem();
//        salesItem3.setTv_product("키보드");
//        salesItem3.setTv_price("20000원");
//        salesItem3.setTv_date("2020-03-04");
//        arrayList.add(salesItem3);



        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialogAdd = new Dialog(getContext(),R.style.Theme_AppCompat_Light_Dialog_Alert);
                dialogAdd .setContentView(R.layout.dialog_add); // xml 레이아웃 연결

                final EditText et_product = dialogAdd.findViewById(R.id.et_product); //상품명
                final EditText et_price = dialogAdd.findViewById(R.id.et_price); //가격
                final EditText et_date = dialogAdd.findViewById(R.id.et_date);  // 날짜
                final EditText et_password = dialogAdd.findViewById(R.id.et_password);  // 날짜

                Button btn_add = dialogAdd.findViewById(R.id.btn_add);
                btn_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(et_product.getText().length()==0 || et_price.getText().length()==0 || et_date.getText().length()==0 || et_password.getText().length()==0){
                            //현재 입력필드에 값을 안 적은 상황이라면..
                            Toast.makeText(getContext(),"비어있는 입력필드가 존재합니다.",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        SalesItem item = new SalesItem();
                        item.setTv_product(et_product.getText().toString());
                        item.setTv_price(et_price.getText().toString());
                        item.setTv_date(et_date.getText().toString());
                        item.setTv_password(et_password.getText().toString());

                        databaseReference.push().setValue(item);


                        arrayList.add(0,item);
                        adapter.notifyItemInserted(0);
                        dialogAdd.dismiss();
                    }
                });

                dialogAdd.show();
            }
        });

        return view;
    }
}
