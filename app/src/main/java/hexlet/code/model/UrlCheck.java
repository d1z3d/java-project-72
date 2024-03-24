package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Objects;

@Getter
@Setter
public class UrlCheck {
    private Long id;
    private Integer statusCode;
    private String title;
    private String h1;
    private String description;
    private Long urlId;
    private Timestamp createdAt;

    public UrlCheck(Integer statusCode, String title, String h1, String description, Long urlId, Timestamp createdAt) {
        this.statusCode = statusCode;
        this.title = title;
        this.h1 = h1;
        this.description = description;
        this.urlId = urlId;
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UrlCheck urlCheck = (UrlCheck) o;
        return Objects.equals(id, urlCheck.id)
                && Objects.equals(statusCode, urlCheck.statusCode)
                && Objects.equals(title, urlCheck.title)
                && Objects.equals(h1, urlCheck.h1)
                && Objects.equals(description, urlCheck.description)
                && Objects.equals(urlId, urlCheck.urlId)
                && Objects.equals(createdAt, urlCheck.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, statusCode, title, h1, description, urlId, createdAt);
    }
}
