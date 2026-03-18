package service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import framework.ModelView;
import model.*;

public class ReservationService {

    public ModelView assignerVehicule(LocalDate dateDebut, LocalDate dateFin, List<Reservation> reservations,
            List<Vehicule> vehicules, Param p) {
        List<Reservation> reservationsSansVehicule = new ArrayList<>(reservations);
        List<Reservation> reservationsAssignees = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm:ss");

        
        reservations.sort(
                Comparator.comparing(Reservation::getDateArrivee)
                        .thenComparing(Reservation::getNbPassager, Comparator.reverseOrder()));
        double dureeDattente = p.getTemps_attente();        

        Map<LocalDateTime, List<Reservation>> groupes = new TreeMap<>();

        if (!reservations.isEmpty()) {
            LocalDateTime debutGroupe = reservations.get(0).getDateArrivee();
            List<Reservation> groupeActuel = new ArrayList<>();
            
            for (Reservation r : reservations) {
                if (r.getDateArrivee().isBefore(debutGroupe.plusMinutes((long) dureeDattente))) {
                    groupeActuel.add(r);
                } else {
                    groupes.put(debutGroupe.truncatedTo(ChronoUnit.MINUTES), new ArrayList<>(groupeActuel));                    
                    debutGroupe = r.getDateArrivee();
                    groupeActuel = new ArrayList<>();
                    groupeActuel.add(r);
                }
            }            
            if (!groupeActuel.isEmpty()) {
                groupes.put(debutGroupe.truncatedTo(ChronoUnit.MINUTES), groupeActuel);
            }
        }

        for (List<Reservation> listeDuGroupe : groupes.values()) {
            listeDuGroupe.sort((r1, r2) -> Integer.compare(r2.getNbPassager(), r1.getNbPassager()));
        }

        for (List<Reservation> groupe : groupes.values()) {
            // System.out.println("Groupe pour " + groupe.get(0).getDateArrivee());
            LocalDateTime datDepart = LocalDateTime.parse("11/11/11 11:11:11", formatter);
            List<Vehicule> ves = new ArrayList<>();
            for (int i = 0; i < groupe.size(); i++) {
                // System.out.println("Traitement de la réservation : " + groupe.get(i));
                Reservation r = groupe.get(i);
                Vehicule vehiculeChoisi = r.getVehiculeApproprie(vehicules, groupe.get(0).getDateArrivee(), groupe.get(0).getDateArrivee().plusMinutes((long) dureeDattente));
                ves.add(vehiculeChoisi);
                if (datDepart.isBefore(r.getDateArrivee())) {
                    datDepart=r.getDateArrivee();
                }
                if (datDepart.isBefore(vehiculeChoisi.getDateRetour())) {
                    datDepart=r.getDateArrivee();
                }
            
                if (vehiculeChoisi != null) {
                    if (vehiculeChoisi.getReservationsAssign() == null) {
                        vehiculeChoisi.setReservationsAssign(new ArrayList<>());
                    }

                    vehiculeChoisi.getReservationsAssign().add(r);
                    
                    groupe.remove(r);
                    i--;
                    vehiculeChoisi.remplirReservation(groupe, reservationsAssignees);
                    reservationsAssignees.add(r);

                }
            }
            for (Vehicule v: ves) {
                v.setDateDepart(datDepart);
            }

        }

        reservationsSansVehicule.removeAll(reservationsAssignees);

        List<Vehicule> vehiculesUtilises = new ArrayList<>();
        for (Vehicule v : vehicules) {
            if (v.getReservationsAssign() != null && !v.getReservationsAssign().isEmpty()) {
                v.getdateretourAssign();
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