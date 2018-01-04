package Siniflar;

import java.util.Calendar;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

public class SpinnerTarih {

    Calendar tarih = Calendar.getInstance();

    public SpinnerModel SpinnerYilAyarla() {

        int gecerliYil = tarih.get(Calendar.YEAR);
        SpinnerModel yilModel = new SpinnerNumberModel(gecerliYil, gecerliYil - 20, gecerliYil, 1);
        return yilModel;

    }

    public SpinnerModel SpinnerAyAyarla() {
        int gecerliAy = tarih.get(Calendar.MONTH)+1;
        SpinnerModel ayModel = new SpinnerNumberModel(gecerliAy, 1, 12, 1);
        return ayModel;
    }

    public SpinnerModel SpinnerGunAyarla(int gun, int ay, int yil) {
        int gecerliGun;
        if (gun == 0) {
            gecerliGun = tarih.get(Calendar.DATE);
        } else {
            gecerliGun = gun;
        }

        int maxAy = 31;

        switch (ay) {
            case 1:
                maxAy = 31;
                break;
            case 2:
                if (yil % 4 == 0) {
                    if (ay == 2) {
                        maxAy = 29;
                    }
                } else {
                    maxAy = 28;
                }
                break;
            case 3:
                maxAy = 31;
                break;
            case 4:
                maxAy = 30;
                break;
            case 5:
                maxAy = 31;
                break;
            case 6:
                maxAy = 30;
                break;
            case 7:
                maxAy = 31;
                break;
            case 8:
                maxAy = 31;
                break;
            case 9:
                maxAy = 30;
                break;
            case 10:
                maxAy = 31;
                break;
            case 11:
                maxAy = 30;
                break;
            case 12:
                maxAy = 31;
                break;
        }

        if (gun > maxAy) {
            gecerliGun = maxAy;
        }
        SpinnerModel gunModel = new SpinnerNumberModel(gecerliGun, 1, maxAy, 1);
        return gunModel;
    }

}
