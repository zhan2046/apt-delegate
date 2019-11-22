package com.apt.delegate;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.apt.delegate.source.IRepository;
import com.apt.delegate.source.Repository;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IRepository repository = Repository.INSTANCE.get();
    }
}
