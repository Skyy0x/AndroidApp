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

import pl.sky0x.app.DataType;
import pl.sky0x.app.R;
import pl.sky0x.app.data.Click;
import pl.sky0x.app.data.DataSystem;
import pl.sky0x.app.data.implementation.http.HTTPDataSystem;
import pl.sky0x.app.data.implementation.mysql.DatabaseInfo;
import pl.sky0x.app.data.implementation.mysql.MySQLDataSystem;
import pl.sky0x.app.data.implementation.mysql.MySQLService;

public class MainActivity extends AppCompatActivity {

    //Information to database
    private static final String
            HOST = "localhost",
            PORT = "3306",
            DATABASE = "clicks",
            USERNAME = "root",
            PASSWORD = "password";

    //Api url
    private static final String URL = "http://127.0.0.1:3000/clicks";

    private DataSystem system;

    private final ExecutorService service = Executors
            .newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new ThreadFactoryBuilder()
                    .setNameFormat("async-executor-%d")
                    .build());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUpDataSystem(DataType.MYSQL);
        setContentView(R.layout.activity_main);

        service.execute(this::updateList);

        findViewById(R.id.button_click)
                .setOnClickListener(view -> {
                    service.execute(() -> {
                        system.addClick(new Click(UUID.randomUUID(), System.currentTimeMillis(), "localhost"));
                        updateList();
                    });
                });
    }

    /**
     * Updating list in this activity
     */
    private void updateList() {
        ListView listView = findViewById(R.id.list_item_2);
        List<String> clicks = system.getClicks()
                .stream()
                .map(click -> click.getUuid().toString())
                .collect(Collectors.toList());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, clicks);
        listView.setAdapter(adapter);
    }

    /**
     * Setting data system
     * @param type of system (HTTP, MYSQL)
     */
    private void setUpDataSystem(DataType type) {
        system = (type == DataType.HTTP ? new HTTPDataSystem(URL) : connectToDatabase());
    }

    /**
     * Connecting to database
     * @return new mysql data system
     */
    private DataSystem connectToDatabase() {
        MySQLService mySQLService = new MySQLService(new DatabaseInfo(
                HOST,
                PORT,
                DATABASE,
                USERNAME,
                PASSWORD
        ));
        try {
            mySQLService.connect();
            mySQLService.createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new MySQLDataSystem(mySQLService);
    }
}