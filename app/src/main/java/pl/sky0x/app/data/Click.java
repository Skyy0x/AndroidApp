package pl.sky0x.app.data;

import java.util.UUID;

public class Click {

    private final UUID uuid;
    private final long time;
    private final String ip;

    public Click(UUID uuid, long time, String ip) {
        this.uuid = uuid;
        this.time = time;
        this.ip = ip;
    }

    public UUID getUuid() {
        return uuid;
    }

    public long getTime() {
        return time;
    }

    public String getIp() {
        return ip;
    }
}
