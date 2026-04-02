<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<div class="w-64 h-screen bg-gray-100 shadow-lg fixed left-0 top-0 flex flex-col p-6 animate-fade-in border-r border-gray-200">

    <div class="mb-10 mt-16">
        <h2 class="text-xs font-bold text-gray-400 uppercase tracking-widest px-2">Menu Principal</h2>
    </div>

    <nav class="flex flex-col space-y-2">

        <a href="${pageContext.request.contextPath}/vehicules"
           class="block text-gray-600 font-medium hover:text-blue-600 hover:bg-blue-50 px-4 py-3 rounded-lg transition-all">
            Véhicules
        </a>

        <a href="${pageContext.request.contextPath}/trajets"
           class="block text-gray-600 font-medium hover:text-blue-600 hover:bg-blue-50 px-4 py-3 rounded-lg transition-all">
            Trajets
        </a>

        <a href="${pageContext.request.contextPath}/trajets/planifier"
           class="block text-gray-600 font-medium hover:text-blue-600 hover:bg-blue-50 px-4 py-3 rounded-lg transition-all">
            Planification
        </a>

        <a href="${pageContext.request.contextPath}/reservation/form"
           class="block text-gray-600 font-medium hover:text-blue-600 hover:bg-blue-50 px-4 py-3 rounded-lg transition-all">
            Réservation
        </a>

        <a href="${pageContext.request.contextPath}/reservations/non-assigner"
           class="block text-gray-600 font-medium hover:text-red-600 hover:bg-red-50 px-4 py-3 rounded-lg transition-all">
            Non assignées
        </a>

    </nav>

</div>