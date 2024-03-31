package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Objects;

@Getter
@Setter
@ToString
public class Url {
    private Long id;
    @ToString.Include
    private String name;
    private Timestamp createdAt;
    private Integer statusCode;
    private Timestamp lastTimeCheck;

    public Url(String name, Timestamp createdAt) {
        this.name = name;
        this.createdAt = createdAt;
    }

    public Url(String name, Timestamp createdAt, Integer statusCode, Timestamp lastTimeCheck) {
        this.name = name;
        this.createdAt = createdAt;
        this.statusCode = statusCode;
        this.lastTimeCheck = lastTimeCheck;
    }
}
