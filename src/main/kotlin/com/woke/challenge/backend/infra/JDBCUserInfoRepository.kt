package com.woke.challenge.backend.infra

import com.woke.challenge.backend.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class JDBCUserInfoRepository : UserInfoRepository {

    override fun exists(username: Username): Boolean {
        return jdbcTemplate.queryForObject(SELECT_COUNT, params(username), Int::class.java) == 1
    }

    override fun get(username: Username): UserInfo {
        return jdbcTemplate.query(SELECT, params(username), rowMapper)[0]
    }

    override fun save(username: Username, userInfo: UserInfo) {
        jdbcTemplate.update(INSERT_ON_DUPLICATE_UPDATE, params(username, userInfo))
    }

    private fun params(username: Username, userInfo: UserInfo): MapSqlParameterSource {
        val params = MapSqlParameterSource("username", username.value)
        params.addValue("firstName", userInfo.firstName.value)
        params.addValue("lastName", userInfo.lastName.value)
        params.addValue("phone", userInfo.phone.value)
        params.addValue("email", userInfo.email.value)
        params.addValue("birth", userInfo.birthDate.value)
        return params
    }

    private fun params(username: Username): MapSqlParameterSource {
        return MapSqlParameterSource("username", username.value)
    }

    class UserInfoMapper: RowMapper<UserInfo> {
        override fun mapRow(rs: ResultSet, rowNum: Int): UserInfo {
            return UserInfo(
                FirstName(rs.getString("first_name")),
                LastName(rs.getString("last_name")),
                PhoneNumber(rs.getString("phone")),
                Email(rs.getString("email")),
                BirthDate(rs.getDate("birth_date"))
            )
        }
    }

    @Autowired
    lateinit var jdbcTemplate: NamedParameterJdbcTemplate

    val rowMapper = UserInfoMapper()

    companion object {
        const val SELECT_COUNT = "SELECT COUNT(*) FROM user_info WHERE username = :username;"
        const val SELECT = "SELECT * FROM user_info WHERE username = :username"
        const val INSERT_ON_DUPLICATE_UPDATE = "INSERT INTO user_info VALUES (:username, :firstName, :lastName, " +
                ":phone, :email, :birth) ON DUPLICATE KEY UPDATE first_name = :firstName, last_name = :lastName, " +
                "phone = :phone, email = :email, birth_date = :birth;"
    }
}