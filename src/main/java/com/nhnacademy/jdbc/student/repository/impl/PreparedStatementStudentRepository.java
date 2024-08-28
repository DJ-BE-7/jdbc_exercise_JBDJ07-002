package com.nhnacademy.jdbc.student.repository.impl;

import com.nhnacademy.jdbc.student.domain.Student;
import com.nhnacademy.jdbc.student.repository.StudentRepository;
import com.nhnacademy.jdbc.util.DbUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.Optional;

@Slf4j
public class PreparedStatementStudentRepository implements StudentRepository {

    @Override
    public int save(Student student){
        //todo#1 insert student
        String sql = "insert into jdbc_students(id, name, gender, age) values(?,?,?,?)";
        try(Connection connection = DbUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, student.getId());
            statement.setString(2, student.getName());
            statement.setString(3, student.getGender().toString());
            statement.setInt(4, student.getAge());
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Student> findById(String id){
        //todo#2 student 조회
        String sql = "select * from jdbc_students where id=?";
        ResultSet rs = null;
        try(Connection connection = DbUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, id);
            rs = statement.executeQuery();
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
        String sql = "update jdbc_students set name=?, gender=?, age=? where id=?";
        try(Connection connection = DbUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, student.getName());
            statement.setString(2, student.getGender().toString());
            statement.setInt(3, student.getAge());
            statement.setString(4, student.getId());

            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteById(String id){
        //todo#4 student 삭제
        String sql = "delete from jdbc_students where id=?";
        try(Connection connection = DbUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, id);
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
