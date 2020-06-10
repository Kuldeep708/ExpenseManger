package com.example.expensemanger;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
public class IncomeFragment extends Fragment {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mIncomeDB;
    private RecyclerView recyclerView;
    private TextView incomSum;
    private EditText amount;
    private EditText type;
    private EditText note;
    private Button btnUpdate;
    private Button btnDelete;
    private int amount_update_forlayout;
    private String type_update_forlayout;
    private String note_update_forlayout;
    private String post_key;




    public IncomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_income, container, false);

        incomSum=view.findViewById(R.id.income_text_result);

        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser=firebaseAuth.getCurrentUser();
        String uId = mUser.getUid();
        mIncomeDB=FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uId);

        recyclerView=view.findViewById(R.id.recyler_id_income);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        mIncomeDB.addValueEventListener(new ValueEventListener() {
            int sum=0;
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    Data data = dataSnapshot1.getValue(Data.class);
                    sum+=data.getAmount();
                    String sum2=String.valueOf(sum);
                    incomSum.setText(sum2);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
     return view;
    }
    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Data,MyViewHolder>adapter=new FirebaseRecyclerAdapter<Data, MyViewHolder>
                (
                        Data.class,
                        R.layout.income_recyler_data,
                        MyViewHolder.class,
                        mIncomeDB
                ) {
            @Override
            protected void populateViewHolder(MyViewHolder viewHolder, final Data model, final int position) {

                viewHolder.setType(model.getType());
                viewHolder.setNote(model.getNote());
                viewHolder.setDate(model.getDate());
                viewHolder.setAmount(model.getAmount());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        post_key= getRef(position).getKey();
                        amount_update_forlayout= model.getAmount();
                        type_update_forlayout=model.getType();
                        note_update_forlayout=model.getNote();

                        updateDataItem();
                    }
                });


            }
        };

        recyclerView.setAdapter(adapter);

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }

        private void setType(String type){
            TextView mType=mView.findViewById(R.id.type_text_income);
            mType.setText(type);
        }

        private void setNote(String note){

            TextView mNote=mView.findViewById(R.id.note_text_income);
            mNote.setText(note);

        }

        private void setDate(String date){
            TextView mDate=mView.findViewById(R.id.date_text_income);
            mDate.setText(date);
        }

        private void setAmount(int amount){

            TextView mAmount=mView.findViewById(R.id.amount_text_income);
            String stamount=String.valueOf(amount);
            mAmount.setText(stamount);

        }


    }

    private void updateDataItem()
    {
        AlertDialog.Builder myDialog=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.update_data_item, null);
        myDialog.setView(view);

        amount = view.findViewById(R.id.amount_edt_update);
        type=view.findViewById(R.id.type_edt_update);
        note=view.findViewById(R.id.note_edt_update);

        amount.setText(String.valueOf(amount_update_forlayout));
        amount.setSelection(String.valueOf(amount_update_forlayout).length());

        type.setText(type_update_forlayout);
        type.setSelection(type_update_forlayout.length());

        note.setText(note_update_forlayout);
        note.setSelection(note_update_forlayout.length());

        btnUpdate =view.findViewById(R.id.btnUpdate_update);
        btnDelete=view.findViewById(R.id.btnDelete_update);

        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(true);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
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

                mIncomeDB.child(post_key).setValue(data);

                dialog.dismiss();

            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIncomeDB.child(post_key).removeValue();
                Toast.makeText(getActivity(),"value removed",Toast.LENGTH_LONG).show();
             dialog.dismiss();
            }
        });


        dialog.show();
    }



}
