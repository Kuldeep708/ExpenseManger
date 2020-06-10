package com.example.expensemanger;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expensemanger.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
public class ExpenseFragment extends Fragment {
   private FirebaseAuth firebaseAuth;
   private DatabaseReference mExpenseDB;
   private RecyclerView recyclerView;
   private TextView expenseSum;
    private EditText amount;
    private EditText type;
    private EditText note;
   private Button btnUpdateExpense;
   private Button btnDeleteExpense;
   private int amount_update_forlayout;
    private String type_update_forlayout;
    private String note_update_forlayout;
    private String post_key;





    public ExpenseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview =inflater.inflate(R.layout.fragment_expense, container, false);
        expenseSum = myview.findViewById(R.id.expense_text_result);
        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser=firebaseAuth.getCurrentUser();
        String uid=mUser.getUid();
        mExpenseDB=FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uid);
        recyclerView = myview.findViewById(R.id.recyler_id_expense);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        mExpenseDB.addValueEventListener(new ValueEventListener() {
            int Sum=0;
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot mysnapshot : dataSnapshot.getChildren())
                {
                    Data data = mysnapshot.getValue(Data.class);
                    Sum+=data.getAmount();

                    String sum2 = String.valueOf(Sum);
                    expenseSum.setText(sum2);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return myview;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Data,MyViewHolder> adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>
                (
                        Data.class,
                        R.layout.expense_recycler_data,
                        MyViewHolder.class,
                        mExpenseDB
                ) {
            @Override
            protected void populateViewHolder(MyViewHolder myViewHolder,final  Data model, final int position) {
                myViewHolder.setDate(model.getDate());
                myViewHolder.setAmount(model.getAmount());
                myViewHolder.setNote(model.getNote());
                myViewHolder.setType(model.getType());

                myViewHolder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        post_key= getRef(position).getKey();
                        amount_update_forlayout= model.getAmount();
                        type_update_forlayout=model.getType();
                        note_update_forlayout=model.getNote();


                        updateExpenseData();
                    }
                });


            }
        };
        recyclerView.setAdapter(adapter);

    }

    public static class  MyViewHolder extends RecyclerView.ViewHolder
    {
       View mview;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mview =itemView;
        }
        private void setType(String type){
            TextView mType=mview.findViewById(R.id.type_text_expense);
            mType.setText(type);
        }

        private void setNote(String note){

            TextView mNote=mview.findViewById(R.id.note_text_expense);
            mNote.setText(note);

        }

        private void setDate(String date){
            TextView mDate=mview.findViewById(R.id.date_text_expense);
            mDate.setText(date);
        }

        private void setAmount(int amount){

            TextView mAmount=mview.findViewById(R.id.amount_text_expense);
            String stamount=String.valueOf(amount);
            mAmount.setText(stamount);

        }
    }

    public void updateExpenseData()
    {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.update_data_item,null);
        myDialog.setView(view);
        final AlertDialog dialog = myDialog.create();

        amount=view.findViewById(R.id.amount_edt_update);
        type =view.findViewById(R.id.type_edt_update);
        note =view.findViewById(R.id.note_edt_update);
        btnUpdateExpense=view.findViewById(R.id.btnUpdate_update);
        btnDeleteExpense=view.findViewById(R.id.btnDelete_update);

        amount.setText(String.valueOf(amount_update_forlayout));
        amount.setSelection(String.valueOf(amount_update_forlayout).length());

        type.setText(type_update_forlayout);
        type.setSelection(type_update_forlayout.length());

        note.setText(note_update_forlayout);
        note.setSelection(note_update_forlayout.length());

        btnUpdateExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String.valueOf(amount_update_forlayout);
                String amount1;

                amount1=amount.getText().toString().trim();
                int mamount=Integer.parseInt(amount1);

                type_update_forlayout=type.getText().toString().trim();
                note_update_forlayout=note.getText().toString().trim();

                String date = DateFormat.getDateInstance().format(new Date());

                Data data = new Data(mamount,type_update_forlayout,note_update_forlayout,post_key,date);

                mExpenseDB.child(post_key).setValue(data);

                  dialog.dismiss();
                            }
        });

        btnDeleteExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpenseDB.child(post_key).removeValue();
                Toast.makeText(getActivity(),"value Reomved",Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

     dialog.show();
    }
}
