package techpalle.com.crudrveg;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction t= manager.beginTransaction();
        RvFragment rf = new RvFragment();
        t.add(R.id.container, rf);
        t.commit();
    }
}
