

package Siniflar;

import Enumlar.KategoriEnum;
import Propertiler.Kategori;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;


public class TabloyaVeriCek {
    
    ArrayList<Kategori> ls=new ArrayList<>();
   
     
    
    public DefaultTableModel datavericek(){
         ls.clear();
         DB db=new DB();
         try {
            String query="select *from kategori";
            ResultSet rs=db.baglan().executeQuery(query);
            while(rs.next()){
                Kategori kg=new Kategori();
                kg.setKatID(rs.getString(""+KategoriEnum.katID));
                kg.setKatAdi(rs.getString(""+KategoriEnum.katAdi));
                kg.setKatAciklama(rs.getString(""+KategoriEnum.katAciklama));
                ls.add(kg);
            }
        } catch (Exception e) {
             System.err.println("Değer Çekme Hatası:" + e);
        }
         
         
         DefaultTableModel dtm=new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };
         dtm.addColumn("Sıra");
         dtm.addColumn("Kategori Adı");
         dtm.addColumn("Kategori Açıklaması");
         for (Kategori l : ls) {
             dtm.addRow(new String[]{l.getKatID(),l.getKatAdi(),l.getKatAciklama()});
        }
         return dtm;
}
}
