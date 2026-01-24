# -- 1. LoginInfo
# INSERT INTO login_info (login_email, login_password) VALUES
# ('john.sender@example.com', 'password123'),
# ('jane.receiver@example.com', 'password456'),
# ('staff.member@papexpress.com', 'staffpass789');
#
# -- 2. UserInfo
# INSERT INTO user_info (first_name, middle_name, last_name, phone_number, login_id) VALUES
# ('John', 'Michael', 'Doe', '+359888111222', 1),
# ('Jane', 'Elizabeth', 'Smith', '+359888333444', 2),
# ('Alex', 'Robert', 'Johnson', '+359888555666', 3);
#
# -- 3. Location
# INSERT INTO location (location_country, location_province, location_region, location_description) VALUES
# ('Bulgaria', 'Sofia', 'Center', '123 Main Street, Building A'),
# ('Bulgaria', 'Plovdiv', 'Trakia', '456 Oak Avenue, Apt 5'),
# ('Bulgaria', 'Sofia', 'Lozenets', '789 Office Boulevard');
#
# -- 4. Company
# INSERT INTO companies (company_name, phone_number, email, company_eik, company_description) VALUES
# ('Papa Express Ltd', '+359888999000', 'info@papexpress.com', '123456789', 'Leading logistics company in Bulgaria');
#
# -- 5. OpenTime
# INSERT INTO open_time (work_time_day_of_week, work_time_start, work_time_end) VALUES
# (1, '08:00:00', '18:00:00'),
# (2, '08:00:00', '18:00:00'),
# (3, '08:00:00', '18:00:00');
#
# -- 6. Office
# INSERT INTO office (office_phone, office_email, company_company_id, open_time_work_time_id, location_location_id) VALUES
# ('+359888777888', 'sofia.office@papexpress.com', 1, 1, 3);
#
# -- 7. Staff
# INSERT INTO staff (office_id, position, staff_data_id) VALUES
# (1, 'COURIER', 3);
#
# -- 8. PriceLocationTax
# INSERT INTO price_location_tax (price_location_tax) VALUES
# (5.50),
# (3.00),
# (8.00);
#
# -- 9. PriceWeightTax
# INSERT INTO price_weight_tax (max_weight_amount, price_weight_tax) VALUES
# (1.0, 2.50),
# (5.0, 5.00),
# (10.0, 10.00);
#
# -- 10. Parcel
# INSERT INTO parcel (parcel_weight, parcel_price, parcel_sent_date, parcel_received_date,
#                    send_location_id, receiver_location_id, parcel_status,
#                    sender_user_id, receiver_user_id, price_location_tax_id, price_weight_id, staff_id) VALUES
# (0.5, 8.00, '2024-01-15 10:30:00', '2024-01-16 14:20:00', 1, 2, 'DELIVERED', 1, 2, 2, 1, 1),
# (3.2, 18.50, '2024-01-20 09:15:00', NULL, 1, 2, 'IN_TRANSIT', 1, 2, 1, 2, 1),
# (7.8, 25.00, '2024-01-22 11:45:00', NULL, 2, 1, 'PENDING', 2, 1, 1, 3, 1);
