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
                // System.out.println("Ajout des reservations non assignes dans le groupe  "+k);
                // for (Reservation r : nonasi) {
                //     System.out.println("Aucune assignation possible pour la réservation #" +
                //             r.getId());
                // }
                // System.out.println("Groupe avec les nouveau reservations");
                // for (Reservation r : groupe) {
                //     System.out.println("Aucune assignation possible pour la réservation #" +
                //             r.getId());
                // }
                nonasi.clear();
            }
            System.out.println("\nGroupe n " + k + " pour " + groupe.get(0).getDateArrivee());
            // System.out.println("Groupe entre " + groupe.get(0).getDateArrivee() + " et "
            // + groupe.get(0).getDateArrivee().plusMinutes((long) dureeDattente));
            LocalDateTime datDepart = LocalDateTime.parse("11/11/11 11:11:11", formatter);
            List<Vehicule> ves = new ArrayList<>();
            List<Reservation> ress = new ArrayList<>();
            trierReservationsParPriorite(groupe);

            for (int i = 0; i < groupe.size(); i++) {
                System.out.println("Traitement de la réservation : " + groupe.get(i));
                Reservation r = groupe.get(i);
                r.setGroup(k);
                LocalDateTime dateCleGroupe = null;
                for (Map.Entry<LocalDateTime, List<Reservation>> entry : groupes.entrySet()) {
                    if (entry.getValue() == groupe) {
                        dateCleGroupe = entry.getKey();
                        break;
                    }
                }
                Vehicule vehiculeChoisi = r.getVehiculeApproprie(vehicules,
                        dateCleGroupe.plusMinutes((long) dureeDattente), ress);

                if (datDepart.isBefore(r.getDateArrivee())) {
                    datDepart = r.getDateArrivee();
                }

                if (vehiculeChoisi != null) {
                    System.out.println("Assignation de la réservation #" + r.getId() + " au véhicule "
                            + vehiculeChoisi.getReference() + " referenceuniqueobjet: "
                            + System.identityHashCode(vehiculeChoisi) + " avec nombre de passager "
                            + r.getNbPassager());
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
                    vehiculeChoisi.remplirReservation(groupe, reservationsAssignees, ress);

                    reservationsAssignees.add(r);

                } else {
                    nonasi.add(r);
                }
            }
            // System.out.println(" Contenue de ress,( ajout dans nonasi): ");
            for (Reservation t : ress) {
                // System.out.println("reservation: " + t.getId()+" avec "+t.getNbPassager()+" passager");
                nonasi.add(t);
            }
            ress.clear();
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

            // System.out.println("Contenue Final de Non assigne avant fin de boucle : ");
            // for (Reservation t : nonasi) {
            //     System.out.println(" --reservation: " + t.getId()+" avec "+t.getNbPassager()+" passager");
            // }

            k++;
        }

        reservationsSansVehicule.removeAll(reservationsAssignees);
        reservationsSansVehicule.addAll(nonasi);
        reservationsSansVehicule = enleverReservationsEnDouble(reservationsSansVehicule);

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
        // TrajetDAO trajetDAO = new TrajetDAO();
        // AssignationDAO assignationDAO = new AssignationDAO();
        // assignationDAO.deleteAllAssignation(dateDebut, dateFin);
        // trajetDAO.deleteAllTrajets(dateDebut, dateFin);

        // for (Vehicule v : vehiculesUtilises) {
        // Trajet trajet = new Trajet();
        // trajet.setIdVehicule(v.getId());
        // trajet.setDateDepart(v.getDateDepart());
        // trajet.setDateRetour(v.getDateRetour());
        // trajet.setDistanceParcourue(v.getDistanceTotal());

        // int idtrajet= trajetDAO.addTrajet(trajet);

        // for (Reservation r : v.getReservationsAssign()) {
        // Assignation assignation = new Assignation();
        // assignation.setIdTrajet(idtrajet);
        // assignation.setIdReservation(r.getId());
        // assignation.setNbpassager(r.getNbPassager());

        // assignationDAO.addAssignation(assignation);
        // }
        // }

        return mv;
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