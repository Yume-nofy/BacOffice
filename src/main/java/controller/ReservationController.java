package controller;

import framework.ControllerAnnotation;
import framework.JsonAnnotation;
import framework.ModelView;
import framework.RequestParam;
import framework.UrlAnnotation;

import dao.ReservationDAO;
import model.Reservation;
import dao.HotelDAO; 
import model.Hotel;

import java.time.format.DateTimeParseException;
import java.time.LocalDateTime;
import java.time.LocalDate; // Ajouté
import java.util.ArrayList;   // Ajouté
import java.util.List;

@ControllerAnnotation
public class ReservationController {

    private final ReservationDAO reservationDAO = new ReservationDAO();
    private final HotelDAO hotelDAO = new HotelDAO();
    @UrlAnnotation(url = "/reservations", method = "GET")
    public ModelView listReservations() {
     List<Reservation> reservations = reservationDAO.getAllReservations();
        List<Hotel> hotels = hotelDAO.getAllHotels(); 

        ModelView mv = new ModelView("reservations.jsp");
        mv.addObject("reservations", reservations);
        mv.addObject("hotels", hotels); 
        return mv;
    }

    @UrlAnnotation(url = "/reservation/add", method = "POST")
    public ModelView addReservation(
            @RequestParam("idClient") String idClient,
            @RequestParam("idHotel") int idHotel,
            @RequestParam("nbPassager") int nbPassager,
            @RequestParam("dateArrivee") String dateArrivee 
    ) {
        LocalDateTime dt = LocalDateTime.parse(dateArrivee);
        Reservation reservation = new Reservation(idClient, idHotel, nbPassager, dt);
        reservationDAO.addReservation(reservation);
         return listReservations();
    }

    @UrlAnnotation(url = "/api/reservations", method = "GET")
    @JsonAnnotation
    public List<Reservation> getReservationsAsJson(@RequestParam("date") String date) {
        // Utilisation de l'attribut de classe au lieu de réinstancier
        List<Reservation> reservations = reservationDAO.getAllReservations();

        if (date == null || date.isEmpty()) {
            return reservations;
        } else {
            List<Reservation> filteredReservations = new ArrayList<>();
            try {
                LocalDate filterDate = LocalDate.parse(date);
                for (Reservation r : reservations) {
                    // Extraction de la date (LocalDate) depuis le LocalDateTime pour comparer
                    if (r.getDateArrivee().toLocalDate().equals(filterDate)) { 
                        filteredReservations.add(r);
                    }
                }
                return filteredReservations;
            } catch (DateTimeParseException e) {
                return List.of();
            }
        }
    }
}