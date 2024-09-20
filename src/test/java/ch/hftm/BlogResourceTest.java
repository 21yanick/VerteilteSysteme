// package ch.hftm;

// import io.quarkus.test.junit.QuarkusTest;
// import org.junit.jupiter.api.Test;

// import ch.hftm.dto.BlogDTO;
// import ch.hftm.dto.CommentDTO;

// import static io.restassured.RestAssured.given;
// import static org.hamcrest.Matchers.is;

// @QuarkusTest
// public class BlogResourceTest {

//     @Test
//     public void testAddComment() {
//         // Blog-Eintrag erstellen
//         BlogDTO blogDTO = new BlogDTO();
//         blogDTO.setTitle("Test Blog");
//         blogDTO.setContent("Dies ist ein Blog für Kommentare");

//         Integer blogId = given()
//             .contentType("application/json")
//             .body(blogDTO)
//             .when()
//             .post("/blogs")
//             .then()
//             .statusCode(201)
//             .extract().path("id");
        
//         // Kommentar hinzufügen
//         CommentDTO commentDTO = new CommentDTO();
//         commentDTO.setText("Dies ist ein Testkommentar");
    
//         given()
//             .contentType("application/json")
//             .body(commentDTO)
//             .when()
//             .post("/blogs/" + blogId + "/comments")
//             .then()
//             .statusCode(201);
//     }

//     @Test
//     public void testAddCommentToNonExistingBlog() {
//         CommentDTO commentDTO = new CommentDTO();
//         commentDTO.setText("Dies ist ein Testkommentar");
    
//         given()
//             .contentType("application/json")
//             .body(commentDTO)
//             .when()
//             .post("/blogs/9999/comments") // Annahme: Blog mit ID 9999 existiert nicht
//             .then()
//             .statusCode(404)
//             .body("message", is("Blog mit ID 9999 nicht gefunden"));
//     }
    

//     @Test
//     public void testPagination() {
//         // Mindestens 3 Blogs existieren
//         BlogDTO blog1 = new BlogDTO();
//         blog1.setTitle("Blog 1");
//         blog1.setContent("Content 1");

//         BlogDTO blog2 = new BlogDTO();
//         blog2.setTitle("Blog 2");
//         blog2.setContent("Content 2");

//         BlogDTO blog3 = new BlogDTO();
//         blog3.setTitle("Blog 3");
//         blog3.setContent("Content 3");
    
//         given().contentType("application/json").body(blog1).post("/blogs").then().statusCode(201);
//         given().contentType("application/json").body(blog2).post("/blogs").then().statusCode(201);
//         given().contentType("application/json").body(blog3).post("/blogs").then().statusCode(201);
        
//         // Teste Pagination
//         given()
//             .queryParam("page", 0)
//             .queryParam("size", 2)
//             .when()
//             .get("/blogs")
//             .then()
//             .statusCode(200)
//             .body("$.size()", is(2));  // Es sollten nur 2 Blogs zurückgegeben werden
//     }

//     @Test
//     public void testGetAllBlogs() {
//         given()
//             .when().get("/blogs")
//             .then()
//             .statusCode(200);
//     }

//     @Test
//     public void testCreateBlog() {
//         BlogDTO newBlog = new BlogDTO();
//         newBlog.setTitle("Test Blog");
//         newBlog.setContent("This is a test content");

//         given()
//             .contentType("application/json")
//             .body(newBlog)
//             .when()
//             .post("/blogs")
//             .then()
//             .statusCode(201);
//     }

//     @Test
//     public void testUpdateBlog() {
//         // Blog erstellen
//         BlogDTO newBlog = new BlogDTO();
//         newBlog.setTitle("Original Title");
//         newBlog.setContent("Original Content");
    
//         Integer blogId = given()
//             .contentType("application/json")
//             .body(newBlog)
//             .when()
//             .post("/blogs")
//             .then()
//             .statusCode(201)
//             .extract().path("id");
    
//         // Blog aktualisieren
//         BlogDTO updatedBlog = new BlogDTO();
//         updatedBlog.setTitle("Updated Title");
//         updatedBlog.setContent("Updated Content");
    
//         given()
//             .contentType("application/json")
//             .body(updatedBlog)
//             .when()
//             .put("/blogs/" + blogId)
//             .then()
//             .statusCode(200)
//             .body("title", is("Updated Title"))
//             .body("content", is("Updated Content"));
//     }
    

//     @Test
//     public void testDeleteBlog() {
//         BlogDTO newBlog = new BlogDTO();
//         newBlog.setTitle("Test Blog");
//         newBlog.setContent("This is a test content");
    
//         Integer blogId = given()
//             .contentType("application/json")
//             .body(newBlog)
//             .when()
//             .post("/blogs")
//             .then()
//             .statusCode(201)
//             .extract().path("id");
    
//         // Blog löschen
//         given()
//             .when()
//             .delete("/blogs/" + blogId)
//             .then()
//             .statusCode(204);
    
//         // Überprüfen, dass der Blog nicht mehr existiert
//         given()
//             .when()
//             .get("/blogs/" + blogId)
//             .then()
//             .statusCode(404)
//             .body("message", is("Blog mit ID " + blogId + " nicht gefunden"));
//     }
    
    

//     @Test
//     public void testBlogDTOResponse() {
//         BlogDTO newBlog = new BlogDTO();
//         newBlog.setTitle("Test Blog");
//         newBlog.setContent("This is a test content");
    
//         Integer blogId = given()
//             .contentType("application/json")
//             .body(newBlog)
//             .when()
//             .post("/blogs")
//             .then()
//             .statusCode(201)
//             .extract().path("id");
    
//         given()
//             .when().get("/blogs/" + blogId)
//             .then()
//             .statusCode(200)
//             .body("id", is(blogId))
//             .body("title", is("Test Blog"))
//             .body("content", is("This is a test content"));
//     }       
// }
