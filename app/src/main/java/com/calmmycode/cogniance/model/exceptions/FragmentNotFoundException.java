package com.calmmycode.cogniance.model.exceptions;

public class FragmentNotFoundException extends RuntimeException {
    public FragmentNotFoundException(){
        super("Unknown Fragment class");
    }
}
