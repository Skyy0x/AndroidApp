package pl.sky0x.app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import pl.sky0x.app.R;
import pl.sky0x.app.data.Click;
import pl.sky0x.app.data.DataSystem;
import pl.sky0x.app.data.implementation.mysql.DatabaseInfo;
import pl.sky0x.app.data.implementation.mysql.MySQLDataSystem;
import pl.sky0x.app.data.implementation.mysql.MySQLService;

public class MainActivity extends AppCompatActivity {

    private DataSystem system;

    private final ExecutorService service = Executors
            .newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new ThreadFactoryBuilder()
                    .setNameFormat("async-executor-%d")
                    .build());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        connectToDatabase();
        setContentView(R.layout.activity_main);

        findViewById(R.id.button_click)
                .setOnClickListener(view -> {
                    system.addClick(new Click(UUID.randomUUID(), System.currentTimeMillis(), "localhost"));
        });

        ListView listView = findViewById(R.id.list_item_2);
        service.execute(() -> {
            List<String> clicks = system.getClicks()
                    .stream()
                    .map(click -> click.getUuid().toString())
                    .collect(Collectors.toList());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, clicks);
            listView.setAdapter(adapter);
        });
    }

    private void connectToDatabase() {
        MySQLService mySQLService = new MySQLService(new DatabaseInfo(
                "localhost",
                "3306",
                "test",
                "roott",
                "password"
        ));
        try {
            mySQLService.connect();
            mySQLService.createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        system = new MySQLDataSystem(mySQLService);
    }
}