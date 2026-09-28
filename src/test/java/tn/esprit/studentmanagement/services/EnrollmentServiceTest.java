package tn.esprit.studentmanagement.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.studentmanagement.entities.Enrollment;
import tn.esprit.studentmanagement.entities.Status;
import tn.esprit.studentmanagement.repositories.EnrollmentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private Enrollment enrollment;

    @BeforeEach
    void setUp() {
        enrollment = new Enrollment();
        enrollment.setIdEnrollment(1L);
        enrollment.setEnrollmentDate(LocalDate.of(2026, 9, 15));
        enrollment.setGrade(15.5);
        enrollment.setStatus(Status.ACTIVE);
    }

    @Test
    void getAllEnrollments_retourneLaListeDuRepository() {
        when(enrollmentRepository.findAll()).thenReturn(List.of(enrollment));

        List<Enrollment> result = enrollmentService.getAllEnrollments();

        assertEquals(1, result.size());
        assertEquals(Status.ACTIVE, result.get(0).getStatus());
        verify(enrollmentRepository, times(1)).findAll();
    }

    @Test
    void getEnrollmentById_existant_retourneLInscription() {
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));

        Enrollment result = enrollmentService.getEnrollmentById(1L);

        assertEquals(15.5, result.getGrade());
    }

    @Test
    void getEnrollmentById_inexistant_leveNoSuchElementException() {
        when(enrollmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> enrollmentService.getEnrollmentById(99L));
    }

    @Test
    void saveEnrollment_delegueAuRepository() {
        when(enrollmentRepository.save(enrollment)).thenReturn(enrollment);

        Enrollment result = enrollmentService.saveEnrollment(enrollment);

        assertEquals(1L, result.getIdEnrollment());
        verify(enrollmentRepository).save(enrollment);
    }

    @Test
    void deleteEnrollment_appelleDeleteById() {
        enrollmentService.deleteEnrollment(1L);

        verify(enrollmentRepository, times(1)).deleteById(1L);
    }
}
