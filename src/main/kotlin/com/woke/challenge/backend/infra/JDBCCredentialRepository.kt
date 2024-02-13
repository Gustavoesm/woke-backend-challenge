package com.woke.challenge.backend.infra

import com.woke.challenge.backend.model.Credential
import com.woke.challenge.backend.model.CredentialRepository
import com.woke.challenge.backend.model.Password
import com.woke.challenge.backend.model.Username
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class JDBCCredentialRepository(private val jdbcTemplate: NamedParameterJdbcTemplate): CredentialRepository {

    override fun save(credential: Credential): Boolean {
        return jdbcTemplate.update(INSERT_CREDENTIAL, params(credential)) > 0
    }

    override fun exists(credential: Credential): Boolean {
        val rowCount = jdbcTemplate.queryForObject(SELECT_COUNT_CREDENTIAL, params(credential), Int::class.java)
        return rowCount == 1
    }

    override fun devIndex(): List<Credential> {
        val index = jdbcTemplate.query(SELECT_ALL, rowMapper)
        return index
    }

    private fun params(credential: Credential): MapSqlParameterSource {
        val paramSource = MapSqlParameterSource()
        paramSource.addValue("username", credential.username.value)
        paramSource.addValue("password", credential.password.value)
        return paramSource
    }

    class CredentialsMapper: RowMapper<Credential> {
        override fun mapRow(rs: ResultSet, rowNum: Int): Credential {
            return Credential(Username(rs.getString("username")), Password(rs.getString("password")))
        }
    }

    val rowMapper = CredentialsMapper()

    companion object {
        const val INSERT_CREDENTIAL = "INSERT INTO credentials (username, password) VALUES (:username, :password);"
        const val SELECT_COUNT_CREDENTIAL = "SELECT COUNT(*) FROM credentials " +
                "WHERE username = :username AND password = :password;"
        const val SELECT_ALL = "SELECT * FROM credentials;"
    }
}