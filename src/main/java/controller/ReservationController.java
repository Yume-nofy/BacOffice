package controller;

import framework.ControllerAnnotation;
import framework.JsonAnnotation;
import framework.ModelView;
import framework.RequestParam;
import framework.UrlAnnotation;

import dao.ReservationDAO;
import dao.TokenDAO;
import dao.VehiculeDAO;
import dao.HotelDAO;
import dao.ParamDAO;
import model.*;
import service.ReservationService;

import java.time.format.DateTimeParseException;
import java.time.LocalDateTime;
import java.time.LocalDate; 
import java.util.ArrayList;   
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
        List<Hotel> hotels = hotelDAO.getAllHotels(); 
        List<Reservation> reservations = reservationDAO.getAllReservations();

        ModelView mv = new ModelView("reservations.jsp");
        mv.addObject("reservation", reservation);
        mv.addObject("reservations", reservations);
        mv.addObject("hotels", hotels); 
        return mv;
    }

    @UrlAnnotation(url = "/api/assignationVehicule", method= "GET")
    @JsonAnnotation
    public ModelView getAssignationVehicule( @RequestParam("dateDebut") String dateDebut,@RequestParam("dateFin") String dateFin) {
        

        LocalDate dateDebutParse = null;
        LocalDate dateFinParse = null;

        if (dateDebut!=null && !dateDebut.isEmpty()) {
            dateDebutParse= LocalDate.parse(dateDebut);
        }

        if (dateFin!=null && !dateFin.isEmpty()) {
            dateFinParse= LocalDate.parse(dateFin);
        }

        List<Reservation> reservations = reservationDAO.getReservationsByDate(dateDebutParse, dateFinParse);
        ParamDAO pDAO = new ParamDAO();
        Param p = pDAO.getParam();

        VehiculeDAO vDAO = new VehiculeDAO();
        List<Vehicule> vehicules = vDAO.getAllVehicules();
        ReservationService rs = new ReservationService();

        return rs.assignerVehicule(dateDebutParse, dateFinParse, reservations, vehicules, p);
    }

    @UrlAnnotation(url = "/api/reservations", method = "GET")
    @JsonAnnotation
    public ModelView getReservationsAsJson(
            @RequestParam("date") String date,
            @RequestParam("token") String tokenValue
    ) {
        TokenDAO tokenDAO = new TokenDAO();
        List<Token> tokens = tokenDAO.getAllTokens();
        
        Token validToken = null;
        for (Token t : tokens) {
            if (t.getToken().equals(tokenValue) && !t.isExpired()) {
                validToken = t;
                break;
            }
        }
        
        if (validToken == null) {
            ModelView mv = new ModelView("error.jsp");
            mv.addObject("error", "Token invalide ou expiré");
            mv.addObject("status", 401);
            return mv;
        }
        
        List<Reservation> reservations = reservationDAO.getAllReservations();

        if (date == null || date.isEmpty()) {
            ModelView mv = new ModelView("jsonView.jsp");
            mv.addObject("data", reservations);
            return mv;
        } else {
            List<Reservation> filteredReservations = new ArrayList<>();
            try {
                LocalDate filterDate = LocalDate.parse(date);
                for (Reservation r : reservations) {
                    if (r.getDateArrivee().toLocalDate().equals(filterDate)) {
                        filteredReservations.add(r);
                }
            }
            ModelView mv = new ModelView("jsonView.jsp");
            mv.addObject("data", filteredReservations);
            return mv;
        } catch (DateTimeParseException e) {
            ModelView mv = new ModelView("error.jsp");
            mv.addObject("error", "Format de date invalide");
            mv.addObject("status", 400);
            return mv;
        }
    }
}
}