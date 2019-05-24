package io.wax911.sample.core.data.mapper.show

import androidx.paging.PagingRequestHelper
import io.wax911.sample.core.dao.DatabaseHelper
import io.wax911.sample.core.model.container.Aniticipated
import io.wax911.sample.core.model.show.Show
import io.wax911.support.core.data.mapper.SupportDataMapper
import org.koin.core.inject
import retrofit2.Response

class AnticipatedShowMapper(
    pagingRequestHelper: PagingRequestHelper.Request.Callback
) : SupportDataMapper<List<Aniticipated<Show>>, List<Show>>(pagingRequestHelper) {

    override val database: DatabaseHelper by inject()

    /**
     * Created mapped objects and handles the database operations which may be required to map various objects,
     * called in [retrofit2.Callback.onResponse] after assuring that the response was a success
     * @see [responseCallback]
     *
     * @param response retrofit response containing data
     * @return Mapped object that will be consumed by [onResponseDatabaseInsert]
     */
    override suspend fun onResponseMapFrom(response: Response<List<Aniticipated<Show>>?>): List<Show> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Inserts the given object into the implemented room database,
     * called in [retrofit2.Callback.onResponse]
     * @see [responseCallback]
     *
     * @param mappedData mapped object from [onResponseMapFrom] to insert into the database
     */
    override suspend fun onResponseDatabaseInsert(mappedData: List<Show>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}