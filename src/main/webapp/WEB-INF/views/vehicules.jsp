<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestion des Vehicules</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
    <!-- 5 lignes de CSS max ! -->
    <style>
        .table-hover tbody tr:hover { background-color: rgba(0,123,255,0.05); }
        .btn-group-sm>.btn, .btn-sm { padding: .25rem .5rem; }
        .modal-header { background-color: #f8f9fa; }
        .container { max-width: 1400px; }
    </style>
</head>
<body class="bg-light">

<div class="container py-4">
    <!-- En-tête -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="text-primary"><i class="bi bi-truck"></i> Gestion des Vehicules</h2>
        <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addModal">
            <i class="bi bi-plus-circle"></i> Nouveau Vehicule
        </button>
    </div>

    <!-- Message de succès -->
    <c:if test="${not empty success}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="bi bi-check-circle-fill"></i> ${success}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- Tableau des vehicules -->
    <div class="card shadow-sm">
        <div class="card-body p-0">
            <table class="table table-hover table-striped mb-0">
                <thead class="table-primary">
                    <tr>
                        <th>ID</th>
                        <th>Reference</th>
                        <th>Type Carburant</th>
                        <th>Nombre de places</th>
                        <th class="text-center">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="v" items="${vehicules}">
                        <tr>
                            <td><span class="badge bg-secondary">${v.id}</span></td>
                            <td><strong>${v.reference}</strong></td>
                            <td>
                                <c:choose>
                                    <c:when test="${v.typeCarburant == 'D'}">
                                        <span class="badge bg-dark">Diesel</span>
                                    </c:when>
                                    <c:when test="${v.typeCarburant == 'Es'}">
                                        <span class="badge bg-success">Essence</span>
                                    </c:when>
                                    <c:when test="${v.typeCarburant == 'El'}">
                                        <span class="badge bg-info text-dark">electrique</span>
                                    </c:when>
                                </c:choose>
                            </td>
                            <td>${v.nbrPlace} place${v.nbrPlace > 1 ? 's' : ''}</td>
                            <td class="text-center">
                                <button class="btn btn-sm btn-outline-primary" 
                                        onclick="editVehicule(${v.id}, '${v.reference}', '${v.typeCarburant}', ${v.nbrPlace})"
                                        data-bs-toggle="modal" data-bs-target="#editModal">
                                    <i class="bi bi-pencil"></i>
                                </button>
                                <a href="${pageContext.request.contextPath}/vehicule/delete?id=${v.id}" 
                                   class="btn btn-sm btn-outline-danger" 
                                   onclick="return confirm('Supprimer ce vehicule ?')">
                                    <i class="bi bi-trash"></i>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty vehicules}">
                        <tr>
                            <td colspan="5" class="text-center text-muted py-4">
                                <i class="bi bi-inbox" style="font-size: 2rem;"></i><br>
                                Aucun vehicule trouve
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
                <h5 class="modal-title"><i class="bi bi-plus-circle text-primary"></i> Ajouter un vehicule</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <form action="${pageContext.request.contextPath}/vehicule/add" method="post">
                <div class="modal-body">
                    <div class="mb-3">
                        <label class="form-label">Reference</label>
                        <input type="text" name="reference" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Type de carburant</label>
                        <select name="typeCarburant" class="form-select" required>
                            <option value="">-- Selectionner --</option>
                            <option value="D">Diesel</option>
                            <option value="Es">Essence</option>
                            <option value="El">electrique</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Nombre de places</label>
                        <input type="number" name="nbrPlace" class="form-control" min="1" required>
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
                <h5 class="modal-title"><i class="bi bi-pencil text-primary"></i> Modifier le vehicule</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <form action="${pageContext.request.contextPath}/vehicule/update" method="post">
                <div class="modal-body">
                    <input type="hidden" name="id" id="edit-id">
                    <div class="mb-3">
                        <label class="form-label">Reference</label>
                        <input type="text" name="reference" id="edit-reference" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Type de carburant</label>
                        <select name="typeCarburant" id="edit-typeCarburant" class="form-select" required>
                            <option value="D">Diesel</option>
                            <option value="Es">Essence</option>
                            <option value="El">electrique</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Nombre de places</label>
                        <input type="number" name="nbrPlace" id="edit-nbrPlace" class="form-control" min="1" required>
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

<script>
function editVehicule(id, reference, typeCarburant, nbrPlace) {
    document.getElementById('edit-id').value = id;
    document.getElementById('edit-reference').value = reference;
    document.getElementById('edit-typeCarburant').value = typeCarburant;
    document.getElementById('edit-nbrPlace').value = nbrPlace;
}
</script>

</body>
</html>