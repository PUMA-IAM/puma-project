/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.sp.controllers;

import puma.util.persistence.EntityManagerProducer;

/**
 *
 * @author jasper
 */
public class ServiceProviderNamingProducer extends EntityManagerProducer {
    @Override
    public String getPersistenceUnitName() {
        return "Service_ProviderPU";
    }    
}
