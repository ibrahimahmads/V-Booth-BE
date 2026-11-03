package com.ibrahimahmads.github.vbooth.controller;

import com.ibrahimahmads.github.vbooth.dto.request.PaketRequest;
import com.ibrahimahmads.github.vbooth.entity.Paket;
import com.ibrahimahmads.github.vbooth.service.PaketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/paket")
@RequiredArgsConstructor
public class PaketController {
    private final PaketService paketService;

    @PostMapping(value = "/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Paket> addPaket(@RequestBody PaketRequest payload){
        return ResponseEntity.status(HttpStatus.CREATED).body(paketService.save(payload));
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<Paket>> getAllPaket(){
        return ResponseEntity.ok(paketService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Paket> getPaketById(@PathVariable UUID id){
        return ResponseEntity.ok(paketService.getById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deletePaket(@PathVariable UUID id){
        paketService.delete(id);
        return ResponseEntity.ok("Paket deleted successfully");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Paket> updatePaket(@PathVariable UUID id,PaketRequest payload){
        return ResponseEntity.ok(paketService.edit(id,payload));
    }
}
