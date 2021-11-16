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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xwray.groupie.Group;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupDataObserver;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.lang.Math;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    private EditText cxNumber;
    private Button btnSetBinary;
    private RecyclerView rv;
    private GroupAdapter adapter;
    private TextView txtAtualNumber;
    private TextView txtAtualState;
    private TextView txtCarry;

    private  Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cxNumber = findViewById(R.id.cx_binario);
        btnSetBinary = findViewById(R.id.btn_setnumeber);
        rv = findViewById(R.id.recycler_main);
        txtAtualNumber = findViewById(R.id.txt_atual_number);
        txtAtualState = findViewById(R.id.txt_state);
        txtCarry = findViewById(R.id.txt_carry);
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
                        "trocou para: -\npróximo bit: -");

                    adapter.clear();
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
        }
        binList.add("0");
        adapter.add(new ItemBinario("0",0));

        int lastindice = numBits;

        int delayH = 1000;

        for(int i = 0;i<number;i++) {
            boolean carry = true;
            int count = 0;
            while (carry) {

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


                String finalBin = bin;
                int finalCount = count;
                boolean finalCarry = carry;

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.clear();
                        char[] binarios = finalBin.toCharArray();
                        Log.e("teste",binarios.toString());
                        txtAtualNumber.setText("Bit: "+(finalCount+1) );
                        txtCarry.setText(finalCarry?"Carry: 1":"Carry: 0");
                        txtAtualState.setText((finalCarry ?
                                "trocou para: 0\npróximo bit: "+(finalCount+2)+"º"
                                :"trocou para: 1\npróximo bit: 1º"));
                        for (char b:
                                binarios) {
                            String bi =String.valueOf(b);
                            ItemBinario itemBinario = new ItemBinario(bi,( (numBits -adapter.getItemCount()) == finalCount)?
                                    (!finalCarry?2:1)
                                    :0);

                            adapter.add(itemBinario);
                        }

                    }
                }, delayH += 2000);

                count++;
            }
        }
        Log.e("teste",""+numBits);





        /*

        int delay = 0;   // delay de 2 seg.
        int interval = 1000;  // intervalo de 1 seg.
        for (String binario : binNumList) {
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {

                    String texto =  binario;
                    txtHistoric.setText(texto);

                    timer.cancel();
                    Log.e("teste",binario);

                }
            }, delay+=2000,interval+=1000);

            Log.e("teste",binario);

        }

          adapter.clear();

        char[] binarios = binNumList.get(binNumList.size()-1).toCharArray();
        for (char b:
                binarios) {
            String bi =String.valueOf(b);
            adapter.add(new ItemBinario(bi));
        }

         int delay = 2000;   // delay de 2 seg.
        int interval = 4000;  // intervalo de 1 seg.

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                txtHistoric.setText("seexo");
                timer.cancel();

            }
        }, delay+=2000,interval+=1000);


 for (String binario : binNumList) {
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {


                    timer.cancel();
                    Log.e("teste", binario);

                }
            }, delay += 2000, interval += 1000);
        }
        char[] binarios = binNumList.get(binNumList.size()-1).toCharArray();
        for (char b:
                binarios) {
            String bi =String.valueOf(b);
            adapter.add(new ItemBinario(bi));
        }

          int delay = 0;   // delay de 2 seg.
        int interval = 1000;  // intervalo de 1 seg.

        for (String binario : binNumList) {
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    txtHistoric.setText(binario);

                    timer.cancel();
                    Log.e("teste",binario);

                }
            }, delay+=2000,interval+=1000);

            Log.e("teste",binario);

        }
        new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                    }
                },500);


        try {
            TimeUnit.MILLISECONDS.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


         for (String binario : binList) {
            bin = bin+binario;
            adapter.add(new ItemBinario(binario));
        }


        * try {
            for (String binario : binList) {

                Future<Integer> future = executorService.submit(new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        adapter.add(new ItemBinario(binario));

                        return 1;
                    }
                });
                while (!future.isDone()) {
                    future.get();

                }

                Thread.sleep(1000);

            }
        }catch (Exception e){

        }*/

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
                    Color.RED:Color.BLACK);
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