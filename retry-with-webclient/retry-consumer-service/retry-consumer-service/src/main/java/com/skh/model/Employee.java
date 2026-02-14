package com.skh.model;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class Employee {

	private Integer empId;
	private String empName;
	private Date empDOJ;
	private Double empSalary;
	private Boolean isPermenentEmp;

	public static String companyType = "IN";

	public Employee() {

	}

	public Employee(Integer empId, String empName, Date empDOJ, Double empSalary, Boolean isPermenentEmp) {
		super();
		this.empId = empId;
		this.empName = empName;
		this.empDOJ = empDOJ;
		this.empSalary = empSalary;
		this.isPermenentEmp = isPermenentEmp;
	}

	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public Date getEmpDOJ() {
		return empDOJ;
	}

	public void setEmpDOJ(Date empDOJ) {
		this.empDOJ = empDOJ;
	}

	public Double getEmpSalary() {
		return empSalary;
	}

	public void setEmpSalary(Double empSalary) {
		this.empSalary = empSalary;
	}

	public Boolean getIsPermenentEmp() {
		return isPermenentEmp;
	}

	public void setIsPermenentEmp(Boolean isPermenentEmp) {
		this.isPermenentEmp = isPermenentEmp;
	}

	@Override
	public int hashCode() {
		return Objects.hash(empDOJ, empId, empName, empSalary, isPermenentEmp);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		return Objects.equals(empDOJ, other.empDOJ) && Objects.equals(empId, other.empId)
				&& Objects.equals(empName, other.empName) && Objects.equals(empSalary, other.empSalary)
				&& Objects.equals(isPermenentEmp, other.isPermenentEmp);
	}

	@Override
	public String toString() {
		return "Employee [empId=" + empId + ", empName=" + empName + ", empDOJ=" + empDOJ + ", empSalary=" + empSalary
				+ ", isPermenentEmp=" + isPermenentEmp + "]" + "\n";
	}

	public List<Employee> fetchAllEmployees() {
		List<Employee> employeeList = IntStream.range(0, 5)
				.mapToObj(x -> new Employee((Math.abs(new Random().nextInt() % 100)), UUID.randomUUID().toString().substring(0, 8),
						(new Random().nextInt() % 2 == 0 ? new Date() : new Date(new Date().getTime() + 86400000)),
						Math.ceil(new Random().nextDouble() * 100000), (x % 2 == 0 ? true : false)))
				.collect(Collectors.toList());
		return employeeList;
	}

	public void displayEmployee(Employee e) {
		System.out.println(e);

	}

}
