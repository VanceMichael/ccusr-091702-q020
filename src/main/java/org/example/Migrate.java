package org.example;

import java.sql.DriverManager;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Migrate {
    private Migrate() {}
    public static void main(String[] args) throws Exception {
        String configured = System.getenv("DATABASE_PATH");
        Path database = Path.of(configured == null || configured.isBlank() ? "data/reading_review.sqlite3" : configured);
        Path parent = database.toAbsolutePath().getParent(); if (parent != null) Files.createDirectories(parent);
        String sql = Files.readString(Path.of("migrations/001_init.sql"));
        try (var connection = DriverManager.getConnection("jdbc:sqlite:" + database)) {
            try (var statement = connection.createStatement()) {
                for (String part : sql.split(";")) {
                    if (!part.isBlank()) statement.execute(part);
                }
            }
        }
        System.out.println(database);
    }
}