package dev.matheuscruz;

import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import io.dapr.client.domain.PublishEventRequest;
import io.quarkus.logging.Log;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.util.Map;
import java.util.UUID;

@Path("/messages")
public class SendMessageResource {

   @POST
   public Response send() {
      DaprClient build = new DaprClientBuilder().build();
      build.saveState("statestore", "orders", UUID.randomUUID().toString()).block();
      build.publishEvent(new PublishEventRequest("pubsub", "orders", Map.of("github", "mcruzdev"))).toFuture()
            .thenAccept(unused -> {
               System.out.println("result is " + unused);
            });
      return Response.accepted().build();
   }

}
