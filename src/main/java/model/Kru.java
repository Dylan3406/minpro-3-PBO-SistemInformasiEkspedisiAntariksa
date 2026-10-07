package model;

public abstract class Kru implements EntitasAntariksa {
    private int idKru;
    private String nama;
    private int usia;
    public Kru(int idKru, String nama, int usia) {
        this.idKru = idKru;
        this.nama = nama;
        this.usia = usia;
    }
    public int getIdKru() {
        return idKru;
    }
    public void setIdKru(int idKru) {
        this.idKru = idKru;
    }
    public String getNama() {
        return nama;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }
    public int getUsia() {
        return usia;
    }
    public void setUsia(int usia) {
        this.usia = usia;
    }
    public abstract String getPeran();
    public abstract String getDetailTugas();
    @Override
    public void tampilkanData() {
        System.out.println("ID Kru       : " + idKru);
        System.out.println("Nama         : " + nama);
        System.out.println("Usia         : " + usia + " tahun");
        System.out.println("Peran        : " + getPeran());
        System.out.println("Detail Tugas : " + getDetailTugas());
    }
    @Override
    public String getIdentitas() {
        return "[KRU-" + getPeran().toUpperCase() + "] #" + idKru + " - " + nama;
    }
}