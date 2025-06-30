package com.groupe.gestion_clinic.dto;

import com.groupe.gestion_clinic.model.Rendezvous;
import lombok.*;

@Builder
@Getter @Setter
@NoArgsConstructor
//@AllArgsConstructor
public class RendezvousDto {

    public static RendezvousDto fromEntity(Rendezvous rendezvous) {
        return
                RendezvousDto.builder().build();
    }
    public static Rendezvous toDto(RendezvousDto rendezvousDto) {
        return
                Rendezvous
                        .builder()
                        .build();
    }

}
