package com.example.projek_uas;

public class Pesanan {
    public String id, namaProduk;
    public int harga, jumlah;

    public Pesanan() {} // Penting untuk Firebase

    public Pesanan(String id, String namaProduk, int harga, int jumlah) {
        this.id = id;
        this.namaProduk = namaProduk;
        this.harga = harga;
        this.jumlah = jumlah;
    }
}
