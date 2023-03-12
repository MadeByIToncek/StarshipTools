package cz.iqlandia.iqplanetarium;

import org.jetbrains.annotations.*;

import java.time.*;

public record CountdownEvent(String name, String description, @Nullable LocalTime time, int x, float ratio) {
}
