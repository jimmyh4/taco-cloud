package tacos.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.Taco;

// @Slf4j autogenerates a Logger from Lombok

// Use @Controller to identify this as a controller and to mark it
// as candidate for component scanning (meaning Spring automatically creates
// an instance of this class as a bean for the Spring app context

// Apply @RequestMapping at class level to indicate that this controller
// handles requests

// Be specific with what kind of HTTP CRUD method is handled in the
// handler methods
@Slf4j
@Controller
@RequestMapping("/design")
public class DesignTacoController {

    @ModelAttribute("design")
    public Taco design(){
        return new Taco();
    }

    /* Model objects "ferry" data between the controller and to whatever view is responsible for rendering
     that data
     The model object being ferried around the design
     controller is a Taco object (the data in taco specifically)? */

    /* On design page, once ingredients are selected
    and taco is named and submitted (POST), user is redirected
    to "orders/current" view (via OrderController)
     */
    @PostMapping
    public String processDesign(Taco design){

        log.info("Processing design: " + design);

        // Redirects user's browsers to relative path
        return "redirect:/orders/current";
    }

    /* For get /design requests, show user design view. */
    @GetMapping
    public String showDesignForm (Model model)
    {
        // Hardcoding a list of ingredients for now, use a DB later
        List<Ingredient> ingredients = Arrays.asList(

                new Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
                new Ingredient("COTO", "Corn Tortilla", Type.WRAP),
                new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
                new Ingredient("CARN", "Carnitas", Type.PROTEIN),
                new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
                new Ingredient("LETC", "Lettuce", Type.VEGGIES),
                new Ingredient("CHED", "Cheddar", Type.CHEESE),
                new Ingredient("JACK", "Jack", Type.CHEESE),
                new Ingredient("SLSA", "Salsa", Type.SAUCE),
                new Ingredient("SRCR", "Sour Cream", Type.SAUCE)
        );


        Type[] types = Ingredient.Type.values();
        for (Type type : types){

            model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
        }

        model.addAttribute("design", new Taco());

        /* At this point, model has these kinds of attributes:
            * Key: (For each ingredient type) Ingredient.Type (lowercased),
             * Value: List of ingredients pertaining to that Ing. type
            * Key: "design", Value: Taco object
         */
        return "design";
    }

    private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type){

        /* Filter generates a list of ingredients, whose type
        matches the Type passed to the argument
         */
        return ingredients.stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
    }
}
