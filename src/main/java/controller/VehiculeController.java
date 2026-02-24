package controller;

import framework.ControllerAnnotation;
import framework.ModelView;
import framework.RequestParam;
import framework.UrlAnnotation;
import framework.JsonAnnotation;

import dao.VehiculeDAO;
import model.Vehicule;

import java.util.List;

@ControllerAnnotation
public class VehiculeController {
    
    private final VehiculeDAO vehiculeDAO = new VehiculeDAO();
    
    @UrlAnnotation(url = "/vehicules", method = "GET")
    public ModelView listVehicules() {
        List<Vehicule> vehicules = vehiculeDAO.getAllVehicules();
        
        ModelView mv = new ModelView("vehicules.jsp");
        mv.addObject("vehicules", vehicules);
        return mv;
    }
    
    @UrlAnnotation(url = "/vehicule/add", method = "POST")
    public ModelView addVehicule(
            @RequestParam("reference") String reference,
            @RequestParam("typeCarburant") String typeCarburant,
            @RequestParam("nbrPlace") int nbrPlace
    ) {
        Vehicule vehicule = new Vehicule(reference, typeCarburant, nbrPlace);
        vehiculeDAO.addVehicule(vehicule);
        
        List<Vehicule> vehicules = vehiculeDAO.getAllVehicules();
        
        ModelView mv = new ModelView("vehicules.jsp");
        mv.addObject("vehicules", vehicules);
        mv.addObject("success", "Vehicule ajoute avec succes!");
        return mv;
    }
    
    @UrlAnnotation(url = "/vehicule/update", method = "POST")
    public ModelView updateVehicule(
            @RequestParam("id") int id,
            @RequestParam("reference") String reference,
            @RequestParam("typeCarburant") String typeCarburant,
            @RequestParam("nbrPlace") int nbrPlace
    ) {
        Vehicule vehicule = new Vehicule(id, reference, typeCarburant, nbrPlace);
        vehiculeDAO.updateVehicule(vehicule);
        
        List<Vehicule> vehicules = vehiculeDAO.getAllVehicules();
        
        ModelView mv = new ModelView("vehicules.jsp");
        mv.addObject("vehicules", vehicules);
        mv.addObject("success", "Vehicule modifie avec succes!");
        return mv;
    }
    
    @UrlAnnotation(url = "/vehicule/delete", method = "GET")
    public ModelView deleteVehicule(@RequestParam("id") int id) {
        vehiculeDAO.deleteVehicule(id);
        
        List<Vehicule> vehicules = vehiculeDAO.getAllVehicules();
        
        ModelView mv = new ModelView("vehicules.jsp");
        mv.addObject("vehicules", vehicules);
        mv.addObject("success", "Vehicule supprime avec succes!");
        return mv;
    }
    
    @UrlAnnotation(url = "/api/vehicules", method = "GET")
    @JsonAnnotation
    public List<Vehicule> getVehiculesAsJson() {
        return vehiculeDAO.getAllVehicules();
    }
}