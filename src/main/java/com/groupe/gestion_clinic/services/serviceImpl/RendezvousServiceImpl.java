package com.groupe.gestion_clinic.services.serviceImpl;

import com.groupe.gestion_clinic.repositories.RendezvousRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RendezvousServiceImpl {
    private final RendezvousRepository rendezvousRepository;
}
