package com.matc89.estacionaufba.interfaces;


import com.matc89.estacionaufba.db.vo.Ocorrencia;

/**
 * Created by tedri on 01/07/2017.
 */

public interface OnOcorrenciaInteractionListener {
    void mostrarOcorrencia(Ocorrencia ocorrencia);
    void editarOcorrencia(Ocorrencia ocorrencia);
    void deletarOcorrencia(Ocorrencia ocorrencia);
    void atualizarOcorrencia(Ocorrencia ocorrencia);
}
