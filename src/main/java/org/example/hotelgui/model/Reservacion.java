package org.example.hotelgui.model;

import java.time.LocalDate;

public class Reservacion {
    private int idReservacion;
    private Habitacion habitacion;
    private Cliente cliente;
    private LocalDate fechaReservacion;
    private LocalDate fechaLlegada;
    private int cantidadNoches;
    private String fechaSalida;
    private double precioTotal;
    private double descuento;
    //private Parqueo parqueo; //conexion con parqueo ya q una reserva tiene un parqueo

    public Reservacion() {
    }

    public Reservacion(int idReservacion, Habitacion habitacion, Cliente cliente, LocalDate fechaLlegada, int cantidadNoches, double precioTotal) {
        this.idReservacion = idReservacion;
        this.habitacion = habitacion;
        this.cliente = cliente;
        this.fechaLlegada = fechaLlegada;
        this.cantidadNoches = cantidadNoches;
        this.precioTotal = precioTotal;
    }

    public int getIdReservacion() {
        return idReservacion;
    }

    public void setIdReservacion(int idReservacion) {
        this.idReservacion = idReservacion;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDate getFechaReservacion() {
        return fechaReservacion;
    }

    public void setFechaReservacion(LocalDate fechaReservacion) {
        this.fechaReservacion = fechaReservacion;
    }

    public LocalDate getFechaLlegada() {
        return fechaLlegada;
    }

    public void setFechaLlegada(LocalDate fechaLlegada) {
        this.fechaLlegada = fechaLlegada;
    }

    public int getCantidadNoches() {
        return cantidadNoches;
    }

    public void setCantidadNoches(int cantidadNoches) {
        this.cantidadNoches = cantidadNoches;
    }

    public String getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(String fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal() {
        //aqui deberiamos tener un calculo entre la totalidad de noches
        //y el precio de la habitacion
        this.precioTotal = this.habitacion.getPrecio() * cantidadNoches;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    //public Parqueo getParqueo() {
        //return parqueo;
    //}

    //public void setParqueo(Parqueo parqueo) {
        //this.parqueo = parqueo;
    //}
}
