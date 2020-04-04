package tacos.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import tacos.dao.IngredientRepository;
import tacos.dao.TacoRepository;
import tacos.domain.Ingredient;
import tacos.domain.Order;
import tacos.domain.Taco;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DesignTacoController {

    private final IngredientRepository ingredientRepo;
    private final TacoRepository tacoRepo;

    @ModelAttribute(name = "taco")
    public Taco taco() {
        return new Taco();
    }

    @ModelAttribute(name = "order")
    public Order order() {
        return new Order();
    }

    @GetMapping
    public String showDesignForm(final Model model) {
        StreamSupport.stream(this.ingredientRepo.findAll().spliterator(), false)
                     .collect(Collectors.groupingBy(Ingredient::getType))
                     .forEach((key, value) -> model.addAttribute(key.toString().toLowerCase(), value));
        return "design";
    }

    @PostMapping
    public String processDesign(@Valid final Taco taco, final Errors errors, @ModelAttribute final Order order) {
        if (errors.hasErrors()) {
            return "redirect:/design";
        }

        log.info("Processing designed taco: " + taco);
        final Taco saved = this.tacoRepo.save(taco);
        order.addTaco(saved);

        return "redirect:/orders/current";
    }
}
