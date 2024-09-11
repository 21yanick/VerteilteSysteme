package ch.hftm;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import ch.hftm.entity.Blog;
import ch.hftm.entity.Comment;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class BlogResourceTest {

    @Test
    public void testAddComment() {
        // Blog-Eintrag erstellen
        Blog blog = new Blog("Test Blog", "Dies ist ein Blog für Kommentare");
        Integer blogId = given()
            .contentType("application/json")
            .body(blog)
            .when()
            .post("/blogs")
            .then()
            .statusCode(201)
            .extract().path("id");
        
        // Neuer Kommentar
        Comment comment = new Comment("Dies ist ein Testkommentar");
    
        given()
            .contentType("application/json")
            .body(comment)
            .when()
            .post("/blogs/" + blogId + "/comments")
            .then()
            .statusCode(201);
    }
      

    @Test
    public void testPagination() {
        // Mindestens 3 Blogs existieren
        Blog blog1 = new Blog("Blog 1", "Content 1");
        Blog blog2 = new Blog("Blog 2", "Content 2");
        Blog blog3 = new Blog("Blog 3", "Content 3");
    
        given().contentType("application/json").body(blog1).post("/blogs").then().statusCode(201);
        given().contentType("application/json").body(blog2).post("/blogs").then().statusCode(201);
        given().contentType("application/json").body(blog3).post("/blogs").then().statusCode(201);
        
        // Teste Pagination
        given()
            .queryParam("page", 0)
            .queryParam("size", 2)
            .when()
            .get("/blogs")
            .then()
            .statusCode(200)
            .body("$.size()", is(2));  // Nur 2 Blogs
    }
    

    @Test
    public void testGetAllBlogs() {
        given()
            .when().get("/blogs")
            .then()
            .statusCode(200);
    }

    @Test
    public void testCreateBlog() {
        Blog newBlog = new Blog("Test Blog", "This is a test content");

        given()
            .contentType("application/json")
            .body(newBlog)
            .when()
            .post("/blogs")
            .then()
            .statusCode(201);
    }

    @Test
    public void testUpdateBlog() {
        Blog updatedBlog = new Blog("Updated Blog", "Updated Content");

        given()
            .contentType("application/json")
            .body(updatedBlog)
            .when()
            .put("/blogs/1")
            .then()
            .statusCode(200);
    }

    @Test
    public void testDeleteBlog() {
        given()
            .when()
            .delete("/blogs/1")
            .then()
            .statusCode(204);
    }

}
