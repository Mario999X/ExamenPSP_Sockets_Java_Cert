package db;

import models.Nave;

import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class StarUnix {
    private final Stack<Nave> registros = new Stack<>();

    private final AtomicInteger misiles = new AtomicInteger(0);
    private final AtomicInteger contadorId = new AtomicInteger(0);

    public StarUnix() {
    }

    private final ReentrantLock lock = new ReentrantLock();

    public void add(Nave item) {
        try {
            lock.lock();
            contadorId.incrementAndGet();
            item.setId(contadorId.intValue());

            System.out.println("Agregando " + item);
            registros.add(item);
            misiles.addAndGet(item.getMisilesProtonicos());
        } finally {
            lock.unlock();
        }
    }

    public List<Nave> getAll() {
        System.out.println("Obteniendo registro completo");
        return registros.stream().toList();
    }

    public String getInfoDetallada() {
        System.out.println("Obteniendo misiles | Recuento de naves");
        return "Total naves: " + registros.size() + " |Total misiles: " + misiles;
    }
}
