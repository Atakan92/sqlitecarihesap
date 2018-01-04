/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carihesapprogrami;

import Enumlar.KategoriEnum;
import Enumlar.MusteriEnum;
import Enumlar.UrunEnum;
import Propertiler.Kategori;
import Propertiler.Musteri;
import Propertiler.Urun;
import Siniflar.DB;
import Siniflar.Satis;
import Siniflar.SpinnerTarih;
import Siniflar.TabloyaVeriCek;
import Siniflar.ValidationKontrol;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Java_sabah
 */
public class NewJFrame extends javax.swing.JFrame {

    /**
     * Creates new form NewJFrame
     */
    public NewJFrame() throws Exception {
        initComponents();
        this.setIconImage(Toolkit.getDefaultToolkit().getImage("media/para.png"));

        //Emre Kod Satiri
        SpinnerTarih st = new SpinnerTarih();
        int yil;
        int ay;

        jSpinnerRaporYilBitis.setModel(st.SpinnerYilAyarla());
        jSpinnerRaporAyBitis.setModel(st.SpinnerAyAyarla());
        yil = (int) jSpinnerRaporYilBitis.getValue();
        ay = (int) jSpinnerRaporAyBitis.getValue();
        jSpinnerRaporGunBitis.setModel(st.SpinnerGunAyarla(0, ay, yil));

        jSpinnerRaporYilBaslangic.setModel(st.SpinnerYilAyarla());
        jSpinnerRaporAyBaslangic.setModel(st.SpinnerAyAyarla());
        yil = (int) jSpinnerRaporYilBaslangic.getValue();
        ay = (int) jSpinnerRaporAyBaslangic.getValue();
        jSpinnerRaporGunBaslangic.setModel(st.SpinnerGunAyarla(0, ay, yil));

        jRadioButtonRaporMusteri.setSelected(true);
        raporTabloGuncelle();
        jLabelRaporUyari.setVisible(false);

        setEnabled(false, jPanel14.getComponents());/////Panelerin Enabled Disabled özeli

        //Emre Kod Satiri Sonu
        //Atakan kod Satısı
        jPanelSatis1.setVisible(false);
        jPanelSatis2.setVisible(false);

        satis.comboDoldur(satis.dataGetir(queryKategori), jComboBoxSatisKategori);
        //Atakan kod Satırı Sonu
        //Samet Kod Satırı

        urunDataGetir();
        urunComboGetir();

        setEnabled(false, jPanel14.getComponents());
        jButtonUrunListesiDuzenle.setEnabled(false);
        jButtonUrunListesiSil.setEnabled(false);
        jButtonUrunDuzenle.setEnabled(false);
        jPanel14.setEnabled(false);
        jTableUrunListesi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //Samet Kod Satırı

        //Mehmet Şeyhmus Kod Satırı
        jTableKategoriListe.setModel(tvc.datavericek());
        musteriDataGetir();
        setEnabled(false, jPanel19.getComponents());
        jButtonKategoriListeDuzenle.setEnabled(false);
        jButtonKategoriListeSil.setEnabled(false);
        jButtonKategoriDuzenle.setEnabled(false);
        jPanel19.setEnabled(false);
        jTableKategoriListe.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setEnabled(false, jPanel11.getComponents());
        jButtonMusteriListeDuzenle.setEnabled(false);
        jButtonMusteriListeSil.setEnabled(false);
        jButtonMusteriDuzenle.setEnabled(false);
        jPanel11.setEnabled(false);
        jTableMusteriBilgi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //Mehmet Şeyhmus Kod Satırı sonu
    }
    //Mehmet Şeyhmus Kod Satırı
    DB db = new DB();
    ArrayList<Kategori> ls = new ArrayList<>();
    ArrayList<Musteri> pms = new ArrayList<>();
    TabloyaVeriCek tvc = new TabloyaVeriCek();
    String id = "";

    public void musteriDataGetir() {
        pms.clear();
        DB db = new DB();
        try {
            String query = "select *from musteri";
            ResultSet rs = db.baglan().executeQuery(query);
            while (rs.next()) {
                Musteri ks = new Musteri();
                ks.setMusID(rs.getString("" + MusteriEnum.musID));
                ks.setMusAdi(rs.getString("" + MusteriEnum.musAdi));
                ks.setMusSoyadi(rs.getString("" + MusteriEnum.musSoyadi));
                ks.setMusTelefon(rs.getString("" + MusteriEnum.musTelefon));
                ks.setMusAdres(rs.getString("" + MusteriEnum.musAdres));

                pms.add(ks);
            }
        } catch (Exception e) {
            System.err.println("Data Getirme Hatası : " + e);
        } finally {
            db.kapat();
        }

        

        DefaultTableModel dtm = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };

        dtm.addColumn("Sıra");
        dtm.addColumn("Müşteri Adı");
        dtm.addColumn("Müşteri Soyadı");
        dtm.addColumn("Müşteri Telefonu");
        dtm.addColumn("Müşteri Adresi");

        for (Musteri l : pms) {
            dtm.addRow(new String[]{l.getMusID(), l.getMusAdi(), l.getMusSoyadi(), l.getMusTelefon(), l.getMusAdres()});
        }
        jTableMusteriBilgi.setModel(dtm);

    }
    //Mehmet Şeyhmus Kod Satırı Sonu
    //Samet Kod Satırı
    ArrayList<Urun> urn = new ArrayList<>();
    ArrayList<Kategori> ktgr = new ArrayList<>();
    int urunSecim = 0;
    //Samet Kod Satırı
    //Atakan Class Kod Satırı
    ArrayList<Musteri> ml = new ArrayList<Musteri>();
    ArrayList<Urun> ul = new ArrayList<Urun>();
    String musteriQuery = "Select m.musID,m.musAdi,m.musSoyadi from musteri as m";
    String uptadeMusteriId = null;
    String uptadeUrunId = null;

    String queryKategori = "Select katID,katAdi from kategori ";
    int comboID;
    String kategoriSecilen;
    String secilenMusteri;
    Satis satis = new Satis();
    //Atakan Kod Satırı Sonu

    //Emre Kod Satiri
    public void setEnabled(boolean enabled, Component[] gelen) {
        for (Component component : gelen) {
            component.setEnabled(enabled);
        }
    }

    private void spinnerTarihUyari() {
        String bitistarihi;
        String baslangictarihi;
        String yil;
        String gun;
        String ay;
        yil = "" + jSpinnerRaporYilBitis.getValue();
        ay = tarihKontrol("" + jSpinnerRaporAyBitis.getValue());
        gun = tarihKontrol("" + jSpinnerRaporGunBitis.getValue());
        bitistarihi = yil + ay + gun;
        yil = "" + jSpinnerRaporYilBaslangic.getValue();
        ay = tarihKontrol("" + jSpinnerRaporAyBaslangic.getValue());
        gun = tarihKontrol("" + jSpinnerRaporGunBaslangic.getValue());
        baslangictarihi = yil + ay + gun;
        if (Integer.valueOf(bitistarihi) < Integer.valueOf(baslangictarihi)) {
            jLabelRaporUyari.setVisible(true);
        } else {
            jLabelRaporUyari.setVisible(false);
        }

    }

    private void spinnerDuzenleBaslangic() {

        SpinnerTarih st = new SpinnerTarih();
        int yil = (int) jSpinnerRaporYilBaslangic.getValue();
        int ay = (int) jSpinnerRaporAyBaslangic.getValue();
        int gun = (int) jSpinnerRaporGunBaslangic.getValue();
        jSpinnerRaporGunBaslangic.setModel(st.SpinnerGunAyarla(gun, ay, yil));
    }

    private void spinnerDuzenleBitis() {
        SpinnerTarih st = new SpinnerTarih();
        int yil = (int) jSpinnerRaporYilBitis.getValue();
        int ay = (int) jSpinnerRaporAyBitis.getValue();
        int gun = (int) jSpinnerRaporGunBitis.getValue();
        jSpinnerRaporGunBitis.setModel(st.SpinnerGunAyarla(gun, ay, yil));
    }

    private String tarihKontrol(String tarih) {
        if (Integer.valueOf(tarih) < 10) {
            tarih = "0" + tarih;
        }
        return tarih;
    }

    private void raporTabloGuncelle() {
        DB db = new DB();
        String gun = tarihKontrol("" + jSpinnerRaporGunBaslangic.getValue());
        String ay = tarihKontrol("" + jSpinnerRaporAyBaslangic.getValue());

        String basTarih = "" + jSpinnerRaporYilBaslangic.getValue() + "-" + ay + "-" + gun;
        gun = tarihKontrol("" + jSpinnerRaporGunBitis.getValue());
        ay = tarihKontrol("" + jSpinnerRaporAyBitis.getValue());

        String bitTarih = "" + jSpinnerRaporYilBitis.getValue() + "-" + ay + "-" + gun;

        String arananDeger = jTextFieldRaporArama.getText();

        String arananYer = "";
        if (jRadioButtonRaporMusteri.isSelected()) {
            arananYer = "" + MusteriEnum.musAdi;
        } else if (jRadioButtonRaporUrunadi.isSelected()) {
            arananYer = "" + UrunEnum.urAdi;
        } else if (jRadioButtonRaporKategori.isSelected()) {
            arananYer = "" + KategoriEnum.katAdi;
        }

        jTableRaporSonuc.setModel(db.satisRaporu(arananYer, arananDeger, basTarih, bitTarih));
        karzarar = db.satisKarZarari(arananYer, arananDeger, basTarih, bitTarih);
        if (karzarar == null) {
            karzarar = "0";
        }
        jLabelRaporKarZarar.setText(karzarar + " TL");
        jTableRaporSonuc.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    String karzarar = "";

    //Emre Kod Satiri Sonu
    //Samet Kod Satırı
    public void urunDataGetir() {
        urn.clear();

        DB db = new DB();
        try {
            String query = "select *from urun,kategori where urun.urKatID=kategori.katID";
            ResultSet rs = db.baglan().executeQuery(query);

            while (rs.next()) {
                Urun ur = new Urun();

                ur.setUrID(rs.getString("" + UrunEnum.urID));
                ur.setUrAdi(rs.getString("" + UrunEnum.urAdi));

                ur.setUrKatID(rs.getString("" + KategoriEnum.katAdi));
                ur.setUrAlisFiyati(rs.getString("" + UrunEnum.urAlis));
                ur.setUrSatisFiyati(rs.getString("" + UrunEnum.urSatis));
                ur.setUrStokAdedi(rs.getString("" + UrunEnum.urStok));
                ur.setUrunAciklama(rs.getString("" + UrunEnum.urAciklama));
                urn.add(ur);

            }

        } catch (SQLException e) {
            System.err.println("Data Getirme Hatası !");
        }

       

        DefaultTableModel dtm = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };
        dtm.addColumn("Ürün Adı");
        dtm.addColumn("Kategori");
        dtm.addColumn("Alış Fiyatı");
        dtm.addColumn("Satış Fiyatı");
        dtm.addColumn("Stok");
        dtm.addColumn("Açıklama");

        for (Urun u : urn) {
            dtm.addRow(new String[]{u.getUrAdi(), u.getUrKatID(), u.getUrAlisFiyati(), u.getUrSatisFiyati(), u.getUrStokAdedi(), u.getUrunAciklama()});
        }
        jTableUrunListesi.setModel(dtm);
        
    }
    //Samet Kod Satırı

    //Samet Kod Satırı
    public void urunComboGetir() {

        ktgr.clear();
        DB db = new DB();
        try {
            String query = "select *from kategori";
            ResultSet rs = db.baglan().executeQuery(query);
            while (rs.next()) {
                Kategori xx = new Kategori();
                xx.setKatID(rs.getString("" + KategoriEnum.katID));
                try {
                    xx.setKatAdi(rs.getString("" + KategoriEnum.katAdi));
                } catch (Exception ex) {
                    Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                ktgr.add(xx);
                //kt.setKatAdi(rs.getString("" + KategoriEnum.katAdi));

                //ktgr.add(kt);
            }

        } catch (SQLException e) {
        } finally {
            db.kapat();
        }

        

        DefaultComboBoxModel cmb = new DefaultComboBoxModel();
        DefaultComboBoxModel cmbb = new DefaultComboBoxModel();
        cmb.addElement("Lütfen Kategori Seçiniz");
        cmbb.addElement("Lütfen Kategori Seçiniz");
        for (Kategori k : ktgr) {
            cmb.addElement(k.getKatAdi());
            cmbb.addElement(k.getKatAdi());
        }
        jComboBoxUrunKategoriEkle.setModel(cmb);
        jComboBoxUrunKategoriDuzenle.setModel(cmbb);

    }
    //Samet Kod Satırı

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableMusteriBilgi = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jButtonMusteriListeDuzenle = new javax.swing.JButton();
        jButtonMusteriListeSil = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jTextFieldMusteriAdiDuzenle = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTextFieldMusteriSoyadiDuzenle = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextFieldMusteriTelefonDuzenle = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextFieldMusteriAdresDuzenle = new javax.swing.JTextField();
        jButtonMusteriDuzenle = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldMusteriAdiEkle = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextMusteriSoyadiEkle = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldMusteriTelefonEkle = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldMusteriAdresEkle = new javax.swing.JTextField();
        jButtonMusteriEkle = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jRadioButtonRaporMusteri = new javax.swing.JRadioButton();
        jRadioButtonRaporKategori = new javax.swing.JRadioButton();
        jRadioButtonRaporUrunadi = new javax.swing.JRadioButton();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jTextFieldRaporArama = new javax.swing.JTextField();
        jPanel18 = new javax.swing.JPanel();
        jSpinnerRaporYilBaslangic = new javax.swing.JSpinner();
        jLabel39 = new javax.swing.JLabel();
        jSpinnerRaporAyBaslangic = new javax.swing.JSpinner();
        jLabel38 = new javax.swing.JLabel();
        jSpinnerRaporGunBaslangic = new javax.swing.JSpinner();
        jLabel37 = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        jSpinnerRaporYilBitis = new javax.swing.JSpinner();
        jSpinnerRaporAyBitis = new javax.swing.JSpinner();
        jSpinnerRaporGunBitis = new javax.swing.JSpinner();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabelRaporUyari = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTableRaporSonuc = new javax.swing.JTable();
        jPanel24 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        jLabelRaporKarZarar = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jComboBoxSatisKategori = new javax.swing.JComboBox<>();
        jButtonSatisKategoriListele = new javax.swing.JButton();
        jLabel33 = new javax.swing.JLabel();
        jPanelSatis1 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableSatisKategori = new javax.swing.JTable();
        jPanelSatis2 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabelSatisSecilenUrun = new javax.swing.JLabel();
        jTextFieldSatisAdet = new javax.swing.JTextField();
        jComboBoxSatisMusteriAdi = new javax.swing.JComboBox<>();
        jButtonSatisTamamla = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldUrunAdiEkle = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldUrunAlisEkle = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldUrunSatisEkle = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextFieldUrunStokEkle = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextFieldUrunAciklamaEkle = new javax.swing.JTextField();
        jComboBoxUrunKategoriEkle = new javax.swing.JComboBox<>();
        jButtonUrunKaydet = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableUrunListesi = new javax.swing.JTable();
        jPanel12 = new javax.swing.JPanel();
        jButtonUrunListesiSil = new javax.swing.JButton();
        jButtonUrunListesiDuzenle = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jTextFieldUrunAdiDuzenle = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jTextFieldUrunAlisDuzenle = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        jTextFieldUrunSatisDuzenle = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        jTextFieldUrunStokDuzenle = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        jTextFieldUrunAciklamaDuzenle = new javax.swing.JTextField();
        jComboBoxUrunKategoriDuzenle = new javax.swing.JComboBox<>();
        jButtonUrunDuzenle = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jTextFieldKullaniciEskiSifre = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jTextFieldKullaniciYeniSifre = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jTextFieldKullaniciYeniSifreTekrar = new javax.swing.JTextField();
        jButtonKullaniciSifreDegistir = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jTextFieldKategoriAdiEkle = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jTextFieldKategoriAciklamaEkle = new javax.swing.JTextField();
        jButtonKategoriKaydet = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableKategoriListe = new javax.swing.JTable();
        jPanel16 = new javax.swing.JPanel();
        jButtonKategoriListeDuzenle = new javax.swing.JButton();
        jButtonKategoriListeSil = new javax.swing.JButton();
        jPanel19 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jTextFieldKategoriAdiDuzenle = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jTextFieldKategoriAciklamaDuzenle = new javax.swing.JTextField();
        jButtonKategoriDuzenle = new javax.swing.JButton();
        jButtonCikis = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("CARİ HESAP PROGRAMI v.1.0.0");
        setResizable(false);

        jTabbedPane1.setPreferredSize(new java.awt.Dimension(1157, 700));

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Müşteri Bilgileri"));

        jTableMusteriBilgi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTableMusteriBilgi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableMusteriBilgiMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableMusteriBilgi);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Satır İşlemleri"));

        jButtonMusteriListeDuzenle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/duzen.png"))); // NOI18N
        jButtonMusteriListeDuzenle.setText("DÜZENLE");
        jButtonMusteriListeDuzenle.setPreferredSize(new java.awt.Dimension(120, 40));
        jButtonMusteriListeDuzenle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMusteriListeDuzenleActionPerformed(evt);
            }
        });

        jButtonMusteriListeSil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/delete.png"))); // NOI18N
        jButtonMusteriListeSil.setText("SİL");
        jButtonMusteriListeSil.setPreferredSize(new java.awt.Dimension(120, 40));
        jButtonMusteriListeSil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMusteriListeSilActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonMusteriListeDuzenle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonMusteriListeSil, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap(42, Short.MAX_VALUE)
                .addComponent(jButtonMusteriListeDuzenle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jButtonMusteriListeSil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 794, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Müşteri Düzenle"));

        jLabel9.setText("Müşteri Adı");

        jLabel10.setText("Soyadı");

        jLabel11.setText("Telefon");

        jLabel12.setText("Adres");

        jButtonMusteriDuzenle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/tick.png"))); // NOI18N
        jButtonMusteriDuzenle.setText("DÜZENLE");
        jButtonMusteriDuzenle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMusteriDuzenleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonMusteriDuzenle, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldMusteriAdiDuzenle, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldMusteriSoyadiDuzenle))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldMusteriTelefonDuzenle))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldMusteriAdresDuzenle)))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldMusteriAdiDuzenle, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldMusteriSoyadiDuzenle, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldMusteriTelefonDuzenle, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldMusteriAdresDuzenle, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonMusteriDuzenle, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Yeni Müşteri Ekle"));

        jLabel1.setText("Müşteri Adı");

        jTextFieldMusteriAdiEkle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldMusteriAdiEkleActionPerformed(evt);
            }
        });

        jLabel2.setText("Soyadı");

        jLabel3.setText("Telefon");

        jLabel4.setText("Adres");

        jButtonMusteriEkle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/tick.png"))); // NOI18N
        jButtonMusteriEkle.setText("KAYDET");
        jButtonMusteriEkle.setPreferredSize(new java.awt.Dimension(120, 40));
        jButtonMusteriEkle.setSelected(true);
        jButtonMusteriEkle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMusteriEkleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonMusteriEkle, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldMusteriAdiEkle, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextMusteriSoyadiEkle))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldMusteriTelefonEkle))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldMusteriAdresEkle)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldMusteriAdiEkle, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextMusteriSoyadiEkle, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldMusteriTelefonEkle, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldMusteriAdresEkle, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonMusteriEkle, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 29, Short.MAX_VALUE))
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("MÜŞTERİLER", jPanel1);

        jPanel22.setBorder(javax.swing.BorderFactory.createTitledBorder("Filtre"));

        jLabel34.setText("Aranılacak Kriter :");

        buttonGroup1.add(jRadioButtonRaporMusteri);
        jRadioButtonRaporMusteri.setText("Müşteri");

        buttonGroup1.add(jRadioButtonRaporKategori);
        jRadioButtonRaporKategori.setText("Kategori");

        buttonGroup1.add(jRadioButtonRaporUrunadi);
        jRadioButtonRaporUrunadi.setText("Ürün Adı");

        jLabel35.setText("Arama :");

        jLabel36.setText("Tarih Aralığı :");

        jTextFieldRaporArama.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldRaporAramaKeyReleased(evt);
            }
        });

        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder("Başlangıç Tarihi"));
        jPanel18.setToolTipText("");

        jSpinnerRaporYilBaslangic.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerRaporYilBaslangicStateChanged(evt);
            }
        });

        jLabel39.setText("Yıl");

        jSpinnerRaporAyBaslangic.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerRaporAyBaslangicStateChanged(evt);
            }
        });

        jLabel38.setText("Ay");

        jSpinnerRaporGunBaslangic.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerRaporGunBaslangicStateChanged(evt);
            }
        });

        jLabel37.setText("Gün");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinnerRaporGunBaslangic, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSpinnerRaporAyBaslangic, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSpinnerRaporYilBaslangic, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel38)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel39)
                        .addGap(28, 28, 28))))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel37))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinnerRaporYilBaslangic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinnerRaporAyBaslangic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinnerRaporGunBaslangic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel25.setBorder(javax.swing.BorderFactory.createTitledBorder("Bitiş Tarihi"));

        jSpinnerRaporYilBitis.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerRaporYilBitisStateChanged(evt);
            }
        });

        jSpinnerRaporAyBitis.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerRaporAyBitisStateChanged(evt);
            }
        });

        jSpinnerRaporGunBitis.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerRaporGunBitisStateChanged(evt);
            }
        });

        jLabel43.setText("Gün");

        jLabel44.setText("Ay");

        jLabel45.setText("Yıl");

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(jLabel44)
                        .addGap(43, 43, 43)
                        .addComponent(jLabel45))
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jSpinnerRaporGunBitis, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSpinnerRaporAyBitis, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(jSpinnerRaporYilBitis, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSpinnerRaporYilBitis, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jSpinnerRaporGunBitis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jSpinnerRaporAyBitis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel43))
                        .addGap(26, 26, 26)))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jLabelRaporUyari.setForeground(new java.awt.Color(255, 0, 0));
        jLabelRaporUyari.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelRaporUyari.setText("Başlangıç Tarihi Bitiş Tarihinden Büyük Olamaz!!!");

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel22Layout.createSequentialGroup()
                                .addComponent(jRadioButtonRaporMusteri, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jRadioButtonRaporUrunadi, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(40, 40, 40)
                                .addComponent(jRadioButtonRaporKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jTextFieldRaporArama)))
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel22Layout.createSequentialGroup()
                                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE))
                            .addComponent(jLabelRaporUyari, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(411, Short.MAX_VALUE))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButtonRaporUrunadi, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioButtonRaporMusteri, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioButtonRaporKategori))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldRaporArama)
                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel25, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                        .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17))
                    .addComponent(jPanel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addComponent(jLabelRaporUyari)
                .addGap(43, 43, 43))
        );

        jPanel23.setBorder(javax.swing.BorderFactory.createTitledBorder("Sonuç"));

        jTableRaporSonuc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Müşteri Adı", "Ürün Kategori", "Ürün Adı", "Adedi", "Fiyatı", "Satış Tarihi"
            }
        ));
        jScrollPane5.setViewportView(jTableRaporSonuc);

        jPanel24.setBorder(javax.swing.BorderFactory.createTitledBorder("Genel Kar-Zarar Durumu"));

        jLabel40.setText("KAR-ZARAR DURUMU : ");

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel40)
                .addGap(145, 145, 145)
                .addComponent(jLabelRaporKarZarar, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(333, Short.MAX_VALUE))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabelRaporKarZarar, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 973, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(69, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("RAPORLAR", jPanel2);

        jPanel21.setBorder(javax.swing.BorderFactory.createTitledBorder("Satış Ekranı"));

        jComboBoxSatisKategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxSatisKategori.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxSatisKategoriİtemStateChanged(evt);
            }
        });
        jComboBoxSatisKategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxSatisKategoriActionPerformed(evt);
            }
        });

        jButtonSatisKategoriListele.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/list.png"))); // NOI18N
        jButtonSatisKategoriListele.setText("LİSTELE");
        jButtonSatisKategoriListele.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSatisKategoriListeleActionPerformed(evt);
            }
        });

        jLabel33.setText("Listelenecek Ürün Kategorisi Seçiniz ...");

        jTableSatisKategori.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Adı", "Satış Fiyatı(TL)", "Açıklama"
            }
        ));
        jTableSatisKategori.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableSatisKategoriMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTableSatisKategori);

        javax.swing.GroupLayout jPanelSatis1Layout = new javax.swing.GroupLayout(jPanelSatis1);
        jPanelSatis1.setLayout(jPanelSatis1Layout);
        jPanelSatis1Layout.setHorizontalGroup(
            jPanelSatis1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSatis1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 659, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelSatis1Layout.setVerticalGroup(
            jPanelSatis1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSatis1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel31.setText("Seçilen Ürün");

        jLabel30.setText("Adet");

        jLabel32.setText("Müşteri");

        jComboBoxSatisMusteriAdi.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxSatisMusteriAdi.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxSatisMusteriAdiİtemStateChanged(evt);
            }
        });
        jComboBoxSatisMusteriAdi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxSatisMusteriAdiActionPerformed(evt);
            }
        });

        jButtonSatisTamamla.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/tick.png"))); // NOI18N
        jButtonSatisTamamla.setText("SATIŞI TAMAMLA");
        jButtonSatisTamamla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSatisTamamlaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelSatis2Layout = new javax.swing.GroupLayout(jPanelSatis2);
        jPanelSatis2.setLayout(jPanelSatis2Layout);
        jPanelSatis2Layout.setHorizontalGroup(
            jPanelSatis2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSatis2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelSatis2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelSatis2Layout.createSequentialGroup()
                        .addGroup(jPanelSatis2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelSatis2Layout.createSequentialGroup()
                                .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelSatisSecilenUrun, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanelSatis2Layout.createSequentialGroup()
                                .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldSatisAdet)))
                        .addGap(60, 60, 60)
                        .addComponent(jButtonSatisTamamla)
                        .addGap(24, 24, 24))
                    .addGroup(jPanelSatis2Layout.createSequentialGroup()
                        .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxSatisMusteriAdi, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(237, Short.MAX_VALUE))))
        );
        jPanelSatis2Layout.setVerticalGroup(
            jPanelSatis2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSatis2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelSatis2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelSatisSecilenUrun, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelSatis2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldSatisAdet, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonSatisTamamla, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelSatis2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBoxSatisMusteriAdi, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(83, 83, 83))
        );

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel21Layout.createSequentialGroup()
                            .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jComboBoxSatisKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonSatisKategoriListele, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jPanelSatis1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanelSatis2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(372, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonSatisKategoriListele, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBoxSatisKategori))
                .addComponent(jLabel33)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanelSatis1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanelSatis2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("SATIŞ YÖNETİMİ", jPanel3);

        jPanel4.setPreferredSize(new java.awt.Dimension(1152, 600));

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Yeni Ürün Ekle"));

        jLabel5.setText("Ürün Adı");

        jLabel6.setText("Kategori");

        jLabel7.setText("Alış Fiyatı");

        jLabel8.setText("Satış Fiyatı");

        jLabel13.setText("Stok");

        jLabel14.setText("Açıklama");

        jComboBoxUrunKategoriEkle.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxUrunKategoriEkle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxUrunKategoriEkleActionPerformed(evt);
            }
        });

        jButtonUrunKaydet.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/tick.png"))); // NOI18N
        jButtonUrunKaydet.setText("KAYDET");
        jButtonUrunKaydet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUrunKaydetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldUrunAciklamaEkle))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldUrunStokEkle))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldUrunSatisEkle))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldUrunAlisEkle))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldUrunAdiEkle))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBoxUrunKategoriEkle, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonUrunKaydet, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldUrunAdiEkle, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxUrunKategoriEkle))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldUrunAlisEkle, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldUrunSatisEkle, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldUrunStokEkle, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldUrunAciklamaEkle, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonUrunKaydet, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13))
        );

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Ürün Listesi"));

        jTableUrunListesi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Adı", "Kategori", "Alış Fiyatı(TL)", "Satış Fiyatı(TL)", "Stok", "Açıklama"
            }
        ));
        jTableUrunListesi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableUrunListesiMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTableUrunListesi);

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Satır İşlemleri"));

        jButtonUrunListesiSil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/delete.png"))); // NOI18N
        jButtonUrunListesiSil.setText("SİL");
        jButtonUrunListesiSil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUrunListesiSilActionPerformed(evt);
            }
        });

        jButtonUrunListesiDuzenle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/duzen.png"))); // NOI18N
        jButtonUrunListesiDuzenle.setText("DÜZENLE");
        jButtonUrunListesiDuzenle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUrunListesiDuzenleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonUrunListesiDuzenle, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
                    .addComponent(jButtonUrunListesiSil, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(36, Short.MAX_VALUE)
                .addComponent(jButtonUrunListesiDuzenle, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonUrunListesiSil, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 890, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder("Ürün Düzenle"));

        jLabel23.setText("Ürün Adı");

        jLabel24.setText("Kategori");

        jLabel41.setText("Alış Fiyatı");

        jLabel42.setText("Satış Fiyatı");

        jLabel46.setText("Stok");

        jLabel47.setText("Açıklama");

        jComboBoxUrunKategoriDuzenle.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxUrunKategoriDuzenle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxUrunKategoriDuzenleActionPerformed(evt);
            }
        });

        jButtonUrunDuzenle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/tick.png"))); // NOI18N
        jButtonUrunDuzenle.setText("DÜZENLE");
        jButtonUrunDuzenle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUrunDuzenleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldUrunAciklamaDuzenle))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldUrunStokDuzenle))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldUrunSatisDuzenle))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldUrunAlisDuzenle))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldUrunAdiDuzenle))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBoxUrunKategoriDuzenle, 0, 358, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonUrunDuzenle, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldUrunAdiDuzenle, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxUrunKategoriDuzenle))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel41, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldUrunAlisDuzenle, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldUrunSatisDuzenle, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel46, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldUrunStokDuzenle, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldUrunAciklamaDuzenle, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonUrunDuzenle, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 1061, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("ÜRÜN YÖNETİMİ", jPanel4);

        jLabel27.setText("Eski Şifre");

        jLabel28.setText("Yeni Şifre");

        jLabel29.setText("Yeni Şifre Tekrar");

        jButtonKullaniciSifreDegistir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/CHANGE.png"))); // NOI18N
        jButtonKullaniciSifreDegistir.setText("DEĞİŞTİR");
        jButtonKullaniciSifreDegistir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonKullaniciSifreDegistirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldKullaniciEskiSifre, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                    .addComponent(jTextFieldKullaniciYeniSifreTekrar)
                    .addComponent(jTextFieldKullaniciYeniSifre))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonKullaniciSifreDegistir, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldKullaniciEskiSifre, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldKullaniciYeniSifre, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldKullaniciYeniSifreTekrar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButtonKullaniciSifreDegistir, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(51, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(794, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(308, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("KULLANICI AYARLARI", jPanel6);

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder("Yeni Kategori Ekle"));

        jLabel21.setText("Kategori Adı");

        jLabel22.setText("Açıklama");

        jButtonKategoriKaydet.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/tick.png"))); // NOI18N
        jButtonKategoriKaydet.setText("KAYDET");
        jButtonKategoriKaydet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonKategoriKaydetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonKategoriKaydet))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldKategoriAciklamaEkle, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
                            .addComponent(jTextFieldKategoriAdiEkle))))
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldKategoriAdiEkle, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addGap(16, 16, 16)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldKategoriAciklamaEkle, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonKategoriKaydet, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder("Kategori Listesi"));

        jTableKategoriListe.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Sıra", "Kategori Adı", "Kategori Açıklaması"
            }
        ));
        jTableKategoriListe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableKategoriListeMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTableKategoriListe);

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder("Satır İşlemleri"));

        jButtonKategoriListeDuzenle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/duzen.png"))); // NOI18N
        jButtonKategoriListeDuzenle.setText("DÜZENLE");
        jButtonKategoriListeDuzenle.setPreferredSize(new java.awt.Dimension(120, 40));
        jButtonKategoriListeDuzenle.setVerifyInputWhenFocusTarget(false);
        jButtonKategoriListeDuzenle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonKategoriListeDuzenleActionPerformed(evt);
            }
        });

        jButtonKategoriListeSil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/delete.png"))); // NOI18N
        jButtonKategoriListeSil.setText("SİL");
        jButtonKategoriListeSil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonKategoriListeSilActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonKategoriListeSil, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                    .addComponent(jButtonKategoriListeDuzenle, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(jButtonKategoriListeDuzenle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(jButtonKategoriListeSil, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(64, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(jScrollPane3)
                .addGap(18, 18, 18)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        jPanel19.setBorder(javax.swing.BorderFactory.createTitledBorder("Kategori Düzenle"));

        jLabel25.setText("Kategori Adı");

        jLabel26.setText("Açıklama");

        jButtonKategoriDuzenle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/tick.png"))); // NOI18N
        jButtonKategoriDuzenle.setText("DÜZENLE");
        jButtonKategoriDuzenle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonKategoriDuzenleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonKategoriDuzenle))
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldKategoriAdiDuzenle, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                            .addComponent(jTextFieldKategoriAciklamaDuzenle))))
                .addContainerGap())
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldKategoriAdiDuzenle, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldKategoriAciklamaDuzenle, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonKategoriDuzenle, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27))
        );

        jTabbedPane1.addTab("KATEGORİ YÖNETİMİ", jPanel5);

        jButtonCikis.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/if_Cancel_1493282 (1).png"))); // NOI18N
        jButtonCikis.setText("ÇIKIŞ");
        jButtonCikis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCikisActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1078, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonCikis))
                .addGap(22, 22, 22))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jButtonCikis)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 564, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jSpinnerRaporGunBitisStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerRaporGunBitisStateChanged
        spinnerTarihUyari();
        raporTabloGuncelle();
    }//GEN-LAST:event_jSpinnerRaporGunBitisStateChanged

    private void jSpinnerRaporAyBitisStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerRaporAyBitisStateChanged
        spinnerTarihUyari();
        spinnerDuzenleBitis();
        raporTabloGuncelle();
    }//GEN-LAST:event_jSpinnerRaporAyBitisStateChanged

    private void jSpinnerRaporYilBitisStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerRaporYilBitisStateChanged
        spinnerTarihUyari();
        spinnerDuzenleBitis();
        raporTabloGuncelle();
    }//GEN-LAST:event_jSpinnerRaporYilBitisStateChanged

    private void jSpinnerRaporGunBaslangicStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerRaporGunBaslangicStateChanged
        spinnerTarihUyari();
        raporTabloGuncelle();

    }//GEN-LAST:event_jSpinnerRaporGunBaslangicStateChanged

    private void jSpinnerRaporAyBaslangicStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerRaporAyBaslangicStateChanged
        spinnerTarihUyari();
        spinnerDuzenleBaslangic();
        raporTabloGuncelle();
    }//GEN-LAST:event_jSpinnerRaporAyBaslangicStateChanged

    private void jSpinnerRaporYilBaslangicStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerRaporYilBaslangicStateChanged
        spinnerTarihUyari();
        spinnerDuzenleBaslangic();
        raporTabloGuncelle();
    }//GEN-LAST:event_jSpinnerRaporYilBaslangicStateChanged

    private void jTextFieldRaporAramaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldRaporAramaKeyReleased
        spinnerTarihUyari();
        raporTabloGuncelle();
    }//GEN-LAST:event_jTextFieldRaporAramaKeyReleased

    private void jComboBoxUrunKategoriEkleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxUrunKategoriEkleActionPerformed

    }//GEN-LAST:event_jComboBoxUrunKategoriEkleActionPerformed

    private void jButtonUrunKaydetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUrunKaydetActionPerformed

        ValidationKontrol vk = new ValidationKontrol();
        String urAdi = jTextFieldUrunAdiEkle.getText();
        String urKatID = "" + jComboBoxUrunKategoriEkle.getSelectedItem();
        String urAlis = jTextFieldUrunAlisEkle.getText();
        String urSatis = jTextFieldUrunSatisEkle.getText();
        String urStok = jTextFieldUrunStokEkle.getText();
        String urAciklama = jTextFieldUrunAciklamaEkle.getText();
        boolean kontrol = true;
        String hataMesaji = "";
        if (vk.isBosGecilemezKontrol(urAdi)) {
            hataMesaji = "Ürün adı boş bırakılamaz !\n";
            //JOptionPane.showMessageDialog(this, "Ürün adı boş bırakılamaz !");
            kontrol = false;
        }
        System.out.println(vk.isNumericKontrol("" + urAlis));
        if (!vk.isNumericKontrol("" + urAlis) || vk.isBosGecilemezKontrol(urAlis)) {
            hataMesaji += "Ürün alış fiyatı sayısal ve pozitif olmak zorundadır !\n";
            //JOptionPane.showMessageDialog(this, "Ürün alış fiyatı sayısal ve pozitif olmak zorundadır !");
            kontrol = false;
        }
        if (!vk.isNumericKontrol("" + urSatis) || vk.isBosGecilemezKontrol(urSatis)) {
            hataMesaji += "Ürün satış fiyatı sayısal ve pozitif olmak zorundadır !\n";
            //JOptionPane.showMessageDialog(this, "Ürün satış fiyatı sayısal ve pozitif olmak zorundadır !");
            kontrol = false;
        }

        if (!vk.isNumericKontrol("" + urStok) || vk.isBosGecilemezKontrol(urStok)) {
            hataMesaji += "Ürün stok değeri sayısal ve pozitif olmak zorundadır !\n";
            //JOptionPane.showMessageDialog(this, "Ürün stok değeri sayısal ve pozitif olmak zorundadır");
            kontrol = false;
        }
        if (jComboBoxUrunKategoriEkle.getSelectedIndex() == 0) {
            hataMesaji += "Lütfen Kategori Seçiniz !";
            //JOptionPane.showMessageDialog(this, "Lütfen Kategori Seçiniz");
            kontrol = false;
        }
        boolean sonKontrol = false;
        if (kontrol) {
            sonKontrol = true;
            if (Double.valueOf(urAlis) > Double.valueOf(urSatis)) {
                int dialogResult = JOptionPane.showConfirmDialog(this, "SATIŞ FİYATI ALIŞ FİYATINDAN DÜŞÜK, ONAYLIYOR MUSUN ? ","Onay Ekranı",JOptionPane.YES_NO_OPTION);

                if (dialogResult == JOptionPane.NO_OPTION || dialogResult == JOptionPane.CLOSED_OPTION ) {
                    sonKontrol = false;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, hataMesaji);
            //hataları burda yazdır
        }
        if (sonKontrol) {
            DB db = new DB();
            try {
                String query = "insert into urun values ( null,  '" + urAdi + "' ,(select katID from kategori where katAdi='" + urKatID + "' ),  " + urAlis + ", " + urSatis + ", " + urStok + ", '" + urAciklama + "') ";
                int ekle = db.baglan().executeUpdate(query);
                if (ekle > 0) {
                    JOptionPane.showMessageDialog(this, "Ekleme işlemi başarılı");
                    urunDataGetir();

                }
            } catch (HeadlessException | SQLException e) {
                //yazma işlemi sırasında bir hata oluştu
                System.err.println("Yazma hatası : " + e);
            } finally {
                db.kapat();
            }

            jTextFieldUrunAdiEkle.setText("");
            jTextFieldUrunAlisEkle.setText("");
            jTextFieldUrunSatisEkle.setText("");
            jTextFieldUrunStokEkle.setText("");
            jTextFieldUrunAciklamaEkle.setText("");
            jComboBoxUrunKategoriEkle.setSelectedIndex(0);

        }
    }//GEN-LAST:event_jButtonUrunKaydetActionPerformed

    private void jTableUrunListesiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableUrunListesiMouseClicked

        //urunSecim = (String) jTableUrunListesi.getValueAt(jTableUrunListesi.getSelectedRow(), 0);
        jButtonUrunListesiDuzenle.setEnabled(true);
        jButtonUrunListesiSil.setEnabled(true);
        urunSecim = jTableUrunListesi.getSelectedRow();
        System.out.println(urn.get(urunSecim).getUrID());
        urunSecim = Integer.valueOf(urn.get(urunSecim).getUrID());

        System.out.println("Seçim : " + urunSecim);

    }//GEN-LAST:event_jTableUrunListesiMouseClicked

    private void jButtonUrunListesiSilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUrunListesiSilActionPerformed

        if (urunSecim == 0) {
            JOptionPane.showMessageDialog(this, "Lütfen Silinecek Ürünü Seçiniz");
        } else {

            DB db = new DB();
            try {
                String query = "delete from urun where urID = '" + urunSecim + "' ";
                //JOptionPane.showMessageDialog(this, urunSecim);
                int sonuc = db.baglan().executeUpdate(query);
                JOptionPane.showMessageDialog(this, "Silme İşlemi Başarılı ");
                if (sonuc > 0) {
                    urunDataGetir();
                    urunSecim = 0;
                }
            } catch (SQLException e) {
            } catch (Exception ex) {
                Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                db.kapat();
            }
            jButtonUrunListesiDuzenle.setEnabled(false);
            jButtonUrunListesiSil.setEnabled(false);
        }
    }//GEN-LAST:event_jButtonUrunListesiSilActionPerformed

    private void jButtonUrunListesiDuzenleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUrunListesiDuzenleActionPerformed

        setEnabled(true, jPanel14.getComponents());
        jButtonUrunDuzenle.setEnabled(true);
        if (urunSecim == 0) {
            JOptionPane.showMessageDialog(this, "Lütfen Düzenlenecek Ürünü Seçiniz");
            System.out.println("Buraya geldim");

        } else {
            try {
                jTextFieldUrunAdiDuzenle.setText((String) jTableUrunListesi.getValueAt(jTableUrunListesi.getSelectedRow(), 0));
                jComboBoxUrunKategoriDuzenle.setSelectedItem(jTableUrunListesi.getValueAt(jTableUrunListesi.getSelectedRow(), 1));
                jTextFieldUrunAlisDuzenle.setText((String) jTableUrunListesi.getValueAt(jTableUrunListesi.getSelectedRow(), 2));
                jTextFieldUrunSatisDuzenle.setText((String) jTableUrunListesi.getValueAt(jTableUrunListesi.getSelectedRow(), 3));
                jTextFieldUrunStokDuzenle.setText((String) jTableUrunListesi.getValueAt(jTableUrunListesi.getSelectedRow(), 4));
                jTextFieldUrunAciklamaDuzenle.setText((String) jTableUrunListesi.getValueAt(jTableUrunListesi.getSelectedRow(), 5));

                System.out.println("Buraya geldim");

            } catch (Exception ex) {
                System.err.println("Seçimi yazdıramadım ");

            }

        }
    }//GEN-LAST:event_jButtonUrunListesiDuzenleActionPerformed

    private void jTableMusteriBilgiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableMusteriBilgiMouseClicked
        jButtonMusteriListeDuzenle.setEnabled(true);
        jButtonMusteriListeSil.setEnabled(true);
        id = "" + jTableMusteriBilgi.getValueAt(jTableMusteriBilgi.getSelectedRow(), 0);
    }//GEN-LAST:event_jTableMusteriBilgiMouseClicked

    private void jButtonMusteriListeDuzenleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMusteriListeDuzenleActionPerformed
        setEnabled(true, jPanel11.getComponents());
        jPanel11.setEnabled(true);
        jButtonMusteriDuzenle.setEnabled(true);
        pms.clear();
        if (id.equals("")) {
            JOptionPane.showMessageDialog(this, "Lütfen değiştirilecek bir değer Seçiniz");
        } else {
            try {
                String query2 = "select *from musteri where musID ='" + id + "'";
                ResultSet rs = db.baglan().executeQuery(query2);
                while (rs.next()) {
                    Musteri ms = new Musteri();
                    ms.setMusID(rs.getString("" + MusteriEnum.musID));
                    ms.setMusAdi(rs.getString("" + MusteriEnum.musAdi));
                    ms.setMusSoyadi(rs.getString("" + MusteriEnum.musSoyadi));
                    ms.setMusTelefon(rs.getString("" + MusteriEnum.musTelefon));
                    ms.setMusAdres(rs.getString("" + MusteriEnum.musAdres));
                    pms.add(ms);
                }
            } catch (Exception e) {
                System.err.println("Değer Çekme Hatası:" + e);
            } finally {
                db.kapat();
            }
            
            for (Musteri md : pms) {
                String dizi[] = new String[]{md.getMusID(), md.getMusAdi(), md.getMusSoyadi(), md.getMusTelefon(), md.getMusAdres()};
                jTextFieldMusteriAdiDuzenle.setText(dizi[1]);
                jTextFieldMusteriSoyadiDuzenle.setText(dizi[2]);
                jTextFieldMusteriTelefonDuzenle.setText(dizi[3]);
                jTextFieldMusteriAdresDuzenle.setText(dizi[4]);
            }
        }
    }//GEN-LAST:event_jButtonMusteriListeDuzenleActionPerformed

    private void jButtonMusteriListeSilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMusteriListeSilActionPerformed
        setEnabled(false, jPanel11.getComponents());
        jButtonMusteriListeDuzenle.setEnabled(false);
        jButtonMusteriListeSil.setEnabled(false);
        if (id.equals("")) {
            JOptionPane.showMessageDialog(this, "Lütfen silinecek bir değer seçiniz...");
        } else {
            try {
                String query = "delete from musteri where musID ='" + id + "'";
                int deger = db.baglan().executeUpdate(query);
                if (deger > 0) {
                    musteriDataGetir();
                    //id="";
                }
            } catch (Exception e) {
            } finally {
                db.kapat();
            }
        }

    }//GEN-LAST:event_jButtonMusteriListeSilActionPerformed

    private void jButtonMusteriDuzenleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMusteriDuzenleActionPerformed
        ValidationKontrol vk = new ValidationKontrol();

        String dAdi = jTextFieldMusteriAdiDuzenle.getText();
        String dSoyadi = jTextFieldMusteriSoyadiDuzenle.getText();
        boolean boslukkont1 = vk.isBoslukkontrol(dAdi);
        boolean boslukkont2 = vk.isBoslukkontrol(dSoyadi);
        if (boslukkont1 == false && boslukkont2 == false) {
            boolean stringkont1 = vk.isStringKontrol(dAdi);
            boolean stringkont2 = vk.isStringKontrol(dSoyadi);
            if (stringkont1 == true && stringkont2 == true) {
                String dTelefon = jTextFieldMusteriTelefonDuzenle.getText();
                boolean telkontrol = false;
                boolean telbosluk = vk.isBosGecilemezKontrol(dTelefon);
                if (!telbosluk) {
                    telkontrol = vk.isTelKontrol(dTelefon);
                }
                if (telkontrol) {
                    String dAdres = jTextFieldMusteriAdresDuzenle.getText();
                    boolean adreskont = vk.isOzelHarfKontrol(dAdres);
                    if (adreskont == false) {
                        try {
                            String query = "update musteri set musAdi='" + dAdi + "',musSoyadi ='" + dSoyadi + "',musTelefon='" + dTelefon + "',musAdres='" + dAdres + "' where musID =" + id + ";";
                            int deger = db.baglan().executeUpdate(query);
                            if (deger > 0) {
                                musteriDataGetir();
                                JOptionPane.showMessageDialog(this, "database değiştirildi.");
                                setEnabled(false, jPanel11.getComponents());
                                setEnabled(false, jPanel8.getComponents());
                                jTextFieldMusteriAdiDuzenle.setText("");
                                jTextFieldMusteriSoyadiDuzenle.setText("");
                                jTextFieldMusteriTelefonDuzenle.setText("");
                                jTextFieldMusteriAdresDuzenle.setText("");
                            }
                        } catch (Exception e) {
                            System.err.println("database değişme hatası" + e);
                        } finally {
                            db.kapat();
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Adres Özel Karakter İçeremez ve Boşluk Bırakılamaz");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Telefon Numarası 11 Haneli ve Başında 0 olmalıdır");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Müsteri Adı ve Soyadı Hatalıdır");
            }
        } else {
            JOptionPane.showMessageDialog(this, " Müsteri Adı ve Açıklamayı Boşluk Bırakamazsınız");
        }


    }//GEN-LAST:event_jButtonMusteriDuzenleActionPerformed

    private void jButtonMusteriEkleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMusteriEkleActionPerformed
        ValidationKontrol vk = new ValidationKontrol();
        Musteri ms = new Musteri();
        ms.setMusAdi(jTextFieldMusteriAdiEkle.getText());
        ms.setMusSoyadi(jTextMusteriSoyadiEkle.getText());
        String dTelefon = jTextFieldMusteriTelefonEkle.getText();
        boolean boslukkont1 = vk.isBoslukkontrol(ms.getMusAdi());
        boolean boslukkont2 = vk.isBoslukkontrol(ms.getMusSoyadi());
        if (boslukkont1 == false && boslukkont2 == false) {
            boolean stringkont1 = vk.isStringKontrol(ms.getMusAdi());
            boolean stringkont2 = vk.isStringKontrol(ms.getMusSoyadi());
            if (stringkont1 == true && stringkont2 == true) {
                ms.setMusTelefon(jTextFieldMusteriTelefonEkle.getText());
                boolean telkontrol = false;
                boolean telbosluk = vk.isBosGecilemezKontrol(dTelefon);
                if (!telbosluk) {
                    telkontrol = vk.isTelKontrol(dTelefon);
                }
                if (telkontrol == true) {
                    ms.setMusAdres(jTextFieldMusteriAdresEkle.getText());
                    boolean adreskont = vk.isOzelHarfKontrol(ms.getMusAdres());
                    if (adreskont == false) {
                        try {
                            String query = "insert into musteri values(null,'" + ms.getMusAdi() + "','" + ms.getMusSoyadi() + "','" + ms.getMusTelefon() + "','" + ms.getMusAdres() + "')";
                            int deger = db.baglan().executeUpdate(query);
                            if (deger > 0) {
                                JOptionPane.showMessageDialog(this, "Ekleme İşlemi Başarılı");
                                musteriDataGetir();
                                jTextFieldMusteriAdiEkle.setText("");
                                jTextMusteriSoyadiEkle.setText("");
                                jTextFieldMusteriTelefonEkle.setText("");
                                jTextFieldMusteriAdresEkle.setText("");
                            }
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(this, e);
                        } finally {
                            db.kapat();
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Adres Özel Karakter İçeremez ve Boşluk Bırakılamaz");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Telefon Numarası 11 Haneli ve Başında 0 olmalıdır");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Müsteri Adı ve Soyadı Hatalıdır");
            }
        } else {
            JOptionPane.showMessageDialog(this, " Müsteri Adı ve Açıklamayı Boşluk Bırakamazsınız");
        }

    }//GEN-LAST:event_jButtonMusteriEkleActionPerformed

    private void jButtonKategoriKaydetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonKategoriKaydetActionPerformed
        ValidationKontrol vk = new ValidationKontrol();
        String urunadi = jTextFieldKategoriAdiEkle.getText();
        String urunAcikla = jTextFieldKategoriAciklamaEkle.getText();
        boolean boslukkont1 = vk.isBoslukkontrol(urunadi);
        boolean boslukkont2 = vk.isBoslukkontrol(urunAcikla);
        if (boslukkont1 == false && boslukkont2 == false) {
            boolean stringkont1 = vk.isStringKontrol(urunadi);
            boolean stringkont2 = vk.isStringKontrol(urunAcikla);
            if (stringkont1 == true && stringkont2 == true) {
                try {
                    String query = "insert into kategori values(null,'" + urunadi + "','" + urunAcikla + "')";
                    int deger = db.baglan().executeUpdate(query);
                    if (deger > 0) {
                        JOptionPane.showMessageDialog(this, "Kategori Değeri Atanmıştır.");
                        jTableKategoriListe.setModel(tvc.datavericek());
                        jTextFieldKategoriAdiEkle.setText("");
                        jTextFieldKategoriAciklamaEkle.setText("");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Değer Atanırken bir sorun ile karşılaşıldı");
                } finally {
                    db.kapat();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Lütfen Sadece String İfadeler Giriniz");
            }
        } else {
            JOptionPane.showMessageDialog(this, " Kategori Adı ve Açıklamayı Boşluk Bırakamazsınız");
        }


    }//GEN-LAST:event_jButtonKategoriKaydetActionPerformed

    private void jTableKategoriListeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableKategoriListeMouseClicked
        jButtonKategoriListeDuzenle.setEnabled(true);
        jButtonKategoriListeSil.setEnabled(true);

        id = "" + jTableKategoriListe.getValueAt(jTableKategoriListe.getSelectedRow(), 0);
    }//GEN-LAST:event_jTableKategoriListeMouseClicked

    private void jButtonKategoriListeDuzenleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonKategoriListeDuzenleActionPerformed
        setEnabled(true, jPanel19.getComponents());
        ls.clear();
        if (id.equals("")) {
            JOptionPane.showMessageDialog(this, "Lütfen değiştirilecek bir değer Seçiniz");
        } else {
            try {
                String query1 = "select *from kategori where katID ='" + id + "'";
                ResultSet rs = db.baglan().executeQuery(query1);
                while (rs.next()) {
                    Kategori kg = new Kategori();
                    kg.setKatID(rs.getString("" + KategoriEnum.katID));
                    kg.setKatAdi(rs.getString("" + KategoriEnum.katAdi));
                    kg.setKatAciklama(rs.getString("" + KategoriEnum.katAciklama));
                    ls.add(kg);
                }
            } catch (Exception e) {
                System.err.println("Değer Çekme Hatası:" + e);
            } finally {
                db.kapat();
            }
            
            for (Kategori l : ls) {
                String dizi[] = new String[]{l.getKatID(), l.getKatAdi(), l.getKatAciklama()};
                jTextFieldKategoriAdiDuzenle.setText(dizi[1]);
                jTextFieldKategoriAciklamaDuzenle.setText(dizi[2]);
            }
        }
    }//GEN-LAST:event_jButtonKategoriListeDuzenleActionPerformed

    private void jButtonKategoriListeSilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonKategoriListeSilActionPerformed

        if (id.equals("")) {
            JOptionPane.showMessageDialog(this, "Lütfen silinecek bir değer seçiniz...");
        } else {
            try {
                String query = "delete from kategori where katID ='" + id + "'";
                int deger = db.baglan().executeUpdate(query);
                if (deger > 0) {
                    jTableKategoriListe.setModel(tvc.datavericek());
                    //id="";
                }
            } catch (Exception e) {
            } finally {
                db.kapat();
            }
            jButtonKategoriListeDuzenle.setEnabled(false);
            jButtonKategoriListeSil.setEnabled(false);
            setEnabled(false, jPanel19.getComponents());
        }
    }//GEN-LAST:event_jButtonKategoriListeSilActionPerformed

    private void jButtonKategoriDuzenleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonKategoriDuzenleActionPerformed
        ValidationKontrol vk = new ValidationKontrol();
        String urunadi = jTextFieldKategoriAdiDuzenle.getText();
        String urunAcikla = jTextFieldKategoriAciklamaDuzenle.getText();
        boolean boslukkont1 = vk.isBoslukkontrol(urunadi);
        boolean boslukkont2 = vk.isBoslukkontrol(urunAcikla);
        if (boslukkont1 == false && boslukkont2 == false) {
            boolean stringkont1 = vk.isStringKontrol(urunadi);
            boolean stringkont2 = vk.isStringKontrol(urunAcikla);
            if (stringkont1 == true && stringkont2 == true) {
                try {
                    String query = "update kategori set katAdi='" + urunadi + "',katAciklama ='" + urunAcikla + "' where katID =" + id + ";";
                    int deger = db.baglan().executeUpdate(query);
                    if (deger > 0) {
                        jTableKategoriListe.setModel(tvc.datavericek());
                        JOptionPane.showMessageDialog(this, "database değiştirildi.");
                        jTextFieldKategoriAdiDuzenle.setText("");
                        jTextFieldKategoriAciklamaDuzenle.setText("");
                    }
                } catch (Exception e) {
                    System.err.println("database değişme hatası" + e);
                } finally {
                    db.kapat();
                }
                jButtonKategoriListeDuzenle.setEnabled(false);
                jButtonKategoriListeSil.setEnabled(false);
                setEnabled(false, jPanel19.getComponents());
            } else {
                JOptionPane.showMessageDialog(this, "Lütfen Sadece String İfadeler Giriniz");
            }
        } else {
            JOptionPane.showMessageDialog(this, " Kategori Adı ve Açıklamayı Boşluk Bırakamazsınız");
        }
    }//GEN-LAST:event_jButtonKategoriDuzenleActionPerformed

    private void jButtonCikisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCikisActionPerformed
        int dialogResult = JOptionPane.showConfirmDialog(this, "EMİN MİSİN ? ","Onay Ekranı",JOptionPane.YES_NO_OPTION);

                if (dialogResult == JOptionPane.YES_OPTION) 
        dispose();
    }//GEN-LAST:event_jButtonCikisActionPerformed

    private void jTextFieldMusteriAdiEkleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldMusteriAdiEkleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldMusteriAdiEkleActionPerformed

    private void jComboBoxSatisKategoriİtemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxSatisKategoriİtemStateChanged
        //yeni kod
        if (jComboBoxSatisKategori.getSelectedIndex() < 1) {
            JOptionPane.showMessageDialog(this, "Lütfen kategori seçin.");
        } else {

            kategoriSecilen = "" + jComboBoxSatisKategori.getSelectedItem();

            for (Kategori kategori : satis.dataGetir(queryKategori)) {
                if (kategori.getKatAdi().equalsIgnoreCase(kategoriSecilen)) {
                    comboID = Integer.valueOf(kategori.getKatID());
                    String queryKateUrun = "select urID,urAdi,urSatis,urAciklama from urun where urun.urKatID=" + comboID + "";
                    ul.clear();
                    ul = satis.dataGetir(queryKateUrun, 1);
                    satis.ListeDoldur(ul, jTableSatisKategori);
                    jTableSatisKategori.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

                    break;
                }
            }

        }

    }//GEN-LAST:event_jComboBoxSatisKategoriİtemStateChanged

    private void jComboBoxSatisKategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxSatisKategoriActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxSatisKategoriActionPerformed

    private void jButtonSatisKategoriListeleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSatisKategoriListeleActionPerformed
        //yenikod

        if (jComboBoxSatisKategori.getSelectedIndex() > 0) {
            jPanelSatis1.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Lütfen kategori seçin");
        }

    }//GEN-LAST:event_jButtonSatisKategoriListeleActionPerformed

    private void jTableSatisKategoriMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableSatisKategoriMouseClicked
        jPanelSatis2.setVisible(true);
        ml = satis.dataGetirMusteri(musteriQuery);
        satis.comboDoldurMusteri(ml, jComboBoxSatisMusteriAdi);
        String urunAd = "" + jTableSatisKategori.getValueAt(jTableSatisKategori.getSelectedRow(), 0);
        uptadeUrunId = satis.idTespitUrun(ul, urunAd);
        jLabelSatisSecilenUrun.setText(urunAd);
    }//GEN-LAST:event_jTableSatisKategoriMouseClicked

    private void jComboBoxSatisMusteriAdiİtemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxSatisMusteriAdiİtemStateChanged

        secilenMusteri = "" + jComboBoxSatisMusteriAdi.getSelectedItem();
        uptadeMusteriId = satis.idTespitMusteri(ml, secilenMusteri);

    }//GEN-LAST:event_jComboBoxSatisMusteriAdiİtemStateChanged

    private void jComboBoxSatisMusteriAdiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxSatisMusteriAdiActionPerformed

    }//GEN-LAST:event_jComboBoxSatisMusteriAdiActionPerformed

    private void jButtonSatisTamamlaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSatisTamamlaActionPerformed
        if (jComboBoxSatisMusteriAdi.getSelectedIndex() < 1) {
            JOptionPane.showMessageDialog(this, "Lütfen müşteri adı giriniz");
        } else {

            int x[] = new int[2];
            try {
                int satisAdet = Integer.valueOf(jTextFieldSatisAdet.getText());
                if (satisAdet > 0) {
                    int musteriId = Integer.valueOf(uptadeMusteriId);
                    int urunId = Integer.valueOf(uptadeUrunId);

                    System.out.println(satisAdet + " " + musteriId + " " + urunId);
                    x = satis.insertIslem(urunId, musteriId, satisAdet);

                    if (x[0] == -1) {
                        JOptionPane.showMessageDialog(this, "Max Stok : " + x[1]);
                    } else if (x[0] == 1) {
                        jComboBoxSatisMusteriAdi.setSelectedIndex(0);
                        JOptionPane.showMessageDialog(this, "Satış başarılı");
                        urunDataGetir();
                    }

                } else {
                    JOptionPane.showMessageDialog(this, "Satış Adedi negati değer alamaz!");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "sayısal değer gir");

            }

        }

    }//GEN-LAST:event_jButtonSatisTamamlaActionPerformed

    private void jButtonKullaniciSifreDegistirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonKullaniciSifreDegistirActionPerformed
        String esifre = jTextFieldKullaniciEskiSifre.getText();
        String ysifre1 = jTextFieldKullaniciYeniSifre.getText();
        String ysifre2 = jTextFieldKullaniciYeniSifreTekrar.getText();
        DB db = new DB();
        if (!ysifre1.equals(ysifre2)) {
            JOptionPane.showMessageDialog(this, "Yeni şifre birbiri ile uyumuyor");
        } else {
            boolean kontrol = db.sifreDegistir(esifre, ysifre1);
            if (kontrol) {
                JOptionPane.showMessageDialog(this, "Şifreniz Değişti");
            } else {
                JOptionPane.showMessageDialog(this, "Eski Şİfreniz Hatalı");
            }
        }
    }//GEN-LAST:event_jButtonKullaniciSifreDegistirActionPerformed

    private void jComboBoxUrunKategoriDuzenleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxUrunKategoriDuzenleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxUrunKategoriDuzenleActionPerformed

    private void jButtonUrunDuzenleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUrunDuzenleActionPerformed

ValidationKontrol vk = new ValidationKontrol();
        String urAdi = jTextFieldUrunAdiDuzenle.getText();
        String urKatID = "" + jComboBoxUrunKategoriDuzenle.getSelectedItem();
        String urAlis = jTextFieldUrunAlisDuzenle.getText();
        String urSatis = jTextFieldUrunSatisDuzenle.getText();
        String urStok = jTextFieldUrunStokDuzenle.getText();
        String urAciklama = jTextFieldUrunAciklamaDuzenle.getText();
        boolean kontrol = true;
        String hataMesaji = "";
        if (vk.isBosGecilemezKontrol(urAdi)) {
            hataMesaji = "Ürün adı boş bırakılamaz !\n";
            //JOptionPane.showMessageDialog(this, "Ürün adı boş bırakılamaz !");
            kontrol = false;
        }
        System.out.println(vk.isNumericKontrol("" + urAlis));
        if (!vk.isNumericKontrol("" + urAlis) || vk.isBosGecilemezKontrol(urAlis)) {
            hataMesaji += "Ürün alış fiyatı sayısal ve pozitif olmak zorundadır !\n";
            //JOptionPane.showMessageDialog(this, "Ürün alış fiyatı sayısal ve pozitif olmak zorundadır !");
            kontrol = false;
        }
        if (!vk.isNumericKontrol("" + urSatis) || vk.isBosGecilemezKontrol(urSatis)) {
            hataMesaji += "Ürün satış fiyatı sayısal ve pozitif olmak zorundadır !\n";
            //JOptionPane.showMessageDialog(this, "Ürün satış fiyatı sayısal ve pozitif olmak zorundadır !");
            kontrol = false;
        }

        if (!vk.isNumericKontrol("" + urStok) || vk.isBosGecilemezKontrol(urStok)) {
            hataMesaji += "Ürün stok değeri sayısal ve pozitif olmak zorundadır !\n";
            //JOptionPane.showMessageDialog(this, "Ürün stok değeri sayısal ve pozitif olmak zorundadır");
            kontrol = false;
        }
        if (jComboBoxUrunKategoriDuzenle.getSelectedIndex() == 0) {
            hataMesaji += "Lütfen Kategori Seçiniz !";
            //JOptionPane.showMessageDialog(this, "Lütfen Kategori Seçiniz");
            kontrol = false;
        }
        boolean sonKontrol = false;
        if (kontrol) {
            sonKontrol = true;
            if (Double.valueOf(urAlis) > Double.valueOf(urSatis)) {
                int dialogResult = JOptionPane.showConfirmDialog(this, "SATIŞ FİYATI ALIŞ FİYATINDAN DÜŞÜK, ONAYLIYOR MUSUN ? ","Onay Ekranı",JOptionPane.YES_NO_OPTION);

                if (dialogResult == JOptionPane.NO_OPTION) {
                    JOptionPane.showMessageDialog(this, "Satış Fiyatı Alış Fiyatından Büyük Olmalıdır");
                    sonKontrol = false;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, hataMesaji);
            //hataları burda yazdır
        }
        if (sonKontrol) {
            DB db = new DB();
            try {
                String query = "update urun set urAdi ='" + urAdi + "', urKatID=(select katID from kategori where katAdi='" + urKatID + "' )"
                        + ",urAlis=" + urAlis + ",urSatis=" + urSatis
                        + ",urStok=" + urStok + ",urAciklama='" + urAciklama + "' where urID=" + urunSecim;
                int ekle = db.baglan().executeUpdate(query);
                if (ekle > 0) {
                    JOptionPane.showMessageDialog(this, "Düzenleme işlemi başarılı");
                    urunDataGetir();
                    setEnabled(false, jPanel14.getComponents());
                    jButtonUrunListesiDuzenle.setEnabled(false);
                    jButtonUrunListesiSil.setEnabled(false);

                }
            } catch (HeadlessException | SQLException e) {
                //yazma işlemi sırasında bir hata oluştu
                System.err.println("Yazma hatası : " + e);
            } finally {
                db.kapat();
            }

            jTextFieldUrunAdiDuzenle.setText("");
            jTextFieldUrunAlisDuzenle.setText("");
            jTextFieldUrunSatisDuzenle.setText("");
            jTextFieldUrunStokDuzenle.setText("");
            jTextFieldUrunAciklamaDuzenle.setText("");
            jComboBoxUrunKategoriDuzenle.setSelectedIndex(0);

        }

    }//GEN-LAST:event_jButtonUrunDuzenleActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new NewJFrame().setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButtonCikis;
    private javax.swing.JButton jButtonKategoriDuzenle;
    private javax.swing.JButton jButtonKategoriKaydet;
    private javax.swing.JButton jButtonKategoriListeDuzenle;
    private javax.swing.JButton jButtonKategoriListeSil;
    private javax.swing.JButton jButtonKullaniciSifreDegistir;
    private javax.swing.JButton jButtonMusteriDuzenle;
    private javax.swing.JButton jButtonMusteriEkle;
    private javax.swing.JButton jButtonMusteriListeDuzenle;
    private javax.swing.JButton jButtonMusteriListeSil;
    private javax.swing.JButton jButtonSatisKategoriListele;
    private javax.swing.JButton jButtonSatisTamamla;
    private javax.swing.JButton jButtonUrunDuzenle;
    private javax.swing.JButton jButtonUrunKaydet;
    private javax.swing.JButton jButtonUrunListesiDuzenle;
    private javax.swing.JButton jButtonUrunListesiSil;
    private javax.swing.JComboBox<String> jComboBoxSatisKategori;
    private javax.swing.JComboBox<String> jComboBoxSatisMusteriAdi;
    private javax.swing.JComboBox<String> jComboBoxUrunKategoriDuzenle;
    private javax.swing.JComboBox<String> jComboBoxUrunKategoriEkle;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelRaporKarZarar;
    private javax.swing.JLabel jLabelRaporUyari;
    private javax.swing.JLabel jLabelSatisSecilenUrun;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelSatis1;
    private javax.swing.JPanel jPanelSatis2;
    private javax.swing.JRadioButton jRadioButtonRaporKategori;
    private javax.swing.JRadioButton jRadioButtonRaporMusteri;
    private javax.swing.JRadioButton jRadioButtonRaporUrunadi;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSpinner jSpinnerRaporAyBaslangic;
    private javax.swing.JSpinner jSpinnerRaporAyBitis;
    private javax.swing.JSpinner jSpinnerRaporGunBaslangic;
    private javax.swing.JSpinner jSpinnerRaporGunBitis;
    private javax.swing.JSpinner jSpinnerRaporYilBaslangic;
    private javax.swing.JSpinner jSpinnerRaporYilBitis;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTableKategoriListe;
    private javax.swing.JTable jTableMusteriBilgi;
    private javax.swing.JTable jTableRaporSonuc;
    private javax.swing.JTable jTableSatisKategori;
    private javax.swing.JTable jTableUrunListesi;
    private javax.swing.JTextField jTextFieldKategoriAciklamaDuzenle;
    private javax.swing.JTextField jTextFieldKategoriAciklamaEkle;
    private javax.swing.JTextField jTextFieldKategoriAdiDuzenle;
    private javax.swing.JTextField jTextFieldKategoriAdiEkle;
    private javax.swing.JTextField jTextFieldKullaniciEskiSifre;
    private javax.swing.JTextField jTextFieldKullaniciYeniSifre;
    private javax.swing.JTextField jTextFieldKullaniciYeniSifreTekrar;
    private javax.swing.JTextField jTextFieldMusteriAdiDuzenle;
    private javax.swing.JTextField jTextFieldMusteriAdiEkle;
    private javax.swing.JTextField jTextFieldMusteriAdresDuzenle;
    private javax.swing.JTextField jTextFieldMusteriAdresEkle;
    private javax.swing.JTextField jTextFieldMusteriSoyadiDuzenle;
    private javax.swing.JTextField jTextFieldMusteriTelefonDuzenle;
    private javax.swing.JTextField jTextFieldMusteriTelefonEkle;
    private javax.swing.JTextField jTextFieldRaporArama;
    private javax.swing.JTextField jTextFieldSatisAdet;
    private javax.swing.JTextField jTextFieldUrunAciklamaDuzenle;
    private javax.swing.JTextField jTextFieldUrunAciklamaEkle;
    private javax.swing.JTextField jTextFieldUrunAdiDuzenle;
    private javax.swing.JTextField jTextFieldUrunAdiEkle;
    private javax.swing.JTextField jTextFieldUrunAlisDuzenle;
    private javax.swing.JTextField jTextFieldUrunAlisEkle;
    private javax.swing.JTextField jTextFieldUrunSatisDuzenle;
    private javax.swing.JTextField jTextFieldUrunSatisEkle;
    private javax.swing.JTextField jTextFieldUrunStokDuzenle;
    private javax.swing.JTextField jTextFieldUrunStokEkle;
    private javax.swing.JTextField jTextMusteriSoyadiEkle;
    // End of variables declaration//GEN-END:variables
}
//Emre methodlar


    //Emre methodlar sonu
