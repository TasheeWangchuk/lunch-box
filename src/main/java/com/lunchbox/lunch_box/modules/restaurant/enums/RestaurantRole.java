package com.lunchbox.lunch_box.modules.restaurant.enums;

/**
 * Restaurant-scoped roles stored in the `restaurant_users` table.
 * Indicates the specific role a user holds within a particular restaurant.
 *
 * These are separate from global AppRole to clearly distinguish:
 * - Who the user IS on the platform (AppRole → user_roles table)
 * - What they DO inside a restaurant (RestaurantRole → restaurant_users.role)
 */
public enum RestaurantRole {

    /** Manages a restaurant: can manage menu, view orders, and oversee staff. */
    MANAGER,

    /** A staff member at a restaurant: handles day-to-day restaurant operations. */
    STAFF
}
