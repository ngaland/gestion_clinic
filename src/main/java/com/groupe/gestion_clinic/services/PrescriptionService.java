package com.groupe.gestion_clinic.services;

import com.groupe.gestion_clinic.dto.PrescriptionDto;
import com.groupe.gestion_clinic.dto.requestDto.PrescriptionRequestDto;

import java.util.List;

public interface PrescriptionService {

    PrescriptionDto createPrescription(PrescriptionRequestDto requestDto);

    PrescriptionDto getPrescriptionById(Integer id);

    PrescriptionDto updatePrescription(Integer id, PrescriptionRequestDto requestDto);

    void deletePrescription(Integer id);

    List<PrescriptionDto> getAllPrescriptions();

    List<PrescriptionDto> getPrescriptionsByRendezvousId(Integer rendezvousId);

    byte[] generatePrescriptionPdf(Integer prescriptionId);
}
