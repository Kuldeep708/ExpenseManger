package com.example.expensemanger;

import android.app.AlertDialog;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expensemanger.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashFragment extends Fragment {
    private FloatingActionButton btn1;
    private FloatingActionButton btn2;
    private FloatingActionButton btn3;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mIncomeDB;
    private DatabaseReference mExpenseDB;
    private TextView setIncresult;
    private TextView setExpresult;
    private RecyclerView mIncomeRecyler;
    private RecyclerView mExpenseRecycler;



    public DashFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dash, container, false);
        mIncomeRecyler=view.findViewById(R.id.income_dash_recyler);
        mExpenseRecycler=view.findViewById(R.id.expense_dash_recyler);
        setExpresult=view.findViewById(R.id.set_expense_result);
        firebaseAuth =FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String uid=firebaseUser.getUid();

        mIncomeDB= FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        mExpenseDB = FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uid);
        setIncresult=view.findViewById(R.id.set_income_result);
        btn1=view.findViewById(R.id.mainplus_ft_btn);
        btn2=view.findViewById(R.id.income_ft_btn);
        btn3=view.findViewById(R.id.expense_ft_btn);

         btn1.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 addData();
                 Toast.makeText(getActivity(),"Other two btn will work now",Toast.LENGTH_LONG).show();
             }
         });

         mIncomeDB.addValueEventListener(new ValueEventListener() {
             int sum=0;
             public void onDataChange(DataSnapshot dataSnapshot) {
                 for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                 {
                     Data data = dataSnapshot1.getValue(Data.class);
                     sum+=data.getAmount();
                     String sum2=String.valueOf(sum);
                     setIncresult.setText(sum2);
                 }

             }
             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });

         mExpenseDB.addValueEventListener(new ValueEventListener() {
             int sum=0;
             public void onDataChange(DataSnapshot dataSnapshot) {
                 for(DataSnapshot mysnapshot:dataSnapshot.getChildren())
                 {
                     Data data = mysnapshot.getValue(Data.class);
                     sum+=data.getAmount();
                     String sum2=String.valueOf(sum);
                     setExpresult.setText(sum2);
                 }

             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });

        LinearLayoutManager layoutManager1= new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
        layoutManager1.setReverseLayout(true);
        layoutManager1.setStackFromEnd(true);
        mIncomeRecyler.setHasFixedSize(true);
        mIncomeRecyler.setLayoutManager(layoutManager1);

        LinearLayoutManager layoutManager2=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        layoutManager2.setReverseLayout(true);
        layoutManager2.setStackFromEnd(true);
        mExpenseRecycler.setHasFixedSize(true);
        mExpenseRecycler.setLayoutManager(layoutManager2);


        return view;
    }
    private void addData()
    {

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomeDataInsert();

            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expenseDataInsert();
            }
        });

    }

    public void  incomeDataInsert()
    {
        AlertDialog.Builder myDialog=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View myview = inflater.inflate(R.layout.custom_layout_for_insert_data,null);
        myDialog.setView(myview);
        final AlertDialog dialog =myDialog.create();
         dialog.setCancelable(false);
        final EditText amount=myview.findViewById(R.id.amount_edt_custom);
        final EditText type = myview.findViewById(R.id.type_edt_custom);
        final EditText note = myview.findViewById(R.id.note_edt_custom);

        Button confirm = myview.findViewById(R.id.btnConfirm_custom);
        Button cancel = myview.findViewById(R.id.btnCancel_custom);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount1 = amount.getText().toString().trim();
                String type1= type.getText().toString().trim();
                String note1=note.getText().toString().trim();

                int amountint =Integer.parseInt(amount1);
                if(TextUtils.isEmpty(amount1))
                {
                    amount.setError("Required field missing");
                }
                if(TextUtils.isEmpty(type1))
                {
                    type.setError("Required field missing");
                }
                if(TextUtils.isEmpty(note1))
                {
                    note.setError("Required field missing");
                }

                String id =mIncomeDB.push().getKey();
                String date= DateFormat.getDateInstance().format(new Date());

                Data data = new Data(amountint,type1,note1,id,date);
                mIncomeDB.child(id).setValue(data);
                Toast.makeText(getActivity(),"data added successfully",Toast.LENGTH_LONG).show();
               dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public void expenseDataInsert() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myview = inflater.inflate(R.layout.custom_layout_for_insert_data, null);
        myDialog.setView(myview);
        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);
        final EditText amount = myview.findViewById(R.id.amount_edt_custom);
        final EditText type = myview.findViewById(R.id.type_edt_custom);
        final EditText note = myview.findViewById(R.id.note_edt_custom);

        Button confirm = myview.findViewById(R.id.btnConfirm_custom);
        Button cancel = myview.findViewById(R.id.btnCancel_custom);

        confirm.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           String amount1 = amount.getText().toString().trim();
                                           String type1 = type.getText().toString().trim();
                                           String note1 = note.getText().toString().trim();

                                           int amountint = Integer.parseInt(amount1);
                                           if (TextUtils.isEmpty(amount1)) {
                                               amount.setError("Required field missing");
                                           }
                                           if (TextUtils.isEmpty(type1)) {
                                               type.setError("Required field missing");
                                           }
                                           if (TextUtils.isEmpty(note1)) {
                                               note.setError("Required field missing");
                                           }

                                           String id = mExpenseDB.push().getKey();
                                           String date = DateFormat.getDateInstance().format(new Date());

                                           Data data = new Data(amountint, type1, note1, id, date);
                                           mExpenseDB.child(id).setValue(data);
                                           Toast.makeText(getActivity(), "data added successfully", Toast.LENGTH_LONG).show();
                                           dialog.dismiss();

                                       }
                                   });



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Data,mIncomeViewHolder> adapter = new FirebaseRecyclerAdapter<Data, mIncomeViewHolder>
                (
                        Data.class,
                        R.layout.dashboard_income_recylerdata,
                        mIncomeViewHolder.class,
                        mIncomeDB
                )
        {
            @Override
            protected void populateViewHolder(mIncomeViewHolder IncomeViewHolder, final Data model, final int position)
            {
                IncomeViewHolder.setAmount(model.getAmount());
                IncomeViewHolder.setDate(model.getDate());
                IncomeViewHolder.setDate(model.getDate());
            }
        };
        mIncomeRecyler.setAdapter(adapter);

        FirebaseRecyclerAdapter<Data,mExpenseViewHolder> adapter1 = new FirebaseRecyclerAdapter<Data, mExpenseViewHolder>

                (
                        Data.class,
                        R.layout.dashboard_expense_recylerdata,
                        DashFragment.mExpenseViewHolder.class,
                        mExpenseDB
                )
        {
            @Override
            protected void populateViewHolder(mExpenseViewHolder ExpenseViewHolder, final Data model,final  int position)
            {
              ExpenseViewHolder.setDate(model.getDate());
              ExpenseViewHolder.setAmount(model.getAmount());
              ExpenseViewHolder.setType(model.getType());
            }
        };
        mExpenseRecycler.setAdapter(adapter1);
    }

    public static class mIncomeViewHolder extends  RecyclerView.ViewHolder
    {
        View mIncomeView;
        public mIncomeViewHolder(View mitemview)
        {
            super(mitemview);
            mIncomeView=mitemview;
        }

        private void setType(String type)
        {
            TextView mtype= mIncomeView.findViewById(R.id.type_income_dashrecyler);
            mtype.setText(type);
        }

        private void setAmount(int amount)
        {
            TextView mamount=mIncomeView.findViewById(R.id.amount_income_dashrecyler);
            String amount2= String.valueOf(amount);
            mamount.setText(amount2);
        }

        private void setDate(String date)
        {
            TextView mdate = mIncomeView.findViewById(R.id.date_income_dashrecyler);
            mdate.setText(date);

        }
    }

    public static class mExpenseViewHolder extends  RecyclerView.ViewHolder
    {
        View mExpenseview;
        public mExpenseViewHolder(View mitemview)
        {
            super(mitemview);
            mExpenseview=mitemview;
        }

        private void setDate(String date)
        {
            TextView mdate= mExpenseview.findViewById(R.id.date_expense_dashrecyler);
            mdate.setText(date);
        }

        private void setAmount(int amount)
        {
            TextView mamount=mExpenseview.findViewById(R.id.amount_expense_dashrecyler);
            String mamount2 = String.valueOf(amount);
            mamount.setText(mamount2);

        }
        private void setType(String type)
        {
            TextView mtype = mExpenseview.findViewById(R.id.type_expense_dashrecyler);
            mtype.setText(type);
        }
    }

}
