document.addEventListener('DOMContentLoaded', () => {
    // Auto-fechar mensagens após 4s
    document.querySelectorAll('.alert').forEach(alert => {
        setTimeout(() => {
            alert.style.opacity = '0';
            setTimeout(() => alert.remove(), 300);
        }, 4000);
    });
    
    // Confirmação de exclusão
    document.querySelectorAll('.btn-delete').forEach(btn => {
        btn.addEventListener('click', (e) => {
            if (!confirm('⚠️ Tem certeza que deseja excluir este contacto?')) {
                e.preventDefault();
            }
        });
    });
    
    // Desabilitar botão após submit para evitar duplicação
    document.querySelectorAll('form').forEach(form => {
        form.addEventListener('submit', () => {
            const btn = form.querySelector('button[type="submit"]');
            if (btn) {
                btn.disabled = true;
                btn.textContent = '⏳ Processando...';
            }
        });
    });
});