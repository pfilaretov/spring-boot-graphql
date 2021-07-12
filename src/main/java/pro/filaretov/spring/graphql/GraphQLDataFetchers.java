package pro.filaretov.spring.graphql;

import com.google.common.collect.ImmutableMap;
import graphql.schema.DataFetcher;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Data fetchers for schema fields
 */
@Component
public class GraphQLDataFetchers {

    private static final List<Map<String, String>> BOOKS = List.of(
        ImmutableMap.of("id", "book-1",
            "name", "Harry Potter and the Philosopher's Stone",
            "pageCount", "223",
            "authorId", "author-1"),
        ImmutableMap.of("id", "book-2",
            "name", "Moby Dick",
            "pageCount", "635",
            "authorId", "author-2"),
        ImmutableMap.of("id", "book-3",
            "name", "Interview with the vampire",
            "pageCount", "371",
            "authorId", "author-3")
    );

    private static final List<Map<String, String>> AUTHORS = List.of(
        ImmutableMap.of("id", "author-1",
            "firstName", "Joanne",
            "lastName", "Rowling"),
        ImmutableMap.of("id", "author-2",
            "firstName", "Herman",
            "lastName", "Melville"),
        ImmutableMap.of("id", "author-3",
            "firstName", "Anne",
            "lastName", "Rice")
    );

    // TODO - create a bean out of this method?
    public DataFetcher getBookByIdDataFetcher() {
        return dataFetchingEnvironment -> {
            String bookId = dataFetchingEnvironment.getArgument("id");
            return BOOKS
                .stream()
                .filter(book -> book.get("id").equals(bookId))
                .findFirst()
                .orElse(null);
        };
    }

    // TODO - create a bean out of this method?
    public DataFetcher getAuthorDataFetcher() {
        return dataFetchingEnvironment -> {
            Map<String, String> book = dataFetchingEnvironment.getSource();
            String authorId = book.get("authorId");
            return AUTHORS
                .stream()
                .filter(author -> author.get("id").equals(authorId))
                .findFirst()
                .orElse(null);
        };
    }

}
