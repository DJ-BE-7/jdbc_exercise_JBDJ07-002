package com.nhnacademy.jdbc.student.repository.impl;

import com.mysql.cj.protocol.x.ContinuousInputStream;
import com.nhnacademy.jdbc.student.domain.Student;
import com.nhnacademy.jdbc.student.repository.StudentRepository;
import com.nhnacademy.jdbc.util.DbUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

@Slf4j
public class StatementStudentRepository implements StudentRepository {

    @Override
    public int save(Student student){
        //todo#1 insert student
        String sql = String.format("insert into jdbc_students(id, name, gender, age) values('%s','%s','%s','%s')",
                student.getId(),
                student.getName(),
                student.getGender().toString(),
                student.getAge()
                );
        try(Connection connection = DbUtils.getConnection();
            Statement statement = connection.createStatement();
        ) {
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Student> findById(String id){
        //todo#2 student 조회
        String sql = String.format("select * from jdbc_students where id='%s'", id);
        ResultSet rs = null;
        try(Connection connection = DbUtils.getConnection();
            Statement statement = connection.createStatement();
        ) {
            rs = statement.executeQuery(sql);
            if (rs.next()) {
                Student student = new Student(
                        rs.getString("id"),
                        rs.getString("name"),
                        Student.GENDER.valueOf(rs.getString("gender")),
                        rs.getInt("age"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
                return Optional.of(student);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public int update(Student student){
        //todo#3 student 수정, name <- 수정합니다.
        String sql = String.format("update jdbc_students set name='%s', gender='%s', age='%s' where id='%s'",
                student.getName(),
                student.getGender().toString(),
                student.getAge(),
                student.getId()
                );
        try(Connection connection = DbUtils.getConnection();
            Statement statement = connection.createStatement();
        ) {
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteById(String id){
       //todo#4 student 삭제
        String sql = String.format("delete from jdbc_students where id='%s'", id);
        try(Connection connection = DbUtils.getConnection();
            Statement statement = connection.createStatement()
        ) {
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
