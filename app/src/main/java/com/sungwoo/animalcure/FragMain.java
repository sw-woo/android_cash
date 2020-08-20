package com.sungwoo.animalcure;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragMain extends Fragment { //프래그먼트는 액티비티의 자식 개념이라고 생각 하면 된다.

    private View view;

    public static FragMain newInstance(){
        FragMain fragMain = new FragMain();
        return fragMain;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_main,container,false);

        return view;
    }
}
