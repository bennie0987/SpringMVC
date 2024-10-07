package com.app.springmvc.module.products.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.app.springmvc.module.products.model.Product;
import com.app.springmvc.module.products.services.FileUploadService;
import com.app.springmvc.module.products.services.ProductsRepository;

/**
 *
 * @author SilentNoise
 */
@Controller
@RequestMapping("/products")
public class ProductsController {
	private static final Logger logger = LoggerFactory.getLogger(ProductsController.class);

	@Autowired
	private ProductsRepository repo;

	@Autowired
	private FileUploadService fileUploadService;

	@GetMapping({ "", "/" })
	public String showProductList(Model model) {
	    List<Product> products = repo.findAll();
	    model.addAttribute("products", products);
	    model.addAttribute("fragment", "products/index :: content_fragment");
	    return "products/index";
	}

	@GetMapping("/edit/{id}")
	public String editProduct(@PathVariable("id") Long id, Model model) {
		Product product = repo.findById(id).orElse(null);
		if (product != null) {
			model.addAttribute("product", product);
			return "products/edit"; // Ensure this template exists
		} else {
			return "redirect:/products"; // Redirect if product not found
		}
	}

	@PostMapping("/update/{id}")
	public String updateProduct(@PathVariable("id") Long id, @RequestParam("imageFile") MultipartFile file,
			@ModelAttribute Product product) {
		try {
			// Find the existing product
			System.out.println("id:" + id);
			Product existingProduct = repo.findById(id)
					.orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));

			if (!file.isEmpty()) {

				// Optionally delete the old file if necessary
				if (existingProduct.getImageFileName() != null && !existingProduct.getImageFileName().isEmpty()) {
					Path oldFilePath = Paths.get("src/main/resources/static/images")
							.resolve(existingProduct.getImageFileName());
					Files.deleteIfExists(oldFilePath);
				}
				// Save the new file
				String fileName = file.getOriginalFilename();
				Path path = Paths.get("src/main/resources/static/images").resolve(fileName);
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				// Set the file name in the product
				existingProduct.setImageFileName(fileName);
			}

			// Update product details from the form
			existingProduct.setName(product.getName());
			existingProduct.setBrand(product.getBrand());
			existingProduct.setCategory(product.getCategory());
			existingProduct.setPrice(product.getPrice());

			// Save the updated product in the database
			repo.save(existingProduct);

		} catch (IOException e) {
			e.printStackTrace(); // Handle the exception as needed
		}

		return "redirect:/products";
	}

	@GetMapping("/delete/{id}")
	public String deleteProduct(@PathVariable("id") Long id) {
		Product product = repo.findById(id).orElse(null);
		if (product != null) {
			String imageFileName = product.getImageFileName();
			if (imageFileName != null && !imageFileName.isEmpty()) {
				// Define the path to the images directory
				Path pathToFile = Paths.get("src/main/resources/static/images").resolve(imageFileName);
				try {
					// Delete the file
					Files.deleteIfExists(pathToFile);
				} catch (IOException e) {
					e.printStackTrace(); // Handle the exception as needed
				}
			}

			// Delete the product from the database
			repo.deleteById(id);
		}
		return "redirect:/products"; // Redirect to the product list after deletion
	}

	@GetMapping("/add")
	public String addProductForm(Model model) {
		model.addAttribute("product", new Product());
		return "products/add";
	}

	@PostMapping("/add")
	public String addProduct(@ModelAttribute Product product, @RequestParam("imageFile") MultipartFile file) {
		try {
			// Save the file and get the filename
			String fileName = fileUploadService.saveFile(file);
			System.out.println("fileName: " + fileName);

			// Set the filename to the product
			product.setImageFileName(fileName);

			// Save the product
			product.setCreatedAt(new Date()); // Automatically set on the server side
			repo.save(product);
		} catch (IOException e) {
			logger.error("Failed to save file", e);
			// Handle the error (e.g., show an error message to the user)
		}

		return "redirect:/products";
	}
}
