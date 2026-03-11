package service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import framework.ModelView;
import model.*;

public class ReservationService {

public ModelView assignerVehicule(LocalDate dateDebut, LocalDate dateFin, List<Reservation> reservations, List<Vehicule> vehicules, Param p) {
    List<Reservation> reservationsSansVehicule = new ArrayList<>(reservations);
    List<Reservation> reservationsAssignees = new ArrayList<>();

    
    reservations.sort(
    Comparator.comparing(Reservation::getDateArrivee)
              .thenComparing(Reservation::getNbPassager, Comparator.reverseOrder())
);

   
    Map<LocalDateTime, List<Reservation>> groupes = new TreeMap<>();
    for (Reservation r : reservations) {
        LocalDateTime cle = r.getDateArrivee().truncatedTo(ChronoUnit.MINUTES);
        groupes.computeIfAbsent(cle, k -> new ArrayList<>()).add(r);
    }
for (List<Reservation> listeDuGroupe : groupes.values()) {
        listeDuGroupe.sort((r1, r2) -> Integer.compare(r2.getNbPassager(), r1.getNbPassager()));
    }
    
    for (List<Reservation> groupe : groupes.values()) {

    for (Reservation r : groupe) {

        // utiliser ta fonction pour trouver un véhicule
        Vehicule vehiculeChoisi = r.getVehiculeApproprie(vehicules);

        if (vehiculeChoisi != null) {

            if (vehiculeChoisi.getReservationsAssign() == null) {
                vehiculeChoisi.setReservationsAssign(new ArrayList<>());
            }

            vehiculeChoisi.getReservationsAssign().add(r);
            reservationsAssignees.add(r);

            // System.out.println("Vehicule choisi: " + vehiculeChoisi.getReference() +
            //                    " pour reservation #" + r.getId());
        }
    }

}

    // 4. Nettoyage et préparation de la réponse
    reservationsSansVehicule.removeAll(reservationsAssignees);

    List<Vehicule> vehiculesUtilises = new ArrayList<>();
    for (Vehicule v : vehicules) {
        if (v.getReservationsAssign() != null && !v.getReservationsAssign().isEmpty()) {
            v.getdateretourAssign(); // Calcul de l'heure de retour
            vehiculesUtilises.add(v);
        }
    }

    ModelView mv = new ModelView("jsonView.jsp");
    mv.addObject("vehicules", vehiculesUtilises);
    mv.addObject("reservationsSansVehicule", reservationsSansVehicule);

    return mv;
}


public int getNombrePassagerTotal(List<Reservation> reservations) {
        return reservations.stream().mapToInt(Reservation::getNbPassager).sum();
    }
}