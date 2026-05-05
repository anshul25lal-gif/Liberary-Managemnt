// Delete confirmation modal — wires up the delete button with dynamic URL and item name
document.addEventListener('DOMContentLoaded', function () {
    const deleteModal = document.getElementById('deleteModal');
    if (deleteModal) {
        deleteModal.addEventListener('show.bs.modal', function (event) {
            const trigger = event.relatedTarget;
            const url = trigger.getAttribute('data-delete-url');
            const name = trigger.getAttribute('data-item-name');
            document.getElementById('deleteConfirmBtn').setAttribute('href', url);
            document.getElementById('deleteItemName').textContent = name;
        });
    }

    // Auto-dismiss alerts after 4 seconds
    document.querySelectorAll('.alert.alert-success, .alert.alert-danger').forEach(function (alert) {
        setTimeout(function () {
            const bsAlert = bootstrap.Alert.getOrCreateInstance(alert);
            bsAlert.close();
        }, 4000);
    });

    // Highlight active navbar link
    const currentPath = window.location.pathname;
    document.querySelectorAll('.navbar .nav-link').forEach(function (link) {
        if (link.getAttribute('href') && currentPath.startsWith(link.getAttribute('href')) && link.getAttribute('href') !== '/') {
            link.classList.add('active', 'fw-semibold');
        }
    });
});
