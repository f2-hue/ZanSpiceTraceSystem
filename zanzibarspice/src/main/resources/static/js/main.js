// Confirm before delete or suspend
function confirmAction(message) {
    return confirm(message);
}

// Auto hide alerts after 4 seconds
document.addEventListener("DOMContentLoaded", function () {
    var alerts = document.querySelectorAll(".alert");
    alerts.forEach(function (alert) {
        setTimeout(function () {
            alert.style.display = "none";
        }, 4000);
    });
});

// Preview QR image if path exists
function showQR(path) {
    var img = document.getElementById("qr-preview");
    if (img && path) {
        img.src = "/" + path;
        img.style.display = "block";
    }
}