package com.example.library_management_service.Service;

import com.example.library_management_service.DTO.BorrowDetailDTO;
import com.example.library_management_service.DTO.BorrowDetailResponseDTO;
import com.example.library_management_service.Entity.Book;
import com.example.library_management_service.Entity.BorrowDetail;
import com.example.library_management_service.Entity.User;
import com.example.library_management_service.Repository.BookRepository;
import com.example.library_management_service.Repository.BorrowDetailRepository;
import com.example.library_management_service.Repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BorrowDetailService {

    private final BorrowDetailRepository borrowDetailRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public BorrowDetailService(
            BorrowDetailRepository borrowDetailRepository,
            UserRepository userRepository,
            BookRepository bookRepository
    ) {
        this.borrowDetailRepository = borrowDetailRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public BorrowDetailDTO createBorrowDetail(BorrowDetailDTO borrowDetailDTO) {
        BorrowDetail borrowDetail = new BorrowDetail();

        // Gán User
        if (borrowDetailDTO.getUserId() != null) {
            User user = userRepository.findById(borrowDetailDTO.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            borrowDetail.setUser(user);
        }

        // Gán Book
        if (borrowDetailDTO.getBookId() != null) {
            Book book = bookRepository.findById(borrowDetailDTO.getBookId())
                    .orElseThrow(() -> new IllegalArgumentException("Book not found"));

            if (book.getQuantity() <= 0) {
                throw new IllegalStateException("Run out of book");
            }
            // 👉 Trừ số lượng đi 1
            book.setQuantity(book.getQuantity() -1);
            bookRepository.save(book);
            borrowDetail.setBook(book);
        }

        // Gán thông tin liên hệ
        mapContactInfo(borrowDetailDTO, borrowDetail);
        borrowDetail.setDate_borrow_book(LocalDateTime.now());
        BorrowDetail saved = borrowDetailRepository.save(borrowDetail);
        return convertToDTO(saved);
    }
    public List<BorrowDetailResponseDTO> getBorrowDetailByID(Long userId) {
        // Kiểm tra user tồn tại
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Lấy danh sách BorrowDetail theo userId
        List<BorrowDetail> borrowDetails = borrowDetailRepository.findByUserId(userId);

        // Convert sang ResponseDTO
        return borrowDetails.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<BorrowDetailResponseDTO> getAllBorrowDetails() {

        List<BorrowDetail> borrowDetails = borrowDetailRepository.findAll();

        // Convert sang ResponseDTO
        return borrowDetails.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    public BorrowDetailResponseDTO approveBorrowDetail(Long borrowDetailId) {
        BorrowDetail borrowDetail = borrowDetailRepository.findById(borrowDetailId)
                .orElseThrow(() -> new IllegalArgumentException("BorrowDetail not found"));

        if (borrowDetail.getStatus() != BorrowDetail.BorrowStatus.PENDING) {
            throw new IllegalStateException("Cannot approve, status is not PENDING");
        }

        borrowDetail.setStatus(BorrowDetail.BorrowStatus.BORROWED); // Chuyển trạng thái
        borrowDetailRepository.save(borrowDetail);

        return convertToResponseDTO(borrowDetail);
    }

    public BorrowDetailResponseDTO returnBorrowDetail(Long borrowDetailId) {
        BorrowDetail borrowDetail = borrowDetailRepository.findById(borrowDetailId)
                .orElseThrow(() -> new IllegalArgumentException("BorrowDetail not found"));

        if (borrowDetail.getStatus() != BorrowDetail.BorrowStatus.BORROWED) {
            throw new IllegalStateException("Cannot return, status is not BORROWED");
        }

        borrowDetail.setStatus(BorrowDetail.BorrowStatus.RETURNED); // Chuyển trạng thái
        borrowDetail.setDate_return_book(LocalDateTime.now());
        borrowDetailRepository.save(borrowDetail);

        return convertToResponseDTO(borrowDetail);
    }

    public boolean deleteBorrowDetail(Long id) {
        if (!borrowDetailRepository.existsById(id)) {
            return false;
        }
        borrowDetailRepository.deleteById(id);
        return true;
    }

    private void mapContactInfo(BorrowDetailDTO dto, BorrowDetail entity) {
        entity.setFullName(dto.getFullName());
        entity.setContact(dto.getContact());
        entity.setEmail(dto.getEmail());
        entity.setAddress(dto.getAddress());
    }

    private BorrowDetailDTO convertToDTO(BorrowDetail borrowDetail) {
        BorrowDetailDTO dto = new BorrowDetailDTO();
        dto.setId(borrowDetail.getId());
        dto.setUserId(borrowDetail.getUser() != null ? borrowDetail.getUser().getId() : null);
        dto.setBookId(borrowDetail.getBook() != null ? borrowDetail.getBook().getId() : null);
        dto.setFullName(borrowDetail.getFullName());
        dto.setContact(borrowDetail.getContact());
        dto.setEmail(borrowDetail.getEmail());
        dto.setAddress(borrowDetail.getAddress());
        return dto;
    }
    private BorrowDetailResponseDTO convertToResponseDTO(BorrowDetail borrowDetail) {
        BorrowDetailResponseDTO dto = new BorrowDetailResponseDTO();

        // User
        if (borrowDetail.getUser() != null) {
            dto.setUserName(borrowDetail.getUser().getUsername());
            dto.setUserEmail(borrowDetail.getUser().getUserDetail().getEmail());
            dto.setUserAddress(borrowDetail.getUser().getUserDetail().getAddress());
        }

        // Book
        if (borrowDetail.getBook() != null) {
            dto.setBookTitle(borrowDetail.getBook().getBookName());
            dto.setBookAuthor(borrowDetail.getBook().getAuthor());
        }

        dto.setStatus(borrowDetail.getStatus().name());
        dto.setDateBorrowBook(borrowDetail.getDate_borrow_book());
        dto.setDateReturnBook(borrowDetail.getDate_return_book());
        dto.setFullName(borrowDetail.getFullName());
        dto.setContact(borrowDetail.getContact());
        dto.setAddress(borrowDetail.getAddress());
        dto.setEmail(borrowDetail.getEmail());
        dto.setBookImage(borrowDetail.getBook().getBookImage());
        dto.setId(borrowDetail.getId());
        dto.setDateBorrowBook(borrowDetail.getDate_borrow_book());
        return dto;
    }

}
