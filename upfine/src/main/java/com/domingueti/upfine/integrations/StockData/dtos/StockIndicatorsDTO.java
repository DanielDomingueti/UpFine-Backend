package com.domingueti.upfine.integrations.StockData.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString
public class StockIndicatorsDTO {

    private String data;
    private String ticker;
    private String empresa;
    private String setor;
    private String subsetor;
    private String tipoDoAtivo;
    private String dataUltimaCotacao;
    private Double valorDaFirma;
    private Double valorDeMercado;
    private String ultimoBalancoProcessado;
    private String dy;
    private String vpa;
    private String lpa;
    private String roic;
    private String roe;
    private String liquidezCorrente;
    private String margemEbit;
    private String margemBruta;
    private String margemLiquida;
    private String evEbitda;
    private Double receitaLiquida12Meses;
    private String pl;
    private String pvp;
    private String pebit;

}
