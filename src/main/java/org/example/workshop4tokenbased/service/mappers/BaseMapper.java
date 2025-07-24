package org.example.workshop4tokenbased.service.mappers;

public interface BaseMapper<T,O> {
    T map(O fromWhat);
}