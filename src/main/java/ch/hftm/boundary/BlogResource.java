package ch.hftm.boundary;

import java.util.List;
import java.util.stream.Collectors;

import ch.hftm.control.BlogService;
import ch.hftm.dto.BlogDTO;
import ch.hftm.dto.CommentDTO;
import ch.hftm.dto.LikeDTO;
import ch.hftm.entity.Blog;
import ch.hftm.entity.Comment;
import ch.hftm.entity.BlogLike;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import jakarta.validation.Valid;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/blogs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Blog Ressourcen", description = "APIs zur Verwaltung von Blogs")
public class BlogResource {

    @Inject
    BlogService blogService;

    @GET
    @Path("/{id}")
    @RolesAllowed({"user", "editor", "admin"})  // Alle Benutzerrollen dürfen Blogs lesen
    @Operation(summary = "Hole einen Blog anhand der ID", description = "Gibt einen Blogeintrag anhand seiner ID zurück")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Blog gefunden"),
        @APIResponse(responseCode = "404", description = "Blog nicht gefunden")
    })
    public Response getBlogById(@PathParam("id") Long id) {
        Blog blog = blogService.getBlogById(id);
        BlogDTO blogDTO = blogService.mapToBlogDTO(blog);
        return Response.ok(blogDTO).build();
    }

    @GET
    @RolesAllowed({"user", "editor", "admin"})  // Alle Benutzerrollen dürfen Blogs auflisten
    @Operation(summary = "Hole alle Blogs mit optionaler Paginierung und Titel-Filter", 
               description = "Gibt eine Liste von Blogs zurück, optional gefiltert nach Titel und mit Paginierung")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Liste von Blogs")
    })
    public Response getAllBlogs(@QueryParam("title") String title, 
                                @QueryParam("page") @DefaultValue("0") int page, 
                                @QueryParam("size") @DefaultValue("10") int size) {
        List<BlogDTO> blogs;
        if (title != null && !title.isEmpty()) {
            blogs = blogService.getBlogsByTitlePaginated(title, page, size)
                               .stream()
                               .map(blogService::mapToBlogDTO)
                               .collect(Collectors.toList());
        } else {
            blogs = blogService.getBlogsPaginated(page, size)
                               .stream()
                               .map(blogService::mapToBlogDTO)
                               .collect(Collectors.toList());
        }
        return Response.ok(blogs).build();
    }

    @POST
    @RolesAllowed({"editor", "admin"})  // Nur "editor" und "admin" dürfen Blogs erstellen
    @Operation(summary = "Erstelle einen neuen Blog", description = "Erstellt einen neuen Blogeintrag und gibt dessen Standort zurück")
    @APIResponses({
        @APIResponse(responseCode = "201", description = "Blog erstellt"),
        @APIResponse(responseCode = "400", description = "Ungültige Eingabedaten")
    })
    public Response createBlog(@Valid BlogDTO blogDTO, @Context UriInfo uriInfo) {
        // 1) DTO -> Entity
        Blog blog = blogService.mapToBlog(blogDTO);

        // 2) Status setzen (z.B. "PENDING")
        blog.setStatus("PENDING");

        // 3) Persistieren
        blogService.addBlog(blog);

        // 4) URI erstellen für "Location"-Header
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(Long.toString(blog.getId()));

        // 5) BlogDTO zurückliefern
        return Response
                .created(uriBuilder.build())
                .entity(blogService.mapToBlogDTO(blog))
                .build();
    }


    @PUT
    @Path("/{id}")
    @RolesAllowed({"editor", "admin"})  // Nur "editor" und "admin" dürfen Blogs aktualisieren
    @Operation(summary = "Aktualisiere einen bestehenden Blog", description = "Aktualisiert einen Blogeintrag anhand der ID")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Blog aktualisiert"),
        @APIResponse(responseCode = "404", description = "Blog nicht gefunden")
    })
    public Response updateBlog(@PathParam("id") Long id, @Valid BlogDTO updatedBlogDTO) {
        Blog existingBlog = blogService.getBlogById(id);
        existingBlog.setTitle(updatedBlogDTO.getTitle());
        existingBlog.setContent(updatedBlogDTO.getContent());
        blogService.updateBlog(existingBlog);
        return Response.ok(blogService.mapToBlogDTO(existingBlog)).build();
    }

    @PATCH
    @Path("/{id}")
    @RolesAllowed({"editor", "admin"})  // Nur "editor" und "admin" dürfen Teile eines Blogs aktualisieren
    @Operation(summary = "Teile eines Blogs aktualisieren", description = "Aktualisiert Teile eines Blogeintrags anhand der ID")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Blog aktualisiert"),
        @APIResponse(responseCode = "404", description = "Blog nicht gefunden")
    })
    public Response patchBlog(@PathParam("id") Long id, BlogDTO patchBlogDTO) {
        Blog existingBlog = blogService.getBlogById(id);
        if (patchBlogDTO.getTitle() != null) {
            existingBlog.setTitle(patchBlogDTO.getTitle());
        }
        if (patchBlogDTO.getContent() != null) {
            existingBlog.setContent(patchBlogDTO.getContent());
        }
        blogService.updateBlog(existingBlog);
        return Response.ok(blogService.mapToBlogDTO(existingBlog)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"admin"})  // Nur "admin" darf Blogs löschen
    @Operation(summary = "Lösche einen Blog", description = "Löscht einen Blogeintrag anhand der ID")
    @APIResponses({
        @APIResponse(responseCode = "204", description = "Blog gelöscht"),
        @APIResponse(responseCode = "404", description = "Blog nicht gefunden")
    })
    public Response deleteBlog(@PathParam("id") Long id) {
        blogService.deleteBlog(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @POST
    @Path("/{id}/comments")
    @RolesAllowed({"user", "editor", "admin"})  // Alle Benutzerrollen dürfen Kommentare hinzufügen
    @Operation(summary = "Einen Kommentar zu einem Blog hinzufügen", description = "Fügt einem Blogeintrag einen neuen Kommentar hinzu")
    @APIResponses({
        @APIResponse(responseCode = "201", description = "Kommentar hinzugefügt"),
        @APIResponse(responseCode = "404", description = "Blog nicht gefunden")
    })
    public Response addComment(@PathParam("id") Long blogId, @Valid CommentDTO commentDTO) {
        Comment comment = blogService.mapToComment(commentDTO);
        blogService.addCommentToBlog(blogId, comment);
        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Path("/{id}/likes")
    @RolesAllowed({"user", "editor", "admin"})  // Alle Benutzerrollen dürfen Likes hinzufügen
    @Operation(summary = "Einen Like zu einem Blog hinzufügen", description = "Fügt einem Blog einen Like hinzu")
    @APIResponses({
        @APIResponse(responseCode = "201", description = "Like hinzugefügt"),
        @APIResponse(responseCode = "404", description = "Blog nicht gefunden")
    })
    public Response addLike(@PathParam("id") Long blogId, @Valid LikeDTO likeDTO) {
        BlogLike like = blogService.mapToLike(likeDTO);
        blogService.addLikeToBlog(blogId, like);
        return Response.status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("/likes/{likeId}")
    @RolesAllowed({"user", "editor", "admin"})  // Alle Benutzerrollen dürfen Likes entfernen
    @Operation(summary = "Einen Like von einem Blog entfernen", description = "Entfernt einen Like von einem Blogeintrag")
    @APIResponses({
        @APIResponse(responseCode = "204", description = "Like entfernt"),
        @APIResponse(responseCode = "404", description = "Like nicht gefunden")
    })
    public Response removeLike(@PathParam("likeId") Long likeId) {
        blogService.removeLikeFromBlog(likeId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
