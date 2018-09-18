package com.example.itemService.web;

import com.example.itemService.dao.InMemoryLRUCache;
import com.example.itemService.dao.InMemoryTTLCache;
import com.example.itemService.models.ItemInfo;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@ComponentScan(basePackages = "com.example.itemService.dao")
public class RestServer extends AbstractVerticle {

    @Autowired
    private InMemoryTTLCache inMemoryTTLCache;

    @Autowired
    private InMemoryLRUCache inMemoryLRUCache;

    @Override
    public void start(Future<Void> future) {

        // Create a router object.
        Router router = Router.router(vertx);

        inMemoryTTLCache = new InMemoryTTLCache();
        inMemoryLRUCache = new InMemoryLRUCache();

        //router.route().handler(BodyHandler.create());

        router.get("/items").handler(this::getAll);

        router.route("/items*").handler(BodyHandler.create());
        router.post("/items").handler(this::addItem);

        // Create the HTTP server and pass the "accept" method to the request handler.
        vertx.createHttpServer().requestHandler(router::accept).listen(8080);

    }


    private void addItem(RoutingContext routingContext) {
        final ItemInfo itemInfo = Json.decodeValue(routingContext.getBodyAsString(),
                ItemInfo.class);
        inMemoryTTLCache.put(itemInfo.getItem().getId(),itemInfo);
        inMemoryLRUCache.set(itemInfo.getItem().getId(),itemInfo);
        routingContext.response()
                .setStatusCode(201)
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(itemInfo));
    }

    private void getAll(RoutingContext routingContext) {
        Collection<ItemInfo> itemsFromLRUCache = inMemoryLRUCache.getAll();
        Collection<ItemInfo> itemsFromTTLCache = inMemoryTTLCache.values();

        if(itemsFromLRUCache.size()>=itemsFromTTLCache.size())
        {
            routingContext.response()
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(itemsFromLRUCache));
        }
        else
        {
            routingContext.response()
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(itemsFromTTLCache));
        }


    }


}
