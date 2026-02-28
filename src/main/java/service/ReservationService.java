package service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import framework.ModelView;
import model.*;

public class ReservationService {

    public ModelView assignerVehicule(LocalDate dateDebut, LocalDate dateFin, List<Reservation> reservations , List<Vehicule> vehicules, Param p) {
        List<Reservation> reservationsSansVehicule = new ArrayList<>();

        Map<String,List<Reservation>> group = new HashMap<>();

        LocalDateTime tempsReservation1 = reservations.get(0).getDateArrivee();
        List<Reservation> rv = new ArrayList<>();
        Reservation avant = reservations.get(0);
        
        for (int i = 0; i < reservations.size(); i++) {
            Reservation reservation = reservations.get(i);
            
            if (i < reservations.size() - 1 && tempsReservation1.plusMinutes(p.getTemps_attente()).compareTo(reservations.get(i+1).getDateArrivee()) >= 0 && avant.getIdHotel()==reservation.getIdHotel()) {
                rv.add(reservation);
                avant=reservation;
            } else {
                rv.add(reservation);
                group.put("group" + i, new ArrayList<>(rv));
                rv.clear();
                if (i < reservations.size() - 1) {
                    tempsReservation1 = reservations.get(i+1).getDateArrivee();
                }
            }
        }

        int indiceVehicule = 0;
        Set<Reservation> reservationsAssignees = new HashSet<>();
        
        for (int i = 0; i < group.size(); i++) {
            List<Reservation> rs = group.get("group" + i);
            if (rs != null && !rs.isEmpty() && indiceVehicule < vehicules.size()) {
                Vehicule v = vehicules.get(indiceVehicule);
                if (getNombrePassagerTotal(rs) <= v.getNbrPlace()) {
                    v.setReservationsAssign(rs);
                    reservationsAssignees.addAll(rs);
                    indiceVehicule++;
                }
            }
        }

        for (List<Reservation> groupeReservations : group.values()) {
            for (Reservation reservation : groupeReservations) {
                if (!reservationsAssignees.contains(reservation)) {
                    reservationsSansVehicule.add(reservation);
                }
            }
        }

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
