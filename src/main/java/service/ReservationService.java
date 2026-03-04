package service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.time.temporal.ChronoUnit;
import framework.ModelView;
import model.*;
import java.util.Map;
import java.util.TreeMap;
import java.util.HashMap;
import java.time.LocalDateTime;
public class ReservationService {

    public ModelView assignerVehicule(LocalDate dateDebut, LocalDate dateFin, List<Reservation> reservations, List<Vehicule> vehicules, Param p) {
            List<Reservation> reservationsSansVehicule = new ArrayList<>(reservations);
            List<Reservation> reservationsAssignees = new ArrayList<>();
            Map<LocalDateTime, List<Reservation>> groupes = new TreeMap<>();
            for (Reservation r : reservations) {
                LocalDateTime cle = r.getDateArrivee().truncatedTo(ChronoUnit.MINUTES);
                groupes.computeIfAbsent(cle, k -> new ArrayList<>()).add(r);
            }

            for (List<Reservation> groupe : groupes.values()) {
                int totalPassagers = groupe.stream().mapToInt(Reservation::getNbPassager).sum();

                Vehicule meilleurChoix = null;

                for (Vehicule v : vehicules) {
                    if (v.getNbrPlaceDisponible() >= totalPassagers) {
                        boolean compatible = true;

                        if (v.getReservationsAssign() != null && !v.getReservationsAssign().isEmpty()) {
                            Reservation rExist = v.getReservationsAssign().get(0);
                            if (!groupe.get(0).memeReservation(rExist)) {
                                compatible = false; 
                            }
                        }

                        if (compatible) {
                            if (meilleurChoix == null || v.getNbrPlaceDisponible() < meilleurChoix.getNbrPlaceDisponible()) {
                                meilleurChoix = v;
                            }
                        }
                    }
                }

                if (meilleurChoix != null) {
                    if (meilleurChoix.getReservationsAssign() == null) {
                        meilleurChoix.setReservationsAssign(new ArrayList<>());
                    }
                    meilleurChoix.getReservationsAssign().addAll(groupe);
                    reservationsAssignees.addAll(groupe);
                }
            }
        reservationsSansVehicule.removeAll(reservationsAssignees);
        
        vehicules.removeIf(v -> v.getReservationsAssign() == null || v.getReservationsAssign().isEmpty());
        
        ModelView mv = new ModelView("jsonView.jsp");
        mv.addObject("vehicules", vehicules);
        mv.addObject("reservationsSansVehicule", reservationsSansVehicule);
        
        return mv;
    }

    public int getNombrePassagerTotal(List<Reservation> reservations) {
        int total = 0;
        for (Reservation reservation : reservations) {
            total += reservation.getNbPassager();
        }
        return total;
    }
}
