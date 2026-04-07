package com.cadastro.controller;

import com.cadastro.config.DatabaseConfig;
import com.cadastro.dao.ContactoDAO;
import com.cadastro.model.Contacto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/contacto")
public class ContactoServlet extends HttpServlet {
    private ContactoDAO contactoDAO;

    @Override
    public void init() throws ServletException {
        contactoDAO = new ContactoDAO();
        // Disponibiliza o ambiente em qualquer JSP
        getServletContext().setAttribute("ambiente", DatabaseConfig.getAmbiente());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "list":
                listarContactos(req, resp);
                break;
            case "edit":
                mostrarFormEdicao(req, resp);
                break;
            case "delete":
                deletarContacto(req, resp);
                break;
            default:
                listarContactos(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("insert".equals(action)) {
            inserirContacto(req, resp);
        } else if ("update".equals(action)) {
            atualizarContacto(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/contacto?action=list");
        }
    }

    private void listarContactos(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Contacto> contactos = contactoDAO.listarTodos();
        req.setAttribute("contactos", contactos);
        req.getRequestDispatcher("/WEB-INF/views/lista.jsp").forward(req, resp);
    }

    private void mostrarFormEdicao(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/contacto?action=list&error=ID não informado");
            return;
        }
        try {
            Long id = Long.parseLong(idParam);
            Contacto c = contactoDAO.buscarPorId(id);
            if (c == null) {
                resp.sendRedirect(req.getContextPath() + "/contacto?action=list&error=Contacto não encontrado");
            } else {
                req.setAttribute("contacto", c);
                req.getRequestDispatcher("/WEB-INF/views/editar.jsp").forward(req, resp);
            }
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/contacto?action=list&error=ID inválido");
        }
    }

    private void inserirContacto(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        Map<String, String> erros = validarCampos(req);
        if (!erros.isEmpty()) {
            String msg = String.join(" ", erros.values());
            resp.sendRedirect(req.getContextPath() + "/contacto?action=list&error=" + java.net.URLEncoder.encode(msg, "UTF-8"));
            return;
        }
        Contacto c = new Contacto();
        c.setNome(req.getParameter("nome"));
        c.setEmail(req.getParameter("email"));
        c.setTelefone(req.getParameter("telefone"));
        c.setEndereco(req.getParameter("endereco"));
        boolean ok = contactoDAO.inserir(c);
        if (ok) {
            resp.sendRedirect(req.getContextPath() + "/contacto?action=list&success=Contacto adicionado com sucesso");
        } else {
            resp.sendRedirect(req.getContextPath() + "/contacto?action=list&error=Erro ao inserir (email pode já existir)");
        }
    }

    private void atualizarContacto(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/contacto?action=list&error=ID não informado");
            return;
        }
        Map<String, String> erros = validarCampos(req);
        if (!erros.isEmpty()) {
            String msg = String.join(" ", erros.values());
            resp.sendRedirect(req.getContextPath() + "/contacto?action=list&error=" + java.net.URLEncoder.encode(msg, "UTF-8"));
            return;
        }
        try {
            Long id = Long.parseLong(idParam);
            Contacto c = new Contacto();
            c.setId(id);
            c.setNome(req.getParameter("nome"));
            c.setEmail(req.getParameter("email"));
            c.setTelefone(req.getParameter("telefone"));
            c.setEndereco(req.getParameter("endereco"));
            boolean ok = contactoDAO.atualizar(c);
            if (ok) {
                resp.sendRedirect(req.getContextPath() + "/contacto?action=list&success=Contacto atualizado");
            } else {
                resp.sendRedirect(req.getContextPath() + "/contacto?action=list&error=Erro ao atualizar");
            }
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/contacto?action=list&error=ID inválido");
        }
    }

    private void deletarContacto(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/contacto?action=list&error=ID não informado");
            return;
        }
        try {
            Long id = Long.parseLong(idParam);
            boolean ok = contactoDAO.deletar(id);
            if (ok) {
                resp.sendRedirect(req.getContextPath() + "/contacto?action=list&success=Contacto removido");
            } else {
                resp.sendRedirect(req.getContextPath() + "/contacto?action=list&error=Erro ao remover");
            }
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/contacto?action=list&error=ID inválido");
        }
    }

    private Map<String, String> validarCampos(HttpServletRequest req) {
        Map<String, String> erros = new HashMap<>();
        String nome = req.getParameter("nome");
        if (nome == null || nome.trim().length() < 3)
            erros.put("nome", "Nome deve ter pelo menos 3 caracteres.");
        String email = req.getParameter("email");
        if (email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$"))
            erros.put("email", "Email inválido.");
        String telefone = req.getParameter("telefone");
        if (telefone != null && !telefone.trim().isEmpty()) {
            String digits = telefone.replaceAll("\\D", "");
            if (digits.length() < 9 || digits.length() > 15)
                erros.put("telefone", "Telefone deve ter 9-15 dígitos.");
        }
        return erros;
    }
}