package com.ibrahimahmads.github.vbooth.service;

import com.ibrahimahmads.github.vbooth.dto.request.PaketRequest;
import com.ibrahimahmads.github.vbooth.entity.Paket;

import java.util.List;
import java.util.UUID;

public interface PaketService {
    Paket save(PaketRequest paket);
    Paket getById(UUID id);
    Paket edit(UUID id, PaketRequest payload);
    void delete(UUID id);
    List<Paket> getAll();
}
