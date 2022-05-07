package com.controller;

import java.util.List;
import java.util.stream.Collectors;

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

	@GetMapping("/viewbyvid")
	public List<Product> getByVid(@RequestParam("v_id")int v_id){
		return pservice.getByVendor(v_id);
	}

//	@GetMapping("/vaddproduct")
//	public int vaddproduct(@RequestParam("c_id")int c_id, @RequestParam("v_id") int v_id,
//			@RequestParam("pname") String pname, @RequestParam("pdesc") String pdesc,
//			@RequestParam("psize") String psize, @RequestParam("pbrand") String pbrand,
//			@RequestParam("pprice") float pprice, @RequestParam("pqty") int pqty, @RequestParam("image_url") String image_url) throws Exception {
//		return pservice.vaddproduct(c_id, "", pname, pdesc, psize, pbrand, pprice, pqty, image_url);
//	}

	@GetMapping("/viewoutofstock")
	public List<Product> viewOutOfStock(@RequestParam("v_id") int v_id)
	{
		return pservice.viewOutOfStock(v_id);
	}
}
