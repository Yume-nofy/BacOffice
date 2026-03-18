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
        List<Vehicule> vehicuFinal = new ArrayList<>();
        reservations.sort(
                Comparator.comparing(Reservation::getDateArrivee)
                        .thenComparing(Reservation::getNbPassager, Comparator.reverseOrder()));
        double dureeDattente = p.getTemps_attente();

        Map<LocalDateTime, List<Reservation>> groupes = new TreeMap<>();

        if (!reservations.isEmpty()) {
            LocalDateTime debutGroupe = reservations.get(0).getDateArrivee();
            List<Reservation> groupeActuel = new ArrayList<>();

            for (Reservation r : reservations) {
                if (!r.getDateArrivee().isAfter(debutGroupe.plusMinutes((long) dureeDattente))) {
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
        List<Reservation> nonasi = new ArrayList<>();

        int k = 1;
        for (List<Reservation> groupe : groupes.values()) {
            if (!nonasi.isEmpty()) {
                groupe.addAll(nonasi);
                nonasi.clear();
            }
            System.out.println("\nGroupe n " + k + " pour " + groupe.get(0).getDateArrivee());
            // System.out.println("Groupe entre " + groupe.get(0).getDateArrivee() + " et "
            // + groupe.get(0).getDateArrivee().plusMinutes((long) dureeDattente));
            LocalDateTime datDepart = LocalDateTime.parse("11/11/11 11:11:11", formatter);
            List<Vehicule> ves = new ArrayList<>();
            for (int i = 0; i < groupe.size(); i++) {
                System.out.println("Traitement de la réservation : " + groupe.get(i));
                Reservation r = groupe.get(i);
                r.setGroup(k);
                Vehicule vehiculeChoisi = r.getVehiculeApproprie(vehicules,
                        groupe.get(0).getDateArrivee().plusMinutes((long) dureeDattente));

                if (datDepart.isBefore(r.getDateArrivee())) {
                    datDepart = r.getDateArrivee();
                }

                if (vehiculeChoisi != null) {
                    System.out.println("Assignation de la réservation #" + r.getId() + " au véhicule "
                            + vehiculeChoisi.getReference() + " referenceuniqueobjet: "
                            + System.identityHashCode(vehiculeChoisi));
                    // System.out.println("Avec date de retour: "+ vehiculeChoisi.getDateRetour());
                    ves.add(vehiculeChoisi);
                    vehicuFinal.add(vehiculeChoisi);
                    // System.out.println(" Set group du véhicule " + vehiculeChoisi.getReference()
                    // + " à " + k);
                    vehiculeChoisi.setGroup(k);
                    // System.out.println("Comparaison date de départ actuelle : " + datDepart + "
                    // avec date de retour du véhicule " + vehiculeChoisi.getReference() + " : " +
                    // vehiculeChoisi.getDateRetour());
                    if (datDepart.isBefore(vehiculeChoisi.getDateRetour())) {
                        datDepart = vehiculeChoisi.getDateRetour();
                    }
                    if (vehiculeChoisi.getReservationsAssign() == null) {
                        vehiculeChoisi.setReservationsAssign(new ArrayList<>());
                    }
                    vehiculeChoisi.getReservationsAssign().add(r);

                    groupe.remove(r);
                    i--;
                    vehiculeChoisi.remplirReservation(groupe, reservationsAssignees);

                    reservationsAssignees.add(r);

                } else {
                    nonasi.add(r);
                }
            }
            for (Vehicule v : ves) {
                // System.out.println("Comparaison date recente du véhicule " + v.getReference()
                // + " : " + v.getdaterecent() + " avec date de départ actuelle : " +
                // datDepart);
                LocalDateTime datRecent = v.getdaterecent();
                if (datRecent.isAfter(datDepart)) {
                    datDepart = datRecent;
                }
            }
            for (Vehicule v : ves) {
                // System.out.println("Départ du véhicule " + v.getReference() + " fixé à " +
                // datDepart);
                v.setDateDepart(datDepart);
                v.getdateretourAssign();
            }
            // for (Reservation r: nonasi) {
            //     System.out.println("Aucune assignation possible pour la réservation #" + r.getId());
            // }
            k++;
        }

        reservationsSansVehicule.removeAll(reservationsAssignees);

        List<Vehicule> vehiculesUtilises = new ArrayList<>();
        System.out.println("\nVéhicules utilisés  Final:");
        for (Vehicule v : vehicuFinal) {
            if (v.getReservationsAssign() != null && !v.getReservationsAssign().isEmpty()) {
                System.out.println("Vehicule " + v.getId() + " - Date retour : "
                        + (v.getDateRetour() == null ? "null" : v.getDateRetour()));
                System.out.println(v.getReference() + " " + System.identityHashCode(v));
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