package com.cadastro.dao;

import com.cadastro.config.DatabaseConfig;
import com.cadastro.model.Contacto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ContactoDAO {
    private static final Logger logger = Logger.getLogger(ContactoDAO.class.getName());

    public boolean inserir(Contacto contacto) {
        String sql = "INSERT INTO contactos (nome, email, telefone, endereco) VALUES (?,?,?,?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, contacto.getNome());
            ps.setString(2, contacto.getEmail());
            ps.setString(3, contacto.getTelefone());
            ps.setString(4, contacto.getEndereco());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) contacto.setId(rs.getLong(1));
                }
                return true;
            }
        } catch (SQLException e) {
            logger.severe("Erro ao inserir: " + e.getMessage());
        }
        return false;
    }

    public List<Contacto> listarTodos() {
        List<Contacto> lista = new ArrayList<>();
        String sql = "SELECT * FROM contactos ORDER BY nome";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Contacto c = new Contacto();
                c.setId(rs.getLong("id"));
                c.setNome(rs.getString("nome"));
                c.setEmail(rs.getString("email"));
                c.setTelefone(rs.getString("telefone"));
                c.setEndereco(rs.getString("endereco"));
                // Converte Timestamp para Date
                Timestamp ts = rs.getTimestamp("data_criacao");
                if (ts != null) {
                    c.setDataCriacao(new Date(ts.getTime()));
                }
                lista.add(c);
            }
        } catch (SQLException e) {
            logger.severe("Erro ao listar: " + e.getMessage());
        }
        return lista;
    }

    public Contacto buscarPorId(Long id) {
        String sql = "SELECT * FROM contactos WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Contacto c = new Contacto();
                    c.setId(rs.getLong("id"));
                    c.setNome(rs.getString("nome"));
                    c.setEmail(rs.getString("email"));
                    c.setTelefone(rs.getString("telefone"));
                    c.setEndereco(rs.getString("endereco"));
                    Timestamp ts = rs.getTimestamp("data_criacao");
                    if (ts != null) {
                        c.setDataCriacao(new Date(ts.getTime()));
                    }
                    return c;
                }
            }
        } catch (SQLException e) {
            logger.severe("Erro ao buscar por ID: " + e.getMessage());
        }
        return null;
    }

    public boolean atualizar(Contacto contacto) {
        String sql = "UPDATE contactos SET nome=?, email=?, telefone=?, endereco=? WHERE id=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, contacto.getNome());
            ps.setString(2, contacto.getEmail());
            ps.setString(3, contacto.getTelefone());
            ps.setString(4, contacto.getEndereco());
            ps.setLong(5, contacto.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Erro ao atualizar: " + e.getMessage());
            return false;
        }
    }

    public boolean deletar(Long id) {
        String sql = "DELETE FROM contactos WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Erro ao deletar: " + e.getMessage());
            return false;
        }
    }
}