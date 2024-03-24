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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Url url = (Url) o;
        return Objects.equals(id, url.id)
                && Objects.equals(name, url.name)
                && Objects.equals(createdAt, url.createdAt)
                && Objects.equals(statusCode, url.statusCode)
                && Objects.equals(lastTimeCheck, url.lastTimeCheck);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, createdAt, statusCode, lastTimeCheck);
    }
}
