<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.backoffice.dto.TrajetDTO" %>
<%@ page import="com.example.backoffice.model.*" %>
<%@ page import="java.time.*" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<%
    List<TrajetDTO> trajets = (List<TrajetDTO>) request.getAttribute("trajets");
    String date = request.getParameter("date");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    
    int totalTrajets = trajets != null ? trajets.size() : 0;
    double totalDistance = 0;
    int totalReservations = 0;
    
    if (trajets != null) {
        for (TrajetDTO t : trajets) {
            if (t.getDistance() != null) totalDistance += t.getDistance();
            if (t.getTrajetReservations() != null) totalReservations += t.getTrajetReservations().size();
        }
    }
%>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestion des Trajets | Parc Auto</title>
    
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dark-theme.css">
    
    <style>
        .gradient-bg { background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%); }
        .animate-fade-in { animation: fadeIn 0.4s ease-out forwards; opacity: 0; }
        @keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }
        .trajet-row:hover { background-color: rgba(243, 244, 246, 1); }
    </style>
</head>
<body class="bg-gray-50 text-gray-800">

    <nav class="gradient-bg text-white shadow-lg sticky top-0 z-50">
        <div class="max-w-7xl mx-auto px-4 h-16 flex justify-between items-center">
            <div class="flex items-center space-x-3">
                
                <span class="font-bold text-lg">Gestion Parc Automobile</span>
            </div>
            <span class="bg-white/20 px-3 py-1 rounded-full text-xs">
                <%= LocalDateTime.now().format(timeFormatter) %>
            </span>
        </div>
    </nav>
    
    <%@ include file="../partial/navigation.jsp" %>

    <main class="ml-64 p-8">
        <div class="max-w-7xl mx-auto">
            
            <div class="mb-8 flex justify-between items-end">
                <div>
                    <h2 class="text-3xl font-extrabold text-gray-900 italic">ETU-3313 / 3305 / 3294</h2>
                    <p class="text-gray-500">Registre complet des trajets et affectations</p>
                </div>
                <form method="get" class="flex space-x-2 bg-white p-2 rounded-lg border shadow-sm">
                    <input type="date" name="date" value="<%= date != null ? date : LocalDate.now() %>" class="border-none focus:ring-0">
                    <button class="gradient-bg text-white px-4 py-2 rounded-md hover:brightness-110">
                        Filtrer
                    </button>
                </form>
            </div>

            <div class="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden mb-10">
                <table class="min-w-full divide-y divide-gray-200">
                    <thead class="bg-gray-100">
                        <tr class="text-left text-xs font-bold text-gray-600 uppercase tracking-widest">
                            <th class="px-6 py-4">Trajet</th>
                            <th class="px-6 py-4">Véhicule & Capacité</th>
                            <th class="px-6 py-4">Réservations en cours</th>
                        </tr>
                    </thead>
                    <tbody class="divide-y divide-gray-100">
                        <% if (trajets != null && !trajets.isEmpty()) { 
                            for (TrajetDTO t : trajets) { 
                                List<TrajetReservation> resList = t.getTrajetReservations();
                        %>
                            <tr class="animate-fade-in trajet-row">
                                <td class="px-6 py-5 align-top">
                                    <div class="text-blue-600 font-bold">#<%= t.getId() %></div>
                                    <div class="font-medium text-gray-800 mt-1"><%= t.getDateTrajet() %></div>
                                    <div class="text-xs text-gray-500 mt-1"><%= t.getHeureDepart() %> - <%= t.getHeureRetour() %></div>
                                </td>

                                <td class="px-6 py-5 align-top">
                                    <% if (t.getVehicule() != null) { %>
                                        <div class="flex flex-col">
                                            <span class="font-bold text-gray-900"><%= t.getVehicule().getReference() %></span>
                                            <span class="text-xs text-gray-500"><%= t.getVehicule().getTypeCarburant().getLibelle() %></span>
                                            <div class="mt-2 flex items-center space-x-2">
                                                <span class="text-[10px] font-bold uppercase px-2 py-0.5 bg-orange-100 text-orange-700 rounded border border-orange-200">
                                                    Capacité: <%= t.getVehicule().getCapacite() %> places
                                                </span>
                                                <span class="text-[10px] font-bold uppercase px-2 py-0.5 bg-blue-50 text-blue-700 rounded border border-blue-200">
                                                    <%= t.getDistance() %> km
                                                </span>
                                            </div>
                                        </div>
                                    <% } %>
                                </td>

                                <td class="px-6 py-5">
                                    <% if(resList != null && !resList.isEmpty()) { %>
                                        <div class="space-y-2">
                                            <% for(TrajetReservation r : resList) { %>
                                                <div class="flex items-center justify-between bg-gray-50 p-2 rounded border border-gray-100">
                                                    <span class="text-xs font-medium text-gray-700">Client <%= r.getReservation().getIdClient() %></span>
                                                    <span class="text-[11px] font-bold bg-white border px-2 py-0.5 rounded shadow-sm text-blue-600">
                                                        <%= r.getNombrePassager() %> passagers
                                                    </span>
                                                </div>
                                            <% } %>
                                        </div>
                                    <% } else { %>
                                        <span class="text-xs text-gray-400 italic">Aucune réservation</span>
                                    <% } %>
                                </td>
                            </tr>
                        <% } } %>
                    </tbody>
                </table>
            </div>

            <div class="grid grid-cols-1 md:grid-cols-3 gap-6 animate-fade-in" style="animation-delay: 0.2s;">
                <div class="bg-white p-6 rounded-xl border-b-4 border-blue-500 shadow-sm flex items-center justify-between">
                    <div>
                        <p class="text-xs font-bold text-gray-400 uppercase">Total Trajets</p>
                        <p class="text-3xl font-black text-gray-900"><%= totalTrajets %></p>
                    </div>
                    <i class="fas fa-map-marked-alt text-blue-100 text-4xl"></i>
                </div>
                
                <div class="bg-white p-6 rounded-xl border-b-4 border-green-500 shadow-sm flex items-center justify-between">
                    <div>
                        <p class="text-xs font-bold text-gray-400 uppercase">Distance Cumulée</p>
                        <p class="text-3xl font-black text-gray-900"><%= String.format("%.1f", totalDistance) %> km</p>
                    </div>
                    <i class="fas fa-road text-green-100 text-4xl"></i>
                </div>

                <div class="bg-white p-6 rounded-xl border-b-4 border-purple-500 shadow-sm flex items-center justify-between">
                    <div>
                        <p class="text-xs font-bold text-gray-400 uppercase">Réservations</p>
                        <p class="text-3xl font-black text-gray-900"><%= totalReservations %></p>
                    </div>
                    <i class="fas fa-ticket-alt text-purple-100 text-4xl"></i>
                </div>
            </div>
        </div>
    </main>

    <footer class="mt-20 py-6 border-t border-gray-200">
        <p class="text-center text-xs text-gray-400">© 2026 Antananarivo Nakah</p>
    </footer>

</body>
</html>