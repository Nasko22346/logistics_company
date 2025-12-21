package org.informatics.logistics_company.repository;

import org.informatics.logistics_company.model.enums.ParcelStatus;
import org.informatics.logistics_company.model.jpa.Parcel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParcelRepository extends JpaRepository<Parcel, Long> {

    // 5d. Всички пратки, които са регистрирани от даден служител
    List<Parcel> findAllByStaffStaffId(Long staffId);

    // 5e. Всички пратки, които са изпратени, но не да получени
    @Query("SELECT p FROM Parcel p WHERE p.parcelStatus != :status")
    List<Parcel> extractAllNotDeliveredParcels(ParcelStatus status);


    // 5f. Всички пратки, които са изпратени от даден клиент
    List<Parcel> findAllBySenderUserId(Long senderId);

    // 5g. Всички пратки, които са получени от даден клиент
    @Query("SELECT p FROM Parcel p WHERE p.receiverUser.id = :receiverId")
    List<Parcel> extractAllParcelsByReceiverID(Long receiverId);

    @Modifying
    @Query("DELETE FROM Parcel p WHERE p.id = :id")
    long deleteByIdentificationNumber(Long id);
}
