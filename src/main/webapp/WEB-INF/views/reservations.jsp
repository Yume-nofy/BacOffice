<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Liste des Réservations</title>
    <style>
        table {
            border-collapse: collapse;
            width: 80%;
            margin: 20px auto;
        }
        th, td {
            border: 1px solid #333;
            padding: 8px;
            text-align: center;
        }
        th {
            background-color: #555;
            color: white;
        }
        form {
            margin: 20px auto;
            width: 80%;
        }
        input, select {
            padding: 5px;
            margin: 5px;
        }
    </style>
</head>
<body>
<h2 style="text-align:center;">Liste des Réservations</h2>

<!-- Tableau des réservations -->
<table>
    <thead>
        <tr>
            <th>ID</th>
            <th>ID Client</th>
            <th>ID Hôtel</th>
            <th>Nombre de Passagers</th>
            <th>Date d'Arrivée</th>
        </tr>
    </thead>
    <tbody>
    <c:forEach var="res" items="${reservations}">
        <tr>
            <td>${res.id}</td>
            <td>${res.idClient}</td>
            <td>${res.idHotel}</td>
            <td>${res.nbPassager}</td>
            <td>${res.dateArrivee}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<!-- Formulaire pour ajouter une réservation -->
<h3 style="text-align:center;">Ajouter une Réservation</h3>
<form action="reservation/add" method="post">
    <label>ID Client:</label>
    <input type="text" name="idClient" required />
    <label>ID Hôtel:</label>
    <input type="number" name="idHotel" required />
    <label>Nombre de Passagers:</label>
    <input type="number" name="nbPassager" required />
    <label>Date d'Arrivée:</label>
    <input type="datetime-local" name="dateArrivee" required />
    <input type="submit" value="Ajouter" />
</form>

</body>
</html>
