package service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import framework.ModelView;
import model.*;
import dao.TrajetDAO;
import dao.AssignationDAO;

public class ReservationService {

    // Classe interne pour retourner le résultat du traitement d'un groupe
    private static class ResultatTraitementGroupe {
        List<Vehicule> vehiculesUtilises;
        List<Reservation> reservationsRestantes;

        ResultatTraitementGroupe(List<Vehicule> vehiculesUtilises, List<Reservation> reservationsRestantes) {
            this.vehiculesUtilises = vehiculesUtilises;
            this.reservationsRestantes = reservationsRestantes;
        }
    }

    public ModelView assignerVehicule(LocalDate dateDebut, LocalDate dateFin, List<Reservation> reservations,
            List<Vehicule> vehicules, Param p) {
        List<Reservation> reservationsSansVehicule = new ArrayList<>(reservations);
        List<Reservation> reservationsAssignees = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm:ss");
        List<Vehicule> vehicuFinal = new ArrayList<>();
        double dureeDattente = p.getTemps_attente();

        // Trier les réservations par date d'arrivée (plus ancienne d'abord)
        trierReservationsParDate(reservations);

        // Grouper les réservations par intervalles de temps
        Map<LocalDateTime, List<Reservation>> groupes = grouperReservations(reservations, dureeDattente);

        // Trier chaque groupe par nombre de passagers (plus grand d'abord)
        for (List<Reservation> groupe : groupes.values()) {
            trierGroupeParPassagers(groupe);
        }

        List<Reservation> reservationsNonAssignees = new ArrayList<>();
        int numeroGroupe = 1;

        for (Map.Entry<LocalDateTime, List<Reservation>> entry : groupes.entrySet()) {
            LocalDateTime debutGroupe = entry.getKey();
            List<Reservation> groupe = entry.getValue();

            // Ajouter les réservations non assignées du groupe précédent
            groupe.addAll(reservationsNonAssignees);
            trierGroupeParPassagers(groupe);
            reservationsNonAssignees.clear();

            System.out.println("\nTraitement du groupe " + numeroGroupe + " pour " + groupe.get(0).getDateArrivee());

            // Traiter le groupe et obtenir les véhicules utilisés et réservations restantes
            ResultatTraitementGroupe resultat = traiterGroupe(groupe, vehicules, debutGroupe, dureeDattente, numeroGroupe, reservationsAssignees);
            vehicuFinal.addAll(resultat.vehiculesUtilises);
            reservationsNonAssignees.addAll(resultat.reservationsRestantes);

            numeroGroupe++;
        }

        // Nettoyer les réservations sans véhicule
        reservationsSansVehicule.removeAll(reservationsAssignees);
        reservationsSansVehicule.addAll(reservationsNonAssignees);
        reservationsSansVehicule = enleverReservationsEnDouble(reservationsSansVehicule);

        // Préparer les véhicules utilisés
        List<Vehicule> vehiculesUtilises = vehicuFinal.stream()
                .filter(v -> v.getReservationsAssign() != null && !v.getReservationsAssign().isEmpty())
                .collect(ArrayList::new, (list, v) -> {
                    System.out.println("Véhicule " + v.getId() + " - Date retour : " + (v.getDateRetour() == null ? "null" : v.getDateRetour()));
                    System.out.println(v.getReference() + " " + System.identityHashCode(v));
                    list.add(v);
                }, ArrayList::addAll);

        ModelView mv = new ModelView("jsonView.jsp");
        mv.addObject("vehicules", vehiculesUtilises);
        mv.addObject("reservationsSansVehicule", reservationsSansVehicule);

        return mv;
    }

    // Trier les réservations par date d'arrivée (plus ancienne d'abord)
    private void trierReservationsParDate(List<Reservation> reservations) {
        reservations.sort(Comparator.comparing(Reservation::getDateArrivee));
    }

    // Grouper les réservations par intervalles de temps basés sur la durée d'attente
    private Map<LocalDateTime, List<Reservation>> grouperReservations(List<Reservation> reservations, double dureeDattente) {
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

        return groupes;
    }

    // Trier un groupe par nombre de passagers (plus grand d'abord)
    private void trierGroupeParPassagers(List<Reservation> groupe) {
        groupe.sort((r1, r2) -> Integer.compare(r2.getNbPassager(), r1.getNbPassager()));
    }

    // Traiter un groupe de réservations
    private ResultatTraitementGroupe traiterGroupe(List<Reservation> groupe, List<Vehicule> vehicules,
            LocalDateTime debutGroupe, double dureeDattente, int numeroGroupe, List<Reservation> reservationsAssignees) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm:ss");
        LocalDateTime datDepart = LocalDateTime.parse("11/11/11 11:11:11", formatter);
        List<Vehicule> vehiculesUtilises = new ArrayList<>();
        List<Reservation> reservationsRestantes = new ArrayList<>();
        List<Reservation> reservationsSplittees = new ArrayList<>();

        for (int i = 0; i < groupe.size(); i++) {
            Reservation r = groupe.get(i);
            r.setGroup(numeroGroupe);

            System.out.println("Traitement de la réservation : " + r);

            Vehicule vehiculeChoisi = r.getVehiculeApproprie(vehicules, debutGroupe.plusMinutes((long) dureeDattente), reservationsSplittees);

            if (datDepart.isBefore(r.getDateArrivee())) {
                datDepart = r.getDateArrivee();
            }

            if (vehiculeChoisi != null) {
                assignerReservationAuVehicule(r, vehiculeChoisi, vehiculesUtilises, reservationsAssignees, groupe, i, datDepart);
                i--; // Ajuster l'index après suppression
            } else {
                reservationsRestantes.add(r);
            }
        }

        // Ajouter les réservations splittees aux restantes
        reservationsRestantes.addAll(reservationsSplittees);

        // Calculer les dates de départ et retour pour les véhicules utilisés
        calculerDatesVehicules(vehiculesUtilises, datDepart);

        return new ResultatTraitementGroupe(vehiculesUtilises, reservationsRestantes);
    }

    // Assigner une réservation à un véhicule
    private void assignerReservationAuVehicule(Reservation r, Vehicule vehiculeChoisi, List<Vehicule> vehiculesUtilises,
            List<Reservation> reservationsAssignees, List<Reservation> groupe, int index, LocalDateTime datDepart) {
        System.out.println("Assignation de la réservation #" + r.getId() + " au véhicule " + vehiculeChoisi.getReference()
                + " avec nombre de passagers " + r.getNbPassager());

        vehiculesUtilises.add(vehiculeChoisi);
        vehiculeChoisi.setGroup(r.getGroup());

        if (datDepart.isBefore(vehiculeChoisi.getDateRetour())) {
            datDepart = vehiculeChoisi.getDateRetour();
        }

        if (vehiculeChoisi.getReservationsAssign() == null) {
            vehiculeChoisi.setReservationsAssign(new ArrayList<>());
        }
        vehiculeChoisi.getReservationsAssign().add(r);

        groupe.remove(index);
        vehiculeChoisi.remplirReservation(groupe, reservationsAssignees, new ArrayList<>());
        reservationsAssignees.add(r);
    }

    // Calculer les dates de départ et retour pour les véhicules
    private void calculerDatesVehicules(List<Vehicule> vehiculesUtilises, LocalDateTime datDepart) {
        for (Vehicule v : vehiculesUtilises) {
            LocalDateTime datRecent = v.getdaterecent();
            if (datRecent.isAfter(datDepart)) {
                datDepart = datRecent;
            }
        }
        for (Vehicule v : vehiculesUtilises) {
            v.setDateDepart(datDepart);
            v.getdateretourAssign();
        }
    }

    public int getNombrePassagerTotal(List<Reservation> reservations) {
        return reservations.stream().mapToInt(Reservation::getNbPassager).sum();
    }

    private void trierReservationsParPriorite(List<Reservation> reservations) {
        reservations.sort((r1, r2) -> {
            if (r1.getNbPassager() != r2.getNbPassager()) {
                return Integer.compare(r2.getNbPassager(), r1.getNbPassager());
            }
            return r1.getDateArrivee().compareTo(r2.getDateArrivee());
        });
    }

    private List<Reservation> enleverReservationsEnDouble(List<Reservation> reservations) {
        List<Reservation> resultat = new ArrayList<>();
        List<Integer> idsVus = new ArrayList<>();
        
        for (Reservation r : reservations) {
            if (!idsVus.contains(r.getId())) {
                idsVus.add(r.getId());
                resultat.add(r);
            }
        }
        
        return resultat;
    }
}