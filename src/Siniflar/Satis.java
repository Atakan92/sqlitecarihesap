package Siniflar;

//import Siniflar.DB;
import Enumlar.KategoriEnum;
import Enumlar.MusteriEnum;
import Enumlar.UrunEnum;
import Propertiler.Kategori;
import Propertiler.Musteri;
import Propertiler.Urun;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class Satis {

    public ArrayList<Kategori> dataGetir(String query) {
        ArrayList<Kategori> ls = new ArrayList<>();
        ls.clear();
        DB db = new DB();
        try {

            ResultSet rs = db.baglan().executeQuery(query);
            while (rs.next()) {
                Kategori tb = new Kategori();
                tb.setKatID(rs.getString("" + KategoriEnum.katID));
                tb.setKatAdi(rs.getString("" + KategoriEnum.katAdi));
                ls.add(tb);
            }

        } catch (Exception e) {
            System.err.println("Data getirme hatası " + e);
        } finally {
            db.kapat();
        }
        return ls;
    }

    public void comboDoldur(ArrayList<Kategori> ls, JComboBox jcb) {

        DefaultComboBoxModel cm = new DefaultComboBoxModel();
        cm.addElement("Kategoriler");
        for (Kategori k : ls) {
            cm.addElement(k.getKatAdi());
        }
        jcb.setModel(cm);
    }

    public ArrayList<Urun> dataGetir(String query, int id) {
        ArrayList<Urun> ls = new ArrayList<>();
        ls.clear();
        DB db = new DB();
        try {

            ResultSet rs = db.baglan().executeQuery(query);
            while (rs.next()) {
                Urun tb = new Urun();
                tb.setUrID(rs.getString("" + UrunEnum.urID));
                tb.setUrAdi(rs.getString("" + UrunEnum.urAdi));
                tb.setUrSatisFiyati(rs.getString("" + UrunEnum.urSatis));
                tb.setUrunAciklama(rs.getString("" + UrunEnum.urAciklama));
                ls.add(tb);
            }
        } catch (Exception e) {
            System.err.println("Data getirme hatası " + e);
        } finally {
            db.kapat();
        }
        return ls;

    }

    public void ListeDoldur(ArrayList<Urun> ls, JTable jt) {
        DefaultTableModel dtm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };
        dtm.addColumn("Adı");
        dtm.addColumn("Satış Fiyatı(TL)");
        dtm.addColumn("Açıklama");

        for (Urun l : ls) {
            dtm.addRow(new String[]{l.getUrAdi(), l.getUrSatisFiyati(), l.getUrunAciklama()});

        }
        jt.setModel(dtm);
    }

    public ArrayList<Musteri> dataGetirMusteri(String query) {
        ArrayList<Musteri> ls = new ArrayList<>();
        ls.clear();
        DB db = new DB();
        try {

            ResultSet rs = db.baglan().executeQuery(query);
            while (rs.next()) {
                Musteri tb = new Musteri();
                tb.setMusID(rs.getString("" + MusteriEnum.musID));
                tb.setMusAdi(rs.getString("" + MusteriEnum.musAdi));
                tb.setMusSoyadi(rs.getString("" + MusteriEnum.musSoyadi));
                ls.add(tb);
            }

        } catch (Exception e) {
            System.err.println("Data getirme hatası " + e);
        } finally {
            db.kapat();
        }
        return ls;

    }

    public void comboDoldurMusteri(ArrayList<Musteri> ls, JComboBox jcb) {

        DefaultComboBoxModel cm = new DefaultComboBoxModel();
        cm.addElement("Müşteriler");
        for (Musteri m : ls) {

            cm.addElement(m.getMusAdi() + " " + m.getMusSoyadi());
        }
        jcb.setModel(cm);
    }

    public String idTespitMusteri(ArrayList<Musteri> ml, String ad) {
        String a = null;
        for (Musteri musteri : ml) {
            System.out.println(musteri.getMusAdi() + " " + musteri.getMusSoyadi());
            if ((musteri.getMusAdi() + " " + musteri.getMusSoyadi()).equalsIgnoreCase(ad)) {
                a = musteri.getMusID();
                break;
            }

        }
        return a;
    }

    public String idTespitUrun(ArrayList<Urun> ul, String ad) {
        String a = null;

        for (Urun urun : ul) {
            if (urun.getUrAdi().equals(ad)) {
                a = urun.getUrID();
                break;

            }

        }

        return a;
    }

    public int [] insertIslem(int urid, int musid, int adet) {
        DB db = new DB();
        int a[]=new int[2];
        String query = "insert into satis values(null," + musid + "," + urid + "," + adet + ",date('now'))";

        String query2 = "update urun set urStok=(Select urStok from urun where urID=" + urid + ")-" + adet + " where urID=" + urid + "";
        String query3 = "select urStok from urun where urID= " + urid + "";

        try {
            Statement st=db.baglan();
            ResultSet rs = st.executeQuery(query3);
            int b=Integer.valueOf(rs.getString("" + UrunEnum.urStok));
            a[0] = b - adet;

            if (a[0] < 0) {
                a[1]=b;
                a[0]=-1;

            } else {
               a[0]=1;
               st.executeUpdate(query);
               st.executeUpdate(query2);


            }
        } catch (Exception e) {
        } finally {
            db.kapat();
        }

        return a;

    }

}
