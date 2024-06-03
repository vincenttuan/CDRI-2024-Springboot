package com.example.cart.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.cart.model.dto.CustomerDto;
import com.example.cart.model.dto.ProductDto;
import com.example.cart.model.po.Customer;
import com.example.cart.repository.CustomerDao;

@Service
public class CustomerService implements UserDetailsService {
	
	@Autowired
	//@Qualifier("InMemoryCustomer")
	@Qualifier("InMySQLCustomer")
	private CustomerDao customerDao;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
	private ModelMapper modelMapper;
	
	public List<CustomerDto> findAllCustomers() {
		List<Customer> customers = customerDao.findAllCustomers();
		// PO 轉 DTO
		return customers.stream()
				.map(customer -> modelMapper.map(customer, CustomerDto.class))
				.toList();
	}
	
	public CustomerDto getCustomerById(Integer id) {
		Customer customer = customerDao.getCustomerById(id);
		return modelMapper.map(customer, CustomerDto.class);
	}
	
	public CustomerDto addCustomer(CustomerDto customerDto) {
		// 密碼加密
		String encodedPassword = passwordEncoder.encode(customerDto.getPassword());
		customerDto.setPassword(encodedPassword);
		
		Customer customer = modelMapper.map(customerDto, Customer.class);
		Customer savedCustomer = customerDao.addCustomer(customer);
		return modelMapper.map(savedCustomer, CustomerDto.class);
	}
	
	public CustomerDto updateCustomer(Integer id, CustomerDto customerDto) {
		if (customerDto.getPassword() != null) {
			// 密碼加密
			String encodedPassword = passwordEncoder.encode(customerDto.getPassword());
			customerDto.setPassword(encodedPassword);
		} else {
			// 取得 pwd
			String pwd = customerDao.getCustomerById(id).getPassword();
			customerDto.setPassword(pwd);
		}
		customerDto.setId(id);
		Customer customer = modelMapper.map(customerDto, Customer.class);
		Customer updatedCustomer = customerDao.updateCustomer(customer);
		return modelMapper.map(updatedCustomer, CustomerDto.class);
	}
	
	// 更新密碼
	public Boolean updatePassword(Integer customerId, String newPassword) {
		String encodedPassword = passwordEncoder.encode(newPassword);
		return customerDao.updatePassword(customerId, encodedPassword);
	}
	
	public Boolean deleteCustomerById(Integer id) {
		return customerDao.deleteCustomerById(id);
	}
	
	public CustomerDto getCustomerByUsername(String username) {
		Customer customer = customerDao.getCustomerByUsername(username);
		return modelMapper.map(customer, CustomerDto.class);
	}
	
	@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerDao.getCustomerByUsername(username);
        if (customer == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return User.builder()
                .username(customer.getUsername())
                //.password(passwordEncoder.encode(customer.getPassword())) // 密码未加密，需要加密
                .password(customer.getPassword()) // 密码已经加密，不需要再加密
                .roles("USER") // 根据实际情况设置用户角色
                .build();
    }
	
}
