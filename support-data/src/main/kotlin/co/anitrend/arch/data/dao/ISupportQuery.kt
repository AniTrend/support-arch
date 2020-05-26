package co.anitrend.arch.data.dao

/**
 * A generalized building block for [androidx.room.Dao] that provides basic
 * read, write and update functionality
 *
 * @since v0.9.X
 */
@Deprecated(
    "Enforces the room persistence layer to be used, consider using your own impl"
)
interface ISupportQuery<T> {

    /**
     * Inserts a new item into the database ignoring items with the same primary key,
     * for both insert or update behavior use: [upsert]
     *
     * @param attribute item/s to insert
     */
    //@Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(attribute: T)

    /**
     * Inserts new items into the database ignoring items with the same primary key,
     * for both insert or update behavior use: [upsert]
     * 
     * @param attribute item/s to insert
     */
    //@Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(attribute: List<T>)

    /**
     * Updates an item in the underlying database
     *
     * @param attribute item/s to update
     */
    //@Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(attribute: T)

    /**
     * Updates a list of items in the underlying database
     *
     * @param attribute item/s to update
     */
    //@Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(attribute: List<T>)

    /**
     * Deletes an item from the underlying database
     *
     * @param attribute item/s to delete
     */
    //@Delete
    suspend fun delete(attribute: T)

    /**
     * Deletes a list of items from the underlying database
     *
     * @param attribute item/s to delete
     */
    //@Delete
    suspend fun delete(attribute: List<T>)

    /**
     * Inserts or updates matching attributes on conflict
     *
     * @param attribute item/s to insert
     */
    //@Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(attribute: T)

    /**
     * Inserts or updates matching attributes on conflict
     *
     * @param attribute item/s to insert
     */
    //@Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(attribute: List<T>)
}
