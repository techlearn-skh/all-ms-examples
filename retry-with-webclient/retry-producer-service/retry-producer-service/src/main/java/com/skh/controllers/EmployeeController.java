package com.skh.controllers;

import com.skh.entities.EmployeeEntity;
import com.skh.services.EmployeeServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeServiceImpl svc;

    public EmployeeController(EmployeeServiceImpl svc) {
        this.svc = svc;
    }

    @GetMapping(path = "/dummy")
    public ResponseEntity<EmployeeEntity> dummy() {
        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setEmpId(12);
        employeeEntity.setEmpName("SKH");
        employeeEntity.setEmpDOJ(new Date());
        employeeEntity.setIsPermenentEmp(true);
        employeeEntity.setEmpSalary(1212d);
        return ResponseEntity.ok(employeeEntity);
    }

    // Create
    @PostMapping
    public ResponseEntity create(@RequestBody Employee employee) {
        System.out.println("employee : " + employee);
        EmployeeEntity created = svc.createEmployee(employee);
        System.out.println(created);
        return ResponseEntity.created(URI.create("/api/employees/" + created.getEmpId())).body(created);
    }

    // Read all
    @GetMapping
    public ResponseEntity<List<EmployeeEntity>> list() {
        return ResponseEntity.ok(svc.getAllEmployees());
    }

    // Read one
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeEntity> get(@PathVariable Integer id) {
        return svc.getEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeEntity> update(@PathVariable Integer id, @RequestBody EmployeeEntity employee) {
        if (!svc.exists(id)) {
            return ResponseEntity.notFound().build();
        }
        EmployeeEntity updated = svc.updateEmployee(id, employee);
        return ResponseEntity.ok(updated);
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!svc.exists(id)) {
            return ResponseEntity.notFound().build();
        }
        svc.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
