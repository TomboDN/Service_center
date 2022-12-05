package mirea.db.service_center.controller;

import lombok.RequiredArgsConstructor;
import mirea.db.service_center.model.Service;
import mirea.db.service_center.service.ServiceService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ServiceController {
    private final SpecificationBuilder<>
    private final ServiceService serviceService;

    @GetMapping("/service")
    public List<Service> findAll() {
        return serviceService.findAll();
    }

    @GetMapping("/service")
    @ResponseBody
    public List<Service> findAllBySpecification(@RequestParam(value = "search") String search, @RequestParam(value = "page") int page, @RequestParam(value = "size") int size) {

        return serviceService.findAll(spec, PageRequest.of(page, size));
    }

    @GetMapping("/service/{id}")
    public Service findById(@PathVariable("id") int id) {
        return serviceService.findById(id);
    }

    @PostMapping("/service")
    public String createService(Service service) {
        serviceService.save(service);
        return "redirect:/index";
    }

    @PostMapping("/service/{id}")
    public String updateService(@PathVariable("id") int id, Service service) {
        service.setId(id);
        serviceService.save(service);
        return "redirect:/index";
    }

    @GetMapping("/service/delete/{id}")
    public String deleteService(@PathVariable("id") int id) {
        serviceService.deleteById(id);
        return "redirect:/index";
    }
}
