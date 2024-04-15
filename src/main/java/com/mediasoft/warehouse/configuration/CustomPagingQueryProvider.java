package com.mediasoft.warehouse.configuration;

import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class CustomPagingQueryProvider implements PagingQueryProvider {

    private String selectClause;
    private String fromClause;
    private Map<String, Order> sortKeys = new HashMap<>();
    private String forUpdateClause;

    public void setSelectClause(String selectClause) {
        this.selectClause = selectClause;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    public void setSortKeys(Map<String, Order> sortKeys) {
        this.sortKeys = sortKeys;
    }

    public void setForUpdateClause(String forUpdateClause) {
        this.forUpdateClause = forUpdateClause;
    }

    @Override
    public void init(DataSource dataSource) throws Exception {

    }

    @Override
    public String generateFirstPageQuery(int pageSize) {
        Assert.notNull(selectClause, "The selectClause must not be null");
        Assert.notNull(fromClause, "The fromClause must not be null");

        StringBuilder sql = new StringBuilder(selectClause);
        sql.append(" ").append(fromClause);

        if (forUpdateClause != null) {
            sql.append(" ").append(forUpdateClause);
        }

        return sql.toString();
    }

    @Override
    public String generateRemainingPagesQuery(int pageSize) {
        return null;
    }

    @Override
    public int getParameterCount() {
        return 0;
    }

    @Override
    public boolean isUsingNamedParameters() {
        return false;
    }

    @Override
    public Map<String, Order> getSortKeys() {
        return null;
    }

    @Override
    public String getSortKeyPlaceHolder(String keyName) {
        return null;
    }

    @Override
    public Map<String, Order> getSortKeysWithoutAliases() {
        return null;
    }
}
