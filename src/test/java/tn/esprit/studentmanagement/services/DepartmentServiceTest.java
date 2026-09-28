package tn.esprit.studentmanagement.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.studentmanagement.entities.Department;
import tn.esprit.studentmanagement.repositories.DepartmentRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    private Department department;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setIdDepartment(1L);
        department.setName("Informatique");
        department.setLocation("Bloc A");
        department.setHead("Mme Trabelsi");
    }

    @Test
    void getAllDepartments_retourneLaListeDuRepository() {
        when(departmentRepository.findAll()).thenReturn(List.of(department));

        List<Department> result = departmentService.getAllDepartments();

        assertEquals(1, result.size());
        assertEquals("Informatique", result.get(0).getName());
        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    void getDepartmentById_existant_retourneLeDepartement() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        Department result = departmentService.getDepartmentById(1L);

        assertEquals("Bloc A", result.getLocation());
    }

    @Test
    void getDepartmentById_inexistant_leveNoSuchElementException() {
        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());

        // Le service appelle Optional.get() : un id inconnu provoque une exception
        assertThrows(NoSuchElementException.class,
                () -> departmentService.getDepartmentById(99L));
    }

    @Test
    void saveDepartment_delegueAuRepository() {
        when(departmentRepository.save(department)).thenReturn(department);

        Department result = departmentService.saveDepartment(department);

        assertEquals(1L, result.getIdDepartment());
        verify(departmentRepository).save(department);
    }

    @Test
    void deleteDepartment_appelleDeleteById() {
        departmentService.deleteDepartment(1L);

        verify(departmentRepository, times(1)).deleteById(1L);
    }
}
