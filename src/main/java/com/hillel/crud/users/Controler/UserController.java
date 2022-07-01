package com.hillel.crud.users.Controler;

import com.hillel.crud.users.model.User;
import com.hillel.crud.users.model.repozitiry.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Optional;
@RequestMapping("/users")
@Controller
public class UserController {
    UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/add-user")
    public String userForm(User user) {
        return "add-user";
    }

    @PostMapping("/add")
    public String addUser(@Valid User user, BindingResult result) {

        if (result.hasErrors()) {
            return "add-user";
        }

        userRepository.save(user);
        return "redirect:/users";

    }

    @GetMapping
    public String getUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "index";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {

        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            userRepository.delete(user.get());
        }

        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") int id, Model model) {

        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new IllegalArgumentException("Invalid user id: " + id);
        }

        model.addAttribute("user", user.get());
        return "update-user";

    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") int id, @Valid User user,
                             BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            user.setId(id);
            return "update-user";
        }

        userRepository.save(user);
        return "redirect:/users";

    }
}
