/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cst8218.stan0304.slider.service;

import cst8218.stan0304.slider.entity.Slider;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

/**
 * Contains all of the paths used by the requests in the URL.
 */
@Stateless
@Path("cst8218.stan0304.slider.entity.slider")
public class SliderFacadeREST extends AbstractFacade<Slider> {

    @PersistenceContext(unitName = "slider_persistence_unit")
    private EntityManager em;
    
    public SliderFacadeREST() {
        super(Slider.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    /*
    post on the root resource creates a new slider if the id in url is null
    if teh id is not null and exists in the databse, update the existing by using the edit() method in the abstract facade class.
    if the id is not null, but doesn't exist in the database, it's a bad request
    */
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response create(Slider entity, @Context UriInfo uriInfo) {
        //if slider id is null, new slider is made
        if(entity.getId() == null){
        super.create(entity);
        URI location = URI.create(uriInfo.getRequestUri().getPath() + "/" + entity.getId());
        return Response.status(Response.Status.CREATED).location(location).entity(entity).build();
        
        //otherwise if the id is not null and is found in the databasethe new attributes are written over the old ones
        }else{
        Slider existingSlider = super.find(entity.getId());
            if (existingSlider != null) {
                existingSlider.update(entity);
                super.edit(existingSlider);
                return Response.status(Response.Status.OK).entity(existingSlider).build();
                
            // if id is not null and the id is not in the databse, bad request is returned
            }else{
                return Response.status(Response.Status.NOT_FOUND).entity("Slider with id " + entity.getId() + " does not exist.").build();
            }
        }
    }
    
    /*
    post request with a specific id in the request body updates the slider with the new values, while keeping the preexisting values.
    */
    @POST
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response update(@PathParam("id") Long id, Slider entity, @Context UriInfo uriInfo) {
        //find the existing slider by id
        Slider existingSlider = super.find(id);
        //check if the existing slider is null
        if (existingSlider == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Slider with ID " + id + " not in database").build();
        }
        // Check if the request id matches the entity id
        if (entity.getId() != null && !entity.getId().equals(id)) {
        return Response.status(Response.Status.NOT_FOUND).entity("ID in the URL does not match ID in the request body").build();
        }
        //update the existing Slider with new non-null values from the entity
        existingSlider.update(entity);
        //merge the changes to update the existing slider in order to preserve old values if they are not overwritten
        super.edit(existingSlider);
        URI location = URI.create(uriInfo.getRequestUri().getPath() + "/" + entity.getId());
        return Response.status(Response.Status.OK).location(location).entity(entity).build();
    }

    //put on root resource returns forbidden response
    @PUT
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response putOnRootResource() {
    return Response.status(Response.Status.NOT_ACCEPTABLE)
            .entity("PUT method not allowed on the root resource")
            .build();
    }
    
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, Slider entity) {
        super.edit(entity);
    }

    //deletes slider iwith matching id
    @DELETE
    @Path("{id}")
    public Response.Status remove(@PathParam("id") Long id) {
        super.remove(super.find(id));
        return Response.Status.ACCEPTED;
    }
    
    //returns slider with matching id
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Slider find(@PathParam("id") Long id) {
        Slider existingSlider = super.find(id);
        existingSlider.timeStep();
        return existingSlider;
    }

    //creates xml or json of all sliders
    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Slider> findAll() {
        
        return super.findAll();
    }
    
    //creates list of sliders with id range
    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Slider> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    //creates list of the required quantity of sliders
    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }
    
    
}