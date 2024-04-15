package com.changeset.cleanup.DAO;

import com.changeset.cleanup.Model.Changeset;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ImpChangesetDAO implements  ChangesetDAO{



    @Override
    public List<Changeset> getChangsetsByDate(Timestamp from, Timestamp to){



    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Changeset> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Changeset> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Changeset> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Changeset getOne(Long aLong) {
        return null;
    }

    @Override
    public Changeset getById(Long aLong) {
        return null;
    }

    @Override
    public Changeset getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Changeset> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Changeset> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Changeset> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Changeset> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Changeset> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Changeset> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Changeset, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Changeset> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Changeset> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Changeset> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<Changeset> findAll() {
        return null;
    }

    @Override
    public List<Changeset> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Changeset entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Changeset> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Changeset> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Changeset> findAll(Pageable pageable) {
        return null;
    }
}
