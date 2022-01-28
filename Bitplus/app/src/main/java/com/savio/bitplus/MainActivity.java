package com.savio.bitplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    private TextView txtAtualNumber;
    private TextView txtAtualState;
    private TextView txtCarry;
    private TextView txtAtual;
    private  Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cxNumber = findViewById(R.id.cx_binario);
        btnSetBinary = findViewById(R.id.btn_setnumeber);
        rv = findViewById(R.id.recycler_main);
        txtAtualNumber = findViewById(R.id.txt_atual_bit);
        txtAtualState = findViewById(R.id.txt_state);
        txtCarry = findViewById(R.id.txt_carry);
        txtAtual = findViewById(R.id.txt_atual_number);
        adapter = new GroupAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        handler = new Handler();
        rv.setLayoutManager(layoutManager);
        rv.addItemDecoration(new DividerItemDecoration(MainActivity.this, LinearLayoutManager.HORIZONTAL));
        rv.setAdapter(adapter);


        btnSetBinary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacksAndMessages(null);
                txtAtualNumber.setText("Bit: 1" );
                txtCarry.setText("Carry: -");
                txtAtualState.setText(
                        "É 0?\n\n");

                    adapter.clear();
                txtAtual.setText("");
                fetchBinary();

            }
        });

    }



    private void fetchBinary(){
        int number = Integer.valueOf(0 + cxNumber.getText().toString());

        String bin = "";
        List<String> binList = new ArrayList<>();
        List<String> binNumList = new ArrayList<>();


        int numBits = (int) (Math.log(number) / Math.log(2));

        for(int i = 0;i<numBits;i++) {
            binList.add("0");
            adapter.add(new ItemBinario("0",0));
            bin+="0";
        }
        binList.add("0");
        adapter.add(new ItemBinario("0",0));
        bin+="0";
        txtAtual.setText("0");
        int lastindice = numBits;

        int delayH = 1500;

        for(int i = 0;i<number;i++) {
            boolean carry = true;
            int count = 0;
            while (carry) {

                String finalBin = bin;
                int finalCount = count;
                int finalI = i;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        char[] binarios = finalBin.toCharArray();
                        Log.e("teste",binarios.toString());
                        txtCarry.setText("Carry: 1");
                        txtAtualNumber.setText("Bit: "+(finalCount+1) );
                        txtAtualState.setText("É 0?\n\n" );
                        txtAtual.setText(""+ finalI);
                        adapter.clear();
                        for (char b:
                                binarios) {
                            String bi =String.valueOf(b);
                            ItemBinario itemBinario = new ItemBinario(bi,((numBits -adapter.getItemCount()) == finalCount)?4:0);

                            adapter.add(itemBinario);
                        }

                    }
                }, delayH += 1500);


                if (binList.get(lastindice - count).equals("0")) {
                    binList.set(lastindice - count, "1");
                    carry = false;

                } else {
                    binList.set(lastindice - count, "0");
                }
                bin = "";
                for (String binario :
                        binList) {
                    bin = bin + binario;
                }

                binNumList.add(bin);
                

                boolean finalCarry = carry;
                String finalBin1 = bin;

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.clear();
                        char[] binarios = finalBin1.toCharArray();
                        Log.e("teste",binarios.toString());
                        txtAtualNumber.setText("Bit: "+(finalCount+1) );
                        txtCarry.setText(finalCarry?"Carry: 1":"Carry: 0");
                        txtAtualState.setText((finalCarry ?
                                "É 0?\nnão\n"
                                :"É 0?\nsim\n"));
                        txtAtual.setText((finalCarry ?
                                ""+finalI
                                :""+(finalI+1) ));
                        for (char b:
                                binarios) {
                            String bi =String.valueOf(b);
                            ItemBinario itemBinario = new ItemBinario(bi,( (numBits -adapter.getItemCount()) == finalCount)?
                                    (!finalCarry?2:1)
                                    :0);

                            adapter.add(itemBinario);
                        }

                    }
                }, delayH += 1500);

                count++;
            }
        }
        Log.e("teste",""+numBits);


        Log.e("teste","espera acabar?");
        }


    private class ItemBinario extends Item<ViewHolder>{
        public String number;
        int flag;
        public ItemBinario(String number,int flag) {
        this.number = number;
        this.flag = flag;

        }
        public  TextView txtNumber;
        public ConstraintLayout constraintLayout;
        @Override
        public void bind(@NonNull  ViewHolder viewHolder, int position) {
            this.txtNumber = viewHolder.getRoot().findViewById(R.id.txt_item_bit);
            this.txtNumber.setText(number);
            constraintLayout = viewHolder.getRoot().findViewById(R.id.constraint_item);

            this.constraintLayout.setBackgroundColor(flag==0?
                Color.WHITE:
                    flag ==1?
                    Color.BLUE:
                    flag ==2?
                    Color.RED:
                            Color.GREEN);
            //0 = branco
            // 1 = azul
            //2 = vermelho
            //2 = azul

        }


        @Override
        public int getLayout() {
            return R.layout.item_binario;
        }
    }

}
