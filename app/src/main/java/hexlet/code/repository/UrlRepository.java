package hexlet.code.repository;

import hexlet.code.model.Url;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UrlRepository extends BaseRepository {
    public static void save(Url url) throws SQLException {
        var sql = "INSERT INTO urls (name, created_at) values (?, ?)";
        try (var connection = dataSource.getConnection();
             var prepareStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            prepareStatement.setString(1, url.getName());
            prepareStatement.setTimestamp(2, url.getCreatedAt());
            prepareStatement.executeUpdate();
            var generatedKey = prepareStatement.getGeneratedKeys();
            if (generatedKey.next()) {
                url.setId(generatedKey.getLong(1));
            } else {
                throw new SQLException("При создании URL произошла ошибка в БД");
            }
        }
    }

    public static Optional<Url> findById(Long id) throws SQLException {
        var sql = "SELECT * FROM urls WHERE id=?";
        try (var connection = dataSource.getConnection();
             var prepareStatement = connection.prepareStatement(sql)) {
            prepareStatement.setLong(1, id);
            var resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                var name = resultSet.getString("name");
                var createdAt = resultSet.getTimestamp("created_at");
                var url = new Url(name, createdAt);
                url.setId(id);
                return Optional.of(url);
            }
            return Optional.empty();
        }
    }

    public static Optional<Url> findByName(String uri) throws SQLException {
        var sql = "SELECT * FROM urls WHERE name=?";
        try (var connection = dataSource.getConnection();
             var prepareStatement = connection.prepareStatement(sql)) {
            prepareStatement.setString(1, uri);
            var resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                var id = resultSet.getLong("id");
                var name = resultSet.getString("name");
                var createdAt = resultSet.getTimestamp("created_at");
                var url = new Url(name, createdAt);
                url.setId(id);
                return Optional.of(url);
            }
            return Optional.empty();
        }
    }

/*    public static List<Url> getEntities() throws SQLException {
        List<Url> result = new ArrayList<>();
        var sql = "SELECT * FROM urls";
        try (var connection = dataSource.getConnection();
             var prepareStatement = connection.prepareStatement(sql)) {
            var resultSet = prepareStatement.executeQuery();
            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var name = resultSet.getString("name");
                var timestamp = resultSet.getTimestamp("created_at");
                var url = new Url(name, timestamp);
                url.setId(id);
                result.add(url);
            }
        }
        return result;
    }*/

    public static List<Url> getLeads() throws SQLException {
        List<Url> result = new ArrayList<>();
        var sql = "SELECT u.id, u.name, t.status_code, u.created_at, max(t.created_at) as last_time_checked "
                + "FROM urls as u "
                + "LEFT JOIN urls_checks as t ON u.id=t.url_id "
                + "GROUP BY u.id, u.name, u.created_at, t.status_code ORDER BY u.id";
        try (var connection = dataSource.getConnection();
             var prepareStatement = connection.prepareStatement(sql)) {
            var resultSet = prepareStatement.executeQuery();
            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var name = resultSet.getString("name");
                var statusCode = resultSet.getInt("status_code");
                var createdAt = resultSet.getTimestamp("created_at");
                var lastTimeChecked = resultSet.getTimestamp("last_time_checked");
                var url = new Url(name, createdAt, statusCode, lastTimeChecked);
                url.setId(id);
                result.add(url);
            }
        }
        return result;
    }
}
