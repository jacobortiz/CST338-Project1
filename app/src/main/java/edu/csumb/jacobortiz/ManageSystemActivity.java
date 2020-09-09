package edu.csumb.jacobortiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import edu.csumb.jacobortiz.DB.FlightDao;
import edu.csumb.jacobortiz.DB.AppDatabase;
import edu.csumb.jacobortiz.DB.LogRecord;

public class ManageSystemActivity extends AppCompatActivity {

    private List<LogRecord> logRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ViewLogActivity", "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_system);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button return_main_button = findViewById(R.id.return_button);
        return_main_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ViewLogActivity", "onClick return called");
                finish();
            }
        });

        Button new_flight = findViewById(R.id.new_flight);
        new_flight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageSystemActivity.this, AddFlightActivity.class);
                startActivity(intent);
            }
        });

        FlightDao dao = AppDatabase.getAppDatabase(ManageSystemActivity.this).dao();
        logRecords = dao.getRecords();

        if(logRecords.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ManageSystemActivity.this);
            builder.setTitle("Error");
            builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.setMessage("There are no logs yet");
            dialog.show();
        }

        RecyclerView rv = findViewById(R.id.recycler_view);
        rv.setLayoutManager( new LinearLayoutManager(this));
        Adapter adapter = new Adapter();
        rv.setAdapter(adapter);
    }

    private class Adapter extends RecyclerView.Adapter<ItemHolder> {

        @NotNull
        @Override
        public ItemHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(ManageSystemActivity.this);
            return new ItemHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position){
            holder.bind(logRecords.get(position));
        }

        @Override
        public int getItemCount() { return logRecords.size(); }
    }

    private static class ItemHolder extends RecyclerView.ViewHolder {

        ItemHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item, parent, false));
        }

        void bind(LogRecord logRecord) {
            TextView item = itemView.findViewById(R.id.item_id);
            item.setText(logRecord.toString());
        }
    }
}
