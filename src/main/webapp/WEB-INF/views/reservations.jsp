<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestion des Réservations</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
    <!-- DatePicker CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-datepicker@1.9.0/dist/css/bootstrap-datepicker.min.css">
    <!-- 5 lignes de CSS max ! -->
    <style>
        .table-hover tbody tr:hover { background-color: rgba(0,123,255,0.05); }
        .btn-group-sm>.btn, .btn-sm { padding: .25rem .5rem; }
        .modal-header { background-color: #f8f9fa; }
        .container { max-width: 1400px; }
        .datepicker { z-index: 9999 !important; }
    </style>
</head>
<body class="bg-light">

<div class="container py-4">
    <!-- En-tête -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="text-primary"><i class="bi bi-calendar-check"></i> Gestion des Réservations</h2>
        <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addModal">
            <i class="bi bi-plus-circle"></i> Nouvelle Réservation
        </button>
    </div>

    <!-- Message de succès -->
    <c:if test="${not empty success}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="bi bi-check-circle-fill"></i> ${success}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- Filtres -->
    <div class="card shadow-sm mb-4">
        <div class="card-body">
            <form action="${pageContext.request.contextPath}/reservations" method="get" class="row g-3">
                <div class="col-md-4">
                    <label class="form-label">Filtrer par hôtel</label>
                    <select name="idHotel" class="form-select" onchange="this.form.submit()">
                        <option value="">Tous les hôtels</option>
                        <c:forEach var="h" items="${hotels}">
                            <option value="${h.id}" ${param.idHotel == h.id ? 'selected' : ''}>${h.nom}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-4">
                    <label class="form-label">Date d'arrivée</label>
                    <input type="text" name="date" class="form-control datepicker" value="${param.date}" autocomplete="off" onchange="this.form.submit()">
                </div>
                <div class="col-md-4 d-flex align-items-end">
                    <a href="${pageContext.request.contextPath}/reservations" class="btn btn-secondary">
                        <i class="bi bi-eraser"></i> Réinitialiser
                    </a>
                </div>
            </form>
        </div>
    </div>

    <!-- Tableau des réservations -->
    <div class="card shadow-sm">
        <div class="card-body p-0">
            <table class="table table-hover table-striped mb-0">
                <thead class="table-primary">
                    <tr>
                        <th>ID</th>
                        <th>Client</th>
                        <th>Hôtel</th>
                        <th>Passagers</th>
                        <th>Date d'arrivée</th>
                        <th class="text-center">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="r" items="${reservations}">
                        <tr>
                            <td><span class="badge bg-secondary">${r.id}</span></td>
                            <td><strong>${r.idClient}</strong></td>
                            <td>${r.nomHotel}</td>
                            <td>${r.nbPassager} passager${r.nbPassager > 1 ? 's' : ''}</td>
                            <td>
                                <fmt:parseDate value="${r.dateArrivee}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both"/>
                                <fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy HH:mm"/>
                            </td>
                            <td class="text-center">
                                <button class="btn btn-sm btn-outline-primary" 
                                        onclick="editReservation(${r.id}, '${r.idClient}', ${r.idHotel}, ${r.nbPassager}, ${parsedDate.time})"
                                        data-bs-toggle="modal" data-bs-target="#editModal">
                                    <i class="bi bi-pencil"></i>
                                </button>
                                <a href="${pageContext.request.contextPath}/reservation/delete?id=${r.id}" 
                                   class="btn btn-sm btn-outline-danger" 
                                   onclick="return confirm('Supprimer cette réservation ?')">
                                    <i class="bi bi-trash"></i>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty reservations}">
                        <tr>
                            <td colspan="6" class="text-center text-muted py-4">
                                <i class="bi bi-inbox" style="font-size: 2rem;"></i><br>
                                Aucune réservation trouvée
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- Modal Ajout -->
<div class="modal fade" id="addModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title"><i class="bi bi-plus-circle text-primary"></i> Ajouter une réservation</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <form action="${pageContext.request.contextPath}/reservation/add" method="post">
                <div class="modal-body">
                    <div class="mb-3">
                        <label class="form-label">Client <span class="text-danger">*</span></label>
                        <input type="text" name="idClient" class="form-control" placeholder="ID Client" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Hôtel <span class="text-danger">*</span></label>
                        <select name="idHotel" class="form-select" required>
                            <option value="">-- Sélectionner un hôtel --</option>
                            <c:forEach var="h" items="${hotels}">
                                <option value="${h.id}">${h.nom}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Nombre de passagers <span class="text-danger">*</span></label>
                        <input type="number" name="nbPassager" class="form-control" min="1" value="1" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Date d'arrivée <span class="text-danger">*</span></label>
                        <input type="datetime-local" name="dateArrivee" class="form-control" required>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Annuler</button>
                    <button type="submit" class="btn btn-primary">Enregistrer</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Modal Modification -->
<div class="modal fade" id="editModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title"><i class="bi bi-pencil text-primary"></i> Modifier la réservation</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <form action="${pageContext.request.contextPath}/reservation/update" method="post">
                <div class="modal-body">
                    <input type="hidden" name="id" id="edit-id">
                    <div class="mb-3">
                        <label class="form-label">Client <span class="text-danger">*</span></label>
                        <input type="text" name="idClient" id="edit-idClient" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Hôtel <span class="text-danger">*</span></label>
                        <select name="idHotel" id="edit-idHotel" class="form-select" required>
                            <option value="">-- Sélectionner un hôtel --</option>
                            <c:forEach var="h" items="${hotels}">
                                <option value="${h.id}">${h.nom}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Nombre de passagers <span class="text-danger">*</span></label>
                        <input type="number" name="nbPassager" id="edit-nbPassager" class="form-control" min="1" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Date d'arrivée <span class="text-danger">*</span></label>
                        <input type="datetime-local" name="dateArrivee" id="edit-dateArrivee" class="form-control" required>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Annuler</button>
                    <button type="submit" class="btn btn-primary">Modifier</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<!-- DatePicker JS -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap-datepicker@1.9.0/dist/js/bootstrap-datepicker.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap-datepicker@1.9.0/dist/locales/bootstrap-datepicker.fr.min.js"></script>

<script>
// Initialisation du datepicker
$(document).ready(function() {
    $('.datepicker').datepicker({
        format: 'yyyy-mm-dd',
        language: 'fr',
        autoclose: true,
        todayHighlight: true
    });
});
function editReservation(id, idClient, idHotel, nbPassager, timestamp) {
    
    document.getElementById('edit-id').value = id;
    document.getElementById('edit-idClient').value = idClient;
    document.getElementById('edit-idHotel').value = idHotel;
    document.getElementById('edit-nbPassager').value = nbPassager;
    
    if (!timestamp) {
        console.error('Date non valide');
        return;
    }
    
    let date = new Date(timestamp);
    console.log("date --");
    console.log(date);
    let year = date.getFullYear();
    let month = String(date.getMonth() + 1).padStart(2, '0');
    let day = String(date.getDate()).padStart(2, '0');
    let hours = String(date.getHours()).padStart(2, '0');
    let minutes = String(date.getMinutes()).padStart(2, '0');
    console.log("year");
    console.log(year);
    console.log("month");
    console.log(month);
    console.log("day");
    console.log(day);
    console.log("hours");
    console.log(hours);
    console.log("minutes");
    console.log(minutes);
    let formattedDate = year + '-' + month + '-' + day + 'T' + hours + ':' + minutes;
    console.log("formattedDate");
    console.log(formattedDate);
    document.getElementById('edit-dateArrivee').value = formattedDate;
}
</script>

</body>
</html>