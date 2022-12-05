package mirea.db.service_center.service;

import lombok.RequiredArgsConstructor;
import mirea.db.service_center.model.Service;
import mirea.db.service_center.repository.ServiceRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@org.springframework.stereotype.Service
@Transactional
@RequiredArgsConstructor
public class ServiceService {
    private final ServiceRepository serviceRepository;

    public List<Service> findAll() {
        return serviceRepository.findAll();
    }

    public List<Service> findAll(Specification<Service> specification, Pageable pageable) {
        return serviceRepository.findAll(specification, pageable).toList();
    }

    public Service findById(int id) {
        return serviceRepository.findById(id).orElse(null);
    }

    public void save(Service order) {
        serviceRepository.save(order);
    }

    public void deleteById(int id) {
        serviceRepository.deleteById(id);
    }
}
