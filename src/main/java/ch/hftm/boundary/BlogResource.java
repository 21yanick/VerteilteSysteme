package ch.hftm.boundary;

import ch.hftm.control.BlogService;
import ch.hftm.entity.Blog;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
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
    
}