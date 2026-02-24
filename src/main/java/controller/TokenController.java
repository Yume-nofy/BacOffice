package controller;

import framework.ControllerAnnotation;
import framework.RequestParam;
import framework.UrlAnnotation;
import framework.JsonAnnotation;

import dao.TokenDAO;
import model.Token;

import java.util.List;

@ControllerAnnotation
public class TokenController {
    
    private final TokenDAO tokenDAO = new TokenDAO();
    
    @UrlAnnotation(url = "/api/token", method = "GET")
    @JsonAnnotation
    public Token getTokenAsJson(@RequestParam("id") int id) {
        return tokenDAO.getTokenById(id);
    }
    
    @UrlAnnotation(url = "/api/tokens", method = "GET")
    @JsonAnnotation
    public List<Token> getAllTokensAsJson() {
        return tokenDAO.getAllTokens();
    }
}