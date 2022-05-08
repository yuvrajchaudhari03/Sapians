package com.service;

import java.util.List;
import java.util.stream.Collectors;

import com.entities.User;
import com.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.entities.Product;
import com.repository.ProductRepository;

@Service
public class ProductService {
	@Autowired
	ProductRepository prepo;
	@Autowired
	UserRepository userRepository;

	public List<Product> getAllProducts() {
		return prepo.findAll().stream().filter(e -> e.getPqty()>0).collect(Collectors.toList());
	}
	public List<Product> getproducts(int p_id)
	{
		return prepo.getproducts(p_id);
	}

	public Product save(Product p) {
		return prepo.save(p);
	}

	public List<Product> getByCategoryId(int c_id) {
		return prepo.getByCategoryId(c_id);
	}
	public List<Product> searchbykeyword(String pname, String pbrand, String pdesc) {
		// TODO Auto-generated method stub
		return prepo.searchbykeyword(pname, pbrand, pdesc);
		
	}
	public List<Product> getAllRaw() {
		return prepo.getAllRaw();
	}
	public List<Product> getAllStitched() {
		return prepo.getAllStitched();
	}
	public List<Product> getByVendor(Integer id) {
		return prepo.getByVid(id);
	}
	public boolean productStatusAction(int p_id,float pprice,int pqty,String action) 
	{
		// TODO Auto-generated method stub
		prepo.productAudit(p_id);
		if(action.equals("yes"))
		{
			prepo.productadd(p_id);
			//prepo.gettotalprice(p_id);
			//prepo.pdadminwallet(pprice,pqty);
			return true;
		}
		else
		{
			prepo.productdel(p_id);
			return false;
		}
		
	}
	public int vaddproduct(int c_id, String v_email, String pname, String pdesc, String psize, String pbrand, float pprice,
						   int pqty, String image_url) throws Exception {
		try{
			User user  = userRepository.findByEmailAddress(v_email);
			return prepo.vaddproduct(c_id,user.getU_id(),pname,pdesc,psize,pbrand,pprice*1.1f,pqty, image_url);
		}
		catch (Exception ex){
			throw new Exception("Adding product failed"+ ex.getMessage());
		}

	}

	public List<Product> viewOutOfStock(int v_id) {
		// TODO Auto-generated method stub
		return prepo.viewOutOfStock(v_id);
	}
}
