package org.sjtugo.api.service.planner;

public class PlaceNotFoundException extends RuntimeException {
    PlaceNotFoundException(){
        super("The place you typed is not found on SJTU campus.");
    }

}
