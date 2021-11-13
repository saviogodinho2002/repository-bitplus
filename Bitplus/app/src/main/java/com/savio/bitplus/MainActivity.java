package com.savio.bitplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.lang.Math;
public class MainActivity extends AppCompatActivity {

    private EditText cxNumber;
    private Button btnSetBinary;
    private RecyclerView rv;
    private GroupAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cxNumber = findViewById(R.id.cx_binario);
        btnSetBinary = findViewById(R.id.btn_setnumeber);
        rv = findViewById(R.id.recycler_main);

        adapter = new GroupAdapter();
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);

        rv.setLayoutManager(layoutManager);
        rv.addItemDecoration(new DividerItemDecoration(MainActivity.this,LinearLayoutManager.HORIZONTAL));
        rv.setAdapter(adapter);

        btnSetBinary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clear();
                fetchBinary();
            }
        });

    }

    private void fetchBinary(){
        int number = Integer.valueOf(0 + cxNumber.getText().toString());


        String bin = "";
        List<String> binList = new ArrayList<>();
        int numBits = (int) (Math.log(number) / Math.log(2));

        for(int i = 0;i<=numBits;i++) {
            binList.add("0");
        }

        int lastindice = numBits;
        for(int i = 0;i<number;i++) {

            boolean carry = true;
            int count = 0;
            while(carry) {
                if(binList.get(lastindice - count).equals("0") ) {

                    binList.set(lastindice - count, "1");

                    carry = false;

                }else {
                    binList.set(lastindice - count, "0");

                }
                count++;
            }

        }
       Log.e("teste",""+numBits);

        for (String binario : binList) {
            bin = bin+binario;
           adapter.add(new ItemBinario(binario));
        }




    }
    private class ItemBinario extends Item<ViewHolder>{
        String number;
        public ItemBinario(String number) {
        this.number = number;
        }

        @Override
        public void bind(@NonNull  ViewHolder viewHolder, int position) {
            TextView txtNumber = viewHolder.getRoot().findViewById(R.id.txt_item_bit);
            txtNumber.setText(number);

        }

        @Override
        public int getLayout() {
            return R.layout.item_binario;
        }
    }

}