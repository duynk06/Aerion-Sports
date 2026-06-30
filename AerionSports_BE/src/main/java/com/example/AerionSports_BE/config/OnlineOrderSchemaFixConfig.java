package com.example.AerionSports_BE.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class OnlineOrderSchemaFixConfig {

    private final JdbcTemplate jdbcTemplate;

    @Bean
    public CommandLineRunner relaxLichSuHoaDonNhanVienColumn() {
        return args -> {
            try {
                Integer nullableFlag = jdbcTemplate.queryForObject(
                        """
                                SELECT CASE WHEN c.is_nullable = 1 THEN 1 ELSE 0 END
                                FROM sys.columns c
                                INNER JOIN sys.tables t ON c.object_id = t.object_id
                                INNER JOIN sys.schemas s ON t.schema_id = s.schema_id
                                WHERE s.name = 'dbo'
                                  AND t.name = 'lich_su_hoa_don'
                                  AND c.name = 'id_nhan_vien'
                                """,
                        Integer.class
                );

                if (nullableFlag != null && nullableFlag == 0) {
                    jdbcTemplate.execute("ALTER TABLE dbo.lich_su_hoa_don ALTER COLUMN id_nhan_vien INT NULL");
                    log.info("Adjusted dbo.lich_su_hoa_don.id_nhan_vien to allow NULL for online orders.");
                }
            } catch (Exception ex) {
                log.warn("Could not auto-adjust dbo.lich_su_hoa_don.id_nhan_vien to NULL. Online checkout will still rely on the current schema.", ex);
            }
        };
    }
}
