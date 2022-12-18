package client;

import models.Nave;
import models.TipoNave;

import java.time.LocalDate;

public class CreadorNavesRandom {

    public CreadorNavesRandom() {
    }

    public Nave creadorNavesRandom() {
        Nave nave = new Nave();
        nave.setId(0);
        nave.setTipoNave(getTipo());
        nave.setSaltoHiperEspacio(getSaltoHiper());
        nave.setMisilesProtonicos((int) (10 + Math.random() * 10));
        nave.setFechaCreacion(LocalDate.now().toString());
        return nave;
    }

    private TipoNave getTipo() {
        int random = (int) (1 + Math.random() * 2);

        TipoNave tipoNave;
        if (random == 1) {
            tipoNave = TipoNave.T_FIGHTER;
        } else tipoNave = TipoNave.X_WIND;

        return tipoNave;
    }

    private Boolean getSaltoHiper() {
        int random = (int) (1 + Math.random() * 2);
        return random != 1;
    }
}
