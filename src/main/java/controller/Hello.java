package controller;

import framework.ControllerAnnotation;
import framework.UrlAnnotation;

@ControllerAnnotation
public class Hello {

    @UrlAnnotation(url = "/hello", method="GET")
    public String Hello() {
        return "hello world";
    }
}