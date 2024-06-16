/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cst8218.stan0304.slider.service;

import cst8218.stan0304.slider.entity.Slider;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
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
 *
 * @author thpst
 */
@Stateless
@Path("cst8218.stan0304.slider.entity.slider")
public class SliderFacadeREST extends AbstractFacade<Slider> {

    public SliderFacadeREST() {
        super(Slider.class);
    }

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
                return Response.status(Response.Status.BAD_REQUEST).entity("Slider with id " + entity.getId() + " does not exist.").build();
            }
        }
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response edit(@PathParam("id") Long id, Slider entity, @Context UriInfo uriInfo) {
        //find the existing slider by id
        Slider existingSlider = super.find(id);
        //check if the existing slider is null
        if (existingSlider == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Slider with ID " + id + " not in database").build();
        }
        
        // Check if the request id matches the entity id
        if (entity.getId() != null && !entity.getId().equals(id)) {
        return Response.status(Response.Status.BAD_REQUEST).entity("ID in the URL does not match ID in the request body").build();
        }

        //update the existing Slider with new non-null values from the entity
        existingSlider.update(entity);

        //merge the changes to update the existing slider in order to preserve old values if they are not overwritten
        super.edit(existingSlider);
        return Response.status(Response.Status.OK).entity(existingSlider).build();
    }

    @DELETE
    @Path("{id}")
    public Response.Status remove(@PathParam("id") Long id) {
        super.remove(super.find(id));
        return Response.Status.ACCEPTED;
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Slider find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Slider> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Slider> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
