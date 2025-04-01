package dev.matheuscruz;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import io.smallrye.common.annotation.NonBlocking;
import io.smallrye.mutiny.Multi;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

@Path("/dapr/subscribe")
public class SubscribeResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Subscription> subscribe() {
        return List.of(new Subscription("pubsub", "orders", new Routes("/pubsub/orders")));
    }

    @Incoming("orders")
    public Uni<Void> consume(Message<String> message) {
        return Uni.createFrom().completionStage(
                () -> {
                    Log.infof("message body %s", message.getPayload());
                    return message.ack();
                });
    }

    @Outgoing("orders.each.second")
    @NonBlocking
    public Multi<String> produce() {
        return Multi.createFrom().ticks()
              .every(Duration.ofSeconds(1))
              .map(l ->{
                  Log.infof("producing message to orders.each.second %s", l);
                  return l.toString();
              });
    }

    public record Subscription(String pubsubName, String topic, Routes routes) {
    }

    public record Routes(@JsonProperty("default") String defaultRoute) {
    }
}
