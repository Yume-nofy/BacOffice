package com.example.backoffice.service;

import com.example.backoffice.repository.ParametreRepository;
import com.example.backoffice.repository.ReservationRepository;
import com.example.backoffice.util.Utils;
import com.example.backoffice.dao.DAO;
import com.example.backoffice.model.GroupeReservation;
import com.example.backoffice.model.Hotel;
import com.example.backoffice.model.Reservation;
import com.example.backoffice.model.Trajet;
import com.example.backoffice.model.Vehicule;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class ReservationService {

    private static final Comparator<Reservation> RESERVATION_COMPARATOR = Comparator
            .comparingInt(Reservation::getNombrePassager)
            .reversed()
            .thenComparing(Reservation::getDateArrivee);

    private final ReservationRepository reservationRepository;
    private final ParametreRepository parametreRepository;
    private final VehiculeService vehiculeService;
    private final TrajetService trajetService;
    private final TrajetReservationService trajetReservationService;

    public ReservationService(DAO dao) {
        this.reservationRepository = new ReservationRepository(dao);
        this.parametreRepository = new ParametreRepository(dao);
        this.vehiculeService = new VehiculeService(dao);
        this.trajetService = new TrajetService(dao);
        this.trajetReservationService = new TrajetReservationService(dao);
    }

    public Reservation reserver(String idClient,
            Integer nombrePassager,
            LocalDateTime dateArrivee,
            Integer idHotel) throws Exception {

        Hotel hotel = new Hotel();
        hotel.setId(idHotel);

        Reservation reservation = new Reservation();
        reservation.setIdClient(idClient);
        reservation.setNombrePassager(nombrePassager);
        reservation.setDateArrivee(dateArrivee);
        reservation.setHotel(hotel);

        reservationRepository.save(reservation);

        return reservation;
    }

    public List<Reservation> getByDateArrivee(String dateStr) throws Exception {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return reservationRepository.getAll();
        }

        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDateTime startOfDay = date.atStartOfDay();

        return reservationRepository.getByDateArrivee(startOfDay);
    }

    public List<Reservation> getNonAssigne(LocalDate date) throws Exception {
        return reservationRepository.getNonAssigne(date);
    }

    public GroupeReservation getNextGroupesReservations(LocalDate date, LocalDateTime dateHeureDebut, List<Reservation> reservations) throws Exception {
        if(dateHeureDebut == null) return null;

        
        LocalTime tempsAttente = parametreRepository.getTempsAttente();
        if (reservations == null || reservations.isEmpty()) {
            throw new Exception("Aucune reservation a traiter");
        }
        LocalDateTime dateHeureFin = dateHeureDebut.plusHours(tempsAttente.getHour())
                .plusMinutes(tempsAttente.getMinute())
                .plusSeconds(tempsAttente.getSecond());

                LocalDateTime dateHeureProchain = null; 
        List<Reservation> groupeReservations = new ArrayList<>();

        for (Reservation reservation : reservations) {
            if (Utils.isBetween(reservation.getDateArrivee(),
                    dateHeureDebut, dateHeureFin)) {
                groupeReservations.add(reservation);
            } else if(dateHeureFin.isBefore(reservation.getDateArrivee())) {
                dateHeureProchain= reservation.getDateArrivee();
                break;
            }
        }

        if(groupeReservations.isEmpty()) return null;
        

        return new GroupeReservation(groupeReservations, dateHeureDebut, dateHeureFin, dateHeureProchain);
    }

    public Trajet traiterReservation(Reservation reservation,
            List<Reservation> assignees,
            List<Reservation> groupeReservations,
            List<Reservation> nonAssignees,
            LocalDateTime heureFin) throws Exception {
        System.out.println("Traitement reservation : " + reservation.getIdClient() + " - " + reservation.getNombrePassager());
        if (!assignees.contains(reservation)) {
            
            Vehicule vehiculeDisponible = vehiculeService.getVehiculeDisponible(reservation, heureFin);
            System.out.println("Vehicule disponible : " + (vehiculeDisponible != null ? vehiculeDisponible.getId() : "Aucun"));
            if (vehiculeDisponible != null) {
            
                reservation.setNonassigne(false);
                Trajet trajet = trajetService.creerTrajet(reservation, vehiculeDisponible);
                assigner(reservation, vehiculeDisponible, assignees, trajet);

                trajetReservationService.remplirVehicule(groupeReservations, assignees, trajet);

                return trajet;
            } else {
                Vehicule vehicule = vehiculeService.getVehiculeDisponible(heureFin);
                if (vehicule != null) {
                    reservation.setNonassigne(false);
                    Trajet trajet = trajetService.creerTrajet(reservation, vehicule);
                    Reservation reservationRestante = trajetReservationService.diviserReservation(reservation,
                            vehicule.getCapacite());

                    assigner(reservation, vehicule, assignees, trajet);

                    if (reservationRestante.getNombrePassager() > 0) {
                        groupeReservations.add(0, reservationRestante);
                        // groupeReservations.sort(RESERVATION_COMPARATOR);
                    }

                    return trajet;
                } else {
                    reservation.setNonassigne(true);
                    nonAssignees.add(reservation);
                }
            }
        }
        return null;
    }

    public void assigner(Reservation reservation, Vehicule vehicule,
            List<Reservation> assignees, Trajet trajet) throws Exception {

        trajetReservationService.creerTrajetReservation(reservation, trajet);
        assignees.add(reservation);

        trajet.getVehicule().diminuerCapaciteRestante(reservation.getNombrePassager());

    }

    public List<Trajet> traiterSousGroupeReservation(LocalDateTime heureDebut, LocalDate date, List<Reservation> assignees, List<Reservation> groupeReservations,
            List<Reservation> nonAssignees) throws Exception {
        
        if(groupeReservations == null || groupeReservations.isEmpty()) {
            return new ArrayList<>();
        }
        LocalTime tempsAttente = parametreRepository.getTempsAttente();
        LocalDateTime heureFin = heureDebut.plusHours(tempsAttente.getHour())
                .plusMinutes(tempsAttente.getMinute())
                .plusSeconds(tempsAttente.getSecond());

        groupeReservations.sort(RESERVATION_COMPARATOR);

        List<Trajet> trajets = new ArrayList<>();
        for (int i = 0; i < groupeReservations.size(); i++) {
            int size = groupeReservations.size();
            Reservation reservation = groupeReservations.get(i);
            Trajet trajet = traiterReservation(reservation, assignees, groupeReservations,
                    nonAssignees, heureFin);
            if (size != groupeReservations.size()) {
                i = -1;
            }
            if (trajet != null)
                trajets.add(trajet);
        }
        return trajets;
    }

    public List<Reservation> traiterGroupeReservation(LocalDate date, List<Reservation> groupeReservations,
            List<Reservation> nonAssigneesPrecedents) throws Exception {

        List<Reservation> nonAssigneesActuelles = new ArrayList<>();
        List<Reservation> assignees = new ArrayList<>();   
        System.out.println("Non Assigne precedents : " + nonAssigneesPrecedents.size());
        for (int i = 0; i < nonAssigneesPrecedents.size(); i++) {
            System.out.println("Non Assigne precedent : " + nonAssigneesPrecedents.get(i).getIdClient() + " - " + nonAssigneesPrecedents.get(i).getNombrePassager());
        }  
        LocalDateTime heureDebut = groupeReservations.get(0).getDateArrivee();

        List<Trajet> trajets = traiterSousGroupeReservation(heureDebut, date, assignees, nonAssigneesPrecedents, nonAssigneesActuelles);       
        int lastIndex = trajets.size()-1;
        if (lastIndex >= 0 && trajets.get(lastIndex).getVehicule().getCapaciteRestante() > 0) {
            trajetReservationService.remplirVehicule(groupeReservations, assignees, trajets.get(lastIndex));
        } 
        List<Trajet> trajets2 = traiterSousGroupeReservation(heureDebut, date, assignees ,groupeReservations, nonAssigneesActuelles);        

        trajets.addAll(trajets2);
        trajetService.preparerTrajet(assignees, trajets);

        System.out.println("Trajets Non Assignes : " + nonAssigneesActuelles.size());
        for (int i = 0; i < nonAssigneesActuelles.size(); i++) {
            System.out.println("Non Assigne : " + nonAssigneesActuelles.get(i).getIdClient() + " - " + nonAssigneesActuelles.get(i).getNombrePassager());
        }
        return nonAssigneesActuelles;
    }

    public void assignation(LocalDate date) throws Exception {
        trajetService.deleteByDate(date);

        List<Reservation> reservations = reservationRepository.getByDateArrivee(date.atStartOfDay());
        GroupeReservation groupeReservation = getNextGroupesReservations(date, reservations.get(0).getDateArrivee(), reservations);
        List<Reservation> nonAssignees = new ArrayList<>();
        
        while(groupeReservation != null) {
            nonAssignees = traiterGroupeReservation(date, groupeReservation.getReservations(), nonAssignees);
            groupeReservation = getNextGroupesReservations(date, groupeReservation.getDateHeureProchain(), reservations);
        }
    }
}
