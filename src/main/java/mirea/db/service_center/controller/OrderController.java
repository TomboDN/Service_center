package mirea.db.service_center.controller;

import com.google.common.base.Joiner;
import lombok.RequiredArgsConstructor;
import mirea.db.service_center.model.Order;
import mirea.db.service_center.service.OrderService;
import mirea.db.service_center.specification.SearchOperation;
import mirea.db.service_center.specification.SpecificationBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/order")
    public List<Order> findAll() {
        return orderService.findAll();
    }

    @GetMapping("/order")
    @ResponseBody
    public List<Order> findAllBySpecification(@RequestParam(value = "search") String search, @RequestParam(value = "page") int page, @RequestParam(value = "size") int size) {
        SpecificationBuilder<Order> builder = new SpecificationBuilder<>();
        String operationSetExper = Joiner.on("|").join(SearchOperation.SIMPLE_OPERATION_SET);
        Pattern pattern = Pattern.compile("(\\w+?)(" + operationSetExper + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(
                    matcher.group(1),
                    matcher.group(2),
                    matcher.group(4),
                    matcher.group(3),
                    matcher.group(5));
        }

        Specification<Order> spec = builder.build();
        return orderService.findAll(spec, PageRequest.of(page, size));
    }

    @GetMapping("/order/{id}")
    public Order findById(@PathVariable("id") int id) {
        return orderService.findById(id);
    }

    @PostMapping("/order")
    public String createOrder(Order order) {
        orderService.save(order);
        return "redirect:/index";
    }

    @PostMapping("/order/{id}")
    public String updateOrder(@PathVariable("id") int id, Order order) {
        order.setId(id);
        orderService.save(order);
        return "redirect:/index";
    }

    @GetMapping("/order/delete/{id}")
    public String deleteOrder(@PathVariable("id") int id) {
        orderService.deleteById(id);
        return "redirect:/index";
    }

}