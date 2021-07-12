package pro.filaretov.spring.graphql;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

import com.google.common.io.Resources;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javax.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Provides GraphQL instance
 */
@Component
public class GraphQLProvider {

    private GraphQL graphQL;

    @Autowired
    private GraphQLDataFetchers graphQLDataFetchers;

    // TODO - bean is created in PostConstruct - is it fine?
    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }

    @SneakyThrows
    @PostConstruct
    public void init() {
        URL url = Resources.getResource("schema.graphqls");
        String schemaDefinitionLanguage = Resources.toString(url, StandardCharsets.UTF_8);
        GraphQLSchema schema = buildSchema(schemaDefinitionLanguage);
        this.graphQL = GraphQL.newGraphQL(schema).build();

    }

    private GraphQLSchema buildSchema(String schemaDefinitionLanguage) {
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(schemaDefinitionLanguage);
        RuntimeWiring runtimeWiring = buildWiring();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }

    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
            .type(newTypeWiring("Query")
                .dataFetcher("bookById", graphQLDataFetchers.getBookByIdDataFetcher()))
            .type(newTypeWiring("Book")
                .dataFetcher("author", graphQLDataFetchers.getAuthorDataFetcher()))

            .build();
    }

}
