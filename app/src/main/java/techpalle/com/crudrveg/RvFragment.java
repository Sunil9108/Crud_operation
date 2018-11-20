package techpalle.com.crudrveg;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class RvFragment extends Fragment {

    MyDataBase md;
    Cursor c;
    EditText et1, et2;
    Button b1,b2;
    RecyclerView rv;
    LinearLayoutManager llm;
    MyAdapter ma;
    int eno = -1; //this is to track which row user has clicked (-1 user not clicked on any row)

    //create an inner class for recycler adapter
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            View v = getActivity().getLayoutInflater().inflate(R.layout.row, viewGroup, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

            c.moveToPosition(i);
            viewHolder.tv1.setText(""+c.getInt(0));
            viewHolder.tv2.setText(c.getString(1));
            viewHolder.tv3.setText(""+c.getInt(2));

        }

        @Override
        public int getItemCount() {
            return c.getCount();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            public TextView tv1, tv2, tv3;
            public CardView cv1;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tv1 = itemView.findViewById(R.id.t1);
                tv2 = itemView.findViewById(R.id.t2);
                tv3 = itemView.findViewById(R.id.t3);
                cv1 = itemView.findViewById(R.id.cd1);

                cv1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = getAdapterPosition(); //which row clicked
                        c.moveToPosition(pos);
                        eno = c.getInt(0);
                        et1.setText(c.getString(1));
                        et2.setText(""+c.getInt(2));
                    }
                });

                cv1.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        int pos = getAdapterPosition(); //which row clicked
                        c.moveToPosition(pos);
                        eno = c.getInt(0);
                        md.deleteEmp(eno);
                        Toast.makeText(getActivity(), "Deleted..", Toast.LENGTH_SHORT).show();
                        c = md.getEmp();
                        ma.notifyDataSetChanged();
                        return true;
                    }
                });
            }
        }
    }

    public RvFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_rv, container, false);
        et1 = v.findViewById(R.id.edt1);
        et2 = v.findViewById(R.id.edt2);
        b1 = v.findViewById(R.id.btn1);
        b2 = v.findViewById(R.id.btn2);
        rv = v.findViewById(R.id.rv1);
        md = new MyDataBase(getActivity());
        md.open();// open the database
        c = md.getEmp();// read emp table rows to cursor
        llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        ma = new MyAdapter();
        rv.setAdapter(ma);
        rv.setLayoutManager(llm);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ename = et1.getText().toString();
                if(TextUtils.isEmpty(ename)) {
                    et1.setError("Please Enter Emp Name");
                    return;
                }
                String i = et2.getText().toString();
                if(TextUtils.isEmpty(i)) {
                    et2.setError("Please Enter Salary");
                    return;
                }
                Integer esal = Integer.parseInt(i);
                md.insertEmp(ename, esal);
                Toast.makeText( getActivity(), "a row inserted", Toast.LENGTH_SHORT).show();
                //Since we inserted a new row, we have to get all rows again to cursor, sot that we can show latest row in recycler view
                c = md.getEmp();
                ma.notifyDataSetChanged();
                et1.setText("");
                et2.setText("");
                et1.requestFocus();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //user want to update employee details
                String ename = et1.getText().toString().trim();
                if(TextUtils.isEmpty(ename)) {
                    et1.setError("Please Enter Emp Name");
                    return;
                }
                String i =et2.getText().toString().trim();
                if(TextUtils.isEmpty(i)) {
                    et2.setError("Please Enter Salary");
                    return;
                }
                Integer esal = Integer.parseInt(i);
                md.updateEmp(eno, ename, esal);
                eno = -1;//reset eno to -1
                //Since row is updated, we have to read latest table values
                Toast.makeText(getActivity(), "Updated..", Toast.LENGTH_SHORT).show();
                c = md.getEmp();
                ma.notifyDataSetChanged();
                et1.setText("");
                et2.setText("");
                et1.requestFocus();
            }
        });

        return v;
    }

}
