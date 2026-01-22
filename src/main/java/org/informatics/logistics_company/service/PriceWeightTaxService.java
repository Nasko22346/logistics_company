package org.informatics.logistics_company.service;

import org.informatics.logistics_company.dto.price_weight_tax.PriceWeightTaxForm;
import org.informatics.logistics_company.model.jpa.PriceWeightTax;
import org.informatics.logistics_company.repository.PriceWeightTaxRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceWeightTaxService {

    private final PriceWeightTaxRepository repo;

    public PriceWeightTaxService(PriceWeightTaxRepository repo) {
        this.repo = repo;
    }

    public List<PriceWeightTax> getAll() {
        return repo.findAll();
    }

    // прост searchbar: филтрираме в паметта (по maxWeight и tax)
    public List<PriceWeightTax> search(String q) {
        if (q == null || q.isBlank()) return repo.findAll();
        String needle = q.trim().toLowerCase();

        return repo.findAll().stream()
                .filter(x ->
                        (x.getId() != null && x.getId().toString().contains(needle)) ||
                                (x.getMaxWeightAmount() != null && x.getMaxWeightAmount().toString().toLowerCase().contains(needle)) ||
                                (x.getWeightTax() != null && x.getWeightTax().toPlainString().toLowerCase().contains(needle))
                )
                .toList();
    }

    public PriceWeightTax create(PriceWeightTaxForm form) {
        PriceWeightTax e = new PriceWeightTax();
        e.setMaxWeightAmount(form.getMaxWeightAmount());
        e.setWeightTax(form.getWeightTax());
        return repo.save(e);
    }

    public PriceWeightTax getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("PriceWeightTax with id " + id + " not found"));
    }

    public PriceWeightTax update(Long id, PriceWeightTaxForm form) {
        PriceWeightTax e = getById(id);
        e.setMaxWeightAmount(form.getMaxWeightAmount());
        e.setWeightTax(form.getWeightTax());
        return repo.save(e);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("PriceWeightTax with id " + id + " not found");
        }
        repo.deleteById(id);
    }
}
