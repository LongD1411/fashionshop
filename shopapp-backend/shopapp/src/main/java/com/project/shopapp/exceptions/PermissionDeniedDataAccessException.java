package com.project.shopapp.exceptions;

public class PermissionDeniedDataAccessException extends Exception{
    public PermissionDeniedDataAccessException(String message){
        super(message);
    }
}
