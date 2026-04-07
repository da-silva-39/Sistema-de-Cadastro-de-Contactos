<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <title>📇 Agenda de Contactos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <div class="header">
        <h1>📇 Meus Contactos</h1>
        <p>Gerencie sua agenda com Java MVC + PostgreSQL (Local ou Neon)</p>
    </div>
    <div class="content">
        <c:if test="${param.success != null}">
            <div class="alert alert-success">✅ ${param.success}</div>
        </c:if>
        <c:if test="${param.error != null}">
            <div class="alert alert-error">⚠️ ${param.error}</div>
        </c:if>

        <div class="form-card">
            <h2>➕ Novo Contacto</h2>
            <form action="${pageContext.request.contextPath}/contacto" method="post">
                <input type="hidden" name="action" value="insert">
                <div class="form-grid">
                    <div class="form-group"><label>Nome *</label><input type="text" name="nome" required></div>
                    <div class="form-group"><label>Email *</label><input type="email" name="email" required></div>
                    <div class="form-group"><label>Telefone</label><input type="tel" name="telefone" placeholder="(84) 91234-5678"></div>
                    <div class="form-group"><label>Endereço</label><textarea name="endereco" rows="2"></textarea></div>
                </div>
                <button type="submit" class="btn btn-primary">💾 Salvar</button>
            </form>
        </div>

        <h2>📋 Lista de Contactos</h2>
        <div class="table-container">
            <c:choose>
                <c:when test="${empty contactos}">
                    <div class="empty-state">📭 Nenhum contacto cadastrado.</div>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead>
                            <tr><th>ID</th><th>Nome</th><th>Email</th><th>Telefone</th><th>Endereço</th><th>Criação</th><th>Ações</th></tr>
                        </thead>
                        <tbody>
                            <c:forEach var="c" items="${contactos}">
                                <tr>
                                    <td>${c.id}</td>
                                    <td><strong>${c.nome}</strong></td>
                                    <td>${c.email}</td>
                                    <td>${c.telefone}</td>
                                    <td>${c.endereco}</td>
                                    <td><fmt:formatDate value="${c.dataCriacao}" pattern="dd/MM/yyyy HH:mm"/></td>
                                    <td class="actions">
                                        <a href="${pageContext.request.contextPath}/contacto?action=edit&id=${c.id}" class="btn btn-warning btn-sm">✏️ Editar</a>
                                        <a href="${pageContext.request.contextPath}/contacto?action=delete&id=${c.id}" class="btn btn-danger btn-sm btn-delete">🗑️ Excluir</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    <div class="footer">
        <p>Ambiente: <strong>${ambiente}</strong> | <a href="https://neon.tech" target="_blank">Neon.tech</a> • PostgreSQL</p>
    </div>
</div>
<script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>