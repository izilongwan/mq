package com.consumer.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.common.entity.R;

@RestController
@RequestMapping("c")
public class UserController {
    @GetMapping("")
    public R<R<Integer>> user() {
        R<Integer> v = new R<Integer>(1);
        return R.SUCCESS(v);
    }

    @GetMapping("{id}")
    public R<R<Integer>> id(@PathVariable Integer id) {
        R<Integer> v = new R<Integer>(id);
        return R.SUCCESS(v);
    }
}
