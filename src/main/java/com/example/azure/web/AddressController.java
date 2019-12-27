package com.example.azure.web;

import com.example.azure.persistence.models.AddressModel;
import com.example.azure.persistence.models.UserModel;
import com.example.azure.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users/{userId}/addresses")
public class AddressController {

    @Autowired
    private UserService userService;

    @PostMapping
    public UserModel addAddress(
            @PathVariable final String userid,
            @RequestBody final AddressModel addressModel) {

        final UserModel user = userService.getUserForId(userid);
        user.getAddresses().add(addressModel);

        userService.save(user);

        return user;
    }
}
