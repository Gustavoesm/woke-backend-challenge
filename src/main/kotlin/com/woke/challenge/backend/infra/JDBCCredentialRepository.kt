package com.woke.challenge.backend.infra

import com.woke.challenge.backend.model.Credential
import com.woke.challenge.backend.model.Password
import com.woke.challenge.backend.model.Username
import com.woke.challenge.backend.model.repositories.CredentialRepository
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class JDBCCredentialRepository(private val jdbcTemplate: NamedParameterJdbcTemplate) : CredentialRepository {

    override fun exists(username: Username): Boolean {
        return jdbcTemplate.queryForObject(SELECT_COUNT_USERNAME, params(username.value), Int::class.java) == 1
    }

    override fun save(credential: Credential): Boolean {
        return jdbcTemplate.update(INSERT_CREDENTIAL, params(credential)) > 0
    }

    override fun devIndex(): List<Credential> {
        val index = jdbcTemplate.query(SELECT_ALL, rowMapper)
        return index
    }

    override fun findByUsername(username: String): Credential {
        return jdbcTemplate.query(SELECT_USERNAME, params(username), rowMapper)[0]
    }

    private fun params(credential: Credential): MapSqlParameterSource {
        val paramSource = MapSqlParameterSource()
        paramSource.addValue("username", credential.username.value)
        paramSource.addValue("password", credential.password.value)
        return paramSource
    }

    private fun params(username: String): MapSqlParameterSource {
        return MapSqlParameterSource("username", username)
    }

    class CredentialsMapper : RowMapper<Credential> {
        override fun mapRow(rs: ResultSet, rowNum: Int): Credential {
            return Credential(Username(rs.getString("username")), Password(rs.getString("password")))
        }
    }

    val rowMapper = CredentialsMapper()

    companion object {
        const val INSERT_CREDENTIAL = "INSERT INTO credentials (username, password) VALUES (:username, :password);"
        const val SELECT_USERNAME = "SELECT * FROM credentials WHERE username = :username;"
        const val SELECT_COUNT_USERNAME = "SELECT COUNT(*) FROM credentials WHERE username = :username;"
        const val SELECT_ALL = "SELECT * FROM credentials;"
    }
}