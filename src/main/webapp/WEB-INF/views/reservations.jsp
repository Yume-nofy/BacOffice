<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Gestion des Réservations</title>
    <style>
        body { font-family: sans-serif; background-color: #f4f4f4; }
        table { border-collapse: collapse; width: 90%; margin: 20px auto; background: white; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }
        th, td { border: 1px solid #ddd; padding: 12px; text-align: center; }
        th { background-color: #007bff; color: white; }
        tr:nth-child(even) { background-color: #f9f9f9; }
        form { background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); width: 400px; margin: 20px auto; }
        label { display: block; margin-top: 10px; font-weight: bold; }
        input, select { width: 100%; padding: 8px; margin-top: 5px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; }
        input[type="submit"] { background-color: #28a745; color: white; border: none; cursor: pointer; margin-top: 15px; font-size: 16px; }
        input[type="submit"]:hover { background-color: #218838; }
        .container { text-align: center; }
    </style>
</head>
<body>

<div class="container">
    <h2>Liste des Réservations</h2>
</div>

<table>
    <thead>
        <tr>
            <th>ID</th>
            <th>ID Client </th>
            <th>ID Hôtel</th>
            <th>Nombre de Passagers</th>
            <th>Date d'Arrivée</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="res" items="${reservations}">
            <tr>
                <td>${res.id}</td>
                <td><strong>${res.idClient}</strong></td>
                <td>${res.idHotel}</td>
                <td>${res.nbPassager}</td>
                <td>${res.dateArrivee}</td>
            </tr>
        </c:forEach>
        <c:if test="${empty reservations}">
            <tr>
                <td colspan="5">Aucune réservation trouvée.</td>
            </tr>
        </c:if>
    </tbody>
</table>

<hr style="width: 80%; margin: 40px auto;">

<div class="container">
    <h3>Ajouter une nouvelle Réservation</h3>
</div>

<form action="${pageContext.request.contextPath}/reservation/add" method="post">
    <label>ID Client (String):</label>
    <input type="text" name="idClient" placeholder="Ex: FB-12345" required />

    <label>Sélectionner l'Hôtel:</label>
    <select name="idHotel" required>
        <option value="">-- Choisir un hôtel --</option>
        <c:forEach var="h" items="${hotels}">
            <option value="${h.id}">${h.nom}</option>
        </c:forEach>
    </select>

    <label>Nombre de Passagers:</label>
    <input type="number" name="nbPassager" min="1" required />

    <label>Date d'Arrivée:</label>
    <input type="datetime-local" name="dateArrivee" required />

    <input type="submit" value="Enregistrer la réservation" />
</form>

</body>
</html>