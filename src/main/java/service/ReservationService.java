package service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import framework.ModelView;
import model.*;

public class ReservationService {

    public ModelView assignerVehicule(LocalDate dateDebut, LocalDate dateFin, 
                                    List<Reservation> reservations, 
                                    List<Vehicule> vehicules, Param p) {
        
        List<Reservation> reservationsSansVehicule = new ArrayList<>(reservations);
        List<Reservation> reservationsAssignees = new ArrayList<>();

        for (Reservation reservation : reservations) {            
            for (Vehicule vehicule : vehicules) {
                if (reservation.getNbPassager() <= vehicule.getNbrPlaceDisponible()) {
                    if (vehicule.getReservationsAssign() == null) {
                        vehicule.setReservationsAssign(new ArrayList<>());
                    }
                    
                    vehicule.getReservationsAssign().add(reservation);
                    reservationsAssignees.add(reservation);
                    break; 
                }
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
