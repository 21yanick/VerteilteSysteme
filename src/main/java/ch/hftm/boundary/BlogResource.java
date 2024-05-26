package ch.hftm.boundary;

import ch.hftm.control.BlogService;
import ch.hftm.entity.Blog;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/blogs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BlogResource {

    @Inject
    BlogService blogService;

    @GET
    public Response getAllBlogs() {
        return Response.ok(blogService.getBlogs()).build();
    }    

    @POST
    public Response createBlog(Blog blog) {
        blogService.addBlog(blog);
        return Response.status(Response.Status.CREATED).entity(blog).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateBlog(@PathParam("id") Long id, Blog updatedBlog) {
        Blog existingBlog = blogService.getBlogById(id);
        if (existingBlog != null) {
            existingBlog.setTitle(updatedBlog.getTitle());
            existingBlog.setContent(updatedBlog.getContent());
            blogService.updateBlog(existingBlog);
            return Response.ok(existingBlog).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBlog(@PathParam("id") Long id) {
        Blog existingBlog = blogService.getBlogById(id);
        if (existingBlog != null) {
            blogService.deleteBlog(id);
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}