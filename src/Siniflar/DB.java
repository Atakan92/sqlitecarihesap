package Siniflar;

import Enumlar.KategoriEnum;
import Enumlar.KullaniciEnum;
import Enumlar.MusteriEnum;
import Enumlar.SatisEnum;
import Enumlar.UrunEnum;
import Propertiler.Kullanici;
import Propertiler.Satis;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class DB {

    private String url = "org.sqlite.JDBC";
    private String db = "jdbc:sqlite:db/cariHesap.db";

    Connection conn = null;
    Statement st = null;

    public Statement baglan() {
        try {
            Class.forName(url);
            conn = DriverManager.getConnection(db);
            st = conn.createStatement();
        } catch (Exception e) {
            System.err.println("Bağlantı Hatası : " + e);
        }
        return st;
    }

    public void kapat() {
        try {
            if (conn != null || !conn.isClosed()) {
                st.close();
                conn.close();

            }
        } catch (Exception e) {
            System.err.println("Kapatma Hatası : " + e);
        }
    }

    public DefaultTableModel satisRaporu(String arananYer, String arananDeger, String basTarih, String bitTarih) {
        ArrayList<Satis> satisListesi = new ArrayList<>();
        String query = "";

        if (arananYer.equals("" + MusteriEnum.musAdi)) {
            String mSoyAdi = "%";
            String mAdi = "%";
            String[] isimDizi = arananDeger.split(" ");
            mAdi = isimDizi[0] + "%";
            if (isimDizi.length > 1) {
                mSoyAdi = isimDizi[isimDizi.length - 1] + "%";
                for (int i = 1; i < isimDizi.length - 1; i++) {
                    mAdi += " " + isimDizi[i] + "%";
                }
            }
            query = "select * from satis"
                    + " inner join musteri as m on satis.satMusID=m.musID"
                    + " inner join urun as u on satis.satUrunID=u.urID"
                    + " inner join kategori as k on k.katID=u.urKatID "
                    + "where julianday(" + SatisEnum.satTarih + ") between julianday('" + basTarih + "') and julianday('" + bitTarih + "')"
                    + "and " + MusteriEnum.musAdi + " like '" + mAdi + "' and " + MusteriEnum.musSoyadi + " like '" + mSoyAdi + "'";

            
        } else {
            String[] isimDizi = arananDeger.split(" ");
            arananDeger = isimDizi[0] + "%";
            for (int i = 1; i < isimDizi.length; i++) {
                arananDeger += " " + isimDizi[i] + "%";
            }
            query = "select * from satis"
                    + " inner join musteri as m on satis.satMusID=m.musID"
                    + " inner join urun as u on satis.satUrunID=u.urID"
                    + " inner join kategori as k on k.katID=u.urKatID "
                    + "where julianday(" + SatisEnum.satTarih + ") between julianday('" + basTarih + "') and julianday('" + bitTarih + "')"
                    + "and " + arananYer + " like '" + arananDeger + "'";

        }
        try {

            ResultSet rs = baglan().executeQuery(query);
            while (rs.next()) {
                Satis sat = new Satis();
                sat.setSatID(rs.getString("" + SatisEnum.satID));
                sat.setSatMusID(rs.getString("" + SatisEnum.satMusID));
                sat.setSatUrunID(rs.getString("" + SatisEnum.satUrunID));
                sat.setSatAdet(rs.getString("" + SatisEnum.satAdet));
                sat.setSatTarih(rs.getString("" + SatisEnum.satTarih));
                sat.setSatKategoriAdi(rs.getString("" + KategoriEnum.katAdi));
                String mAdi = rs.getString("" + MusteriEnum.musAdi);
                String mSoyadi = rs.getString("" + MusteriEnum.musSoyadi);
                sat.setSatMusteriAdi(mAdi + " " + mSoyadi);
                sat.setSatUrunAdi(rs.getString("" + UrunEnum.urAdi));
                sat.setSatUrunSatisFiyati(rs.getString("" + UrunEnum.urSatis));

                String[] tf = sat.getSatTarih().split("-");
                sat.setSatTarih(tf[2] + "/" + tf[1] + "/" + tf[0]);

                satisListesi.add(sat);
            }
        } catch (Exception e) {
            System.err.println("Veri tabanı bağlantı hatası : " + e);
        }finally{
                kapat();
            }

        DefaultTableModel dtm = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };
        //sutun bilgilerini 
        dtm.addColumn("Müşteri Adı");
        dtm.addColumn("Ürün Kategori");
        dtm.addColumn("Ürün Adı");
        dtm.addColumn("Adedi");
        dtm.addColumn("Fiyatı");
        dtm.addColumn("Satış Tarihi");
        for (Satis satis : satisListesi) {
            dtm.addRow(new String[]{satis.getSatMusteriAdi(), satis.getSatKategoriAdi(), satis.getSatUrunAdi(),
                satis.getSatAdet(), satis.getSatUrunSatisFiyati(), satis.getSatTarih()});
        }
        return dtm;
    }

    public String satisKarZarari(String arananYer, String arananDeger, String basTarih, String bitTarih) {
        String karzarar = "";
        String queryKarZarar = "";
        if (arananYer.equals("" + MusteriEnum.musAdi)) {
            String mSoyAdi = "%";
            String mAdi = "%";
            String[] isimDizi = arananDeger.split(" ");
            mAdi = isimDizi[0] + "%";
            if (isimDizi.length > 1) {
                mSoyAdi = isimDizi[isimDizi.length - 1] + "%";
                for (int i = 1; i < isimDizi.length - 1; i++) {
                    mAdi += " " + isimDizi[i] + "%";
                }
            }
            queryKarZarar = "select sum((satis.satAdet*u.urSatis)-(satis.satAdet*u.urAlis)) as karzarar from satis"
                    + " inner join musteri as m on satis.satMusID=m.musID"
                    + " inner join urun as u on satis.satUrunID=u.urID"
                    + " inner join kategori as k on k.katID=u.urKatID "
                    + "where julianday(" + SatisEnum.satTarih + ") between julianday('" + basTarih + "') and julianday('" + bitTarih + "')"
                    + "and " + MusteriEnum.musAdi + " like '" + mAdi + "' and " + MusteriEnum.musSoyadi + " like '" + mSoyAdi + "'";
        } else {
            String[] isimDizi = arananDeger.split(" ");
            arananDeger = isimDizi[0] + "%";
            for (int i = 1; i < isimDizi.length; i++) {
                arananDeger += " " + isimDizi[i] + "%";
            }
            queryKarZarar = "select sum((satis.satAdet*u.urSatis)-(satis.satAdet*u.urAlis)) as karzarar from satis"
                    + " inner join musteri as m on satis.satMusID=m.musID"
                    + " inner join urun as u on satis.satUrunID=u.urID"
                    + " inner join kategori as k on k.katID=u.urKatID "
                    + "where julianday(" + SatisEnum.satTarih + ") between julianday('" + basTarih + "') and julianday('" + bitTarih + "')"
                    + "and " + arananYer + " like '" + arananDeger + "'";
        }
        try {
            ResultSet rs2 = baglan().executeQuery(queryKarZarar);
            karzarar = rs2.getString("karzarar");
        } catch (Exception e) {
            System.err.println("Veri tabanı bağlantı hatası : " + e);
        }finally{
                kapat();
            }
        return karzarar;
    }

    public boolean GirisKontrol(Kullanici kul){
        String query="select count(*) as eslesme from kullanici where " +KullaniciEnum.kulAdi+ "='"+kul.getKulAdi()+"' "
                + "and "+KullaniciEnum.kulSifre+"='"+kul.getKulSifre()+"'";
        try {
            ResultSet rs=baglan().executeQuery(query);
            System.out.println(query);
            System.out.println(rs.getString("eslesme"));
            if(Integer.valueOf(rs.getString("eslesme"))>0){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
        }finally{
                kapat();
            }
        return false;
    }

public boolean sifreDegistir(String esifre, String ysifre){
    String query="select count(*) as eslesme from kullanici where "+KullaniciEnum.kulSifre+"='"+esifre+"' ";
     try {
            ResultSet rs=baglan().executeQuery(query);
            
            System.out.println(query);
            System.out.println(rs.getString("eslesme"));
            
            if(Integer.valueOf(rs.getString("eslesme"))>0){
                kapat();
                String updateQuery="update kullanici set "+KullaniciEnum.kulSifre+"='"+ysifre+"'";
                int sonuc = baglan().executeUpdate(updateQuery);
                System.out.println("sıkıntı yok");
                if(sonuc>0){
                    return true;
                }else{
                    return false;
                }
                    
            }else{
                return false;
            }
     }catch (Exception e) {
         System.err.println("Kullanici Veri Tabani Bağlantı Hatası: " + e);
        }finally{
                kapat();
            }
            
    return false;
}
}
