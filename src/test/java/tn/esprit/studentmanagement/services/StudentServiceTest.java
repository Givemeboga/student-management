package tn.esprit.studentmanagement.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.studentmanagement.entities.Student;
import tn.esprit.studentmanagement.repositories.StudentRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setIdStudent(1L);
        student.setFirstName("Amira");
        student.setLastName("Ben Salah");
        student.setEmail("amira.bensalah@esprit.tn");
    }

    @Test
    void getAllStudents_retourneLaListeDuRepository() {
        when(studentRepository.findAll()).thenReturn(List.of(student));

        List<Student> result = studentService.getAllStudents();

        assertEquals(1, result.size());
        assertEquals("Mauvais", result.get(0).getFirstName());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void getAllStudents_sansEtudiant_retourneListeVide() {
        when(studentRepository.findAll()).thenReturn(List.of());

        List<Student> result = studentService.getAllStudents();

        assertTrue(result.isEmpty());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void getStudentById_existant_retourneLEtudiant() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Student result = studentService.getStudentById(1L);

        assertNotNull(result);
        assertEquals("amira.bensalah@esprit.tn", result.getEmail());
    }

    @Test
    void getStudentById_inexistant_retourneNull() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertNull(studentService.getStudentById(99L));
    }

    @Test
    void saveStudent_delegueAuRepository() {
        when(studentRepository.save(student)).thenReturn(student);

        Student result = studentService.saveStudent(student);

        assertEquals(1L, result.getIdStudent());
        verify(studentRepository).save(student);
    }

    @Test
    void deleteStudent_appelleDeleteById() {
        studentService.deleteStudent(1L);

        verify(studentRepository, times(1)).deleteById(1L);
    }
}
