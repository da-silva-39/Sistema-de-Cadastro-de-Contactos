<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <title>✏️ Editar Contacto</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <div class="header"><h1>✏️ Editar Contacto</h1></div>
    <div class="content">
        <div class="form-card">
            <form action="${pageContext.request.contextPath}/contacto" method="post">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="id" value="${contacto.id}">
                <div class="form-grid">
                    <div class="form-group"><label>Nome *</label><input type="text" name="nome" value="${contacto.nome}" required></div>
                    <div class="form-group"><label>Email *</label><input type="email" name="email" value="${contacto.email}" required></div>
                    <div class="form-group"><label>Telefone</label><input type="tel" name="telefone" value="${contacto.telefone}"></div>
                    <div class="form-group"><label>Endereço</label><textarea name="endereco">${contacto.endereco}</textarea></div>
                </div>
                <div style="display:flex; gap:10px;">
                    <button type="submit" class="btn btn-primary">💾 Salvar</button>
                    <a href="${pageContext.request.contextPath}/contacto?action=list" class="btn btn-secondary">↩️ Cancelar</a>
                </div>
            </form>
        </div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>