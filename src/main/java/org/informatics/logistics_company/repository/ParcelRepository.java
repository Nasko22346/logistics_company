package org.informatics.logistics_company.repository;

import org.informatics.logistics_company.dto.parcel.ParcelResponse;
import org.informatics.logistics_company.model.enums.ParcelStatus;
import org.informatics.logistics_company.model.jpa.Parcel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParcelRepository extends JpaRepository<Parcel, Long> {
    boolean existsByTrackingNumber(String trackingNumber);

    @EntityGraph(attributePaths = {
            "sendLocation", "receiverLocation",
            "senderUser", "receiverUser",
            "priceLocationTax", "priceWeightTax",
            "staff", "staff.staffUserDetails"
    })
    List<Parcel> findAllByOrderByIdDesc();

    @EntityGraph(attributePaths = {
            "sendLocation", "receiverLocation",
            "senderUser", "receiverUser",
            "priceLocationTax", "priceWeightTax",
            "staff", "staff.staffUserDetails"
    })
    Optional<Parcel> findWithAllById(Long id);

    @EntityGraph(attributePaths = {
            "sendLocation", "receiverLocation",
            "senderUser", "receiverUser",
            "priceLocationTax", "priceWeightTax",
            "staff", "staff.staffUserDetails"
    })
    List<Parcel> findByTrackingNumberContainingIgnoreCaseOrderByIdDesc(String q);

    @EntityGraph(attributePaths = {
            "sendLocation", "receiverLocation",
            "senderUser", "senderUser.loginDetails",
            "receiverUser", "receiverUser.loginDetails",
            "priceLocationTax", "priceWeightTax",
            "staff"
    })
    List<Parcel> findBySenderUser_IdOrderByIdDesc(Long senderId);

    @EntityGraph(attributePaths = {
            "sendLocation", "receiverLocation",
            "senderUser", "senderUser.loginDetails",
            "receiverUser", "receiverUser.loginDetails",
            "priceLocationTax", "priceWeightTax",
            "staff"
    })
    List<Parcel> findByReceiverUser_IdOrderByIdDesc(Long receiverId);

    @EntityGraph(attributePaths = {
            "sendLocation", "receiverLocation",
            "senderUser", "senderUser.loginDetails",
            "receiverUser", "receiverUser.loginDetails",
            "priceLocationTax", "priceWeightTax",
            "staff"
    })
    List<Parcel> findBySenderUser_IdOrReceiverUser_IdOrderByIdDesc(Long senderId, Long receiverId);

    @EntityGraph(attributePaths = {
            "sendLocation", "receiverLocation",
            "senderUser", "senderUser.loginDetails",
            "receiverUser", "receiverUser.loginDetails",
            "priceLocationTax", "priceWeightTax",
            "staff"
    })
    List<Parcel> findByStaff_StaffIdOrderByIdDesc(Long staffId);

    @EntityGraph(attributePaths = {
            "sendLocation", "receiverLocation",
            "senderUser", "senderUser.loginDetails",
            "receiverUser", "receiverUser.loginDetails",
            "priceLocationTax", "priceWeightTax",
            "staff"
    })
    List<Parcel> findByReceivedDateIsNullOrderByIdDesc();

    List<Parcel> findAllByStaffStaffId(Long staffId);

    @Query("SELECT p FROM Parcel p WHERE p.parcelStatus != :status")
    @EntityGraph(attributePaths = {
            "sendLocation", "receiverLocation",
            "senderUser", "senderUser.loginDetails",
            "receiverUser", "receiverUser.loginDetails",
            "priceLocationTax", "priceWeightTax",
            "staff"
    })
    List<Parcel> extractAllNotDeliveredParcels(ParcelStatus status);

    List<Parcel> findAllBySenderUserId(Long senderId);

    @Query("SELECT p FROM Parcel p WHERE p.receiverUser.id = :receiverId")
    List<Parcel> extractAllParcelsByReceiverID(Long receiverId);

    @Modifying
    @Query("DELETE FROM Parcel p WHERE p.id = :id")
    long deleteByIdentificationNumber(Long id);

    @Query("""
        select p from Parcel p
        left join fetch p.senderUser su
        left join fetch p.receiverUser ru
        left join fetch p.staff st
        left join fetch st.staffUserDetails sUI
        left join fetch p.sendLocation sl
        left join fetch p.receiverLocation rl
        where (:hideDelivered = false or p.parcelStatus <> org.informatics.logistics_company.model.enums.ParcelStatus.DELIVERED)
          and (
               :q is null or :q = '' or
               lower(p.trackingNumber) like lower(concat('%', :q, '%')) or
               lower(concat(coalesce(su.firstName,''),' ',coalesce(su.middleName,''),' ',coalesce(su.lastName,''))) like lower(concat('%',:q,'%')) or
               lower(concat(coalesce(ru.firstName,''),' ',coalesce(ru.middleName,''),' ',coalesce(ru.lastName,''))) like lower(concat('%',:q,'%')) or
               lower(concat(coalesce(sUI.firstName,''),' ',coalesce(sUI.middleName,''),' ',coalesce(sUI.lastName,''))) like lower(concat('%',:q,'%'))
          )
        order by p.id desc
    """)
    List<Parcel> adminSearch(@Param("q") String q, @Param("hideDelivered") boolean hideDelivered);

    @EntityGraph(attributePaths = {
            "priceWeightTax",
            "priceLocationTax",
            "priceLocationTax.location"
    })
    List<Parcel> findAllBySentDateBetween(LocalDateTime from, LocalDateTime to);

    @EntityGraph(attributePaths = {
            "priceWeightTax",
            "priceLocationTax",
            "priceLocationTax.location"
    })
    List<Parcel> findAllBySentDateBetweenAndParcelStatusNot(
            LocalDateTime from, LocalDateTime to, ParcelStatus status
    );

    @EntityGraph(attributePaths = {
            "priceWeightTax",
            "priceLocationTax",
            "priceLocationTax.location"
    })

    List<Parcel> findAllByReceivedDateBetweenAndParcelStatus(
            LocalDateTime from, LocalDateTime to, ParcelStatus status
    );
}
