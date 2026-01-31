package controller;

import framework.Authorize;
import framework.Role;
import framework.ControllerAnnotation;
import framework.ModelView;
import framework.RequestParam;
import framework.UrlAnnotation;

@ControllerAnnotation
public class Hello {

    @UrlAnnotation(url = "/hello", method="GET")
    public String Hello() {
        return "hello world";
    }
}