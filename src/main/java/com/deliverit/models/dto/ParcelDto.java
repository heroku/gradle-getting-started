package com.deliverit.models.dto;

import javax.validation.constraints.Positive;

public class ParcelDto {


    public static final String WEIGHT_ERROR_MESSAGE = "Weight can't be negative.";
    public static final String CATEGORY_ID_ERROR_MESSAGE = "Category ID must be positive.";
    public static final String USER_ID_ERROR_MESSAGE = "User ID must be positive.";
    public static final String WAREHOUSE_ID_ERROR_MESSAGE = "Warehouse ID must be positive.";

    @Positive(message = WEIGHT_ERROR_MESSAGE)
    private double weight;

    @Positive(message = CATEGORY_ID_ERROR_MESSAGE)
    private int categoryId;

    @Positive(message = USER_ID_ERROR_MESSAGE)
    private int userId;

    @Positive(message = WAREHOUSE_ID_ERROR_MESSAGE)
    private int warehouseId;

    public ParcelDto() {
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }
}
