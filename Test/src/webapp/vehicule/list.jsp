<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.backoffice.model.Vehicule" %>
<%@ page import="com.example.backoffice.model.TypeCarburant" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<%
    String message = (String) request.getAttribute("message");
    String error = (String) request.getAttribute("error");
    List<Vehicule> vehicules = (List<Vehicule>) request.getAttribute("vehicules");
    List<TypeCarburant> types = (List<TypeCarburant>) request.getAttribute("typeCarburants");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    
    // Statistiques (Calculées ici pour être affichées en bas)
    int totalVehicules = vehicules != null ? vehicules.size() : 0;
    int totalCapacite = vehicules != null ? vehicules.stream().mapToInt(Vehicule::getCapacite).sum() : 0;
    int typesCarburant = types != null ? types.size() : 0;
%>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestion des Véhicules | Parc Auto</title>
    
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dark-theme.css">
    
    <style>
        .gradient-bg { background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%); }
        .animate-slide-in { animation: slideIn 0.5s ease-out forwards; }
        @keyframes slideIn { from { opacity: 0; transform: translateY(20px); } to { opacity: 1; transform: translateY(0); } }
        .vehicle-card:hover { transform: translateY(-4px); transition: all 0.3s ease; }
    </style>
</head>
<body class="bg-gray-50 text-gray-800">

    <nav class="gradient-bg text-white shadow-lg sticky top-0 z-50">
        <div class="max-w-7xl mx-auto px-4 h-16 flex justify-between items-center">
            <div class="flex items-center space-x-4">
                
                <h1 class="text-xl font-bold">Gestion du Parc Automobile</h1>
            </div>
            <span class="bg-white/20 px-3 py-1 rounded-full text-sm">
                <i class="far fa-clock mr-1"></i> <%= LocalDateTime.now().format(timeFormatter) %>
            </span>
        </div>
    </nav>

    <%@ include file="../partial/navigation.jsp" %>

    <main class="ml-64 p-8">
        <div class="max-w-7xl mx-auto animate-slide-in">
            
            <div class="mb-8 flex flex-col md:flex-row md:items-center md:justify-between gap-4">
                <div>
                    <h2 class="text-3xl font-extrabold text-gray-900">Flotte de Véhicules</h2>
                    <p class="text-gray-600 italic">ETU-3313 / 3305 / 3294 - Antananarivo</p>
                </div>
                <button onclick="openAddModal()" 
                        class="gradient-bg text-white px-6 py-3 rounded-xl font-semibold shadow-lg hover:opacity-90 transform hover:scale-105 transition-all flex items-center">
                    <i class="fas fa-plus-circle mr-2"></i> Nouveau véhicule
                </button>
            </div>

            <% if (message != null) { %>
                <div class="mb-6 bg-green-100 border-l-4 border-green-500 p-4 text-green-700 flex justify-between">
                    <span><i class="fas fa-check-circle mr-2"></i> <%= message %></span>
                    <button onclick="this.parentElement.remove()"><i class="fas fa-times"></i></button>
                </div>
            <% } %>

            <div class="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden mb-12">
                <div class="px-6 py-4 bg-gray-50 border-b flex justify-between items-center font-bold">
                    <span><i class="fas fa-list text-blue-500 mr-2"></i> Inventaire</span>
                </div>
                <div class="overflow-x-auto">
                    <table class="min-w-full divide-y divide-gray-200">
                        <thead class="bg-gray-50 text-xs font-bold text-gray-500 uppercase tracking-wider">
                            <tr>
                                <th class="px-6 py-4 text-left">ID</th>
                                <th class="px-6 py-4 text-left">Référence</th>
                                <th class="px-6 py-4 text-left">Capacité</th>
                                <th class="px-6 py-4 text-left">Carburant</th>
                                <th class="px-6 py-4 text-center">Actions</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-gray-100 bg-white text-sm">
                            <% if (vehicules != null && !vehicules.isEmpty()) { 
                                for (Vehicule v : vehicules) { 
                            %>
                                <tr class="hover:bg-gray-50 transition-colors">
                                    <td class="px-6 py-4 font-bold text-blue-600">#<%= v.getId() %></td>
                                    <td class="px-6 py-4 font-medium"><%= v.getReference() %></td>
                                    <td class="px-6 py-4">
                                        <span class="px-3 py-1 bg-blue-50 text-blue-700 rounded-full text-xs font-bold border border-blue-100">
                                            <i class="fas fa-users mr-1"></i> <%= v.getCapacite() %> places
                                        </span>
                                    </td>
                                    <td class="px-6 py-4">
                                        <% if (v.getTypeCarburant() != null) { %>
                                            <span class="px-2 py-1 bg-gray-100 rounded text-xs">
                                                <i class="fas fa-gas-pump mr-1"></i> <%= v.getTypeCarburant().getLibelle() %>
                                            </span>
                                        <% } %>
                                    </td>
                                    <td class="px-6 py-4 text-center space-x-3">
                                        <button onclick="openEditModal(<%= v.getId() %>, '<%= v.getReference() %>', <%= v.getCapacite() %>, <%= v.getTypeCarburant().getId() %>)" 
                                                class="text-indigo-600 hover:scale-120 transition-transform">
                                            <i class="fas fa-edit"></i>
                                        </button>
                                        <form action="${pageContext.request.contextPath}/vehicules" method="post" class="inline">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="id" value="<%= v.getId() %>">
                                            <button type="submit" onclick="return confirm('Supprimer ce véhicule ?')" class="text-red-600">
                                                <i class="fas fa-trash"></i>
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            <% } } %>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mt-8 animate-slide-in">
                <div class="bg-white rounded-xl shadow-md p-6 border-l-4 border-blue-500 vehicle-card flex justify-between items-center">
                    <div>
                        <p class="text-xs font-bold text-gray-400 uppercase">Total Véhicules</p>
                        <p class="text-3xl font-black text-gray-900 mt-1"><%= totalVehicules %></p>
                    </div>
                    <i class="fas fa-car text-blue-100 text-4xl"></i>
                </div>
                
                <div class="bg-white rounded-xl shadow-md p-6 border-l-4 border-green-500 vehicle-card flex justify-between items-center">
                    <div>
                        <p class="text-xs font-bold text-gray-400 uppercase">Sièges Disponibles</p>
                        <p class="text-3xl font-black text-gray-900 mt-1"><%= totalCapacite %></p>
                    </div>
                    <i class="fas fa-users text-green-100 text-4xl"></i>
                </div>
                
                <div class="bg-white rounded-xl shadow-md p-6 border-l-4 border-purple-500 vehicle-card flex justify-between items-center">
                    <div>
                        <p class="text-xs font-bold text-gray-400 uppercase">Types Énergie</p>
                        <p class="text-3xl font-black text-gray-900 mt-1"><%= typesCarburant %></p>
                    </div>
                    <i class="fas fa-gas-pump text-purple-100 text-4xl"></i>
                </div>
            </div>

        </div>
    </main>

    <div id="vehicleModal" class="fixed inset-0 z-50 hidden bg-black/50 flex items-center justify-center p-4">
        <div class="bg-white rounded-2xl shadow-2xl w-full max-w-md overflow-hidden transform transition-all animate-slide-in">
            <div class="gradient-bg px-6 py-4 text-white flex justify-between items-center">
                <h3 id="modalTitle" class="font-bold">Nouveau véhicule</h3>
                <button onclick="closeModal()"><i class="fas fa-times"></i></button>
            </div>
            <form action="${pageContext.request.contextPath}/vehicules" method="post" class="p-6 space-y-4">
                <input type="hidden" name="action" id="formAction" value="create">
                <input type="hidden" name="id" id="vehicleId">
                
                <div>
                    <label class="block text-xs font-bold text-gray-500 uppercase mb-1">Référence</label>
                    <input type="text" name="reference" id="reference" required class="w-full border rounded-lg px-4 py-2 focus:ring-2 focus:ring-blue-500 outline-none">
                </div>
                
                <div>
                    <label class="block text-xs font-bold text-gray-500 uppercase mb-1">Capacité (Passagers)</label>
                    <input type="number" name="capacite" id="capacite" required min="1" class="w-full border rounded-lg px-4 py-2 focus:ring-2 focus:ring-blue-500 outline-none">
                </div>
                
                <div>
                    <label class="block text-xs font-bold text-gray-500 uppercase mb-1">Type Carburant</label>
                    <select name="idTypeCarburant" id="idTypeCarburant" required class="w-full border rounded-lg px-4 py-2 outline-none">
                        <% if (types != null) { for (TypeCarburant t : types) { %>
                            <option value="<%= t.getId() %>"><%= t.getLibelle() %></option>
                        <% } } %>
                    </select>
                </div>

                <div class="pt-4 flex justify-end space-x-3">
                    <button type="button" onclick="closeModal()" class="px-4 py-2 text-gray-400">Annuler</button>
                    <button type="submit" class="gradient-bg text-white px-6 py-2 rounded-lg font-bold">Enregistrer</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        function openAddModal() {
            document.getElementById('modalTitle').innerText = "Ajouter un véhicule";
            document.getElementById('formAction').value = "create";
            document.getElementById('vehicleId').value = "";
            document.getElementById('reference').value = "";
            document.getElementById('capacite').value = "";
            document.getElementById('vehicleModal').classList.remove('hidden');
        }

        function openEditModal(id, ref, cap, fuelId) {
            document.getElementById('modalTitle').innerText = "Modifier le véhicule #" + id;
            document.getElementById('formAction').value = "update";
            document.getElementById('vehicleId').value = id;
            document.getElementById('reference').value = ref;
            document.getElementById('capacite').value = cap;
            document.getElementById('idTypeCarburant').value = fuelId;
            document.getElementById('vehicleModal').classList.remove('hidden');
        }

        function closeModal() {
            document.getElementById('vehicleModal').classList.add('hidden');
        }
    </script>

    <footer class="max-w-7xl mx-auto px-8 py-8 mt-12 border-t text-center text-gray-400 text-xs">
        &copy; <%= java.time.LocalDate.now().getYear() %>  Antananarivo Nakah.
    </footer>
</body>
</html>