package ch.hftm.boundary;

import java.util.List;

import ch.hftm.control.BlogService;
import ch.hftm.entity.Blog;
import ch.hftm.entity.Comment;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import jakarta.validation.Valid;

@Path("/blogs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BlogResource {

    @Inject
    BlogService blogService;

    @GET
    public Response getAllBlogs(@QueryParam("title") String title, 
                                @QueryParam("page") @DefaultValue("0") int page, 
                                @QueryParam("size") @DefaultValue("10") int size) {
        List<Blog> blogs;
        if (title != null && !title.isEmpty()) {
            blogs = blogService.getBlogsByTitlePaginated(title, page, size);
        } else {
            blogs = blogService.getBlogsPaginated(page, size);
        }
        return Response.ok(blogs).build();
    }     

    @POST
    public Response createBlog(@Valid Blog blog, @Context UriInfo uriInfo) {
        blogService.addBlog(blog);
        // Location-Header
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        uriBuilder.path(Long.toString(blog.getId()));
        return Response.created(uriBuilder.build()).entity(blog).build();
    }
    
    @PUT
    @Path("/{id}")
    public Response updateBlog(@PathParam("id") Long id, @Valid Blog updatedBlog) {
        Blog existingBlog = blogService.getBlogById(id);
        if (existingBlog != null) {
            existingBlog.setTitle(updatedBlog.getTitle());
            existingBlog.setContent(updatedBlog.getContent());
            blogService.updateBlog(existingBlog);
            return Response.ok(existingBlog).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PATCH
    @Path("/{id}")
    public Response patchBlog(@PathParam("id") Long id, Blog patchBlog) {
        Blog existingBlog = blogService.getBlogById(id);
        if (existingBlog != null) {
            if (patchBlog.getTitle() != null) {
                existingBlog.setTitle(patchBlog.getTitle());
            }
            if (patchBlog.getContent() != null) {
                existingBlog.setContent(patchBlog.getContent());
            }
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

    @POST
    @Path("/{id}/comments")
    public Response addComment(@PathParam("id") Long blogId, @Valid Comment comment) {
        blogService.addCommentToBlog(blogId, comment);
        return Response.status(Response.Status.CREATED).build();
    }
}