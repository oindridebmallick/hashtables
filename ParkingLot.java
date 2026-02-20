import java.util.*;

public class ParkingLot {

    enum Status { EMPTY, OCCUPIED, DELETED }

    static class Spot {
        String plate;
        Status status = Status.EMPTY;
        long entryTime;
    }

    private Spot[] table;
    private int capacity;

    public ParkingLot(int capacity) {
        this.capacity = capacity;
        table = new Spot[capacity];
        for (int i = 0; i < capacity; i++)
            table[i] = new Spot();
    }

    private int hash(String plate) {
        return Math.abs(plate.hashCode()) % capacity;
    }

    public int parkVehicle(String plate) {
        int index = hash(plate);
        int probes = 0;

        while (table[index].status == Status.OCCUPIED) {
            index = (index + 1) % capacity;
            probes++;
        }

        table[index].plate = plate;
        table[index].status = Status.OCCUPIED;
        table[index].entryTime = System.currentTimeMillis();

        System.out.println("Parked at spot " + index + " (" + probes + " probes)");
        return index;
    }

    public void exitVehicle(String plate) {
        int index = hash(plate);

        while (!plate.equals(table[index].plate)) {
            index = (index + 1) % capacity;
        }

        long duration = System.currentTimeMillis() - table[index].entryTime;
        table[index].status = Status.DELETED;

        System.out.println("Vehicle exited. Duration: " + duration / 1000 + " seconds");
    }

    public static void main(String[] args) throws InterruptedException {
        ParkingLot lot = new ParkingLot(10);

        lot.parkVehicle("ABC123");
        Thread.sleep(2000);
        lot.exitVehicle("ABC123");
    }
}