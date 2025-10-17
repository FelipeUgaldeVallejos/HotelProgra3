package org.example.hotelgui.controller;

import javafx.application.Platform;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Supplier;



public final class Async {
    public static final ExecutorService EXECUTOR = new ThreadPoolExecutor(2, //core
            4, //maximo (va a depender de la capacidad de memoria)
            60L, TimeUnit.SECONDS,  //Ociocidad: cuanto va a esperar un hilo para ejecutarse
            new LinkedBlockingQueue<>(), r -> {
                    Thread t = new Thread(r);
                    t.setName("exec-bg-" + t.getId());
                    t.setDaemon(true); //la info que iba con el proces del hilo se mantenga
                    return t;
            }
    );


    public Async() {
    }

    //crear una tarea con resultados dentro del hilo principal de la interfaz grafica
    public static <T> void run(Supplier<T> supplier, Consumer<T> onSuccess,
                               Consumer<Throwable> onError) {
        EXECUTOR.submit(() -> {
            try {
                T result = supplier.get();
                if (onSuccess != null) {
                    Platform.runLater(() -> onSuccess.accept(result));
                }
            } catch (Throwable e) {
                if (onError != null)
                    Platform.runLater(() -> onError.accept(e));
            }
        });

    }

    //esta es una version del metodo run donde no hay un resultado
    //la accion se ejecuto pero no se necesita una respuesta
    //es un procedimiento que no necesito q finalice para continuar con lo que estaba haciendo
    public static void runVoid(Runnable action, Runnable onSuccess, Consumer<Throwable> onError) {
        EXECUTOR.submit(() -> {
            try {
                action.run();
                if  (onSuccess != null){
                    Platform.runLater(onSuccess);
                }
            }
            catch (Throwable ex) {
                if  (onError != null){
                    Platform.runLater(() -> onError.accept(ex));
                }
            }
        });
    }


    public static void shutdown() {
        EXECUTOR.shutdown(); //se necesitan apagar los hilos para mejor el rendimiento
    }

}


