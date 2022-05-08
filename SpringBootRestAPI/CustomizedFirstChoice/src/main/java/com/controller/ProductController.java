package com.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.constants.Utilities;
import com.service.FilesStorageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.entities.Product;
import com.service.ProductService;

@CrossOrigin("*")
@RequestMapping("/product")
@RestController
public class ProductController {
	@Autowired
	ProductService pservice;

	@Autowired
	FilesStorageService storageService;

	@Autowired
	Utilities utilities;
	
	@GetMapping("/getallproducts")
	public List<Product> getAllProducts() {
		return pservice.getAllProducts();
	}

	@PostMapping("/addproduct")
	public ResponseEntity<Integer> save(@RequestBody Product p) throws Exception {
		String loggedInUser = SecurityContextHolder.getContext().getAuthentication().getName();
		Integer id = pservice.vaddproduct(p.getCat().getC_id(), loggedInUser, p.getPname(),
				p.getPdesc(), p.getPsize(), p.getPbrand(), p.getPprice(), p.getPqty(), p.getImageUrl());
		return ResponseEntity.ok(id);
	}

	@GetMapping("/getbycid")
	public List<Product> getByCategoryId(@RequestParam("c_id") int c_id) {
		return pservice.getByCategoryId(c_id);
	}

	@PostMapping("/searchbykeyword")
	public List<Product> searchbykeyword(@RequestBody Product p) {
		return pservice.searchbykeyword(p.getPname(), p.getPbrand(), p.getPdesc());
	}
	@GetMapping("/raw")
	public List<Product> getAllRaw() {
		return pservice.getAllRaw().stream().filter(e -> e.getPqty()>0).collect(Collectors.toList());
	}


	@GetMapping("/search/{data}")
	public List<Product> searchRaw(@PathVariable("data") String data) {
		return pservice.getAllProducts().stream().filter((e) -> {
			return StringUtils.containsIgnoreCase(e.getPname(),data) || StringUtils.containsIgnoreCase(e.getPbrand(),data);
		}).collect(Collectors.toList());
	}

	@GetMapping("/stitched")
	public List<Product> getAllStitched() {
		return pservice.getAllStitched().stream().filter(e -> e.getPqty()>0).collect(Collectors.toList());
	}

	@GetMapping("/byVendor/IS")
	public List<Product> getByVid(){
		Integer id = utilities.getCurrentUserId();
		return pservice.getByVendor(id);
	}

	@GetMapping("/byVendor/OOS")
	public List<Product> viewOutOfStock()
	{
		Integer id = utilities.getCurrentUserId();
		return pservice.viewOutOfStock(id);
	}
}
