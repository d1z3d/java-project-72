package hexlet.code.repository;

import hexlet.code.model.UrlCheck;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.SQLException;
import java.sql.Statement;


public class UrlCheckRepository extends BaseRepository {
    public static void save(UrlCheck urlCheck) throws SQLException {
        var sql = "INSERT INTO url_checks (status_code, title, h1, description, url_id, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (var connection = dataSource.getConnection();
             var prepareStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            prepareStatement.setInt(1, urlCheck.getStatusCode());
            prepareStatement.setString(2, urlCheck.getTitle());
            prepareStatement.setString(3, urlCheck.getH1());
            prepareStatement.setString(4, urlCheck.getDescription());
            prepareStatement.setLong(5, urlCheck.getUrlId());
            prepareStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            prepareStatement.executeUpdate();
            var generatedKey = prepareStatement.getGeneratedKeys();
            if (generatedKey.next()) {
                urlCheck.setId(generatedKey.getLong(1));
            } else {
                throw new SQLException("При создании проверки сайта произошла ошибка в БД");
            }
        }
    }

    public static List<UrlCheck> getEntitiesByUrlId(Long urlId) throws SQLException {
        List<UrlCheck> result = new ArrayList<>();
        var sql = "SELECT c.* FROM urls as u JOIN url_checks as c ON c.url_id=u.id WHERE u.id=? ORDER BY c.id";
        try (var connection = dataSource.getConnection();
             var prepareStatement = connection.prepareStatement(sql)) {
            prepareStatement.setLong(1, urlId);
            var resultSet = prepareStatement.executeQuery();
            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var statusCode = resultSet.getInt("status_code");
                var title = resultSet.getString("title");
                var h1 = resultSet.getString("h1");
                var description = resultSet.getString("description");
                var createdAt = resultSet.getTimestamp("created_at");
                UrlCheck urlCheck = new UrlCheck(statusCode, title, h1, description, urlId);
                urlCheck.setId(id);
                urlCheck.setCreatedAt(createdAt);
                result.add(urlCheck);
            }
        }
        return result;
    }
    public static Map<Long, UrlCheck> getLastChecks() throws SQLException {
        Map<Long, UrlCheck> result = new HashMap<>();
        var sql = "SELECT DISTINCT ON (url_id) id, status_code, title, h1, description, url_id, created_at "
                + "FROM url_checks "
                + "ORDER BY url_id, id desc";
        try (var connection = dataSource.getConnection();
             var prepareStatement = connection.prepareStatement(sql)) {
            var resultSet = prepareStatement.executeQuery();
            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var statusCode = resultSet.getInt("status_code");
                var title = resultSet.getString("title");
                var h1 = resultSet.getString("h1");
                var description = resultSet.getString("description");
                var urlId = resultSet.getLong("url_id");
                var createdAt = resultSet.getTimestamp("created_at");
                var urlCheck = new UrlCheck(statusCode, title, h1, description, urlId);
                urlCheck.setId(id);
                urlCheck.setCreatedAt(createdAt);
                result.put(urlId, urlCheck);
            }
        }
        return result;
    }
}
