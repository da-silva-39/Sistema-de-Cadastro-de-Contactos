package com.cadastro.config;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

public class DatabaseConfig {

    // ===== ALTERE AQUI PARA MUDAR ENTRE LOCAL E NEON =====
    private static final boolean USAR_NEON = true;   // true = Neon Cloud, false = PostgreSQL local

    // Configurações Locais
    private static final String LOCAL_URL = "jdbc:postgresql://localhost:5432/agenda_db";
    private static final String LOCAL_USER = "postgres";
    private static final String LOCAL_PASS = "JOSE200739";

    // Configurações Neon (substitua pelos seus dados)
    private static final String NEON_URL = "jdbc:postgresql://ep-raspy-voice-al2g2hp3-pooler.c-3.eu-central-1.aws.neon.tech:5432/neondb?sslmode=require";
    private static final String NEON_USER = "neondb_owner";
    private static final String NEON_PASS = "npg_8pmBZd0FIysR";

    private static DataSource dataSource;
    private static String ambienteAtivo;

    static {
        try {
            String url, user, pass;
            if (USAR_NEON) {
                url = NEON_URL;
                user = NEON_USER;
                pass = NEON_PASS;
                ambienteAtivo = "production (Neon.tech)";
            } else {
                url = LOCAL_URL;
                user = LOCAL_USER;
                pass = LOCAL_PASS;
                ambienteAtivo = "development (PostgreSQL local)";
            }

            BasicDataSource ds = new BasicDataSource();
            ds.setDriverClassName("org.postgresql.Driver");
            ds.setUrl(url);
            ds.setUsername(user);
            ds.setPassword(pass);
            ds.setInitialSize(3);
            ds.setMaxTotal(10);
            ds.setMaxIdle(5);
            ds.setMinIdle(2);

            dataSource = ds;
            System.out.println("✅ Pool de conexões iniciado: " + ambienteAtivo);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao configurar DatabaseConfig", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static String getAmbiente() {
        return ambienteAtivo;
    }
}